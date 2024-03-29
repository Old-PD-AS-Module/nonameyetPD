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
import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.actors.mobs.npcs.Wandmaker;
import com.ravenwolf.nnypd.levels.Room.Type;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Halo;
import com.ravenwolf.nnypd.visuals.effects.particles.FlameParticle;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;

//        viewDistance = 7;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
    }

    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
    }
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}
	}
	
	@Override
	protected void createMobs() {
		super.createMobs();
		
		Wandmaker.Quest.spawn( this, roomEntrance );
	}
	
	@Override
	protected void decorate() {
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) { 
				
				float c = 0.05f;
				if (map[i + 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				
				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&  
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 6 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL && 
				map[i - WIDTH] == Terrain.WALL && 
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 3 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}

//        while (true) {
//            int pos = roomEntrance.random_top();
//            if (map[pos] == Terrain.WALL) {
//                map[pos] = Terrain.WALL_SIGN;
//                break;
//            }
//        }
	}

    @Override
    public String tileName( int tile ) {
        return PrisonLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return PrisonLevel.tileDescs(tile);
    }
	
//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "又黑又冷的水";
		default:
			return Level.tileNames(tile);
		}
	}
	
//	@Override
	public static String tileDescs(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return "地板上残留着一些干枯的血迹。";
			case Terrain.BOOKSHELF:
				return "这个书架可能是监狱图书馆的残留物。里面会不会藏着些有用的东西？";
			case Terrain.SHELF_EMPTY:
				return "这个书架可能是监狱图书馆的残留物。";
			case Terrain.EMPTY_SP2:
				return "地上到处都是骨头的残骸，这里发生了什么？";
			default:
				return Level.tileDescs(tile);
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Torch( i ) );
			}
		}
	}
	
	private static class Torch extends Emitter {
		
		private int pos;
		
		public Torch( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 1, p.y + 3, 2, 0 );
			
			pour( FlameParticle.FACTORY, 0.15f );
			
			add( new Halo( 16, 0xFFFFCC, 0.2f ).point( p.x, p.y ) );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
}