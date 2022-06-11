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
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.Icons;

public class Baldric extends Bag {

	{
//		name = "Heavy quiver";
		name = "重箭袋";
		image = ItemSpriteSheet.BALDRIC;
		
		size = 13;//14
        visible = false;
        unique = true;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof ThrowingWeapon;
	}
	
	@Override
    public Icons icon() {
        return Icons.BALDRIC;
    }

	@Override
	public int price() {
		return 50;
	}
	
	@Override
/*	public String info() {
		return
			"This leather quiver allows you to store any number of ammunition. Very convenient.";
	}*/
	public String info() {
		return
				"这个皮革箭筒可以让你储存任意数量的弹药。非常方便";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( Baldric.class ) == null && super.doPickUp( hero ) ;

    }
}
