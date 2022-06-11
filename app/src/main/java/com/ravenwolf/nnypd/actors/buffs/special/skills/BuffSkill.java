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

package com.ravenwolf.nnypd.actors.buffs.special.skills;

import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.watabou.utils.Bundle;

public abstract class BuffSkill extends Buff {

    private static final String COOLDOWN = "cooldown";

    private float cooldown;
    public float CD = 100f;

    abstract public void doAction();

    public float getMaxCD(){
        return CD;
    }

    @Override
    public boolean act() {
        spend( TICK );
        decrease(1);
        return true;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COOLDOWN, cooldown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt( COOLDOWN );
    }

    public void decrease(int ammount){
        if (cooldown>0)
            cooldown-=ammount;
        /*else
            detach();*/
    }

    public float getCD(){
        return cooldown;
    }

    public void setCD(float cd){
        cooldown = cd;
    }
}