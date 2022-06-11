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
package com.ravenwolf.nnypd.actors.buffs.debuffs;

import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;


public class Revealed extends Debuff {


    @Override
//    public String toString() {
//        return "Revealed";
//    }
    public String toString() {
        return "标记";
    }

    @Override
//    public String statusMessage() { return "revealed"; }
    public String statusMessage() { return "标记"; }

    @Override
//    public String playerMessage() { return "You are revealed!"; }
    public String playerMessage() { return "你被标记了"; }

    @Override
    public int icon() {
        return BuffIndicator.REVEALED;
    }

    @Override
//    public String description() {
//        return "Revealed ";
//    }
    public String description() {
        return "显形";
    }

}
