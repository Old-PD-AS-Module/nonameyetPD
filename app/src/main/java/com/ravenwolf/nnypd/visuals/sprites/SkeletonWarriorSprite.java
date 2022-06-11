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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.mobs.SkeletonWarrior;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class SkeletonWarriorSprite extends MobLayeredSprite {

	public SkeletonWarriorSprite() {
		super();

		texture( Assets.SKELETON );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 8, true );
		//idle.frames(frames, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 23, 24);
		idle.frames(frames, 21);
		
		run = new Animation( 15, true );
		run.frames( frames, 25, 26, 27, 28, 29, 30 );

        attack = new Animation( 12, false );
        attack.frames( frames, 21, 35, 36, 37 );

		stab = new Animation( 14, false );
		stab.frames( frames, 35, 37, 37, 21 );

		backstab = new Animation( 12, false );
		backstab.frames( frames, 21, 36, 37, 21 );

        die = new Animation( 12, false );
        die.frames( frames, 31, 32, 33, 34 );
		
		play( idle );

    }


	protected Weapon getWeaponToDraw(){
		if(ch instanceof SkeletonWarrior) {
			SkeletonWarrior mob = (SkeletonWarrior) ch;
			return mob.weap;
		}
		return null;
	}
	
	@Override
	public void die() {
		super.die();
		if (Dungeon.visible[ch.pos]) {
			emitter().burst( Speck.factory( Speck.BONE ), 6 );
		}
	}
	
	@Override
	public int blood() {
		return 0xFFcc9966;
	}
}
