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
import com.watabou.noosa.TextureFilm;

public class FrostElementalSprite extends MobSprite {

	public FrostElementalSprite() {
		super();

		int offset=21;

		texture( Assets.ELEMENTAL );
		
		TextureFilm frames = new TextureFilm( texture, 12, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0+offset, 1+offset, 2+offset );
		
		run = new Animation( 12, true );
		run.frames( frames, 0+offset, 1+offset, 3+offset );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 4+offset, 5+offset, 6+offset );
		
		die = new Animation( 15, false );
		die.frames( frames, 7+offset, 8+offset, 9+offset, 10+offset, 11+offset, 12+offset, 13+offset, 12+offset );

        cast = attack.clone();

		sleep = new Animation(6,true);
		sleep.frames( frames, 16+offset,17+offset,18+offset);
		
		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		add( State.CHILLED );
	}
	
	@Override
	public void die() {
		super.die();
		remove( State.CHILLED );
	}

	@Override
	public int blood() {
		return 0xFF8EE3FF;
	}
}
