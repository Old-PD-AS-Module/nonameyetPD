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
import com.ravenwolf.nnypd.actors.blobs.Electricity;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;

public class PotionOfSparklingDust extends Potion {

    public static final int BASE_VAL	= 150;
    public static final int MODIFIER	= 15;

	{
		name = "雷暴药剂";
        shortName = "Th";
        harmful = true;
		icon=13;
	}
	
	@Override
	public void shatter( int cell ) {


		Blob blob = Blob.seed( cell, BASE_VAL + MODIFIER * alchemySkill(), Electricity.class );
		GameScene.add( blob );
		if (Dungeon.visible[cell]) {

            setKnown();
            splash(cell);
            Sample.INSTANCE.play(Assets.SND_SHATTER);

        }
	}
	
	@Override
	public String desc() {

		return
				"这瓶药剂中存储着大量的电流，一旦暴露在空气中，它将会产生一个强大的电场，并且会在水面上迅速扩散，伤害任何接触到它的生物";

	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 40 * quantity : super.price();
	}
}
