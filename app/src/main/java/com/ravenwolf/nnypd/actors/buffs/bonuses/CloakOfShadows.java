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
package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.ShroudingFog;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class CloakOfShadows extends Bonus {

    @Override
    public String toString() {
//        return "Cloak Of Shadows";
        return "暗影迷雾";
    }

    @Override
    public String statusMessage() { return "暗影迷雾"; /*return "cloak of shadows";*/ }

    @Override
    public int icon() {
        return BuffIndicator.CLOACK;
    }

    public int actingPriority(){
        return 10;
    }

/*    @Override
    public String description() {
        return "A dense fog conceal your movements. The fog block enemy vision, but you ara able to see over it. Protecting you from ranged enemies " +
                "and allowing you to ambush unsuspecting victims with devastating attacks.";
    }*/
    @Override
    public String description() {
        return "浓雾掩盖了你的行动。并阻挡了敌人的视线，使你免受于远程敌人的攻击，但你依然可以看透这些迷雾。并允许你准备下一次的伏击";
    }


    @Override
    public boolean act() {
        for (int n : Level.NEIGHBOURS9) {
            int cell = target.pos + n;
            GameScene.add(Blob.seed(cell, 10, ShroudingFog.class),-Actor.TICK);
        }
        return super.act();
    }

}
