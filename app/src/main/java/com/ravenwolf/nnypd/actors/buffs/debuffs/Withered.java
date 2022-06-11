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

public class Withered extends Debuff {

    @Override
    public Element buffType() {
        return Element.UNHOLY;
    }

/*    @Override
    public String toString() {
        return "Withered";
    }

    @Override
    public String statusMessage() { return "withered"; }

    @Override
    public String playerMessage() { return "You feel weakened!"; }*/
    @Override
    public String toString() {
        return "衰弱";
    }

    @Override
    public String statusMessage() { return "衰弱"; }

    @Override
    public String playerMessage() { return "你感受到力量正在被抽走！"; }

    @Override
    public int icon() {
        return BuffIndicator.WITHER;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.WITHERED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.WITHERED );
    }

    @Override
/*    public String description() {
        return "Touch of unholy magic corrupted your body, weakening your weapons and armor. " +
                "Means of recovery such as healing are hindered as well.";
    }*/
    public String description() {
        return "衰弱感渗透了你的全身，并弱化你的武器和护甲。治疗，充能等回复手段也因此受到限制。";
    }

}
