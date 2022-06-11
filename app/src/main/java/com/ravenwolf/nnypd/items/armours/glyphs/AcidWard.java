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
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class AcidWard extends Armour.Glyph {

	@Override
	public Glowing glowing() {
		return GREEN;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Acid.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of acid ward";
    }

    @Override
    protected String name_n() {
        return "%s of corrosion";
    }

    @Override
    protected String desc_p() {
        return "cover your enemies in caustic ooze on hit and decrease damage from acids";
    }

    @Override
    protected String desc_n() {
        return "cover you in caustic ooze you on hit";
    }*/
    @Override
    protected String name_p() {
        return "蚀护%s";
    }

    @Override
    protected String name_n() {
        return "腐蚀%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时使敌人身上附着腐蚀淤泥，同时获得酸蚀属性抗性";
    }

    @Override
    protected String desc_n() {
        return "被命中时自己会被腐蚀淤泥覆盖";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            if ( isShield )
                attacker.damage(Random.IntRange(damage / 3, damage / 2), this, Element.ACID);
            else
                attacker.damage(Random.IntRange(damage / 4, damage / 3), this, Element.ACID);
            return true;
        }

        return false;

    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {

        defender.damage(Random.IntRange(damage / 4, damage / 3), this, Element.ACID);
        return true;

    }
}
