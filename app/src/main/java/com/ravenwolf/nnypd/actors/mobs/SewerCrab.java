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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.actors.mobs.npcs.Ghost;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.visuals.sprites.CrabSprite;

public class SewerCrab extends MobHealthy {

    public SewerCrab() {

        super( 4 );

		name = "下水道螃蟹";
		spriteClass = CrabSprite.class;
		
		loot = new MysteryMeat();
		lootChance = 0.25f;

	}

	@Override
	public String getTribe() {
		return TRIBE_ACUATIC;
	}

    @Override
    public float moveSpeed() {
        //return state == HUNTING || state == FLEEING ? super.moveSpeed() * 2.0f: super.moveSpeed() ;
		return state == HUNTING || state == FLEEING ? super.moveSpeed() * 2f: super.moveSpeed() ;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}


	@Override
	public String description() {
//		return
//			"These huge crabs are at the top of the food chain in the sewers. " +
//			"They are extremely fast and their thick exoskeleton can withstand " +
//			"heavy blows.";

		return "这些巨大的螃蟹位于下水道食物链的顶端。"

				+ ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
				"尽管它们的甲壳很厚，但只要它们愿意，依旧可以进行快速的移动。" : "" )

				+ ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
				"曾经有人假设，如果不加以控制，它们可能会在一座城市地下形成庞大的群体" : "" )

				+ ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
				"如果你不想缺胳膊断腿的话，最好小心点。" : "" )

				+ ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
				"它们的巨钳甚至能够能击穿金属护甲，外表的硬壳似乎坚不可摧，被注意到的话它们就会把你当做下一个猎物。" : "" )

				;
	}
}
