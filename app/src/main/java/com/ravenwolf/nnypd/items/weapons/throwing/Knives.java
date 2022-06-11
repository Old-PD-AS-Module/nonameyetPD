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

import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Knives extends ThrowingWeaponLight {

	{
		name = "飞刀";
		image = ItemSpriteSheet.THROWING_KNIFE;
		critical=new PierceCritical(this,false, 2f);
	}
	
	public Knives() {
		this( 1 );
	}
	
	public Knives(int number) {
        super( 1 );
		quantity = number;
	}

	@Override
	public float getBackstabMod() {
		return 0.30f;
	}

	@Override
	public String desc() {
		return
				"这些轻巧的金属刀片只需手腕轻抖就会精准地飞向目标"
						+"\n\n这件武器在对付未察觉你的敌人时候更为有效。";
	}
	

}
