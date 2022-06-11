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
import com.ravenwolf.nnypd.actors.mobs.HauntedArmor;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.HauntedParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class HauntedArmorSprite extends MobSprite {

	protected static final int OFFSET=9;

	private Emitter hauntedParticles;

	public HauntedArmorSprite() {
		super();
		texture( Assets.HAUNTED_ARMOR);
		updateArmor(0);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		if (ch instanceof HauntedArmor) {
			HauntedArmor haunt = (HauntedArmor) ch;
			updateArmor(haunt.armor.getHauntedIndex());
		}

		hauntedParticles = emitter();
		hauntedParticles.autoKill = false;
		hauntedParticles.pour(HauntedParticle.FACTORY, 0.15f);
		hauntedParticles.on = true;
	}

	public void updateArmor(int index) {
		int offset= OFFSET*index;

		TextureFilm frames = new TextureFilm( texture, 14, 18 );

		idle = new Animation( 2, true );
		idle.frames( frames, offset+0, offset+1, offset+2 );

		run = new Animation( 8, true );
		run.frames( frames, offset+1, offset+3 );

		attack = new Animation( 10, false );
		attack.frames( frames, offset+4, offset+5, offset+6 );

		sleep = new Animation(1,true);
		sleep.frames( frames, offset+0, offset+1, offset+2);

		die = new Animation( 1, false );
		die.frames( frames, 8);

		alpha(0.8f);
		play( idle );
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
		emitter().burst( HauntedParticle.FACTORY, 10 );
		emitter().burst( ShadowParticle.UP, 8 );
		hauntedParticles.killAndErase();
	}

	@Override
	public int blood() {
		return 0x664488EE;
	}

}
