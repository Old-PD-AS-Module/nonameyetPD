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
package com.ravenwolf.nnypd.items.weapons.melee;

import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;

public class Dagger extends MeleeWeaponLight {
	
	{
		name = "匕首";
		image = ItemSpriteSheet.DAGGER;
		drawId= WeaponSprite.DAGGER;
		critical=new PierceCritical(this);
	}

	protected int[][] weapRun() {
        return new int[][]{	{0, 0, 4, 4, 1, 1  },	//frame
                {2, 3, 4, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}

	public Dagger() {
		super( 1 );
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)+1;
	}

	@Override
	public float getBackstabMod(){
		return 0.75f;
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}

	@Override
	public String desc() {
		/*return "A simple iron dagger with a well worn wooden handle. It is ideal for backstabbing, "+
				"dealing heavy damage to unsuspecting enemies.";*/
		return "一个破旧的简易铁质匕首"
				+"\n\n这种武器在对付未察觉你的敌人时更为有效";
	}
}
