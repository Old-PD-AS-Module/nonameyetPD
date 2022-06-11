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


import com.ravenwolf.nnypd.actors.buffs.BuffReactive;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Combo extends BuffReactive {

//    private static String TXT_COMBO = "combo %dx!";
    private static String TXT_COMBO = "%dx 连击！";

    public int count = 0;

    @Override
    public int icon() {
        return count > 2 ? BuffIndicator.COMBO : BuffIndicator.NONE;
    }

    @Override
//    public String toString() {
//        return "Combo x" + count;
//    }
    public String toString() {
        return count+"连击！";
    }


    @Override
/*    public String description() {
        return "Every consecutive attack increases your damage slightly. Performing any actions " +
                "except attacking again will reduce this counter, though.";
    }*/
    public String description() {
        return "每次连续的攻击都会小幅的增加你的伤害。不过在执行任何近战攻击之外的行为时都会中断该效果。并减少总连击数";
    }


    @Override
    public void check(){
        super.check();
        //if dont hit for one turn lose half combo points
        if (duration==1 && count>0) {
            count /= 2;
            if (!(target instanceof Hero))
                duration--;//for monks properly losing combo after two missed hits /not attacking
        }
    }


    public void prolong(int amount){
        duration+=amount;
    }
	
	public void hit( ) {
        count++;

        //fail( 2 );
        if (target instanceof Hero) {//hero act is called 2 times for turn??
                reset(4);//allow 1 turn of missed hit for hero
        }else
            reset( 3 );//for monks

        if ( target.sprite.visible && count >= 3 ) {
            target.sprite.showStatus( CharSprite.DEFAULT, TXT_COMBO, count );
        }

//        if( target instanceof Hero && ((Hero) target).rangedWeapon instanceof RangedWeaponFlintlock ) {
//
//            postpone( target.attackDelay() * 3 / 2  );
//
//        } else {
//
//            postpone( target.attackDelay() * 1.01f );
//
//        }
	}

    public float modifier() {

        if ( count > 2 ) {

            return ( count - 2 ) * 0.125f;

        } else {

            return 0.0f;

        }
    }

    public float modifier2() {

        if ( count > 2 ) {

            return  (float) Math.min(0.9, count  * 0.075f); //combo bonus stop at 12

        } else {

            return 0.0f;

        }
    }

//	@Override
//	public boolean act() {
//		detach();
//		return true;
//	}

    private static final String COUNT	= "count";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, count );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt( COUNT );
    }
}
