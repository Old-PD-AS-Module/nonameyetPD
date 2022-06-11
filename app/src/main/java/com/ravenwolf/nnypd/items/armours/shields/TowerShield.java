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
package com.ravenwolf.nnypd.items.armours.shields;

import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.ShieldSprite;


public class TowerShield extends Shield {

	{
//		name = "tower shield";
		name = "塔盾";
		image = ItemSpriteSheet.SHIELD_TOWER;
		drawId= ShieldSprite.SHIELD_TOWER;
	}

	public TowerShield() { super( 4 ); }

	@Override
	public boolean bonusAgainstRanged() {
		return true;
	}

	@Override
	public int penaltyBase() {
		return super.penaltyBase() + 2;
	}

	@Override
/*	public String desc() {
		return "Size of this shield allows to cover your whole body behind it. "
				+"\n\n This shield is very effective blocking ranged attacks.";
	}*/
	public String desc() {
		return "坚实而又可靠的巨盾！足以保护你的全身。此盾牌可以有效防御远程攻击。";
	}

}
