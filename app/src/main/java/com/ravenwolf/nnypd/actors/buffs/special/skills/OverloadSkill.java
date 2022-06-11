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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Overload;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.watabou.noosa.audio.Sample;


public class OverloadSkill extends BuffSkill {

    {
        CD = 80f;
    }

    @Override
    public void doAction(){

        Hero hero=Dungeon.hero;
        int duration = 20;
        if (hero.subClass == HeroSubClass.CONJURER)
            duration +=5;
        BuffActive.add( hero, Overload.class, duration);
        hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 12);
        Sample.INSTANCE.play(Assets.SND_BEACON,1,1,0.5f);
        Sample.INSTANCE.play(Assets.SND_TELEPORT);
        Dungeon.hero.spendAndNext(1f);

        setCD(getMaxCD());

    }
}
