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

public class Disrupted extends Debuff {

    @Override
    public Element buffType() {
        return Element.DISPEL;
    }

/*    @Override
    public String toString() {
        return "Disrupted";
    }

    @Override
    public String statusMessage() { return "disrupted"; }*/
    @Override
    public String toString() {
        return "混乱";
    }

    @Override
    public String statusMessage() { return "混乱"; }

//    @Override
//    public String playerMessage() { return "You catch fire! Quickly, run to the water!"; }

    @Override
    public int icon() {
        return BuffIndicator.DISRUPT;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.DISRUPTED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.DISRUPTED );
    }

    @Override
/*    public String description() {
        return "You are not supposed to be able to see description of this debuff, but if you "+
                "somehow do, then it is just the same as Confused, but for magical creatures.";
    }*/
    public String description() {
        return "你不应该能够看到这个减益的描述，但是如果你看到了，那么对于魔法生物来说，这就像处于迷茫中。";
    }
}
