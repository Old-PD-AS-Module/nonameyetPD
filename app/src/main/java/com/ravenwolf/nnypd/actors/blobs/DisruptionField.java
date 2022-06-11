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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.BArray;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.PurpleParticle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Arrays;


public class DisruptionField extends Blob {

    public int[] power;

    public DisruptionField() {
        super();

        power = new int[LENGTH];
//        name = "burst of disruptive energy";
        name = "分裂力场爆发出的强大能量";
    }

    //Used by charged staff
    public static int DELAYED_BURST_STATE=20;

    public static int INIT_STATE=10;
    public static int BURST_STATE=2;
    public static int REMOVE_STATE=1;
    public static int SPREAD_STATE=3;
    public static int NO_STATE=0;



    @Override
    protected void evolve() {

        boolean[] notBlocking = BArray.not( Level.solid, null );
        Char ch;
        //expand

        boolean heard=false;

        //first explode all the ready blobs
        for (int i = 0; i < LENGTH; i++) {
            if (cur[i] == BURST_STATE) {

                for (int n : Level.NEIGHBOURS9) {
                    int pos=n+i;
                    if (cur[pos]>0) {
                        if (Dungeon.visible[pos]) {
                            CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
                            heard = true;
                        }

                        if ((ch = Actor.findChar(pos)) != null) {
                            if (ch instanceof Char) {

                                //int effect =  cur[pos]==BURST_STATE? (int)(power[i]*1.33f):power[i];
                                int effect = power[i];

                                Char mob = ch;
                                //deals half damage to hero and allies
                                if (mob.isFriendly())
                                    effect/=2;

                                if (mob.isMagical())
                                    BuffActive.addFromDamage(mob, Disrupted.class, effect);
                                ch.damage(ch.absorb(Random.Int(effect / 2, effect), true), this, Element.ENERGY);

                            }
                        }
                        if (cur[pos]<SPREAD_STATE) {
                            off[pos] = NO_STATE;
                            cur[pos] = NO_STATE;
                        }
                    }
                }
                power[i] = 0;
            }
        }
        if (heard)
            Sample.INSTANCE.play(Assets.SND_MELD);

        volume=0;
        //Expand
        for (int i = 0; i < LENGTH; i++) {
            if (cur[i] == DELAYED_BURST_STATE) {
                off[i] = BURST_STATE;
                cur[i] = BURST_STATE;
                volume+=BURST_STATE;
            }else
            if (cur[i] > BURST_STATE  ) {

                if (cur[i] ==INIT_STATE){
                    cur[i]=SPREAD_STATE;
                    off[i]=SPREAD_STATE;
                    volume+=SPREAD_STATE;
                    if ((ch = Actor.findChar(i)) != null && ch instanceof Mob)
                        ((Mob)ch).inspect( i );

                }else if (cur[i] ==SPREAD_STATE) {
                    cur[i] =BURST_STATE;
                    off[i]=BURST_STATE;
                    volume+=BURST_STATE;
                    if ((ch = Actor.findChar(i)) != null && ch instanceof Mob)
                        ((Mob)ch).inspect( i );

                    for (int n : Level.NEIGHBOURS8) {
                        int pos=i + n;
                        if (notBlocking[pos] && cur[pos] == 0) {
                            if ((ch = Actor.findChar(pos)) != null && ch instanceof Mob)
                                ((Mob)ch).inspect( pos );
                            cur[pos] = REMOVE_STATE;
                            off[pos] = REMOVE_STATE;
                            volume += REMOVE_STATE;
                        }
                    }
                }
            }

        }



    }

/*
    @Override
    protected void evolve() {

        boolean[] notBlocking = BArray.not( Level.solid, null );
        Char ch;
        //expand

        boolean heared=false;

        //first explode all the ready blobs
        for (int i = 0; i < LENGTH; i++) {
            if (cur[i] == BURST_STATE) {
                if ((ch = Actor.findChar(i)) != null) {
                    if (ch instanceof Char) {


                        int effect = (int) Math.sqrt(ch.totalHealthValue() * power[i]);

                        Char mob = (Char        ) ch;
                        if (mob.isMagical())
                            BuffActive.addFromDamage(mob, Disrupted.class, effect);
                        ch.damage(Random.Int(effect / 2, effect) + 1, this, Element.ENERGY);
                    }
                }
                off[i] = 0;
                cur[i] = 0;
                power[i]=0;
                if (Dungeon.visible[ i ]) {
                    CellEmitter.get(i).burst(ElmoParticle.FACTORY, 6);
                    heared=true;
                }
            }
        }
        if (heared)
            Sample.INSTANCE.play(Assets.SND_MELD);

        volume=0;
        boolean[] spread = new boolean[LENGTH];
        //Expand
        for (int i = 0; i < LENGTH; i++) {
            if (cur[i] > BURST_STATE && !spread[i] ) {
                spread[i]=true;


                if (cur[i] !=INIT_STATE) {
                    int state = --cur[i];
                    off[i]=state;
                    volume+=state;
                    int spreadPower= power[i]/2;
                    for (int n : Level.NEIGHBOURS8) {
                        if (notBlocking[i + n] && cur[i + n] == 0) {
                            if (power[i + n] < spreadPower)
                                power[i + n] = spreadPower;
                            cur[i + n] = state;
                            off[i + n] = state;
                            volume += state;
                            spread[i + n] = true; //prevent keep spreading in the same turn
                        }
                    }
                }else{
                    int state = SPREAD_STATE;
                    cur[i]=state;
                    off[i]=state;
                    volume+=state;

                }
            }

        }



    }*/


    public void seed( int cell, int state ) {

        if (cur[cell]>NO_STATE) {
            if (cur[cell]==SPREAD_STATE || cur[cell]==BURST_STATE)
                state=cur[cell];
            cur[cell] = NO_STATE;
            //amount=SPREAD_STATE+1; //to make it grow more?
        }
        super.seed(cell,state);
        //power[cell]=4;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.pour(PurpleParticle.BURST, 0.10f );
    }

    @Override
//    public String tileDesc() {
//        return "Sparks of disrupting energy are lurking here.";
//    }
    public String tileDesc() {
        return "分裂力场强大的能量聚集在此处。";
    }


    private static final String POWER		= "power";
    private static final String START	= "start";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        if (volume > 0) {

            int start=bundle.getInt( START );//uses same start used by it parent to calc the cur

            int end;
            for (end=LENGTH-1; end > start; end--) {
                if (cur[end] > 0) {
                    break;
                }
            }

            bundle.put( START, start );
            bundle.put( POWER, trim( start, end + 1 ) );

        }
    }

    private int[] trim( int start, int end ) {
        int len = end - start;
        int[] copy = new int[len];
        System.arraycopy(power, start, copy, 0, len);
        return copy;
    }


    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle(bundle);

        int[] data = bundle.getIntArray( POWER );
        if (data != null) {
            int start = bundle.getInt( START );
            for (int i=0; i < data.length; i++) {
                power[i + start] = data[i];
            }
        }

        if (Level.resizingNeeded) {
            int[] power = new int[Level.LENGTH];
            Arrays.fill(power, 0);

            int loadedMapSize = Level.loadedMapSize;
            for (int i=0; i < loadedMapSize; i++) {
                System.arraycopy( this.power, i * loadedMapSize, power, i * Level.WIDTH, loadedMapSize );
            }

            this.power = power;
        }
    }

}