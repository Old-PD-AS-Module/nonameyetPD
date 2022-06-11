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

import com.ravenwolf.nnypd.Element;

import java.util.HashSet;
import java.util.Locale;

public class RingOfConcentration extends Ring {
	
	{
        name = "意志之戒";
        shortName = "Co";
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Mind.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Concentration();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指可以增强佩戴者的意志力，并且间接地增加了他们的魔能属性。顺便还可以让玩家更容易摆脱各种精神类debuff的影响"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指会使增强你_" + mainEffect + "%_的意志力，并缩短鉴定物品的时间。" +
                        "而且会增加你对精神debuff_" + sideEffect + "%_的抗性"
        );

        return desc.toString();

	}

    public class Concentration extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉现在充满了决心" :
                    "你感觉现在很难集中注意力" ;
        }
    }
}
