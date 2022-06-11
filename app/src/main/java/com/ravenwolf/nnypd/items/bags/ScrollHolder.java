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
import com.ravenwolf.nnypd.items.scrolls.Scroll;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.Icons;

public class ScrollHolder extends Bag {

	{
//		name = "scroll holder";
		name = "卷轴筒";
		image = ItemSpriteSheet.HOLDER;
		
		size = 14;
        visible = false;
        unique = true;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Scroll;
	}

    @Override
    public Icons icon() {
        return Icons.SCROLL_HOLDER;
    }
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
/*	public String info() {
		return
			"You can place any number of scrolls into this tubular container. " +
			"It saves room in your backpack and protects scrolls from fire.";
	}*/
	public String info() {
		return
				"你可以向这个管状容器内放入任意数量的卷轴。这个容器不仅能节省你的背包空间还能保护里面的卷轴不被点燃。";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( ScrollHolder.class ) == null && super.doPickUp( hero ) ;

    }
}
