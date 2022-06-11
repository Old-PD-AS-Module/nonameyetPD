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

import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;

public class Shocked extends Debuff {

//    private static final String TXT_DISARMED = "Sudden shock have made you drop your %s on the ground!";
    private static final String TXT_DISARMED = "强大的能量使你不小心把%s扔在了地上";

    /*
    @Override
    public Element buffType() {
        return Element.SHOCK;
    }
*/

    /*    @Override
        public String toString() {
            return "Electrified";
        }

        @Override
        public String statusMessage() { return "electrified"; }

        @Override
        public String playerMessage() { return "You are electrified!"; }*/
    @Override
    public String toString() {
        return "触电";
    }

    @Override
    public String statusMessage() { return "触电"; }

    @Override
    public String playerMessage() { return "你触电了！"; }

    @Override
    public int icon() {
        return BuffIndicator.SHOCKED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.POISONED );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.POISONED );
//    }

    @Override
/*    public String description() {
        return "Lightning coarses through your body, waiting for a moment to be released - being " +
                "zapped again or stepping into water will discharge it, stunning you for some time. " +
                "Also, your wands replenish slower because of the static interference.";
    }*/
    public String description() {
        return "电流遍布在你的身上，再次被击中或进入水中将会释放这些电流，" +
                "同时，你的法杖和魔咒的充能速度会因为电流的影响而变得缓慢。";
    }

    @Override
    public boolean act(){

        if( target.isAlive() && !target.flying && Level.water[ target.pos ] ){
            discharge();
        }

        return super.act();
    }

    public void discharge() {
/*
        target.damage(
                Random.IntRange( getDuration()*2,  (int)Math.sqrt( target.totalHealthValue() *getDuration() ) ),
                this, Element.SHOCK_PERIODIC
        );

//        target.sprite.showStatus( CharSprite.NEGATIVE, "ZAP!");

        if( target instanceof Hero ) {
            Camera.main.shake( 2, 0.3f );
            Hero hero = (Hero)target;
            EquipableItem weapon = Random.oneOf( hero.belongings.weap1, hero.belongings.weap2 );

            if( weapon != null && weapon.disarmable() ) {
                GLog.w(TXT_DISARMED, weapon.name());
                weapon.doDrop(hero);
            }

        } else {

            if (target instanceof Mob && Bestiary.isBoss(target))
                target.delay( 1 );
            else
                target.delay( Random.IntRange( 1, 2 ) );

        }
*/
        int force=getDuration()*2 / target.totalHealthValue() + 1;
        target.delay( force);
        if( target instanceof Hero ) {
            Camera.main.shake(2, 0.3f);
        }

        if (target.sprite.visible) {
            target.sprite.centerEmitter().burst( SparkParticle.FACTORY, (int)Math.ceil( getDuration() ) + 1 );
        }

        detach();
    }

}
