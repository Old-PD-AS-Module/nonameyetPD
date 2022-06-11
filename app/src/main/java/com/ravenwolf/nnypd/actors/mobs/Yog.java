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
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.CorrosiveGas;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.BuffPassive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.FlameParticle;
import com.ravenwolf.nnypd.visuals.sprites.BurningFistSprite;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.RottingFistSprite;
import com.ravenwolf.nnypd.visuals.sprites.YogSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class Yog extends Mob {
	
	{
        name = Dungeon.depth == Statistics.deepestFloor ? "Yog-Dzewa" : "震慑之Yog-Dzewa";
		spriteClass = YogSprite.class;
		
		HP = HT = 500;

        armorClass = 20;
        dexterity = 1; //prevent backstabs
		
		EXP = 50;
        loot = Gold.class;
        lootChance = 4f;
		
		state = PASSIVE;

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
	}

    private final static int FIST_RESPAWN_MIN = 26;
    private final static int FIST_RESPAWN_MAX = 34;

    private static final String TXT_DESC =
            "Yog-Dzewa是一位上古之神，来自混沌位面的强大存在。一个世纪前，古代矮人在同恶魔大军的战争中惨胜，却无法杀死这个强大的怪物。只能将它存在封印在都城之下的厅堂中，希望这位力量被削弱的怪物永远无法重见天日。";

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

    @Override
    public boolean immovable(){
        return true;
    }
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();
		
		do {
			fist1.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add(fist2);
	}
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

        int decreaseValue = 1;

        //living fists decrease damage taken
		if (Dungeon.level.mobs.size() > 0) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof BurningFist || mob instanceof RottingFist) {
                    if( src instanceof Char ) {
                        mob.beckon( ((Char)src).pos );
                    }
                    decreaseValue += 2;
				}
			}
		}

        dmg /= decreaseValue;

        //if fists are dead, damage received reduce respawn cooldown
        RespawnBurning respBurn=buff( RespawnBurning.class );
        if (respBurn!=null)
            Buff.shorten(this,RespawnBurning.class,Math.min(respBurn.duration(),dmg/10));
        RespawnRotting resRott=buff( RespawnRotting.class );
        if (resRott!=null)
            Buff.shorten(this,RespawnRotting.class,Math.min(resRott.duration(),dmg/10));

		super.damage(dmg, src, type);
	}

    protected void throwItem() {
        Heap heap = Dungeon.level.heaps.get( pos );
        if (heap != null) {
            int n;
            do {
                n = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Level.passable[n] && !Level.avoid[n]);
            Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
        }
    }

    @Override
    protected boolean act() {
        throwItem();

        state = PASSIVE;

        if ( HP < HT  ) {
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            HP++;
        }

        return super.act();
    }
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause, Element dmg ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause, null );
			}
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		super.die( cause, dmg );
		
		yell( "..." );
	}
	
