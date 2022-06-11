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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.Lightning;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class StormWard extends Armour.Glyph {

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Shock.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of storm ward";
    }

    @Override
    protected String name_n() {
        return "%s of potential";
    }

    @Override
    protected String desc_p() {
        return "shock your enemies on hit and give you lightning resistance";
    }

    @Override
    protected String desc_n() {
        return "shock you on hit";
    }*/
    @Override
    protected String name_p() {
        return "静电%s";
    }

    @Override
    protected String name_n() {
        return "电势%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时电击敌人，并获得雷电抗性";
    }

    @Override
    protected String desc_n() {
        return "被击中时会电击自身";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            points[0] = defender.pos;
            nPoints = 1;
            affected.clear();
            affected.add(defender);
            if (isShield)
                hit(attacker, Random.IntRange(damage / 3, damage / 2));
            else
                hit(attacker, Random.IntRange(damage / 4, damage / 3));

            attacker.sprite.parent.add(new Lightning(points, nPoints, null));

            return true;
        }
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        defender.damage(Random.IntRange(damage / 4, damage / 3), this, Element.SHOCK);
        return true;
    }

    private void hit( Char ch, int damage ) {

        if (damage < 1) {
            return;
        }

        affected.add(ch);

        ch.damage(damage, this, Element.SHOCK);

        points[nPoints++] = ch.pos;

        HashSet<Char> ns = new HashSet<Char>();
        for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
            Char n = Actor.findChar( ch.pos + Level.NEIGHBOURS8[i] );
            if (n != null && !affected.contains( n )) {
                ns.add( n );
            }
        }

        if (ns.size() > 0) {
            hit( Random.element( ns ), Random.IntRange( damage / 2, damage ) );
        }
    }
}
