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
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class AltarKnowledge extends AltarPower {

    private static final String TXT_PROCEED =
//            "Your %s has been identified.";
            "您的%s已鉴定。";

    @Override
    protected void evolve() {
        super.evolve();

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.ALTAR_OF_AWARENESS);
        }
    }

    public WndBag.Mode getBagMode( ) {
        return WndBag.Mode.UNIDENTIFED;
    }

    public Item affectItem(Item item ){
        if (!item.isIdentified()) {
            item.identify();
            volume = off[pos] = cur[pos] -= 1;

            GLog.i( TXT_PROCEED, item );

            Sample.INSTANCE.play(Assets.SND_SECRET);

            CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 8);

            if (cur[pos] <= 0) {
                GLog.i(AltarPower.TXT_NO_MORE_POWER);
                Journal.remove(Feature.ALTAR_OF_AWARENESS);
            }
        }
        return item;
    }


    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);

        emitter.pour(  Speck.factory( Speck.QUESTION ), 0.6f );
    }

    @Override
    public String tileDesc() {
        return
/*                "Power of knowledge radiates from this altar. " +
                        "Offer an item to reveal all of their secrets.";*/
                "知识的力量从这个祭坛散发出来。 " +
                        "提供一件物品来揭示他们所有的秘密。";
    }
}
