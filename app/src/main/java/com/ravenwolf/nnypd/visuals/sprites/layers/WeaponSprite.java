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
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;


public class WeaponSprite extends EquipmentSprite {

    //public static final int KNUCKLEDUSTER	    = -1;
    public static final int DAGGER			    = 0;
    public static final int ASSASSIN_BLADE    	= 1;
    public static final int OBSIDIAN_BLADE   	= 2;
    public static final int DEERHORN_BLADE    	= 3;
    public static final int KATAR           	= 4;
    public static final int GLADIUS             = 5;
    public static final int SWORD			    = 6;
    public static final int KATANA			    = 7;
    public static final int SCIMITAR    	    = 8;
    public static final int KOPESH    	        = 9;
    public static final int MAGE_STAFF          = 10;

    public static final int MACE			    = 11;
    public static final int HAND_AXE    	    = 12;
    public static final int BATTLE_AXE		    = 13;
    public static final int LONG_STAFF    	    = 14;
    public static final int SPEAR			    = 15;
    public static final int GLAIVE			    = 16;
    public static final int HALBERD             = 17;

    public static final int GREAT_AXE		    = 18;
    public static final int WARHAMMER           = 19;
    public static final int GREATSWORD          = 20;
    public static final int FLAIL   	        = 21;

    public static final int DOUBLE_BLADE   	    = 23;


    private static final int WEAP_FRAME_HEIGHT	= 19;
    private static final int WEAP_FRAME_WIDTH	= 20;


    public WeaponSprite(CharSprite parentSprite){
        super.init(parentSprite,"weapons.png",WEAP_FRAME_HEIGHT);
    }

    protected  int getWidth(){
        return WEAP_FRAME_WIDTH;
    }
    protected  int getHeight(){
        return WEAP_FRAME_HEIGHT;
    }

    protected int[][] getDrawData(int animationId, EquipableItem item){

        if (item instanceof MeleeWeapon)
            return item.getDrawData(animationId);
        return null;
    }

}
