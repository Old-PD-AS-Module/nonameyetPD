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
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class ShadowBatSprite extends MobSprite {

	private Emitter shadowParticles;

	public ShadowBatSprite() {
		super();

		texture( Assets.BAT );
		
		TextureFilm frames = new TextureFilm( texture, 15, 15 );

		idle = new Animation( 8, true );
		idle.frames( frames, 10, 11 );
		
		run = new Animation( 10, true );
		run.frames( frames, 10, 11 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 12, 13, 10, 11 );
		
		die = new Animation( 8, false );
		die.frames( frames, 14, 15, 16, 17 );

		sleep = new Animation(1,true);
		sleep.frames( frames, 18,19);
		
		play( idle );

	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		//shadowParticles = centerEmitter();
		shadowParticles = emitter();
		shadowParticles.autoKill = false;
		shadowParticles.pour(ShadowParticle.TRAIL, 0.15f);
		shadowParticles.on = !ch.sprite.sleeping;
	}

	@Override
	public void update() {
		super.update();
		if(shadowParticles!=null) {
			//shadowParticles.pos(center());
			shadowParticles.visible = visible;
		}
	}

	@Override
	public void play(Animation anim) {
		if(shadowParticles!=null)
			shadowParticles.on = (anim != sleep /*&& anim != die*/);
		super.play(anim);
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
	}

	public void kill() {
		if (shadowParticles != null)
			shadowParticles.killAndErase();
		super.kill();
	}

	@Override
	public int blood() {
		return 0x88440044;
	}
}
