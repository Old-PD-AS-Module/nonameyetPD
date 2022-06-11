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


public class Axe extends MeleeWeaponMedium {

	{
		name = "手斧";
		image = ItemSpriteSheet.HAND_AXE;
		drawId= WeaponSprite.HAND_AXE;
		critical=new BladeCritical(this, false, 2f);
	}

	public Axe() {
		super( 1);
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)+1;
	}

	@Override
	public String desc() {
		return "一把轻便的斧头，通常是用来砍树，但是它的利刃对敌人来说同样致命"
				+"\n\n这种武器更擅长于暴击敌人。";
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}
}
