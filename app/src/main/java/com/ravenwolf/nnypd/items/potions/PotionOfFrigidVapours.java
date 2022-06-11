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
package com.ravenwolf.nnypd.items.potions;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.FrigidVapours;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;

public class PotionOfFrigidVapours extends Potion {

    public static final int BASE_VAL	= 200;
    public static final int MODIFIER	= 15;
	
	{
        name = "冰霜药剂";
        shortName = "Fr";
        harmful = true;
        icon=1;
	}
	
	@Override
	public void shatter( int cell ) {

        GameScene.add( Blob.seed( cell, BASE_VAL + MODIFIER * alchemySkill(), FrigidVapours.class ) );

        if (Dungeon.visible[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

	}
	
	@Override
	public String desc() {
        return
                "这瓶药剂接触空气时会立即挥发并在周围凝结一片冷气，任何被冷气包裹的角色都会被冻结在原地无法行动。.";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 30 * quantity : super.price();
    }
}
