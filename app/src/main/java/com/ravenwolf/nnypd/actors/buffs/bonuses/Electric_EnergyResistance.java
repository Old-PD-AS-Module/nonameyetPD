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
package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Electric_EnergyResistance extends Bonus {

    @Override
    public int icon() {
        return BuffIndicator.ENERGY_RESISIT;
    }

/*
    @Override
    public String toString() {
        return "Improved resistance";
    }

    @Override
    public String statusMessage() { return "improved resistance"; }

    @Override
    public String playerMessage() { return "You feel sparkling"; }


    @Override
    public String description() {
        return "You emit an electromagnetic disrupting aura, increasing your resistance against electric and energy damage.";
    }
*/

    @Override
    public String toString() {
        return "增加抗性";
    }

    @Override
    public String statusMessage() { return "增加抗性"; }

    @Override
    public String playerMessage() { return "你感觉很奇怪"; }


    @Override
    public String description() {
        return "你身上散发着一种混乱的电场，从而增加了加你对雷电能量的抗性";
    }

}