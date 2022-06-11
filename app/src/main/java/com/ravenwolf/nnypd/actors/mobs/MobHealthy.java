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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Dungeon;

public abstract class MobHealthy extends Mob {

    //made them less vulnerable to Debuffs
    @Override
    protected float healthValueModifier() { return 0.75f; }

    protected MobHealthy(int exp) {
        this( Dungeon.chapter(), exp, false );
    }

    protected MobHealthy(int t, int exp, boolean isBoss) {

        tier = t;

        HT = 2+tier * 8 + exp;
        armorClass = tier * 6 - 2;

        minDamage = tier * 2 + 1;
        maxDamage = tier * 6 + 1;

        accuracy = exp + tier;
        dexterity = exp +tier;

        super.adjustStatsByDifficulty(isBoss,exp);
    }

    @Override
    public int minAC() {
        return super.minAC()+2;
    }

    @Override
    public int STR() {
        return super.STR()+4;
    }

    @Override
    public float awareness(){
        //return super.awareness() * ( 1.0f - tier * 0.05f );
        return super.awareness() * ( 0.55f + tier * 0.05f );
    }

    @Override
    public float stealth(){
        //return super.stealth() * ( 1.0f - tier * 0.05f );
        return super.stealth() * ( 0.55f + tier * 0.05f );
    }
}
