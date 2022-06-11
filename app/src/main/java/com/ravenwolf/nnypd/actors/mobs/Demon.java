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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.DemonSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;


public class Demon extends MobPrecise {

    private int impCD = Random.IntRange(2,8);
    private int blinkCD=0;

    public boolean summoning = false;
    private Emitter summoningEmitter = null;
    public int summoningPos = -1;

    public Demon() {

        super( 20 );

        name = "上等恶魔";
        spriteClass = DemonSprite.class;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Dispel.class, Element.Resist.PARTIAL);
        resistances.put(Element.Flame.class, Element.Resist.PARTIAL);

        HUNTING = new Hunting();
	}

    public String getTribe() {
        return TRIBE_LONER;
    }

    @Override
    public int minAC() {
        return super.minAC()+2;
    }

    @Override
    public int dexterity() {
        return summoning ? 0 : super.dexterity();
    }

    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            if (summoning){
                if (isFriendly()){
                    //cancel summoning
                    summoning=false;
                    summoningEmitter.burst( ElmoParticle.FACTORY, 2 );
                    sprite.idle();
                    spend(TICK);
                    return true;
                }
                //push anything on summoning spot away, to the furthest valid cell
                if (Actor.findChar(summoningPos) != null) {
                    int pushPos = pos;
                    for (int c : Level.NEIGHBOURS8) {
                        int candidatePos=summoningPos + c;
                        if (Actor.findChar(candidatePos) == null
                                && !Dungeon.level.solid[candidatePos]
                                && Dungeon.level.distance(pos, candidatePos) > Dungeon.level.distance(pos, pushPos))
                        {
                            pushPos = candidatePos;
                        }
                    }
                    //push enemy, or wait a turn if there is no valid pushing position
                    if (pushPos != pos) {
                        Char ch = Actor.findChar(summoningPos);
                        int auxPos = summoningPos + summoningPos -  pushPos;
                        ch.knockBack(auxPos,0,1);
                    } else {
                        spend(TICK);
                        return true;
                    }
                }
                summoning = false;
                Mob imp = new Imp();
                imp.pos = summoningPos;
                summoningPos = -1;
                GameScene.add(imp, 1f);
                imp.state=imp.HUNTING;
                //FIXME
                imp.HP--;//reduce hp to prevent them from stealing

                Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 1.5f);
                summoningEmitter.burst( ElmoParticle.FACTORY, 5 );
                sprite.idle();

                spend(TICK);
                return true;
            }if(enemy!=null) {
                if (impCD <= 0 && blinkCD <= 0 && Level.adjacent(pos, enemy.pos)) {
                    blink();
                    blinkCD = Random.IntRange(5, 8);
                    return true;
                }

                if (impCD <= 0 && enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 6 && !isFriendly()) {
                    summoningPos = -1;
                    for (int c : Level.NEIGHBOURS8) {
                        int candidatePos = enemy.pos + c;
                        if (Actor.findChar(candidatePos) == null
                                && !Dungeon.level.solid[candidatePos]
                                && Level.fieldOfView[candidatePos]
                                && Dungeon.level.distance(pos, candidatePos) < Dungeon.level.distance(pos, summoningPos)
                        ) {
                            summoningPos = enemy.pos + c;
                        }
                    }
                    if (summoningPos != -1) {
                        impCD = Random.IntRange(20, 30);
                        summoning = true;

                        sprite.cast(summoningPos);

                        spend(2 * TICK);
                    } else {
                        return super.act(enemyInFOV, justAlerted);
                    }
                    return true;
                }
            }

            return super.act(enemyInFOV,justAlerted);
        }
    }

    private void blink() {

        int newPos;

        do {
            newPos = Random.Int( Level.LENGTH );
        } while ( Level.solid[newPos] || !Level.fieldOfView[newPos] ||
                Actor.findChar(newPos) != null || pos == newPos ||
                Ballistica.cast(pos, newPos, false, false) != newPos );

        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
        }

        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
        }

        ((DemonSprite)sprite).blink(pos, newPos);
        move( newPos );
        spend( 1 / moveSpeed() );

    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(impCD>0)
            impCD--;
        if(blinkCD>0)
            blinkCD--;
        return ( super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, /*true */ false) == enemy.pos && !isCharmedBy( enemy ));
    }

    public void onCastComplete() {
        if (summoning){
            summoningEmitter = CellEmitter.get(summoningPos);
            summoningEmitter.pour(ElmoParticle.FACTORY, 0.2f);
            next();
        }else
            super.onCastComplete();
    }

    @Override
    protected void onRangedAttack( int cell ) {
        onAttackComplete();
        if (Dungeon.visible[cell]) {
            CellEmitter.get(cell).burst( ElmoParticle.FACTORY, 8 );
            CellEmitter.get( cell ).burst( Speck.factory( Speck.BLAST ), 4 );
        }

        super.onRangedAttack( cell );
    }


    @Override
    public void die(Object cause, Element dmg) {
        if (summoningEmitter != null){
            summoningEmitter.killAndErase();
            summoningEmitter = null;
        }
        super.die(cause,dmg);
    }

	@Override
	public String description() {
        return
                "上等恶魔是古神所支配的最恶毒、最强大、最危险的恶魔！它们拥有卓越的身体强度和魔法技能，并且会可以召唤出一些低阶恶魔辅助战斗";
    }

	//restore summon effect when loading game
    public void restoreSummoning(){
        if (summoning && summoningPos != -1){
            sprite.cast(summoningPos);
        }else{
            summoning = false;
        }
    }

    private static final String IMP_CD = "imp_cd";
    private static final String BLINK_CD = "blink_cd";
    private static final String SUMMONING = "summoning";
    private static final String SUMMONING_POS = "summoning_pos";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(IMP_CD, impCD);
        bundle.put(BLINK_CD, blinkCD);
        bundle.put(SUMMONING, summoning);
        bundle.put(SUMMONING_POS, summoningPos);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        impCD = bundle.getInt(IMP_CD);
        blinkCD = bundle.getInt(BLINK_CD);
        summoning = bundle.getBoolean(SUMMONING);
        summoningPos = bundle.getInt(SUMMONING_POS);

    }
}
