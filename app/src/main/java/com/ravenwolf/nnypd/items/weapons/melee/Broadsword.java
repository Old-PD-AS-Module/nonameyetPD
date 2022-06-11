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

public class Broadsword extends MeleeWeaponMedium {
	
	{
		name = "长剑";
		image = ItemSpriteSheet.SWORD;
		drawId= WeaponSprite.SWORD;
		critical=new BladeCritical(this);
	}
	
	public Broadsword() {
		super( 3 );
	}

	@Override
	public int guardStrength(int bonus){
		return ((super.guardStrength(bonus)-1)*2);
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.45f;
	}

	@Override
	public String desc() {
		return "这柄剑的厚长利刃闪烁着令人安心的寒光。"
				+"\n\n这种武器更适合于用来弹反敌人，且增加防御力。";
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}
}
