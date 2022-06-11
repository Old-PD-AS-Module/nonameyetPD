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
package com.ravenwolf.nnypd.actors.buffs.special;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Guard extends BuffActive {

/*    private static String TXT_PARRIED = "parried";
    private static String TXT_BLOCKED = "blocked";

    private static String TXT_PARRY_BROKEN = "parry failed!";
    private static String TXT_BLOCK_BROKEN = "block failed!";*/
    private static String TXT_PARRIED = "格挡";
    private static String TXT_BLOCKED = "格挡";

    private static String TXT_PARRY_BROKEN = "格挡失败！";
    private static String TXT_BLOCK_BROKEN = "格挡失败！";

    public int realDuration;

    private static final String REAL_DURATION = "object";

    @Override
    public int icon() {
        return BuffIndicator.GUARD;
    }

    @Override
//    public String toString() {
//        return "Guard";
//    }
    public String toString() {
        return "格挡";
    }


    public boolean act(){

        if (target.moving && target instanceof Hero) {
            Hero hero = (Hero) target;
            if (hero.subClass == HeroSubClass.KNIGHT) {
                add(1);
            }
        }
        return super.act();
    }

    @Override
//    public String statusMessage() { return "guard"; }
    public String statusMessage() { return "格挡"; }

    boolean whitShield=true;

    public void guard(Char src){
        if (src instanceof Hero){
            Hero hero=(Hero)src;
            if (!(hero.belongings.weap2 instanceof Shield)){
                whitShield=false;
            }

        }
    }

    @Override
/*    public String description() {
        return "You are standing in a defensive position, trying to block physical attacks. Every " +
                "successful block will possibly expose your attacker to a powerful counterattack.";
    }*/
    public String description() {
        return "你正保持着格挡的姿态，随时准备挡下敌人的攻击。每次成功的格挡都有可能会弹反敌人";
    }


    public void fail(boolean withShield ) {
        target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCK_BROKEN : TXT_PARRY_BROKEN);
    }

    public void proc( boolean withShield ) {

        if( target.sprite.visible ) {
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCKED : TXT_PARRIED);

            Camera.main.shake(2, 0.1f);

        }
        decrease();
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(REAL_DURATION, realDuration );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        realDuration = bundle.getInt(REAL_DURATION);
    }

}
