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
package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Sunlight;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Rejuvenation extends Bonus {

    @Override
    public int icon() {
        return BuffIndicator.SUNLIGHT;
    }

/*    @Override
    public String toString() {
        return "Consecrated";
    }

    @Override
    public String statusMessage() { return "consecrated"; }

    @Override
    public String description() {
        return "Being bathed in holy light restores your health. The longer you are affected by " +
                "this effect, the more health it restores.";
    }*/
    @Override
    public String toString() {
        return "神圣";
    }

    @Override
    public String statusMessage() { return "神圣"; }

    @Override
    public String description() {
        return "沐浴在圣光之下，你的生命力逐渐开始恢复。在其下停留越久，恢复力越强。";
    }
    @Override
    public boolean act() {

        Blob blob = Dungeon.level.blobs.get( Sunlight.class );

        if (blob != null && blob.cur[ target.pos ] > 0 ){

            double effect =  Math.sqrt( target.totalHealthValue() ) + getDuration();

            //effect *= target.ringBuffs( RingOfVitality.Vitality.class );

            target.heal( Random.Int( (int)effect ) + 1 );

            spend( TICK );

        } else {

            detach();

        }

        return true;
    }

}
