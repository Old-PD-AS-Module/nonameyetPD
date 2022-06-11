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


public class ArcaneShield extends Shield {

	{
		name = "奥术之盾";
		image = ItemSpriteSheet.SHIELD_ARCANE;
		drawId= ShieldSprite.SHIELD_ARCANE;
	}

	public ArcaneShield() { super( 3 ); }

	@Override
	public boolean bonusAgainstRanged() {
		return true;
	}

	@Override
	public String desc() {
		return "一个表面覆盖着能量水晶的盾牌"
				+"\n\n此盾牌可以有效防御远程攻击，并且可以完全阻挡魔法伤害。";
	}
}
