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
package com.ravenwolf.nnypd.items.bags;

import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.keys.Key;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.Icons;

public class Keyring extends Bag {

	{
//		name = "keys";
		name = "钥匙串";
		image = ItemSpriteSheet.KEYRING;
		
		size = 14;
        visible = false;
        unique = true;
	}

	@Override
	public boolean grab( Item item ) {
		return item instanceof Key;
	}

    @Override
    public Icons icon() {
        return Icons.KEYRING;
    }
	
	@Override
	public int price() {
		return 0;
	}
	
	@Override
/*	public String info() {
		return
			"This is a copper keyring, that lets you keep all your keys " +
			"separately from the rest of your belongings.";
	}*/
	public String info() {
		return
				"这是一个铜制钥匙串，用于存放各种钥匙。";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( Keyring.class ) == null && super.doPickUp( hero ) ;

    }
}
