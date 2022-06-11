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
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.watabou.utils.Callback;

public class Fury extends BuffSkill {

    {
        CD = 80f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }

    protected static CellSelector.Listener striker = new CellSelector.Listener() {

        private int count;
        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                Hero hero = Dungeon.hero;
                MeleeWeapon curWep = hero.belongings.weap1 instanceof MeleeWeapon ? (MeleeWeapon) hero.belongings.weap1 :
                        hero.belongings.weap2 instanceof MeleeWeapon ? (MeleeWeapon) hero.belongings.weap2 : null;

                if (curWep == null) {
//                    GLog.w("You need a melee weapon to use this skill");
                    GLog.w("你需要装备一个近战武器来使用此技能");
                    return;
                }
                hero.currentWeapon = curWep;
                final Char enemy = Actor.findChar(target);

                if (hero == enemy) {
//                    GLog.w("You cant hit yourself");
                    GLog.w("你不能攻击自己");
                    return;
                }
                if (enemy != null && (Level.adjacent(enemy.pos, hero.pos) ||
                        Level.distance(enemy.pos, hero.pos) <= curWep.reach() && Ballistica.cast(hero.pos, enemy.pos, false, true)==enemy.pos)) {

                    Fury skill = hero.buff(Fury.class);
                    if (skill != null)
                        skill.setCD(skill.getMaxCD());

                    count = 4;
                    if (hero.subClass == HeroSubClass.KNIGHT) {
                        count++;
                        if (hero.belongings.weap2 instanceof Shield) {
                            BuffActive.add(hero, Guard.class, ((Shield)hero.belongings.weap2).guardTurns(), true);
                        }else if (hero.belongings.weap1 instanceof MeleeWeapon || hero.belongings.weap2 instanceof MeleeWeapon ) {
                            BuffActive.add(hero, Guard.class, 5, true);
                        }
                    }
                    count = (int) (count * curWep.speedFactor(hero));
                    ((HeroSprite) hero.sprite).attack(hero.currentWeapon, enemy.pos, new Callback() {
                        @Override
                        public void call() {
                            doAttack(enemy);
                        }
                    });

                } else
//                    GLog.w("You need to select a valid nearby enemy");
                    GLog.w("请选择一个有效的敌人");
            }else
//                GLog.w("You need to target a nearby enemy");
                GLog.w("你需要瞄准附近的敌人");

        }

        private void doAttack(final Char enemy){

            Hero hero = Dungeon.hero;

            int damage = hero.damageRoll();
            //weapon penalty reduce damage efficiency
            //float damagePenalty =1- hero.currentWeapon.currentPenalty(hero,hero.currentWeapon.str())*0.025f;
            damage=(int)(damage * hero.currentWeapon.penaltyFactor(hero, true)/*damagePenalty*/);
            count--;
            int auxDmg=damage;
            if (hero.subClass == HeroSubClass.BERSERKER && count == 0){
                auxDmg += damage/3;
            }

            hero.hitEnemy(enemy, auxDmg);
            CellEmitter.get(enemy.pos).start(Speck.factory(Speck.BLAST_FIRE, true), 0.05f, 2);

            if (enemy.isAlive() && count>0) {
                ((HeroSprite)hero.sprite).attack(hero.currentWeapon,enemy.pos, new Callback() {
                    @Override
                    public void call() {
                        doAttack(enemy);
                    }});

            }else {

                if (hero.subClass == HeroSubClass.BERSERKER){
                    int finalDmg = damage + damage/3;
                    hero.currentWeapon.setIncreaseCombo(false);
                    for (int j : Level.NEIGHBOURS8) {
                        Char c = Actor.findChar(enemy.pos + j );
                        if (c instanceof Mob && ((Mob)c).hostile) {
                            hero.hitEnemy(c, finalDmg);
                            CellEmitter.get(c.pos).start(Speck.factory(Speck.BLAST_FIRE, true), 0.05f, 2);
                        }
                    }
                }
                hero.currentWeapon.setIncreaseCombo(true);
                hero.sprite.idle();
                hero.spendAndNext(1f);
            }

        }

        @Override
//        public String prompt() {
//            return "Choose enemy to strike";
//        }
        public String prompt() {
            return "选择要攻击的敌人";
        }
    };
}
