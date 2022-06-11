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
package com.ravenwolf.nnypd.items.armours.shields;

import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.ShieldSprite;


public class GreatShield extends Shield {

	{
//		name = "great shield";
		name = "巨盾";
		image = ItemSpriteSheet.SHIELD_GREAT;
		drawId= ShieldSprite.SHIELD_GREAT;
	}

	public GreatShield() { super( 4 ); }


	@Override
	public int dr( int bonus ) {
		return super.dr(bonus)-bonus-1;
	}

	public int guardTurns(){
		return super.guardTurns()+1;
	}

	@Override
/*	public String desc() {
		return "This great shield is kite heavy but proves a strong defense." +
				"\n\n This shield provides an additional blocking instance.";
	}*/
	public String desc() {
		return "这种盾牌虽然笨重，但是可以提供不错防御。它可以让你获得额外一回合的格挡状态";
	}

}
