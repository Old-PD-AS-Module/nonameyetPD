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
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;


public class AdditionalSprite extends EquipmentSprite {


    public static final int ADD_QUIVER  	    = 1;
    public static final int ADD_OBSIDIAN		= 2;
    public static final int ADD_DEERHORN		= 3;

    private static final int WEAP_FRAME_HEIGHT	= 11;
    private static final int WEAP_FRAME_WIDTH	= 12;


    //quiver animations
    private final int[][] bowIddle = {	{0, 0, 0, 0, 0, 0, 0, 0 },	//frame
            {-3, -3, -3, -3, -3, -3, -3, -3 },	//x
            {0, 0, 0, 0, 0, 0, 0, 0}};//y

    private final int[][] bowAtk = {	{0, 0, 0, 0, 0, 0 },	//frame
            {-4, -3, -2, -3, -3, -3 },	//x
            {0, 0, 0, 0, 0, 0}};//y

    private final int[][] bowRun = {	{0, 0, 0, 0, 0, 0  },	//frame
            {-3, -3, -3, -3, -3, -3  },	//x
            {0, 0, 1, 0, 0, 0 }};//y

    //offhand weapon animations
    private final int[][] dualIddle = {	{0, 0, 0, 0, 0, 0, 0, 0 },	//frame
            {6, 6, 6, 6, 6, 6, 6, 6 },	//x
            {4, 4, 4, 4, 4, 4, 4, 4}};//y

    private final int[][] dualFly = {	{0 },	//frame
            {7},	//x
            {4}};//y

    private final int[][] dualAtk = {	{1, 1, 0, 0 },	//frame
            {7, 7, 4, 6 },	//x
            {3, 3, 4, 4}};//y

    private final int[][] dualRun = {	{0, 0, 0, 0, 1, 1  },	//frame
            {5, 4, 5, 6, 7, 7  },	//x
            {4, 4, 4, 4, 3, 3 }};//y

    public AdditionalSprite(CharSprite parentSprite){
        super.init(parentSprite,"weapons_additional.png",WEAP_FRAME_HEIGHT);
    }


    protected  int getWidth(){
        return WEAP_FRAME_WIDTH;

    }
    protected  int getHeight(){
        return WEAP_FRAME_HEIGHT;
    }


    protected int[][] getDrawData(int animationId, EquipableItem item){

        if (item!=null && item.getAdditionalDrawId()!=-1) {
            if (item instanceof RangedWeapon) {
                if (animationId == HeroSprite.ANIM_RANGED_ATTACK)
                    return bowAtk;
                if (animationId == HeroSprite.ANIM_RUN)
                    return bowRun;
                return bowIddle;
            } else {
                if (animationId == HeroSprite.ANIM_ATTACK)
                    return dualAtk;
                if (animationId == HeroSprite.ANIM_RUN)
                    return dualRun;
                if (animationId == HeroSprite.ANIM_IDLE)
                    return dualIddle;
                if (animationId == HeroSprite.ANIM_FLY)
                    return dualFly;
                return null;
            }
        } else
            return null;
    }

}
