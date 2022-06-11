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
import com.ravenwolf.nnypd.actors.blobs.CorrosiveGas;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class PotionOfCorrosiveGas extends Potion {

    public static final int BASE_VAL	= 300;
    public static final int MODIFIER	= 30;

	{
		name = "酸蚀药剂";
        shortName = "CG";
        harmful = true;
		icon=11;
	}
	
	@Override
	public void shatter( int cell ) {

        GameScene.add( Blob.seed( cell, BASE_VAL + MODIFIER * /*alchemySkill()*/Random.Int(1,10), CorrosiveGas.class ) );

		if (Dungeon.visible[cell]) {
			setKnown();
			
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}
	}
	
	@Override
	public String desc() {
		return
				"打碎这瓶药剂会导致里面的酸蚀性气体产生爆炸，形成一团高度腐蚀且及其易燃的有毒气体。最好把它扔向远处的敌人，而不是自己打开。";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 40 * quantity : super.price();
	}
}
