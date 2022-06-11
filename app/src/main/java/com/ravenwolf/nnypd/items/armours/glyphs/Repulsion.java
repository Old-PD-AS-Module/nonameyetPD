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
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Repulsion extends Armour.Glyph {
	
	@Override
	public Glowing glowing() {
		return MUSTARD;
	}

/*    @Override
    protected String name_p() {
        return "%s of repulsion";
    }

    @Override
    protected String name_n() {
        return "%s of bounce";
    }

    @Override
    protected String desc_p() {
        return "rebounds force against attackers, sending them flying back.";
    }

    @Override
    protected String desc_n() {
        return "increase force of the attackers, sending you flying back.";
    }*/
    @Override
    protected String name_p() {
        return "反斥%s";
    }

    @Override
    protected String name_n() {
        return "反弹%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时，会弹飞攻击者";
    }

    @Override
    protected String desc_n() {
        return "被击中时，会弹飞自己";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        //push enemy
        if (Level.adjacent(attacker.pos, defender.pos)) {
            attacker.sprite.emitter().burst( Speck.factory(Speck.BLAST), 1 );
            //shields push an additional tile
            if (isShield)
                attacker.knockBack(defender.pos, damage*3,3);
            else
                attacker.knockBack(defender.pos, damage*3,2);
            return true;
        }
        return false;
    }


    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {

        //push defender
        if (Level.adjacent(attacker.pos, defender.pos)) {
            defender.sprite.emitter().burst( Speck.factory(Speck.BLAST), 1 );
            defender.knockBack(attacker, damage*3/2);
            return true;
        }
        return false;
    }




}
