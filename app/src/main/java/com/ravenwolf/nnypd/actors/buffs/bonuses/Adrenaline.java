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
import com.watabou.noosa.Image;

public class Adrenaline extends Bonus {

/*    @Override
    public String toString() {
        return "Adrenaline";
    }

    @Override
    public String statusMessage() { return "Adrenaline"; }

    @Override
    public String playerMessage() { return "You feel a surge of physical power"; }*/

    @Override
    public String toString() {
        return "肾上腺素";
    }

    @Override
    public String statusMessage() { return "肾上腺素"; }

    @Override
    public String playerMessage() { return "你感觉身体里涌出一股力量！"; }

    @Override
    public int icon() {
        return BuffIndicator.ADRENALINE;
    }

    @Override
    public void tintIcon(Image icon) {
       icon.hardlight(0f, 0f, 0.9f);
    }

/*    @Override
    public String description() {
        return "A surge of physical power, adrenaline enhance both attack and movement speed.\n" +
                "Adrenaline allows its target to run at 1.5x speed, and attack at 1.5x speed.";
    }*/
    @Override
    public String description() {
        return "你感觉身体里涌出一股力量，肾上腺素提高了你的攻击和移动速度。.\n" +
                "肾上腺素可以使你的移动速度和攻击速度提升1.5倍。";
    }



}