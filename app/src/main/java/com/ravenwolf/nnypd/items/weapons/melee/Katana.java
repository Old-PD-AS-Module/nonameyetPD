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

public class Katana extends MeleeWeaponMedium {

	{
		name = "太刀";
		image = ItemSpriteSheet.KATANA;
		drawId= WeaponSprite.KATANA;
		critical=new BladeCritical(this,false, 1.5f);
	}

	public Katana() {
		super( 3 );
	}

	@Override
	public float getBackstabMod(){
		return 0.45f;
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.60f;
	}


	@Override
	public String desc() {

		return "一把微微弯曲但很长的刀. 平稳且精准，但想使用需要很高的技巧."
				+"\n\n 这把武器很适合弹反，对未察觉的敌人更有效，且更容易暴击";
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}
}
