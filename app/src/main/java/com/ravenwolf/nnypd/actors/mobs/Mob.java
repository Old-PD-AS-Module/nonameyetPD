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
import com.ravenwolf.nnypd.Challenges;
import com.ravenwolf.nnypd.Difficulties;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Amok;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Controlled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.rings.RingOfFortune;
import com.ravenwolf.nnypd.items.rings.RingOfShadows;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeaponLight;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.features.Chasm;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.visuals.effects.Wound;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.ui.HealthIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.HashSet;


public abstract class Mob extends Char {

    private static final String	TXT_DIED     = "你听见有什么东西在远处消亡了";
    private static final String TXT_HEARD    = "你认为自己听到了%s在附近%s的声音";

    protected static final String TXT_ECHO  = "回响..";

    protected static final String TXT_RAGE	= "#$%^";

    public static final String TRIBE_DEFAULT = "NONE";
    public static final String TRIBE_LONER = "LONER";
	public static final String TRIBE_UNDEAD	= "UNDEAD";
    public static final String TRIBE_BEAST	= "BEAST";
    public static final String TRIBE_ACUATIC= "ACUATIC";
    public static final String TRIBE_GNOLL= "GNOLL";
	
	public AiState SLEEPING     = new Sleeping();
	public AiState HUNTING		= new Hunting();
	public AiState WANDERING	= new Wandering();
	public AiState FLEEING		= new Fleeing();
	public AiState PASSIVE		= new Passive();
	public AiState state        = SLEEPING;
	
	public Class<? extends CharSprite> spriteClass;
	
	protected int target = -1;

	protected int EXP = 0;
    protected int tier = 0;
    protected int maxLvl = 25;

    protected int minDamage = 0;
    protected int maxDamage = 0;

    protected int accuracy = 0;
    protected int dexterity = 0;
    protected int armorClass = 0;

    protected HashMap<Class<? extends Element>, Float> resistances = new HashMap<>();

    protected Char enemy;
    protected boolean enemySeen;
    protected boolean alerted = false;
    protected static final float TIME_TO_WAKE_UP = 1f;

    public boolean hostile = true;
    public boolean special = false;
    public boolean noticed = false;

    private boolean recentlyNoticed = false;

	private static final String STATE	= "state";
    private static final String TARGET	= "target";
    private static final String ALERTED	= "alerted";
    private static final String NOTICED	= "noticed";

    public String getTribe() {
        return TRIBE_DEFAULT;
    }


    /*追加代码*/
    public boolean mindVision() {
        return true;
    }


    @Override
    public int STR() {
        return 7+tier*2;
    }

    @Override
    public int accuracy() {

        float modifier = 1.0f;

        if( buff(Enraged.class) != null )
            modifier *= 1.5f;

        if( buff( Tormented.class ) != null ||  buff( Banished.class ) != null )
            modifier *= 0.5f;

        if( buff( Charmed.class ) != null )
            modifier *= 0.5f;

       if( buff( Controlled.class ) != null )
            modifier *= 0.75f;

       //dazed enemies will have less chance to hit
        if( buff( Dazed.class ) != null || buff( Disrupted.class ) != null )
            modifier *= 0.5f;

        if ( buff( Blinded.class ) != null ) {
            modifier *= 0.25f;
        }

        if( buff( Chilled.class ) != null )
            modifier *= 0.5f;

        return (int)( accuracy * modifier );
    }

    @Override
    public int dexterity() {

        if( !enemySeen || stunned ){
            return 0;
        }

        return (int)(dexterity * dextModifier());
    }

    @Override
    public int magicSkill() {

        return accuracy() * 2;

    }

    @Override
    public int armourAC() {

        int AC=armorClass;

        if( buff( Shielding.class ) != null ){
            AC += totalHealthValue() / 5;
        }

        if( buff( Petrificated.class ) != null ){
            AC += totalHealthValue() / 4;
        }

        return AC;

    }

    @Override
    public int minAC() {
        return super.minAC() + Math.min(tier-1,armorClass);//some enemies have 0 AC so min AC is 0 also
    }

