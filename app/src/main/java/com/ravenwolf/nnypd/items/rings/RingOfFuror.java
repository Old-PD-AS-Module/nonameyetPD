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

import java.util.Locale;

public class RingOfFuror extends Ring {

	{
        name = "狂怒之戒";
        shortName = "Fu";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Furor();
	}
	
	@Override
	public String desc() {
        String mainEffect = "??";
        String currentEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 150 * effect( bonus ) );
            currentEffect = String.format( Locale.getDefault(), "%.0f", 150 * effect( bonus )*(1-(float) Dungeon.hero.HP/Dungeon.hero.HT) );
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指会提升使用者的怒火，使其能够更快速的进行攻击" +
                        "使用者的血量越低，速度就越快，对满血状态的使用者没有任何加成"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指最多可以使你的攻击速度提升_" + mainEffect + "%_。" +
                        "根据的你血量，当前会使你的攻击速度提升_" + currentEffect + "%_。"
        );

        return desc.toString();
	}
	
	public class Furor extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉浑身充满了怒火" :
                    "突然间你感觉自己变得麻木了，一切都是那么的索然无味" ;
        }
	}
}
