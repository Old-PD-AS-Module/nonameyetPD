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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.sprites.ShamanSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GnollShaman extends MobCaster {

    private boolean charged = false;
    private int barrierCD = 0;

    private static final String BARRIER_CD = "barrierCD";
    private static final String CHARGED = "charged";

    public GnollShaman() {

        super( Dungeon.chapter()*2+4 );

        name = "豺狼萨满";
		spriteClass = ShamanSprite.class;

        loot = Generator.random(Generator.Category.SCROLL);
        lootChance = 0.2f;

        HUNTING = new Hunting();
	}

    @Override
    public String getTribe() {
        return TRIBE_GNOLL;
    }

    @Override
    public boolean act() {

        if (barrierCD>0)
            barrierCD--;

        if( !enemySeen )
            charged = false;

        return super.act();
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !Level.adjacent( pos, enemy.pos ) && !charged ) {

            charged = true;

            if( Dungeon.visible[ pos ] ) {
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_WHITE, 15);
            }

            spend( attackDelay() );

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }


    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.blueLight(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() + minDamage/2 : super.damageRoll();
    }

    @Override
    public boolean cast( Char enemy ) {
        return castBolt(enemy, damageRoll(),false,Element.ENERGY);
    }

    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (barrierCD <= 0 && !charged) {
                //search first for allied gnolls
                for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {

                    if (mob != GnollShaman.this && mob.getTribe().equals(getTribe()) && mob.HP < mob.HT && !(mob instanceof GnollShaman) && Level.distance(pos, Dungeon.hero.pos) < 6
                            && canCastBolt(mob) && !mob.hasBuff(Shielding.class) && isFriendly() == mob.isFriendly()) {
                        final Mob target = mob;
                        sprite.cast(target.pos, new Callback() {
                            @Override
                            public void call() {
                                MagicMissile.blueLight(sprite.parent, pos, target.pos, new Callback() {
                                    @Override
                                    public void call() {
                                        BuffActive.add(target, Shielding.class, Random.IntRange(10, 16));
                                    }
                                });
                                spend(TICK);
                                next();
                                sprite.idle();//prevent executing on attack after cast animation end
                            }
                        });
                        Sample.INSTANCE.play(Assets.SND_ZAP);
                        barrierCD = Random.IntRange(22, 30);
                        return false;
                    }
                }
                //cast on themselves if wounded
                if (HP < HT && Random.Int(3) == 0) {
                    sprite.cast(pos, new Callback() {
                        @Override
                        public void call() {
                            BuffActive.add(GnollShaman.this, Shielding.class, Random.IntRange(10, 16));
                            spend(TICK);
                            next();
                            sprite.idle();//prevent executing on attack after cast animation end
                        }
                    });
                    Sample.INSTANCE.play(Assets.SND_ZAP);
                    barrierCD = Random.IntRange(22, 30);
                    return false;
                }

            }
            return super.act(enemyInFOV,justAlerted);
        }
    }

    @Override
    public String description() {
        return
                "最有智慧的豺狼精通萨满魔法，这些豺狼萨满在战斗时更喜欢使用魔法以弥补力量的不足，并对那些质疑它们地位的人毫不犹豫地进行攻击。";
    }
    
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
        bundle.put( BARRIER_CD, barrierCD );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
        barrierCD = bundle.getInt( BARRIER_CD );
    }
}
