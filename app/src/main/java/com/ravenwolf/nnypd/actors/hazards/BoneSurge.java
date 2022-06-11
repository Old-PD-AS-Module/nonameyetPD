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

package com.ravenwolf.nnypd.actors.hazards;


import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.BlastParticle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;


public class BoneSurge extends Hazard {

    private static int W = Level.WIDTH;


    private int strength;
    private int duration;

    public BoneSurge() {
        super();
        spriteClass = SubmergedPiranha.InivisibleHazardSprite.class;

        this.pos = 0;

        this.strength = 0;
        this.duration = 0;
    }

    public void setValues( int pos, int strength, int duration ) {

        this.pos = pos;

        this.strength = strength;
        this.duration = duration;

    }

    @Override
    public boolean act() {

        duration--;

        if( duration > 0 ){
            spend( TICK );
        } else {
            explode();
        }

        return true;
    }

    public void press( int cell, Char ch ) {

    }


    public void explode() {

        for (int n : Level.NEIGHBOURS9) {

            int cell = pos + n;

            Char ch = Actor.findChar(cell);

            if( ch != null && !ch.immovable() ) {
                ch.knockBack(cell, strength,1);
            }

            if( Dungeon.visible[ cell ] ){
                CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 2 );
                CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 4 );
            }
        }

        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
        destroy();
    }

    private static final String STRENGTH = "strength";
    private static final String DURATION = "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( STRENGTH, strength );
        bundle.put( DURATION, duration );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        strength = bundle.getInt( STRENGTH );
        duration = bundle.getInt( DURATION );
    }

}
