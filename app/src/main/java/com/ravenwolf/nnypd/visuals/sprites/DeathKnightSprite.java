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

import com.ravenwolf.nnypd.actors.mobs.Blackguard;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;
import com.watabou.noosa.TextureFilm;

public class DeathKnightSprite extends MobLayeredSprite {

	public DeathKnightSprite() {
		super();

		weapSprite= new WeaponSprite(this);
		
		texture( Assets.DEATHKNIGHT);
		
		TextureFilm frames = new TextureFilm( texture, 16, 17 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 0, 1, 0 );
		
		run = new Animation( 15, true );
		run.frames( frames, 2, 3, 4, 5, 6, 7 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 8, 9, 10 );

		stab = new Animation( 14, false );
		stab.frames( frames, 8, 10, 10, 0 );

		backstab = new Animation( 12, false );
		backstab.frames( frames, 0, 9, 10, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 11, 12, 13, 14, 15, 16, 17 );

		play( idle );
	}


	@Override
	public int blood() {
		return 0xFFC2C6CB;
	}


	protected Weapon getWeaponToDraw(){
		if (ch instanceof Blackguard) {
			Blackguard mob = (Blackguard) ch;
			return mob.weap;
		}
		return null;
	}


}
