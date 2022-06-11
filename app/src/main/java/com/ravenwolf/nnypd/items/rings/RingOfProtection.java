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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;

import java.util.HashSet;
import java.util.Locale;

public class RingOfProtection extends Ring {

	{
        name = "保护之戒";
        shortName = "Pr";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Protection();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", Dungeon.hero.STR * effect( bonus )/2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus )/2 );
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指能够强化佩戴者的体格，引导身体的力量化为坚实的护甲防御。同时增强佩戴者对各类法术与元素的抗性。"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指会提高你_" + mainEffect + "_点的护甲等级" +
                        "并且提高_ " + sideEffect + "%的元素抗性_。"
        );

        return desc.toString();
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Flame.class);
        RESISTS.add(Element.Shock.class);
        RESISTS.add(Element.Acid.class);
        RESISTS.add(Element.Frost.class);
        RESISTS.add(Element.Energy.class);
        RESISTS.add(Element.Unholy.class);
    }
	
	public class Protection extends RingBuff {
		
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉自己变得更加结实了" :
                    "你感觉自己变得更加脆弱了" ;
        }
	}
}
