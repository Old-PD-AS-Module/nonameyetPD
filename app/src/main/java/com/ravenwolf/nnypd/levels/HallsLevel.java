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

import android.opengl.GLES20;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import javax.microedition.khronos.opengles.GL10;

public class HallsLevel extends RegularLevel {

	{
		minRoomSize = 6;
		
//		viewDistance = 4;
		
		color1 = 0x801500;
		color2 = 0xa68521;
	}
	
//	@Override
//	public void create() {
//		addItemToSpawn( new Torch() );
//		super.create();
//	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.55f : 0.40f, 6 );
    }

    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.55f : 0.30f, 3 );
    }
	
	@Override
	protected void decorate() {
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) { 
				
				int count = 0;
				for (int j=0; j < NEIGHBOURS8.length; j++) {
					if ((Terrain.flags[map[i + NEIGHBOURS8[j]]] & Terrain.PASSABLE) > 0) {
						count++;
					}
				}
				
				if (Random.Int( 80 ) < count) {
					map[i] = Terrain.EMPTY_DECO;
				}
				
			} else
			if (map[i] == Terrain.WALL && 
				map[i-1] != Terrain.WALL_DECO && map[i-WIDTH] != Terrain.WALL_DECO && 
				Random.Int( 20 ) == 0) {
				
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
        return HallsLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return HallsLevel.tileDescs(tile);
    }

	public static String tileNames( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return "冰冷岩浆";
			case Terrain.GRASS:
				return "灰烬苔藓";
			case Terrain.HIGH_GRASS:
				return "灰烬菌菇";
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return "台柱";
			default:
				return Level.tileNames(tile);
		}
	}

	public static String tileDescs(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return "看起来像是岩浆，但是摸起来却是冰凉的。";
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return "这个柱子由类人生物头骨垒成。真帅。";
			case Terrain.BOOKSHELF:
				return "用远古语言写就的书籍堆积在书架里。里面是否隐藏了一些秘密？";
			case Terrain.SHELF_EMPTY:
				return "用远古语言写就的书籍堆积在书架里。";
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
			if (level.map[i] == 63) {
				scene.add( new Stream( i ) );
			}
		}
	}
	
	private static class Stream extends Group {
		
		private int pos;
		
		private float delay;
		
		public Stream( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( 2 );
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((FireParticle)recycle( FireParticle.class )).reset( 
						p.x + Random.Float( DungeonTilemap.SIZE ), 
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
		
		@Override
		public void draw() {
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
			super.draw();
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		}
	}
	
	public static class FireParticle extends PixelParticle.Shrinking {
		
		public FireParticle() {
			super();
			
			color( 0xEE7722 );
			lifespan = 1f;
			
			acc.set( 0, +80 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			speed.set( 0, -40 );
			size = 4;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}
}
