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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.mobs.npcs.AmbitiousImp;
import com.ravenwolf.nnypd.items.food.OverpricedRation;
import com.ravenwolf.nnypd.visuals.sprites.MonkSprite;

public class DwarfMonk extends MobEvasive {

//    public static final String TXT_DISARMED = "Monk's attack has knocked your %s out of your hands!";

    public static boolean swarmer = true;

    public DwarfMonk() {

        super( 13 );

        name = "矮人武僧";

        spriteClass = MonkSprite.class;

		loot = new OverpricedRation();
		lootChance = 0.1f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
	}

    @Override
    public float attackSpeed() {
        return super.attackSpeed() * 2.0f;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		AmbitiousImp.Quest.process( this );
		super.die( cause, dmg );

	}
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if (!blocked && isAlive())
            Buff.affect(this, Combo.class).hit();
		
		return damage;
	}

/*
    @Override
    public boolean add( Buff buff ) {
        if (buff instanceof Exposed) {
            spend(1/attackSpeed());
        }
        return super.add( buff );
    }
*/
    @Override
    public int damageRoll() {

        int dmg = super.damageRoll();

        Combo buff = buff( Combo.class );

        if( buff != null ) {

            dmg += (int) (dmg * buff.modifier());

        }

        return dmg;
    }
	
	@Override
	public String description() {
        return
                "这些僧侣皆是狂热的信徒，倾其所能从一切异族手中保护他们城市的秘密。他们既不披甲亦不执械，仅仅依靠他们的徒手格斗技巧退敌。";
    }
}
