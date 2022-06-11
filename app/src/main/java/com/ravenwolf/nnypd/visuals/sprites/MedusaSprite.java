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
import com.ravenwolf.nnypd.actors.mobs.Medusa;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class MedusaSprite extends MobSprite {

	Animation charging;
	private Emitter chargeParticles;

	public MedusaSprite() {
		super();
		
		texture( Assets.MEDUSA );

		TextureFilm frames = new TextureFilm( texture, 22, 22 );
		
		idle = new Animation( 3, true );
		idle.frames(frames, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 4);
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 5, 6, 7, 8 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 9, 10, 11, 12, 13);

		//used to play charging animation
		charging = new Animation( 12, true );
		charging.frames( frames,  0, 14);

		
		die = new Animation( 8, false );
		die.frames( frames, 0, 14, 15, 16, 17, 18, 19, 20 );
		
		play( idle );

	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		chargeParticles = centerEmitter();
		chargeParticles.autoKill = false;
		chargeParticles.pour(EnergyParticle.FACTORY, 0.05f);
		chargeParticles.on = false;
		if (((Medusa)ch).phase== Medusa.PHASE_STONE_GAZE)
			play(charging);

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
}
