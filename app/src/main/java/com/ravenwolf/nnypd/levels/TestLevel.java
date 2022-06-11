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


import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.TestStatue;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.potions.PotionOfStrength;
import com.ravenwolf.nnypd.levels.painters.Painter;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.Scene;

public class TestLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;

//        viewDistance = 8;
	}
	
//	private int stairs = 0;

    private static final int TOP			= 3;
    private static final int ARENA_WIDTH    = 11;
    private static final int ARENA_HEIGHT   = 11;
    private static final int CHAMBER_HEIGHT	= 3;

    private static final int LEFT	= (WIDTH - ARENA_WIDTH) / 2;
    private static final int CENTER_X = LEFT + ARENA_WIDTH / 2;
    private static final int CENTER_Y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT / 2;
    private static final int CENTER = CENTER_Y * WIDTH + CENTER_X;

    private static final int DOOR1 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT ;
    private static final int DOOR2 = (TOP + CHAMBER_HEIGHT) * WIDTH + LEFT + ARENA_WIDTH - 1;



	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}


    public String currentTrack() {
        return Assets.TRACK_CHAPTER_4;
    }

    @Override
    protected boolean build() {

        Painter.fill(this, LEFT, TOP, ARENA_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY);
        Painter.fill(this, LEFT, TOP + CHAMBER_HEIGHT, ARENA_WIDTH, 1, Terrain.WALL_DECO);

//        int left = pedestal( true );
//        int right = pedestal( false );
//        map[left] = map[right] = Terrain.PEDESTAL;
//        for (int i=left+1; i < right; i++) {
//            map[i] = Terrain.EMPTY_SP;
//        }

        entrance = (TOP + 1) * WIDTH + CENTER_X;
        map[entrance] = Terrain.ENTRANCE;

        exit = (TOP - 1) * WIDTH + CENTER_X;
        map[exit] = Terrain.LOCKED_EXIT;

//        int sign = ( TOP - 1 ) * WIDTH + CENTER_X + 1;
//        map[sign] = Terrain.WALL_SIGN;

        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, ARENA_WIDTH, ARENA_HEIGHT, Terrain.EMPTY );
        Painter.fill( this, LEFT, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );
        Painter.fill( this, LEFT + ARENA_WIDTH - 1, TOP + CHAMBER_HEIGHT + 1, 1, ARENA_HEIGHT, Terrain.EMPTY_SP );

        int x = LEFT;
        int y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
        while (x < LEFT + ARENA_WIDTH) {
            map[(y - ARENA_HEIGHT) * WIDTH + x] = Terrain.WALL;
            map[y * WIDTH - WIDTH + x] = Terrain.WALL;
            x++;
        }
        map[y * WIDTH  +LEFT] =  Terrain.EMPTY;
        map[y * WIDTH  +x-1] = Terrain.EMPTY;

        y-=2;
        map[y * WIDTH  +LEFT] =  Terrain.STATUE;
        map[y * WIDTH  +x-1] = Terrain.STATUE;

        map[ DOOR1 ] = Terrain.DOOR_CLOSED;
        map[ DOOR2 ] = Terrain.DOOR_CLOSED;


        return true;
    }

    @Override
    protected void createItems() {


        drop(new PotionOfStrength().quantity(10), TOP * WIDTH + CENTER_X - 1).type = Heap.Type.CHEST;

        Dungeon.depth=26;
        for ( int i = 18 ; i > 0 ; i-- ) {

            drop( Generator.randomEquipment().identify(), TOP * WIDTH + CENTER_X - 2, true ).randomizeType();

        }
    }


        @Override
    protected void decorate() {


//        for (int i=0; i < LENGTH; i++) {
//            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
//                map[i] = Terrain.EMPTY_DECO;
//            } else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
//                map[i] = Terrain.WALL_DECO;
//            }
//        }
    }

    @Override
    protected void createMobs() {
        int x = LEFT+2;
        int y = TOP + CHAMBER_HEIGHT + ARENA_HEIGHT;
        while (x < LEFT + ARENA_WIDTH-2) {
            if(x % 2 == 0 ) {
                Mob mob = new TestStatue();

                //map[y * WIDTH - WIDTH + x] = x % 2 == 0 ? Terrain.STATUE_WATER_WALL : Terrain.WALL;
                mob.pos = y * WIDTH - 6 * WIDTH + x;
                mobs.add(mob);

                Actor.occupyCell(mob);
            }else{
                Mob mob = new TestStatue();
                //Mob mob = new GnollShaman();

                //map[y * WIDTH - WIDTH + x] = x % 2 == 0 ? Terrain.STATUE_WATER_WALL : Terrain.WALL;
                mob.pos = y * WIDTH - 7 * WIDTH + x;
                mobs.add(mob);

                Actor.occupyCell(mob);
            }
            x++;
        }


        /*Mob mob = new Medusa();

        mob.pos =  (TOP + CHAMBER_HEIGHT+2) * WIDTH + LEFT + ARENA_WIDTH /2;
        mobs.add(mob);

        Actor.occupyCell(mob);*/
    }

    public Actor respawner() {
        return null;
    }


	@Override
	public String tileName( int tile ) {
		return SewerLevel.tileNames( tile );
	}

	@Override
	public String tileDesc( int tile ) {
        return SewerLevel.tileDescs(tile);
	}

    @Override
    public void addVisuals( Scene scene ) {
        SewerLevel.addVisuals( this, scene );
    }

    @Override
    public int nMobs() {
        return 0;
    }

}
