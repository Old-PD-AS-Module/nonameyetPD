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
  import com.ravenwolf.nnypd.actors.buffs.Buff;
  import com.ravenwolf.nnypd.actors.buffs.BuffActive;
  import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
  import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
  import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
  import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
  import com.ravenwolf.nnypd.actors.buffs.debuffs.Stun;
  import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
  import com.ravenwolf.nnypd.actors.buffs.special.UnholyArmor;
  import com.ravenwolf.nnypd.items.keys.SkeletonKey;
  import com.ravenwolf.nnypd.items.misc.Gold;
  import com.ravenwolf.nnypd.levels.CityBossLevel;
  import com.ravenwolf.nnypd.levels.Level;
  import com.ravenwolf.nnypd.misc.utils.GLog;
  import com.ravenwolf.nnypd.scenes.GameScene;
  import com.ravenwolf.nnypd.visuals.Assets;
  import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
  import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
  import com.ravenwolf.nnypd.visuals.effects.Flare;
  import com.ravenwolf.nnypd.visuals.effects.LifeDrain;
  import com.ravenwolf.nnypd.visuals.effects.Speck;
  import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
  import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
  import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
  import com.ravenwolf.nnypd.visuals.sprites.KingSprite;
  import com.ravenwolf.nnypd.visuals.sprites.UndeadSprite;
  import com.watabou.noosa.Camera;
  import com.watabou.noosa.audio.Sample;
  import com.watabou.noosa.tweeners.AlphaTweener;
  import com.watabou.utils.Bundle;
  import com.watabou.utils.Random;

  import java.util.ArrayList;
  import java.util.HashMap;

  public class DwarvenKing extends MobPrecise {

    private final static int LINE_GREETINGS = 0;
    private final static int LINE_AWAKENING = 1;
    private final static int LINE_THREATENED = 2;
    private final static int LINE_CHANNELING = 3;
    private final static int LINE_ANNOYANCE = 4;
    private final static int LINE_EMPOWERED = 5;
    private final static int LINE_MOVEASIDE = 6;
    private final static int LINE_NEARDEATH = 7;

      private final static String TXT_ENRAGED = "矮人国王被激怒了！";
      private final static String TXT_STOPPED = "矮人国王停止了仪式。";
      private final static String TXT_CALMDWN = "矮人国王不再狂暴。";
      private final static String TXT_SUMMONS = "矮人国王施展了一些强大的咒术！";
      private final static String TXT_CHANNEL = "矮人国王开始了某种仪式...";

      private static String[][] LINES = {

              {
                      "大胆！",
                      "是谁竟敢扰我清梦？",
                      "人类？正在我的王座之间？",
              },
              {
                      "苏醒吧，我的仆人们！",
                      "我命令你们前去战斗！",
                      "仆人们！盛宴之时已至！",
              },
              {
                      "区区蝼蚁...你会为此付出代价的！",
                      "你的死法将会非 常 痛 苦...",
                      "是时候教训你了，凡人。",
                      "你将会成为我强大的奴仆。",
              },
              {
                      "来吧，我的仆从们！",
                      "见识一下我真正的力量！",
                      "向我而来，我的臣民们！",
                      "你的国王在此命令你！",
                      "从坟墓里站起来吧！",
              },
              {
                      "这些死人就像它们活着的时候那样没用。",
                      "这些奉承者都是些一文不值的废物。",
                      "到头来还得我亲自动手。",
              },
              {
                      "是时候把你从棋盘上抹去了，凡人。",
                      "游戏到此结束了，凡人。",
                      "你的死期到了！去死吧！",
                      "你的惨叫声将会成为我耳中的交响乐。",
                      "你要为你所做的一切付出代价，人类。",
              },
              {
                      "滚开，虫子！",
                      "别惹我生气，小不点。",
                      "你这样可没法阻止我。",
              },
              {
                      "你杀不死我，" + Dungeon.hero.heroClass.title() + "...我是...不朽的... ",
                      "我会回来的, " + Dungeon.hero.heroClass.title() + "...我会...回来...",
                      "哦，不！这怎么可能？！我竟被...一介凡人...杀掉...",
              },
      };


    private static final float SPAWN_DELAY	= 1f;
    private static final float BASE_ENRAGE	= 5f;


    public DwarvenKing() {

        super( 5, 25, true );


        name = Dungeon.depth == Statistics.deepestFloor ? "矮人国王" : "不灭的矮人国王";
        spriteClass = KingSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
    }

    public int breaks = 0;
    public boolean phase = false;

    private static final String PHASE	= "phase";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
        breaks = bundle.getInt( BREAKS );
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }

    @Override
    public boolean immovable(){
          return phase || buff( UnholyArmor.class ) != null;
      }

      @Override
      public boolean add( Buff buff ) {
          if (phase && (buff instanceof Ensnared || buff instanceof Stun || buff instanceof Frozen))
              return false;
          else
              return super.add(buff);
      }

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public int dexterity() {
        return buff( UnholyArmor.class ) == null ? super.dexterity() : 0 ;
    }

    @Override
    public int armourAC() {
        return buff( UnholyArmor.class ) == null ? super.armourAC() : 0;
    }

    @Override
    protected boolean getCloser( int target ) {
        return phase ?
                super.getCloser( CityBossLevel.pedestal() ) :
                super.getCloser( target );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) && ( !phase || ( buff( Enraged.class ) != null ) );
    }

    @Override
    public boolean act() {

        int throne = Dungeon.level instanceof CityBossLevel ? CityBossLevel.pedestal() : 0;


        if( Dungeon.hero.isAlive() ) {

            if (buff(UnholyArmor.class) != null) {

                sprite.cast(throne, null);
                spend(TICK);
                return true;

            } else if (throne > 0 && phase) {

               /* if (Level.adjacent(pos, throne) && findChar(throne) != null) {

                    yell( LINE_MOVEASIDE );
                    GLog.w( TXT_ENRAGED );

                    BuffActive.add( this, Enraged.class, TICK );
                    enemy = findChar(throne);

//                    return super.act();

                } else*/ if (pos == throne) {

                    sprite.cast(throne, null);
                    sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
                    Sample.INSTANCE.play(Assets.SND_CHALLENGE);

                    //sacrificeAllMinions();

                    awakenWells();

                    if (breaks > 0) {

                        Buff.affect(this, UnholyArmor.class, armorDuration( breaks ) );
                        yell( LINE_CHANNELING );
                        GLog.w( TXT_CHANNEL );

                        Debuff.removeAll( this );

                    } else {

                        yell( LINE_AWAKENING );
                        GLog.w( TXT_SUMMONS );
                        phase = false;

                    }

                    Buff.detach(this, Enraged.class);
                    spend(TICK);

                    return true;
                }

            } else if (!phase && 3 - breaks > 4 * HP / HT) {


                breaks++;
                phase = true;

                Buff.detach(this, Enraged.class);
                yell( LINE_THREATENED );

                if (pos != throne) {
                    Char enemy = Actor.findChar(throne);
                    if (enemy != null && enemy != this) {
                        enemy.knockBack(pos, damageRoll(), 1);
                    }
                    if (Actor.findChar(throne) != null){
                        //dont change position if still occupied
                        spend(TICK);
                        return true;
                    }

                    CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 2);
                    ((KingSprite) sprite).blink(pos, throne);
                    move(throne);
                    CellEmitter.get(throne).burst(ElmoParticle.FACTORY, 2);
                }
                spend(TICK);
                return true;

            } else {

                phase = false;
                return super.act();

            }
        }

        return super.act();
    }

    @Override
    public boolean cast( Char enemy ) {
        if (hasBuff(UnholyArmor.class)) {

            Blob blob = Dungeon.level.blobs.get(Spawner.class);

            if (blob != null) {
                for (int well : CityBossLevel.wells()) {
                    if (blob.cur[well] > 1) {
                        GameScene.add(Blob.seed(well, -1, Spawner.class));
                    }
                }
            }

            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof Undead) {
                    mob.target = pos;
                    BuffActive.add(mob, Enraged.class, TICK * 2);
                }
            }
        }

        return true;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if( buff( Enraged.class ) != null ) {
            dmg = dmg / 2 + Random.Int( 1 + dmg % 2 );
        }

        super.damage(dmg, src, type);
    }

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> result=new HashMap<>();;
        result.putAll( super.resistances());

        if( buff( UnholyArmor.class ) != null ){
            for( Class<? extends Element> type : UnholyArmor.RESISTS ) {
                result.put( type, Element.Resist.IMMUNE );
            }
        }else if (phase){
            for( Class<? extends Element> type : UnholyArmor.RESISTS ) {
                result.put( type, Element.Resist.PARTIAL );
            }
        }

        return result;
    }


    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( enemy != null && buff( Enraged.class ) != null ) {
            Camera.main.shake(1, 0.1f);
            enemy.knockBack(this, damage);
        }

        return damage;
    }

    @Override
    public void remove( Buff buff ) {
        if( buff instanceof UnholyArmor ) {
            phase = false;

            int duration = ((UnholyArmor) buff).consumed();

            if( duration > 0 ) {
                BuffActive.add( this, Enraged.class, BASE_ENRAGE + TICK * duration );
                yell( LINE_EMPOWERED );
                GLog.w( TXT_ENRAGED );
            } else {
                new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );
                yell( LINE_ANNOYANCE );
                GLog.w( TXT_STOPPED );
            }
            spend( TICK );
        } else if( buff instanceof Enraged ) {
            GLog.w( TXT_CALMDWN );
        }

        super.remove( buff );
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

        yell( LINE_NEARDEATH );
        Camera.main.shake(3, 0.5f);
        new Flare( 6, 48 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 3f );

        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead) {
                mob.die( cause, null );
            }
        }

        Blob blob = Dungeon.level.blobs.get( Spawner.class );

        if (blob != null) {
            blob.remove();
        }

        Badges.validateBossSlain();
        GameScene.bossSlain();

        super.die( cause, dmg );

	}
	
	@Override
	public void notice() {
		super.notice();

        if( enemySeen && HP == HT && breaks == 0 ) {
            yell( LINE_GREETINGS );
        }
	}
	
	@Override
	public String description() {
        return
                "这位最后的矮人国王以对生死机制的透彻理解而闻名。他说服王国的议会成员参加了一场本应让他们所有人获得永生的仪式。最终成功的仅他一人——以及一支死灵军队。";
    }


	public static class Undead extends MobPrecise {

		public DwarvenKing king = null;
		public int well = 0;

        public Undead() {

            super( 5, 0, false );

            name = "矮人亡灵";
			spriteClass = UndeadSprite.class;


            minDamage /= 2;
            maxDamage /= 2;

            armorClass /= 2;

            resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);
            resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

		}

        public boolean isMagical() {
            return true;
        }

        private static final String WELL	= "well";

        @Override
        public int STR() {
            return super.STR()-3;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( WELL, well );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            well = bundle.getInt( WELL );
        }

        @Override
        public int dexterity() {
            return buff( Enraged.class ) == null ? super.dexterity() : 0 ;
        }

        @Override
        public int armourAC() {
            return buff( Enraged.class ) == null ? super.armourAC() : 0;
        }

        @Override
        public boolean act() {

            if( king == null ) {
                king = king();
//            } else if (!enemySeen) {
//                beckon(king.pos);
            }

            if( buff( Enraged.class ) != null && king != null && king.isAlive() && Level.adjacent( pos, king.pos ) ) {

                int healthRestored = Math.min( Random.IntRange( HP / 3, HP / 2 ), king.HT - king.HP );

                king.HP += healthRestored;

                if (king.sprite.visible) {
                    sprite.parent.add( new LifeDrain( pos, king.pos, null ) );
                    king.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    king.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));
                }

                UnholyArmor buff = king.buff( UnholyArmor.class );

                if( buff != null ) {

                    buff.consumed( 2/*king.breaks*/ );
                    sprite.showStatus(CharSprite.NEGATIVE, "献身");
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );

                }

                die( this );

                return true;
            }
            return super.act();
        }


        @Override
        protected boolean getCloser( int target ) {
            return buff( Enraged.class ) != null && king != null && king.isAlive() ?
                    super.getCloser( king.pos ) :
                    super.getCloser( target );
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return super.canAttack( enemy ) && buff( Enraged.class ) == null;
        }

		@Override
		public int attackProc( Char enemy, int damage, boolean blocked ) {

            if( Random.Int( 12 ) < 5 ) {
                BuffActive.addFromDamage( enemy, Withered.class, damage );
            }

			return damage;
		}

		
		@Override
		public void die( Object cause, Element dmg ) {
            if( well > 0 && king != null) {
                GameScene.add( Blob.seed( well, spawnDelay( king.breaks ), Spawner.class ) );
            }

			if (Dungeon.visible[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
                sprite.emitter().burst( Speck.factory( Speck.BONE ), 6 );
			}

            super.die( cause, dmg );
		}
		
		@Override
		public String description() {
            return
                    "这些矮人的亡魂，是按照矮人国王的意愿复活的，曾经的王国议会成员。它们的骨骼粗壮结实，但是有着异常微小的头骨";
        }
	}

    public static class Spawner extends Blob {

        public Spawner() {
            super();

            name = "骸骨堆";
        }

        @Override
        protected void evolve() {

            int from = WIDTH + 1;
            int to = Level.LENGTH - WIDTH - 1;

            for (int pos=from; pos < to; pos++) {

                if (cur[pos] > 0) {

                    cur[pos]--;

                    if( cur[ pos ] <= 0 ) {

                        if( !spawnSkeleton( pos ) ) {
                            cur[ pos ] = 1;
                        }
                    }
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
        public String tileDesc() {
            return "这里的骸骨仍然在活动，让人感觉不寒而栗。";
        }
    }

    private void sacrificeAllMinions() {
        int healthRestored = 0;

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead ) {

                healthRestored += Random.IntRange( mob.HP / 3, mob.HP / 2 );

                if (sprite.visible || mob.sprite.visible) {
                    sprite.parent.add( new LifeDrain( mob.pos, pos, null ) );
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( mob.sprite, 2f );
                }

                mob.die( this );
            }
        }

        healthRestored = Math.min( healthRestored, HT - HP );

        if( healthRestored > 0 ) {

            HP += healthRestored;

            if (sprite.visible) {
                sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(healthRestored));
                sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));
            }
        }
    }

    private void awakenWells() {
        int dormant = 3 - breaks;
        ArrayList<Integer> wells = new ArrayList<>();
        Blob blob = Dungeon.level.blobs.get(Spawner.class);

        for (int well : CityBossLevel.wells()) {
            if (blob == null || blob.cur[well] <= 0) {
                wells.add(well);
            } else if (blob.cur[well] > 0) {
                GameScene.add(Blob.seed(well, spawnDelay(breaks), Spawner.class));
            }
        }

        while (wells.size() > dormant) {

            Integer w = Random.element(wells);

            GameScene.add(Blob.seed(w, spawnDelay(breaks), Spawner.class));
            wells.remove(w);

        }
    }
