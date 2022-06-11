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

public class RingOfVitality extends Ring {
	
	{
        name = "活力之戒";
        shortName = "Vi";
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Body.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Vitality();
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
                "戴上这么戒指可以提高佩戴者的恢复机制，强化所有来源的治疗效果，并提高其对中毒，虚弱等负面效果的抗性"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指会增加_" + mainEffect + "%_的治疗效果(包括自然恢复)" +
                        "并且提高_" + sideEffect + "%_的负面物理抗性"
        );

        return desc.toString();
	}
	
	public class Vitality extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉自己血液充满了活力" :
                    "不适的感觉传遍了你的全身" ;
        }
	}
}
