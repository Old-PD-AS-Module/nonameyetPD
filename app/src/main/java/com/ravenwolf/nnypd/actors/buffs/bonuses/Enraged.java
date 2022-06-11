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

import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Enraged extends Bonus {

    public int refreshDuration=0;

/*
    @Override
    public String toString() {
        return "Enraged";
    }

    @Override
    public String statusMessage() { return "enraged"; }

    @Override
    public String playerMessage() { return "You. Feel. REALLY. ANGRY!"; }
*/
    @Override
    public String toString() {
        return "狂怒";
    }

    @Override
    public String statusMessage() { return "狂怒"; }

    @Override
    public String playerMessage() { return "我-要-撕-碎-你-们-这-些-怪-物！"; }

    @Override
    public int icon() {
        return BuffIndicator.ENRAGED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.ENRAGED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.ENRAGED );
    }

    @Override
/*    public String description() {
        return "Murderous rage boils in your veins. Your damage is increased, and every kill will " +
                "renew duration of this buff.";
    }*/
    public String description() {
        return "怒火所激发的杀戮本能正在你的血液中游走。你现在能够造成更高的伤害！每次杀戮都会恢复这个buff的持续时间！";
    }


    public void reset( int dur ) {

        if( getDuration() < dur ){
            setDuration(dur);
        }

        delay( TICK );
    }

    private static final String REFRESH_DURATION	= "refresh";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( REFRESH_DURATION, refreshDuration );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        refreshDuration = bundle.getInt( REFRESH_DURATION );
    }
}