/*
    private int knockBack( Char enemy, int damage ) {

        int newPos = enemy.pos + enemy.pos - pos;

        int bonus = damageRoll() / 2;

        if ( Level.solid[newPos] ) {

            damage += bonus;
            BuffActive.addFromDamage( enemy, Dazed.class, damage );

        } else {

            Char ch = Actor.findChar(newPos);

            if( ch != null ) {

                BuffActive.addFromDamage( enemy, Dazed.class, damage );
                ch.damage( Char.absorb( bonus, ch.minAC(), ch.armorClass() ), this, Element.PHYSICAL );
//                enemy.damage( Char.absorb( bonus, enemy.armorClass() ), this, Element.PHYSICAL );


            }

            if( ch == null || !ch.isAlive() ) {

                Actor.addDelayed(new Pushing(enemy, enemy.pos, newPos), -1);

                Actor.freeCell(enemy.pos);
                enemy.pos = newPos;
                Actor.occupyCell(enemy);

                Dungeon.level.press(newPos, enemy);

            }
        }

        return damage;
    }
*/
    private static boolean spawnSkeleton( int pos ) {

        int spawn_pos=pos;
        if( Actor.findChar(pos) != null ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            if (candidates.size() > 0) {
                spawn_pos = candidates.get( Random.Int( candidates.size() ) );
            } else {
                return false;
            }
        }

        Undead clone = new Undead();

        clone.well = pos;
        clone.pos = spawn_pos;

        clone.king = king();
        clone.state = clone.HUNTING;

        if( clone.king != null )
            clone.HT = clone.HP = 15 + clone.king.breaks * 5;

        Dungeon.level.press(clone.pos, clone);
        Sample.INSTANCE.play(Assets.SND_CURSED);

        GameScene.add(clone, SPAWN_DELAY);

        if (Dungeon.visible[clone.pos]) {

            clone.sprite.alpha(0);
            clone.sprite.parent.add(new AlphaTweener(clone.sprite, 1, 0.5f));
            clone.sprite.emitter().start(Speck.factory(Speck.RAISE_DEAD), 0.01f, 15);

        }

        clone.sprite.idle();

        return true;
    }

    private static DwarvenKing king() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof DwarvenKing) {
                return (DwarvenKing)mob;
            }
        }

        return null;
    }

    private static int spawnDelay( int breaks ) {
        return Random.IntRange( 8  , 12 ) + amountOfSkeletons();
    }

    private static int amountOfSkeletons(){
        int count = 0;
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Undead) {
                count++;
            }
        }
        return count;
    }

    private static float armorDuration( int breaks ) {
        return 8 + breaks * 4 + Random.Int( 6 );
    }

    private void yell( int line ) {
        yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
    }
}
