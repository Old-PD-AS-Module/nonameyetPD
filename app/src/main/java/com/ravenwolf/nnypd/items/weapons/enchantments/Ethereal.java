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
package com.ravenwolf.nnypd.items.weapons.enchantments;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Ethereal extends Weapon.Enchantment {

    @Override
    public Glowing glowing() {
        return CYAN;
    }

    @Override
    protected String name_p() {
        return "轻盈之%s";
    }

    @Override
    protected String name_n() {
        return "钝重之%s";
    }

    @Override
    protected String desc_p() {
        return "重量更轻并减少武器惩罚，并且攻击概率击穿护甲";
    }

    @Override
    protected String desc_n() {
        return "增加武器的重量";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }

}
