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
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlastWave;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.sprites.ArcaneOrbSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;


public class SummonArcaneOrbSkill extends BuffSkill {

    {
        CD = 100f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null &&  Dungeon.visible[target] && !Level.solid[target] && Actor.findChar(target) == null ) {

                spawnArcaneOrb( target);

                SummonArcaneOrbSkill skill = Dungeon.hero.buff(SummonArcaneOrbSkill.class);
                if (skill!=null)
                    skill.setCD(skill.getMaxCD());
            }else{
//                GLog.i("You cant cast that here");
                GLog.i("你不能在这里召唤它");
            }
        }

        @Override
//        public String prompt() {
//            return "Choose a spot to summon";
//        }
        public String prompt() {
            return "请选择召唤的位置";
        }
    };


    protected void spawnArcaneOrb( final int cell) {

        final  int stats = 6+Dungeon.hero.lvl;
        final int level = Dungeon.chapter();
        Dungeon.hero.sprite.cast(cell, new Callback() {
            @Override
            public void call() {
                ArcaneOrb.spawnAt( stats, level, cell);

                CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 6 );
                Sample.INSTANCE.play( Assets.SND_LIGHTNING , 1, 1, 0.75f );
                Dungeon.hero.spendAndNext(1f);
            }
        });
    }

    public static class ArcaneOrb extends NPC {

        private int stats;
        private int lvl;

        public ArcaneOrb() {
            super();
//            name = "arcane orb";
            name = "奥术之球";
            spriteClass = ArcaneOrbSprite.class;

            resistances.put(Element.Physical.class, Element.Resist.PARTIAL);
            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

            flying = true;
            baseSpeed = 2f;

            hostile = false;
            friendly = true;
        }

        public boolean sharedVision(){
            return true;
        }

        @Override
        public Element damageType() {
            return Element.ENERGY;
        }

        @Override
        public boolean isMagical() {
            return true;
        }

        @Override
        public boolean isEthereal() {
            return true;
        }

        public static ArcaneOrb spawnAt(int stats, int lvl, int pos) {
            // cannot spawn on walls or already occupied tiles
            if (!Level.solid[pos] && Actor.findChar(pos) == null) {

                ArcaneOrb orb = new ArcaneOrb();

                orb.pos = pos;
                orb.enemySeen = true;
                orb.state = orb.HUNTING;

                GameScene.add(orb, 1f);
                orb.sprite.spawn();

                orb.adjustStats(stats, lvl);
                orb.HP = orb.HT;

                return orb;
            } else {
                return null;
            }
        }

        public boolean attack( Char enemy ) {
            boolean hit = super.attack(enemy);
            if (hit && enemy.isAlive() && Dungeon.hero != null &&  Dungeon.hero.isAlive() &&
                    Dungeon.hero.belongings.weap1 instanceof MeleeWeapon &&  Dungeon.hero.belongings.weap1.isEnchanted()){
                if (Weapon.Enchantment.procced(enemy, Dungeon.hero.belongings.weap1.bonus)) {
                    Dungeon.hero.belongings.weap1.enchantment.public_proc_p( this, enemy, damageRoll());
                }

            }
            return hit;
        }

        @Override
        public int defenseProc( Char enemy, int damage,  boolean blocked ) {
            if (Dungeon.hero != null &&  Dungeon.hero.isAlive() &&
                    Dungeon.hero.belongings.armor != null &&  Dungeon.hero.belongings.armor.isEnchanted()){
                if (Armour.Glyph.procced(this, Dungeon.hero.belongings.armor.bonus))
                    //always proc with the uncursed enchantment
                    Dungeon.hero.belongings.armor.glyph.proc_p(enemy, this, damage * 2, false);
            }

            return super.defenseProc(enemy,damage,blocked);
        }

        //lose one HP after each attack
        public void onAttackComplete() {

            for (int n : Level.NEIGHBOURS8) {
                int auxPos = pos + n;

                if (Level.solid[auxPos] || auxPos == enemy.pos){//ignore main target
                    continue;
                }

                Char ch = Actor.findChar(auxPos);
                if (ch != null && !ch.isFriendly() && !(ch instanceof NPC)) {
                    attack( ch );
                }
            }
            if (Level.fieldOfView[pos])
                BlastWave.createAtPos( pos , sprite.blood(), false);

            super.onAttackComplete();
            HP -= lvl;
            if (HP <= 0)
                die(this);
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
                }
            }

            return super.act();
        }

        protected void adjustStats(int stats, int lvl) {

            HT = lvl * 20;
            armorClass = 0;

            minDamage = lvl * 3;
            maxDamage = lvl * 5;

            accuracy = stats;
            dexterity = stats / 2;

            this.stats = stats;
            this.lvl = lvl;
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
                    "A magical orb composed of pure energy, it is resistant to physical attacks, " +
                    "but not very durable and will quickly fade as time passes. " +
                    "While this entity is uncontrollable, it will chase enemies at great speed " +
                    "releasing waves of arcane energy that damage any surrounding foe.";
        }*/
        public String description() {
            return
                    "由奥术能量组成的球状体，可以抵抗部分物理攻击，" +
                            "这个实体是不可控的，拥有以极快的速度，并且会释放奥术能量攻击周围的敌人。而它会随着时间的流逝而逐渐消失。";
        }

        private static final String STATS	= "stats";
        private static final String CHARGES	= "lvl";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( STATS, stats );
            bundle.put( CHARGES, lvl);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            adjustStats( bundle.getInt( STATS ), bundle.getInt( CHARGES ) );
        }

    }

}
