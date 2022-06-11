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
import com.ravenwolf.nnypd.actors.mobs.Medusa;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.items.misc.TomeOfMasterySkill;
import com.ravenwolf.nnypd.levels.painters.Painter;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class NagaBossLevel extends Level {
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

	}

	private static final int ROOM_LEFT		= WIDTH / 2 - 4;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 4;
	private static final int ROOM_TOP		= 2;
	private static final int ROOM_BOTTOM	= 6;

	private static final int WING_LEFT		= ROOM_LEFT-8;
	private static final int WING_RIGTH		= ROOM_RIGHT+8;
	private static final int WING_PASSAGE_Y		= 12;

	private static final int THRONE	    = ( ROOM_BOTTOM + 2 ) * WIDTH + WIDTH / 2;

	private static final int[][] STATUES =
			{
					{THRONE + WIDTH * 3 + 3,THRONE + WIDTH * 3 + 4},
					{THRONE + WIDTH * 3 - 3,THRONE + WIDTH * 3 - 4},

					{THRONE + WIDTH * 6 + 6,THRONE + WIDTH * 6 + 7},
					{THRONE + WIDTH * 6 - 6,THRONE + WIDTH * 6 - 7},

					{THRONE + WIDTH * 9 + 3,THRONE + WIDTH * 9 + 4},
					{THRONE + WIDTH * 9 - 3,THRONE + WIDTH * 9 - 4},
			};
	
	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	private static final String DOOR	= "door";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "dropped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( DOOR, arenaDoor );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		arenaDoor = bundle.getInt( DOOR );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {

		Painter.fill( this, ROOM_LEFT, ROOM_BOTTOM+1, ROOM_RIGHT - ROOM_LEFT + 1, 14, Terrain.EMPTY );
		Painter.fill( this, WING_LEFT, WING_PASSAGE_Y, WING_RIGTH - WING_LEFT + 1, 5, Terrain.EMPTY );
		Painter.fill( this, WING_LEFT+4, WING_PASSAGE_Y-2, WING_RIGTH - WING_LEFT -7, 9, Terrain.EMPTY );

		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(Terrain.STATUE_BRUTE, Terrain.STATUE_FROG,Terrain.STATUE_SHAMAN,
				Terrain.STATUE_BRUTE, Terrain.STATUE_FROG,Terrain.STATUE_SHAMAN));

		for( int[] altar : STATUES) {
			map[ altar [0]] = Terrain.WELL;
			map[ altar[1] ] = (int)Random.oneOf( list.toArray());
			list.remove(new Integer(map[ altar[1] ]));
		}
		
		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1, 
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1, 
			ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT-1, ROOM_TOP + 2,
				ROOM_RIGHT - ROOM_LEFT + 3, 2, Terrain.EMPTY );
		
		Painter.fill( this, ROOM_LEFT, ROOM_TOP, 
			ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.STATUE );

		//clear exit door
		map[ROOM_TOP * WIDTH + WIDTH / 2] = Terrain.EMPTY;

		exit = (ROOM_TOP - 1) * WIDTH + WIDTH / 2;
		map[exit] = Terrain.LOCKED_EXIT;
		
		arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * WIDTH;
		map[arenaDoor] = Terrain.DOOR_CLOSED;

		entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) + 
			Random.Int( ROOM_TOP + 2, ROOM_BOTTOM - 1 ) * WIDTH;
		map[entrance] = Terrain.ENTRANCE;


		boolean[] patch = Patch.generate( 0.4f, 5 );
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}

		boolean[] grass = Patch.generate( 0.4f, 6 );
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && grass[i]) {
				map[i] = Terrain.GRASS;
			}
		}
		
		return true;
	}
	
	@Override
	protected void decorate() {	
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+WIDTH] == Terrain.WALL) {
					n++;
				}
				if (map[i-WIDTH] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 8 ) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

	}
	
	@Override
	protected void createMobs() {	
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {

		int chestPos =  ROOM_LEFT +(ROOM_RIGHT - ROOM_LEFT)/2 + ROOM_TOP * WIDTH;
		map[chestPos] = Terrain.EMPTY;

		drop( new SkeletonKey(), chestPos ).type = Heap.Type.LOCKED_CHEST;
		drop( new TomeOfMasterySkill(), chestPos, true );


		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * WIDTH;
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.BONES;
		}
	}
	
	@Override
    public int randomRespawnCell( boolean ignoreTraps, boolean ignoreView ) {

        int cell;

        if( !enteredArena ) {

            do {
                cell = super.randomRespawnCell( ignoreTraps, ignoreView );
            } while ( outsideEntranceRoom(cell) );

        } else if( !keyDropped ) {

            do {
                cell = super.randomRespawnCell( ignoreTraps, ignoreView );
            } while ( !outsideEntranceRoom(cell) );

        } else {

            cell = super.randomRespawnCell( ignoreTraps, ignoreView );

        }

        return cell;
    }
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && outsideEntranceRoom(cell) && hero == Dungeon.hero) {
			
			enteredArena = true;

			Mob boss = new Medusa();
			//boss.state = boss.HUNTING;
			do {
				boss.pos = Random.Int( LENGTH );
			} while (
				!passable[boss.pos] ||
				!outsideEntranceRoom(boss.pos) ||
				Dungeon.visible[boss.pos]);
			GameScene.add( boss );
			
			set( arenaDoor, Terrain.WALL );
			GameScene.updateMap( arenaDoor );
			
			CellEmitter.get( arenaDoor ).start(Speck.factory(Speck.ROCK), 0.07f, 10 );
			Camera.main.shake(3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
            Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof GoldenKey) {
			
			keyDropped = true;
			
			CellEmitter.get( arenaDoor ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
			
			set(arenaDoor, Terrain.EMPTY_DECO);
			GameScene.updateMap(arenaDoor);
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
		
		return super.drop( item, cell );
	}
	
	private boolean outsideEntranceRoom(int cell) {
		int cx = cell % WIDTH;
		int cy = cell / WIDTH;
		return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1 || cy > ROOM_BOTTOM + 1;
	}

    @Override
    public boolean noTeleport() {
        return enteredArena && !keyDropped;
    }

    @Override
    public String tileName( int tile ) {
        return CavesLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CavesLevel.tileDescs(tile);
    }
	
	@Override
	public void addVisuals( Scene scene ) {
		CavesLevel.addVisuals( this, scene );
	}

    public String currentTrack() {
        return enteredArena && !keyDropped ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

    public static int getRandomPedestal(int pos){
		ArrayList list=new ArrayList();
		for( int[] altar : STATUES) {
			if (isDestructibleStatue(altar [1]))
				list.add(altar[0]);
		}


		if (list.isEmpty()) return 0;
		int targetPedestal=(int)(Random.oneOf( list.toArray()));
		while (list.size()>1 && Level.distance(pos, targetPedestal) < 4 && Actor.findChar(targetPedestal)!=null) {
			targetPedestal = getRandomPedestal(pos);
		}

		return targetPedestal;
	}

	public static boolean isDestructibleStatue(int pos){
		return Dungeon.level.map[pos]==Terrain.STATUE_BRUTE || Dungeon.level.map[pos]==Terrain.STATUE_FROG || Dungeon.level.map[pos]==Terrain.STATUE_SHAMAN;
	}

}
