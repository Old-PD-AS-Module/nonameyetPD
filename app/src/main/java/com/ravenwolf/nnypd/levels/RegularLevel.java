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
package com.ravenwolf.nnypd.levels;

import com.ravenwolf.nnypd.Bones;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hazards.HauntedArmorSleep;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Piranha;
import com.ravenwolf.nnypd.actors.mobs.Statue;
import com.ravenwolf.nnypd.actors.mobs.Wraith;
import com.ravenwolf.nnypd.actors.mobs.npcs.Blacksmith;
import com.ravenwolf.nnypd.actors.mobs.npcs.Enchantress;
import com.ravenwolf.nnypd.actors.mobs.npcs.MysteriousGuy;
import com.ravenwolf.nnypd.actors.mobs.npcs.PlagueDoctor;
import com.ravenwolf.nnypd.actors.mobs.npcs.VendingMachine;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.levels.Room.Type;
import com.ravenwolf.nnypd.levels.painters.Painter;
import com.ravenwolf.nnypd.levels.traps.IceBarrierTrap;
import com.ravenwolf.nnypd.levels.traps.SpearsTrap;
import com.ravenwolf.nnypd.levels.traps.Trap;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class RegularLevel extends Level {

    protected HashSet<Room> rooms = new HashSet<Room>();
	
	protected Room roomEntrance;
	protected Room roomExit;
	
	protected ArrayList<Room.Type> specials;
	
	public int secretDoors;
	
	@Override
	protected boolean build() {
		
		if (!initRooms()) {
			return false;
		}

		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );
		do {
			do {
				roomEntrance = Random.element( rooms );
			} while ( largeRoom == roomEntrance || roomEntrance.width() < 4 || roomEntrance.height() < 4 );
			
			do {
				roomExit = Random.element( rooms );
			} while (roomExit == roomEntrance  || largeRoom == roomExit || roomExit.width() < 4 || roomExit.height() < 4);

	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = roomEntrance.distance();
			
			if (retry++ > 10) {
				return false;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.EXIT;

		HashSet<Room> connected = new HashSet<Room>();
		connected.add( roomEntrance );

		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );
		if (path == null)
			return false;
		
		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}
		
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}
		
		int nConnected = (int)(rooms.size() * Random.Float( 0.5f, 0.7f ));
		while (connected.size() < nConnected) {
			Room cr = Random.element( connected );
			Room or = Random.element( cr.neighbours);
			if (!connected.contains( or )) {
				cr.connect( or );
				connected.add( or );
			}
		}
		
		if (Dungeon.shopOnLevel()) {
			Room shop = null;
			for (Room r : roomEntrance.connected.keySet()) {
				if (r != largeRoom && r.connected.size() == 1 && r.width() >= 6 && r.height() >= 5) {
					shop = r;
					break;
				}
			}
			
			if (shop == null) {
				return false;
			} else {
				shop.type = Type.SHOP;
			}
		}

		if (largeRoom != null && largeRoom.connected.size() > 0)
			largeRoom.type = Type.LARGE;


		specials = new ArrayList( Room.SPECIALS );
		if (Dungeon.bossLevel( Dungeon.depth + 1 )) {
			specials.remove( Type.WEAK_FLOOR );
		}

		if (Dungeon.isPathwayLvl()) {
			boolean specialExit = false;
			specials.remove( Type.WEAK_FLOOR );

			for (Room r : rooms) {
				if (r.type == Type.NULL && r.connected.size() == 1 && r.edges().size() <3 && r.width() > 5 && r.height() > 4 && r.top < 3 ) {
					if (r.top==0)
						r.top=1;
					specialExit=true;
					r.type =Type.KEY_KEEPER;
					break;
				}
			}
			if (!specialExit)
				return false;
		}

		assignRoomType();

		//sometimes blacksmith dont spawn?
		if (Dungeon.depth==17 && !Blacksmith.Quest.hasSpawned()){
			//FIXME this can make guaranteed rooms like wells and altars to not spawn
			return false;
		}

		paint();
		paintWater();
		paintGrass();
		
		placeTraps();
