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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.mobs.npcs.Ghost;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.levels.features.Door;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.Pushing;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.GooSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;


public class GooSpawn extends MobEvasive {

    private static final float SPLIT_DELAY	= 1f;
    private static final int SPAWN_HEALTH = 8;

    public GooSpawn() {

        super( 3 );

        name = "小咕咕";
        spriteClass = GooSprite.SpawnSprite.class;

        minDamage += tier;
        maxDamage += tier;
        HP=HT+=EXP*2;

        armorClass = 0;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
        resistances.put(Element.Body.class, Element.Resist.IMMUNE);
        resistances.put(Element.Shock.class, Element.Resist.VULNERABLE);
	}

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isEthereal() {
        return true;
    }

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();

        if( hasBuff( Chilled.class ) ){
            resistances.put( Element.Physical.class, Element.Resist.VULNERABLE );
        }

        return resistances;
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }

    @Override
    public boolean act() {

        if ((state == SLEEPING || Level.water[pos]) && HP < HT && buff(Chilled.class) == null) {
            if (sprite.visible)
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            HP++;
        }
        return super.act();
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }
        if ( type == Element.PHYSICAL && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH * 2) ) {

            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;

            for (int n : Level.NEIGHBOURS8) {
                if (passable[pos + n] && Actor.findChar(pos + n) == null) {
                    candidates.add(pos + n);
                }
            }

            if (candidates.size() > 0) {

                GooSpawn clone = new GooSpawn();

                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                clone.HT = dmg * 2;
                clone.HP = clone.HT / 2;
                clone.EXP = 0;

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR_CLOSED) {
                    Door.enter(clone.pos);
                }

                Dungeon.level.press(clone.pos, clone);

                GameScene.add(clone, SPLIT_DELAY);

                Burning burning = buff( Burning.class );
                if ( burning != null) {
                    BuffActive.add( clone, Burning.class, burning.getDuration() );
                }
                Chilled chilled = buff( Chilled.class );
                if ( chilled != null) {
                    BuffActive.add( clone, Chilled.class, chilled.getDuration() );
                }

                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );
            }
        }

        super.damage( dmg, src, type );
    }

    @Override
    public String description(){

        return "人们对黏液和它们的分裂体知之甚少。很有可能它甚至不是一种生物，而是一种从下水道中收集的物质的集合体，它获得了某种基本但非常邪恶的感知。";
    }

}
