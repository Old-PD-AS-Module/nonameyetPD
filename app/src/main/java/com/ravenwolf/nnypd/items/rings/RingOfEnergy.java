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

public class RingOfEnergy extends Ring {
	
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
		return new Energy();
	}
	
	@Override
	public String desc() {
		return isTypeKnown() ?
				( isCursed() && isIdentified() ? "通常这枚戒指" : "这枚戒指" ) +
						"会提高佩戴者的意志力，这枚戒指通常被一些法师所使用，因为意志力越强法杖类的恢复速度就越快，" +
						"顺便还可以让玩家更容易摆脱各种精神类debuff的影响" +
						( isCursed() && isIdentified() ? "然而，这枚诅咒的戒指会造成相反的效果" : "" ) :
				super.desc();
	}

	public class Energy extends RingBuff {
		@Override
		public String desc() {
			return !isCursed() ?
					"你感觉现在充满了决心" :
					"你感觉现在很难集中注意力" ;
		}
	}
}