    @Override
    public float guardPenalty() {
        return enemySeen && !stunned? super.guardPenalty() : 0.0f;
    }

    @Override
    public int damageRoll() {

        int damage = Random.NormalIntRange( minDamage, maxDamage );

        if( buff( Enraged.class ) != null )
            damage += Random.NormalIntRange( minDamage, maxDamage )/2;

        if( buff( Poisoned.class ) != null )
            damage -= damage/4;

        if( buff( Withered.class ) != null )
            damage-= damage/4;

        if (buff( Charmed.class ) != null)
            damage /= 2;

        if( buff( Controlled.class ) != null )
            damage-= damage/4;

        Decay decay=buff(Decay.class);
        if (decay!=null ){
            damage -= damage *decay.getReduction();
        }

        return damage;
    }

    @Override
    public float awareness() {
        return state == HUNTING ? super.awareness() : super.awareness() * 0.5f ;
    }

    @Override
    public float stealth() {
        return state != HUNTING ? super.stealth() : super.stealth() * 0.5f ;
    }

    public int viewDistance() {
        return ( state != SLEEPING ? super.viewDistance() : super.viewDistance() / 2 ) ;
    }

    public int dodgeValue(boolean ranged){
        int dex=dexterity();
        if (ranged && dex==0){
            dex=(int)(dexterity * dextModifier()/3);
        }
        return dex;
    }


    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();
        resistances.putAll(this.resistances);

