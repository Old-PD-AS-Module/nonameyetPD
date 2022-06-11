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

import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Mending;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.utils.Random;

public class PotionOfMending extends Potion {

    public static final float DURATION	= 15f;
    public static final float MODIFIER	= 1.0f;

	{
		name = "治疗药剂";
        shortName = "Me";
		icon=2;
	}
	
	@Override
	protected void apply( Hero hero ) {

        hero.heal( hero.HT / 4 + ( hero.HT % 4 > Random.Int(4) ? 1 : 0 ) );

        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 5);

        BuffActive.add( hero, Mending.class, DURATION + alchemySkill() * MODIFIER );
        setKnown();
    }

	@Override
	public String desc() {
		return
				"当饮用这瓶药剂后，会大幅提升饮用者的自然恢复速度，并清除大多数的异常状态";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 30 * quantity : super.price();
	}
}
