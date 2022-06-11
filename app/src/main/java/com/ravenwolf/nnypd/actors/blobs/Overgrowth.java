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
package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.items.wands.CharmOfThorns;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.LeafParticle;
import com.watabou.utils.Random;

public class Overgrowth extends Blob {
	
	@Override
	protected void evolve() {
		super.evolve();
		
		if (volume > 0) {
			
			boolean mapUpdated = false;

            int growth[] = new int[LENGTH];

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {

                    growth[i] = 10;

                    for (int n : Level.NEIGHBOURS8) {
                        switch( Dungeon.level.map[ i + n ] ) {
                            case Terrain.EMBERS:
                                growth[i] += 1;
                                break;
                            case Terrain.GRASS:
                                growth[i] += 3;
                                break;
                            case Terrain.HIGH_GRASS:
                                growth[i] += 5;
                                break;
                        }
                    }
                }
            }
            int stats = 6+Dungeon.chapter()*4;
            int level = Dungeon.chapter();

			for (int i=0; i < LENGTH; i++) {
				if (cur[i] > 0) {

                    if ( !Level.chasm[i] ) {

                        int c = Dungeon.level.map[i];

                        if ( Random.Int( 100 ) < growth[i] ) {

                            switch (c) {
                                case Terrain.EMPTY:
                                case Terrain.EMPTY_DECO:
                                case Terrain.EMBERS:

                                    Level.set(i, Terrain.GRASS);

//                                    if (Dungeon.visible[i]) {
                                        mapUpdated = true;
                                        GameScene.updateMap( i );
                                        GameScene.discoverTile( i, c );
//                                    }

                                    break;

                                case Terrain.GRASS:

                                        Level.set(i, Terrain.HIGH_GRASS);

//                                    if (Dungeon.visible[i]) {
                                        mapUpdated = true;
                                        GameScene.updateMap(i);
                                        GameScene.discoverTile(i, c);
//                                    }
                                   // break;

                                case Terrain.HIGH_GRASS:


                                    if (/*Dungeon.level.heaps.get(i) == null && */Actor.findChar(i) == null && Random.Int(500) < growth[i]) {

                                        // first we check the targeted tile
                                        CharmOfThorns.Thornvine.spawnAt( stats, 2 + level, i );

                                        //Dungeon.level.drop(Generator.random(Generator.Category.HERB), i, true).type = Heap.Type.HEAP;

                                    }
                                    break;

                            }
                        }

                        Char ch = Actor.findChar(i);
                        if (ch != null && !ch.flying ) {

                            if( ch.buff( Ensnared.class ) == null ){
                                BuffActive.add( ch, Ensnared.class, TICK * 2 );
                            } else if ( Random.Int(50) < growth[i] ) {
                                BuffActive.add( ch, Ensnared.class, TICK );
                            }
                        }

                    } else {

                        off[ i ] = 0;

                    }
				}
			}
			
			if (mapUpdated) {
//				GameScene.updateMap();
				Dungeon.observe();
			}
		}
	}

/*    @Override
    public String tileDesc() {
        return "A little tornado of leaves and roots is swirling on the ground here." +
                "Be careful walking here, as it can root you!";
    }*/

    @Override
    public String tileDesc() {
        return "一些充满了能量的植物正在此处疯狂生长。" +
                "路过这里时要多加小心，因为它同样会缠绕住你！";
    }
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.start( LeafParticle.LEVEL_SPECIFIC, 0.2f, 0 );
	}
}
