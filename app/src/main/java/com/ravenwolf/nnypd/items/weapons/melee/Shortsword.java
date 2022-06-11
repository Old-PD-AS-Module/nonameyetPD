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

import com.ravenwolf.nnypd.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;

public class Shortsword extends MeleeWeaponMedium {

	{
		name = "短剑";
		image = ItemSpriteSheet.GLADIUS;
		drawId= WeaponSprite.GLADIUS;
		critical=new BladeCritical(this);
	}
	
	public Shortsword() {
		super( 1 );
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)+1;
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.45f;
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}
	
	@Override
	public String desc() {
		return
				"它看上去确实很短，只比匕首长出几厘米。"
						+"\n\n这种武器更适合用来弹反敌人";
	}
}
