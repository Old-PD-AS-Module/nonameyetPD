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
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.Icons;

public class Backpack extends Bag {
	{
//        name = "backpack";
        name = "背包";
        image = ItemSpriteSheet.BACKPACK;

        size = 19;
	}

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//
//        actions.remove( AC_THROW );
//        actions.remove( AC_DROP );
//
//        return actions;
//    }

    @Override
    public Icons icon() {
        return Icons.BACKPACK;
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
//    public String info() {
//        return "That's your backpack. Everything useful goes in here.";
//    }
    public String info() {
        return "这是你的背包，所有物品都储存在这里面。";
    }


    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( Backpack.class ) == null && super.doPickUp( hero ) ;

    }
}
