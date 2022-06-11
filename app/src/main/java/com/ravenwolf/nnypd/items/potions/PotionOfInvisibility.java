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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.hero.Hero;

public class PotionOfInvisibility extends Potion {

    public static final float DURATION	= 10f;
    public static final float MODIFIER	= 1.0f;

	{
		name = "隐形药剂";
        shortName = "In";
		icon=3;
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Invisibility.class, DURATION + alchemySkill() * MODIFIER );
        setKnown();
    }
	
	@Override
	public String desc() {
		return
				"饮用这瓶药剂会使你暂时的进入隐身状态，敌人看不到隐身的你，但是它们依旧是到处寻找直至发现你，实施攻击，阅读卷轴，释放法杖都会使药剂的效果消失";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 40 * quantity : super.price();
    }

}
