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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ShaftParticle;

public class Sanctuary extends Blob {
	
	@Override
	protected void evolve() {
		
		for (int i=0; i < LENGTH; i++) {
			
			int offv = cur[i] > 0 ? cur[i] - 1 : 0;
			off[i] = offv;
			
			if (offv > 0) {
				
				volume += offv;
				
				Char ch = Actor.findChar( i );

				if ( ch != null ) {
					if (ch.isMagical()){
						//Disrupted disrupt=BuffActive.add( ch, Disrupted.class, TICK * 2 );
						Disrupted disrupt=BuffActive.add( ch, Disrupted.class, (float)Math.sqrt(offv)+1 );
						int healthValue = (int) Math.sqrt(ch.totalHealthValue()*3f);

						if (disrupt.getDuration() > healthValue)
							BuffActive.add( ch, Banished.class, TICK * 2 );
					}else {
						if (ch instanceof Hero) {
							BuffActive.add(ch, Shielding.class, TICK * 2);
						}
					}
				}
			}
		}

		//clears miasma
		Blob blob = Dungeon.level.blobs.get( Miasma.class );

		if (blob != null) {
			for (int pos=0; pos < LENGTH; pos++) {
				if ( cur[pos] > 0 && blob.cur[ pos ] >0 ) {
					blob.clear( pos );
				}
			}
		}
	}

	public void seed( int cell, int amount ) {
		int diff = amount - cur[cell];
		if (diff > 0) {
			cur[cell] = amount;
			volume += diff;
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use(emitter);
		emitter.start(ShaftParticle.FACTORY, 1.0f, 0);
	}

	@Override
	public String tileDesc() {
//		return "Shafts of light pierce the gloom of the underground, restoring hope of everything they touch.";
		return "圣光穿透了层层的黑暗,覆盖了此处的所有单位！";
	}
}