        if( buff( Shielding.class ) != null ){
            for( Class<? extends Element> type : Shielding.RESISTS) {
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + Shielding.RESISTANCE );
            }
        }
        if( buff( Disrupted.class ) != null ){
            resistances.put( Element.Energy.class, ( resistances.containsKey( Element.Energy.class ) ? resistances.get( Element.Energy.class ) : 0.0f )+Element.Resist.VULNERABLE);
        }
        if( buff( Frozen.class ) != null ){
            resistances.put( Element.Frost.class, ( resistances.containsKey( Element.Frost.class ) ? resistances.get( Element.Frost.class ) : 0.0f )+Element.Resist.PARTIAL );
        }
        if( buff( Petrificated.class ) != null ){
            resistances.put( Element.Body.class, ( resistances.containsKey( Element.Body.class ) ? resistances.get( Element.Body.class ) : 0.0f )+Element.Resist.IMMUNE );
        }
        return resistances;
    }
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle(bundle);
		
		if (state == SLEEPING) {
			bundle.put( STATE, Sleeping.TAG );
		} else if (state == WANDERING) {
			bundle.put( STATE, Wandering.TAG );
		} else if (state == HUNTING) {
			bundle.put( STATE, Hunting.TAG );
		} else if (state == FLEEING) {
			bundle.put( STATE, Fleeing.TAG );
		} else if (state == PASSIVE) {
			bundle.put( STATE, Passive.TAG );
		}

		bundle.put( TARGET, target );
        bundle.put( NOTICED, noticed );
        bundle.put( ALERTED, enemySeen );
	}

	protected void adjustStatsByDifficulty(boolean isBoss, int exp){
        if( !isBoss ) {
            if( Dungeon.difficulty == Difficulties.EASY ) {
                HT += HT/2;
            } else if( Dungeon.difficulty > Difficulties.EASY ) {
                HT = HT * 2 + HT/2 * (Dungeon.difficulty -1);
            }

            EXP = exp;
            maxLvl = exp + 5;

        } else {

            if( Dungeon.difficulty > Difficulties.HARDCORE ) {
                HT = HT * 14;
            } else {
                HT = HT * 7 + HT * 2 * Dungeon.difficulty;
            }

            EXP = exp * 5;
            maxLvl = 25;

            minDamage += tier - 1;
            maxDamage += tier - 1;

            dexterity /= 2;
            armorClass /= 2;
        }

        HP = HT;
    }
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle(bundle);
		
		String state = bundle.getString( STATE );
		if (state.equals( Sleeping.TAG )) {
			this.state = SLEEPING;
		} else if (state.equals( Wandering.TAG )) {
			this.state = WANDERING;
		} else if (state.equals( Hunting.TAG )) {
			this.state = HUNTING;
		} else if (state.equals( Fleeing.TAG )) {
			this.state = FLEEING;
		} else if (state.equals( Passive.TAG )) {
			this.state = PASSIVE;
		}

		target = bundle.getInt( TARGET );
        noticed = bundle.getBoolean( NOTICED );
        enemySeen = bundle.getBoolean( ALERTED );
	}
	
	public CharSprite sprite() {
		CharSprite sprite = null;
		try {
			sprite = spriteClass.newInstance();
		} catch (Exception e) {
            return null;
		}
		return sprite;
	}
	
	@Override
	protected boolean act() {
		
		super.act();

		boolean justAlerted = alerted;
        alerted = false;

        if( noticed ) {

            noticed = false;
            recentlyNoticed = true;

        } else if( recentlyNoticed && Level.distance( pos, Dungeon.hero.pos ) > 2 ) {

            recentlyNoticed = false;

        }
		
		if (stunned) {
			spend( TICK );
			return true;
		}

		enemy = chooseEnemy();
		
		boolean enemyInFOV = 
			enemy != null && enemy.isAlive() && 
			Level.fieldOfView[enemy.pos] && enemy.invisible <= 0;

        boolean act = state.act( enemyInFOV, justAlerted );

        if( !recentlyNoticed && Dungeon.hero.isAlive() && !Dungeon.hero.restoreHealth
            && !sprite.visible && state != PASSIVE && Level.distance( pos, Dungeon.hero.pos ) == 2
            && Dungeon.hero.detected( this )
        ) {
            Dungeon.hero.interrupt( "你被附近的声响吵醒了。" );

            if( !enemySeen ) {
                GLog.w(TXT_HEARD, name, state.status());
            }

//            noticed = true;
        }
		
		return act;
	}

    public void resetEnemy() {
        enemy = null;
    }
	
	protected Char chooseEnemy() {

        if(  enemy != null && !enemy.isAlive() )
            enemy = null;

        HashSet<Char> candidates = new HashSet<>();

        if( isFriendly() ){

            for( Mob mob : Dungeon.level.mobs ){
                if( mob != this && Level.fieldOfView[ mob.pos ] && mob.hostile && !mob.isFriendly() ){
                    candidates.add( mob );
                }
            }

        } else {

            //66% to proc amok
            boolean amokProc=buff(Amok.class)!=null && Random.Int(3)!=0;
            candidates.add( Dungeon.hero );

            for( Mob mob : Dungeon.level.mobs ){
                if( mob != this && Level.fieldOfView[ mob.pos ] && (mob.isFriendly() || amokProc) ){
                    candidates.add( mob );
                }
            }

            if (amokProc){
                //if proc, have chance to choose a random enemy (even themselves)
                if (Random.Int(2)==0){
                    enemy=this;
                    candidates.add(this);
                }
                enemy=Random.oneOf(candidates.toArray(new Char[candidates.size()]));
            }
        }

        if( candidates.size() > 0 && !candidates.contains(enemy)){
            for( Char ch : candidates ){
                if( enemy == null || ch == Dungeon.hero || Level.distance( pos, ch.pos ) < Level.distance( pos, enemy.pos ) ){
                    if( Level.adjacent( pos, ch.pos ) || Dungeon.findPath( this, pos, ch.pos,
                            flying ? Level.passable : Level.mob_passable, Level.fieldOfView ) > -1 ){
                        enemy = ch;
                    }
                }
            }
        }
        //if for some reason no enemy is found
        if (enemy==null && !isFriendly())
            enemy=Dungeon.hero;

        return enemy;
	}
	
	protected boolean moveSprite( int from, int to ) {

		if (sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
			sprite.move( from, to );
			return true;
		} else {
			sprite.place( to );
			return true;
		}
	}

    // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
    protected boolean canBeBackstabed() {
        return (dexterity() != 0 || invulnerable() || this.stunned) ? false : true;
    }

    @Override
	public boolean add( Buff buff ) {

        if ( buff.awakensMobs() ){
            if( state != HUNTING && !isScared()) {
                notice();
                state = HUNTING;
            }
            if ( !(buff instanceof Blinded)) {
                enemySeen = true;
                alerted = true;
            }
        }

        if ( buff instanceof Tormented || buff instanceof Banished ) {
            state = FLEEING;
        }

        return super.add(buff);
    }
	
	@Override
	public void remove( Buff buff ) {
		super.remove(buff);
		if (isAlive() && (buff instanceof Tormented || buff instanceof Banished)) {
			sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
			state = HUNTING;
		}
	}

    protected boolean canCastBolt( Char enemy ){
        return !hasBuff(Disrupted.class) && !hasBuff(Dazed.class) && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
    }
	
	protected boolean canAttack( Char enemy ) {
		return this.equals(enemy) || Level.adjacent( pos, enemy.pos ) && !enemy.invulnerable() && ( !isCharmedBy( enemy ) || Bestiary.isBoss( this ) );
	}

    protected int nextStepTo( Char enemy ) {
        return Dungeon.findPath( this, pos, enemy.pos,
                flying ? Level.passable : Level.mob_passable,
                Level.fieldOfView );
    }

	protected boolean getCloser( int target ) {

        if (rooted) {

            return false;

        }

		int step = Dungeon.findPath(this, pos, target,
                flying ? Level.passable : Level.mob_passable,
                Level.fieldOfView);

		if (step != -1) {

            Char ch = Actor.findChar( step );

            if( ch == null ) {

                if( !enemySeen ) {

                    for (int n : Level.NEIGHBOURS9) {
                        ch = Actor.findChar(step + n);
                        if ( ch != null && ch instanceof Hero && detected(ch) ) {
                            beckon(ch.pos);
                            break;
                        }
                    }
                }

//                if( ch == null ) {
                    move(step);
                    return true;
//                }

            } else {
                Invisibility.dispel( ch );
                beckon(step);
            }
//			return true;
//		} else {
//			return false;
		}
        return false;
    }
	
	protected boolean getFurther( int target ) {

        if (rooted) {
            return false;
        }

		int step = Dungeon.flee(this, pos, target,
                flying ? Level.passable : Level.mob_passable,
                Level.fieldOfView);
		if (step != -1) {
            Char ch = Actor.findChar( step );

            if( ch == null ) {

                move(step);
                return true;

            } else {
                Invisibility.dispel(ch);
                beckon(step);
            }
		}
        return false;
    }
	
