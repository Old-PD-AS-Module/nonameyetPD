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
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Piranha;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.HazardSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SubmergedPiranha extends Hazard {

    private int mobHP;
    private int mobHT;

    @Override
    public void press(int cell, Char ch ){

    }

    @Override
    protected boolean act(){
        if ((Dungeon.hero.isAlive() && Dungeon.hero.buff(MindVision.class)!=null)){
            spawnPiranha(null);
        }else {
            for (int n : Level.NEIGHBOURS9) {
                int pos = this.pos + n;
                Char c = Actor.findChar(pos);
                if (Level.insideMap(pos) && (c = Actor.findChar(pos)) != null && c.isFriendly() && c.invisible == 0 && Dungeon.findPath(c, c.pos, ((Hazard) this).pos, Level.water, Level.fieldOfView) != -1) {
                    spawnPiranha(c);
                    return true;
                }
            }
            for (int n : Level.NEIGHBOURS16) {
                try{
                    int pos = this.pos + n;
                    Char c = Actor.findChar(pos);
                    if (c != null && c.isFriendly() && c.invisible==0 && Dungeon.findPath(c, c.pos, this.pos, Level.water,Level.fieldOfView)!=-1) {
                        spawnPiranha(c);
                        return true;
                    }
                }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
            }
        }
        spend( TICK );
        return true;
    }

    public SubmergedPiranha(){
        super();
        spriteClass = InivisibleHazardSprite.class;
    }

    public void setStats(int cell, int HT, int HP){
        pos=cell;
        this.mobHT=HT;
        this.mobHP=HP;
    }

    public Mob spawnPiranha(Char c){
        Mob mob;
        int cell = this.pos;
        if (Actor.findChar( pos ) != null ){
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            for (int n : Level.NEIGHBOURS8) {
                int pos = cell + n;
                if( Level.water[ pos ] && Actor.findChar( pos ) == null )
                    candidates.add( pos );
            }
            if ( candidates.size() > 0 )
                cell= candidates.get(Random.Int(candidates.size()));
        }
        mob = new Piranha();
        mob.state = mob.WANDERING;

        if (c!=null) {
            mob.aggro(c);
            mob.beckon(c.pos);
        }

        mob.pos = cell;

        mob.HT=mobHT;
        mob.HP=mobHP;
        //if there is piranha nearby, delay one turn to prevent insta-death
        boolean delay =false;
        for (int n : Level.NEIGHBOURS8) {
            if (Actor.findChar( cell + n )instanceof Piranha) {
                delay = true;
                break;
            }
        }
        if (delay)
            GameScene.add(mob,TICK);
        else
            GameScene.add(mob);
        Actor.occupyCell( mob );
        destroy();

        return mob;
    }

    public void destroy() {
        super.destroy();
        sprite.kill();
        sprite.destroy();

    }

    private static final String HT = "HT";
    private static final String HP = "HP";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( HP, mobHP );
        bundle.put( HT, mobHT );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        mobHT = bundle.getInt( HT );
        mobHP = bundle.getInt( HP );

    }

    public static class InivisibleHazardSprite extends HazardSprite {
        @Override
        public int spritePriority() {
            return 3;
        }

        public void kill(){
            parent.erase(this);
        }

        public InivisibleHazardSprite() {
            super();
            visible=false;
        }

        @Override
        protected String asset() {
            return Assets.HAZ_PIRANHA;
        }
    }
}
