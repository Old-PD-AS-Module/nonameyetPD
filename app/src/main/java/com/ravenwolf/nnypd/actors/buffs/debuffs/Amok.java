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
package com.ravenwolf.nnypd.actors.buffs.debuffs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Amok extends Debuff {

    @Override
    public Element buffType() {
        return Element.MIND;
    }

/*    @Override
    public String toString() {
        return "Amok";
    }

    @Override
    public String statusMessage() { return "amok"; }

    @Override
    public String playerMessage() { return "You are amoked!"; }
    @Override*/
    public String toString() {
        return "狂乱";
    }

    @Override
    public String statusMessage() { return "狂乱"; }

    @Override
    public String playerMessage() { return "你现在很疯狂！"; }

    @Override
    public int icon() {
        return BuffIndicator.SOUL_LINK;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.AMOKED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.AMOKED );
    }

    @Override
/*    public String description() {
        return "Amok causes a state of great rage and confusion in its target." +
                " When a creature is amoked, they will attack whatever is near them, or even themselves.";
    }*/
    public String description() {
        return "狂乱会导致当前生物进入一种极度愤怒和混乱的状态，狂乱的生物会不分敌我的攻击任何靠近它的单位。甚至于它们自己！";
    }


}
