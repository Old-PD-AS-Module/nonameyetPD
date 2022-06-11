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

import com.ravenwolf.nnypd.actors.buffs.BuffReactive;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Focused extends BuffReactive {

    @Override
    public int icon() {
        return BuffIndicator.FOCUSED;
    }

    @Override
    public String toString() {
        return "专注";
    }

//    @Override
//    public String statusMessage() { return "focused"; }
/*
    @Override
    public boolean attachTo( Char target ) {

        //Buff.detach( target, Combo.class);
        //Buff.detach( target, Guard.class );

        return super.attachTo( target );
    }
*/
    @Override
/*    public String description() {
        return "You spent a turn to take aim at present enemies. Your next attack will " +
                "be 50% more precise than usual.";
    }*/
    public String description() {
        return "你消耗了一回合为瞄准做准备。下一次攻击会提高50%的精准度";
    }



}
