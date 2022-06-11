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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.watabou.utils.Random;

public class ScrollOfIdentify extends InventoryScroll {

//    private static final String TXT_REVEALED	= "You notice something peculiar!";
//    private static final String TXT_IDENTIFIED	= "Your equipped items are identified!";
//    private static final String TXT_NOTHING 	= "Nothing happens.";

    {
        name = "鉴定卷轴";
        shortName = "Id";

        inventoryTitle = "选择一个要鉴定的物品";
		mode = WndBag.Mode.UNIDENTIFED;

        spellSprite = SpellSprite.SCROLL_IDENTIFY;
        spellColour = SpellSprite.COLOUR_RUNE;
        icon=0;
	}

//    @Override
//    protected void doRead() {
//
//        boolean identified = identify(
//                curUser.belongings.weapon,
//                curUser.belongings.armor,
//                curUser.belongings.ring1,
//                curUser.belongings.ring2
//        );
//
//        boolean revealed = reveal();
//
//        if( identified )
//            GLog.i( TXT_IDENTIFIED );
//
//        if( revealed )
//            GLog.i( TXT_REVEALED );
//
//        if( !revealed && !identified )
//            GLog.i( TXT_NOTHING );
//
//        new Flare( 5, 32 ).color( 0x3399FF, true ).show(curUser.sprite, 2f);
//        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange( 7, 9));
//
//        super.doRead();
//    }

	@Override
    public void onItemSelected(Item item) {
		
//		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange(6, 9));

        item.identify();
        GLog.i("你鉴定出了" + item);

//        Badges.validateItemLevelAcquired(item);

    }
	
	@Override
	public String desc() {
        return
                "阅读这张卷轴上的符文能够赋予阅读者看穿事物本质，揭露其用途和质量的能力。不过卷轴的效果十分短暂，你只能在有限的时间里鉴定出一个道具。";
    }

    public static boolean identify( Item... items ) {

        boolean procced = false;

        for (int i=0; i < items.length; i++) {
            Item item = items[i];
            if (item != null) {

                item.identify();
            }
        }

        return procced;
    }

//    public static boolean reveal() {
//
//        int length = Level.LENGTH;
//        int[] map = Dungeon.level.map;
//        boolean[] visible = Dungeon.visible;
//
//        boolean noticed = false;
//
//        for (int i=0; i < length; i++) {
//
//            int terr = map[i];
//
//            if (visible[i]) {
//
//                if (Dungeon.visible[i] && (Terrain.flags[terr] & Terrain.TRAPPED) != 0) {
//
//                    Level.set( i, Terrain.discover(terr) );
//                    GameScene.updateMap( i );
//
//                    GameScene.discoverTile(i, terr);
//                    CellEmitter.get(i).start(Speck.factory(Speck.DISCOVER), 0.1f, 5);
//
//                    noticed = true;
//                }
//            }
//        }
//        Dungeon.observe();
//
//        if (noticed) {
//            Sample.INSTANCE.play( Assets.SND_SECRET );
//        }
//
//        return noticed;
//    }
	
	@Override
	public int price() {
		return isTypeKnown() ? 30 * quantity : super.price();
	}
}
