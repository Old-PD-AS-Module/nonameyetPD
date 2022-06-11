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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Rejuvenation;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ShaftParticle;
import com.watabou.utils.Random;

public class Sunlight extends Blob {
	
	@Override
	protected void evolve() {

        super.evolve();
        if (volume > 0) {
            boolean mapUpdated = false;
/*
            Blob blob = Dungeon.level.blobs.get( Thunderstorm.class );
            if (blob != null) {
                int par[] = blob.cur;
                for (int i=0; i < LENGTH; i++) {
                    if (cur[i] > 0) {
                        blob.volume -= par[i];
                        par[i] = 0;
                    }
                }
            }*/

            int growth[] = new int[LENGTH];
            for (int i=0; i < LENGTH; i++) {
                if (cur[i] > 0) {
                    growth[i] = 2;
                    for (int n : Level.NEIGHBOURS8) {
                        if (Level.water[i + n]) {
                            growth[i]++;
                        }
                    }
                }
            }

            for (int i=0; i < LENGTH; i++) {
                int c = Dungeon.level.map[i];
                if ( Random.Int(20) < growth[i] ) {
                    if (c == Terrain.EMBERS) {
                        Level.set(i, Terrain.GRASS);
                        mapUpdated = true;
                    } else if (c == Terrain.GRASS) {
                        Level.set(i, Terrain.HIGH_GRASS);
                        mapUpdated = true;
                    } else if (c == Terrain.HIGH_GRASS && Dungeon.level.heaps.get(i) == null && Random.Int( 50 ) < growth[i]) {
                        Dungeon.level.drop(Generator.random(Generator.Category.HERB), i, true).type = Heap.Type.HEAP;
                    }
                }

                if (cur[i] > 0 ) {
                    Char ch = Actor.findChar(i);
                    if( ch != null ){
                        //BuffActive.add( ch, ch.isMagical() ? Disrupted.class : Rejuvenation.class, TICK );
                        BuffActive.add( ch, ch.isFriendly() || ch instanceof NPC ? Rejuvenation.class : Dazed.class, TICK );

                        if( ch.isMagical() && !ch.isFriendly()) {
                            int effect = (int)Math.sqrt( ch.totalHealthValue() );
                            ch.damage( effect, this, Element.ENERGY );
                            if( ch.sprite.visible ){
                                ch.sprite.emitter().start( Speck.factory( Speck.HOLY ), 0.05f, 2 );
                            }
                        }

                    }
                }
            }

            if (mapUpdated) {
                GameScene.updateMap();
                Dungeon.observe();
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use(emitter);
		emitter.start(ShaftParticle.FACTORY, 1.0f, 0);
	}
	
	@Override
/*	public String tileDesc() {
		return "Shafts of light pierce the gloom of the underground, restoring life of everything they touch.";
	}*/
    public String tileDesc() {
        return "阳光刺破了地牢中的黑暗，为其碰触的所有单位带来生机。";
    }

}
