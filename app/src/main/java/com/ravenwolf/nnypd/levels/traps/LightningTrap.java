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
package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Electricity;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.scenes.GameScene;

public class LightningTrap extends Trap {

    // FIXME
    public static LightningTrap TRAP = new LightningTrap();
	
	// 00x66CCEE
	
	public static void trigger( int pos ) {

        Level.set( pos, Terrain.INACTIVE_TRAP );

        Blob blob = Blob.seed( pos, 50, Electricity.class );
        GameScene.add( blob );

       /* boolean visible = false;
        boolean cross = Random.Int( 2 ) == 0;

        int[] tiles = cross ? Level.NEIGHBOURS5 : Level.NEIGHBOURSX ;

        for( int n : tiles ) {

            Char ch = Actor.findChar( pos + n );

            if (ch != null) {

                int power = 10 + Dungeon.chapter() * 3;

                power = Random.IntRange( power / 2, power );

                ch.damage(n == 0 ? power : power / 2, TRAP, Element.SHOCK);

            }

            visible = visible || Dungeon.visible[ pos ];

        }

        if( visible ) {

            Emitter emitter = CellEmitter.center( pos );

            int[] points1 = new int[2];
            int[] points2 = new int[2];

            if( cross ) {

                points1[0] = pos - Level.WIDTH;
                points1[1] = pos + Level.WIDTH;

                points2[0] = pos - 1;
                points2[1] = pos + 1;

            } else {

                points1[0] = pos - Level.WIDTH - 1;
                points1[1] = pos + Level.WIDTH + 1;

                points2[0] = pos - Level.WIDTH + 1;
                points2[1] = pos + Level.WIDTH - 1;

            }

            emitter.parent.add( new Lightning( points1, 2, null ) );
            emitter.parent.add( new Lightning( points2, 2, null ) );

            emitter.burst( SparkParticle.FACTORY, Random.Int( 4, 6 ) );

        }*/
	}
}
