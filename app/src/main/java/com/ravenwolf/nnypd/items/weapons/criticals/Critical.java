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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.watabou.utils.Random;

public abstract class Critical {

    private boolean betterCriticals=false;
    private float criticalModifier=1f;
    protected Weapon weap;

    public abstract String name();

    public Critical(Weapon weap){
        this.weap=weap;
    }

    public Critical(Weapon weap,boolean betterCritical,float criticalModifier ){
        this.weap=weap;
        this.betterCriticals=betterCritical;
        this.criticalModifier=criticalModifier;
    }

    public int proc(Char attacker, Char defender, int damage ){
        if (crit(attacker, defender, damage)) {
            defender.sprite.showStatus(CharSprite.NEGATIVE, "暴击！");
            return proc_crit(attacker, defender, damage);
        }
        return damage;
    }

    public abstract int proc_crit(Char attacker, Char defender, int damage );


    protected boolean crit(Char attacker, Char defender, int damage){

        float penalty=1f;
        if (attacker instanceof Hero){
            //penalty due lower strength gives crit chance penalty
            if (weap.speedFactor(attacker) < 1) penalty=weap.speedFactor(attacker);
            //attacking unseen mobs have 50% penalty
            if (!Dungeon.visible[defender.pos])
                penalty*=0.5f;
        }
        //awareness lower than 100% reduces crit chance
        if (attacker.awareness() < 0)
            penalty*=attacker.awareness();

        //awareness higher than 100% gives +1% each 2 points of excess awareness
        float awarenessBonus=Math.max(0,(attacker.awareness()-1f)/2f);

        float critChance = 0.125f * criticalModifier + awarenessBonus;

        //subclasses bonus
        if (attacker instanceof Hero) {
            Hero hero = (Hero) attacker;
            if (hero.subClass == HeroSubClass.SLAYER) {
                Combo combo = hero.buff(Combo.class);
                if (combo != null && combo.count > 2)
                    critChance += 0.025f * combo.count;
            }

            if (hero.subClass == HeroSubClass.SNIPER && weap instanceof RangedWeapon || weap instanceof ThrowingWeapon) {
                critChance += 0.1f;
            }
        }

        critChance*=penalty;
        return Random.Float() < critChance;
    }

    public boolean isBetterCriticals() {
        return betterCriticals;
    }

    public float getCriticalModifier() {
        return criticalModifier;
    }

}
