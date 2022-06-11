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

package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Mending extends Bonus {

/*    @Override
    public String toString() {
        return "Mending";
    }

    @Override
    public String statusMessage() { return "mending"; }*/
    @Override
    public String toString() {
        return "恢复";
    }

    @Override
    public String statusMessage() { return "恢复"; }

    @Override
    public int icon() {
        return BuffIndicator.MENDING;
    }

    @Override
    public void applyVisual(){
        target.sprite.add( CharSprite.State.MENDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.MENDING );
    }

    @Override
/*    public String description() {
        return "Warm, tingly sensation flows under your skin, and you can feel your wounds closing " +
                "at an accelerated rate.";
    }*/
    public String description() {
        return "温暖舒适的感觉遍布着整个身体，你感受到自己身上的伤口正在快速愈合";
    }
/*
    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            Buff.detach( target, Poisoned.class );
            Buff.detach( target, Bleeding.class );
            Buff.detach( target, Crippled.class );
            Buff.detach( target, Withered.class );
            return true;
        } else {
            return false;
        }
    }
*/
    @Override
    public boolean act() {

        int effect = 10;

        if (target.hasBuff(Poisoned.class)) target.buff(Poisoned.class).decrease();
        if (target.hasBuff(Bleeding.class)) target.buff(Bleeding.class).decrease();
        if (target.hasBuff(Crippled.class)) target.buff(Crippled.class).decrease();
        if (target.hasBuff(Withered.class)) target.buff(Withered.class).decrease();

        if( target instanceof Hero ) {

            Hero hero = ((Hero) target);

            effect += (hero.lvl - 1) + hero.strBonus;

        }else {
            effect += target.HT/20;
        }

        int healthRestored = ( effect / 10 + ( effect % 10 > Random.Int(10) ? 1 : 0 ) );

        target.heal( healthRestored > 0 ? healthRestored : 1 );

        return super.act();
    }
}
