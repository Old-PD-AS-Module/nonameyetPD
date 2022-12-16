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

import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;

public class Blinded extends Debuff {

//    public final static String TXT_CANNOT_READ = "Reading? You can barely see your hands.";
    public final static String TXT_CANNOT_READ = "阅读？你现在连自己的手都看不清！";
/*
    @Override
    public Element buffType() {
        return Element.MIND;
    }
*/
/*    @Override
    public String toString() {
        return "Blinded";
    }

    @Override
    public String statusMessage() { return "blinded"; }

    @Override
    public String playerMessage() { return "You cannot see anything!"; }*/
    @Override
    public String toString() {
        return "失明";
    }

    @Override
    public String statusMessage() { return "失明"; }

    @Override
    public String playerMessage() { return "你什么都看不见了！"; }

    @Override
    public int icon() {
        return BuffIndicator.BLINDED;
    }

    @Override
    public void applyVisual() {

        if (target.sprite.visible) {
            Sample.INSTANCE.play( Assets.SND_DEGRADE ,0.8f,0.8f,0.6f);
        }

        target.sprite.add( CharSprite.State.BLINDED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLINDED );
    }

    @Override
/*    public String description() {
        return "Your eyes are unable to perceive anything further than a length of your hand. Your " +
                "attack and perception is decreased, and you are unable to read scrolls.";
    }*/
    public String description() {
        return "你现在无法分辨周围视野的任何事物。你的命中和感知能力大幅下降，更不能阅读任何卷轴。";
    }
}
