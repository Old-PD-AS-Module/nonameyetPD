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
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.windows.WndBag;

public class AltarUncurse extends AltarPower {

    private static final String TXT_PROCCED =
//            "Your item has been uncursed.";
            "你的物品已经变回了初始的样子";

    private static final String TXT_NOT_CURSED =
//            "Your item was free of malevolent magic.";
            "你物品上的邪恶魔法消失了";

    @Override
    protected void evolve() {
        super.evolve();

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.ALTAR_OF_TRANSMUTATION);
        }
    }
/*
    public  String getDescription( ){
        return "Select an item to cleanse";
    }
*/
    public WndBag.Mode getBagMode( ) {
        return WndBag.Mode.CURSED;
    }

    public Item affectItem( Item item ) {

        item.identify(Item.CURSED_KNOWN);
        if (item.isCursed()) {
            item.uncurse();
            GLog.i(TXT_PROCCED);
            CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
        }else{
            CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 4);
            GLog.i(TXT_NOT_CURSED);
        }
        volume = off[pos] = cur[pos] -= 1;
        if (cur[pos] <= 0) {
            GLog.i(AltarPower.TXT_NO_MORE_POWER);
            Journal.remove(Feature.ALTAR_OF_TRANSMUTATION);
        }
        return item;
    }


/*
    public static void affectCell( int cell ) {

        AltarUncurse water = (AltarUncurse)Dungeon.level.blobs.get( AltarUncurse.class );
        if (water != null &&
                water.volume > 0 &&
                water.pos == cell &&
                water.affect()) {

            //CellEmitter.center( cell ).burst( AltarParticle.FACTORY, Random.IntRange( 6, 10 ) );

        }
    }

    protected boolean affect() {
        if (pos == Dungeon.hero.pos) {
            if (Ankh.uncurse( Dungeon.hero,
                    Dungeon.hero.belongings.weap1,
                    Dungeon.hero.belongings.weap2,
                    Dungeon.hero.belongings.armor,
                    Dungeon.hero.belongings.ring1,
                    Dungeon.hero.belongings.ring2
            )){
                volume = off[pos] = cur[pos] =0;
                GLog.i(TXT_PROCCED);
                CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
                GLog.i(AltarPower.TXT_NO_MORE_POWER);
                Journal.remove(Feature.ALTAR_OF_CLEANSING);
                return true;
            }
        }else {
            Heap heap;
            if ((heap = Dungeon.level.heaps.get(pos)) != null) {
                Item item = heap.peek();
                if (item.isCursedKnown()) {
                    item.identify(Item.CURSED_KNOWN);
                    item.uncurse();

                    volume = off[pos] = cur[pos] -= 1;

                    GLog.i(TXT_PROCCED);

                    CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
                   // hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );

                    if (cur[pos] <= 0) {
                        GLog.i(AltarPower.TXT_NO_MORE_POWER);
                        Journal.remove(Feature.ALTAR_OF_CLEANSING);
                        return true;
                    }
                }
                //throw item away
                int n;
                do {
                    n = pos + Level.NEIGHBOURS8[Random.Int(8)];
                } while (!Level.passable[n] && !Level.avoid[n]);
                Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
            }
        }
        return false;
    }
*/
    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);

        emitter.pour(  Speck.factory( Speck.BANISH ), 0.6f );
    }

    @Override
    public String tileDesc() {
        return
/*                "A blessed altar " +
                        "There is still power to remove curses from items.";
        */
                "祭坛 " +
                        "这儿有一种平静的力量可以把物品的诅咒祛除";

    }
}
