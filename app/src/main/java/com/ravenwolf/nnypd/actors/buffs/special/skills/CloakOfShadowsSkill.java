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
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.ShroudingFog;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;

public class CloakOfShadowsSkill extends BuffSkill {

    {
        CD = 120f;
    }

    @Override
    public void doAction() {

        BuffActive.add( target, com.ravenwolf.nnypd.actors.buffs.bonuses.CloakOfShadows.class, 20);
        for (int n : Level.NEIGHBOURS9) {
            int cell = target.pos + n;
            GameScene.add(Blob.seed(cell, 10, ShroudingFog.class),-Actor.TICK);
        }
        Sample.INSTANCE.play(Assets.SND_MELD,1,1,0.5f);
        Dungeon.hero.spendAndNext(1f);
        setCD(getMaxCD());

    }
}
