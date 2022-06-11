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
package com.ravenwolf.nnypd.items.wands;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.body.MageArmor;
import com.ravenwolf.nnypd.items.rings.RingOfConcentration;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.items.weapons.melee.Quarterstaff;

import java.util.Locale;

public abstract class WandUtility extends Wand {


	{
        usagesToKnow /=2;
	}

    public int basePower(){
        return 16;
    }

    protected int getMaxDamage(int magicPower, float effectiveness){
        return (int)( (magicPower/4f +basePower()) * effectiveness);
    }

    @Override
	public int maxCharges(int bonus) {
        return bonus > 0 ? 3 + bonus : 3 ;
	}

    @Override
    public float rechargeRate(int bonus) {
        return 50f / ( 1.0f + effectiveness(bonus)/2) ;
    }

    public float effectiveness(int bonus) {
        return super.effectiveness(bonus) +curCharges * 0.2f;
    }

    protected void onZap( int cell ){

    }

    @Override
    protected void cursedProc(Hero hero){
	    curCharges = (curCharges+1)/2;
        onZap( hero.pos );
    }

    @Override
    protected void spendCharges() {
	    onChargesSpent(curCharges);
        if (charger != null) {
            curCharges = 0;
        }
    }

    protected String getType(){
        return "魔咒";
    }

    @Override
    protected String wandInfo() {

        Hero hero = Dungeon.hero;

        final String p = "\n\n";
        StringBuilder info = new StringBuilder();

        int magicPower =
                hero.belongings.ring1 instanceof RingOfSorcery && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfSorcery && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1 instanceof Quarterstaff && !hero.belongings.weap1.isIdentified() ?
                        hero.magicSkill : hero.magicSkill();

        float willpower =
                hero.belongings.ring1 instanceof RingOfConcentration && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfConcentration && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.armor instanceof MageArmor && !hero.belongings.armor.isIdentified() ?
                        hero.baseWillpower() : hero.willpower();


        int auxCharges=curCharges;
        //set charges to max to retrieve damage
        curCharges=maxCharges(isIdentified() ? bonus : 0 );
        // again, if the wand is not identified yet, then values are displayed as if it was unupgraded
        int max = getMaxDamage(magicPower,  effectiveness( isIdentified() ? bonus : 0 ));
        int min = max * 3 / 5;
        //empty charges to retrieve minimum recharge rate
        curCharges=0;
        String recharge = String.format( Locale.getDefault(), "%.1f", rechargeRate() / willpower );
        //restore current charges
        curCharges=auxCharges;


        if ( !isIdentified() ){

            info.append(
                    "这个魔咒_" + ( isCursedKnown() && isCursed() ? "是诅咒的" : "未被鉴定" ) +"_"
            );

            info.append( p );


            info.append(
                    "根据你当前的魔能和意志属性，" +
                            getEffectDescription( min ,  max, false) +
                            "并且_每" + recharge + "回合_恢复1充能。"
            );

        } else {

            info.append(
                    "这个魔咒_" + ( isCursed() ? "是诅咒的" : "未被诅咒" ) + "_。当前充能数为_" +
                            getCharges() + "/" + maxCharges() + "_。"/* and will have _" + chance +"%_ " +
                "chance to " + ( bonus < 0 ? "miscast when used." : "squeeze an additional charge." )*/
            );

            info.append( p );
            info.append(
                    "根据你当前的魔能和意志属性，" +
                            getEffectDescription( min ,  max, true) +
                            "并且_每" + recharge  + "回合_恢复1充能"
            );

        }

        return info.toString();
    }

    protected String getEffectDescription(int min , int max, boolean known){
        return  "这个魔咒" +(known? "":"(可能) ")+"会造成_" + min + "-" + max + "点伤害_";
    }

}