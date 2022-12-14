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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mimic;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashMap;


public class SpectralBladesSkill extends BuffSkill {

    {
        CD = 80f;
    }

    private SpectralBlade weapSpectralBlade=new SpectralBlade();


    private void addTarget(final Hero curUser, Mob mob, final HashMap<Callback, Mob> targets){
        Callback callback = new Callback() {
            @Override
            public void call() {
                int damage = Dungeon.hero.damageRoll();

                Mob mob=targets.get( this );
                Dungeon.hero.hitEnemy(mob, damage) ;
                if(mob.isAlive()) {
                    BuffActive.add(mob, Revealed.class, 20, true);
                    if (mob.state != mob.HUNTING)
                        mob.state = mob.WANDERING;
                }

                targets.remove( this );
                if (targets.isEmpty()) {
                    curUser.spendAndNext( curUser.attackDelay() );
                }
            }
        };

        ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
                reset( curUser.pos, mob.pos, ItemSpriteSheet.SPECTRAL_BLADE, Random.Float(0.8f, 1.2f),new ItemSprite.Glowing(0x000000, 0.6f), callback );

        targets.put( callback, mob );
    }


    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    private void checkTargetsAtCell(int cell, ArrayList<Mob> mobs){
        if (Ballistica.isEdgeCell(cell))
            return;

        Char c = Actor.findChar(cell );
        if (c instanceof Mob && ((Mob)c).hostile) {
            Mob mob = (Mob)c;
            if (!mobs.contains(mob))
                mobs.add(mob);
        }
/*
        SubmergedPiranha subP = Hazard.findHazard(cell, SubmergedPiranha.class);
        if (subP != null) {
            Mob mob = subP.spawnPiranha(null);
            mobs.add(mob);
        }
*/
        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null && heap.type == Heap.Type.CHEST_MIMIC){
            Mimic m = Mimic.spawnAt( heap.hp, heap.pos, heap.items );
            if (m != null) {
                heap.destroy();
                mobs.add(m);
            }
        }
    }

    protected CellSelector.Listener striker = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                Hero hero=Dungeon.hero;
                if (target == hero.pos)
//                    GLog.w("You cant cast that on yourself");
                    GLog.w("你不能瞄准你自己");

                Ballistica.castToMaxDist(Dungeon.hero.pos, target, 7, false, false);

                HashMap<Callback, Mob> targets = new HashMap();
                ArrayList<Mob> mobs = new ArrayList();

                int remainingTargets = 6;
                if (hero.subClass == HeroSubClass.SNIPER)
                    remainingTargets+=2;

                hero.currentWeapon=weapSpectralBlade;

                for (int i=1; i <= Ballistica.distance; i++) {

                    int cell = Ballistica.trace[i];
                    //check possible targets at one range
                    for (int j : Level.NEIGHBOURS9) {
                        checkTargetsAtCell(cell + j, mobs);
                    }
                }



                int finalCell = Ballistica.trace[Ballistica.distance];
                //check possible targets at two range for max distance point
                if (Ballistica.distance > 4) {
                    for (int j : Level.NEIGHBOURS16) {
                        checkTargetsAtCell(finalCell + j, mobs);
                    }
                }

                //shoot a spectral blade for every mob, until reach max targets
                for (Mob mob : mobs) {
                    addTarget(hero, mob, targets);
                    remainingTargets--;
                    if (remainingTargets < 1)
                        break;
                }

                if (hero.subClass == HeroSubClass.SNIPER) {
                    //hit targets an additional time if remaining shots
                    while (remainingTargets > 0 && !mobs.isEmpty()) {
                        Mob mob = Random.element(mobs);
                        mobs.remove(mob);
                        addTarget(hero, mob, targets);
                        remainingTargets--;
                        if (remainingTargets < 1)
                            break;

                    }
                }

                //add additional blades going to a random cell for more punch
                for (int i = 0; i</*2 + */remainingTargets; i++) {
                    int dest=finalCell + Level.NEIGHBOURS8[Random.Int(8)];
                    if (!Ballistica.isEdgeCell(dest))
                        ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                            reset(hero.pos, dest, ItemSpriteSheet.SPECTRAL_BLADE, Random.Float(0.8f, 1.2f), new ItemSprite.Glowing(0x000000, 0.6f), null);
                }

                CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 4 );
                hero.sprite.cast( target );
                hero.busy();
                setCD(getMaxCD());
            }else {
//                GLog.w("You cant cast that here");
                GLog.w("你不能瞄准那里");
            }
        }

        @Override
