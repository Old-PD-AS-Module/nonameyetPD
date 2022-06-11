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
import com.ravenwolf.nnypd.actors.blobs.AltarChallenge;
import com.ravenwolf.nnypd.actors.blobs.AltarEnchant;
import com.ravenwolf.nnypd.actors.blobs.AltarGold;
import com.ravenwolf.nnypd.actors.blobs.AltarKnowledge;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class AltarOfPowerPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );


		if (Random.Int(3) == 0)
			room.entrance().set( Room.Door.Type.HIDDEN );
		else
			room.entrance().set( Room.Door.Type.REGULAR );

		int charges = 2;

		int pos;
		switch (Random.Int(4)) {

			case 0 :
				pos =surroundedAltar(level, room);
				AltarGold gold = (AltarGold) level.blobs.get(AltarGold.class);
				if (gold == null) {
					try {
						gold = new AltarGold();
					} catch (Exception e) {
						gold = null;
					}
				}

				gold.seed(pos, charges + 1 + Random.Int(3));
				level.blobs.put(AltarGold.class, gold);
				break;
			case 1 :
				pos = sprutedAltar(level, room);
				AltarEnchant enchant = (AltarEnchant) level.blobs.get(AltarEnchant.class);
				if (enchant == null) {
					try {
						enchant = new AltarEnchant();
					} catch (Exception e) {
						enchant = null;
					}
				}

				enchant.seed(pos, charges);
				level.blobs.put(AltarEnchant.class, enchant);
				room.entrance().set( Room.Door.Type.HIDDEN );
				break;
			case 2 :
				if (Random.Int(2)==0) {
					pos =challengeAltar(level, room);
					AltarChallenge chall = (AltarChallenge) level.blobs.get(AltarChallenge.class);
					if (chall == null) {
						try {
							chall = new AltarChallenge();
						} catch (Exception e) {
							chall = null;
						}
					}
					chall.seed(pos, charges);
					level.blobs.put(AltarChallenge.class, chall);
					break;
				}
			case 3 :
				pos =centeredAltar(level, room);
				AltarKnowledge know = (AltarKnowledge) level.blobs.get(AltarKnowledge.class);
				if (know == null) {
					try {
						know = new AltarKnowledge();
					} catch (Exception e) {
						know = null;
					}
				}

				know.seed(pos, charges + Random.Int(3));
				level.blobs.put(AltarKnowledge.class, know);
				break;
		}

	}


	private static int centeredAltar(Level level, Room room){
		Point c = room.center();
		set( level, c.x, c.y, Terrain.PEDESTAL );

		Room.Door door = room.entrance();
		int x = c.x;
		int y = c.y;
		if (door.x == room.left) {
			x = c.x + 1;
		} else if (door.x == room.right) {
			x = c.x - 1;
		} else if (door.y == room.top) {
			y = c.y + 1;
		} else if (door.y == room.bottom) {
			y = c.y - 1;
		}

		if (door.x == room.left || door.x == room.right) {
			set( level, x, y + 1, Terrain.STATUE );
			set( level, x, y - 1, Terrain.STATUE );
		} else if (door.y == room.top || door.y == room.bottom) {
			set( level, x + 1, y, Terrain.STATUE );
			set( level, x - 1, y, Terrain.STATUE );
		}
		return c.x + Level.WIDTH * c.y;
	}

	private static int surroundedAltar(Level level, Room room){
		Point c = room.center();
		int cx = c.x;
		int cy = c.y;

		Room.Door entrance = room.entrance();

		if (entrance.x == room.left) {
			cx = room.right - 2;
		} else if (entrance.x == room.right) {
			cx = room.left + 2;
		} else if (entrance.y == room.top) {
			cy = room.bottom - 2;
		} else if (entrance.y == room.bottom) {
			cy = room.top + 2;
		}


		fill( level, cx - 1, cy - 1, 3, 3, Terrain.EMPTY_SP );
		set( level, cx, cy, Terrain.PEDESTAL );
		return cx + cy * Level.WIDTH;
	}

	private static int sprutedAltar(Level level, Room room){
		Point c = room.center();
		int cx = c.x;
		int cy = c.y;

		Room.Door entrance = room.entrance();

		if (entrance.x == room.left) {
			fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.HIGH_GRASS );
			set(level, new Point(room.right - 2, room.top + 1), Terrain.HIGH_GRASS);
			set(level, new Point(room.right - 2, room.bottom - 1), Terrain.HIGH_GRASS);
			cx = room.right - 1;
		} else if (entrance.x == room.right) {
			fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.HIGH_GRASS );
			set(level, new Point(room.left + 2, room.top + 1), Terrain.HIGH_GRASS);
			set(level, new Point(room.left + 2, room.bottom - 1), Terrain.HIGH_GRASS);
			cx = room.left + 1;
		} else if (entrance.y == room.top) {
			fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.HIGH_GRASS );
			set(level, new Point(room.left + 1, room.bottom - 2), Terrain.HIGH_GRASS);
			set(level, new Point(room.right - 1, room.bottom - 2), Terrain.HIGH_GRASS);
			cy = room.bottom - 1;
		} else if (entrance.y == room.bottom) {
			fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.HIGH_GRASS );
			set(level, new Point(room.left + 1, room.top + 2), Terrain.HIGH_GRASS);
			set(level, new Point(room.right - 1, room.top + 2), Terrain.HIGH_GRASS);
			cy = room.top + 1;
		}


		set( level, cx, cy, Terrain.PEDESTAL );
		return cx + cy * Level.WIDTH;
	}

	private static int challengeAltar(Level level, Room room){
		fill( level, room, 1, Dungeon.isPathwayLvl() ? Terrain.HIGH_GRASS : Terrain.CHASM );

		Point c = room.center();
		Room.Door door = room.entrance();
		if (door.x == room.left || door.x == room.right) {
			Point p = drawInside( level, room, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
			for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
				set( level, p, Terrain.EMPTY_SP );
			}
		} else {
			Point p = drawInside( level, room, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
			for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
				set( level, p, Terrain.EMPTY_SP );
			}
		}

		fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMBERS );
		set( level, c, Terrain.PEDESTAL );
		door.set( Room.Door.Type.EMPTY );
		return c.x + c.y * Level.WIDTH;

	}



}
