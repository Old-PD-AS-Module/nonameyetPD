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

public class RingOfFortune extends Ring {

	{
        name = "财富之戒";
        shortName = "Fo";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Fortune();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) /2);
        }

        StringBuilder desc = new StringBuilder(
                "在佩戴这枚戒指时，多数人都会认为它没有任何作用，" +
                        "但是长期佩戴后你会就发现自己的运气增加了很多并会默默获得比原先更多的财富"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指时会增加_" + mainEffect + "%_的金币获取和物品掉落 "+
                        "同时踩中陷阱时，陷阱会有_" + sideEffect + "%_的概率不会触发"

        );

        return desc.toString();
	}
	
	public class Fortune extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "装备这枚戒指后你并没能感受到任何特殊效果，这该算是件好事吗" :
                    "装备这枚戒指后你并没能感受到任何特殊效果，这该算是件坏事吗" ;
        }
	}
}
