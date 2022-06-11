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

public class RingOfShadows extends Ring {

	{
        name = "暗影之戒";
        shortName = "Sh";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Shadows();
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
                "这枚戒指上奇特的魔法控制着环绕在佩戴者周围的暗影，帮助他们更有效的隐藏在各种境之下。对于那些涉及到暗杀和潜行工作的人这是必不可少的物品"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "这枚戒指会提高你_" + mainEffect + "%_潜行属性" +
                        "并且提高_" + sideEffect + "%_的伏击伤害"
        );

        return desc.toString();
	}
	public class Shadows extends RingBuff {

        @Override
        public String desc() {
            return !isCursed() ?
                    "突然间你感觉到有一股暗影围绕在你的周围，使你更难被发现" :
                    "突然间你感觉周边的阴影围绕着你，更加突出了你的存在。" ;
        }
	}
}
