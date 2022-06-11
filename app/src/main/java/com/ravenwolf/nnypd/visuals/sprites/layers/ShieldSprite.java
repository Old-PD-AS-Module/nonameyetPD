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
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;


public class ShieldSprite extends EquipmentSprite {

    public static final int SHIELD_SMALL        = 6;
    public static final int SHIELD_ARCANE       = 5;
    public static final int SHIELD_ROUND        = 4;
    public static final int SHIELD_WAR		    = 3;
    public static final int SHIELD_KITE		    = 2;
    public static final int SHIELD_TOWER		= 1;
    public static final int SHIELD_GREAT        = 0;

    private static final int SHIELD_FRAME_HEIGHT	= 13;
    private static final int SHIELD_FRAME_WIDTH	= 4;


    public ShieldSprite(CharSprite parentSprite){
        super.init(parentSprite,"shields.png",SHIELD_FRAME_HEIGHT);
    }

    protected  int getWidth(){
        return SHIELD_FRAME_WIDTH;

    }
    protected  int getHeight(){
        return SHIELD_FRAME_HEIGHT;
    }

    protected int[][] getDrawData(int animationId, EquipableItem item){

        if (item==null || !(item instanceof Shield))
            return null;
        return item.getDrawData(animationId);
    }
}
