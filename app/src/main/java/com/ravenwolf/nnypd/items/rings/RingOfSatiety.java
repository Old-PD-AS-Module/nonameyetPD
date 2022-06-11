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

public class RingOfSatiety extends Ring {

	{
        name = "饱食之戒";
        shortName = "Sa";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Satiety();
	}
	
	@Override
	public String desc() {
        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) /4);
        }

        StringBuilder desc = new StringBuilder(
                "饱食之戒能够优化佩戴者代谢机能，使他们能够在长时间不进食的情况下活动更久，并提高各种食物的增益效果"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指会降低_" + mainEffect + "%的饱腹度消耗率_" +
                        "并且提高饱腹buff下_" + sideEffect + "%_的意志，感知和潜行属性。"
        );

        return desc.toString();

	}
	
	public class Satiety extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉胃中充满了一股舒适的暖流" :
                    "你感觉自己饿的更快了" ;
        }
	}
}
