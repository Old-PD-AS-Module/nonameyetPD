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
package com.ravenwolf.nnypd.items.quest;

import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class CorpseDust extends Item {
	
	{
		name = "尸尘";
		image = ItemSpriteSheet.DUST;
		
//		cursed = true;
//		cursedKnown = true;
//
		unique = true;
	}
	
	@Override
	public String info() {
		return
				"从表面上看，这团尸尘和普通灰尘差不多。但是你的直觉告诉你尽快脱手为好。";
	}
}
