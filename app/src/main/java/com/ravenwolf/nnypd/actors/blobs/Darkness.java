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
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;

public class Darkness extends Blob {

	public int actingPriority() {
		return 13;
	}
	
	@Override
	protected void evolve() {
		super.evolve();

        Blob blob = Dungeon.level.blobs.get( Sunlight.class );

        if (blob != null) {

            int par[] = blob.cur;

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {
                    blob.volume -= par[i];
                    par[i] = 0;
                }
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour( Speck.factory( Speck.DARKNESS ), 0.2f );
	}
	
	@Override
//	public String tileDesc() { return "A cloud of impenetrable darkness is swirling here, obstructing your vision."; }
	public String tileDesc() { return "一片乌黑的浓烟在此盘旋，阻挡着你的视线。"; }
}
