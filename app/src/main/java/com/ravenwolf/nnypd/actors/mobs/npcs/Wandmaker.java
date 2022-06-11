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
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.quest.CorpseDust;
import com.ravenwolf.nnypd.items.wands.CharmOfWarden;
import com.ravenwolf.nnypd.items.wands.CharmOfBlastWave;
import com.ravenwolf.nnypd.items.wands.CharmOfShadows;
import com.ravenwolf.nnypd.items.wands.CharmOfThorns;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.wands.WandOfAvalanche;
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.items.wands.CharmOfDomination;
import com.ravenwolf.nnypd.items.wands.WandOfDisintegration;
import com.ravenwolf.nnypd.items.wands.WandOfFirebolt;
import com.ravenwolf.nnypd.items.wands.WandOfFreezing;
import com.ravenwolf.nnypd.items.wands.WandOfLightning;
import com.ravenwolf.nnypd.items.wands.WandOfMagicMissile;
import com.ravenwolf.nnypd.items.wands.WandOfAcidSpray;
import com.ravenwolf.nnypd.items.wands.CharmOfSanctuary;
import com.ravenwolf.nnypd.items.wands.CharmOfDecay;
import com.ravenwolf.nnypd.items.wands.WandOfSmiting;
import com.ravenwolf.nnypd.levels.PrisonLevel;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.sprites.WandmakerSprite;
import com.ravenwolf.nnypd.visuals.windows.WndQuest;
import com.ravenwolf.nnypd.visuals.windows.WndWandmaker;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wandmaker extends NPC {

	{
		name = "制杖老人";
		spriteClass = WandmakerSprite.class;
	}

	private static final String TXT_BERRY1	=
			"啊，能在这片地方遇见一位英雄可真是个惊喜！我来这里是为了寻找一个稀有材料，其名为_腐莓种子_。" +
					"作为一名法师我在这里有自保之力，可我在地牢里转几圈就迷路了，很丢人对吧。也许你能帮帮我？我很愿意将" +
					"自己的一把精制法杖或者魔咒作为奖励送给你！";

	private static final String TXT_DUST1	=
			"啊，能在这片地方遇见一位英雄可真是个惊喜！我来这里是为了寻找一个稀有材料，其名为_尸尘_。你能够从遗骸之中找到它，而这片地牢最不缺的就是遗骸了。" +
					"作为一名法师我在这里有自保之力，可我在地牢里转几圈就迷路了，很丢人对吧。也许你能帮帮我？我很愿意将" +
					"自己的一把精制法杖或者魔咒作为奖励送给你！";

	private static final String TXT_BERRY2	=
			"腐莓种子找到了吗，%s？没有？没事，我不着急。";

	private static final String TXT_DUST2	=
			"尸尘找到了吗，%s？没有？试着找些遗骸看看。";
	
	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

    @Override
    public boolean immovable() {
        return true;
    }

	@Override
	public boolean invulnerable(){
		return true;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
	}
	
	@Override
    public boolean add( Buff buff ) {
        return false;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {
			
			Item item = Quest.alternative ?
				Dungeon.hero.belongings.getItem( CorpseDust.class ) :null;
			if (item != null) {
				GameScene.show( new WndWandmaker( this, item ) );
			} else {
				tell( Quest.alternative ? TXT_DUST2 : TXT_BERRY2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_DUST1 : TXT_BERRY1 );
			Quest.given = true;
			
			Quest.placeItem();
			
			Journal.add( Journal.Feature.WANDMAKER );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	@Override
	public String description() {
		return
				"这位老先生的表情看起来十分困扰。他正在被一个神圣护盾保护着。";
	}
	
	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean completed;

		private static boolean given;
		
		public static Wand wand1;
		public static Wand wand2;
		
		public static void reset() {
			spawned = false;

			wand1 = null;
			wand2 = null;
		}
		
		private static final String NODE		= "wandmaker";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String COMPLETED	= "completed";
		private static final String GIVEN		= "given";
		private static final String WAND1		= "wand1";
		private static final String WAND2		= "wand2";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( ALTERNATIVE, alternative );
				node.put( COMPLETED, completed );

				node.put(GIVEN, given );
				
				node.put( WAND1, wand1 );
				node.put( WAND2, wand2 );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				alternative	=  node.getBoolean( ALTERNATIVE );
                completed	=  node.getBoolean( COMPLETED );

				given = node.getBoolean( GIVEN );
				
				wand1 = (Wand)node.get( WAND1 );
				wand2 = (Wand)node.get( WAND2 );

			} else {
				reset();
			}
		}
		
		public static void spawn( PrisonLevel level, Room room ) {
			if (!spawned && Dungeon.depth > 7 && Random.Int( 12 - Dungeon.depth ) == 0) {

				Wandmaker npc = new Wandmaker();
				do {
					npc.pos = room.random();
				} while (level.map[npc.pos] == Terrain.ENTRANCE || level.map[npc.pos] == Terrain.SIGN);
				level.mobs.add( npc );
				Actor.occupyCell( npc );
				
				spawned = true;
                completed = true;
                alternative = true;
//				alternative = Random.Int( 2 ) == 0;

				given = false;
				
				switch (Random.Int( 7 )) {
				case 0:
					wand1 = new WandOfAvalanche();
					break;
				case 1:
					wand1 = new WandOfDisintegration();
					break;
				case 2:
					wand1 = new WandOfFirebolt();
					break;
				case 3:
					wand1 = new WandOfLightning();
					break;
				case 4:
					wand1 = new WandOfSmiting();
					break;
                case 5:
                    wand1 = new WandOfMagicMissile();
                    break;
				case 6:
					wand1 = new WandOfAcidSpray();
					break;
				case 7:
					wand1 = new WandOfFreezing();
					break;
				}
				wand1.random().uncurse().upgrade();
				
				switch (Random.Int( 7)) {
				case 0:
					wand2 = new CharmOfDomination();
					break;
				case 1:
					wand2 = new CharmOfBlink();
					break;
				case 2:
					wand2 = new CharmOfThorns();
					break;
				case 3:
					wand2 = new CharmOfShadows();
					break;
				case 4:
					wand2 = new CharmOfDecay();
					break;
                case 5:
                    wand2 = new CharmOfSanctuary();
                    break;
				case 6:
					wand2 = new CharmOfWarden();
					break;
				case 7:
					wand2 = new CharmOfBlastWave();
					break;
				}
				wand2.random().uncurse(3).upgrade();
			}
		}
		
		public static void placeItem() {
			if (alternative) {
				
				ArrayList<Heap> candidates = new ArrayList<Heap>();
				for (Heap heap : Dungeon.level.heaps.values()) {
					if (heap.type == Heap.Type.BONES && !Dungeon.visible[heap.pos]) {
						candidates.add( heap );
					}
				}
				
				if (candidates.size() > 0) {
					Random.element( candidates ).drop( new CorpseDust() );
				} else {
					int pos = Dungeon.level.randomRespawnCell();
					while (Dungeon.level.heaps.get( pos ) != null) {
						pos = Dungeon.level.randomRespawnCell();
					}
					
					Heap heap = Dungeon.level.drop( new CorpseDust(), pos );
					heap.type = Heap.Type.BONES_CURSED;
					heap.sprite.link();
				}
				
			} else {
				
				int shrubPos = Dungeon.level.randomRespawnCell();
				while (Dungeon.level.heaps.get( shrubPos ) != null) {
					shrubPos = Dungeon.level.randomRespawnCell();
				}
				//Dungeon.level.plant(new Rotberry.Seed(), shrubPos);
				
			}
		}
		
		public static void complete() {
			wand1 = null;
			wand2 = null;

            completed = true;

			Journal.remove( Journal.Feature.WANDMAKER );
		}

        public static boolean isCompleted() {
            return completed;
        }
	}
	/*
	public static class Rotberry extends Plant {

		private static final String TXT_DESC =
			"Berries of this shrub taste like sweet, sweet death.";

		{
			image = 7;
			plantName = "Rotberry";
		}

		@Override
		public void activate( Char ch ) {
			super.activate( ch );

			GameScene.add( Blob.seed( pos, 100, CorrosiveGas.class ) );

			Dungeon.level.drop( new Seed(), pos ).sprite.drop();
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}

		public static class Seed extends Plant.Seed {
			{
				plantName = "Rotberry";

				name = "seed of " + plantName;
//				image = ItemSpriteSheet.HERB_ROTBERRY;

				plantClass = Rotberry.class;
				alchemyClass = PotionOfStrength.class;
			}

			@Override
			public boolean collect( Bag container ) {
				if (super.collect( container )) {

					if (Dungeon.level != null) {
						for (Mob mob : Dungeon.level.mobs) {
							mob.beckon( Dungeon.hero.pos );
						}

						GLog.w( "The seed emits a roar that echoes throughout the dungeon!" );
						CellEmitter.center( Dungeon.hero.pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
						Sample.INSTANCE.play( Assets.SND_CHALLENGE );
					}

					return true;
				} else {
					return false;
				}
			}

			@Override
			public String desc() {
				return TXT_DESC;
			}
		}
	}*/
}
