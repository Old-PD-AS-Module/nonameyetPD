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
import com.ravenwolf.nnypd.actors.hazards.Hazard;
import com.ravenwolf.nnypd.actors.hazards.SubmergedPiranha;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;


public class Electricity extends Blob {

    public Electricity() {
        super();

//        name = "sparkling particles";
        name = "雷电粒子";
    }

    private boolean[] water;
    private boolean listened=false;
    int startCell;


    @Override
    protected void evolve() {
        super.evolve();

        //Char ch;
        listened=false;

        water = Level.water;

        for (int i=0; i < LENGTH; i++) {
        //spread first..

            if (cur[i] > 0) {
                spreadFromCell(i, cur[i]);
            }
        }


        //..then decrement/shock
        for (int i2 = 0; i2 < 1024; i2++) {
            if (this.cur[i2] > 0) {
                SubmergedPiranha sp = Hazard.findHazard(i2, SubmergedPiranha.class);
                if (sp != null) {
                    sp.spawnPiranha(null);
                }
                Char aChar = Actor.findChar(i2);
                if (aChar != null) {
                    int effect = (int) Math.sqrt(aChar.totalHealthValue());
                    aChar.damage(Random.Int(effect / 2, effect) + 1, this, Element.SHOCK);
                }
                int[] iArr2 = this.off;
                iArr2[i2] = this.cur[i2] / 2;
                this.volume += iArr2[i2];
            } else if (Level.distance(i2, this.startCell) > 1) {
                int i3 = this.volume;
                int[] iArr3 = this.off;
                this.volume = i3 - iArr3[i2];
                iArr3[i2] = 0;
            }
        }

    }

    private void spreadFromCell( int cell, int power ){

        if(!listened && Level.distance(cell, Dungeon.hero.pos) <= 4) {
            Sample.INSTANCE.play(Assets.SND_LIGHTNING);
            listened=true;
        }

        cur[cell] = Math.max(cur[cell], power);

        for (int c : Level.NEIGHBOURS4){
            if (water[cell + c] && cur[cell + c] < power){
                spreadFromCell(cell + c, power);
            }
        }
    }

    public void seed( int cell, int amount ) {
        super.seed(cell,amount);
        startCell=cell;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( SparkParticle.FACTORY, 0.05f, 0 );
    }

    @Override
//    public String tileDesc() {
//        return "A field of electric dust is sparkling brightly here.";
//    }
    public String tileDesc() {
        return "一些奔腾的雷电粒子正在这里逐步扩散";
    }

}