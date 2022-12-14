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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.particles.AltarParticle;
import com.ravenwolf.nnypd.visuals.sprites.SpiritWolfSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;


public class SummonSpiritWolfSkill extends BuffSkill {

    {
        CD = 100f;
    }

    public SpiritWolf wolfInstance;

    @Override
    public boolean act() {
        if (getSpiritWolf() != null) {
            spend(TICK);
            return true;
        }else
            return super.act();
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null &&  Dungeon.visible[target] &&  !Level.solid[target] && !Level.chasm[target] && Actor.findChar(target) == null ) {

                spawnSpiritWolf( target);
                SummonSpiritWolfSkill skill = Dungeon.hero.buff(SummonSpiritWolfSkill.class);
                if (skill!=null)
                    skill.setCD(skill.getMaxCD());
            }else{
//                GLog.w("You cant cast that here");
                GLog.w("你不能瞄准那里");
            }
        }

        @Override
//        public String prompt() {
//            return "Choose a spot to summon";
//        }
        public String prompt() {
            return "选择要召唤的地点";
        }
    };

    public SpiritWolf getSpiritWolf(){
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof SpiritWolf) {
                return (SpiritWolf)mob;
            }
        }
        return null;
    }


    protected void spawnSpiritWolf(final int cell) {

        final int stats = 6 + Dungeon.hero.lvl;
        final int level = Dungeon.chapter() ;

        Dungeon.hero.sprite.cast(cell);
        MagicMissile.blueFire(Dungeon.hero.sprite.parent, Dungeon.hero.pos, cell, new Callback() {
            @Override
            public void call() {
                SpiritWolf.spawnAt(stats,level, cell);
                CellEmitter.center( cell ).burst( AltarParticle.FACTORY, 6 );
                Sample.INSTANCE.play( Assets.SND_LIGHTNING, 1,1,0.75f);
                Dungeon.hero.spendAndNext(1f);
            }
        });

    }


    private static final String WOLF	= "wolf";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WOLF, wolfInstance );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        wolfInstance = (SpiritWolf) bundle.get( WOLF );
    }

    public static class SpiritWolf extends NPC {

        private int stats;
        private int lvl;

        public SpiritWolf() {
            super();
//            name = "spirit wolf";
            name = "幽魂之狼";
            spriteClass = SpiritWolfSprite.class;

            //resistances.put(Element.Physical.class, Element.Resist.PARTIAL);
            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

            resistances.put(Element.Frost.class, Element.Resist.PARTIAL);
            resistances.put(Element.Flame.class, Element.Resist.PARTIAL);
            resistances.put(Element.Acid.class, Element.Resist.PARTIAL);
            resistances.put(Element.Shock.class, Element.Resist.PARTIAL);

            baseSpeed = 2f;

            hostile = false;
            friendly = true;
        }
/*
        @Override
        public Element damageType() {
            return Element.ENERGY;
        }
*/
        public boolean ignoresMissiles(){
            return true;
        }

        public boolean sharedVision(){
            return true;
        }

        @Override
        public boolean isMagical() {
            return true;
        }

        @Override
        public boolean isEthereal() {
            return true;
        }

        public static SpiritWolf spawnAt(int stats, int lvl, int pos) {
            // cannot spawn on walls, chasms or already occupied tiles
            if (!Level.solid[pos] && !Level.chasm[pos] && Actor.findChar(pos) == null) {

                SpiritWolf wolf = new SpiritWolf();

                wolf.pos = pos;
                wolf.enemySeen = true;
                wolf.state = wolf.HUNTING;

                GameScene.add(wolf, 1f);
                Dungeon.level.press(wolf.pos, wolf);

                wolf.sprite.turnTo(Dungeon.hero.pos, pos);
                wolf.sprite.spawn();

                wolf.adjustStats(stats, lvl);
                wolf.HP = wolf.HT;

                return wolf;
            } else {
                return null;
            }
        }

        @Override
        public float attackSpeed() {
            return super.attackSpeed() * 2.0f;
        }

        @Override
        protected boolean act() {
            HP -= lvl;
            if (HP <= 0) {
                die(this);
                return true;
            }

            //will attempt to stay close to hero
            Hero hero=Dungeon.hero;
            if(hero !=null && hero.isAlive()){

                if (enemy==null || !enemy.isAlive()){
                    if(Level.distance(pos,hero.pos)>4)
                        target = hero.pos;
                    else
                        enemy = hero.enemy();
                }
            }

            return super.act();
        }

        protected void adjustStats(int stats, int lvl) {

            HT = lvl * 40;
            armorClass = 0;

            minDamage = lvl * 2;
            maxDamage = lvl * 4;

            accuracy = stats;
            dexterity = stats;

            this.stats = stats;
            this.lvl = lvl;
        }


        public void die( Object src, Element dmg ) {
            Sample.INSTANCE.play( Assets.SND_GHOST, 1,1,0.75f);
            super.die(src, dmg);
        }


        @Override
        public float awareness(){
            return super.awareness() * ( 1.0f + lvl * 0.05f );
        }

        @Override
        public float stealth(){
            return super.stealth() * ( 1.0f + lvl * 0.05f );
        }


        @Override
        public void interact() {
            //swap position
            int curPos = pos;

            moveSprite( pos, Dungeon.hero.pos );
            move( Dungeon.hero.pos );

            Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
            Dungeon.hero.move( curPos );

            Dungeon.hero.spend( 1 / Dungeon.hero.moveSpeed() );
            Dungeon.hero.busy();

        }

        @Override
        protected Char chooseEnemy() {
            //attack the nearest enemy
            if (enemy == null || !enemy.isAlive() || enemy.invulnerable() || !Level.adjacent(pos, enemy.pos)) {
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && !mob.isFriendly() && !mob.invulnerable() && Level.fieldOfView[mob.pos] &&
                            (enemy == null || !enemy.isAlive() || Level.distance(pos, enemy.pos) > Level.distance(pos, mob.pos))){
                            enemy = mob;
                    }
                }
            }
            return enemy;
        }

        @Override
/*        public String description() {
            return
                "A wolf familiar form the spirit realm. " +
                "It will chase and hunt your enemies at tremendous speed." +
                "Due to its incorporeal nature, it will not block missiles";
        }        */
        public String description() {
            return
                "一只有灵魂之力构成的幽魂之狼. " +
                "它会以极快的速度追捕你的敌人" +
                "由于它没有实体，所以远程投掷物将会穿透它";
        }

        private static final String STATS	= "stats";
        private static final String LVL = "lvl";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( STATS, stats );
            bundle.put(LVL, lvl);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            adjustStats( bundle.getInt( STATS ), bundle.getInt(LVL) );
        }

    }

}
