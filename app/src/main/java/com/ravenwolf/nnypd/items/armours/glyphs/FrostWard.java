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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class FrostWard extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return BLUE;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Frost.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of frost ward";
    }

    @Override
    protected String name_n() {
        return "%s of glaciers";
    }

    @Override
    protected String desc_p() {
        return "freezes your enemies on hit and make you resistant to frost";
    }

    @Override
    protected String desc_n() {
        return "freeze you on hit";
    }*/
    @Override
    protected String name_p() {
        return "严冰%s";
    }

    @Override
    protected String name_n() {
        return "霜冻%s";
    }

    @Override
    protected String desc_p() {
        return "冻伤击中你的敌人，并获得寒冰属性抗性";
    }

    @Override
    protected String desc_n() {
        return "被击中时冻伤自身";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            if (isShield)
                BuffActive.addFromDamage( attacker, Chilled.class, damage*3/2 );
            else
                BuffActive.addFromDamage( attacker, Chilled.class, damage );
            return true;
        }
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        BuffActive.addFromDamage( defender, Chilled.class, damage );
        return true;
    }
}