//		placeSign();
		
		return true;
	}
	
	protected boolean initRooms() {
		rooms = new HashSet<Room>();
		largeRoom = null;
		altarSpawned = false;
		split( new Rect( 0, 0, WIDTH - 1, HEIGHT - 1 ) );


		if (rooms.size() < 9) {
			return false;
		}

		if (Random.Int(3)==0) {
			//removes largest rooms
			for (int i = 0; i < 2; i++) {
				//removes larger room
				if (rooms.size() > 10) {
					int max = 0;
					Room rToRemove = null;
					for (Room r : rooms) {
						int size = r.width() + r.height();
						if (r != largeRoom && size > max) {
							rToRemove = r;
							max = size;
						}
					}
					rooms.remove(rToRemove);
				}
			}
		}else {
			//removes center room
			for (int i = 0; i < 3; i++) {
				if (rooms.size() > 10) {
					int min = WIDTH;
					Room rToRemove = null;
					for (Room r : rooms) {
						int dist = Math.abs(r.top + r.height() / 2 - HEIGHT / 2) + Math.abs(r.left + r.width() / 2 - WIDTH / 2);
						if (r != largeRoom && dist < min) {
							rToRemove = r;
							min = dist;
						}
					}
					rooms.remove(rToRemove);
				}
			}
		}
		Room[] ra = rooms.toArray( new Room[0] );
		for (int i=0; i < ra.length-1; i++) {
			for (int j=i+1; j < ra.length; j++) {
				ra[i].addNeighbour(ra[j]);
			}
		}

		return true;
	}
	
	protected void assignRoomType() {
		
		int chapter = Dungeon.chapter();

		for (Room r : rooms) {
			if (r.type == Type.NULL && 
				r.connected.size() == 1) {

				if (specials.size() > 0 &&
					r.width() > 3 && r.height() > 3
                  && ( Random.Int( 6 - (chapter+1)/2 ) == 0 ||
                    Dungeon.depth % 6 == 3 && specials.contains( Type.LABORATORY ) || wellNeeded || altarNeeded)
						//(Dungeon.depth == Dungeon.CAVES_PATHWAY || Dungeon.depth % 6 == Dungeon.GUARANTEED_WELL_LEVEL )&& specials.contains( Type.MAGIC_WELL ) )
                ) {

					if (pitRoomNeeded) {

						r.type = Type.PIT;
						pitRoomNeeded = false;

//						specials.remove( Type.ARMORY );
//						specials.remove( Type.CRYPT );
//						specials.remove( Type.LABORATORY );
//						specials.remove( Type.LIBRARY );
//						specials.remove( Type.STATUE );
//						specials.remove( Type.TREASURY );
//						specials.remove( Type.VAULT );
//						specials.remove( Type.WEAK_FLOOR );

					} else if (Dungeon.depth % 6 == 3 && specials.contains( Type.LABORATORY )) {
						
						r.type = Type.LABORATORY;
						
					} else if (wellNeeded){
						r.type = Type.MAGIC_WELL;
						wellNeeded=false;
						Dungeon.wells++;

					}  else if (altarNeeded){
						r.type = Type.ALTAR_POWER;
						Dungeon.altars++;
						altarNeeded=false;
						altarSpawned=true;
					} else {
						int n = specials.size();
						r.type = specials.get( Random.Int( n ) );

//						r.type = specials.get( Math.min( Random.Int( n ), Random.Int( n ) ) );
//						if (r.type == Type.WEAK_FLOOR) {
//							weakFloorCreated = true;
//						}

					}
					
					Room.useType( r.type );
					specials.remove( r.type );
//					specialRooms++;
					
				} else if (Random.Int( 2 ) == 0){

					HashSet<Room> neighbours = new HashSet<Room>();
					for (Room n : r.neighbours) {
						if (!r.connected.containsKey( n ) && 
							!Room.SPECIALS.contains( n.type ) &&
								n.type != Type.KEY_KEEPER &&
							n.type != Type.PIT) {
							
							neighbours.add(n);
						}
					}
					if (neighbours.size() > 1) {
						r.connect( Random.element( neighbours ) );
					}
				}
			}else if (r.type == null && r.width() > 4 && r.height() > 4){
				if (altarNeeded) {
					r.type = Type.ALTAR_POWER;
					Dungeon.altars++;
					altarNeeded = false;
				}
			}
		}
		
		int count = 0;
		for (Room r : rooms) {
			if (r.type == Type.NULL) {
				int connections = r.connected.size();
				if (connections == 0) {
					
				} else if (Random.Int( connections * connections ) == 0) {
					r.type = Type.STANDARD;
					count++;
				} else {
					r.type = Type.TUNNEL; 
				}
			}
		}
		
		while (count < 4) {
			Room r = randomRoom( Type.TUNNEL, 1 );
			if (r != null) {
				r.type = Type.STANDARD;
				count++;
			}
		}
	}
	
	protected void paintWater() {
		boolean[] lake = water();
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && lake[i]) {
				map[i] = Terrain.WATER;
			}
		}
	}
	
	protected void paintGrass() {
		boolean[] grass = grass();
		
		if (feeling == Feeling.GRASS) {
			
			for (Room room : rooms) {
				if (room.type != Type.NULL && room.type != Type.PASSAGE && room.type != Type.TUNNEL) {
					grass[(room.left + 1) + (room.top + 1) * WIDTH] = true;
					grass[(room.right - 1) + (room.top + 1) * WIDTH] = true;
					grass[(room.left + 1) + (room.bottom - 1) * WIDTH] = true;
					grass[(room.right - 1) + (room.bottom - 1) * WIDTH] = true;
				}
			}
		}

		for (int i=WIDTH+1; i < LENGTH-WIDTH-1; i++) {
			if (map[i] == Terrain.EMPTY && grass[i]) {
				int count = 1;
				for (int n : NEIGHBOURS8) {
					if (grass[i + n]) {
						count++;
					}
				}
				map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
			}
		}
	}
	
	protected abstract boolean[] water();
	protected abstract boolean[] grass();


	protected void placeTraps() {
		IceBarrierTrap iceBarrierTrap;
		int nTraps = nTraps();
		float[] trapChances = trapChances();
		int nTraps2 = nTraps + (this.feeling == Level.Feeling.PERMAFROST ? 6 : 0);
		for (int i = 0; i < nTraps2; i++) {
			int trapPos = -1;
			for (int j = 0; j < 5 && (trapPos = randomTrapCell()) <= -1; j++) {
			}
			if (trapPos > -1) {
				if (this.feeling != Level.Feeling.PERMAFROST || Random.Int(2) != 0) {
					try {
						iceBarrierTrap = (IceBarrierTrap) trapClasses()[Random.chances(trapChances)].newInstance();
					} catch (Exception e) {
					}
				} else if (Random.Int(3) == 0) {
					iceBarrierTrap = new IceBarrierTrap();
				} else {
					iceBarrierTrap = new ChillingTrap();
				}
				setTrap(iceBarrierTrap, trapPos);
				iceBarrierTrap.hide();
				if (Random.Int(Dungeon.chapter() + 1) == 0) {
					iceBarrierTrap.reveal();
				}
				this.map[trapPos] = iceBarrierTrap.visible ? 13 : 14;
			}
		}
	}

	protected void placeSign() {

		while (true) {
			int pos = roomEntrance.random_top();
			if ( map[pos] == Terrain.WALL || map[pos] == Terrain.WALL_DECO ) {
				map[pos] = Terrain.WALL_SIGN;
				break;
			}
		}
	}
	
	protected int nTraps() {
		return 2 + Dungeon.depth > 1 ? feeling == Feeling.TRAPS ?
                Dungeon.chapter() + Random.Int(6) :
                Random.Int( Dungeon.chapter() ) + Random.Int(6) :
                0 ;
	}

