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

public class Wonders extends Armour.Glyph {

/*    @Override
    protected String name_p() {
        return "%s of wonders";
    }

    @Override
    protected String name_n() {
        return "%s of chaos";
    }

    @Override
    protected String desc_p() {
        return "do random stuff to your enemies on hit";
    }

    @Override
    protected String desc_n() {
        return "do random stuff to you on hit";
    }*/
    @Override
    protected String name_p() {
        return "紊乱%s";
    }

    @Override
    protected String name_n() {
        return "混沌%s";
    }

    @Override
    protected String desc_p() {
        return "被命中时对攻击者造成随机效果";
    }

    @Override
    protected String desc_n() {
        return "被命中时对自身造成随机效果";
    }

    @Override
    public boolean proc( Armour armor, Char attacker, Char defender, int damage ) {
        return random().proc( armor, attacker, defender, damage );
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        return true;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        return true;
    }

}
