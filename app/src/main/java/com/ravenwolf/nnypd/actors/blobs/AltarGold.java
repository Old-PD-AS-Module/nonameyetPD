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
package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.Journal.Feature;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.windows.WndBag;

public class AltarGold extends AltarPower {


//    private static final String TXT_PROCEED	= "Your %s have tuned into %dg";
    private static final String TXT_PROCEED	= "您的%s已调整为%dg";


    @Override
    protected void evolve() {
        super.evolve();

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.ALTAR_OF_TRANSMUTATION);
        }
    }
/*
    public  String getDescription( ){
        return "Select an item to transmute in gold";
    }
*/
    public WndBag.Mode getBagMode( ) {
        return WndBag.Mode.FOR_SALE;
    }

    public Item affectItem( Item item ) {

        sell(item );
        CellEmitter.get(pos).burst( Speck.factory( Speck.COIN ), 10);

        volume = off[pos] = cur[pos] -= 1;
        if (cur[pos] <= 0) {
            GLog.i(AltarPower.TXT_NO_MORE_POWER);
            Journal.remove(Feature.ALTAR_OF_TRANSMUTATION);
        }
        return item;
    }


    private void sell( Item item ) {
        Hero hero = Dungeon.hero;
        if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false, false )) {
            return;
        }

        item.detachAll( hero.belongings.backpack );

        //calculates real price
        int oldKnowValue = item.known;
        item.known = Item.UPGRADE_KNOWN;
        int price = item.price();
        item.known = oldKnowValue;

        price+= price/2;//increase price 50%

        new Gold( price ).doPickUp( hero );
        GLog.i( TXT_PROCEED, item.name(), price );
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);

        emitter.pour(  Speck.factory( Speck.COIN ), 0.4f );
    }

    @Override
    public String tileDesc() {
        return
                /*"Power of transmutation radiates from this altar. " +
                        "Offer an item to transmute it into gold.";*/
                "这座祭坛散发出嬗变的力量。" +
                        "提供一件物品将其转化为黄金。";
    }
}
