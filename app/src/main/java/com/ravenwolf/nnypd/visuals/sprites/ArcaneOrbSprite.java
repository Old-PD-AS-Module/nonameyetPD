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

public class ArcaneOrbSprite extends MobSprite {

	public ArcaneOrbSprite() {
		super();

		texture( Assets.ARCANE_ORB);

		TextureFilm frames = new TextureFilm( texture, 18, 20 );

		idle = new Animation( 12, true );
		//idle.frames( frames, 4, 5, 6, 7,10,4, 5, 6, 7,2,3 );
		idle.frames( frames, 4, 13, 13, 6, 13 , 13 ,5, 13, 13, 7/*, 2,3*/);
		
		run = new Animation( 16, true );
		run.frames( frames, 4, 13, 13, 6, 13 , 13 ,5, 13, 13, 7 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 8, 9, 10, 11, 2, 3 );

        spawn = new Animation( 10, false );
        spawn.frames( frames, 12, 0, 1, 2, 3 );
		
		die = new Animation( 10, false );
		die.frames( frames, 3, 2, 1, 0, 12 );

		play( idle );
	}


	@Override
	public int blood() {
		return 0xFFEE77;
	}
}
