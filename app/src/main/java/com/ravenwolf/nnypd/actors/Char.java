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
package com.ravenwolf.nnypd.actors;

import com.ravenwolf.nnypd.Difficulties;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.BuffReactive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Adrenaline;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Levitation;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Overload;
import com.ravenwolf.nnypd.actors.buffs.special.SoulLink;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Controlled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.Exposed;
import com.ravenwolf.nnypd.actors.buffs.special.Focused;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.buffs.special.Light;
import com.ravenwolf.nnypd.actors.buffs.special.UnholyArmor;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Tengu;
import com.ravenwolf.nnypd.items.armours.shields.ArcaneShield;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.rings.RingOfSharpShooting;
import com.ravenwolf.nnypd.items.rings.RingOfVitality;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.levels.features.Door;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Pushing;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Char extends Actor {

//	protected static final String TXT_HIT		= "%s hit %s";
//	protected static final String TXT_KILL		= "You %s...";
/*	protected static final String TXT_DEFEAT	= "%s is defeated!";

	protected static final String TXT_DODGED	= "dodged";
	protected static final String TXT_MISSED	= "missed";

	protected static final String TXT_GUARD 	= "guard";
	protected static final String TXT_AMBUSH	= "sneak attack!";
	protected static final String TXT_COUNTER	= "counter attack!";
	protected static final String TXT_EXPOSED	= "exposed!";*/
    protected static final String TXT_DEFEAT	= "你击败了%s。";

    protected static final String TXT_DODGED	= "闪避";
    protected static final String TXT_MISSED	= "未命中";

    protected static final String TXT_GUARD 	= "格挡";
    protected static final String TXT_AMBUSH	= "伏击！";
    protected static final String TXT_COUNTER	= "弹反";
    protected static final String TXT_EXPOSED	= "抵挡!";

//	private static final String TXT_YOU_MISSED	= "%s %s your attack";
//	private static final String TXT_SMB_MISSED	= "%s %s %s's attack";

    public boolean ignoreDarkness() {
        return false;
    }

    protected static final int VIEW_DISTANCE	= 8;

	public int pos = 0;
    public int prevPos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";

	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;

    public boolean friendly = false;
	public boolean stunned      = false;
	public boolean rooted		= false;
	public boolean flying		= false;
    public boolean moving		= false;

	public int invisible		= 0;

	private HashSet<Buff> buffs = new HashSet<Buff>();


	@Override
	protected boolean act() {
		Dungeon.level.updateFieldOfView( this );
        BuffReactive.check( this );
        moving = false;

		return false;
	}

    public int viewDistance() {
        return buff( Blinded.class ) == null ? VIEW_DISTANCE - (Dungeon.level.feeling== Level.Feeling.HAUNT? 1 : 0 ): 1 ;
    }

    private static final String POS			= "pos";
	private static final String TAG_HP		= "HP";
	private static final String TAG_HT		= "HT";
	private static final String BUFFS		= "buffs";

	@Override
    public int actingPriority(){
        return 3;
    }

    public boolean ignoresMissiles(){
        return false;
    }

    public boolean sharedVision(){
        return hasBuff(Revealed.class);
    }

	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );

		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );

		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachOnLoad( this );
			}
		}
	}

    public boolean isFriendly(){
        return !Bestiary.isBoss( this ) && isCharmedBy( Dungeon.hero ) || friendly;
    }

	public void expose(Char enemy){
	    //Tormented chars cannot counter attack
	    if (enemy.hasBuff(Tormented.class))
	        return;

        Exposed exposed = Buff.affect( this, Exposed.class );

        if( exposed != null ) {
            exposed.object = enemy.id();
            exposed.reset(0);
        }
    }

    public boolean attack( Char enemy ) {
        int damageRoll = damageRoll();

        return attack(enemy,damageRoll);
        //return attack(enemy,damageRoll);
    }

	
	public boolean attack( Char enemy, int damageRoll ){

        Guard guarded = enemy.buff( Guard.class );

        if( guarded != null && enemy.guard( damageRoll, accuracy(), isRanged() )) {
            //successful block
            guarded.proc( enemy.hasShield() );

            attackProc( enemy, damageRoll, true );
            enemy.defenseProc( this, damageRoll, true );

            //enemy awareness affect counter chance
            if (enemy instanceof Hero) { //hero can no longer be exposed
                if (Level.adjacent( pos, enemy.pos ) && Random.Float() * awareness() < enemy.counterChance()) {
                    expose(enemy);
                }
            }

            return true;

        } else if( hit( this, enemy, isRanged() && !ignoresDistancePenalty(), false ) ) {

            hitEnemy( enemy,  damageRoll);
            return true;

        } else {//miss

            boolean visibleFight = Dungeon.visible[ pos ] || Dungeon.visible[ enemy.pos ];
            if ( visibleFight ) {
                Sample.INSTANCE.play(Assets.SND_MISS);
            }
            if (enemy instanceof Hero && ((Hero)enemy).subClass == HeroSubClass.SLAYER){
                Hero hero = (Hero)enemy;
                if (hero.belongings.weap1 instanceof MeleeWeapon && Level.adjacent(hero.pos,pos) &&   hero.awareness() * Random.Float(0.30f) > Random.Float()){
                    expose(hero);
                    CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 2 );
                }
            }
            enemy.missed();
            return false;
        }
	}

	//Logic for processing all the damage calculations. Requires to have the current or ranged weapon variable assigned
	public int hitEnemy(Char enemy, int damageRoll){

        damageRoll = enemy.defenseProc( this, damageRoll, false );

        if( !ignoresAC(enemy) ) {
            damageRoll = enemy.absorb( damageRoll, penetrateAC(enemy) );
        }

        damageRoll = attackProc( enemy, damageRoll, false );
        enemy.damage( damageRoll, this, damageType());

        Guard guarded = enemy.buff( Guard.class );
        if( guarded != null ) guarded.fail( enemy.hasShield() );

        if (enemy == Dungeon.hero) {
//            Dungeon.hero.interrupt( "You were awoken by an attack!" );
            Dungeon.hero.interrupt( "你被突如其来的攻击惊醒了！" );

        }

        boolean visibleFight = Dungeon.visible[ pos ] || Dungeon.visible[ enemy.pos ];
        if (visibleFight) {
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
            enemy.sprite.bloodBurstA(sprite.center(), damageRoll );
        }
        return damageRoll;
    }
	
	public static boolean hit( Char attacker, Char defender, boolean ranged, boolean magic ) {

	    if (defender.equals(attacker))
	        return true;

        //FIXME
	    if (defender instanceof Tengu.TenguClone){
            ((Tengu.TenguClone)defender).reveal();
//            GLog.w("Your attack pass trough the illusion");
            GLog.w("你攻击了幻影。");
            return false;
        }
    /*
            Guard guard=defender.buff( Guard.class );
            if( guard != null && defender.hasShield())
                return true;
    */
        if( defender.isExposedTo(attacker) )
            return true;

        if( defender.isCharmedBy(attacker) )
            return true;

        int attValue = ( magic ? attacker.magicSkill() : attacker.accuracy() );

        if( !ranged || Level.fieldOfView[ defender.pos ] )
            attValue *= 2.5f;

        if( attacker.buff( Focused.class ) != null ){
            attValue *= 1.5f;
        }

        int defValue = defender.dodgeChance(ranged);

        if( ranged ) {
            int distance = Math.min( 10, Level.distance(attacker.pos, defender.pos) );
            //should be replaced by weap max range?
            if( distance > 1 ) {
                float penalty=1-(10- distance)/9.0f;

                penalty=1-(penalty/attacker.ringBuffs(RingOfSharpShooting.SharpShooting.class));
                attValue =  (int)(attValue *penalty);
            }else if( distance==1 && !magic)
                //Ranged weapons have its accuracy reduced at close combat
                    attValue =  (int)(attValue *0.4f);

        }

        int roll = Random.Int( attValue + defValue );

        return attValue > roll;
	}

    public int absorb( int damage ) {
        return absorb( damage,false );
    }

    public int absorb( int damage, boolean penetrate ) {
        Guard guarded = buff( Guard.class );
	    return absorb( damage,guarded!=null,penetrate );
    }

    //uses min AC
    private int absorb( int damage,boolean withShield, boolean penetrate ) {

        int minAC=minAC();

        int maxAC=armorClass(withShield);
        int AC=Random.NormalIntRange(minAC,maxAC);

        return AC > 0 && damage > 0 ? damage * damage / ( damage + (penetrate ? AC : AC * 2) ) : damage;
    }

    public boolean guard( int damage, int accuracy, boolean ranged ) {

        float guardStrength = guardStrength(ranged) * guardPenalty();
        int enemyChanceToHit = accuracy;
        enemyChanceToHit += damage;
        float blockChance = guardStrength / (guardStrength + enemyChanceToHit);
        return Random.Float() < blockChance;
    }


    public void missed() {

        if ( sprite.visible ) {
            sprite.showStatus( CharSprite.NEUTRAL, dexterity() > 0 ? TXT_DODGED : TXT_MISSED );
        }

        if ( this == Dungeon.hero ) {
            Dungeon.hero.interrupt();
        }
    }

    protected float healthValueModifier() {
        return 1.0f;
    }

    final public int currentHealthValue() {
        return (int)( HP * healthValueModifier() );
    }

    final public int totalHealthValue() {
        return (int)( HT * healthValueModifier() );
    }

	public int accuracy() {
        return 0;
    }

    public boolean isEthereal() {
        return false;
    }

    public boolean isSolid() {
        return  buff( Petrificated.class ) != null;
    }

	public int dexterity() {
		return 0;
	}

    public float dextModifier() {
        float modifier = 1.0f;

        if( buff( Crippled.class ) != null && !flying )
            modifier *= 0.5f;

        if( buff( Dazed.class ) != null )
            modifier *= 0.75f;

        if( buff( Revealed.class ) != null )
            modifier *= 0.5f;

        if( buff( Disrupted.class ) != null )
            modifier *= 0.5f;

        if( buff( Chilled.class ) != null )
            modifier *= 0.5f;

        if( buff( Ensnared.class ) != null )
            modifier *= 0.5f;

        return modifier;
    }

    public int dodgeValue(boolean ranged){
        return dexterity();
    }

    public int dodgeChance(boolean ranged) {
        int defValue= dodgeValue(ranged);

        //Blocking with shield reduces dodge at half
        if(hasBuff(Guard.class) && hasShield())
            defValue/=2;

        if (this instanceof Hero && ((Hero)this).subClass == HeroSubClass.SLAYER)
            return defValue;

        //reduces dodge chance if near unpassable terrain
        int impassable = 16;

        for (int n : Level.NEIGHBOURS8) {
            try{
                if( Actor.findChar( pos + n ) != null || Level.solid[pos + n] || Level.chasm[pos + n] && !flying ) {
                    impassable--;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
        }

        defValue = defValue * impassable / 16;

        return defValue;
    }
	public int magicSkill() {
		return 0;
	}

    public float attackDelay() {
        return TICK / attackSpeed();
    }

    public float attackSpeed(){
        return TICK * (buff( Adrenaline.class ) != null ? 1.5f : 1f);
    }

    public int damageRoll() {
        return 0;
    }

    public int armourAC() {
        return 0;
    }

    public int minAC() {
        return (buff( Petrificated.class ) != null ? totalHealthValue() / 6 :0);
    }

    public int shieldAC() {
        return 0;
    }

    public int guardStrength(boolean ranged) {
        return 0;
    }

    public int armorClass() {

        return armorClass( false );

    }

	public int armorClass( boolean withShield ) {

        float armourMod = 1.0f;

        Decay decay = buff(Decay.class);
        if ( decay != null ) {
            armourMod *= 1-decay.getReduction();
        }

        if ( buff(Withered.class) != null ) {
            armourMod *= 0.75f;
        }

        if ( buff(Revealed.class) != null ) {
            armourMod *= 0.5f;
        }

        if ( buff(Corrosion.class) != null ) {
            armourMod *= 0.5f;
        }

		return Math.round(( armourAC() + ( withShield ? shieldAC() : 0 )) * armourMod );

	}

    public float guardPenalty() {

        float guardChance = 1.0f;

        if ( buff( Poisoned.class ) != null ) {
            guardChance *= 0.75f;
        }

        if ( buff( Dazed.class ) != null ) {
            guardChance *= 0.5f;
        }

        if ( buff( Blinded.class ) != null ) {
            guardChance *= 0.25f;
        }

        if ( buff( Disrupted.class ) != null ) {
            guardChance *= 0.5f;
        }

        return guardChance;
    }

    public float counterChance() {
        return awareness() * 0.5f;
    }

    public float counterBonusDmg() {
        return 0.3f;
    }

    public boolean invulnerable(){
        return hasBuff(UnholyArmor.class);
    }
	
	public int attackProc( Char enemy, int damage, boolean blocked ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage, boolean blocked ) {

        if( isExposedTo( enemy, true ) ) {
            float damageBonus = enemy.damageRoll() * enemy.counterBonusDmg();
            damage += damageBonus;
            //FIXME
            if (enemy instanceof Hero && ((Hero)enemy).subClass != HeroSubClass.KNIGHT)
                damage*=0.66f;//counter attacks deals 0.66 damage
            if( sprite != null ) {
                sprite.emitter().burst(Speck.factory(Speck.MASTERY), 6);
                sprite.showStatus(CharSprite.POSITIVE, TXT_COUNTER);
            }
            //shield slam cause knock back on exposed enemies
            //if (enemy instanceof Hero && ((Hero)enemy).currentWeapon instanceof Shield.ShieldSlam){
            //     knockBack(enemy, damage);
            //    spend(1f);
            //}

        }
		return damage;
	}

    public int STR() {
        return 0;
    }

	public Element damageType() {
		return Element.PHYSICAL;
	}

    public boolean ignoresAC(Char enemy) { return false; }

    public boolean penetrateAC(Char enemy){
        return penetrateAC(enemy, damageType());
    }

    public boolean penetrateAC(Char enemy,Element type){
        return !( type instanceof Element.Physical );
    }

    public boolean penetrateShield(Char enemy,Element type){
        if (enemy instanceof Hero && ((Hero) enemy).belongings.weap2 instanceof Shield){
            Shield shield = (Shield)((Hero) enemy).belongings.weap2;
            if (shield instanceof ArcaneShield)
                return false;

            if(shield.glyph != null) {
                if (type != null && type.getClass() == shield.glyph.resistance())
                    return false;
            }
        }
        return !( type instanceof Element.Physical );
    }


    public boolean hasShield() {
        return false;
    }

    public boolean immovable() {
        return false;
    }

	public float moveSpeed() {
        return ( buff( Levitation.class ) == null && buff( Adrenaline.class ) == null? ( buff( Crippled.class ) == null ? baseSpeed : baseSpeed * 0.5f ) : baseSpeed * 1.5f );
	}

    public float awareness() {
        return buff( Dazed.class ) == null && buff( Blinded.class ) == null && buff( Disrupted.class ) == null ? 1.0f : 0.5f ;
    }

    public float stealth() {
        return buff( Burning.class ) == null && buff( Ensnared.class ) == null ? 1.0f : 0.5f ;
    }

    public float willpower(){
	    return buff( Tormented.class ) == null ? 1.0f : 0.5f ;
    }

    public boolean isRanged() {
        return false;
    }

    public boolean ignoresDistancePenalty(){
        return false;
    }

    public boolean isMagical() {
        return false;
    }

    public boolean isHeavy() {
        return STR() > Dungeon.hero.STR();
    }

	public void heal( int value ) {

        if (HP <= 0 || value <= 0) {
            return;
        }

        if( buff( Withered.class ) != null ) {
            value -= value/4 + ( Random.Int( 4 ) < value % 4 ? 1 : 0 );
        }
        if( buff( RingOfVitality.Vitality.class ) != null ){
            value *= ringBuffsHalved( RingOfVitality.Vitality.class );
        }

        HP = Math.min( HP + value, HT );

        sprite.showStatus( CharSprite.POSITIVE, Integer.toString( value ) );

    }

	public void damage( int dmg, Object src, Element type ) {

		if (HP <= 0) {
			return;
		}
        if( this instanceof Hero ){
            if( Dungeon.difficulty == Difficulties.EASY ) {
                dmg -= ( dmg / 3);
            } else if( Dungeon.difficulty == Difficulties.IMPOSSIBLE ) {
                dmg += ( dmg / 2 + ( Random.Int(2) < dmg % 2 ? 1 : 0 ) );
            }

//            Dungeon.hero.interrupt( "You were awoken by an attack!" );
            Dungeon.hero.interrupt( "你被突如其来的攻击惊醒了！" );

        }

        SoulLink soulLink=buff(SoulLink.class);
        if (soulLink != null && soulLink.object != 0) {
            dmg =soulLink.redirectDamage( dmg, src, type);
        }

        //then reduce it
        boolean amplified = false;
        int textColor = CharSprite.NEGATIVE;

        if( type != null ) {

            float resist = Element.Resist.getResistance( this, type );

            if( !Element.Resist.checkIfDefault( resist ) ) {

                if ( Element.Resist.checkIfNegated( resist ) ) {

                    dmg = 0;
                    textColor = CharSprite.NEUTRAL;

                } else if ( Element.Resist.checkIfPartial( resist ) ) {

                    dmg = dmg / 2 + Random.Int(dmg % 2 + 1);
                    textColor = CharSprite.WARNING;

                } else if ( Element.Resist.checkIfAmplified( resist ) ) {

                    dmg += Random.IntRange( 1, dmg );
                    amplified = true;

                }
            }

            dmg = type.proc( this, dmg );
            //Element effect could have killed the target, so return...
            if (HP <= 0) {
                return;
            }
        }

		sprite.showStatus( textColor,  dmg + ( amplified ? "!" : "" ) );

        sprite.flash();

        Frozen buff=buff(Frozen.class);
        if (buff!=null )
            buff.detach();

        Frenzy frenzy=buff( Frenzy.class );
        if( frenzy != null ){
            frenzy.increase(dmg);
        }

        if( src instanceof Char && isCharmedBy( (Char)src ) ) {
            removeCharmSource();
        }

        if (this == Dungeon.hero && dmg > 0) {

            if (dmg > HT/5)
                Camera.main.shake(GameMath.gate(1, dmg / (HT / 5), 5), 0.3f);
            if (HP - dmg <= HT/5)
                GameScene.flash(0x330000);

            Hero hero = (Hero) this;
            if (hero.subClass == HeroSubClass.BERSERKER && src instanceof Mob) {
                if (Math.sqrt(HP*2) <= Random.Int(dmg*3)) {
                    if( frenzy != null ){
                        frenzy.increase(dmg);
                    }else {
                        int bonus = dmg + (int)(dmg * (1 - HP/ (float)HT));
                        BuffActive.add(hero, Frenzy.class, GameMath.gate(8, bonus, 18));
                    }
                }
            }
        }

        HP -= dmg;

		if ( !isAlive() ) {
			die(src, type);
		}
	}
	
	public void destroy() {
		HP = 0;

		Actor.remove(this);
        Actor.freeCell(pos);
    }

    public void die( Object src) {

        die(src, null);

    }

	public void die( Object src, Element dmg ) {
		destroy();

		sprite.die();
	}

    public boolean detected( Char ch ) {
	    float stealth=ch.stealth() * (ch.buff( Light.class) != null ? 0.5f :1);
        return Random.Float( stealth ) * ( !ch.flying ? Dungeon.level.stealthModifier( ch.pos ) : 1.5f )
                < Random.Float( awareness() * 2.0f ) / Math.sqrt( distance(ch) + 1 );

    }
	
	public boolean isAlive() {
		return HP > 0;
	}

    public boolean isDamagedOverTime() {
        for (Buff b : buffs) {
            if (b instanceof Burning
                || b instanceof Poisoned
                || b instanceof Corrosion
                || b instanceof Crippled
                || b instanceof Bleeding
                || b instanceof Decay
            ) {
                return true;
            }
        }
        return false;
    }

	@Override
	public void spend( float time ) {

		float timeScale = 1f;

		if (buff( Chilled.class ) != null) {
			timeScale *= 0.667f;
		}

//		if (buff( Speed.class ) != null) {
//			timeScale *= 1.5f;
//		}

		super.spend( time / timeScale );
	}
	
	public HashSet<Buff> buffs() {
		return buffs;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<T>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}


    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffs( Class<T> c ) {

        float bonus = 1.0f;

        for (Buff b : buffs) {
            if ( c.isInstance( b )) {
                bonus += ((Ring.RingBuff)b).effect();
            }
        }
        //Overload increase rings effect by 20%
        if (this instanceof Hero && ((Hero)this).subClass == HeroSubClass.CONJURER && hasBuff(Overload.class))
            bonus+=0.2f;

        return bonus;
    }


    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsHalved( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 1.0f + bonus ) / 2.0f;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsThirded( Class<T> c ) {

        float bonus = ringBuffs( c );

        return ( 2.0f + bonus ) / 3.0f;

    }

    @SuppressWarnings("unchecked")
    public <T extends Ring.RingBuff> float ringBuffsBaseZero( Class<T> c ) {

        return ringBuffs( c ) - 1.0f;

    }

	@SuppressWarnings("unchecked")
	public <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

    @SuppressWarnings("unchecked")
    public <T extends Buff> boolean hasBuff(  Class<T> c ) {

        return buff( c )!=null;
    }

    public boolean isExposedTo( Char ch, boolean detach ) {
        int chID = ch.id();
        for (Buff b : buffs) {
            if (b instanceof Exposed && ((Exposed)b).object == chID) {
                if (detach)
                    b.detach();
                return true;
            }
        }
        return false;
    }

    public boolean isExposedTo( Char ch ) {

        return isExposedTo(ch, false);
    }

    public boolean isCharmedBy( Char ch ) {

        int chID = ch.id();

        return isCharmed() == chID;
    }

    public int isCharmed() {

        for (Buff b : buffs){
            if( b instanceof Charmed ){
                return ( (Charmed) b ).object;
            } else if( b instanceof Controlled ){
                return ( (Controlled) b ).object;
            }
        }

        return 0;
    }

    public void removeCharmSource() {

        for (Buff b : buffs){
            if( b instanceof Charmed ){
                 ( (Charmed) b ).object=0;
            } else if( b instanceof Controlled ){
                ( (Controlled) b ).object=0;
            }
        }

    }

    public boolean isScared() {

        return buff( Tormented.class ) != null || buff( Banished.class ) != null;

    }
	
	public boolean add( Buff buff ) {

	    if (!isAlive())
            return false;

		buffs.add(buff);
		Actor.add(buff);

        return true;
	}
	
	public void remove( Buff buff ) {
		
		buffs.remove(buff);
		Actor.remove(buff);

    }
	
	public void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs(buffClass)) {
			remove(buff);
		}
	}
	
	@Override
	protected void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[0])) {
			buff.detach();
		}
	}

	public void updateSpriteState() {
		for (Buff buff:buffs) {

            buff.applyVisual();

		}
	}

	public void move( int step ) {
		
		if (Level.adjacent( step, pos ) && Random.Int( 2 ) == 0 && ( ( buff( Dazed.class ) != null ) ) ) {

			step = pos + Level.NEIGHBOURS8[Random.Int( 8 )];

			if ( Level.solid[step] || Actor.findChar( step ) != null ) {
				return;
			}
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

        Actor.freeCell( pos );

        prevPos=pos;
		pos = step;
		
		if (Dungeon.level.map[pos] == Terrain.DOOR_CLOSED) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.visible[pos];
		}

        Actor.occupyCell( this );

        Dungeon.level.press(pos, this);

        moving = true;
	}

	private void knockbackDamage(int damage){

        if(isSolid()) {
//            sprite.showStatus(CharSprite.NEGATIVE, "crushed");
            sprite.showStatus(CharSprite.NEGATIVE, "重击");

            damage += damage / 2;
        }
        damage( absorb( damage ), null, Element.PHYSICAL );
        if (isAlive() && damage>0)
            BuffActive.addFromDamage( Char.this, Dazed.class, damage );
    }

    private void pushedIntoSomething( final int pushDamage, final int collidePos, final int finalPos, final Callback callback){
        knockbackDamage(pushDamage);
        Char pushedInto=Actor.findChar(collidePos);
        if (pushedInto != null) {
            pushedInto.knockbackDamage(pushDamage);
        }
        if( Dungeon.visible[ Char.this.pos ] ) {
            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.5f );
            Camera.main.shake( 2, 0.1f );
        }
        Actor.addDelayed(new Pushing(Char.this, collidePos, finalPos, new Callback() {

            @Override
            public void call(){
                if( callback != null ){
                    callback.call();
                }
            }
        }), -1 );

        for (Mob mob : Dungeon.level.mobs) {
            if ( this != mob && Level.distance( this.pos, mob.pos ) <= 4 ) {
                mob.beckon( this.pos );
            }
        }
    }

    public void knockBack( Char attacker, int damage) {
        knockBack( attacker.pos, damage, 1 ,null);
    }

    public void knockBack( int sourcePos, int damage, int amount ) {
        knockBack( sourcePos, damage, amount ,null);
    }

    public void knockBack(int sourcePos, int damage, int amount, final Callback callback) {
        //Do nothing if char is immovable
        if (immovable()){
            if( callback != null )
                callback.call();
            return;
        }

        //Check where it would land the target if nothing on the way
        int dist=Level.distance(sourcePos, pos)+1;
        Ballistica.castToMaxDist( sourcePos, pos, dist+amount);
        int newPos=pos;
        //Based on amount of pushed cells whe calculate the bonus damage
        int pushedCells=0;
        boolean hitSolidObject=false;
        int tempPos;

        for (int i=dist; i <= Ballistica.distance ; i++) {
            tempPos = Ballistica.trace[i];
            Char ch = Actor.findChar(tempPos);
            pushedCells++;
            if ( Level.solid[tempPos] || ch != null) {
                final int pushDamage =(int) (damage*Math.sqrt(pushedCells) / 3);
                final int collidePos=tempPos;
                final int finalPos=newPos;
                Actor.addDelayed(new Pushing(this, pos, collidePos, new Callback() {
                    @Override
                    public void call(){
                        pushedIntoSomething(pushDamage, collidePos, finalPos, callback);
                    }
                }), -1 );
                hitSolidObject=true;
                break;
            }
            newPos=tempPos;
        }

        if( newPos !=pos ) {
            if (!hitSolidObject)
                Actor.addDelayed(new Pushing(this, pos, newPos, callback), -1);

            Actor.freeCell(pos);
            pos = newPos;
            Actor.occupyCell(this);
            Dungeon.level.press(newPos, this);
        }
    }

	public int distance( Char other ) {
		return Level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		next();
	}
	
	public void onAttackComplete() {
		next();
	}

    public void onCastComplete() {
        next();
    }

    public void onComplete() {
        next();
    }
	
	public void onOperateComplete() {
		next();
	}

	public HashMap<Class<? extends Element>, Float> resistances() {
		return new HashMap<>();
	}

}
