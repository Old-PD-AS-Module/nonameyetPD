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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.Splash;
import com.watabou.noosa.TextureFilm;

public class AbominationSprite extends MobSprite {

	private Animation chargingChain;

	public AbominationSprite() {
		super();
		
		texture( Assets.ABOMINATION );

		TextureFilm frames = new TextureFilm( texture, 19, 16 );
		
		idle = new Animation( 4, true );
		idle.frames( frames, 0, 1, 2, 1, 0 , 0, 1, 2, 1, 0, 3 ,3);
		
		run = new Animation( 8, true );
		run.frames( frames, 4, 5, 6 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 7, 8, 9 );
		
		die = new Animation( 12, false );
		die.frames( frames, 10, 11, 12, 13 );

		sleep = new Animation(1,true);
		sleep.frames( frames, 15,16);

		chargingChain = new Animation( 4, true );
		chargingChain.frames( frames, 1,9, 8 );
		
		play( idle );

		//scale=new PointF(1.2f,1.2f);
	}

	public void chargeChain(int from, int to ) {
		turnTo( from , to );
		play(chargingChain);
	}


	@Override
	public void die() {
		super.die();
		if (Dungeon.visible[ch.pos]) {
			emitter().burst( Speck.factory( Speck.BONE ), 6 );
			Splash.at( ch.pos, blood(), 20 );
		}
	}
	
}
