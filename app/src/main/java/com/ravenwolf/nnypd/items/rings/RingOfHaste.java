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

public class RingOfHaste extends Ring {

	{
		name = "急速之戒";
        shortName = "Ha";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Haste();
	}
	
	@Override
	public String desc() {
		return isTypeKnown() ?
				( isCursed() ? "通常，佩戴这枚戒指" : "佩戴这枚戒指" ) +
						"会提升佩戴着的移动速度，使其能够更快的行动." +
						( isCursed() ? "然而，这枚诅咒的戒指会造成相反的效果" : "" ) :
				super.desc();
	}
	
	public class Haste extends RingBuff {
        @Override
        public String desc() {
			return !isCursed() ?
					"你感觉周围的一切都开始变慢了" :
					"你感觉周围的一切都开始变快了" ;
		}
	}
}
