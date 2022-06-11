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
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.ConfusionGas;
import com.ravenwolf.nnypd.actors.blobs.CorrosiveGas;
import com.ravenwolf.nnypd.actors.blobs.Darkness;
import com.ravenwolf.nnypd.actors.blobs.Electricity;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.blobs.FrigidVapours;
import com.ravenwolf.nnypd.actors.blobs.Miasma;
import com.ravenwolf.nnypd.actors.blobs.Web;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Amok;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Shocked;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.misc.TomeOfMastery;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfClairvoyance;
import com.ravenwolf.nnypd.items.weapons.throwing.PoisonDarts;
import com.ravenwolf.nnypd.items.weapons.throwing.Shurikens;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.sprites.TenguSprite;
import com.ravenwolf.nnypd.visuals.ui.HealthIndicator;
import com.ravenwolf.nnypd.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Tengu extends MobRanged {

	private static final int JUMP_DELAY = 8;

    private int timeToJump = 0;
    protected int breaks = 0;
    protected boolean clonePhase = false;
    protected int hitsBeforeEnrage = 0;
    protected boolean dart=false;
    protected boolean damagedThisTurn=false;

    private static Tengu originalTengu() {
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Tengu) {
                return (Tengu)mob;
            }
        }

        return null;
    }

    public Tengu() {

        super( 3, 15, true );

        name = Dungeon.depth == Statistics.deepestFloor ? "天狗" : "天狗的幻影";
        spriteClass = TenguSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
    }

    @Override
    public float attackDelay() {
        return buff( Enraged.class ) == null ? 1.0f : 0.5f ;
    }

    @Override
    public float moveSpeed() {
        return buff( Enraged.class ) == null ? TICK/*super.moveSpeed()*/ : TICK/*super.moveSpeed()*/ * 2;
    }

    @Override
    public int dexterity() {
        return clonePhase ? 0 : super.dexterity();
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

    @Override
    public int damageRoll() {
        return buff( Enraged.class ) == null ? super.damageRoll() : super.damageRoll()*2 /3;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        if (Random.Float()>0.15f || clonePhase) {
            dart=false;
            ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                    reset(pos, cell, new Shurikens(), new Callback() {
                        @Override
                        public void call() {
                            onAttackComplete();
                        }
                    });
        }else {
            dart = true;
            ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                    reset(pos, cell, new PoisonDarts(), new Callback() {
                        @Override
                        public void call() {
                            onAttackComplete();
                        }
                    });
        }

        super.onRangedAttack(cell);
    }

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( dart) {
            damage/=3;
            if (!blocked) {
                BuffActive.add(enemy, Dazed.class, 1);
            }
        }

        return damage;
    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (!rooted && Level.fieldOfView[target]) {
			jump(false, true);
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}

   /* @Override
    public float moveSpeed() {
        return numberOfClones>0?0:super.moveSpeed();
    }*/
	
	@Override
	protected boolean doAttack( Char enemy ) {

		timeToJump++;
		if ( !rooted && timeToJump >= JUMP_DELAY ) {
			jump(false, true);
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

    private void jumpClones(boolean clearClones) {

        HealthIndicator.instance.target(null);
        Buff.detach(this, Poisoned.class);
        Buff.detach(this, Bleeding.class);
        Buff.detach(this, Crippled.class);
        Buff.detach(this, Withered.class);
        Buff.detach(this, Corrosion.class);
        Buff.detach(this, Burning.class);
        Buff.detach(this, Chilled.class);
        Buff.detach(this, Dazed.class);
        Buff.detach(this, Blinded.class);
        Buff.detach(this, Shocked.class);
        Buff.detach(this, Decay.class);
        Buff.detach(this, Tormented.class);
        Buff.detach(this, Amok.class);
        Buff.detach(this, Revealed.class);
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof TenguClone) {
                TenguClone clone=(TenguClone) mob;
                if (clearClones)
                    clone.revealed=0;
                //clone.updateAlpha();
                else if (clone.revealed>0)
                    clone.revealed--;
                clone.updateAlpha();
                clone.jump();
            }
            if (!(mob instanceof Tengu)){
                mob.resetEnemy();
            }
        }
        TagAttack.target(null);
        TagAttack.updateState();
    }

    @Override
    public void onAttackComplete(){
        if (clonePhase) {
            //super.onAttackComplete();
            attack(enemy);
            //jumpClones();
            jump(false, false);
            next();
        }else super.onAttackComplete();
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        super.damage( dmg, src, type );

        if (clonePhase) {
            damagedThisTurn = true;
        }

        timeToJump++;
    }



    public void enrage(){
        BuffActive.add(this, Enraged.class, breaks * Random.Float(3.0f, 5.0f));

        if (Dungeon.visible[pos]) {
            GLog.n("天狗被激怒了！");
        }
        sprite.idle();
        spend(TICK);
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i( "天狗已不再狂暴。" );
        }

        super.remove(buff);
    }


    @Override
    public boolean add( Buff buff ) {
        if (clonePhase && !(buff instanceof PinCushion))
            return false;
        else
            return super.add(buff);
    }

    @Override
    public boolean act() {


        if (clonePhase){

            hitsBeforeEnrage--;
            if (hitsBeforeEnrage<1) {
                killClones();
                //enrage();
            }else
                if (damagedThisTurn) {
                    damagedThisTurn=false;
                    jump(true, true);
                    return true;
                }
            damagedThisTurn=false;
            return super.act();
        }

        if( 3 - breaks > 4 * HP / HT && !clonePhase && !hasBuff(Enraged.class)) {
        //if( 2 - breaks > 3 * HP / HT ) {
            breaks++;
            if (breaks!=3){
                clonePhase=true;
                int numberOfClones=2;//breaks + 1;
                //hitsBeforeEnrage=breaks*4 + 2;//3/*numberOfClones*/;
                hitsBeforeEnrage=breaks*3+2;//3/*numberOfClones*/;
                for (int i = 0 ; i < numberOfClones ; i++) {
                    TenguClone clone = new TenguClone();
                    clone.pos = pos;
                    clone.state=clone.HUNTING;
                    clone.enemy=enemy;
                    clone.enemySeen=enemySeen;
                    clone.alerted=alerted;
                    Dungeon.level.press(clone.pos, clone);
                    GameScene.add(clone);
                    clone.notice();
                }
                jump(false, true);
                //jumpClones();


            }else {
                enrage();
            }
            return true;

        }/* else if( buff( Enraged.class ) != null ) {

            timeToJump++;

        }*/

        return super.act();
    }
	
	private void jump(boolean clearClones, boolean spendTime) {

        timeToJump = 0;
        Buff.detach(this, PinCushion.class);

        if (!clonePhase)
            for( int i = 0 ; i < /*4*/3 ; i++ ){
                int trapPos;
                do{
                    trapPos = Random.Int( Level.LENGTH );
                }
                while( !Level.fieldOfView[ trapPos ] || !Level.passable[ trapPos ] || Actor.findChar( trapPos ) != null );

                if( Dungeon.level.map[ trapPos ] == Terrain.INACTIVE_TRAP ){
                    Level.set( trapPos, Terrain.BLADE_TRAP );
                    GameScene.updateMap( trapPos );
                    ScrollOfClairvoyance.discover( trapPos );
                }

            }
        else //if (clonePhase)
            jumpClones(clearClones);

        int newPos;

        do{
            newPos = Dungeon.level.randomRespawnCell( false, true );
        } while( Level.adjacent( pos, newPos ) ||
                ( enemy != null && Level.adjacent( newPos, enemy.pos ) ) );


		sprite.move( pos, newPos );
		move( newPos );
        clearBlobs(newPos);
		
		if (Dungeon.visible[newPos]) {
			CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
		}
		if (spendTime) {
		    float speed=moveSpeed();
		    if (clonePhase && enemy instanceof Hero)
                speed=enemy.attackSpeed();
            spend(1 / speed);
        }

	}
	
	@Override
	public void notice() {
		super.notice();
        if( enemySeen ) {
            yell( "直面我, " + Dungeon.hero.heroClass.title() + "!" );
        }
	}


	private static void clearBlobs(int pos){
        Blob[] blobs = {
                Dungeon.level.blobs.get( Fire.class ),
                Dungeon.level.blobs.get( CorrosiveGas.class ),
                Dungeon.level.blobs.get( ConfusionGas.class ),
                Dungeon.level.blobs.get( FrigidVapours.class ),
                Dungeon.level.blobs.get( Web.class ),
                Dungeon.level.blobs.get( Electricity.class ),
                Dungeon.level.blobs.get( Darkness.class ),
                Dungeon.level.blobs.get( Miasma.class ),
        };

        for (Blob blob : blobs) {

            if (blob == null) {
                continue;
            }

            int value = blob.cur[pos];
            if (value > 0) {
                blob.cur[pos] -= value;
                blob.volume -= value;
            }
        }

    }

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public void die( Object cause, Element dmg ) {

//		Badges.Badge badgeToCheck = null;
//		switch (Dungeon.hero.heroClass) {
//		case WARRIOR:
//			badgeToCheck = Badge.MASTERY_WARRIOR;
//			break;
//		case SCHOLAR:
//			badgeToCheck = Badge.MASTERY_SCHOLAR;
//			break;
//		case BRIGAND:
//			badgeToCheck = Badge.MASTERY_BRIGAND;
//			break;
//		case ACOLYTE:
//			badgeToCheck = Badge.MASTERY_ACOLYTE;
//			break;
//		}
//		if (!Badges.isUnlocked( badgeToCheck )) {
//			Dungeon.bonus.drop( new TomeOfMastery(), pos ).sprite.drop();
//		}

        Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
        super.die( cause, dmg );

        Badges.validateBossSlain();

        killClones();

        yell( "终于解脱了..." );
    }

    public void killClones(){
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof TenguClone) {
                mob.die(null);
            }
        }
        clonePhase=false;
        damagedThisTurn=false;
    }
	
	@Override
	public String description() {
        return
                "天狗是远古刺客组织的成员，组织的名字也叫天狗。这些刺客以大量使用手里剑,幻术和陷阱而闻名。";/*+
            "Rumors tell that this prisoner was one of the most dangerous assassin of the clan, "+
            "you can tell by the look in his eyes that its sanity has been lost long ago...";*/

    }

    private static final String TIME_TO_JUMP	= "timeToJump";
    private static final String BREAKS	= "breaks";
    private static final String CLONES	= "clones";
    private static final String HITS_TO_ENRAGE	= "hitsBeforeEnrage";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( TIME_TO_JUMP, timeToJump );
        bundle.put( CLONES, clonePhase );
        bundle.put( HITS_TO_ENRAGE, hitsBeforeEnrage );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        clonePhase = bundle.getBoolean( CLONES );
        timeToJump = bundle.getInt( TIME_TO_JUMP );
        hitsBeforeEnrage= bundle.getInt( HITS_TO_ENRAGE );
    }

    public static class TenguClone extends MobRanged {

        public int revealed=0;
        public TenguClone() {
            super(3,0,false );

            HT=originalTengu().HT;
            HP=originalTengu().HP;
            name = Dungeon.depth == Statistics.deepestFloor ? "天狗" : "天狗的幻影";
            spriteClass = TenguSprite.class;
            armorClass=0;

            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

        }

        private static final String REVEALED	= "revealed";

        @Override
        public int dexterity() {
            return 0;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( REVEALED, revealed );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            revealed = bundle.getInt( REVEALED );
        }

        public void updateAlpha(){
            if (revealed>0 || Dungeon.hero!=null && Dungeon.hero.hasBuff(MindVision.class) ){
                sprite.alpha(0.4f);
            }else
                sprite.alpha(1);
        }

        public void reveal(){
            revealed=2;
            updateAlpha();
        }

        @Override
        public boolean act() {
            /*Tengu tengu =originalTengu();
            if (tengu!=null) {
                enemySeen = tengu.enemySeen;
                alerted = tengu.alerted;
            }*/
            spend( TICK );
            return true;
        }

        @Override
        public void damage( int dmg, Object src, Element type ) {
            reveal();
            //HP = 0;
            //die(src,type);
        }

        @Override
        public boolean add( Buff buff ) {
            if (buff.awakensMobs())
                reveal();
            return false;
        }

        @Override
        public boolean isMagical() {
            return true;
        }


        public void jump() {

            int newPos;
            Char enemy=originalTengu().enemy;
            do{
                newPos = Dungeon.level.randomRespawnCell( false, true );
            } while( Level.adjacent( pos, newPos ) ||
                    ( enemy != null && Level.adjacent( newPos, enemy.pos ) ) );

            HP=originalTengu().HP;
            sprite.move( pos, newPos );
            move( newPos );

            clearBlobs(newPos);

            if (Dungeon.visible[newPos]) {
                CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
            }

        }

        @Override
        public void die( Object cause, Element dmg ) {
            if (Dungeon.visible[pos])
                CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            destroy();
            sprite.kill();
        }

        @Override
        public String description() {
            Tengu tengu =originalTengu();
            return tengu.description()+ (revealed > 0 ? " 看上去，这个幻影非常的真实。":"");
        }
    }
}
