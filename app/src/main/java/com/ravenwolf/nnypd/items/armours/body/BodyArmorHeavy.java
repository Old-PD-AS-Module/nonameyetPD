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

package com.ravenwolf.nnypd.items.armours.body;

import com.ravenwolf.nnypd.items.armours.glyphs.Durability;

public abstract class BodyArmorHeavy extends BodyArmor {

    public BodyArmorHeavy(int tier) {
        super( tier );
    }

    @Override
    public int dr( int bonus ) {
        return super.dr(bonus) +2//+ state +tier/*- 1*/
                + bonus
                //+ ( glyph instanceof Durability || bonus >= 0 ? bonus : 0 )
                + ( glyph instanceof Durability & isCursedKnown()? !isCursed()? 1 : -1 : 0 ) ;
                //+ ( glyph instanceof Durability && bonus >= 0 ? 1 : 0 ) ;
    }

    @Override
    public int minDr( int bonus ) {
        return super.minDr(bonus)+2;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }


    @Override
    public int penaltyBase() {
        return super.penaltyBase()+ tier * 4 + 4;
    }

    @Override
//    public String descType() {
//        return "heavy";
//    }
    public String descType() {
        return "重型";
    }

}
