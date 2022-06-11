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
package com.ravenwolf.nnypd.items.armours.body;

import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;


public class SplintArmor extends BodyArmorHeavy {

	{
//		name = "splint armor";
		name = "锁子甲";
		image = ItemSpriteSheet.ARMOR_SPLINT;
        appearance = 7;
	}

	public int getHauntedIndex(){
		return 6;
	}
	
	public SplintArmor() {
		super( 2 );
	}
	
	@Override
/*	public String desc() {
		return 
			"The metal stripes sewn together to create a flexible, yet effective armor.";
	}*/
	public String desc() {
		return
				"长条的金属片被一条条固定在一起，形成了这套灵活且有效的护甲。";
	}
}
