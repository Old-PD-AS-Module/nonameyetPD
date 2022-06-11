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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Petrificated extends Debuff {

    @Override
    public Element buffType() {
        return Element.BODY;
    }

/*    @Override
    public String toString() {
        return "Petrified";
    }

    @Override
    public String statusMessage() { return "petrified"; }

    @Override
    public String playerMessage() { return "Your skin turn to stone!"; }*/
    @Override
    public String toString() {
        return "石化";
    }

    @Override
    public String statusMessage() { return "石化"; }

    @Override
    public String playerMessage() { return "你被石化了！"; }

    @Override
    public int icon() {
        return BuffIndicator.PETRIFICATED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.PETRIFICATED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.PETRIFICATED );
    }

    @Override
/*    public String description() {
        return "Your skin has turned to stone. You cannot move, but your armor class is increased " +
                "and unaffected by body effects";
    }*/
    public String description() {
        return "你被石化了，期间不能做任何行动，但是你的护甲抵抗会增加";
    }

    @Override
    public boolean act() {

        return super.act();
    }

    public boolean attachOnLoad( Char target ) {
        target.stunned = true;
        return super.attachOnLoad(target);
    }

    @Override
    public boolean attachTo( Char target ) {
        //ethereal enemies are unaffected
        /*if(target.isEthereal())
            return false;*/

        if (super.attachTo( target )) {
            Stun.freeze(target);
            Buff.detach( target, Bleeding.class );
            Buff.detach( target, Crippled.class );
            Buff.detach( target, Poisoned.class );
            Buff.detach( target, Burning.class );
            Buff.detach( target, Chilled.class );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        Stun.unfreeze( target );
    }
}
