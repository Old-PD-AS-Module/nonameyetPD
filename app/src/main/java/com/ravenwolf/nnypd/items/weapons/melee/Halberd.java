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

public class Halberd extends MeleeWeaponHeavy {

	{
		name = "斧枪";
		image = ItemSpriteSheet.HALBERD;
		drawId= WeaponSprite.HALBERD;
		critical=new BladeCritical(this, true, 1f);
	}

	protected int[][] weapRun() {
        return new int[][]{	{1, 1, 1, 1, 2, 2  },	//frame
                {3, 4, 4, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}
	protected int[][] weapAtk() {
        return new int[][]{	{1, 4, 3, 0 },	//frame
                {3, 4, 8, 4 },	//x
                {0, -3, 0, 0}};
	}
	protected int[][] weapStab() {
        return new int[][]{	{2, 2, 2, 0 },	//frame
                {1, 8, 8, 3 },	//x
                {0, -2, -2, 0}};
	}

	public Halberd() {
		super( 4 );
	}

	@Override
	public int reach(){
		return 2;
	}

	@Override
	public int max( int bonus ) {
		return super.max(bonus) -2;
	}

	/*@Override
	public float speedFactor( Hero hero ) {

		return super.speedFactor( hero ) * 0.75f;

	}*/

	@Override
	public Type weaponType() {
		return Type.M_POLEARM;
	}
	
	@Override
	public String desc() {
		return "有一天，一个人突发奇想，把长枪的范围和战斧的杀伤力结合起来。造就了现在这把强大的武器"
				+"\n\n这把武器拥有额外的攻击距离，并且有更强的暴击效果";
	}
}
