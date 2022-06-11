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
package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SummoningTrap extends Trap {

	private static final float DELAY = 2f;
	
//	private static final Mob DUMMY = new Mob() {};
	
	// 0x770088
	
	public static void trigger( int pos ) {
		
		if (Dungeon.bossLevel()) {
			return;
		}
		
		int nMobs = 2;
		if (Random.Int( 7-Dungeon.chapter() ) == 0) {
			nMobs++;
		}
		
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				candidates.add( p );
			}
		}
		
		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );
			int placePos = candidates.get(index);
			if (Actor.findChar( placePos ) == null) {
				Mob mob = Bestiary.mob(Dungeon.depth);
				mob.state = mob.HUNTING;
				GameScene.add(mob, DELAY);
				CharmOfBlink.appear(mob, placePos);
				nMobs--;
			}
			
			candidates.remove( index );

		}
	}
}
