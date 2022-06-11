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

public class GnollBerserkerSprite extends MobSprite {


	public GnollBerserkerSprite() {
		super();

		texture( Assets.BRUTE );

		TextureFilm frames = new TextureFilm( texture, 13, 16 );


        idle = new Animation( 2, true );
        idle.frames( frames, 19, 19, 19, 20, 19, 19, 20, 20 );

        run = new Animation( 12, true );
        run.frames( frames, 23, 24, 25, 26 );

        attack = new Animation( 12, false );
        attack.frames( frames, 21, 22 );

        die = new Animation( 12, false );
        die.frames( frames, 27, 28, 29 );

        sleep = new Animation(1,true);
        sleep.frames( frames, 31,32);

/*
        idle = new Animation( 2, true );
        idle.frames( frames, 21, 21, 21, 22, 21, 21, 22, 22 );

        run = new Animation( 12, true );
        run.frames( frames, 25, 26, 27, 28 );

        attack = new Animation( 12, false );
        attack.frames( frames, 23, 24 );

        die = new Animation( 12, false );
        die.frames( frames, 29, 30, 31 );

        sleep = new Animation(1,true);
        sleep.frames( frames, 33,34);
*/
        cast = attack.clone();

		play( idle );
	}

}