//	@Override
//	public void move( int step ) {
//
//        if ( !rooted ) {
//            super.move( step );
//
//            Dungeon.level.press( step, this );
//        }
//	}

    @Override
    public boolean isRanged() {
        return enemy != null && Level.distance( pos, enemy.pos ) > 1;
    }
	
	protected boolean doAttack( Char enemy ) {

        final int enemyPos = enemy.pos;

		boolean visible = Dungeon.visible[pos] || Dungeon.visible[enemyPos];

        if( meleeAttackRange()>0 && Level.adjacent( pos, enemyPos ) || Level.distance( pos, enemy.pos ) <= meleeAttackRange() ) {

            if ( visible ) {

                Dungeon.visible[pos] = true;
                sprite.attack( enemyPos );

            } else {

                attack( enemy );

            }

        } else {

            if ( visible ) {

                Dungeon.visible[pos] = true;
                sprite.cast( enemyPos, new Callback() {
                    @Override
                    public void call() { onRangedAttack( enemyPos ); }
                }  );

            } else {

                cast( enemy );

            }
        }

        if( enemy == Dungeon.hero ) {
            noticed = true;
        }
				
		spend( attackDelay() );
		
		return !visible;
	}

    protected int meleeAttackRange() {
        return 1;
    }

    protected void onRangedAttack( int cell ) {

        if ( enemy == Dungeon.hero ) {
            Dungeon.hero.interrupt( "你被突如其来的攻击惊醒了！" );
        }
        sprite.idle();
    }
	
	@Override
	public void onAttackComplete() {
		attack( enemy );

		if (isExposedTo(enemy)){
            final Hero hero = (Hero)enemy;
            final Mob mob = this;

            if (hero.belongings.weap1 instanceof MeleeWeapon) {

                hero.currentWeapon = hero.belongings.weap1;

                ((HeroSprite) hero.sprite).attack(hero.currentWeapon, this.pos, new Callback() {
                    @Override
                    public void call() {
                        int damage = hero.damageRoll();
                        //weapon penalty reduce damage efficiency
                        float damagePenalty = 1 - hero.currentWeapon.currentPenalty(hero, hero.currentWeapon.str()) * 0.025f;
                        damage = (int) (damage * damagePenalty);
                        hero.hitEnemy(mob, damage);
                        mob.next();
                    }
                });
                return;
            }
        }
		super.onAttackComplete();
	}

    @Override
    public void onCastComplete() {
        cast(enemy);
        super.onCastComplete();
    }

    public boolean cast( Char enemy ) {
        return attack(enemy);
    }

    public boolean castBolt( Char enemy , int damageRoll, boolean ignoresArmor, Element type ) {

        boolean penetrateShield=penetrateShield(enemy,type);
        //boolean ignoresShield =ignoresArmor && penetrateShield;

        Guard guarded = enemy.buff( Guard.class );

        if( guarded != null && /*!ignoresShield && */ enemy.hasShield() && enemy.guard( damageRoll, accuracy(), true ) ) {

            guarded.proc(enemy.hasShield());

            enemy.defenseProc(this, damageRoll, true);
            if (penetrateShield) //if blocked, and penetrate still will deal 2/3 of damage
                damageRoll -= damageRoll/3;
            else
                return false;//else will block full damage
        }

        if (hit( this, enemy, true, true )) {
            boolean penetrate=penetrateAC(enemy,type);
            ignoresArmor=ignoresArmor & penetrate;//if cant penetrate is because armor enchantment, so cannot ignore armor

            enemy.defenseProc(this, damageRoll, false);

            if (ignoresArmor)
                enemy.damage( damageRoll, this,type );
            else
                enemy.damage( enemy.absorb(damageRoll,penetrate), this, type );

        } else {
            enemy.missed();
            return false;
        }
        enemy.sprite.flash();

        return true;
    }


	@Override
	public int defenseProc( Char enemy, int damage, boolean blocked ) {

        if (dexterity() == 0 && !invulnerable() && Dungeon.visible[pos] && !stunned && enemy instanceof Hero) {
            Hero hero = (Hero) enemy;

            Weapon weapon = hero.rangedWeapon != null ? hero.rangedWeapon : hero.currentWeapon;
            if (weapon != null) {

                float backstabBonus= weapon.getBackstabMod()+hero.ringBuffsBaseZero(RingOfShadows.Shadows.class);

                float stealthBonus=(enemy.stealth()-1f);
                backstabBonus += stealthBonus/2;
                if (hero.subClass == HeroSubClass.ASSASSIN){
                    backstabBonus += 0.25f;
                }

                if (hero.rangedWeapon != null){
                    //reduced stealth bonus based on accuracy penalty, as throwing weapons no longer reduce stealth
                    if (weapon instanceof ThrowingWeapon)
                        backstabBonus -= hero.rangedWeapon.currentPenalty(hero,hero.rangedWeapon.str())*0.025f;
                    //ranged weapons except light throwing get half backstab bonus
                    if (!(weapon instanceof ThrowingWeaponLight))
                        backstabBonus/=2;
                }
                if (backstabBonus>0) {
                    damage += hero.damageRoll()*backstabBonus;
                    Wound.hit(this);
                }
            }

            if (sprite != null ) {
                sprite.showStatus(CharSprite.DEFAULT, TXT_AMBUSH);
            }

        }

        return super.defenseProc( enemy, damage, blocked );
	}
	
	public void aggro( Char ch ) {
		enemy = ch;
	}

    @Override
    public void damage( int dmg, Object src, Element type ) {

        HealthIndicator.instance.target( this );

        super.damage( dmg, src, type );

        if (isAlive()) {

            if (!isScared()) {
                if (src != null && state == FLEEING || state == WANDERING || state == SLEEPING) {
                    notice();
                    state = HUNTING;
                }
            }
            if (src instanceof Char){

                Char ch=(Char) src;
                //enemies only fight each other if charmed
                if (ch instanceof Hero || ch.isFriendly()) {
                    //only swap enemy if current attacking char is closer than actual enemy
                    if (enemy == null || Level.distance(pos, enemy.pos)> Level.distance(pos, ch.pos)) {
                        enemy = (Char) src;
                    }
                }
            }
            enemySeen = true;
            alerted = true;
        }
	}
	
	@Override
	public void destroy() {
        dropLoot();
		Dungeon.level.mobs.remove(this);

		if (Dungeon.hero.isAlive()) {

			if (hostile) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;
			}

            if ( EXP > 0 && Dungeon.hero.lvl <= maxLvl + Dungeon.hero.lvlBonus ) {
                int exp = EXP;

                /*float bonus = Dungeon.hero.ringBuffs(RingOfKnowledge.Knowledge.class) * exp - exp;
                exp += (int)bonus;
                exp += Random.Float() < bonus % 1 ? 1 : 0 ;*/

                Dungeon.hero.earnExp(exp);
			}
		}

        super.destroy();
	}
	
	@Override
	public void die( Object cause, Element dmg ) {

        if( this != cause ){

            if( Dungeon.visible[ pos ] && cause == Dungeon.hero ){

                Enraged buff = Dungeon.hero.buff( Enraged.class );

                if( buff != null ){
                    //buff.reset( Enraged.DURATION );
                    buff.reset(buff.refreshDuration);
                }
            }

            if( Dungeon.visible[ pos ] && !( this instanceof NPC ) ){
                GLog.i( TXT_DEFEAT, name );
            }
        }
        if (Level.chasm[pos] && !(this instanceof NPC)){
            //mobs killed over chasm will fall instead
            Chasm.mobFall(this);
        }else {
            super.die(cause, dmg);
        }

	}
	
	protected Object loot = null;
	protected float lootChance = 0;
	
	@SuppressWarnings("unchecked")
	protected void dropLoot() {

		if (loot != null && Random.Float() < lootChance * Dungeon.hero.ringBuffs( RingOfFortune.Fortune.class ) ) {

			Item item = null;

			if (loot instanceof Generator.Category) {
				
				item = Generator.random( (Generator.Category)loot );
				
			} else if (loot instanceof Class<?>) {
				
				item = Generator.random( (Class<? extends Item>)loot );
				
			} else {
				
				item = (Item)loot;
				
			}

            if( item instanceof Gold ) {
                item.quantity = Bestiary.isBoss(this) ?
                        Random.IntRange(350, 500) + item.quantity * 4 :
                        Math.max( 1, item.quantity / (6 - Dungeon.chapter() ) );
            }

            if( item instanceof ThrowingWeaponAmmo || item instanceof Explosives ) {
                item.quantity = Math.max( 1, item.quantity / (6 - Dungeon.chapter() ) );
            }

			Dungeon.level.drop( item, pos ).sprite.drop();
		}
	}
	
	public boolean reset() {
		return false;
	}

    public void knockBack(int sourcePos, final int damage, final int amount, final Callback callback) {
	    super.knockBack(sourcePos,damage,amount,callback);
        beckon( sourcePos );
    }

    public void beckon( int cell ) {

        enemySeen = true;
        target = cell;

        if ( state == WANDERING || state == SLEEPING ) {

            notice();
            state = HUNTING;

        }
    }

    public void inspect( int cell ) {

        if ( state == SLEEPING || state == WANDERING ) {

            state = WANDERING;
            if (!Level.chasm[cell] )//prevent suicide behaviour
                target = cell;

        }
    }
	
	public String description() {
		return "Real description is coming soon!";
	}
	
	public void notice() {
        if( sprite != null && sprite.visible && state != PASSIVE) {
            sprite.showAlert();
        }
	}
	
	public void yell( String str ) {

        str = "\"" + str + "\"";

		GLog.i("%s: %s", name, str.replaceAll("\\n", " "));

        for( String s : str.split( "\\n" ) ) {
            sprite.showStatus(CharSprite.DEFAULT, Utils.format("%s", s));
        }
	}
	
	public interface AiState {
		boolean act(boolean enemyInFOV, boolean justAlerted);
		String status();
	}

	private void alertNearby(){
        if (enemy instanceof Hero && ((Hero)enemy).subClass == HeroSubClass.ASSASSIN)
            return;

        for ( Mob mob : Dungeon.level.mobs ) {
            if ( mob != Mob.this && !mob.enemySeen && ( Level.distance( pos, mob.pos ) <= 2 || Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE ) ) ) {
                mob.beckon( target );
            }
        }
    }

    public void looseTrack(){
        enemySeen=false;
    }

    protected class Sleeping implements AiState {

		public static final String TAG	= "SLEEPING";
		
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            enemySeen = enemyInFOV && detected( enemy );

			if ( enemySeen ) {

                notice();
				state = HUNTING;
				target = enemy.pos;

                alertNearby();
				
				spend( TIME_TO_WAKE_UP );
				
			} else {

				spend( TICK );
				
			}
			return true;
		}
		
		@Override
		public String status() {
            return "睡觉";
        }
	}

    protected class Wandering implements AiState {
		
		public static final String TAG	= "WANDERING";
		
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            enemySeen = enemyInFOV && detected(enemy);

			if ( enemySeen ) {

                notice();
				state = HUNTING;
				target = enemy.pos;

                alertNearby();

                spend( TICK );
				
			} else {
				int oldPos = pos;
                if (target != -1){
                    if(Random.Int(4)==0) {
                        //spend a turn doing nothing
                        spend(TICK / moveSpeed());
                        return true;
                    }else if( getCloser( target )) {
                        spend( TICK / moveSpeed() );
                        return moveSprite( oldPos, pos );
                    }
				}
                target = Dungeon.level.randomDestination();
                spend( TICK / moveSpeed() );
			}
			return true;
		}
		
		@Override
		public String status() {
            return "游荡";
        }
	}

    protected class Hunting implements AiState {
		
		public static final String TAG	= "HUNTING";
		
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			enemySeen = enemyInFOV;

            if( justAlerted ) {
                alertNearby();
            }

            if (enemySeen && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemySeen) {
                    target = enemy.pos;
                }

                int oldPos = pos;

                if (target != -1 && getCloser( target )) {

                    spend( TICK / moveSpeed() );
                    return moveSprite( oldPos,  pos );

                } else {

                    if( enemy != null && enemy.invisible <= 0 ) {

//                        target = enemy.pos;
                        target = nextStepTo( enemy );

                        if (!enemySeen && !detected(enemy) ) {
                            state = WANDERING;
                        }

                    } else {

                        target = Dungeon.level.randomDestination();
                        state = WANDERING;

                    }

                    spend( TICK / moveSpeed() );

                    return true;
                }
            }
		}
		
		@Override
		public String status() {
            return enemySeen ? "进行攻击" : "寻找猎物";
        }
	}
	
	protected class Fleeing implements AiState {
		
		public static final String TAG	= "FLEEING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            enemySeen = enemyInFOV;
            if (enemySeen) {
				target = enemy.pos;
			}
            int oldPos = pos;
            if (target != -1 && getFurther( target )) {
                if (!enemySeen && enemy ==null || 1 + Random.Int(Dungeon.level.distance(pos, target)) >= 5 && !detected(enemy) ) {
                    target = Dungeon.level.randomDestination();
                    state = WANDERING;
                }
                spend( TICK / moveSpeed() );
                return moveSprite( oldPos,  pos );

            } else {

				spend( TICK );
				nowhereToRun();

				return true;
			}

		}
		
		protected void nowhereToRun() {
            if (buff( Tormented.class ) == null) {
				state = HUNTING;
			}
		}
		
		@Override
		public String status() {
            return "逃跑";
        }
	}
	
	protected class Passive implements AiState {
		
		public static final String TAG	= "PASSIVE";
		
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = false;
			spend( TICK );
			return true;
		}
		
		@Override
		public String status(){
            return "备战";
//			return "passive";
        }
	}
}
