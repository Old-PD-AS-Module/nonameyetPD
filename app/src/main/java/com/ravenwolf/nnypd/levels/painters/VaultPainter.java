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
package com.ravenwolf.nnypd.levels.painters;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap.Type;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.keys.IronKey;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Random;

public class VaultPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		int cx = (room.left + room.right) / 2;
		int cy = (room.top + room.bottom) / 2;
		int c = cx + cy * Level.WIDTH;
		
//		switch (Random.Int( 3 )) {
//
//		case 0:
			level.drop( prize( level ), c, true ).type = Type.LOCKED_CHEST;
			level.addItemToSpawn( new GoldenKey() );
//			break;
			
//		case 1:
//			Item i1, i2;
//			do {
//				i1 = prize( level );
//				i2 = prize( level );
//			} while (i1.getClass() == i2.getClass());
//			level.drop( i1, c ).type = Type.CRYSTAL_CHEST;
//			level.drop( i2, c + Level.NEIGHBOURS4[Random.Int( 4 )]).type = Type.CRYSTAL_CHEST;
//			level.addItemToSpawn( new GoldenKey() );
//			break;
//
//		case 2:
//			level.drop( prize( level ), c );
//			set( level, c, Terrain.PEDESTAL );
//			break;
//		}
		
		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
	
	private static Item prize( Level level ) {

        Item wand = level.itemToSpawnAsPrize( Wand.class );
        Item ring = level.itemToSpawnAsPrize( Ring.class );

        Item prize = Random.oneOf( wand, ring );

        if( wand != null && prize != wand )
            level.addItemToSpawn( wand );

        if( ring != null && prize != ring )
            level.addItemToSpawn( ring );

        if( prize != null ) {
            return prize;
        }

        //Try to get a item of the same chapter or higher
		for (int i=0; i < 5; i++) {
			Item another = Generator.random( Random.oneOf(
					Generator.Category.WEAPON,
					Generator.Category.ARMOR
			) );
			if (another.lootChapter() >= Dungeon.chapter()) {
				prize = another;
				break;
			}
		}
		//chance to have an additional upgrade
		if (Random.Int(1+prize.bonus*2)==0)
			prize.bonus++;

		if( prize != null ) {
			return prize;
		}

		return Generator.random( Random.oneOf(
			Generator.Category.WAND, 
			Generator.Category.RING 
		) );
	}
}
