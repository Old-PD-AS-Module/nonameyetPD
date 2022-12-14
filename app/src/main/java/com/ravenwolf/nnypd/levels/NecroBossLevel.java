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
import com.ravenwolf.nnypd.actors.mobs.Necromancer;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.levels.painters.Painter;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.sprites.NecromancerSprite;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class NecroBossLevel extends Level {
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

	}

	private static final int ROOM_LEFT		= WIDTH / 2 - 4;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 4;
	private static final int ROOM_TOP		= 2;
	private static final int ROOM_BOTTOM	= 6;

	public static final int THRONE	    = ( ROOM_BOTTOM + 3 ) * WIDTH + WIDTH / 2;
	public static final int PIT_START	    = 11*Level.WIDTH;
	private static final int CHEST_POS	= THRONE + WIDTH * 5;
	public static final int ABOMINATION_TOMB	= THRONE + WIDTH * 6;

	private static final int[] STATUES =
			{THRONE + WIDTH * 1 + 2,THRONE + WIDTH * 1 - 2,
			THRONE + WIDTH * 5 + 2,THRONE + WIDTH * 5 - 2,
			THRONE + WIDTH * 7 + 3,THRONE + WIDTH * 7 - 3
					,THRONE + WIDTH * 8
			};
	
	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
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


		//Painter.fill( this, ROOM_LEFT, ROOM_BOTTOM+1, ROOM_RIGHT - ROOM_LEFT + 1, 12, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT, ROOM_BOTTOM+1, ROOM_RIGHT - ROOM_LEFT + 1, 5, Terrain.EMPTY );
		Painter.fill( this, ROOM_LEFT, ROOM_BOTTOM+5, ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.PEDESTAL );
		Painter.fill( this, ROOM_LEFT-1, ROOM_BOTTOM+6, ROOM_RIGHT - ROOM_LEFT + 3, 7, Terrain.EMPTY_SP2);

		//Painter.fill( this, ROOM_LEFT+3, ROOM_BOTTOM+5, ROOM_RIGHT - ROOM_LEFT -5, 2, Terrain.EMPTY_SP );


		for( int statue : STATUES) {
			if(map[ statue] == Terrain.EMPTY)
				map[ statue] = Terrain.STATUE;
			else {
				map[statue] = Terrain.STATUE_SP;
				map[statue+WIDTH] = Terrain.EMPTY_SP;
			}
		}

		map[ ABOMINATION_TOMB ] = Terrain.STATUE_SP;
		//map[ABOMINATION_TOMB+WIDTH] = Terrain.EMPTY_SP;
		map[ THRONE ] = Terrain.PEDESTAL;

		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1, 
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1, 
			ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY );

		Painter.fill( this, ROOM_LEFT-1, ROOM_TOP + 2,
				ROOM_RIGHT - ROOM_LEFT + 3, 2, Terrain.EMPTY );
		
		/*Painter.fill( this, ROOM_LEFT, ROOM_TOP,
			ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.STATUE );

		//clear exit door
		map[ROOM_TOP * WIDTH + WIDTH / 2] = Terrain.EMPTY;*/

		exit = (ROOM_TOP - 1) * WIDTH + WIDTH / 2;
		map[exit+WIDTH] = Terrain.PEDESTAL;
		map[exit+WIDTH+1] = Terrain.PEDESTAL;
		map[exit+WIDTH-1] = Terrain.PEDESTAL;


		map[exit+WIDTH*3+2] = Terrain.STATUE;
		map[exit+WIDTH*3-2] = Terrain.STATUE;

		map[exit] = Terrain.LOCKED_EXIT;
		
		arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * WIDTH;
		map[arenaDoor] = Terrain.DOOR_CLOSED;

		entrance =exit+WIDTH*4;
		map[entrance] = Terrain.ENTRANCE;

/*
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
		*/
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
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * WIDTH;
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.BONES;
		}

		drop( new GoldenKey(), THRONE +WIDTH+Random.IntRange(-1,1)).type = Heap.Type.BONES;
		drop( new SkeletonKey(), CHEST_POS ).type = Heap.Type.LOCKED_CHEST;
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

		super.press(cell, hero);

		if (!enteredArena && cell == CHEST_POS ) {

			Heap chest = Dungeon.level.heaps.get( cell );

			if( chest.type != Heap.Type.LOCKED_CHEST ) {

				enteredArena = true;

				Necromancer boss = new Necromancer();
				boss.pos = THRONE;
				Dungeon.level.press(boss.pos, boss);
				GameScene.add( boss,1f );
				((NecromancerSprite)boss.sprite).blink(THRONE,THRONE);
				boss.beckon(cell);

				if (Dungeon.visible[boss.pos]) {
					boss.sprite.alpha(0);
					boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.5f));
				}

				seal();

				/*Mob spawn = new Necromancer.BoneSpawn();
				spawn.pos=boss.pos+WIDTH *4;
				GameScene.add(spawn, 2f );
				spawn.spend(Actor.TICK);
				spawn.beckon(cell);*/

				Dungeon.observe();

				Music.INSTANCE.play( currentTrack(), true );
			}
		}
	}
	/*
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			
			keyDropped = true;
			seal(cell);

		}
		
		return super.drop( item, cell );
	}
*/
	public void seal() {
		enteredArena = true;
		set( arenaDoor, Terrain.WALL_SIGN );
		GameScene.updateMap( arenaDoor );
		new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( arenaDoor, 2f );
	}

	public void unseal() {

		new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( arenaDoor, 2f );

		set(arenaDoor, Terrain.DOOR_CLOSED);
		GameScene.updateMap(arenaDoor);
		Dungeon.observe();
		keyDropped=true;

		Music.INSTANCE.play( currentTrack(), true );
	}
	
	private boolean outsideEntranceRoom(int cell) {
		int cx = cell % WIDTH;
		int cy = cell / WIDTH;
		return cx < ROOM_LEFT - 1 || cx > ROOM_RIGHT + 1 || cy < ROOM_TOP - 1 || cy > ROOM_BOTTOM + 1;
	}

//    @Override
//    public boolean noTeleport() {
//        return enteredArena && !keyDropped;
//    }

    @Override
    public String tileName( int tile ) {
        return PrisonLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return PrisonLevel.tileDescs(tile);
    }
	
	@Override
	public void addVisuals( Scene scene ) {
		PrisonLevel.addVisuals( this, scene );
	}

    public String currentTrack() {
        return enteredArena && !keyDropped ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

}
