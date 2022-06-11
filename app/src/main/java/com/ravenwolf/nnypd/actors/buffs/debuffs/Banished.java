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

public class Banished extends Debuff {
	
//	public int object = 0;

    @Override
    public Element buffType() {
        return Element.DISPEL;
    }

/*    @Override
    public String toString() {
        return "Banished";
    }

    @Override
    public String statusMessage() { return "banished"; }*/
    @Override
    public String toString() {
        return "放逐";
    }

    @Override
    public String statusMessage() { return "放逐"; }


    @Override
    public int icon() {
        return BuffIndicator.BANISH;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BANISHED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BANISHED );
    }


    @Override
/*    public String description() {
        return "You are not supposed to be able to see description of this debuff, but if you " +
                "somehow do, then it is just the same as Tormented, but for magical creatures.";
    }*/
    public String description() {
        return "你不应该看到这个减益的描述，但是如果你 " +
                "以某种方式看到了，那么对于魔法生物来说，它将是痛苦的。";
    }

    @Override
    public boolean act() {

        target.damage( (int) Math.sqrt( target.totalHealthValue() *1.2f)-(int) Math.sqrt( target.currentHealthValue()*0.8f)  + 1, this, Element.DISPEL );

        return super.act();

    }

//    private static final String OBJECT	= "object";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle( bundle );
//        bundle.put( OBJECT, object );
//
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        object = bundle.getInt( OBJECT );
//    }
}
