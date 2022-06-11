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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypd.visuals.sprites.layers.AdditionalSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;


public class DeerHornBlade extends MeleeWeaponTHDual {

	{
		name = "月牙弯刃";
		//image = ItemSpriteSheet.DEERHORN_BLADE;
		drawId= WeaponSprite.DEERHORN_BLADE;
		critical=new BladeCritical(this);
	}

	public int getAdditionalDrawId(){
		if (!(Dungeon.hero.belongings.weap2 instanceof Shield))
			return AdditionalSprite.ADD_DEERHORN;
		else
			return super.getAdditionalDrawId();
	}

	public DeerHornBlade() {
		super( 3 );
	}

	@Override
	public int guardStrength(int bonus){
		return ((super.guardStrength(bonus)-1)*2);
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.60f;
	}


/*
	@Override
	public int max( int bonus ) {
		return super.max(bonus) - 2;
	}

	@Override
	public float speedFactor( Hero hero ) {

		//return super.speedFactor( hero ) * 1.33f;
		return super.speedFactor( hero ) * 2;

	}


*/
	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}

	@Override
	public String desc() {
		return "这是一对尖锐的弯曲刀刃，这种特殊武器拥有更快的攻击速度，并且有额外的防御力加成"
				+"\n\n这是一件非常快速的武器，并且非常适合弹反敌人";
	}
}
