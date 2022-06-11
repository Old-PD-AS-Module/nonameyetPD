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
package com.ravenwolf.nnypd.actors.mobs.npcs;

import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.visuals.sprites.ShopkeeperPlagueDocSprite;

public class ShopkeeperPagueDoc extends Shopkeeper {

	private static final String TXT_GREETINGS = "有什么需要帮忙的吗？";
	
	{
		name = "瘟疫商人";
		spriteClass = ShopkeeperPlagueDocSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public String description() {
		return 
			/*"Hidden under a beak mask, you cant tell if this plague doctor has been corrupted by the plague or is still sane. " +
            "the hat he wears makes him look friendly, though ";*/
				"俗话说，哪里有瘟疫，哪里就有瘟医，哪里就会有死亡。不过，你并不知道这个瘟医为什么在这里卖东西。但至少他看起来并没有受到瘟疫的影响";
	}
}
