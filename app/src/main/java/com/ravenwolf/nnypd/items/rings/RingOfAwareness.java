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

public class RingOfAwareness extends Ring {

	{
        name = "感知之戒";
        shortName = "Aw";
	}
	
//	@Override
//	public boolean doEquip( Hero hero ) {
//		if (super.doEquip( hero )) {
//			Dungeon.hero.search( false );
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	@Override
	protected RingBuff buff( ) {
		return new Awareness();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        //String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) / 2 );
            //sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * effect( bonus ) /2);
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指可以增强佩戴者的感知能力，使其感官更加敏锐，并可在弹反时造成更致命的伤害"
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将提高你_" + mainEffect + "%_的感知能力和弹反伤害加成"
        );

        return desc.toString();

	}
	
	public class Awareness extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "你感觉自己变得更加敏锐了" :
                    "你感觉自己的感知能力变差了" ;
        }
	}
}
