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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;


public class WarCry extends BuffSkill {

    {
        CD = 120f;
    }

    @Override
    public void doAction(){

        Hero hero=Dungeon.hero;
        for (Mob mob : Dungeon.level.mobs) {
            if( mob.hostile && mob.state != mob.PASSIVE && Level.distance(hero.pos, mob.pos)<=12 ) {
                mob.beckon(hero.pos);
            }
        }

        int duration=30;
        //duration+=duration*(1-hero.HP/(float)hero.HT);
        BuffActive.add( hero, Frenzy.class, duration );

        setCD(getMaxCD());
        hero.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);
        Dungeon.hero.spendAndNext(1f);

    }

}
