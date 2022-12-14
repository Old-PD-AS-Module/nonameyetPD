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

package com.ravenwolf.nnypd.actors.hazards;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.HauntedArmor;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HauntedArmorSleep extends Hazard {

    public BodyArmor armor;
    //used to wake them when using a scroll of challenge
    public boolean challenged=false;

    @Override
    public void press(int cell, Char ch ){
    }

    @Override
    protected boolean act(){
        if (challenged/*(Dungeon.hero.isAlive() && Dungeon.hero.buff(MindVision.class)!=null)*/){
            spawnHauntedArmor(Dungeon.hero);
        }else {
            for (int n : Level.NEIGHBOURS9) {
                int pos = this.pos + n;
                Char c = Actor.findChar(pos);
                if (c != null && c.isFriendly()) {
                    spawnHauntedArmor(c);
                    break;
                }
            }
        }
        spend( TICK );
        return true;
    }

    public HauntedArmorSleep(){
        super();
        spriteClass = SubmergedPiranha.InivisibleHazardSprite.class;
    }

    public void setStats(int cell, BodyArmor armor){
        pos=cell;
        this.armor=armor;
    }

    public void spawnHauntedArmor(Char c){
        Mob mob;
        int cell = this.pos;
        Heap heap = Dungeon.level.heaps.get(cell);
        if (heap!=null) {
            Item it = heap.items.getLast();

            if (it instanceof BodyArmor) {
                heap.items.remove(it);
                if (heap.items.isEmpty())
                    heap.destroy();

                if (Actor.findChar(pos) != null) {
                    ArrayList<Integer> candidates = new ArrayList<Integer>();
                    for (int n : Level.NEIGHBOURS8) {
                        int pos = cell + n;
                        if (Actor.findChar(pos) == null && !Level.solid[pos])
                            candidates.add(pos);
                    }
                    if (candidates.size() > 0)
                        cell = candidates.get(Random.Int(candidates.size()));
                }
                armor.identify(Item.ENCHANT_KNOWN);
                //always uncursed armors?
                //armor.cursed=false;
                mob = new HauntedArmor(armor);
                mob.state = mob.HUNTING;
                mob.spend(Actor.TICK);

                if (c != null) {
                    mob.aggro(c);
                    mob.beckon(c.pos);
                }

                if (Dungeon.visible[cell])
//                    GLog.n( "The armor is possessed by an evil spirit!" );
                    GLog.n( "恶灵正依附在这个护甲上！" );
                mob.pos = cell;
                CellEmitter.get(cell).burst(ShadowParticle.CURSE, 8);
                Sample.INSTANCE.play(Assets.SND_CURSED, 1, 1, 0.5f);

                GameScene.add(mob);
                Actor.occupyCell(mob);
            }
        }

        destroy();
    }

    public void destroy() {
        super.destroy();
        sprite.kill();
        sprite.killAndErase();
    }

    private static final String ARMOR	= "armor";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ARMOR, armor );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        armor = (BodyArmor) bundle.get( ARMOR );
    }

}
