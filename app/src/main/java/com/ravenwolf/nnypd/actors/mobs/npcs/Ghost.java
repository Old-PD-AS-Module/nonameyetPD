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
package com.ravenwolf.nnypd.actors.mobs.npcs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.BodyArmorCloth;
import com.ravenwolf.nnypd.items.quest.DriedRose;
import com.ravenwolf.nnypd.items.quest.RatSkull;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.levels.SewerLevel;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.GhostSprite;
import com.ravenwolf.nnypd.visuals.windows.WndQuest;
import com.ravenwolf.nnypd.visuals.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost extends NPC {

	{
		name = "悲伤幽灵";
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}

	private static final String TXT_ROSE1	=
			"你好，冒险者…曾经我像你一样——既强大又自信...然后死在了这里...可还我不能离开这个地方...直到我找到自己的_干枯玫瑰_" +
					"它对我来说非常重要...应该是被一些怪物夺走了...";

	private static final String TXT_ROSE2	=
			"请...帮我...寻找我的玫瑰...";

	private static final String TXT_RAT1	=
			"你好，冒险者...曾经我像你一样——既强大又自信...然后死在了这里...可还我不能离开这个地方...直到我完成我的复仇…杀死那个_腐臭老鼠_，就是它夺走了我的生命...";

	private static final String TXT_RAT2	=
			"请...帮我...杀了那个可憎的怪物...";

	
	public Ghost() {
		super();

		Sample.INSTANCE.load(Assets.SND_GHOST);
	}

	@Override
	public boolean invulnerable(){
		return true;
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero != null && Dungeon.hero.belongings.getItem( DriedRose.class ) != null){
			target = Dungeon.hero.pos;
		}
		return super.act();
	}

    private void flee() {
        int newPos = -1;
        for (int i=0; i < 10; i++) {
            newPos = Dungeon.level.randomRespawnCell();
            if (newPos != -1) {
                break;
            }
        }
        if (newPos != -1) {

            Actor.freeCell( pos );

            CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
            pos = newPos;
            sprite.place( pos );
            sprite.visible = Dungeon.visible[pos];
        }
    }
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 0;
//	}
	
//	@Override
//	public String defenseVerb() {
//		return "evaded";
//	}


    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean immovable() {
        return true;
    }
	
	@Override
	public float moveSpeed() {
		return 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
        flee();
    }
	
	@Override
	public boolean add( Buff buff ) {
        flee();
        return false;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );//生成玫瑰
		
		if (Quest.given) {
			
			Item item = Quest.alternative ?
				Dungeon.hero.belongings.getItem( RatSkull.class ) :
				Dungeon.hero.belongings.getItem( DriedRose.class );	
			if (item != null) {
				GameScene.show( new WndSadGhost( this, item ) );
			} else {
				GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT2 : TXT_ROSE2 ) );

                flee();
			}
			
		} else {
			GameScene.show( new WndQuest( this, Quest.alternative ? TXT_RAT1 : TXT_ROSE1 ) );
			Quest.given = true;
			
			Journal.add( Journal.Feature.GHOST );
		}
	}


	
	@Override
	public String description() {
		return
				"这个幽灵几乎没有固定的形体。它看起来像是由一片无形的昏暗光斑和一张悲痛的面孔所组成的。";
	}
	
