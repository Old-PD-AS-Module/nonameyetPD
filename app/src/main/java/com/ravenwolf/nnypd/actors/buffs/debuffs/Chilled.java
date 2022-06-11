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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.food.FrozenCarpaccio;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Chilled extends Debuff {

//    public final static String TXT_CANNOT_LIGHT = "Your lantern is too cold to be lit again. You'll have to wait a little.";
    public final static String TXT_CANNOT_LIGHT = "你的油灯温度太低了，无法点亮，你得等一会";

    @Override
    public Element buffType() {
        return Element.FROST;
    }

/*    @Override
    public String toString() {
        return "Chilled";
    }

    @Override
    public String statusMessage() { return "chilled"; }

    @Override
    public String playerMessage() { return "Intense cold slows your movement!"; }*/
    @Override
    public String toString() {
        return "冻伤";
    }

    @Override
    public String statusMessage() { return "冻伤"; }

    @Override
    public String playerMessage() { return "严寒会大幅降低你的速度！"; }

    @Override
    public int icon() {
        return BuffIndicator.FROZEN;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.CHILLED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.CHILLED );
    }

    @Override
/*    public String description() {
        return "Brrr, it is cold! All your actions are slowed, your lantern is not working, and on " +
                "top of that you are less likely to hit and dodge. Oh, and being in water prolongs " +
                "duration of this effect.";
    }*/
    public String description() {
        return "寒冰使你所有的动作都变慢了，油灯也会被它熄灭，最重要的是它限制了你的大部分行动，让你很难命中与闪避。若在水里则会继续延长这种效果的持续时间。";
    }

    private final static int FREEZE_THRESHOLD = 8;

    private int frezzeCounter=0;

    public void resetFrezzeCounter(){
        frezzeCounter=0;
    }

    @Override
    public boolean act() {

        if( Level.water[ target.pos ] && !target.flying) {
            if (Random.Int( 2 ) == 0 )
                setDuration(getDuration()+1);
            frezzeCounter++;
        }

        if (target.isAlive()) {
            Frozen buffFrozen = target.buff(Frozen.class);
            if (buffFrozen == null){
                if (frezzeCounter > FREEZE_THRESHOLD)
                    BuffActive.add(target, Frozen.class, frezzeCounter - FREEZE_THRESHOLD + (Level.water[target.pos] && !target.flying ? 1 : 0));
            }
        }

        return super.act();
    }

    public void add( float value ) {
        super.add(value);
        //If reach frezze duration
        if (target.isAlive()) {
            Frozen buffFrozen = target.buff(Frozen.class);
            //if already frozen, extends duration
            if (buffFrozen != null) {
                buffFrozen.add(value);
            } else
                //increase freeze counter
                frezzeCounter+=value;
        }
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            Buff.detach( target, Burning.class );
            if( target instanceof Hero ){
                Hero hero = (Hero)target;
                freezeHeroBackpack(hero);
                OilLantern lantern = hero.belongings.getItem( OilLantern.class );

                if( lantern != null && lantern.isActivated() ){
                    lantern.deactivate( hero, false );
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static void freezeHeroBackpack(Hero hero){
        Item item = hero.belongings.randomUnequipped();
        if (item instanceof MysteryMeat) {
            item.detach( hero.belongings.backpack );
            FrozenCarpaccio carpaccio = new FrozenCarpaccio();
            if (!carpaccio.collect( hero.belongings.backpack )) {
                Dungeon.level.drop( carpaccio, hero.pos ).sprite.drop();
            }
        }

    }

    @Override
    public void detach() {
        super.detach();
    }

    private static final String COUNT	= "count";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, frezzeCounter );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        frezzeCounter = bundle.getInt( COUNT );
    }
}
