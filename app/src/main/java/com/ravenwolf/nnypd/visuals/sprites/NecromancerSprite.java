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

import com.ravenwolf.nnypd.actors.mobs.Necromancer;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class NecromancerSprite extends MobSprite {


	private Animation charging;
	private Animation castSpecial;
	private Animation blink;

	public NecromancerSprite(){
		super();

		texture( Assets.NECROMANCER );
		TextureFilm film = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );

		run = new Animation( 8, true );
		run.frames( film, 0, 0, 0, 2, 3, 4 );

		cast = new Animation( 8, false );
		cast.frames( film, 5, 6, 7, 8 );

		charging = new Animation( 5, true );
		charging.frames( film, 7, 8 );

		die = new Animation( 10, false );
		die.frames( film, 9, 10, 11, 12 );

		blink = new Animation( 10, false );
		blink.frames( film, 12, 11, 10, 9, 0 );

		attack = cast.clone();
		castSpecial = cast.clone();

		idle();
	}

	public void charge(){
		play(charging);
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == castSpecial){
			if(ch instanceof Necromancer && ((Necromancer)ch).chargingBones || ((Necromancer)ch).summoningState==2)
				charge();
			else
				idle();
		}else if (anim == blink) {
			isMoving = false;
			idle();
		}
	}

    public void cast(int cell) {
        turnTo( ch.pos , cell );
        play(cast);
    }

	public void castSpecial(int cell, Callback callback ) {
		animCallback = callback;
		turnTo( ch.pos, cell );
		play(castSpecial);
	}

	public void blink( int from, int to ) {
		place( to );
		play( blink );
		turnTo( from , to );
		isMoving = true;
		ch.onMotionComplete();
	}

}
