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
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class ShadowStrike extends BuffSkill {


    {
        CD = 80f;
    }

    final ArrayList<Mob> targets = new ArrayList();

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }

    protected CellSelector.Listener striker = new CellSelector.Listener() {

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

                if (enemy instanceof Mob) {

                    if (enemy != null && Dungeon.visible[target] && Level.distance(enemy.pos, hero.pos) <= 8 && Ballistica.cast(hero.pos, enemy.pos, false, false)==enemy.pos) {

                        ShadowStrike skill = hero.buff(ShadowStrike.class);
                        if (skill != null)
                            skill.setCD(skill.getMaxCD());

                        targets.add( (Mob)enemy );

                        if (hero.subClass == HeroSubClass.SLAYER) {
                            int additionalTargets = 4;
                            for (int j : Level.NEIGHBOURS8) {
                                Char c = Actor.findChar(target + j );
                                if (c instanceof Mob && ((Mob)c).hostile) {
                                    targets.add((Mob) c);
                                    additionalTargets--;
                                    if (additionalTargets==0)
                                        break;
                                }
                            }
                            if (additionalTargets > 0) {
                                for (int j : Level.NEIGHBOURS16) {
                                    try {
                                        Char c = Actor.findChar(target + j);
                                        if (c instanceof Mob && ((Mob) c).hostile) {
                                            targets.add((Mob) c);
                                            additionalTargets--;
                                            if (additionalTargets == 0)
                                                break;
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                    }//could be searching beyond the map limits
                                }
                            }
                        }
                        doAttack(true);
                        hero.busy();

/*
                        appear( hero, enemy.pos );

                        ((HeroSprite)hero.sprite).attack(hero.currentWeapon,enemy.pos, new Callback() {
                            @Override
                            public void call() {

                                Hero hero = Dungeon.hero;
                                int damage = hero.damageRoll();
                                //weapon penalty reduce damage efficiency
                                float damagePenalty =1- hero.currentWeapon.currentPenalty(hero,hero.currentWeapon.str())*0.025f;

                                for (int j = 0; j < 3; j++) {
                                    int auxDmg = hero.damageRoll();
                                    if (auxDmg > damage)
                                        damage = auxDmg;
                                }
                                damage=(int)(damage*damagePenalty);

                                if (hero.subClass == HeroSubClass.ASSASSIN) {
                                    //bonus damage based on missing health
                                    int bonus = (int) (damage * (1 - (float) enemy.HP / enemy.HT));
                                    //make them wandering so attack is a backstab
                                    ((Mob) enemy).looseTrack();
                                    damage += bonus;
                                }
                                hero.hitEnemy(enemy,  damage);
                                //BuffActive.addFromDamage(enemy, Withered.class, damage*2);

                                appear( hero, hero.pos );
                                hero.spendAndNext( hero.attackDelay() );
                            }});

                        CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 4 );
                        */
                    } else
//                        GLog.w("You need to select a reachable enemy");
                        GLog.w("你需要选择一个攻击范围内的敌人");
                } else
//                    GLog.w("You need to select a valid enemy");
                    GLog.w("你需要选择一个有效的敌人");
            }else
//                GLog.w("You need to target an enemy");
                GLog.w("你需要瞄准敌人");

        }

        @Override
//        public String prompt() {
//            return "Choose an enemy to attack";
//        }
        public String prompt() {
            return "请选择要攻击的敌人";
        }
    };
/*
    @Override
    public void doAction() {

        Hero hero=Dungeon.hero;
        intialPos=hero.pos;

        MeleeWeapon curWep = Dungeon.hero.belongings.weap1 instanceof MeleeWeapon ? (MeleeWeapon)Dungeon.hero.belongings.weap1 :
                Dungeon.hero.belongings.weap2 instanceof MeleeWeapon ? (MeleeWeapon) Dungeon.hero.belongings.weap2 : null;

        if (curWep == null) {
            GLog.i("You need a melee weapon to use this skill");
            return;
        }
        Dungeon.hero.currentWeapon = curWep;

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.fieldOfView[mob.pos] && Level.distance(hero.pos,mob.pos)<=6 && mob.hostile) {
                targets.add( mob );
            }
        }

        setCD(getMaxCD());
        doAttack();
        hero.busy();
    }

*/
    private void doAttack(final boolean primaryTarget){
        Hero hero = Dungeon.hero;

        if (targets.isEmpty()) {
            appear( hero, hero.pos );
            //CharmOfBlink.appear( hero, intialPos );
            hero.spendAndNext( hero.attackDelay() );
            return;
        }

        final Mob mob = targets.remove(0);
        if (mob != null && mob.isAlive()) {
            appear(hero, mob.pos);
            ((HeroSprite) hero.sprite).attack(hero.currentWeapon, mob.pos, new Callback() {
                @Override
                public void call() {

                    Hero hero = Dungeon.hero;
                    int damage = hero.damageRoll();

                    for (int j = 0; j < 3; j++) {
                        int auxDmg = hero.damageRoll();
                        if (auxDmg > damage)
                            damage = auxDmg;
                    }
                    //make it wandering so attack is a backstab
                    mob.looseTrack();
                    //weapon penalty reduce damage efficiency
                    damage = (int) (damage * hero.currentWeapon.penaltyFactor(hero, true));
                    if (!primaryTarget)
                        damage = (int)(damage * 0.66f);
                    if (hero.subClass == HeroSubClass.ASSASSIN) {
                        //bonus damage based on missing health
                        int bonus = (int) (damage * (1 - (float) mob.HP / mob.HT));
                        damage += bonus + damage;
                    }
                    hero.hitEnemy(mob, damage);
                    doAttack(false);
                }
            });

            CellEmitter.get(hero.pos).burst(Speck.factory(Speck.WOOL), 4);
        }else {
            doAttack(false);
        }

    }


    public void appear( Char ch, int pos ) {

        ch.sprite.interruptMotion();

        //ch.move( pos );
        ch.sprite.place( pos );

        if (ch.invisible == 0) {
            ch.sprite.alpha( 0 );
            ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
        }

        //ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
        Sample.INSTANCE.play( Assets.SND_TELEPORT );
    }
}
