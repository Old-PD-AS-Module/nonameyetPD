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
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class PotionOfLiquidFlame extends Potion {

    public static final float BASE_VAL	= 0.25f;
    public static final float MODIFIER	= 0.05f;

	{
        name = "液火药剂";
        shortName = "Li";
        harmful = true;
        icon=5;
	}
	
	@Override
	public void shatter( int cell ) {

		GameScene.add( Blob.seed( cell, 2, Fire.class ) );

        //float chance = BASE_VAL + MODIFIER * alchemySkill();
        float chance = 0.4f;

        for (int n : Level.NEIGHBOURS8) {
            if( Level.flammable[ cell + n ] || !Level.water[ cell + n ] &&
                    Level.passable[ cell + n ] && chance > Random.Float() ) {
                GameScene.add( Blob.seed( cell + n, 2, Fire.class ) );
            }
        }

        if (Dungeon.visible[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }
	}
	
	@Override
	public String desc() {
        return
                "这瓶药剂里装着一种不稳定的化合物，一旦暴露在空气中，就会猛烈地燃烧起来";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 30 * quantity : super.price();
    }
}