//        public String prompt() {
//            return "Choose direction to attack";
//        }
        public String prompt() {
            return "请选择你的攻击方向";
        }
    };

/*
    protected CellSelector.Listener striker = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            Ballistica.cast(Dungeon.hero.pos, target, true, false);

            int AoE = 6;

            if (target != null && Ballistica.distance < 8) {

                Hero hero=Dungeon.hero;

                HashMap<Callback, Mob> targets = new HashMap();
                ArrayList<Mob> mobs = new ArrayList();

                int remainingTargets = 8;
                hero.currentWeapon=weapSpectralBlade;

                for (Mob mob : Dungeon.level.mobs) {
                    if ( mob.hostile && Level.distance(target,mob.pos) < AoE) {
                        mobs.add(mob);
                        addTarget(hero, mob, targets);
                        remainingTargets--;
                        if (remainingTargets < 1)
                            break;
                    }
                }

                //Check piranhas
                if (remainingTargets > 0) {
                    for (Hazard hazard : Dungeon.level.hazards) {
                        if (hazard instanceof SubmergedPiranha && Level.distance(target, hazard.pos) < AoE) {
                            Mob mob = ((SubmergedPiranha)hazard).spawnPiranha(null);
                            mobs.add(mob);
                            addTarget(hero, mob, targets);
                            remainingTargets--;
                            if (remainingTargets < 1)
                                break;
                        }
                    }
                }
                //Check mimics
                if (remainingTargets > 0) {
                    for (Heap heap : Dungeon.level.heaps.values()) {
                        if (heap.type == Heap.Type.CHEST_MIMIC) {
                            Mimic m = Mimic.spawnAt( heap.hp, heap.pos, heap.items );
                            if (m != null) {
                                heap.destroy();
                                mobs.add(m);
                                addTarget(hero, m, targets);
                                remainingTargets--;
                                if (remainingTargets < 1)
                                    break;
                            }
                        }
                    }
                }

                while (remainingTargets > 0 && !mobs.isEmpty()){
                    Mob mob =Random.element(mobs);
                    mobs.remove(mob);
                    addTarget(hero, mob, targets);
                    remainingTargets--;
                    if (remainingTargets < 1)
                        break;

                }

                //add additional blades going to a random cell for more punch
                for (int i = 0; i<2 + remainingTargets; i++)
                    ((MissileSprite)hero.sprite.parent.recycle( MissileSprite.class )).
                            reset( hero.pos, target +Level.NEIGHBOURS8[Random.Int(8)], ItemSpriteSheet.SPECTRAL_BLADE, Random.Float(0.8f, 1.2f),new ItemSprite.Glowing(0x000000, 0.6f), null );



                CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 4 );
                hero.sprite.cast( target );
                hero.busy();
                setCD(getMaxCD());
            }else {
                GLog.w("You cant cast that here");
            }
        }

        @Override
        public String prompt() {
            return "Choose direction to attack";
        }
    };

*/

    public static class SpectralBlade extends MeleeWeapon {

        {
//            name = "spectral blade";
            name = "幻影飞刀";
        }

        public SpectralBlade() {
            super( 0 );
        }

        private int baseDmg(Hero hero){
            //float modifier = hero.ringBuffsHalved( RingOfAccuracy.Accuracy.class );
            //return (int)(hero.attackSkill*modifier);

            return 8+(hero.lvl/2);
            //return 10+(hero.lvl/2);
        }

        @Override
        public int min( int bonus ) {
            return baseDmg(Dungeon.hero)/2;
        }

        @Override
        public int max( int bonus ) {
            return baseDmg(Dungeon.hero)/**2/3*/;
        }

        @Override
        public boolean increaseCombo(){
            return false;
        }

        @Override
        public boolean penetrateAC(){
            return true;
        }


        @Override
        protected int getDamageSTBonus(Hero hero){
            //damage is not increased based on Hero ST. only based on accuracy
            return 0;
        }

        @Override
        public boolean isIdentified() {	return true; }
    }
}
