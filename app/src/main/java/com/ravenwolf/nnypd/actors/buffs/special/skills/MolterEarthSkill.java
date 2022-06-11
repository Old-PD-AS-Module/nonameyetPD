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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.FellFire;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;


public class MolterEarthSkill extends BuffSkill {

    {
        CD = 100f;
    }

    @Override
    public void doAction(){
        Hero hero=Dungeon.hero;
        for (Mob mob : Dungeon.level.mobs) {
            if (Level.fieldOfView[mob.pos] && Level.distance(hero.pos,mob.pos)<=8 && mob.hostile) {

                //Debuff.add(mob, Burning.class,8);
                Debuff.add(mob, FellFire.class,8);
                Debuff.add(mob, Ensnared.class,4);
                CellEmitter.get( mob.pos ).burst( Speck.factory( Speck.BLAST_FIRE ), 4 );
            }
        }

        CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.BLAST_FIRE ), 4 );
        hero.sprite.cast( hero.pos );
        hero.busy();
        hero.spendAndNext( Actor.TICK );
        setCD(getMaxCD());
    }
}
