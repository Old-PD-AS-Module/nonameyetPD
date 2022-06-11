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

package com.ravenwolf.nnypd.visuals.sprites.layers;


import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;


public class RangedWeaponSprite extends EquipmentSprite {


    public static final int SLING               = 0;
    public static final int BOW        		    = 6;
    public static final int ARBALEST	        = 2;
    public static final int PISTOLE             = 1;
    public static final int ARQUEBUS            = 4;
    public static final int HANDCANNON          = 5;
    public static final int LONG_BOW           	= 7;
    public static final int COMPOSITE_BOW	    = 8;
    public static final int HEAVY_CROSSBOW	    = 3;

    private static final int RNG_WEAP_FRAME_HEIGHT	= 15;
    private static final int RNG_WEAP_FRAME_WIDTH	= 18;


    public RangedWeaponSprite(CharSprite parentSprite){
        super.init( parentSprite,"weapons_ranged.png",RNG_WEAP_FRAME_HEIGHT);
    }


    protected  int getWidth(){
        return RNG_WEAP_FRAME_WIDTH;

    }
    protected  int getHeight(){
        return RNG_WEAP_FRAME_HEIGHT;
    }

    protected int[][] getDrawData(int animationId, EquipableItem item){

        if (item instanceof RangedWeapon)
            return item.getDrawData(animationId);
        else
            return null;
    }

    public void draw(int curFrame){
        //FIXME
        //reimplemented because the offset if different
        // --should update draw data in every ranged weapon
        if (drawData!=null) {
            image.center(parentSprite.center());//centers the image at the hero center
            image.x += (drawData[1][curFrame] -2 )* (image.flipHorizontal ? -1 : 1);
            image.y += drawData[2][curFrame]+1;
            image.alpha(parentSprite.alpha());
            image.ra=parentSprite.ra;
            image.ba=parentSprite.ba;
            image.ga=parentSprite.ga;
            image.draw();
        }

    }
}
