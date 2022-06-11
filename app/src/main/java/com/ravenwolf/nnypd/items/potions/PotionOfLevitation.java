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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Levitation;
import com.ravenwolf.nnypd.actors.hero.Hero;

public class PotionOfLevitation extends Potion {

    public static final float DURATION	= 20f;
    public static final float MODIFIER	= 1.0f;

	{
		name = "浮空药剂";
        shortName = "Le";
		icon=4;
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Levitation.class, DURATION + alchemySkill() * MODIFIER );
        setKnown();
    }
	
	@Override
	public String desc() {
		return
				"饮用这瓶古怪的药剂能让你暂时的你漂浮在低空中，从而可以更快且更隐蔽地移动。漂浮状态下你会直接飞越陷阱与裂缝，且视野不再被高草阻挡"+
						"然而，漂浮依旧无法躲过火焰或气体类充满整个空间的东西";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 30 * quantity : super.price();
    }
}
