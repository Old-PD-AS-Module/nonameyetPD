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
import com.ravenwolf.nnypd.items.potions.Potion;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.Icons;

public class PotionSash extends Bag{

    {
//        name = "potion belt";
        name = "药剂挎带";
        image = ItemSpriteSheet.BELT;

        size = 14;
        visible = false;
        unique = true;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Potion;
    }

    @Override
    public Icons icon() {
        return Icons.POTION_BELT;
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
/*    public String info() {
        return
            "You can store a significant number of potions in the curiously made containers " +
            "which go around this wondrous sash. It would not only save room in your backpack, " +
            "but also protect these potions from breaking.";
    }*/
    public String info() {
        return
                "你可以把大量的药剂存放到这个特殊的跨带上。不仅能节省你的背包空间，还可以保护挎带内的药剂不受外部环境的影响。";
    }

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( PotionSash.class ) == null && super.doPickUp( hero ) ;

    }
}
