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


public abstract class WandCombat extends Wand {

    @Override
    public int maxCharges( int bonus ) {
        return 2 + ( bonus > 0 ? bonus : 0 ) ;
    }


    @Override
    protected String wandInfo() {

        Hero hero = Dungeon.hero;

        final String p = "\n\n";
        StringBuilder info = new StringBuilder();

        // if we are not sure what stats we currently have due to some of the relevant equipment
        // being unidentified, then use base values of these stats. not the best way to do it,
        // but it should work for now. maybe when i'll get to reworking weapons, i'll expand this
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

        // again, if the wand is not identified yet, then values are displayed as if it was unupgraded
        int max = getMaxDamage(magicPower,  effectiveness( isIdentified() ? bonus : 0 ));
        int min = max * 3 / 5;

        String recharge = String.format( Locale.getDefault(), "%.1f", rechargeRate() / willpower );

        /*String chance = String.format( Locale.getDefault(), "%.0f",
            100 * ( !isIdentified() || bonus < 0
            ? miscastChance( isIdentified() ? bonus : 0 )
            : squeezeChance( isIdentified() ? bonus : 0 ) )
        );*/

        if ( !isIdentified() ){

            info.append(
                "这个法杖目前_" + ( isCursedKnown() && isCursed() ? "是诅咒的" : "未被鉴定" ) +
                "_。一般情况下它的充能数为_" + maxCharges( 0 ) + "_。" /*and will probably " +
                "have _" + chance + "% chance_ to miscast when used."*/
            );

            info.append( p );

            info.append(
                    "根据你当前的魔能和意志属性，这个法杖释放时" +
                            "可能会造成_" + min + "-" + max + "点伤害_，" +
                            "并且_每" + recharge + "回合_恢复1充能。"
            );

        } else {

            info.append(
                    "这个法杖目前_" + ( isCursed() ? "是诅咒的" : "未被诅咒" ) + "_。当前充能数为_" +
                            getCharges() + "/" + maxCharges() + "_。"/* and will have _" + chance +"%_ " +
                "chance to " + ( bonus < 0 ? "miscast when used." : "squeeze an additional charge." )*/
            );

            info.append( p );

            info.append(
                    "根据你当前的魔能和意志属性，这个法杖每次释放时" +
                            "会造成_" + min + "-" + max + "点伤害_，" +
                            "并且_每" + recharge + "回合_恢复1充能。"
            );

        }

        return info.toString();
    }
}
