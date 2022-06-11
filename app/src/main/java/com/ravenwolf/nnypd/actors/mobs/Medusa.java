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

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Mending;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.NagaBossLevel;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.LifeDrain;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.MedusaSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Medusa extends MobRanged {

    static final int PHASE_FIGTH=0;
    public static final int PHASE_STONE_GAZE=1;
    static final int PHASE_ABSORB=2;
    protected int breaks = 0;
    public int phase=0;
    protected int maxTurnsToHeal=0;
    protected int targetPedestal = 0;
    protected int statuesConsumed = 0;

    public Medusa() {

        super( 4, 20, true );

        name = "美杜莎";
        spriteClass = MedusaSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL);
        resistances.put(Element.Shock.class, Element.Resist.PARTIAL);
    }


    @Override
    public float moveSpeed() {
        return phase==PHASE_ABSORB?super.moveSpeed() * 2:super.moveSpeed();
    }


    @Override
    protected float healthValueModifier() { return 0.25f; }


    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                reset(pos, cell, new Arrows(), 2f,  new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });
        super.onRangedAttack(cell);
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i( "美杜莎停止了狂暴" );
        }

        super.remove(buff);
    }

    public int defenseProc( Char enemy, int damage, boolean blocked ) {
        damage=super.defenseProc(enemy,damage,blocked);
        if ( phase==PHASE_STONE_GAZE )
            damage/=2;
        if (enemy!=null && !enemy.hasBuff(Blinded.class) && enemySeen && Dungeon.visible[pos] && (phase==PHASE_STONE_GAZE /*|| buff( Enraged.class ) != null*/)){
            BuffActive.add( enemy, Tormented.class, 2f );
        }
        return damage;
    }

    @Override
    public int dexterity() {
        return phase==PHASE_STONE_GAZE ? 0 :super.dexterity();
    }

    @Override
    protected boolean getCloser( int target ) {
        return phase==PHASE_ABSORB && targetPedestal!=0?
                super.getCloser( targetPedestal ) :
                super.getCloser( target );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return (!enemy.hasBuff(Petrificated.class) && (super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos))
        && ( phase==PHASE_FIGTH || ( buff( Ensnared.class ) != null ) || targetPedestal!=0 &&  Actor.findChar(targetPedestal)!=null);
    }


    @Override
    public boolean act() {

        if (phase==PHASE_ABSORB) {

            if (targetPedestal == pos) {
                phase = PHASE_FIGTH;
                targetPedestal = 0;
                if (HP < HT) {

                    int healthRestored = 0;

                    int statuePos=-1;
                    for (int i : Level.NEIGHBOURS4) {

                        if (Dungeon.level.map[pos + i] == Terrain.STATUE_BRUTE) {
                            BuffActive.add(this, Enraged.class,  Random.Float(7.0f, 12.0f)+statuesConsumed);

                            if (Dungeon.visible[pos]) {
                                GLog.n("美杜莎被激怒了！");
                            }
                            statuePos=pos+i;

                        }
                        if (Dungeon.level.map[pos + i] == Terrain.STATUE_SHAMAN) {
                            BuffActive.add(this, Shielding.class,  Random.Float(7.0f, 12.0f)+statuesConsumed);

                            statuePos=pos+i;
                        }
                        if (Dungeon.level.map[pos + i] == Terrain.STATUE_FROG) {
                            BuffActive.add(this, Mending.class,  Random.Float(7.0f, 12.0f)+statuesConsumed);

                            statuePos=pos+i;
                        }
                    }
                    if (statuePos!=-1) {

                        Level.set(statuePos, Terrain.EMPTY_DECO);
                        GameScene.updateMap(statuePos);
                        Dungeon.observe();

                        if (sprite.visible) {
                            CellEmitter.get( statuePos ).start(Speck.factory(Speck.ROCK), 0.07f, 6);
                            sprite.parent.add(new LifeDrain(statuePos, pos, null));
                        }
                        healthRestored += HT / 10;
                        healthRestored = Math.min(healthRestored, HT - HP);

                        HP += healthRestored;
                        if (Dungeon.visible[pos]) {
                            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                            sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));
                        }
                        spend(TICK);
                    }
                    return true;
                }
            }

            maxTurnsToHeal--;
            if (maxTurnsToHeal<1 || targetPedestal==0) {
                phase = PHASE_FIGTH;
                return super.act();
            }
        }

        if (phase==PHASE_FIGTH) {

            if (5 - statuesConsumed > 6 * HP / HT ) {
                phase = PHASE_STONE_GAZE;
                statuesConsumed++;

                if (Random.Int(2)==1)
                    yell( "直视我！" );
                else
                    yell( "你逃不掉的！" );

                    spend(3*TICK );
                if (Dungeon.hero!=null)
                    ((MedusaSprite)sprite).charge(Dungeon.hero.pos);
                else
                    ((MedusaSprite)sprite).charge(pos);
                return true;
            }

        }

        if (phase==PHASE_STONE_GAZE) {
            phase=PHASE_ABSORB;
            targetPedestal = NagaBossLevel.getRandomPedestal(pos);
            maxTurnsToHeal=Level.distance(pos, targetPedestal)+3;

            for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
                if (mob!=this && !mob.hasBuff(Blinded.class) && Level.distance(pos,mob.pos)<=mob.viewDistance() &&
                        mob.pos== Ballistica.cast(pos, mob.pos, false, false)) {
                    if (mob.isEthereal())
                        BuffActive.add(mob, Banished.class, 3f);
                    else
                        BuffActive.add(mob, Petrificated.class, 3f);
                }
            }

            Hero hero=Dungeon.hero;
            if (hero !=null) {
                if ( enemySeen && Dungeon.visible[pos]) {
                    new Flare(6, 48).color(SpellSprite.COLOUR_RUNE, true).show(sprite, 3f);
                    Sample.INSTANCE.play(Assets.SND_MELD,1f,1f,0.4f);
                    if (!enemy.hasBuff(Blinded.class)) {
                        BuffActive.add(enemy, Petrificated.class, 3f);
                    }
                }
            }
            spend(TICK);
            sprite.idle();
            return true;
        }

        return super.act();
    }

	@Override
	public void notice() {
		super.notice();
        if( enemySeen && HP==HT) {
            Sample.INSTANCE.play(Assets.SND_CHARMS);
            yell( "我会把你变成这些精美的雕像, " + Dungeon.hero.heroClass.title() + "!" );
        }
	}

    @Override
    public float awareness(){
        return super.awareness()/2.0f;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        GameScene.bossSlain();
        Dungeon.level.drop( new GoldenKey(), pos ).sprite.drop();
        super.die( cause, dmg );

        Badges.validateBossSlain();

        yell( "我的诅咒终于结束了..." );
    }


	
	@Override
	public String description() {
        return "人们关于这种生物到底是什么，有很多猜测，多年来流传着好几种传言。" +
                "传说中有一位古代女祭司因为她的虚荣受到了可怕的诅咒，从而创造出来的生物，有些传说甚至认为美杜莎是人类和恶魔的后代。虽然这些故事可能都不是真的，但她的存在本身就是违背常理的。"+
                (phase==PHASE_STONE_GAZE?"/n":"");
    }


    private static final String BREAKS	= "breaks";
    private static final String PHASE	= "phase";
    private static final String STATUES_CONSUMED	= "statuesConsumed";
    private static final String TARGET_PEDESTAL	= "targetPedestal";
    private static final String MAX_TURNS_HEAL	= "maxTurnsToHeal";




    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( STATUES_CONSUMED, statuesConsumed );
        bundle.put( PHASE, phase );
        bundle.put( TARGET_PEDESTAL, targetPedestal );
        bundle.put( MAX_TURNS_HEAL, maxTurnsToHeal );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        statuesConsumed= bundle.getInt( STATUES_CONSUMED );
        phase= bundle.getInt( PHASE );
        targetPedestal= bundle.getInt( TARGET_PEDESTAL );
        maxTurnsToHeal= bundle.getInt( MAX_TURNS_HEAL );
    }


}
