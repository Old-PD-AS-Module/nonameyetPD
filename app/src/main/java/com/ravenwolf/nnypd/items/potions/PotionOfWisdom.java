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
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.rings.RingOfKnowledge;
import com.watabou.utils.Random;

public class PotionOfWisdom extends Potion {

	{
		name = "经验药剂";
        shortName = "Wi";
		icon=0;
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();

        int exp = hero.maxExp();

        float bonus = Dungeon.hero.ringBuffs(RingOfKnowledge.Knowledge.class) * exp - exp;

        exp += (int)bonus;
        exp += Random.Float() < bonus % 1 ? 1 : 0 ;

		hero.earnExp( exp );
        hero.lvlBonus++;
	}
	
	@Override
	public String desc() {
		return
				"众多战斗积累而来的经验被浓缩为液态，这种药剂能够瞬间提升你的等级";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 100 * quantity : super.price();
	}
}
