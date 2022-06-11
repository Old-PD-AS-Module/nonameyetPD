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

public abstract class MobEvasive extends Mob {

    protected MobEvasive(int exp) {
        this( Dungeon.chapter(), exp, false );
    }

    protected MobEvasive(int t, int exp, boolean isBoss) {

        tier = t;

        HT = 3+tier * 3 + exp;
        armorClass = tier * 2 -1;

        minDamage = tier;
        maxDamage = tier * 4;

        accuracy = 2 + tier * 2 + exp;
        dexterity = 4 + tier * 4 + exp;

        super.adjustStatsByDifficulty(isBoss,exp);

    }

    @Override
    public int STR() {
        return super.STR()-2;
    }

    @Override
    public float stealth(){
        return super.stealth() * ( 1.0f + tier * 0.1f );
    }
}
