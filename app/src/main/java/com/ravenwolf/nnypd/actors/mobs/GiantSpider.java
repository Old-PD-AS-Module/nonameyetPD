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

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Web;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.sprites.SpinnerSprite;
import com.watabou.utils.Random;

public class GiantSpider extends MobHealthy {

    public GiantSpider() {

        super( 7 );

		name = "巨型蜘蛛";
		spriteClass = SpinnerSprite.class;
		
		loot = new MysteryMeat();
		lootChance = 0.25f;

		maxDamage -= tier*2;

		FLEEING = new Fleeing();
	}

	@Override
	public String getTribe() {
		return TRIBE_BEAST;
	}
	
	@Override
	protected boolean act() {
		boolean result = super.act();

		if (state == FLEEING && !isScared() ) {
			if (enemy != null && enemySeen && enemy.buff( Poisoned.class ) == null) {
				state = HUNTING;
			}
		}

		return result;
	}
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Poisoned.class, damage *3);
            state = FLEEING;
		}

		if( Random.Int( 3 ) == 0) {
			GameScene.add(Blob.seed(enemy.pos, Random.IntRange(5, 7), Web.class));
		}
		
		return damage;
	}

/*
	@Override
	public boolean add( Buff buff ) {

		if (buff instanceof Ensnared) {

			Blob blob = Dungeon.level.blobs.get(Ensnared.class);

			if (blob.cur[pos] > 0)

				return false;
		}
	}
	*/
//	@Override
//	public void move( int step ) {
//		if (state == FLEEING) {
//			GameScene.add( Blob.seed( pos, Random.IntRange( 5, 7 ), Web.class ) );
//		}
//		super.move( step );
//	}
	
	@Override
	public String description() {
		return
				"这些在地下大量繁殖的蜘蛛会尽量避免直接战斗，而更愿意用毒攻击它的目标，而后扬长而去。它的腹部储存了大量的蛛丝，用于在猎物中毒后将其束缚起来。";
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Tormented.class ) == null) {
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
