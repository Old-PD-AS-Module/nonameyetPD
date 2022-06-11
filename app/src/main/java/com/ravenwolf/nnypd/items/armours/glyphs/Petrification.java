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

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Petrification extends Armour.Glyph {

	
	@Override
	public Glowing glowing() {
		return GRAY;
	}

/*    @Override
    protected String name_p() {
        return "%s of petrification";
    }

    @Override
    protected String name_n() {
        return "%s of paralyzation";
    }

    @Override
    protected String desc_p() {
        return "temporarily turn the attacker into stone";
    }

    @Override
    protected String desc_n() {
        return "temporarily turn the wearer into stone";
    }*/
    @Override
    protected String name_p() {
        return "石化%s";
    }

    @Override
    protected String name_n() {
        return "僵持%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时会石化击中你的敌人";
    }

    @Override
    protected String desc_n() {
        return "被击中时会石化自己";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
	    //has less chance to proc for ranged enemies
        if (!Level.adjacent(attacker.pos, defender.pos) && Random.Int(2)==0)
            return false;

        if (!attacker.isEthereal()) {
            //FIXME
            if(attacker instanceof Hero)
                damage/=2;
            if (isShield)
                BuffActive.addFromDamage(attacker, Petrificated.class, damage*2);
            else
                BuffActive.addFromDamage(attacker, Petrificated.class, damage);

            return true;
        }else
            return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        BuffActive.addFromDamage( defender, Petrificated.class, damage/2 );
        return true;
    }

}
