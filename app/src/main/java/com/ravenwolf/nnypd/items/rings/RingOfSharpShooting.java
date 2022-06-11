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

public class RingOfSharpShooting extends Ring {

	{
		name = "神射之戒";
        shortName = "Ss";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new SharpShooting();
	}
	
	@Override
	public String desc() {
		String mainEffect = "??";
		String sideEffect = "??";

		if( isIdentified() ){
			mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect(bonus) );
			sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) /2 );
		}

		StringBuilder desc = new StringBuilder(
				"这枚戒指会增强佩戴者的瞄准能力，使其在使用远程武器时能造成更多的伤害和精度"
		);

		desc.append( "\n\n" );

		desc.append( super.desc() );

		desc.append( " " );

		desc.append(
				"佩戴这枚戒指会提升_" + mainEffect + "%_的投武伤害，以及_" + sideEffect + "%_的远程武器伤害" +
						"同时还会减少_" + mainEffect + "%_的射程惩罚"
		);

		return desc.toString();

	}
	
	public class SharpShooting extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉你的远程攻击更加精准了." :
                    "你感觉你的远程攻击精度下降了." ;
        }
	}
}
