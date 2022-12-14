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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.FellFire;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.BlastParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.sprites.HazardSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;


public class FellRune extends Hazard {

    private static int W = Level.WIDTH;

    private int strength;
    private int duration;

    public FellRune() {
        super();

        this.var=9;//sprite frame
        this.pos = 0;

        this.strength = 0;
        this.duration = 0;

        spriteClass = RuneSprite.class;

    }


    public String desc() {
        return "A fell rune is placed here. It will burn with magical fire any intruder that step on it. ";
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
            ((FellRune.RuneSprite)sprite).disappear();
            destroy();
        }

        return true;
    }

    public void press( int cell, Char ch ) {
        if( ch!=null && ch.isFriendly()){
            explode();
        }
    }


    public void explode() {

        int cell = pos;

        Char ch = Actor.findChar( cell );
        if( ch != null ){
            BuffActive.addFromDamage(ch,FellFire.class,strength*2);
        }

        if( Dungeon.visible[ cell ] ){
            CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 2 );
            CellEmitter.get( cell ).burst(ElmoParticle.FACTORY,  6);
        }

        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );

        ((FellRune.RuneSprite)sprite).explode();
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

    public static class RuneSprite extends HazardSprite {

        private static float ANIM_TIME = 0.25f;

        private float time;
        protected Emitter burning;

        public RuneSprite(){
            super();
            time = 0.0f;
        }

        @Override
        protected String asset(){
            return Assets.HAZ_RUNE;
        }

        @Override
        public int spritePriority(){
            return 3;
        }

        @Override
        public void link( Hazard hazard ) {

            super.link( hazard );

            burning = GameScene.emitter();

            if( burning != null ){
                burning.pos( this );
                burning.pour( ElmoParticle.FACTORY, 0.6f );
            }

            parent.add( burning );
        }

        @Override
        public void update() {
            super.update();

            time += Game.elapsed * 3;

            tint( 1f, 1.2f, 1.0f, 0.2f + (float)Math.sin( time ) * 0.1f );
            speed.polar( time, 1.0f );

            if (burning != null) {
                burning.visible = visible;
            }
        }

        public void appear( ) {

            am = 0.0f;

            parent.add(new AlphaTweener( this, 1.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }

        public void disappear( ) {

            if (burning != null) {
                burning.on = false;
                burning = null;
            }

            parent.add(new AlphaTweener( this, 0.0f, ANIM_TIME ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }


        public void explode() {

            if (burning != null) {
                burning.on = false;
                burning = null;
            }

            parent.add(new ScaleTweener( this, new PointF(2, 2), ANIM_TIME ) {
                @Override
                protected void onComplete() {

                    RuneSprite.this.killAndErase();
                    parent.erase( this );

                }

                @Override
                protected void updateValues(float progress) {
                    super.updateValues(progress);
                    am = 1 - progress;
                }
            });
        }
    }
}
