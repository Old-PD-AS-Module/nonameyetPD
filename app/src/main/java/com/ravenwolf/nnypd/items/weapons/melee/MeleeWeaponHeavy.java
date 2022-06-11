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


public abstract class MeleeWeaponHeavy extends MeleeWeapon {

    public MeleeWeaponHeavy(int tier) {
        super( tier );
    }

    @Override
    public String descType() {
        return "重型";
    }

    @Override
    public int min( int bonus ) {
        return super.min(bonus) +2;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 4;
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus)+1;
    }

    public float getBackstabMod() {
        return 0.10f;
    }

    public int dmgMod() {
        return super.dmgMod()+1;
    }

    @Override
    public int penaltyBase() {
        return 10; //25%
    }



}
