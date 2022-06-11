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
package com.ravenwolf.nnypd.visuals.sprites;

import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.TextureFilm;

public class ScorpionSprite extends MobSprite {
	
	public ScorpionSprite() {
		super();
		
		texture( Assets.SCORPION);
		
		TextureFilm frames = new TextureFilm( texture, 18, 17 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 2, 1, 2 );
		
		run = new Animation( 8, true );
		run.frames( frames, 5, 5, 6, 6 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 0, 3, 4 );
		
//		cast = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 0, 7, 8, 9, 10 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0x007044;
	}

//	@Override
//	public void attack( int cell ) {
//		if (!Level.adjacent( cell, ch.pos )) {
//
//			cellToAttack = cell;
//			turnTo( ch.pos , cell );
//			play(cast);
//
//		} else {
//
//			super.attack( cell );
//
//		}
//	}

//	@Override
//	public void onComplete( Animation anim ) {
//		if (anim == cast) {
//			idle();
//
//			((MissileSprite)parent.recycle( MissileSprite.class )).
//			fail( ch.pos, cellToAttack, new Javelins(), new Callback() {
//				@Override
//				public void call() {
//					ch.onAttackComplete();
//				}
//			} );
//		} else {
//			super.onComplete( anim );
//		}
//	}
}
