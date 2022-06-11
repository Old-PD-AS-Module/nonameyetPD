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

public class FlameWard extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return ORANGE;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Flame.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of flame ward";
    }

    @Override
    protected String name_n() {
        return "%s of combustion";
    }

    @Override
    protected String desc_p() {
        return "burn your enemies on hit and decrease damage from fire";
    }

    @Override
    protected String desc_n() {
        return "burn you on hit";
    }*/
    @Override
    protected String name_p() {
        return "烈火%s";
    }

    @Override
    protected String name_n() {
        return "易燃%s";
    }

    @Override
    protected String desc_p() {
        return "燃烧击中你的敌人，并减少所受火焰属性伤害";
    }

    @Override
    protected String desc_n() {
        return "被击中时点燃自己";
    }

	@Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            if (isShield)
                attacker.damage(Random.IntRange(damage / 3, damage / 2), this, Element.FLAME);
            else
                attacker.damage(Random.IntRange(damage / 4, damage / 3), this, Element.FLAME);
            return true;
        }

        return false;
	}

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {

            defender.damage(Random.IntRange(damage / 4, damage / 3), this, Element.FLAME);
            return true;

    }
}
