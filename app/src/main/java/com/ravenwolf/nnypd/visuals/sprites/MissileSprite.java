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
package com.ravenwolf.nnypd.visuals.sprites;

import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.items.Item;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED = 300f;//360f;
	
	private Callback callback;
	
	public MissileSprite() {
		super();
		originToCenter();
	}

    public void reset( int from, int to, int image, Callback listener ) {
            reset( from, to, image, 1.0f, null, listener );
    }

    public void reset( int from, int to, Item item, Callback listener ) {
        if (item == null) {
            reset( from, to, 0, 1.0f, null, listener );
        } else {
            reset( from, to, item.imageAlt(), 1.0f, item.glowing(), listener );
        }
    }

	public void reset( int from, int to, Item item, float speed_modifier, Callback listener ) {
		if (item == null) {
			reset( from, to, 0, speed_modifier, null, listener );
		} else {
			reset( from, to, item.imageAlt(), speed_modifier, item.glowing(), listener );
		}
	}

	public void reset( int from, int to, int image, float speed_modifier, Glowing glowing, Callback listener ) {
		revive();
		
		view( image, glowing );
		
		this.callback = listener;

		point( DungeonTilemap.tileToWorld( from ) );
		PointF dest = DungeonTilemap.tileToWorld( to );
		
		PointF d = PointF.diff( dest, point() );

		speed.set( d ).normalize().scale( SPEED /** speed_modifier*/ );

        scale.x = 0.8f;
        scale.y = 0.8f;

        flipVertical=false;
		flipHorizontal=false;

        alpha(0.9f);

        // FIXME
		if (image == ItemSpriteSheet.THROWING_SPEAR){
			scale.x = 1.2f;
			scale.y = 1.2f;
		}
		if (
			image == ItemSpriteSheet.BOOMERANG
			|| image == ItemSpriteSheet.HUNTING_BOLAS
			|| image == ItemSpriteSheet.THROWING_STAR
			|| image == ItemSpriteSheet.CHAKRAM
			|| image == ItemSpriteSheet.MOON_GLAIVE
			) {

			angularSpeed = 1440;

		} else

		if (
            image == ItemSpriteSheet.THROWING_DART
            || image == ItemSpriteSheet.THROWING_KNIFE
            || image == ItemSpriteSheet.JAVELIN
//            || image == ItemSpriteSheet.HARPOON
            || image == ItemSpriteSheet.HARPOON_THROWN
            || image == ItemSpriteSheet.HARPOON_RETURN
            || image == ItemSpriteSheet.ARROW
					|| image == ItemSpriteSheet.BLUNTED_ARROW
            || image == ItemSpriteSheet.QUARREL
			|| image == ItemSpriteSheet.THROWING_SPEAR
			|| image == ItemSpriteSheet.SPECTRAL_BLADE
        ) {
			angularSpeed = 0;
			angle = ( image != ItemSpriteSheet.HARPOON_RETURN ? 135 : -45 ) - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);
		} else {
			angularSpeed = 720;
		}

		//wavy effect when throw
		if(image == ItemSpriteSheet.MOON_GLAIVE) {
			origin.x = 10;
			origin.y = 10;
		}else{
			origin.x = SIZE/2;
			origin.y = SIZE/2;
		}

		PosTweener tweener = new PosTweener( this, dest, d.length() / (SPEED * speed_modifier) );
		tweener.listener = this;
		parent.add( tweener );
	}

	@Override
	public void onComplete( Tweener tweener ) {
		kill();
		if (callback != null) {
			callback.call();
		}
	}


	public void spin( int pos, int image, int rotation_speed, Glowing glowing) {
		revive();

		PointF pointPos=DungeonTilemap.tileToWorld( pos );
		pointPos.y-=8;
		pointPos.x-=rotation_speed>0?SIZE/4:SIZE-SIZE/4;
		point( pointPos );
		view( image, glowing );

		origin.x=SIZE;
		origin.y=SIZE;

		scale.x = 0.8f;
		scale.y = 0.8f;

		alpha(0.9f);

		angularSpeed = rotation_speed;

	}
}