//    protected int nSecrets() {
//        return feeling == Feeling.TRAPS ?
//                ( Dungeon.depth - 1 ) / 6 + 1 + Random.Int( 3 ) :
//                Random.Int( ( Dungeon.depth - 1 ) / 6 + 1 ) + Random.Int( 3 ) ;
//    }

	protected Class<?>[] trapClasses() {
		return new Class[]{SpearsTrap.class};
	}

	protected float[] trapChances() {
		float[] chances = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		return chances;
	}
	
	protected int minRoomSize = 7;
	protected int maxRoomSize = 11;

	private Room largeRoom = null;
	
	protected void split( Rect rect ) {
		
		int w = rect.width();
		int h = rect.height();

		if (largeRoom == null && Random.Int(3) == 0 &&  (w == 10 &&  h == 10 ||
				w == 10 &&  h == 9 || w == 9 &&  h == 10 )){
			largeRoom = (Room)new Room().set( rect );
			rooms.add( largeRoom );
			return;
		}
		
		if (w > maxRoomSize && h < minRoomSize) {

			int vw = Random.Int( rect.left + 3, rect.right - 3 );
			split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
			split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			
		} else 
		if (h > maxRoomSize && w < minRoomSize) {

			int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
			split( new Rect( rect.left, rect.top, rect.right, vh ) );
			split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			
		} else 	
		if ((Math.random() <= (minRoomSize * minRoomSize / rect.square()) && w <= maxRoomSize && h <= maxRoomSize) || w < minRoomSize || h < minRoomSize) {

			rooms.add( (Room)new Room().set( rect ) );
			
		} else {
			
			if (Random.Float() < (float)(w - 2) / (w + h - 4)) {
				int vw = Random.Int( rect.left + 3, rect.right - 3 );
				split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
				split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			} else {
				int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
				split( new Rect( rect.left, rect.top, rect.right, vh ) );
				split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			}
			
		}
	}
	
	protected void paint() {
		
		for (Room r : rooms) {
			if (r.type != Type.NULL) {
				placeDoors( r );
				r.type.paint( this, r );
//			} else {
//				if (feeling == Feeling.CHASM && Random.Int( 2 ) == 0) {
//					Painter.fill( this, r, Terrain.WALL );
//				}
			}
		}
		
		for (Room r : rooms) {
			paintDoors( r );
		}
	}
	
	private void placeDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			Room.Door door = r.connected.get( n );
			if (door == null) {
				
				Rect i = r.intersect( n );
				if (i.width() == 0) {
					door = new Room.Door( 
						i.left, 
						Random.IntRange( i.top + 1, i.bottom - 1 ) );
				} else {
					door = new Room.Door( 
						Random.IntRange( i.left + 1, i.right - 1 ),
						i.top);
				}

				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}
	
	protected void paintDoors( Room r ) {

//        int nSecrets = nSecrets();

		for (Room n : r.connected.keySet()) {

			if (joinRooms( r, n )) {
				continue;
			}
			
			Room.Door d = r.connected.get( n );
			int door = d.x + d.y * WIDTH;
			
			switch (d.type) {
			case EMPTY:
				map[door] = Terrain.EMPTY;
				break;
			case TUNNEL:
				map[door] = tunnelTile();
				break;
			case REGULAR:
//				if (Dungeon.depth > 1 && secretDoors < nSecrets && Random.Int( 10 - Dungeon.depth % 6  ) == 0 ) {
//                    map[door] = Terrain.DOOR_ILLUSORY;
//                    secretDoors++;
//                } else {
                    map[door] = Terrain.DOOR_CLOSED;
//                }
				break;
			case UNLOCKED:
				map[door] = Terrain.DOOR_CLOSED;
				break;
			case HIDDEN:
				map[door] = Terrain.DOOR_ILLUSORY;
				break;
			case BARRICADE:
				map[door] = Terrain.BARRICADE;
				break;
			case LOCKED:
				map[door] = Terrain.LOCKED_DOOR;
				break;
			}
		}
	}
	
	protected boolean joinRooms( Room r, Room n ) {
		
		if (r.type != Room.Type.STANDARD || n.type != Room.Type.STANDARD) {
			return false;
		}

/*
		if (n.type != Room.Type.STANDARD && n.type != Room.Type.LARGE && n.type != Type.TUNNEL) {
			return false;
		}
		*/

		int maxMergeSize = maxRoomSize;
		//prison and dwarven city don't have larger merges
		if (Dungeon.chapter() != 2 && Dungeon.chapter() != 4)
			maxMergeSize+= maxRoomSize/2;

		Rect w = r.intersect(n);
		if (w.left == w.right) {
			
			if (w.bottom - w.top < 3) {
				return false;
			}
			
			if (w.height() == Math.max( r.height(), n.height() )) {
				return false;
			}
			//allow for large merges
			if (r.width() + n.width() > maxMergeSize) {
				return false;
			}
			
			w.top += 1;
			w.bottom -= 0;
			
			w.right++;
			
			Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.EMPTY );
			
		} else {
			
			if (w.right - w.left < 3) {
				return false;
			}
			
			if (w.width() == Math.max( r.width(), n.width() )) {
				return false;
			}
			
			if (r.height() + n.height() > maxMergeSize) {
				return false;
			}
			
			w.left += 1;
			w.right -= 0;
			
			w.bottom++;
			
			Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.EMPTY );
		}
		
		return true;
	}
	
	@Override
	public int nMobs() {
		return 4 + Math.max( Dungeon.depth % 6, Dungeon.chapter() ) + ( feeling == Feeling.TRAPS ? Dungeon.chapter() : 0 );
	}
	
	@Override
	protected void createMobs() {
		int nMobs = nMobs();
		for (int i=0; i < nMobs; i++) {

            Mob mob;

            int pos = -1;

            do {
                pos = randomRespawnCell();
            } while (pos == -1);

            if( feeling == Feeling.GRASS && map[pos] == Terrain.HIGH_GRASS && Random.Int( 4 ) == 0 ) {
                mob = new Statue();
                mob.state = mob.PASSIVE;
            } else if( feeling == Feeling.WATER && map[pos] == Terrain.WATER && Random.Int( 3 ) == 0 ) {
                mob = new Piranha();
                mob.state = mob.SLEEPING;
//            } else if( feeling == Feeling.TRAPS && Random.Int( 5 ) == 0 ) {
//                mob = new Mimic();
//                ((Mimic)mob).items.add(new Gold().random());
//                ((Mimic)mob).items.add(new Gold().random());
//                mob.state = mob.WANDERING;
            } else if( feeling == Feeling.HAUNT && Random.Int(6 ) == 0 ) {

                mob = new Wraith();
                mob.state = mob.HUNTING;

            } else {

                mob = Bestiary.mob(Dungeon.depth);

                pos=getValidPos(pos,mob);

            }
            mob.pos = pos;

            //some mobs spawn wandering
            if (i +1 < Dungeon.chapter() && mob.state == mob.SLEEPING)
				mob.state = mob.WANDERING;
			mobs.add( mob );

			Actor.occupyCell( mob );
		}

		for (Heap heap : heaps.values()) {
			if (heap.type== Heap.Type.HEAP && heap.items.get(0) instanceof BodyArmor) {
				if ( feeling == Feeling.HAUNT && Random.Int(3) == 0|| Dungeon.chapter() > Random.Int(8)){
					HauntedArmorSleep hauntedArmor = new HauntedArmorSleep();
					hauntedArmor.setStats(heap.pos, (BodyArmor) ((BodyArmor) heap.items.get(0)).inscribe());
					hazards.add(hauntedArmor);
				}
			}
			/*if (heap.type== Heap.Type.HEAP && heap.items.get(0) instanceof MeleeWeapon) {
				if ( feeling == Feeling.HAUNT && Random.Int(5) == 0 || Dungeon.chapter() > Random.Int(10)){
					HauntedWeaponSleep hauntedWeapon = new HauntedWeaponSleep();
					hauntedWeapon.setStats(heap.pos, (MeleeWeapon) ((MeleeWeapon) heap.items.get(0)).enchant());
					hazards.add(hauntedWeapon);
				}
			}*/
		}

		if (specialNPCNeeded){
			specialNPCNeeded = false;
			Dungeon.specialNPCs++;
			spawnSpecialNPC(Dungeon.chapter());
		}

	}

	private void spawnSpecialNPC(int chapter){

		if (chapter < 3 && Random.Int(3) >0)
			PlagueDoctor.spawn(this);
		else if (chapter < 3 && Random.Int(3) >0)
			Enchantress.spawn(this);
		else if (chapter > 2 && chapter < 5 && Random.Int(chapter) < 2)
			VendingMachine.spawn(this, Random.Int(2) == 0);
		else
			MysteriousGuy.spawn(this);
	}


	private int getValidPos(int pos, Mob mob){

		int newPos=pos;

		//attempt 10 times
		boolean valid=true;
		for (int j=0; j<10; j++) {
			//check possible targets at one range
			for (int i : Level.NEIGHBOURS8) {
				try {
					Char n = Actor.findChar(newPos + i);
					if (mob.getTribe() == Mob.TRIBE_LONER || n instanceof Mob && !mob.getTribe().equals(((Mob)n).getTribe())){
						valid=false;
						break;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}//could be searching beyond the map limits
			}

			if (valid){
				//check possible targets at two range
				for (int i : Level.NEIGHBOURS16) {

					try {
						Char n = Actor.findChar(newPos+i);
						//has path to the character (no wall in between)
						if (mob.getTribe() == Mob.TRIBE_LONER || n instanceof Mob && (!mob.getTribe().equals(((Mob)n).getTribe()) &&
						 n.pos == Ballistica.cast(newPos, n.pos, false, false))) {
							valid = false;
							break;
						}

					} catch (ArrayIndexOutOfBoundsException e) {
					}//could be searching beyond the map limits
				}
			}
			if (valid)
				break;
			else {
				valid=true;
				do {
					newPos = randomRespawnCell();
				} while (newPos == -1);
			}
		}
		return newPos;
	}
	
	@Override
	public int randomRespawnCell( boolean ignoreTraps, boolean ignoreView ) {
		int count = 0;
		int cell = -1;
		
		while (true) {
			
			if (++count > 10) {
				return -1;
			}
			
			Room room = randomRoom( Room.Type.STANDARD, 10 );
			if (room == null) {
				continue;
			}
			
			cell = room.random();

            // FIXME

			if (
                Actor.findChar( cell ) == null &&
                distance(Dungeon.hero.pos, cell) > 8 &&
                ( ignoreView || !Dungeon.visible[cell] ) && !solid[cell] &&
                ( Level.mob_passable[cell] ||
                ignoreTraps && Level.passable[cell] )
            ) {

				return cell;

			}
			
		}
	}
	
	@Override
	public int randomDestination() {

        int cell = -1;

        if (!rooms.isEmpty())
        {
            while (true) {

                Room room = Random.element(rooms);
                if (room == null) {
                    continue;
                }

                cell = room.random();
                if (Level.mob_passable[cell]) {
                    return cell;
                }

            }
        }

        return cell;
	}
	
	@Override
	protected void createItems() {

        int chapter = Dungeon.chapter();
        int itemsMin = 1;
        int itemsMax = (chapter+1)/2 + 1;

		for ( int i = Random.IntRange( itemsMin, itemsMax ) - (chapter > 3 ? 1: 0) ; i > 0 ; i-- ) {

            drop( Generator.randomEquipment(), randomDropCell(), true ).randomizeType();

        }

        for ( int i = Random.IntRange( itemsMin, itemsMax ) - (Dungeon.depth % 3==0 ? 0: 1) ; i > 0 ; i-- ) {

            drop( Generator.randomComestible(), randomDropCell(), true ).randomizeType();

        }

        for ( int i = Random.IntRange( itemsMin, itemsMax ); i > 0 ; i-- ) {

            drop( Generator.random( Generator.Category.GOLD ), randomDropCell(), true ).randomizeType();

        }

        if( feeling == Feeling.HAUNT ) {

            for ( int i = Random.IntRange( itemsMin, itemsMax ); i > 0 ; i-- ) {

                drop(Generator.random(), randomDropCell( false ), true).type = Random.Int( 6 - chapter ) == 0 ? Heap.Type.BONES_CURSED : Heap.Type.BONES ;

            }
        }

        if( feeling == Feeling.TRAPS ) {

            for ( int i = Random.IntRange( itemsMin, itemsMax ); i > 0 ; i-- ) {

                drop(Random.oneOf(new Gold().random(),Generator.random()), randomDropCell( false ), true).type = Random.Int( 6 - chapter ) == 0 ? Heap.Type.CHEST_MIMIC : Heap.Type.CHEST ;

            }
        }

		for (Item item : itemsToSpawn) {
//			int cell = randomDropCell();
//			if (item instanceof ScrollOfUpgrade) {
//				while (map[cell] == Terrain.FIRE_TRAP || map[cell] == Terrain.SECRET_FIRE_TRAP) {
//					cell = randomDropCell();
//				}
//			}
			drop( item, randomDropCell(), true ).randomizeType();
		}
		
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomDropCell( false ), true ).type = Heap.Type.BONES_CURSED;
		}

		int baseChance= feeling == Level.Feeling.GRASS ? 22 : 18;

        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.HIGH_GRASS && heaps.get( i ) == null && Random.Int( baseChance - chapter * 2 ) == 0 ) {
                drop( Generator.random( Generator.Category.HERB ), i, true ).type = Heap.Type.HEAP;
            }
        }

        //randomly change empty libraries for bookshelves
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.SHELF_EMPTY && Random.Int( 5 ) == 0 ) {
				map[i] = Terrain.BOOKSHELF;
			}
		}

    }

	protected Room randomRoom( Room.Type type, int tries ) {
		for (int i=0; i < tries; i++) {
			Room room = Random.element( rooms );
			if (room.type == type) {
				return room;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.type != Type.NULL && room.inside( pos )) {
				return room;
			}
		}
		
		return null;
	}

    protected int randomTrapCell() {
        for (int i=0; i < 10; i++) {
            Room room = randomRoom( Room.Type.STANDARD, 1 );
            if (room != null) {
                int pos = room.random();
                if (map[pos] == Terrain.EMPTY || map[pos] == Terrain.EMPTY_DECO || map[pos] == Terrain.EMBERS || map[pos] == Terrain.GRASS) {
                    return pos;
                }
            }
        }
        return -1;
    }

    protected int randomDropCell() {
        return randomDropCell( true );
    }

	protected int randomDropCell( boolean ignoreHeaps ) {
		while ( true ) {
			Room room = randomRoom( Room.Type.STANDARD, 1 );
			if ( room != null ) {
				int pos = room.random();
				if ( !solid[pos] && mob_passable[pos] && ( ignoreHeaps || heaps.get( pos ) == null ) ) {
					return pos;
				}
			}
		}
	}
	
	@Override
	public int pitCell() {
		for (Room room : rooms) {
			if (room.type == Type.PIT) {
				return room.random();
			}
		}
		
		return super.pitCell();
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "rooms", rooms );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		rooms = new HashSet<Room>( (Collection<? extends Room>) bundle.getCollection( "rooms" ));
		for (Room r : rooms) {
			if (r.type == Type.WEAK_FLOOR) {
				weakFloorCreated = true;
				break;
			}
		}
	}
	
}
