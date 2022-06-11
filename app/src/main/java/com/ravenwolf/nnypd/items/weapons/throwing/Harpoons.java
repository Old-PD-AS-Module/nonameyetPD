/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2019 Considered Hamster
 *
 * No Name Yet Pixel Dungeon
 * Copyright (C) 2018-2019 RavenWolf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.effects.Pushing;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Harpoons extends ThrowingWeaponSpecial/*ThrowingWeaponHeavy*/ {

	{
        name = "鱼叉";
		image = ItemSpriteSheet.HARPOON;
        //critical=new PierceCritical();
	}

	public Harpoons() {
		this( 1 );
	}

	public Harpoons(int number) {
        super( 4 );
		quantity = number;
	}

    @Override
    public int baseAmount() {
        return 2;
    }

    @Override
    public int penaltyBase() {
        return 6;
    }

    @Override
    public int image() {
        return ItemSpriteSheet.HARPOON;
    }

    @Override
    public int imageAlt() {
        return ItemSpriteSheet.HARPOON_THROWN;
    }
/*
    @Override
    public boolean returnOnHit(Char enemy){
        return  !enemy.isHeavy();
    }
*/

    public boolean stick(Char enemy){
        return true;
    }

    @Override
    protected boolean returnOnMiss(){
        return true;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        damage=super.proc(attacker, defender, damage);

        if( !Level.adjacent( attacker.pos, defender.pos ) ) {
            if( !defender.immovable() && !defender.isHeavy()) {

                int distance = Math.max( 0, (attacker.STR() - defender.STR())/2 + 1 );

                int newPos = Ballistica.trace[Math.max( 1, Ballistica.distance - distance - 1 )];
                Actor.addDelayed(new Pushing(defender, defender.pos, newPos), -1);

                Actor.freeCell(defender.pos);
                defender.pos = newPos;
                Actor.occupyCell(defender);
                Dungeon.level.press( newPos, defender );
                //FIXME workaround to drop harpoon after pushing for ethereal enemies
                if (defender.isEthereal()){
                    PinCushion pinCushion =defender.buff(PinCushion.class);
                    if (pinCushion != null)
                        pinCushion.detach();
                }

//                defender.delay( 1f );
            }
        }
        return damage;
    }
	
	@Override
	public String desc() {
        return
                "鱼叉的重量主要来源于其尾部衔接的链条。这种罕见的投掷武器可以将目标拉扯到自己身旁。若目标无法被拉扯，则命中的鱼叉会掉落在地上。";
    }
}
