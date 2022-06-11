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

package com.ravenwolf.nnypd.items.weapons.melee;

public abstract class MeleeWeaponMediumPolearms extends MeleeWeaponMedium {


    protected int[][] weapRun() {
        return new int[][]{	{1, 1, 1, 1, 2, 2  },	//frame
                {3, 4, 4, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
    }
    protected int[][] weapAtk() {
        return new int[][]{	{1, 4, 3, 0 },	//frame
                {3, 4, 6, 4 },	//x
                {0, -3, 0, 0}};
    }
    protected int[][] weapStab() {
        return new int[][]{	{2, 2, 3, 0 },	//frame
                {3, 8, 8, 4 },	//x
                {0, -2, -2, 0}};
    }

    public MeleeWeaponMediumPolearms(int tier) {
        super( tier);
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) -2;
    }

    @Override
    public int penaltyBase() {
        return super.penaltyBase()+2;
    }

}
