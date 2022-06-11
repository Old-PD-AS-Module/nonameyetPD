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

public class Kopesh extends MeleeWeaponMedium {
//	khopsh
	{
		name = "克赫帕什镰形刀";
		image = ItemSpriteSheet.KOPESH;
		drawId= WeaponSprite.KOPESH;
		critical=new BladeCritical(this,false, 1.5f);
	}

	public Kopesh() {
		super( 4 );
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.75f;
	}

	@Override
	public int penaltyBase() {
		return super.penaltyBase()+2;
	}

	@Override
	public String desc() {
		return "这把沉重的刀刃末端有一条凸出的曲线。"
				+"\n\n  这种武器非常适合弹反，且更容易暴击";
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}
}
