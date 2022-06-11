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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.AdditionalSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.RangedWeaponSprite;

public class LongBow extends RangedWeaponMissile {


	public int getAdditionalDrawId(){
		if ( Dungeon.hero.belongings.ranged instanceof Arrows)
			return AdditionalSprite.ADD_QUIVER;
		else
			return super.getAdditionalDrawId();
	}


	{
		name = "长弓";
		image = ItemSpriteSheet.LONG_BOW;
		drawId= RangedWeaponSprite.LONG_BOW;
		critical=new PierceCritical(this,true,1);
	}

	public LongBow() {
		super( 2 );
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

		return "长弓是由一整块木头雕刻而成的，这种弓适合在远处拿来攻击敌人"
				+"\n\n这件武器会造成更强大的暴击效果。";
	}
}
