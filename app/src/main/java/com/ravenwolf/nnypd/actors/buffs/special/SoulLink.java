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
package com.ravenwolf.nnypd.actors.buffs.special;


import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SoulLink extends Buff {


    @Override
//    public String toString() {
//        return "Soul Link";
//    }
    public String toString() {
        return "灵魂链接";
    }

/*    @Override
    public String statusMessage() { return "soul link"; }

    @Override
    public String playerMessage() { return "You are soul linked!"; }*/
    @Override
    public String statusMessage() { return "灵魂链接"; }

    @Override
    public String playerMessage() { return "你处于灵魂链接状态!"; }

    @Override
    public int icon() {
        return BuffIndicator.SOUL_LINK;
    }


    @Override
//    public String description() {
//        return "Soul link ";
//    }
    public String description() {
        return "灵魂链接 ";
    }

    public int object = 0;

    private static final String OBJECT	= "object";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( OBJECT, object );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        object = bundle.getInt( OBJECT );
    }


    public int redirectDamage( int damage,  Object src, Element type ) {
        Actor target = Actor.findById(object);
        if (target instanceof Char){
            Char c = (Char) target;
            if (c.isAlive()){
                int redirectDmg = damage/2;
                c.damage(redirectDmg, src, type);
                return damage - redirectDmg;
            }

        }
        //if no valid object
        //detach();
        return damage;
    }
}
