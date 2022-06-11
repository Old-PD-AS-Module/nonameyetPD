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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FellFire extends Debuff {

    @Override
    public Element buffType() {
        return Element.ENERGY;
    }

/*    @Override
    public String toString() {
        return "Burning";
    }

    @Override
    public String statusMessage() { return "burning"; }

    @Override
    public String playerMessage() { return "You are engulfed by spectral fire!"; }*/
    @Override
    public String toString() {
        return "灵魂灼烧";
    }

    @Override
    public String statusMessage() { return "灵魂灼烧"; }

    @Override
    public String playerMessage() { return "你正被无情的魔焰所吞噬！"; }


    @Override
    public int icon() {
        return BuffIndicator.FELL_FIRE;
    }

    @Override
    public void applyVisual() {

        if (target.sprite.visible) {
            Sample.INSTANCE.play( Assets.SND_BURNING );
        }

        target.sprite.add( CharSprite.State.FELL_FIRE );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.FELL_FIRE );
    }

    @Override
/*    public String description() {
        return "This spectral fire is much more dangerous than regular one, as it cannot be extinguished by any means. " +
                "Luckily, it doesn't spread to flammable objects.";
    }*/
    public String description() {
        return "这种奇怪的火焰比普通的火要危险得多，因为任何人都无法扑灭它，只能等它自己燃烧殆尽。不过幸运的是，它不会引燃其他物品";
    }
	
	@Override
	public boolean act() {

        Invisibility.dispel(target);
        target.damage(Random.Int( (int)Math.sqrt(target.totalHealthValue() ) ) + 1, this, Element.ENERGY );

		return super.act();
	}

    @Override
    public boolean attachTo( Char target ) {

        if (super.attachTo( target )) {

            Invisibility.dispel(target);
            Buff.detach( target, Ensnared.class );
            Buff.detach( target, Chilled.class );

            return true;
        } else {
            return false;
        }
    }

}
