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
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Tenacity extends Armour.Glyph {

	@Override
	public Glowing glowing() {
		return MUSTARD;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Mind.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of tenacity";
    }

    @Override
    protected String name_n() {
        return "%s of vulnerability";
    }

    @Override
    protected String desc_p() {
        return "increase your protection when on low health and give you greater mental resilience";
    }

    @Override
    protected String desc_n() {
        return "decrease your protection when on low health";
    }*/
    @Override
    protected String name_p() {
        return "强韧%s";
    }

    @Override
    protected String name_n() {
        return "脆弱%s";
    }

    @Override
    protected String desc_p() {
        return "低生命值时获得额外防护能力，并提高精神属性抗性";
    }

    @Override
    protected String desc_n() {
        return "低生命值时受到更多伤害";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        return false;
    }
}
