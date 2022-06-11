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
import com.ravenwolf.nnypd.actors.blobs.AltarChallenge;
import com.ravenwolf.nnypd.actors.blobs.AltarEnchant;
import com.ravenwolf.nnypd.actors.blobs.AltarGold;
import com.ravenwolf.nnypd.actors.blobs.AltarKnowledge;
import com.ravenwolf.nnypd.actors.mobs.Piranha;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.body.BodyArmorCloth;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LargeRoomPainter extends Painter {



	public static void paint( Level level, Room room ) {

		//turn to standard room so enemies can spawn inside and cna be combined with other rooms
		room.type = Room.Type.STANDARD;

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY);

		boolean horizontal;
		if (room.width()  == 9)
			horizontal= false;
		else if (room.height()  == 9)
			horizontal= true;
		else
			horizontal =Random.Int(2) == 0;//random alignment

		Point c = room.center();
		Room.Door door = room.entrance();
		door.set( Room.Door.Type.REGULAR );

		//Large square room
		if (room.width()  == 10 && room.height()  == 10 && Random.Int(4) > 0){
			//Build largest inner rooms
			if (Dungeon.isPathwayLvl() || Random.Int(Dungeon.chapter() ) == 0){
				//trapped chests
				if (!Dungeon.isPathwayLvl() && Random.Int(2) == 0) {
					InnerRoomStyle style= new InnerRoomStyle(Terrain.CHASM,Terrain.EMPTY_SP,Terrain.EMPTY_SP, Terrain.SECRET_ALARM_TRAP);
					crossCenter(level, c, style);
				}else{
					InnerRoomStyle style= new InnerRoomStyle(Terrain.WALL,Terrain.EMPTY,Terrain.EMPTY, Terrain.SECRET_SUMMONING_TRAP);
					crossCenter(level, c, style);
					fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.SECRET_SUMMONING_TRAP );
				}
				level.drop(prize(level),c.x + c.y * Level.WIDTH, true).type = Heap.Type.CHEST;
			} else {
				//chasm: requires levitation potion
				InnerRoomStyle style = new InnerRoomStyle(Terrain.CHASM, Terrain.EMPTY_SP, Terrain.CHASM, Terrain.PEDESTAL);
				crossCenter(level, c, style);
				//special room, no enemies allowed
				room.type = Room.Type.LARGE;
				level.drop(prize(level),c.x + c.y * Level.WIDTH, true).type = Heap.Type.CHEST;
			}
		}else {
			InnerRoomStyle style = null;
			switch (Random.Int(6)) {
				//small square
				case 0:
					//random displacement
					if (room.width() == 10)
						c.x += Random.Int(3) - 1;
					if (room.height() == 10)
						c.y += Random.Int(3) - 1;
					if (Dungeon.isPathwayLvl() || Random.Int(Dungeon.chapter()) > 0) {
						//bookshelf, requires flame potion or explosives to open
						style = new InnerRoomStyle(Terrain.EMPTY_SP, Terrain.SHELF_EMPTY, Terrain.EMPTY_SP, Terrain.EMPTY_SP);
						innerSecretChest(level, c, style);
						set(level, c.x + (Random.Int(2) == 0 ? -1 : 1), c.y + Random.Int(3) - 1, Terrain.BOOKSHELF);
						set(level, c.x + Random.Int(3) - 1, c.y + (Random.Int(2) == 0 ? -1 : 1), Terrain.BOOKSHELF);
					} else {
						//trapped chasm
						style = new InnerRoomStyle(Terrain.CHASM, Terrain.EMPTY, Terrain.EMPTY, Random.Int(2) == 0 ? Terrain.SECRET_ALARM_TRAP : Terrain.SECRET_BOULDER_TRAP);
						innerSecretChest(level, c, style);
						randomChasmPatter(level, c, style);
					}
					level.drop(prize(level), c.x + c.y * Level.WIDTH, true).type = Heap.Type.CHEST;
					break;
				case 1:
					//inner library
					style = new InnerRoomStyle(Terrain.WALL, Terrain.EMPTY_SP, Terrain.DOOR_CLOSED, Terrain.EMPTY);
					innerLibrary(level, c, style, horizontal);
					//price inside innerLibrary
					break;
				case 2:
					//pool: piranha infested
					style = new InnerRoomStyle(Terrain.WALL, Terrain.EMPTY_SP, Terrain.EMPTY_SP, Terrain.PEDESTAL);
					semiCrossCenter(level, c, style, horizontal);

					fill(level, c.x - 1, c.y - 1, 3, 3, Terrain.WATER);

					int amount = 3;
					for (int i = 0; i < amount; i++) {
						Piranha piranha = new Piranha();
						do {
							piranha.pos = room.random();
						} while (level.map[piranha.pos] != Terrain.WATER || Actor.findChar(piranha.pos) != null);

						piranha.special = true;

						level.mobs.add(piranha);
						Actor.occupyCell(piranha);
					}

					level.drop(prize(level), c.x + c.y * Level.WIDTH, true).type = Heap.Type.CHEST;
					//special room, no enemies allowed
					room.type = Room.Type.LARGE;
					break;

				default:
					if (Dungeon.chapter() == 1 && (level.altarSpawned ||Random.Int(2) == 0)) {
						//statue bridge
						style = new InnerRoomStyle(Terrain.CHASM, Terrain.EMPTY_SP, Terrain.EMPTY_SP, Terrain.STATUE_SP);
						semiCrossCenter(level, c, style, horizontal);
						randomChasmPatter(level, c, style);
						level.drop(Generator.random( Generator.Category.MISC), c.x + (c.y + (Random.Int(2) == 0 ? -1 : 1)) * Level.WIDTH, true);
					} else if (Dungeon.chapter() == 2 && (level.altarSpawned ||Random.Int(2) == 0)) {
						//prison block
						style = new InnerRoomStyle(Terrain.WALL, Terrain.EMPTY, Terrain.DOOR_CLOSED);
						cellBlockCenter(level, c, style, horizontal);
						Point p;
						if (horizontal)
							p= new Point(c.x + (Random.Int(2) == 0 ? -1 : 1),  c.y);
						else
							p= new Point(c.x ,  c.y + (Random.Int(2) == 0 ? -1 : 1));
						level.drop(Random.oneOf(Generator.random(Generator.Category.MISC),Generator.random(Generator.Category.THROWING)), p.x  + p.y * Level.WIDTH, true).type = Heap.Type.BONES_CURSED;
						level.drop(new Gold().random(), p.x  + p.y  * Level.WIDTH, true);
					} else if (Dungeon.chapter() == 4 && (level.altarSpawned ||Random.Int(2) == 0)) {
						//dwarven library
						fill( level, room, 1, Terrain.EMPTY_SP);
						style = new InnerRoomStyle(Terrain.SHELF_EMPTY, Terrain.EMPTY_SP, Terrain.EMPTY_SP, Terrain.PEDESTAL);
						style.outterFloorTile=Terrain.EMPTY_SP;
						semiCrossCenter(level, c, style, horizontal);
						level.drop( Generator.random( Generator.Category.SCROLL), c.x + c.y * Level.WIDTH, true);
					}  else {
						style = new InnerRoomStyle(Terrain.WALL, Terrain.EMPTY, Terrain.DOOR_CLOSED, Terrain.STATUE);
						semiCrossCenter(level, c, style, horizontal);
						//spawns a random altar
						if (Random.Int(3)==0)
							randomAltar(level, c);
						else{
							for (int i=0; i < 2; i++) {
								level.drop( consumablePrize( level ), c.x + (Random.Int(2) == 0 ? -1 : 1)+ c.y * Level.WIDTH, true ).type = Heap.Type.BONES;;
							}
						}
					}
			}
		}

	}

	private static void randomAltar(Level level, Point c){

		set( level, c, Terrain.PEDESTAL );
		int pos = c.x + Level.WIDTH * c.y;
		int charges = 2;
		switch (Random.Int(4)) {

			case 0:
				AltarGold gold = (AltarGold) level.blobs.get(AltarGold.class);
				if (gold == null) {
					try {
						gold = new AltarGold();
					} catch (Exception e) {
						gold = null;
					}
				}

				gold.seed(pos, charges + Random.Int(3));
				level.blobs.put(AltarGold.class, gold);
				break;
			case 1 :
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
				break;
			case 2 :
				if (Random.Int(2)==0) {
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

	private static void innerSecretChest(Level level, Point c, InnerRoomStyle style){
		fill( level, c.x - 2, c.y - 2, 5, 5, style.wallTile );
		fill( level, c.x - 1, c.y - 1, 3, 3, style.floorTile );
		set( level, c, style.centerTile );
		randomCenterDoor( level,  c,2, style);
	}

	private static void crossCenter(Level level, Point c, InnerRoomStyle style){

		fill( level, c.x - 2, c.y - 2, 5, 5, style.wallTile );
		fill( level, c.x - 3, c.y - 3, 7, 7, style.wallTile );
		fill( level, c.x - 1, c.y - 1, 3, 3, style.floorTile );
		set( level, c, style.centerTile );

		//internal halls
		set( level, c.x - 2, c.y, style.floorTile );
		set( level, c.x + 2, c.y, style.floorTile );
		set( level, c.x , c.y - 2, style.floorTile );
		set( level, c.x , c.y + 2, style.floorTile );

		//clear outer edges
		set( level, c.x - 3, c.y - 3, style.outterFloorTile );
		set( level, c.x + 3, c.y + 3, style.outterFloorTile );
		set( level, c.x - 3, c.y + 3, style.outterFloorTile );
		set( level, c.x + 3, c.y - 3, style.outterFloorTile );

		int randAmmount = Random.Int(10);
		if (randAmmount > 7){//four doors
			set( level, c.x - 3, c.y, style.entranceTile );
			set( level, c.x + 3, c.y, style.entranceTile );
			set( level, c.x, c.y - 3, style.entranceTile );
			set( level, c.x, c.y + 3, style.entranceTile );
		}else
		if (randAmmount > 4) {//two doors
			randomCenterDoor( level,  c,3, style);
			randomCenterDoor( level,  c,3, style);
		}else{
			randomCenterDoor( level,  c,3, style);
		}

	}

	private static void semiCrossCenter(Level level, Point c, InnerRoomStyle style, boolean horizontal){

		fill( level, c.x - 2, c.y - 2, 5, 5, style.wallTile );
		if (horizontal)
			fill( level, c.x - 3, c.y - 2, 7, 5, style.wallTile );
		else
			fill( level, c.x - 2, c.y - 3, 5, 7, style.wallTile );
		fill( level, c.x - 1, c.y - 1, 3, 3, style.floorTile );
		set( level, c, style.centerTile );

		//internal halls
		if (horizontal) {
			set(level, c.x - 2, c.y, style.floorTile);
			set(level, c.x + 2, c.y, style.floorTile);
		}else {
			set(level, c.x, c.y - 2, style.floorTile);
			set(level, c.x, c.y + 2, style.floorTile);
		}

		//doors
		if (horizontal) {
			set(level, c.x - 3, c.y, style.entranceTile);
			set(level, c.x + 3, c.y, style.entranceTile);
		}else {
			set(level, c.x, c.y - 3, style.entranceTile);
			set(level, c.x, c.y + 3, style.entranceTile);
		}

		//clear edges
		if (horizontal) {
			set( level, c.x - 3, c.y - 2, style.outterFloorTile );
			set( level, c.x + 3, c.y + 2, style.outterFloorTile );
			set( level, c.x - 3, c.y + 2, style.outterFloorTile );
			set( level, c.x + 3, c.y - 2, style.outterFloorTile );
		}else {
			set(level, c.x - 2, c.y + 3, style.outterFloorTile);
			set(level, c.x + 2, c.y - 3, style.outterFloorTile);
			set(level, c.x - 2, c.y - 3, style.outterFloorTile);
			set(level, c.x + 2, c.y + 3, style.outterFloorTile);
		}
	}


	private static void cellBlockCenter(Level level, Point c, InnerRoomStyle style,boolean horizontal){

		if (horizontal) {
			fill(level, c.x - 3, c.y - 2, 7, 5, style.wallTile);
			fill( level, c.x - 2, c.y - 1, 5, 3, style.floorTile );
			fill(level, c.x , c.y - 1, 1, 3, style.wallTile);
		}else {
			fill(level, c.x - 2, c.y - 3, 5, 7, style.wallTile);
			fill( level, c.x - 1, c.y - 2, 3, 5, style.floorTile );
			fill(level, c.x -1, c.y , 3, 1, style.wallTile);
		}

		//doors
		if (horizontal) {
			set(level, c.x - 3, c.y + Random.Int(3) - 1, style.entranceTile);
			set(level, c.x + 3, c.y + Random.Int(3) - 1, style.entranceTile);
		}else {
			set(level, c.x + Random.Int(3) - 1, c.y - 3, style.entranceTile);
			set(level, c.x + Random.Int(3) - 1, c.y + 3, style.entranceTile);
		}
	}

	private static void innerLibrary(Level level, Point c, InnerRoomStyle style, boolean horizontal){

		if (horizontal) {
			fill(level, c.x - 3, c.y - 2, 7, 5, style.wallTile);
			fill( level, c.x - 2, c.y - 1, 5, 3, style.floorTile );
			fill(level, c.x -1 , c.y , 3, 1, Terrain.BOOKSHELF);
			//set(level, c.x  + Random.Int(3)-1, c.y + (Random.Int(2) == 0 ? - 1:1), Terrain.BOOKSHELF);
		}else {
			fill(level, c.x - 2, c.y - 3, 5, 7, style.wallTile);
			fill( level, c.x - 1, c.y - 2, 3, 5, style.floorTile );
			fill(level, c.x , c.y - 1, 1, 3, Terrain.BOOKSHELF);
			//set(level, c.x + (Random.Int(2) == 0 ? - 1:1), c.y + Random.Int(3)-1 , Terrain.BOOKSHELF);
		}

		if (Random.Int(2) == 0)
			set(level, c.x, c.y , Terrain.SHELF_EMPTY);

		//doors
		if (horizontal) {
			set(level, c.x + (Random.Int(2) == 0 ? - 3:3), c.y + Random.Int(3) - 1, style.entranceTile);
			Point p= new Point(c.x + (Random.Int(2) == 0 ? - 2:2), c.y + (Random.Int(2) == 0 ? - 1:1));
			level.drop( Generator.random( Generator.Category.SCROLL), p.x + p.y * Level.WIDTH, true);
		}else {
			set(level, c.x + Random.Int(3) - 1, c.y + (Random.Int(2) == 0 ? - 3:3), style.entranceTile);
			Point p= new Point(c.x + (Random.Int(2) == 0 ? - 1:1), c.y + (Random.Int(2) == 0 ? - 2:2));
			level.drop( Generator.random( Generator.Category.SCROLL), p.x + p.y * Level.WIDTH, true);
		}
	}

	private static void randomCenterDoor(Level level, Point c, int radio, InnerRoomStyle style){

		switch (Random.Int(4)){
			case 0:
				set( level, c.x - radio, c.y, style.entranceTile );
				break;
			case 1:
				set( level, c.x +radio, c.y, style.entranceTile );
				break;
			case 2:
				set( level, c.x, c.y - radio, style.entranceTile );
				break;
			default:
				set( level, c.x, c.y + radio, style.entranceTile );
		}

	}

	private static void randomChasmPatter(Level level, Point c, InnerRoomStyle style){

		switch (Random.Int(4)){
			case 0:
				set( level, c.x - 1, c.y -1, style.wallTile );
				set( level, c.x + 1, c.y +1, style.wallTile );
				if (Random.Int(2)== 0){
					set( level, c.x - 2, c.y -2, style.outterFloorTile );
					set( level, c.x + 2, c.y +2, style.outterFloorTile );
				}
				break;
			case 1:
				set( level, c.x - 1, c.y +1, style.wallTile );
				set( level, c.x + 1, c.y -1, style.wallTile );
				if (Random.Int(2)== 0){
					set( level, c.x - 2, c.y +2, style.outterFloorTile );
					set( level, c.x + 2, c.y -2, style.outterFloorTile );
				}
				break;
		}

	}


	private static Item prize(Level level ) {
		Item prize=null;
			switch (Random.Int(4)){
				case 0:
					do {
						prize = Generator.random(Generator.Category.WEAPON);
					} while (prize instanceof ThrowingWeapon || prize.lootChapter()+prize.bonus < Dungeon.chapter() +1 );
					break;
				case 1:
					do {
						prize = Generator.random( Generator.Category.ARMOR );
					} while (prize instanceof BodyArmorCloth || prize.lootChapter()+prize.bonus < Dungeon.chapter() +1 );
					break;
				case 2:
					prize =Generator.random( Generator.Category.WAND ).random();
					break;
				case 3:
					prize = Generator.random( Generator.Category.RING ).random();
					break;
			}
		return prize;
	}

	private static Item consumablePrize( Level level ) {

		Item prize = level.itemToSpawnAsPrize();
		if (prize != null) {
			return prize;
		}

		return Generator.random( Random.oneOf(
				Generator.Category.POTION,
				Generator.Category.SCROLL,
				Generator.Category.THROWING,
				Generator.Category.GOLD
		) );
	}

}