//	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
//	static {
//		IMMUNITIES.add( Stun.class );
//		IMMUNITIES.add( Ensnared.class );
//	}
//
//	@Override
//	public HashSet<Class<?>> immunities() {
//		return IMMUNITIES;
//	}
	
	public static class Quest {

		private static boolean spawned;

		private static boolean alternative;

		private static boolean given;

		private static boolean processed;

		private static boolean completed;

		private static int depth;
		
		private static int left2kill;
		
		public static Weapon weapon;
		public static Armour armor;
		
		public static void reset() {
			spawned = false;
            completed = false;
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
        private static final String LEFT2KILL	= "left2kill";
        private static final String GIVEN		= "given";
        private static final String PROCESSED	= "processed";
        private static final String COMPLETED	= "completed";
        private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( ALTERNATIVE, alternative );
				if (!alternative) {
					node.put( LEFT2KILL, left2kill );
				}
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
                node.put( PROCESSED, processed );
                node.put( COMPLETED, completed );

				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	=  node.getBoolean( ALTERNATIVE );
				if (!alternative) {
					left2kill = node.getInt( LEFT2KILL );
				}
				
				given	= node.getBoolean( GIVEN );
				depth	= node.getInt( DEPTH );
                processed	= node.getBoolean( PROCESSED );
                completed	= node.getBoolean( COMPLETED );
                given	= node.getBoolean( GIVEN );

				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armour)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 1 && Random.Int( 6 - Dungeon.depth ) == 0 ) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1 );
				level.mobs.add( ghost );
				Actor.occupyCell( ghost );
				
				spawned = true;
				alternative = false;
				if (!alternative) {
					left2kill = 8;
				}
				
				completed = false;
				given = false;
				processed = false;
				depth = Dungeon.depth;

				do {
					weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
				} while (weapon instanceof ThrowingWeapon || weapon.lootChapter()+weapon.bonus < 2 || weapon.lootChapter()+weapon.bonus > 4/* || weapon.isCursed()*/);

				do {
					armor = (Armour)Generator.random( Generator.Category.ARMOR );
				} while (armor instanceof BodyArmorCloth || armor.lootChapter()+armor.bonus < 2 || armor.lootChapter()+armor.bonus > 4/* || armor.isCursed()*/ );

				weapon.identify();
				weapon.cursed=false;
				armor.identify();
				armor.cursed=false;

			}
		}

		public static void process( int pos ) {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				if (alternative) {
					
//					FetidRat rat = new FetidRat();
//					rat.pos = Dungeon.level.randomRespawnCell();
//					if (rat.pos != -1) {
//						GameScene.add( rat );
						processed = true;
//					}
//
				} else {
					
					if (Random.Int( left2kill ) == 0) {
						Dungeon.level.drop( new DriedRose(), pos ).sprite.drop();
						processed = true;
					} else {
						left2kill--;
					}
					
				}
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;

            completed = true;
			
			Journal.remove( Journal.Feature.GHOST );
		}

        public static boolean isCompleted() {
            return completed;
        }
	}
	
//	public static class FetidRat extends Mob {
//
//		{
//			name = "fetid rat";
//			spriteClass = FetidRatSprite.class;
//
//			HP = HT = 14;
//			dexterity = 6;
//
//			EXP = 0;
//			maxLvl = 5;
//
//			state = WANDERING;
//
//            HP = HT = Random.NormalIntRange( HT, HT * 2 );
//		}
//
//		@Override
//		public int damageRoll() {
//			return Random.NormalIntRange( 0, 12 );
//		}
//
//		@Override
//		public int accuracy() {
//			return 16;
//		}
//
//		@Override
//		public int armorClass() {
//			return 2;
//		}
//
//		@Override
//		public int defenseProc( Char enemy, int damage ) {
//
//			GameScene.add( Blob.seed( pos, 50, CorrosiveGas.class ) );
//
//			return super.defenseProc(enemy, damage);
//		}
//
//		@Override
//		public void die( Object cause ) {
//			super.die( cause );
//
//			Dungeon.level.drop( new RatSkull(), pos ).sprite.drop();
//		}
//
//		@Override
//		public String description() {
//			return
//				"This marsupial rat is much larger, than a regular one. It is surrounded by a foul cloud.";
//		}
//
//		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
//		static {
//			IMMUNITIES.add( CorrosiveGas.class );
//		}
//
//		@Override
//		public HashSet<Class<?>> immunities() {
//			return IMMUNITIES;
//		}
//	}
}
