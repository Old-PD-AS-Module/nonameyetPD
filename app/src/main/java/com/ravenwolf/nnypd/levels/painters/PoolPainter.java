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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.Piranha;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.keys.Key;
import com.ravenwolf.nnypd.items.potions.PotionOfSparklingDust;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class PoolPainter extends Painter {
	
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.WATER );
		
		Room.Door door = room.entrance(); 
		door.set( Room.Door.Type.REGULAR );

		int x = -1;
		int y = -1;

		Point a = null;
		Point b = null;
		Point c = null;

		if (door.x == room.left) {

			a = new Point( room.left+1, door.y-1 );
			b = new Point( room.left+1, door.y );
			c = new Point( room.left+1, door.y+1 );

			x = room.right - 1;
			y = room.top + room.height() / 2;
			
		} else if (door.x == room.right) {

			a = new Point( room.right-1, door.y-1 );
			b = new Point( room.right-1, door.y );
			c = new Point( room.right-1, door.y+1 );
			
			x = room.left + 1;
			y = room.top + room.height() / 2;
			
		} else if (door.y == room.top) {

			a = new Point( door.x+1, room.top+1 );
			b = new Point( door.x, room.top+1 );
			c = new Point( door.x-1, room.top+1 );
			
			x = room.left + room.width() / 2;
			y = room.bottom - 1;
			
		} else if (door.y == room.bottom) {

			a = new Point( door.x+1, room.bottom-1 );
			b = new Point( door.x, room.bottom-1 );
			c = new Point( door.x-1, room.bottom-1 );
			
			x = room.left + room.width() / 2;
			y = room.top + 1;
			
		}

		if (a != null && level.map[a.x + a.y * Level.WIDTH] == Terrain.WATER) {
			set( level, a, Terrain.EMPTY_SP );
		}
		if (b != null && level.map[b.x + b.y * Level.WIDTH] == Terrain.WATER) {
			set( level, b, Terrain.EMPTY_SP );
		}
		if (c != null && level.map[c.x + c.y * Level.WIDTH] == Terrain.WATER) {
			set( level, c, Terrain.EMPTY_SP );
		}

		int pos = x + y * Level.WIDTH;
		Item prize =  prize( level );
		level.drop( prize, pos, true ).type =
			Random.Int( 3 ) == 0  || prize instanceof BodyArmor ? Heap.Type.CHEST : Heap.Type.HEAP;
		set(level, pos, Terrain.PEDESTAL);
		
		//level.addItemToSpawn( new PotionOfInvisibility() );
		level.addItemToSpawn( new PotionOfSparklingDust() );

        int amount = 3 + Random.Int( ( Dungeon.chapter() + 1 ) / 2 );

		for (int i=0; i < amount; i++) {
			Piranha piranha = new Piranha();
			do {
				piranha.pos = room.random();
			} while (level.map[piranha.pos] != Terrain.WATER || Actor.findChar( piranha.pos ) != null);

            piranha.special = true;

			level.mobs.add( piranha );
			Actor.occupyCell( piranha );
		}
	}
	
	private static Item prize( Level level ) {
		
		Item prize = level.itemToSpawnAsPrize( Key.class );

		if (prize != null) {
			return prize;
		}
		
		prize = Generator.random( Random.oneOf(  
			Generator.Category.WEAPON, 
			Generator.Category.ARMOR 
		) );

		for (int i=0; i < 4; i++) {
			Item another = Generator.random( Random.oneOf(  
				Generator.Category.WEAPON, 
				Generator.Category.ARMOR 
			) );
			if (another.bonus-(another.cursed ?1:0) > prize.bonus-(prize.cursed ?1:0)) {
				prize = another;
			}
		}
		
		return prize;
	}
}
