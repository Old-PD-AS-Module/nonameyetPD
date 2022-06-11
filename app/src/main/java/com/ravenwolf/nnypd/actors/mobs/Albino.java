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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.visuals.sprites.AlbinoSprite;
import com.watabou.utils.Random;

public class Albino extends MobEvasive {

	{
		name = "白化老鼠";
		spriteClass = AlbinoSprite.class;
	}

	public Albino() {
		super( Dungeon.chapter(), 3, false );
		minDamage += tier;
		maxDamage += tier;
		HP=HT+=tier*4;
	}

	@Override
	public String getTribe() {
		return TRIBE_BEAST;
	}
	

	@Override
	public int attackProc(Char enemy, int damage, boolean blocked ) {
			if ( !blocked && Random.Int( enemy.HT ) < damage *4) {
				BuffActive.addFromDamage( enemy, Bleeding.class, damage*2 );
			}

		return damage;
	}

	@Override
	public String description() {
		return "这儿有一只可爱的小白鼠，小小的，白白的，很想被人rua一下。 " ;
	}
}
