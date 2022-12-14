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
package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.mobs.Robot;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.NagaBossLevel;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.BlastParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.SmokeParticle;
import com.watabou.utils.Random;

public class Explosion {

	// Returns true, if this cell is visible
    public static boolean affect(int c, int r, int radius, int damage, Object source) {
        Heap heap;
        boolean terrainAffected = false;
        if (Dungeon.visible[c]) {
            CellEmitter.get(c).burst(BlastParticle.FACTORY, 12 / (r + 1));
            CellEmitter.get(c).burst(SmokeParticle.FACTORY, 6 / (r + 1));
        }

        if (Level.flammable[c]) {
            Level.set(c, Terrain.EMBERS);
            GameScene.updateMap(c);
            terrainAffected = true;
        }

        if (NagaBossLevel.isDestructibleStatue(c)) {
            Level.set(c, Terrain.EMPTY_DECO);
            GameScene.updateMap(c);
            CellEmitter.get( c ).start(Speck.factory(Speck.ROCK), 0.07f, 6);
            terrainAffected = true;
        }
        if (NagaBossLevel.isDestructibleStatue(c)) {
            Level.set(c, 1);
            GameScene.updateMap(c);
            CellEmitter.get(c).start(Speck.factory(8), 0.07f, 6);
            terrainAffected = true;
        }
        Char ch = Actor.findChar(c);
        if (ch != null && ch.isAlive()) {
            int mod = (ch.totalHealthValue() * damage) / 50;
            int dmg = ((Random.IntRange(mod / 2, mod) + Random.IntRange(damage / 2, damage)) * ((radius - r) + 2)) / (radius + 2);
            if (dmg > 0) {
                ch.damage(ch.absorb(dmg, true), source, Element.PHYSICAL);
                if (ch.isAlive()) {
                    BuffActive.addFromDamage(ch, Dazed.class, damage);
                }
            }
        }
        if (Dungeon.hero.isAlive() && (heap = Dungeon.level.heaps.get(c)) != null) {
            if (source instanceof Robot) {
                heap.blast("爆炸");
            } else {
                heap.blast("爆炸");
            }
        }
        Dungeon.level.press(c, null);
        return terrainAffected;
    }
}
