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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfClairvoyance;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.FlameParticle;
import com.watabou.utils.Random;

public class Fire extends Blob {

    public Fire() {
        super();

//        name = "raging fire";
        name = "烈焰";
    }

	@Override
	protected void evolve() {

		boolean[] flammable = Level.flammable;
		
		int from = WIDTH + 1;
		int to = Level.LENGTH - WIDTH - 1;
		
		boolean observe = false;

        Blob blob = Dungeon.level.blobs.get( Overgrowth.class );

        if (blob != null) {

            int par[] = blob.cur;

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {
                    blob.volume -= par[i];
                    par[i] = 0;
                }
            }
        }
		
		for (int pos=from; pos < to; pos++) {
			
			int fire;
			
			if (cur[pos] > 0) {

				burn( pos );

				fire = cur[pos] - Random.Int( 2 );

				if (fire <= 0 && flammable[pos]) {
					
					int oldTile = Dungeon.level.map[pos];
					Level.set( pos, Terrain.EMBERS );
					
//					if (Dungeon.visible[pos]) {
                        observe = true;
                        GameScene.updateMap( pos );
                        GameScene.discoverTile( pos, oldTile );
//					}
				}
				
			} else {

				if (flammable[pos] && (cur[pos-1] > 0 || cur[pos+1] > 0 || cur[pos-WIDTH] > 0 || cur[pos+WIDTH] > 0)) {

                    int flammability = 10;

                    for (int n : Level.NEIGHBOURS8) {
                        if (Level.water[pos + n]) {
                            flammability--;
                        }
                    }

                    if (Random.Int(20) < flammability) {

                        fire = 4;
                        burn(pos);

                    } else {

                        fire = 0;

                    }

				} else {
					fire = 0;
				}
			}
			
			volume += (off[pos] = fire);

		}
		
		if (observe) {
			Dungeon.observe();
		}
	}
	
	private void burn( int pos ) {
		Char ch = Actor.findChar(pos);
		if (ch != null) {
            if( ch.buff( Burning.class ) == null ){
                BuffActive.add( ch, Burning.class, TICK * 4 );
            } else {
                BuffActive.add( ch, Burning.class, TICK * 1.5f );
            }
		}
		
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null) {
			heap.burn();
		}

        if( Dungeon.level.map[pos] == Terrain.DOOR_ILLUSORY) {

            GameScene.discoverTile( pos, Dungeon.level.map[pos] );

            Level.set( pos, Terrain.DOOR_CLOSED);

            GameScene.updateMap( pos );

            ScrollOfClairvoyance.discover( pos );

        }
	}
	
	public void seed( int cell, int amount ) {
		if (cur[cell] == 0) {
			volume += amount;
			cur[cell] = amount;
		} else {
            volume += amount - cur[cell];
            cur[cell] = amount;
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start(FlameParticle.FACTORY, 0.03f, 0 );
	}
	
	@Override
//	public String tileDesc() { return "A fire is raging here. Better avoid it."; }
	public String tileDesc() { return "一团火焰正在这里肆虐。最好离它远点。"; }
}
