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

package com.ravenwolf.nnypd.items.weapons.criticals;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.weapons.Weapon;


public class BladeCritical extends Critical {

    public BladeCritical(Weapon weap){
        super(weap);
    }

    public BladeCritical(Weapon weap, boolean betterCriticals,float criticalModifier){
        super(weap,betterCriticals,criticalModifier);
    }

    public String name(){
        return "斩击";
    }

    @Override
    public int proc_crit(Char attacker, Char defender, int damage ) {
        boolean improvedCrit= isBetterCriticals();
        int bonusDamage =attacker instanceof Hero ? (weap.min()+1)/2 +1 : 0;
        if (defender.isEthereal() || defender.isSolid()) {
            if (improvedCrit)
                damage += bonusDamage*1.5f;
            else
                damage += bonusDamage;
        }else {
            if (improvedCrit) {
                damage += bonusDamage;
                BuffActive.addFromDamage(defender, Crippled.class, damage);
            }else {
                damage += bonusDamage / 2;
                BuffActive.addFromDamage(defender, Bleeding.class, damage);
            }
        }
        return damage;
        /*
        int bonusDamage =weap.min();
        if (defender.isEthereal()) {
            defender.sprite.showStatus(CharSprite.NEGATIVE, "sliced");
            if (improvedCrit)
                damage += bonusDamage*1.5f;
            else
                damage += bonusDamage;
        } else if (defender.HP > damage){
            if (improvedCrit) {
                damage += bonusDamage;
                BuffActive.addFromDamage(defender, Crippled.class, damage);
            }else {
                damage += bonusDamage/2;
                BuffActive.addFromDamage(defender, Bleeding.class, damage);
            }
        }
        return damage;*/
    }


}
