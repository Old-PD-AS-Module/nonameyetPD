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

public class Dazed extends Debuff {

    @Override
    public Element buffType() {
        return Element.MIND;
    }

/*    @Override
    public String toString() {
        return "Dazed";
    }

    @Override
    public String statusMessage() { return "dazed"; }

    @Override
    public String playerMessage() { return "Everything is spinning around you!"; }*/
    @Override
    public String toString() {
        return "眩晕";
    }

    @Override
    public String statusMessage() { return "眩晕"; }

    @Override
    public String playerMessage() { return "你感觉整个世界都在围着你转！"; }

    @Override
    public int icon() {
        return BuffIndicator.VERTIGO;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.VERTIGO );
    }

    @Override
    public void removeVisual() {
        if (!target.hasBuff(Stun.class))
            target.sprite.remove( CharSprite.State.VERTIGO );
    }

    @Override
/*    public String description() {
        return "Everything is spinning around you! Your movement can be erratic at times, you " +
                "cannot concentrate, and your dexterity and perception are decreased.";
    }*/
    public String description() {
        return "你感觉整个世界都在围着你转，导致你的注意力无法集中，所有行动都会受到影响，并且大幅降低你的闪避和感知。";
    }
}