//	@Override
//	public void notice() {
//		super.notice();
//
//        if( enemySeen ) {
//            yell( "Greetings, mortal. Are you ready to die?" );
//        }
//	}
	
	@Override
	public String description() {
		return TXT_DESC;
			
	}

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();

        if( buff( RespawnBurning.class ) == null ){
            resistances.put( Element.Flame.class, Element.Resist.IMMUNE );
        }
        if( buff( RespawnRotting.class ) == null ){
            resistances.put( Element.Acid.class, Element.Resist.IMMUNE );
        }
        return resistances;
    }
	
	public static class RottingFist extends MobHealthy {

        public RottingFist() {

            super( 5, 25, true );
            name = "腐烂之拳";
			spriteClass = RottingFistSprite.class;

			EXP = 0;
			
			state = WANDERING;

            resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
            resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Body.class, Element.Resist.PARTIAL );
		}

        @Override
        public void die( Object cause, Element dmg ) {
            super.die( cause, dmg );

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if ( mob instanceof Yog && mob.isAlive() ) {
                    Buff.affect( mob, RespawnRotting.class, Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );
                }
            }
        }

        @Override
        protected float healthValueModifier() { return 0.25f; }

		@Override
		public int attackProc( Char enemy, int damage, boolean blocked ) {

            BuffActive.addFromDamage( enemy, Corrosion.class, damage );

			return damage;
		}

        @Override
        public void damage( int dmg, Object src, Element type ) {

            if ( type == Element.ACID ) {

                if (HP < HT) {
                    int reg = Math.min( dmg / 2, HT - HP );

                    if (reg > 0) {
                        HP += reg;
                        sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                        sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                    }
                }

            } else {

                super.damage(dmg, src, type);

            }
        }

        @Override
        public boolean add( Buff buff ) {

            return !(buff instanceof Corrosion ) && super.add( buff );

        }

        @Override
        public int defenseProc( Char enemy, int damage,  boolean blocked ) {

            GameScene.add( Blob.seed( pos, 25, CorrosiveGas.class ) );

            return super.defenseProc(enemy,damage,blocked);
        }
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
	}
	
	public static class BurningFist extends MobRanged {

        public BurningFist() {

            super( 5, 25, true );
            name = "燃烧之拳";
			spriteClass = BurningFistSprite.class;
			
			EXP = 0;
			
			state = WANDERING;

            resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
            resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

            resistances.put( Element.Flame.class, Element.Resist.IMMUNE );
            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Body.class, Element.Resist.PARTIAL );
		}
		
		@Override
		public void die( Object cause, Element dmg ) {
			super.die( cause, dmg );

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if ( mob instanceof Yog && mob.isAlive() ) {
                    Buff.affect( mob, RespawnBurning.class, Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );
                }
            }
		}

        @Override
        protected float healthValueModifier() { return 0.25f; }

        @Override
        public boolean act() {

            GameScene.add( Blob.seed( pos, 2, Fire.class ) );

            return super.act();
        }
		
		@Override
		protected boolean canAttack( Char enemy ) {
			return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
		}

        @Override
        protected void onRangedAttack( int cell ) {

            MagicMissile.fire(sprite.parent, pos, cell,
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
            return castBolt(enemy,damageRoll(),false,Element.FLAME);
        }
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}

	}

    public static class RespawnBurning extends BuffPassive {

        private boolean warned = false;

        private static final String WARNED	= "warned";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( WARNED, warned );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            warned = bundle.getBoolean( WARNED );
        }

        @Override
        public void detach() {

            if( target.isAlive() ) {

                if( warned ) {

                    BurningFist fist = new BurningFist();

                    ArrayList<Integer> candidates = new ArrayList<Integer>();

                    for (int n : Level.NEIGHBOURS8) {
                        int cell = target.pos + n;
                        if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                            candidates.add(cell);
                        }
                    }

                    if (candidates.size() > 0) {
                        fist.pos = candidates.get(Random.Int(candidates.size()));
                    } else {
                        fist.pos = Dungeon.level.randomRespawnCell();
                    }

                    GameScene.add(fist);

                    fist.sprite.alpha(0);
                    fist.sprite.parent.add(new AlphaTweener(fist.sprite, 1, 0.5f));
                    fist.sprite.emitter().burst(FlameParticle.FACTORY, 15);

                    GLog.w("燃烧之拳复活了！");
                    super.detach();

                } else {
                    warned=true;
                    GLog.w( "燃烧之拳很快就要复活！" );
                    spend( Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );

                }
            }else
                super.detach();
        }
    }

    public static class RespawnRotting extends BuffPassive {

        private boolean warned = false;

        private static final String WARNED	= "warned";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( WARNED, warned );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            warned = bundle.getBoolean( WARNED );
        }

        @Override
        public void detach() {

            if( target.isAlive() ) {

                if (warned) {

                    RottingFist fist = new RottingFist();

                    ArrayList<Integer> candidates = new ArrayList<Integer>();

                    for (int n : Level.NEIGHBOURS8) {
                        int cell = target.pos + n;
                        if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                            candidates.add(cell);
                        }
                    }

                    if (candidates.size() > 0) {
                        fist.pos = candidates.get(Random.Int(candidates.size()));
                    } else {
                        fist.pos = Dungeon.level.randomRespawnCell();
                    }

                    GameScene.add(fist);

                    fist.sprite.alpha(0);
                    fist.sprite.parent.add(new AlphaTweener(fist.sprite, 1, 0.5f));
                    fist.sprite.emitter().burst(Speck.factory(Speck.TOXIC), 15);


                    GLog.w("腐烂之拳复活了！");
                    super.detach();

                } else {
                    warned=true;
                    GLog.w("腐烂之拳很快就要复活！");
                    spend(Random.IntRange(FIST_RESPAWN_MIN, FIST_RESPAWN_MAX));

                }
            }else
                super.detach();
        }
    }
}