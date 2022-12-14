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
package com.ravenwolf.nnypd.visuals.effects;

import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.visuals.effects.particles.AcidParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.AltarParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.FlameParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.LeafParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.PoisonParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.WoolParticle;
import com.ravenwolf.nnypd.visuals.sprites.GooSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MagicMissile extends Emitter {

	private static final float SPEED	= 200f;
	
	private Callback callback;
	
	private float sx;
	private float sy;
	private float time;
	
	public void reset( int from, int to, Callback callback ) {
		reset(from, to, SPEED, callback);
	}

	public void reset( int from, int to, float speed, Callback callback ) {
		this.callback = callback;

		revive();

		PointF pf = DungeonTilemap.tileCenterToWorld( from );
		PointF pt = DungeonTilemap.tileCenterToWorld( to );

		x = pf.x;
		y = pf.y;
		width = 0;
		height = 0;

		PointF d = PointF.diff( pt, pf );
		PointF pointSpeed = new PointF( d ).normalize().scale( speed );
		sx = pointSpeed.x;
		sy = pointSpeed.y;
		time = d.length() / speed;
	}
	
	public void size( float size ) {
		x -= size / 2;
		y -= size / 2;
		width = height = size;
	}
	
	public static void blueLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset(from, to, callback);
        missile.size( 5 );
		missile.pour( MagicParticle.FACTORY, 0.02f );
	}
	
	public static void fire( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size(4);
		missile.pour( FlameParticle.FACTORY, 0.01f );
	}
	
	public static void earth( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 2 );
		missile.pour( EarthParticle.FACTORY, 0.01f );
	}
	
	public static void purpleLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 2 );
		missile.pour( PurpleParticle.MISSILE, 0.01f );
	}
	
	public static void whiteLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( WhiteParticle.FACTORY, 0.01f );
	}
	
	public static void wool( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 3 );
		missile.pour( WoolParticle.FACTORY, 0.01f );
	}

	public static void bless( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 6 );
		missile.pour( BlessParticle.FACTORY, 0.01f );
	}

	public static void acid( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 9 );
		missile.pour( AcidParticle.FACTORY, 0.01f );
	}
	
	public static void poison( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 3 );
		missile.pour( PoisonParticle.MISSILE, 0.01f );
	}
	
	public static void foliage( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( LeafParticle.GENERAL, 0.01f );
	}
	
	public static void slowness( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset(from, to, callback);
		missile.pour( SlowParticle.FACTORY, 0.01f );
	}
	
	public static void force( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size(4);
		missile.pour( ForceParticle.FACTORY, 0.01f );
	}
	
	public static void coldLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( ColdParticle.FACTORY, 0.01f );
	}

    public static void shadow( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
        missile.reset( from, to, callback );
        missile.size( 4 );
        missile.pour( ShadowParticle.MISSILE, 0.01f );
    }

	public static void miasma( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( GooSprite.GooParticle.FACTORY, 0.01f );
	}

    public static void frost( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
        missile.reset( from, to, callback );
        missile.size( 5 );
        //missile.pour( SnowParticle.FACTORY, 0.005f );
		missile.pour( Speck.factory(Speck.FROST), 0.02f );
    }

	public static void blast( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 7 );
		missile.pour( Speck.factory(Speck.BLAST), 0.02f );
	}

	public static void fellFire( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 7 );
		missile.pour( ElmoParticle.FACTORY, 0.02f );
	}

    public static void web( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
        missile.reset( from, to, callback );
        missile.size( 5 );
        missile.pour( Speck.factory(Speck.COBWEB), 0.05f );
    }

    public static void bones( Group group, int from, int to, Callback callback ) {
        MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
        missile.reset( from, to, callback );
        missile.size( 5 );
        missile.pour( Speck.factory(Speck.BONE), 0.02f );
    }

	public static void blueFire( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, 400f, callback );
		missile.size( 6 );
		missile.pour(AltarParticle.FACTORY, 0.005f );
	}
	
	@Override
	public void update() {
		super.update();
		if (on) {
			float d = Game.elapsed;
			x += sx * d;
			y += sy * d;
			if ((time -= d) <= 0) {
				on = false;

                if( callback != null ) {
                    callback.call();
                }
			}
		}
	}
	
	public static class MagicParticle extends PixelParticle {
		
//		public static final Emitter.Factory FACTORY = Speck.factory( Speck.DISCOVER );
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
//			public void emit( Emitter emitter, int index, float x, float y ) {
//				((MagicParticle)emitter.recycle( MagicParticle.class )).fail( x, y );
//			}

            public void emit ( Emitter emitter, int index, float x, float y ) {
                ((Speck)emitter.recycle( Speck.class )).reset( index, x, y, Speck.DISCOVER );
            }
			@Override
			public boolean lightMode() {
				return true;
			}
};
		
		public MagicParticle() {
			super();
			
			color( 0x88CCFF );
			lifespan = 0.5f;
			
			speed.set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
		}
		
		@Override
		public void update() {
			super.update();
			// alpha: 1 -> 0; size: 1 -> 4
			size( 4 - (am = left / lifespan) * 3 );
		}
	}

	public static class EarthParticle extends PixelParticle.Shrinking {
		
		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((EarthParticle)emitter.recycle( EarthParticle.class )).reset( x, y );
			}
		};

		public static final Emitter.Factory FALLING = new Emitter.Factory() {
			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				((EarthParticle) emitter.recycle(EarthParticle.class)).resetFalling(x, y);
			}
		};


		public EarthParticle() {
			super();
			
			lifespan = 0.5f;
			
			color( ColorMath.random( 0x555555, 0x777766 ) );
			
			acc.set( 0, +40 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			size = 4;
			
			speed.set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
		}

		public void resetFalling(float f, float f2) {
			reset(f, f2 - 6.0f);
			this.lifespan = 0.8f;
			this.left = 0.8f;
			this.size = 6.0f;
			this.acc.y = 25.0f;
			this.speed.y = -6.0f;
			this.angularSpeed = Random.Float(-90.0f, 90.0f);
		}

	}

	public static class BlessParticle extends PixelParticle {

		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((BlessParticle)emitter.recycle( BlessParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
		};

		public BlessParticle() {
			super();

			color( ColorMath.random( 0xEEE577, 0xFFF799 ) );
			//color( ColorMath.random( 0xFFFFFF, 0xFFF799 ) );
			lifespan = 0.6f;

			am = 0.5f;
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;
		}

		@Override
		public void update() {
			super.update();
			// size: 3 -> 0
			speed.set( Random.Float( -20, +20 ), Random.Float( -20, +20 ) );
			size( (left / lifespan) * 3 );
		}
	}
	
	public static class WhiteParticle extends PixelParticle {
		
		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((WhiteParticle)emitter.recycle( WhiteParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
        };
		
		public WhiteParticle() {
			super();
			
			lifespan = 0.4f;
			
			am = 0.5f;
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
		}
		
		@Override
		public void update() {
			super.update();
			// size: 3 -> 0
			size( (left / lifespan) * 3 );
		}
	}

	public static class SlowParticle extends PixelParticle {
		
		private Emitter emitter;
		
		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((SlowParticle)emitter.recycle( SlowParticle.class )).reset( x, y, emitter );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
        };
		
		public SlowParticle() {
			super();
			
			lifespan = 0.6f;
			
			color( 0x664422 );
			size( 2 );
		}
		
		public void reset( float x, float y, Emitter emitter ) {
			revive();
			
			this.x = x;
			this.y = y;
			this.emitter = emitter;
			
			left = lifespan;
			
			acc.set( 0 );
			//speed.set( Random.Float( -20, +20 ), Random.Float( -20, +20 ) );
			speed.set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
		}
		
		@Override
		public void update() {
			super.update();
			
			am = left / lifespan;
			acc.set( (emitter.x - x), (emitter.y - y) );
			//acc.set( (emitter.x - x) * 10, (emitter.y - y) * 10 );
		}
	}
	
	public static class ForceParticle extends PixelParticle {
		
		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((ForceParticle)emitter.recycle( ForceParticle.class )).reset( x, y );
			}
		};
		
		public ForceParticle() {
			super();
			
			lifespan = 0.6f;

			size( 4 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			acc.set( 0 );
			speed.set( Random.Float( -40, +40 ), Random.Float( -40, +40 ) );
		}
		
		@Override
		public void update() {
			super.update();
			
			am = (left / lifespan) / 2;
			acc.set( -speed.x * 10, -speed.y * 10 );
		}
	}
	
	public static class ColdParticle extends PixelParticle.Shrinking {
		
		public static final Emitter.Factory FACTORY = new Factory() {	
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((ColdParticle)emitter.recycle( ColdParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			}
        };
		
		public ColdParticle() {
			super();
			
			lifespan = 0.6f;
			
			//color( 0x2244FF );
			color( 0x00AA66 );

		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			size = 8;
		}
		
		@Override
		public void update() {
			super.update();
			
			am = 1 - left / lifespan;
		}
	}
}
