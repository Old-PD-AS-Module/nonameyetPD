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
package com.ravenwolf.nnypd.items.weapons.ranged;


import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.RangedWeaponSprite;

public class Arbalest extends RangedWeaponCrossbow /*RangedWeaponMissile */{

	{
		name = "劲弩";
		//image = ItemSpriteSheet.ARBALEST;
		image = ItemSpriteSheet.HEAVY_CROSSBOW;
		drawId= RangedWeaponSprite.HEAVY_CROSSBOW;
		critical=new PierceCritical(this, true,1.5f);
	}

	public Arbalest() {
		super( 5 );
	}

	@Override
	public boolean incompatibleWithShield() {
		return true;
	}

	@Override
	public Type weaponType() {
		return Type.R_MISSILE;
	}
	
	@Override
	public String desc() {
		return "这件诡秘的器械允许使用者以荒谬的力量射出弩箭！"
				+"\n\n这种武器更擅长于暴击敌人，并且会造成额外的效果。";
	}
}
