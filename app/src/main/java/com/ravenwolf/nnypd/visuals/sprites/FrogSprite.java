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

public class FrogSprite extends MobSprite {

	public FrogSprite() {
		super();
		
		texture( Assets.FROG );
		
		TextureFilm frames = new TextureFilm( texture, 19, 14 );
		
		idle = new Animation( 5, true );
		idle.frames(frames, 0, 1, 2, 3, 0, 1, 2, 3, 0, 11, 2, 3);
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 4, 5, 6, 7, 0 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 0, 8, 9);

		sleep = new Animation(1,true);
		sleep.frames( frames, 10,11);
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 10, 12, 13, 14 );
		
		play( idle );
	}
}
