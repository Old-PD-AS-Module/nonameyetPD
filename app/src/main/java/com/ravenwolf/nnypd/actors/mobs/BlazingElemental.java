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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.FellFire;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.BlazingElementalSprite;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;

public class BlazingElemental extends MobPrecise {

    private static String TXT_ENSNARED = "元素轻易挣脱了缠绕！";

    public BlazingElemental() {

        super( 16 );

        name = "魔焰元素";
        spriteClass = BlazingElementalSprite.class;

        flying = true;
        armorClass = 0;

        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Flame.class, Element.Resist.IMMUNE );
        resistances.put( Element.Frost.class, Element.Resist.IMMUNE );

    }

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isEthereal() {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.ENERGY;
    }


    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy );
    }


    @Override
    public void damage( int dmg, Object src, Element type ) {

        if ( type == Element.FLAME || type == Element.FROST ) {

            if (HP < HT) {
                int reg = Math.min( dmg / 2, HT - HP );

                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                }
            }

        } else {

            super.damage(dmg, src, type);

        }
    }
	
	@Override
	public boolean add( Buff buff ) {
        if (buff instanceof Ensnared) {
            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_ENSNARED );
            }
            return false;
        } else {
            return super.add( buff );
        }
	}


    @Override
    public int defenseProc( Char enemy, int damage,  boolean blocked ) {

        if (Level.adjacent(enemy.pos, pos)) {
            BuffActive.addFromDamage(enemy, FellFire.class,damage/2);
        }
        return super.defenseProc(enemy,damage,blocked);
    }

	@Override
	public String description() {
        return
                "这种元素是由多种魔法能量组成的，它会造成一种无法通过常规手段熄灭的魔法火焰。近距离面对这些魔法元素并不是一个好的想法，因为在近距离攻击它时这种火焰同样会溅射到攻击者身上。这些元素本质太过混乱了，即使是强大的恶魔术士也不能将其完全控制。";

    }

}
