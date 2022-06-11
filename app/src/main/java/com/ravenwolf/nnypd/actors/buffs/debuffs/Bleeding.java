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

public class Bleeding extends Debuff {
    @Override
    public Element buffType() {
        return Element.BODY;
    }

/*    @Override
    public String toString() {
        return "Bleeding";
    }

    @Override
    public String statusMessage() { return "bleeding"; }

    @Override
    public String playerMessage() { return "You are bleeding!"; }*/
    @Override
    public String toString() {
        return "流血";
    }

    @Override
    public String statusMessage() { return "流血"; }

    @Override
    public String playerMessage() { return "你正在流血"; }

    @Override
    public int icon() {
        return BuffIndicator.BLEEDING;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BLEEDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLEEDING );
    }

    @Override
/*    public String description() {
        return "That wound is leaking a worrisome amount of blood. Bleeding causes damage every turn.";
    }*/
    public String description() {
        return "伤口正在不安的涌出大量血液，每回合都会造成伤害，直到流血停止";
    }

    @Override
    public boolean act() {

        int dmg=(int) Math.sqrt( target.totalHealthValue() ) *getDuration();
        dmg = ( dmg / 3 + ( Random.Int( 3 ) < dmg % 3 ? 1 : 0 ) );
        target.damage( dmg , this, Element.BODY );

        return super.act();
    }

    @Override
    public void add( float value ) {
        //the more bleed the target have is harder to increase the buff
        if (getDuration()>0)
            value=value/getDuration();
        super.add(value);
    }
}
