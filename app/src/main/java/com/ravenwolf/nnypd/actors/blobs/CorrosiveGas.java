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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.mobs.Elemental;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.utils.Random;

public class CorrosiveGas extends Blob {

    public CorrosiveGas() {
        super();
//        name = "cloud of gas";
        name = "腐蚀性气体";
    }

	@Override
    protected void evolve() {
        Char ch;
        CorrosiveGas.super.evolve();
        boolean terrainAffected = false;
        for (int i = 0; i < 1024; i++) {
            if (this.cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                BuffActive.add(ch, Corrosion.class, 2.0f);
                if (ch.buff(Burning.class) != null || (ch instanceof Elemental)) {
                    GameScene.add(Blob.seed(ch.pos, 2, Fire.class));
                }
            }
            if (this.cur[i] > 0 && Random.Int(4) == 0 && Dungeon.level.map[i] == 9) {
                Level.set(i, 10);
                GameScene.updateMap(i);
                terrainAffected = true;
            }
        }
        Blob blob = Dungeon.level.blobs.get(Fire.class);
        if (blob != null) {
            for (int pos = 0; pos < 1024; pos++) {
                if (this.cur[pos] > 0 && blob.cur[pos] < 2) {
                    int flammability = 0;
                    for (int n : Level.NEIGHBOURS8) {
                        if (blob.cur[pos + n] > 0) {
                            flammability++;
                        }
                    }
                    if (Random.Int(4) < flammability) {
                        int i2 = blob.volume;
                        blob.cur[pos] = 2;
                        blob.volume = i2 + 2;
                        int i3 = this.volume;
                        int[] iArr2 = this.cur;
                        this.volume = i3 - (iArr2[pos] / 2);
                        iArr2[pos] = iArr2[pos] - (iArr2[pos] / 2);
                    }
                }
            }
        }
        if (terrainAffected) {
            Dungeon.observe();
        }
    }
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.CAUSTIC ), 0.4f );
	}
	
	@Override
//	public String tileDesc() { return "A greenish cloud of highly caustic gas is swirling here."; }
	public String tileDesc() { return "这里盘绕着一片致命的腐蚀性气体。"; }

//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromGas();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
//		GLog.n( "You died from a toxic gas.." );
//	}
}
