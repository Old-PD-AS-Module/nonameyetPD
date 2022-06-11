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
import com.watabou.utils.Bundle;

public class Decay extends Debuff {

    @Override
    public Element buffType() {
        return Element.UNHOLY;
    }

/*    @Override
    public String toString() {
        return "Decay";
    }

    @Override
    public String statusMessage() { return "decay"; }

    @Override
    public String playerMessage() { return "You feel cursed!"; }*/
    @Override
    public String toString() {
        return "衰变";
    }

    @Override
    public String statusMessage() { return "衰变"; }

    @Override
    public String playerMessage() { return "你受到了一股恶意的诅咒！"; }

    @Override
    public int icon() {
        return BuffIndicator.POLYMORPH;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.DECAY );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.DECAY );
    }

    @Override
/*    public String description() {
        return "A powerful curse was cast on you, making your body to slowly decay. " +
                "The curse will grow in power over time reducing your damage and resistance the longer ir remains on you. ";
    }*/
    public String description() {
        return "你身上被施加了一个强大的诅咒，使你的身体逐渐衰弱。随着时间的推移，诅咒的力量将会越来越强，你的伤害和抗性将会大幅降低。 ";
    }

    private int power=0;
    @Override
    public boolean act() {

        power++;

        target.damage( (int) Math.sqrt( target.totalHealthValue()*power )/4  , this, Element.UNHOLY_PERIODIC );

        /*if (target.isAlive() && target.HP<=target.HT/2) {
            Withered withered=target.buff(Withered.class);

            if (withered==null)
                withered =BuffActive.add(target, Withered.class, 2);

            if (withered!=null) withered.delay(TICK);
        }*/
        return super.act();

    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put( "POWER" , power );
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        power = bundle.getInt("POWER");
        super.restoreFromBundle( bundle );
    }

    public float getReduction(){
        return Math.min(0.75f,power*0.05f);
    }

}
