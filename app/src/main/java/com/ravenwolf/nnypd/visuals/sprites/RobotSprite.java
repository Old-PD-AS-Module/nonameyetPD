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
import com.ravenwolf.nnypd.actors.mobs.Robot;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class RobotSprite extends MobSprite {


	Animation charging;
	private Emitter chargeParticles;

	public RobotSprite() {
		super();
		
		texture( Assets.ROBOT);
		
		TextureFilm frames = new TextureFilm( texture, 18, 15 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 0, 0 ,0, 0, 0, 0 ,0, 0, 0, 0 ,0, 0, 0, 0 ,0, 2, 3, 4, 4 ,5 ,6,7, 8, 9, 9, 10, 0  );
		
		run = new Animation( 12, true );
		run.frames( frames, 11, 12, 13, 14, 15, 16 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 0, 1 );

		//used to play charging animation
		charging = new Animation( 12, true );
		charging.frames( frames,  0, 0);
		
		die = new Animation( 8, false );
		die.frames( frames, 17, 18 );

		sleep = new Animation(1,true);
		sleep.frames( frames, 17);
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		chargeParticles = centerEmitter();
		chargeParticles.autoKill = false;
		chargeParticles.pour(EnergyParticle.FACTORY, 0.05f);
		chargeParticles.on = false;
		if (((Robot)ch).autoDestroy)
			play(charging);

	}


	@Override
	public void onComplete( Animation anim ) {

		super.onComplete( anim );

		if (anim == die) {
			chargeParticles.burst( Speck.factory( Speck.WOOL ), 12 );
		}
	}

	@Override
	public int blood() {
		return 0xFFFFFF88;
	}

	@Override
	public void update() {
		super.update();
		if (chargeParticles!=null) {
			chargeParticles.pos(center());
			chargeParticles.visible = visible;
		}
	}

	public void charge( int pos ){
		turnTo(ch.pos, pos);
		play(charging);
	}

	@Override
	public void play(Animation anim) {
		if (chargeParticles!=null)
			chargeParticles.on = anim == charging;
		super.play(anim);

	}

//	@Override
//	public void attack( int pos ) {
//		attackPos = pos;
//		super.attack( pos );
//	}
//
//	@Override
//	public void onComplete( Animation anim ) {
//		super.onComplete( anim );
//
//		if (anim == attack) {
//			if (Dungeon.visible[ch.pos] || Dungeon.visible[attackPos]) {
//				parent.add( new DeathRay( center(), DungeonTilemap.tileCenterToWorld( attackPos ) ) );
//			}
//		}
//	}
}
