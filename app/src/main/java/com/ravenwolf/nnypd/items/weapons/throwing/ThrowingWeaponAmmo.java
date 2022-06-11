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
package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.Item;
import com.watabou.utils.Random;

public abstract class ThrowingWeaponAmmo extends ThrowingWeapon {

    public ThrowingWeaponAmmo(int tier) {
        super( tier );
    }

    @Override
    public String descType() {
        return "弹药";
    }

    @Override
    public int min( int bonus ) {
        return tier - 1;
    }

    @Override
    public int max( int bonus ) {
        return tier + 1;
    }

    @Override
    public int str( int bonus ) {
        return 8;
    }

    @Override
    public int penaltyBase() {
        return 0;
    }

    @Override
    public int baseAmount() {
        return 20 ;
    }


    @Override
    public Item random() {

        quantity = baseAmount();

        quantity += Random.Int( quantity + 1 );

        quantity = quantity * ( 4 + Dungeon.chapter() - lootChapter() ) / 4;

        quantity = Math.max( 2, quantity );

        return this;
    }


    @Override
    public int price() {
        return quantity * ( tier + 1 );
    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder( desc() );

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append("你正随身携带着这些" + name + "。");

        } else if( Dungeon.hero.belongings.backpack.contains(this) ) {

            info.append( "这些" + name + "正在在背包里。" );

        } else {

            info.append( "这些" + name + "正在地面上。" );

        }

        return info.toString();
    }


}
