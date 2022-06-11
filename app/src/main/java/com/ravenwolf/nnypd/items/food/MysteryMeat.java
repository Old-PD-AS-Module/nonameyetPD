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
package com.ravenwolf.nnypd.items.food;

import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;


public class MysteryMeat extends Food {

/*	{
		name = "raw meat";
		image = ItemSpriteSheet.MEAT;
		energy = Satiety.MAXIMUM * 0.20f;
		message = "That food tasted... strange.";
	}


	@Override
	public String desc() {
		return "Eating this is better than starving, but it is still better be cooked.";
	}*/
	{
		name = "生肉";
		image = ItemSpriteSheet.MEAT;
		energy = Satiety.MAXIMUM * 0.20f;
		message = "这个吃起来..有点怪";
	}


	@Override
	public String desc() {
		return "吃这个总比挨饿好，但还是煮熟了吃比较好。";
	}
	
	public int price() {
		return 5 * quantity;
	}
}
