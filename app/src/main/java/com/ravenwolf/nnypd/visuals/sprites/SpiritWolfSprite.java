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
import com.ravenwolf.nnypd.visuals.effects.particles.AltarParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.HauntedParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class SpiritWolfSprite extends MobSprite {

	private Emitter hauntedParticles;

	public SpiritWolfSprite() {
		super();

		texture( Assets.SPIRIT_WOLF);

		TextureFilm frames = new TextureFilm( texture, 18, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 1);
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 2, 3, 4, 5, 6 );
		
		attack = new Animation( 12, false );
		attack.frames( frames,  7, 8, 9, 0 );

        spawn = new Animation( 10, false );
        spawn.frames( frames, 3, 4, 5, 6 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0 );

		alpha(0.8f);
		play( idle );
	}


	@Override
	public void link(Char ch) {
		super.link(ch);

		hauntedParticles = emitter();
		hauntedParticles.autoKill = false;
		hauntedParticles.pour(HauntedParticle.FACTORY, 0.15f);
		hauntedParticles.on = true;
	}

	@Override
	public void update() {
		super.update();
		if(hauntedParticles !=null) {
			hauntedParticles.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();
		emitter().burst( AltarParticle.FACTORY, 10 );
		hauntedParticles.killAndErase();
	}

	@Override
	public int blood() {
		return 0x664488EE;
	}

}
