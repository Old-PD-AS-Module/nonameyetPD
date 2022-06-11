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

public class Greatsword extends MeleeWeaponMedium  {
	
	{
		name = "巨剑";
		image = ItemSpriteSheet.GREATSWORD;
		drawId= WeaponSprite.GREATSWORD;
		critical=new BladeCritical(this);
	}

	protected int[][] weapRun() {
        return new int[][]{	{4, 4, 4, 0, 0, 0  },	//frame
                {2, 3, 5, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}
	protected int[][] weapAtk() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {1, 0, 7, 3 },	//x
                {0, -3, -2, 0}};
	}
	
	public Greatsword() {
		super( 4);
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.45f;
	}

	@Override
	public int guardStrength(int bonus){
		return ((super.guardStrength(bonus)-2)*3);
	}

	@Override
	public int penaltyBase() {
		return super.penaltyBase()+2;
	}

	@Override
	public String desc() {
		return "这把巨剑每次进行的沉重挥舞都能造成大量的伤害。"
				+"\n\n这种武器更适合用来弹反敌人，且增加防御力。";
	}
}
