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
package com.ravenwolf.nnypd.visuals.effects;

import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.audio.Sample;

public class PetrificationFX extends Gizmo {

	private float phase;

	private CharSprite target;

	public PetrificationFX(CharSprite target ) {
		super();

		this.target = target;
		phase = 0;
	}
	
	@Override
	public void update() {
		super.update();

		//target.tint( strength(), strength(), strength(),0.5f );

		if ((phase += Game.elapsed * 2) < 1) {
			target.tint( 0.5f, 0.5f, 0.5f, phase * strength() );
		} else {
			target.tint( 0.5f, 0.5f, 0.5f, strength() );
		}
	}

	private float strength(){
		/*Petrification buff=target.ch.buff(Petrification.class);
		return  buff.strength();*/
		return  0.6f;
	}
	
	public void melt() {

		target.resetColorAlpha();
		killAndErase();

		if (visible) {
			Splash.at( target.center(), 0xAAAAAAAA/*0xFFB2D6FF*/, 4 );
			Sample.INSTANCE.play( Assets.SND_CURSED, 1, 1, 0.5f );
		}
	}
	
	public static PetrificationFX petrify(CharSprite sprite ) {
		
		PetrificationFX iceBlock = new PetrificationFX( sprite );
		if (sprite.parent!=null)
			sprite.parent.add( iceBlock );
		
		return iceBlock;
	}
}
