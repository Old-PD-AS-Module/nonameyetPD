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

import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;


public class LongStaff extends MeleeWeaponMedium {

	{
		name = "铁头棍";
		image = ItemSpriteSheet.LONG_STAFF;
		drawId= WeaponSprite.LONG_STAFF;
		critical=new BluntCritical(this);
	}

	protected int[][] weapRun() {
        return new int[][]{	{1, 1, 1, 1, 2, 2  },	//frame
                {3, 4, 4, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}
	protected int[][] weapAtk() {
        return new int[][]{	{1, 4, 3, 0 },	//frame
                {3, 4, 6, 4 },	//x
                {0, -3, 0, 0}};
	}

	public LongStaff() {
		super( 2 );
	}


	@Override
	public int min( int bonus ) {
		return super.min(bonus) -1;
	}

	@Override
	public int max( int bonus ) {
		return super.max(bonus) -/*3*/2;
	}

	@Override
	public Type weaponType() {
		return Type.M_POLEARM;
	}
	

	@Override
	public int guardStrength(int bonus){
		return (super.guardStrength(bonus)-1)*2;
	}

	@Override
	public String desc() {
		return "一根坚硬的木棍，足以有效地格挡攻击，同时会提高使用者的专注能力"
				+"\n\n这种武器会稍微增强你的魔法效果，并且拥有额外的防御力。";
	}

}
