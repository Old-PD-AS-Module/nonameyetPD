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

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Blazing extends Weapon.Enchantment {

    @Override
    public Glowing glowing() {
        return ORANGE;
    }

    @Override
    protected String name_p() {
        return "烈焰%s";
    }

    @Override
    protected String name_n() {
        return "灼烧%s";
    }

    @Override
    protected String desc_p() {
        return "攻击会点燃敌人";
    }

    @Override
    protected String desc_n() {
        return "攻击会点燃自己";
    }

	@Override
	protected boolean proc_p( Char attacker, Char defender, int damage ) {
            defender.damage(Random.IntRange(damage / 3, damage / 2), this, Element.FLAME);
            return true;
	}

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
            attacker.damage(Random.IntRange(damage / 4, damage / 3), this, Element.FLAME);
            return true;
    }
}
