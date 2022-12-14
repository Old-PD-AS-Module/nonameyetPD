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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.SnowParticle;

public class FrigidVapours extends Blob {
    int startCell;
    public FrigidVapours() {
        super();

//        name = "frigid vapours";
        name = "冰雾";
    }

    @Override
    protected void evolve() {
        super.evolve();


        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {

                Char ch = Actor.findChar( i );

                if( ch != null ){

                    //ch.damage( Random.NormalIntRange(1,(int)(Math.sqrt( ch.totalHealthValue())/2)),this, Element.FROST );
                    BuffActive.addFromDamage( ch, Chilled.class, 2+Dungeon.depth/2 );
                    /*if( ch.buff( Chilled.class ) == null ){
                        BuffActive.add( ch, Chilled.class, TICK * 2 );
                    } else {
                        BuffActive.add( ch, Chilled.class, TICK );
                    }*/
                }

                Heap heap = Dungeon.level.heaps.get( i );
                if (heap != null) {
                    heap.freeze( TICK );
                }
            }
        }

        Blob blob = Dungeon.level.blobs.get( Fire.class );

        if (blob != null) {

            for (int pos=0; pos < LENGTH; pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    blob.clear( pos );

                }
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( SnowParticle.FACTORY, 0.3f );
    }

    public void seed(int cell, int amount) {
        FrigidVapours.super.seed(cell, amount);
        this.startCell = cell;
    }

    @Override
//    public String tileDesc() {
//        return "A cloud of freezing vapours is swirling here.";
//    }
    public String tileDesc() {
        return "一团冰冷刺骨的雾气正在此处盘旋。";
    }

//
//
//	// Returns true, if this cell is visible
//	public static boolean affect( int cell, int duration, Fire fire ) {
//
//		Char ch = Actor.findChar( cell );
//		if (ch != null) {
//
//            BuffActive.add( ch, Chilled.class, (float)duration );
//
////			Buff.prolong( ch, Chilled.class, duration * ( Level.water[cell] && !ch.flying ? 2 : 1 ) );
//		}
//
//		if (Dungeon.visible[cell]) {
//			CellEmitter.get( cell ).start( SnowParticle.FACTORY, 0.2f, 6 + duration );
//			return true;
//		} else {
//			return false;
//		}
//	}
}
