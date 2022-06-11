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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.BanditSprite;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bandit extends MobPrecise {


    protected static final String TXT_STOLE	= "%s偷走了你的金币！";
    protected static final String TXT_SCAPED = "%s带着你的金币逃走了！";


    public Bandit() {

        super( 8 );

        name = "疯狂强盗";
        spriteClass = BanditSprite.class;

        WANDERING = new Wandering();
        FLEEING = new Fleeing();
        state = HUNTING;

        loot = Generator.random(Generator.Category.RING);
        lootChance = 0.2f;
	}

    public int stoledGold=0;

    public String getTribe() {
        return TRIBE_LONER;
    }

    @Override
    public float moveSpeed() {
        if (stoledGold>0) return super.moveSpeed()*0.75f;
        else return super.moveSpeed();
    }

    @Override
    public void die( Object cause, Element dmg ) {
        if (stoledGold >0) {
            Gold gold=new Gold();
            gold.quantity=stoledGold;
            Dungeon.level.drop( gold, pos ).sprite.drop();
        }
        super.die( cause, dmg );
    }

    @Override
    public String description(){

        return "虽然这些囚犯逃出了他们的牢房，但这个地方仍然是关押着他们的监狱。随着时间的推移，这个地方摧毁了他们的心智以及对自由的希望。在很久以前，这些疯狂的小偷和强盗就已经忘记了它们是谁以及它们在为什么而偷窃。\n\n这些敌人更愿意偷走你的东西然后逃跑而不是战斗。一定不要让它们离开你的视线，否则你可能再也看不到自己的被盗物品了。";

    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {
        if (!blocked && isAlive()) {
            if (stoledGold == 0 && enemy instanceof Hero && steal()) {
                state = FLEEING;
                BuffActive.addFromDamage( enemy, Dazed.class, damage * 5/2);
            }
        }
        return damage;
    }


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( "GOLD", stoledGold );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        stoledGold = bundle.getInt( "GOLD" );
    }

    protected boolean steal() {

        int steal=Math.min(Dungeon.gold,Random.Int(300,450));
        if (steal>50) {
            Dungeon.gold -= steal;

            Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 1.5f);
            GLog.w(TXT_STOLE, this.name);
            stoledGold=steal;
        }

        return true;
    }

    @Override
    public boolean isScared() {

        return stoledGold > 0 || super.isScared();
    }


    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            super.act(enemyInFOV, justAlerted);

            //if an enemy is just noticed and the thief posses an item, run, don't fight.
            if (state == HUNTING && stoledGold > 0 && !isFriendly()){
                state = FLEEING;
            }

            return true;
        }
    }

    private class Fleeing extends Mob.Fleeing {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            super.act(enemyInFOV, justAlerted);

            if (state == WANDERING && Dungeon.hero !=null &&  stoledGold > 0 && !isFriendly()){
                if (!enemyInFOV && Dungeon.level.distance(pos, Dungeon.hero.pos) >= 6) {

                    int count = 32;
                    int newPos;
                    do {
                        newPos = Dungeon.level.randomRespawnCell();
                        if (count-- <= 0) {
                            break;
                        }
                    }
                    while (newPos == -1 || Dungeon.level.fieldOfView[newPos] || Dungeon.level.distance(newPos, pos) < (count / 3));

                    if (newPos != -1) {

                        if (Dungeon.level.fieldOfView[pos])
                            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
                        pos = newPos;
                        sprite.place(pos);
                        sprite.visible = Dungeon.level.fieldOfView[pos];
                        if (Dungeon.level.fieldOfView[pos])
                            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

                    }

                    if (stoledGold > 0) GLog.w(TXT_SCAPED, "疯狂强盗");
                    stoledGold = 0;
                    state = WANDERING;

                } else {
                    state = FLEEING;
                    target=Dungeon.hero.pos;
                }
            }

            return true;

        }

        @Override
        protected void nowhereToRun() {
            if (buff( Tormented.class ) == null && buff( Charmed.class ) == null) {
                if (enemySeen) {
                    sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
                    state = HUNTING;
                }
            } else {
                super.nowhereToRun();
            }
        }
    }

}
