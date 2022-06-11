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
import com.watabou.utils.Random;

public class Poisoned extends Debuff {

//    public final static String TXT_CANNOT_EAT = "You can't stomach anything right now.";
    public final static String TXT_CANNOT_EAT = "你全身难受恶心，现在吃不下任何东西。";

    @Override
    public Element buffType() {
        return Element.BODY;
    }

/*    @Override
    public String toString() {
        return "Poisoned";
    }

    @Override
    public String statusMessage() { return "poisoned"; }

    @Override
    public String playerMessage() { return "You are poisoned!"; }    */
    @Override
    public String toString() {
        return "中毒";
    }

    @Override
    public String statusMessage() { return "中毒"; }

    @Override
    public String playerMessage() { return "你中毒了！"; }


    @Override
    public int icon() {
        return BuffIndicator.POISONED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.POISONED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.POISONED );
    }

    @Override
/*    public String description() {
        return "You are not feeling well... Seems like there is a poison in your veins. This will " +
                "slowly damage you and make you weaker, decreasing your attack and defence capabilities.";
    }*/
    public String description() {
        return "你感觉非常难受...毒素似乎遍布了你的全身。期间会对你造成持续伤害并且大幅降低你的攻击和防御能力。";
    }

    @Override
    public boolean act() {

        target.damage( Random.NormalIntRange(1,(int)(Math.sqrt( target.totalHealthValue()))),this, Element.BODY );
        return super.act();

    }

}
