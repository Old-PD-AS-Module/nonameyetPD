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
package com.ravenwolf.nnypd.items.armours.glyphs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Retribution extends Armour.Glyph {
	
	@Override
	public Glowing glowing() {
		return RED;
	}
/*
    @Override
    public Class<? extends Element> resistance() {
        return Element.Body.class;
    }
*/
/*    @Override
    protected String name_p() {
        return "%s of retribution";
    }

    @Override
    protected String name_n() {
        return "%s of mercy";
    }

    @Override
    protected String desc_p() {
        return "reflect melee damage back to attacker";
    }

    @Override
    protected String desc_n() {
        return "heal your attacker on hit";
    }*/
    @Override
    protected String name_p() {
        return "复仇%s";
    }

    @Override
    protected String name_n() {
        return "仁慈%s";
    }

    @Override
    protected String desc_p() {
        return "将受到的近战伤害反弹给攻击者";
    }

    @Override
    protected String desc_n() {
        return "治疗击中你的敌人";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            int reflected=damage/4;

            reflected+=reflected*2*(1-defender.HP/(float)defender.HT);
            if (isShield) reflected=(int)(damage*1.5f);

            if (reflected>0) {
                attacker.damage(attacker.absorb(reflected), this, Element.PHYSICAL);
                attacker.sprite.burst(0x660022, (int) Math.sqrt(reflected / 2) + 1);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {

        if ( !attacker.isMagical() ) {

            int effValue = Math.min( Random.IntRange(damage / 4, damage / 3), attacker.HT - attacker.HP );

            if( effValue > defender.HP ) {
                effValue = defender.HP;
            }

            if ( effValue > 0 ) {

                attacker.HP += effValue;
                attacker.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(effValue));
                defender.sprite.burst(0x660022, (int) Math.sqrt(effValue / 2) + 1);

                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
