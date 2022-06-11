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

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.Demon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;

public class DemonSprite extends MobSprite {

	private Animation summoning;
    private Animation blink;

	public DemonSprite() {
		super();
		
		texture( Assets.DEMON );
		
		TextureFilm frames = new TextureFilm( texture, 17, 16 );

		idle = new Animation( 1, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new Animation( 15, true );
		run.frames( frames, 2, 3, 4, 5, 6, 7 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 8, 9, 10 );

		die = new Animation( 8, false );
		die.frames( frames,  15, 16, 17, 18, 19 );

		cast = new Animation( 10, false );
		cast.frames( frames, 11, 12, 13, 14 );

        blink = new Animation( 10, false );
        blink.frames( frames, 11, 12, 13);

		summoning = new Animation( 10, true );
		summoning.frames( frames,  13, 14 );
		
		play( idle );
	}

	
	@Override
	public void die() {
		super.die();
		emitter().start( ElmoParticle.FACTORY, 0.1f, 6 );
	}

	public void summon(int from, int to ) {
		turnTo( from , to );
		play(summoning);
	}

	@Override
	public void onComplete(Animation anim) {

        super.onComplete(anim);
		if (anim == cast){
			if (ch instanceof Demon){
				if (((Demon) ch).summoning){
					summon(ch.pos,((Demon) ch).summoningPos);
				} else {
					idle();
				}
			} else {
				idle();
			}
		} else if (anim == blink){
            idle();
            isMoving=false;
        }
	}

    public void blink( int from, int to ) {
        place( to );
        play(blink);
        turnTo( from , to );
        isMoving = true;
        ch.onMotionComplete();
    }


	@Override
	public void link(Char ch) {
		super.link(ch);
		if (((Demon)ch).summoning)
			((Demon)ch).restoreSummoning();

	}
}
