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
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Mending;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;//新增
import com.ravenwolf.nnypd.actors.buffs.special.SoulLink;//新增
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Stun;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.misc.TomeOfMastery;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.NecroBossLevel;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Chains;
import com.ravenwolf.nnypd.visuals.effects.Effects;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.LifeDrain;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Pushing;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.effects.Splash;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.sprites.NecromancerSprite;
import com.ravenwolf.nnypd.visuals.sprites.AbominationSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Necromancer extends MobCaster {

    public boolean charged = false;
    private int specialCD = 5;
    private int explosionCD = 8;
    protected int breaks = 0;
    public boolean chargingBones = false;

    public int summoningState = 0;//0 none, 1 summoning, 2 summoned
    private Emitter summoningEmitter = null;

    private static final String SUMMON_STATE	= "summon_state";
    private static final String BREAKS	= "breaks";
    private static final String SPECIAL_CD = "specialCD";
    private static final String SURGE_CD = "surgeCD";
    private static final String CHARGED = "charged";
    private static final String CHARGING_BONES = "chargingBones";

    public Necromancer() {

        super( 3, 8, true );

        name = "死灵法师";
        spriteClass = NecromancerSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        maxDamage -= 2*tier;
        armorClass+=tier;

        HUNTING = new Hunting();

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put(Element.Frost.class, Element.Resist.PARTIAL);
        resistances.put(Element.Flame.class, Element.Resist.PARTIAL);
        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);
        resistances.put(Element.Shock.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);
	}

    private static Abomination getSpawn() {
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Abomination) {
                return (Abomination)mob;
            }
        }
        return null;
    }

    private static Necromancer getNecromancer() {
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Necromancer) {
                return (Necromancer)mob;
            }
        }
        return null;
    }

    private static int getBoneExplosionDamage() {
        return getNecromancer().damageRoll()*3/2;
    }

    private void summonAbomination(int cell) {
        Mob spawn = new Abomination();
        spawn.pos = cell;
        GameScene.add(spawn, 1f);
        spawn.state=spawn.HUNTING;
        spawn.yell("新鲜的血肉！");
        Dungeon.level.set(cell, Terrain.EMPTY_SP2);
        GameScene.updateMap(cell);
        Sample.INSTANCE.play(Assets.SND_BONES);
        Splash.at( cell, spawn.sprite.blood(), 10 );
        SoulLink soullink = Buff.affect(this, SoulLink.class);
        soullink.object = spawn.id();

    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (summoningState==1 && summoningEmitter == null){
            summoningEmitter = CellEmitter.get( NecroBossLevel.ABOMINATION_TOMB );
            summoningEmitter.pour(ElmoParticle.FACTORY, 0.1f);
            ((NecromancerSprite)sprite).charge();
        }
        if(chargingBones)
            ((NecromancerSprite)sprite).charge();
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

    @Override
    public int dexterity() {
        return  summoningState==1 || chargingBones ? 0 : super.dexterity();
    }

    @Override
    public void notice() {
        super.notice();
        if( enemySeen && summoningState==0 ) {
            Sample.INSTANCE.play(Assets.SND_DEATH);
            yell( "你的骸骨将属于我, " + Dungeon.hero.heroClass.title() + "!" );
        }
    }

    private void blink(){
        int newPos;
        Buff.detach(this, PinCushion.class);
        if(getSpawn()==null) {
            do {
                newPos = Dungeon.level.randomRespawnCell(false, true);
            } while (newPos < NecroBossLevel.PIT_START + Level.WIDTH * 2 || Level.adjacent(pos, newPos) ||
                    (enemy != null && Level.adjacent(newPos, enemy.pos)));

        }else {
            newPos = NecroBossLevel.THRONE;
            //if not already on Pedestal
            if (pos!=newPos ) {
                //try to push enemy if on Pedestal
                Char enemy = Actor.findChar(newPos);
                if (enemy != null && enemy != this) {
                    enemy.knockBack(pos, damageRoll(), 1);
                }
                if (Actor.findChar(newPos) != null)
                    newPos = pos;//dont change position if still occupied
            }
        }
        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
        }

        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
        }

        ((NecromancerSprite)sprite).blink(pos,newPos);
        move( newPos );
    }

    @Override
    public boolean act() {
        if (specialCD >0)
            specialCD--;
        if (explosionCD >0)
            explosionCD--;

        if(summoningState == 1 ){//finish summoning, return to pedestal and rest
            summoningState =2;
            summonAbomination(NecroBossLevel.ABOMINATION_TOMB);
            summoningEmitter.burst( Speck.factory( Speck.RATTLE ), 5 );
            blink();
            spend(TICK*2);
            return true;
        }
        if (chargingBones) {
            chargingBones=false;
            BoneSurge blob = (BoneSurge) Dungeon.level.blobs.get(BoneSurge.class);
            if (blob != null) {
                for (int pos = 0; pos < Level.LENGTH; pos++) {
                    if (blob.cur[pos] > 0) {
                        blob.boneExplosion(pos);
                    }
                }
                Sample.INSTANCE.play(Assets.SND_BONES);
                Sample.INSTANCE.play(Assets.SND_BLAST, 1.0f, 1.0f, 0.8f);

                sprite.idle();
                spend(TICK);
                return true;
            }
        }

        return super.act();
    }

    private void castBoneSurge(){
        chargingBones=true;
        for (int i = Random.Int(2) ; i < 5+breaks ; i++) {
            int bonePos;
            do{
                bonePos = Dungeon.level.randomRespawnCell( false, true );
            } while( Level.adjacent( pos, bonePos ) || Dungeon.level.map[bonePos] != Terrain.EMPTY_SP2 /*Level.distance(bonePos, Dungeon.hero.pos)>6*/);
            GameScene.add(Blob.seed(bonePos, 6, BoneSurge.class));
        }
        explosionCD = Random.IntRange(1, 5);

        ((NecromancerSprite)sprite).castSpecial(enemy.pos,null);
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if( !charged && (!Level.adjacent( pos, enemy.pos ) )) {
            charged = true;
            sprite.idle();
            if( Dungeon.visible[ pos ] ) {
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_BLACK, 15);
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

        MagicMissile.shadow(sprite.parent, pos, cell,
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
    public boolean cast( Char enemy ) {

        return castBolt(enemy, damageRoll() * (enemy!=Dungeon.hero?2:1),false,Element.ENERGY);
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i( name +"不再狂暴了." );
        }

        super.remove(buff);
    }

    public void enrage(){
        BuffActive.add(this, Enraged.class, breaks * Random.Float(4.0f, 6.0f));
        yell("你会后悔的！");
        if (Dungeon.visible[pos]) {
            GLog.n(name +"被激怒了！");
        }
        sprite.idle();
        specialCD = Random.IntRange(4, 8);
        spend(TICK);
    }


    @Override
    public void die( Object cause, Element dmg ) {
        super.die( cause, dmg );
        Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();

        GameScene.bossSlain();
        ((NecroBossLevel) Dungeon.level).unseal();
        Badges.validateBossSlain();
        if (getSpawn()!=null)
            getSpawn().die(cause);

        yell( "死亡并非终点..." );
    }

    @Override
    public String description() {
        return
                "死灵法师是所有巫师中最受争议的，因为它诅咒了自己的灵魂，用自己的人性交换了一种起死回生的能力，并对所有生者发动战争";
    }

    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {

            final Abomination spawn= getSpawn();

            if (!charged){
                //first break, summon abomination
                if(summoningState == 0 && 7>10*HP/HT ){
                    summoningState =1;
                    summoningEmitter = CellEmitter.get( NecroBossLevel.ABOMINATION_TOMB );
                    summoningEmitter.pour(ElmoParticle.FACTORY, 0.1f);
                    ((NecromancerSprite)sprite).castSpecial(NecroBossLevel.ABOMINATION_TOMB,null);
                    spend(TICK);
                    breaks++;
                    yell( "我的奴仆将消灭你。出来吧，憎恶！" );
                    return true;
                }
                //blink around
                if(spawn==null && specialCD <=0){
                    blink();
                    spend( 1/moveSpeed() );
                    specialCD = Random.IntRange(6, 10);
                    return true;
                }

                if( spawn != null && (3 - breaks > 4 * HP / HT || 3 - breaks > 4 * spawn.HP / spawn.HT)) {
                    if ( 3-breaks > 4 * spawn.HP / spawn.HT /*&& canCastBolt(spawn)*/) {
                        ((NecromancerSprite)sprite).castSpecial(spawn.pos, new Callback() {
                            @Override
                            public void call() {
                                MagicMissile.miasma(sprite.parent, pos, spawn.pos, new Callback() {
                                    @Override
                                    public void call() {
                                        spawn.enrage(breaks);
                                        next();
                                    }
                                });
                            }
                        });
                        charged=false;
                        breaks++;
                        spend(TICK);
                        Sample.INSTANCE.play(Assets.SND_ZAP);
                        yell( "杀掉那个人，我的仆从！" );
                        specialCD += 4;
                        return false;
                    } else {
                        ((NecromancerSprite)sprite).castSpecial(spawn.pos, new Callback() {
                            @Override
                            public void call() {
                                BuffActive.add(Necromancer.this, Mending.class, Random.IntRange(5, 8));
                                spawn.damage(HT / 5,this,null);
                                Necromancer.this.heal(HT / 6);
                                if (sprite.visible || spawn.sprite.visible) {
                                    sprite.parent.add( new LifeDrain( spawn.pos, pos, null ) );
                                    spawn.enrage(breaks);
                                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( spawn.sprite, 2f );
                                }
                                next();
                            }
                        });
                        charged=false;
                        breaks++;
                        spend(TICK);
                        Sample.INSTANCE.play(Assets.SND_ZAP);
                        yell( "夺取它的生命，我的仆从！" );
                        return false;
                    }
                }
                //if enemy is in the pit area
                if (enemyInFOV && explosionCD <=0 &&  enemy.pos> NecroBossLevel.PIT_START +Level.WIDTH){
                    //before summon bones, if abomination its alive, go outside the pit
                    if(spawn!=null && pos > NecroBossLevel.PIT_START +Level.WIDTH){
                        blink();
                        spend( 1/moveSpeed() );
                        return true;
                    }if (!Level.adjacent( pos, enemy.pos ) ) {
                        castBoneSurge();
                        spend(TICK * 2);
                        return true;
                    }else
                        return super.act(enemyInFOV,justAlerted);
                }

            }else{

                if (spawn !=null && specialCD <=0 && canCastBolt(spawn)){
                    ((NecromancerSprite)sprite).castSpecial(spawn.pos, new Callback() {
                        @Override
                        public void call() {
                            MagicMissile.blueLight(sprite.parent, pos, spawn.pos, new Callback() {
                                @Override
                                public void call() {
                                    BuffActive.add(spawn, Shielding.class,  Random.IntRange(6, 8));
                                    next();
                                }
                            });
                        }
                    });
                    spend(TICK);
                    Sample.INSTANCE.play(Assets.SND_ZAP);
                    specialCD = Random.IntRange(8, 10);
                    charged=false;
                    return false;
                }
            }
            return super.act(enemyInFOV,justAlerted);
        }
    }
    
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
        bundle.put(SPECIAL_CD, specialCD);
        bundle.put(SURGE_CD, explosionCD);
        bundle.put(BREAKS, breaks);
        bundle.put(SUMMON_STATE, summoningState);
        bundle.put(CHARGING_BONES, chargingBones);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
        specialCD = bundle.getInt(SPECIAL_CD);
        explosionCD = bundle.getInt(SURGE_CD);
        breaks = bundle.getInt(BREAKS);
        summoningState= bundle.getInt(SUMMON_STATE);
        chargingBones=bundle.getBoolean( CHARGING_BONES );
    }


    public static class Abomination extends MobHealthy {

        private static final String CHAIN_CD = "chainCD";
        private MissileSprite hook;
        private boolean charged;
        private int chainCD =4;

        public Abomination() {
            super(3,7,true );

            name =  "憎恶";
            spriteClass = AbominationSprite.class;

            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);

            minDamage -= tier;
            maxDamage -= tier*2;
            armorClass-=tier;

            HT=HT*3/4;
            HP=HT;
        }

        @Override
        public float awareness(){
            return super.awareness()/2.0f;
        }

        @Override
        public void updateSpriteState() {
            super.updateSpriteState();
            if (charged && hook == null){
                chargeHook();
            }
        }

        private void chargeHook(){
            ((AbominationSprite)sprite).chargeChain( pos, enemy==null?pos:enemy.pos);
            hook=(MissileSprite)sprite.parent.recycle( MissileSprite.class );
            int speed=540*(sprite.flipHorizontal?1:-1);
            hook.flipHorizontal=!sprite.flipHorizontal;
            hook.flipVertical=!sprite.flipHorizontal;
            hook.spin(pos,ItemSpriteSheet.HARPOON_THROWN,speed,null);
        }

        private void resetHook(){
            charged = false;
            if (hook!=null) {
                hook.kill();
                hook=null;
            }
        }
        @Override
        public boolean add( Buff buff ) {
            if (buff instanceof Stun || buff instanceof Frozen)
                resetHook();
            return  super.add(buff);
        }
        @Override
        public void knockBack(int sourcePos, int damage, int amount, final Callback callback) {
            resetHook();
            super.knockBack(sourcePos,damage,amount,callback);
        }

        @Override
        public boolean act() {
            boolean act=super.act();
            if (charged && !enemySeen) {
                resetHook();
                sprite.idle();
            }
            return act;
        }

        @Override
        protected void onRangedAttack(final int cell ) {
            chainCD =Random.IntRange(4, 6);
            int hitTile= Ballistica.cast(pos, cell, false, true);
            sprite.parent.add(new Chains(pos, hitTile, false, Effects.Type.CHAIN));
            ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                    reset(pos, hitTile, ItemSpriteSheet.HARPOON_THROWN, new Callback() {
                        @Override
                        public void call() {
                            chain(cell);
                            next();
                        }
                    });
            super.onRangedAttack( cell );
        }

        @Override
        protected boolean doAttack( Char enemy ) {

            if( !Level.adjacent( pos, enemy.pos ) && !charged && canHook()) {
                charged = true;
                chargeHook();
                spend( TICK);
                return true;
            } else {
                resetHook();
                return super.doAttack( enemy );
            }
        }

        @Override
        protected boolean getCloser( int target ) {
            //abomination cannot climb pedestal
            if (target < NecroBossLevel.PIT_START) {
                return getFurther(target);
            } else {
                return super.getCloser(target);
            }
        }

        private boolean canHook(){
            return chainCD <=0 && enemy instanceof Hero && (Level.distance(pos, enemy.pos) > 3 || enemy.pos < NecroBossLevel.PIT_START )
                    && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos ;
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            chainCD--;
            return super.canAttack( enemy ) || charged || canHook();
        }


        private boolean chain(int targetPos){
            Char enemy =null;
            for (int i=1; i <= Ballistica.distance +1; i++) {

                int pos = Ballistica.trace[i];

                enemy =Actor.findChar( pos );
                if(enemy!=null || Dungeon.level.solid[pos]) {
                    targetPos=pos;
                    break;
                }
            }

            if (enemy==null) {
                //if hit a statue
                if(Dungeon.level.map[targetPos]==Terrain.STATUE_SP){
                    damage( absorb( damageRoll()*2,true), this, Element.PHYSICAL );
                    BuffActive.addFromDamage( this, Stun.class, 3);
                    CellEmitter.get( targetPos ).burst(Speck.factory(Speck.ROCK),2);
                    CellEmitter.get( pos ).burst(Speck.factory(Speck.ROCK),4);
                    Dungeon.level.set(targetPos, Terrain.EMPTY_SP);
                    GLog.i( "坟墓从地面上缓缓升起，憎恶出现在面前！" );
                }
                return false;
            }else {
                if (enemy.immovable())
                    return false;
                yell( "过来这里！" );
                int newPos = Ballistica.trace[1];

                //try to pull to the position behind?
                int auxPos = pos + pos - newPos;
                if (!Level.solid[auxPos] && !Level.chasm[auxPos] && Actor.findChar( auxPos ) == null )
                    newPos =auxPos;

                Actor.addDelayed(new Pushing(enemy, enemy.pos, newPos), -1);

                enemy.damage( enemy.absorb( damageRoll()*2/3), this, Element.PHYSICAL );
                BuffActive.addFromDamage( enemy, Crippled.class, damageRoll()*2/3);
                enemy.sprite.bloodBurstA(sprite.center(),8);

                Actor.freeCell(enemy.pos);
                enemy.pos = newPos;
                Actor.occupyCell(enemy);
                Dungeon.level.press(newPos, enemy);
            }
            return true;
        }

        @Override
        protected float healthValueModifier() { return 0.25f; }

        @Override
        public float attackSpeed() {
            return 0.75f;
        }

        @Override
        public float moveSpeed() {
            return hasBuff(Enraged.class) ? 1f : 0.75f;
        }

        @Override
        public boolean isMagical() {
            return true;
        }

        @Override
        public String description() {
            return "这种生物是由畸形的血肉和骨骼堆积而成的山。几个头骨和手臂从这只可憎的怪物的前部伸出来，扭动并伸出来捕捉它们的猎物。其中一只较大的手臂握着一个巨大的链钩，可能是用来捕捉远处敌人的。";
        }

        @Override
        public void die( Object cause, Element dmg ) {
            super.die( cause, dmg );
            resetHook();
            Necromancer necro=getNecromancer();
            if (necro!=null && necro.isAlive()) {
                BuffActive.add(necro, Enraged.class, Random.IntRange(8, 12));
                necro.enrage();
                next();
            }
        }

        public void enrage(int breaks){
            BuffActive.add(this, Enraged.class, breaks * Random.Float(3.0f, 5.0f));

            if (Dungeon.visible[pos]) {
                GLog.n(name +"被激怒了！");
            }
            sprite.idle();
            spend(TICK);
        }

        @Override
        public void remove( Buff buff ) {
            if( buff instanceof Enraged ) {
                sprite.showStatus( CharSprite.NEUTRAL, "..." );
                GLog.i( name +"停止了狂暴。" );
            }
            super.remove(buff);
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( CHAIN_CD, chainCD);
            bundle.put( CHARGED, charged );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            chainCD = bundle.getInt( CHAIN_CD);
            charged=bundle.getBoolean( CHARGED );
        }
    }


    public static class BoneSurge extends Blob {

        public BoneSurge() {
            super();
            name = "骸骨爆炸";
        }

        @Override
        protected void evolve() {
            int from = WIDTH + 1;
            int to = Level.LENGTH - WIDTH - 1;
            for (int pos=from; pos < to; pos++) {

                if (cur[pos] > 0) {

                    cur[pos]--;
                }
                volume += ( off[pos] = cur[pos] );
            }
        }

        public void seed( int cell, int amount ) {
            if (cur[cell] == 0) {
                volume += amount;
                cur[cell] = amount;
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use(emitter);
            emitter.start( Speck.factory(Speck.RATTLE), 0.1f, 0 );
        }

        @Override
        public String tileDesc()  {
            return "由于受到黑暗魔法的影响，这些骸骨正在剧烈地摇晃。当它们爆炸的时候，你可不想靠得太近。";
        }

        public void boneExplosion( int pos) {
            for (int n : Level.NEIGHBOURS9) {

                int cell = pos + n;
                Char ch = Actor.findChar(cell);

                if( ch != null ) {
                    ch.damage( ch.absorb( getBoneExplosionDamage()), this, Element.PHYSICAL );
                }
            }

            if (Dungeon.visible[pos]) {
                CellEmitter.get(pos).burst( Speck.factory( Speck.BONE ), 8 );
                CellEmitter.get( pos ).burst( Speck.factory( Speck.BLAST ), 2 );
            }
            volume -= cur[pos];
            cur[pos] = 0;
        }
    }

}
