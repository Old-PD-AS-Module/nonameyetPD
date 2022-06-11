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
import com.watabou.utils.Random;

import java.util.Locale;

public class Frenzy extends Bonus {

    @Override
    public String toString() {
        return "狂暴";
    }

    @Override
    public String statusMessage() { return "狂暴"; }

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
    public String description() {
        return "怒火所激发的杀戮本能正在你的血液中游走。你的伤害会在这个buff的时间内持续增加，当受到伤害时则会增加buff的持续时间。" + 
		"当前的伤害加成为"+String.format( Locale.getDefault(), "%.0f", 100*getDamageBonus() ) +"%";
    }

    public void increase( int dmg ) {

        dmg=dmg*36;
        dmg= ( dmg / target.HT + ( dmg % target.HT > Random.Int(target.HT) ? 1 : 0 ) );
        add(dmg);
    }

    public float getDamageBonus() {
        return Math.min(getDuration()/50f,1f);
    }

}