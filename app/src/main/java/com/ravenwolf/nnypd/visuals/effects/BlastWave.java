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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.levels.Level;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;

public class BlastWave extends Image {

	private static final float TIME_TO_FADE = 0.6f;

	private float time;
	private boolean shrink;

	public BlastWave() {
		super( Effects.get( Effects.Type.RIPPLE ) );
		origin.set( width / 2, height / 2 );
		hardlight( SpellSprite.COLOUR_HOLY );
	}
	
	public void reset( int p ) {
		revive();
		
		x = (p % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;
		
		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha( p );
			if (shrink)
				scale.set( 1 + p*2 );
			else
            	scale.set( 3 - p*2 );
		}
	}
	
	public static void createAtChar( Char ch ) {
		BlastWave w = (BlastWave)ch.sprite.parent.recycle( BlastWave.class );
		ch.sprite.parent.bringToFront( w );
		w.reset( ch.pos );
	}

	public static void createAtPos( int pos ) {
		createAtPos(pos,SpellSprite.COLOUR_HOLY, false);
	}

	public static void createAtPos( int pos, int color, boolean shrink ) {
		Group parent = Dungeon.hero.sprite.parent;
		BlastWave w = (BlastWave)parent.recycle( BlastWave.class );
		parent.bringToFront( w );
		w.reset( pos );
		w.hardlight( color );
		w.shrink=shrink;
	}
}
