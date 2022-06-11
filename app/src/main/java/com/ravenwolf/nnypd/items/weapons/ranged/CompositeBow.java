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

public class CompositeBow extends RangedWeaponMissile {

	//weapon animations
	protected int[][] meleeAtk() {
        return new int[][]{	{1, 1, 0, 0 },	//frame
                {4, 4, 3, 3 },	//x
                {-1, -1, 0, 0}};
	}

	protected int[][] weapRun() {
		return new int[][]{	{0, 0, 0, 0, 1, 1  },	//frame
				{3, 3, 3, 3, 4, 4  },	//x
				{0, 0, 1, 0, -1, -2 }};
	}

	public int getAdditionalDrawId(){
		if ( Dungeon.hero.belongings.ranged instanceof Arrows)
			return AdditionalSprite.ADD_QUIVER;
		else
			return super.getAdditionalDrawId();
	}

	{
		name = "复合弓";
		image = ItemSpriteSheet.COMPOSITE_BOW;
		drawId= RangedWeaponSprite.COMPOSITE_BOW;
		critical=new PierceCritical(this,false,1.5f);
	}

	public CompositeBow() {
		super( 4 );
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
		return "只有熟练弓箭手才能完全掌握这把强大的弓！"
				+"\n\n这种武器更擅长于暴击敌人。";
	}
}
