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

public class RingOfAccuracy extends Ring {

	{
        name = "精准之戒";
        shortName = "Ac";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Accuracy();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
       // String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect(bonus) / 2 );
            //sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
                "传说这枚戒指中寄宿着无数被历史遗忘的猎手与战士的灵魂，带上它或许能够提升佩戴者的各类远程和近战武器的命中"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将会提高你_" + mainEffect + "%_的命中和连击奖励"
        );

        return desc.toString();
	}
	
	public class Accuracy extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉自己的战斗技巧得到了提升。" :
                    "你感觉自己的战斗技巧变得生疏了起来。" ;
        }
	}
}
