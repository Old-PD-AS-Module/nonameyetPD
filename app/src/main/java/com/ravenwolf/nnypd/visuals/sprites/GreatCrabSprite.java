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

public class GreatCrabSprite extends MobSprite {

	public GreatCrabSprite() {
		super();
		
		texture( Assets.CRAB );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 17, 18, 17, 19 );
		
		run = new Animation( 15, true );
		run.frames( frames, 20, 21, 22, 23 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 24, 25, 26 );
		
		die = new Animation( 12, false );
		die.frames( frames, 27, 28, 29, 30 );

		sleep = new Animation(1,true);
		sleep.frames( frames, 32,33);
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFFFFEA80;
	}
}
