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
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class AltarPower extends Blob {


    protected static final String TXT_NO_MORE_POWER =
//            "The altar runs out of power.";
            "祭坛耗尽了力量";

    protected int pos;

    public static boolean isMagicAltar(int cell){
        AltarKnowledge know = (AltarKnowledge)Dungeon.level.blobs.get( AltarKnowledge.class );
        if (know != null && know.volume > 0 && know.pos == cell){
            return true;
        }
        AltarEnchant ench = (AltarEnchant)Dungeon.level.blobs.get( AltarEnchant.class );
        if (ench != null && ench.volume > 0 && ench.pos == cell){
            return true;
        }
        AltarUncurse uncurse = (AltarUncurse)Dungeon.level.blobs.get( AltarUncurse.class );
        if (uncurse != null && uncurse.volume > 0 && uncurse.pos == cell){
            return true;
        }
        AltarChallenge challenge = (AltarChallenge)Dungeon.level.blobs.get( AltarChallenge.class );
        if (challenge != null && challenge.volume > 0 && challenge.pos == cell){
            return true;
        }
        AltarGold gold = (AltarGold)Dungeon.level.blobs.get( AltarGold.class );
        if (gold != null && gold.volume > 0 && gold.pos == cell){
            return true;
        }
        return false;
    }


    public static AltarPower getAltarBlob(int pos){
        AltarKnowledge know = (AltarKnowledge)Dungeon.level.blobs.get( AltarKnowledge.class );
        if (know != null && know.volume > 0 && know.cur[pos] > 0){
            return know;
        }
        AltarEnchant ench = (AltarEnchant)Dungeon.level.blobs.get( AltarEnchant.class );
        if (ench != null && ench.volume > 0 && ench.cur[pos] > 0){
            return ench;
        }
        AltarUncurse uncurse = (AltarUncurse)Dungeon.level.blobs.get( AltarUncurse.class );
        if (uncurse != null && uncurse.volume > 0 && uncurse.cur[pos] > 0){
            return uncurse;
        }
        AltarChallenge challenge = (AltarChallenge)Dungeon.level.blobs.get( AltarChallenge.class );
        if (challenge != null && challenge.volume > 0){
            return challenge;
        }
        AltarGold gold = (AltarGold)Dungeon.level.blobs.get( AltarGold.class );
        if (gold != null && gold.volume > 0 ){
            return gold;
        }
        return null;
    }

    public static void affectCell(int pos){
        Heap heap;
        if ((heap = Dungeon.level.heaps.get(pos)) != null) {
            //throw item away
            int n;
            do {
                n = pos + Level.NEIGHBOURS8[Random.Int(8)];
            } while (!Level.passable[n] && !Level.avoid[n]);
            Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
        }
    }

    public String getDescription( ){
        return "选择一个要放上去的物品";
    }

    public abstract WndBag.Mode getBagMode( );

    public abstract Item affectItem(Item item );


    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];
    }

    @Override
    public void seed( int cell, int amount ) {
        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;
    }


}
