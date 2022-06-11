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

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.watabou.gltextures.Gradient;
import com.watabou.gltextures.SmartTexture;
import com.watabou.noosa.Game;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Flare extends Visual {
	
	private float duration = 0;
	private float lifespan;
	
	private boolean lightMode = true;
	
	private SmartTexture texture;
	
	private FloatBuffer vertices;
	private ShortBuffer indices;

	public static float NO_ANGLE=999f;

	private boolean blink;
	private float maxAngle=NO_ANGLE;
	private float minAngle=NO_ANGLE;
	
	private int nRays;

	@SuppressLint("FloatMath")
	public Flare( int nRays, float radius ) {
		this(nRays,radius, false, false, NO_ANGLE);
	}

	@SuppressLint("FloatMath")
	public Flare( int nRays, float radius, boolean blink, boolean thinRays, float angle) {
		
		super( 0, 0, 0, 0 );
		this.blink=blink;
		int gradient[] = {0xFFFFFFFF, 0x00FFFFFF};
		texture = new Gradient( gradient );
		
		this.nRays = nRays;

		if (angle!=NO_ANGLE){
			minAngle=(float)Math.toDegrees(angle)-45f;
			this.angle=minAngle;
			maxAngle=minAngle+30;
		}
		//angle = 45;
		angularSpeed = 180;
		
		vertices = ByteBuffer.
			allocateDirect( (nRays * 2 + 1) * 4 * (Float.SIZE / 8) ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
		
		indices = ByteBuffer.
			allocateDirect( nRays * 3 * Short.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asShortBuffer();
		
		float v[] = new float[4];
		
		v[0] = 0;
		v[1] = 0;
		v[2] = 0.2f;
		v[3] = 0;
		vertices.put( v );
		
		v[2] = 0.75f;
		v[3] = 0;
		
		for (int i=0; i < nRays; i++) {
			final float piX2=3.1415926f * 2;
			
			float a = i * piX2 / nRays;
			//if (a<3) {bottom half
			//GLog.i(a+"");
			//GLog.i((a+0.2f)+" "+(piX2/8)*7);
			float auxRadius=radius;
			if (blink && i % 2==0)
				auxRadius*=1.3;

			if (minAngle==NO_ANGLE  ||  a<piX2/4 ) {
				v[0] = (float) Math.cos(a) * auxRadius;
				v[1] = (float) Math.sin(a) * auxRadius;
				vertices.put(v);

				a += thinRays ? 0.075f : piX2 / nRays / 2;
				v[0] = (float) Math.cos(a) * auxRadius;
				v[1] = (float) Math.sin(a) * auxRadius;
				vertices.put(v);

				indices.put((short) 0);
				indices.put((short) (1 + i * 2));
				indices.put((short) (2 + i * 2));
			}
		}
		
		indices.position( 0 );
	}
	
	public Flare color( int color, boolean lightMode ) {
		this.lightMode = lightMode;
		hardlight( color );
		
		return this;
	}
	
	public Flare show( Visual visual, float duration ) {
        point( visual.center() );
        visual.parent.addToBack( this );

        lifespan = this.duration = duration;

        return this;
    }

    public Flare show(int cell, float duration ) {

        point( DungeonTilemap.tileCenterToWorld( cell ) );
        lifespan = this.duration = duration;
        GameScene.effect( this );

        return this;
    }
	
	@Override
	public void update() {
		super.update();
		
		if (duration > 0) {
			if ((lifespan -= Game.elapsed) > 0) {
				if (angle>maxAngle)
					angle=minAngle;

				float p = 1 - lifespan / duration;	// 0 -> 1
				p =  p < 0.25f ? p * 4 : (1 - p) * 1.333f;
				if (blink) {
					float blinkAlpha = lifespan % 0.25f;
					scale.set(Math.max(0, p - blinkAlpha * 4));
					alpha(p + blinkAlpha * 8);
				}else{
					scale.set(p);
					alpha(p);
				}
			} else {
				killAndErase();
			}
		}
	}
	
	@Override
	public void draw() {
		
		super.draw();
		
		if (lightMode) {
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
			drawRays();
			GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		} else {
			drawRays();
		}
	}
	
	private void drawRays() {
		
		NoosaScript script = NoosaScript.get();
		
		texture.bind();
		
		script.uModel.valueM4( matrix );
		script.lighting( 
			rm, gm, bm, am, 
			ra, ga, ba, aa );
		
		script.camera( camera );
		script.drawElements( vertices, indices, nRays * 3 );
	}
}
