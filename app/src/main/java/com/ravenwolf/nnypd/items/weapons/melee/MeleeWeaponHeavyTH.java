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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.armours.shields.Shield;

public abstract class MeleeWeaponHeavyTH extends MeleeWeapon {

    protected int[][] weapAtk() {
        return new int[][]{	{2, 3, 4, 0 },	//frame
                {-2, 2, 5, 2 },	//x
                {0, 0, 0, 0}};
    }
    protected int[][] weapRun() {
        return new int[][]{	{1, 1, 1, 0, 0, 0  },	//frame
                {2, 3, 5, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
    }
    public MeleeWeaponHeavyTH(int tier) {

        super( tier );

    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " heavy two-handed weapon_. It can be used with wands and throwing weapons, " +
//                "but its strength requirement will increase if paired with shield or another melee weapon.";
        return "重型双手";
    }

    public float getBackstabMod() {
        return 0.10f;
    }

    @Override
    public int penaltyBase() {
        return super.penaltyBase()+2;
    }

    @Override
    public int min( int bonus ) {
        return super.min(bonus) +1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) +tier-1;
    }

    @Override
    public int strShown( boolean identified ) {
        /*return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap1 && incompatibleWith( Dungeon.hero.belongings.weap2 ) ?
                        Dungeon.hero.belongings.weap2.str(
                                Dungeon.hero.belongings.weap2.isIdentified() ?
                                        Dungeon.hero.belongings.weap2.bonus : 0
                        )/2 : 0 );*/

        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap1 && incompatibleWith( Dungeon.hero.belongings.weap2 ) ?
                        tier : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return /*item instanceof MeleeWeapon ||*/ item instanceof Shield ; }
/*
    @Override
    public int penaltyBase() {
        return super.penaltyBase()+ 4;
    }
*/

}
