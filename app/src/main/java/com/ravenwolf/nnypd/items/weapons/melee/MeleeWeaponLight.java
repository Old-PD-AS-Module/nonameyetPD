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

public abstract class MeleeWeaponLight extends MeleeWeapon {

    private static final String TXT_EQUIP_TITLE = "装备副手武器";
    private static final String TXT_EQUIP_MESSAGE =
            "这是一个轻型单手武器，你可以把它装备到副手武器。" +
                    "你想把它当做主手武器还是副手武器？";

    private static final String TXT_EQUIP_PRIMARY = "主手武器";
    private static final String TXT_EQUIP_SECONDARY = "副手武器";

    public MeleeWeaponLight(int tier) {
        super(tier);
    }

    @Override
    public String descType() {
        return "轻型";
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus)-1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) - 2;
    }


    @Override
    public int guardStrength(int bonus){
        return (super.guardStrength(bonus)-1);
    }

    @Override
    public int penaltyBase() {
        return 0;
    }

    public float getBackstabMod() {
        return 0.30f;
    }

}
