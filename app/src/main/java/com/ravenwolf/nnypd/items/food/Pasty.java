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

public class Pasty extends Food {

/*	{
		name = "dwarven pasty";
		image = ItemSpriteSheet.PASTY;

		energy = Satiety.MAXIMUM;
		message = "That food tasted delicious!";
	}

	@Override
	public String desc() {
		return "This is an authentic dwarven pasty with traditional filling of beer and sand. " +
				"Food like these can last for years without spoiling.";
	}*/
	{
		name = "肉馅饼";
		image = ItemSpriteSheet.PASTY;

		energy = Satiety.MAXIMUM;
		message = "吃起来很不错!";
	}

	@Override
	public String desc() {
		return "这是块正宗的矮人馅饼，内含土豆和牛肉的传统食物，非常美味！";
	}

	@Override
	public int price() {
		return 40 * quantity;
	}
}
