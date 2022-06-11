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
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Corrosion extends Debuff {

    @Override
    public Element buffType() {
        return Element.ACID;
    }

/*    @Override
    public String toString() { return "Corrosion"; }

    @Override
    public String statusMessage() { return "corrosion"; }

    @Override
    public String playerMessage() { return "Caustic ooze is eating your flesh!"; }*/
    @Override
    public String toString() { return "酸蚀"; }

    @Override
    public String statusMessage() { return "酸蚀"; }

    @Override
    public String playerMessage() { return "强酸正在腐蚀着你的身体！"; }

    @Override
    public int icon() {
        return BuffIndicator.CAUSTIC;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BLIGHTED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLIGHTED );
    }

    @Override
/*    public String description() {
        return "Caustic ooze corrodes your armor and skin. This is not only painful, but also " +
                "slowly damages your armor and makes it less effective for the duration of the " +
                "effect. Use water to remove it faster!";
    }*/
    public String description() {
        return "强酸正腐蚀着你的护甲和皮肤。无比痛苦，而且期间内你护甲的防御能力也会大大降低。尽快用水清洗掉它！";
    }

    @Override
    public boolean act() {

        target.damage(
            Random.Int( (int)Math.sqrt(
                target.totalHealthValue() * 0.5f
            ) ) + 1, this, Element.ACID_PERIODIC
        );


        if ( !target.isAlive()){// || Level.water[ target.pos ] && !target.flying ) {
            detach();
            return true;
        }
        if ( Level.water[ target.pos ] && !target.flying ) {
            decrease();
        }

        return super.act();
    }

}
