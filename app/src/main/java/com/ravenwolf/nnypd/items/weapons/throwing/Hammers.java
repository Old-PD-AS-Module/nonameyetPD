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
package com.ravenwolf.nnypd.items.weapons.throwing;


import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Hammers extends ThrowingWeaponHeavy {

	{
		name = "投掷锤";
		image = ItemSpriteSheet.THROWING_HAMMER;
		critical=new BluntCritical(this,true, 2.5f);
	}

	public Hammers() {
		this( 1 );
	}

	public Hammers(int number) {
        super( 3 );
		quantity = number;
	}
	
	@Override
	public String desc() {
		return
				"这些沉重的锤子是用来扔向敌人的。"
						+"\n\n这种武器更擅长于暴击敌人，并且会造成额外的效果。";
	}
}
