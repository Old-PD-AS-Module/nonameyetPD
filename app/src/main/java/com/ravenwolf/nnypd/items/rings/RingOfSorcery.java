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
package com.ravenwolf.nnypd.items.rings;

import java.util.Locale;

public class RingOfSorcery extends Ring {

	{
        name = "奥术之戒";
        shortName = "So";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Sorcery();
	}

    @Override
    public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ));
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指会增加佩戴者的魔能属性，这枚戒指通常被各种法师使用，因为它会直接提升佩戴者对于法杖和魔咒的控制"
        );

        desc.append( "\n\n" );
        desc.append( super.desc() );
        desc.append( " " );

        desc.append(
                "佩戴这枚戒指会提升你_" + mainEffect + "%_的魔能属性，" +
                        "并且会增加武器和护甲_" + sideEffect + "%_的附魔触发率"
        );

        return desc.toString();
    }


    public class Sorcery extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉自己对魔法的运用越发熟练" :
                    "你感觉自己对魔法的运用越发生疏" ;
        }
    }
}
