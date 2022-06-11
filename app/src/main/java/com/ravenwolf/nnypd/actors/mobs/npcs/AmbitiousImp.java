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
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.actors.mobs.DwarfMonk;
import com.ravenwolf.nnypd.actors.mobs.DwarfSeniorMonk;
import com.ravenwolf.nnypd.actors.mobs.Golem;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.quest.DwarfToken;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.rings.RingOfAccuracy;
import com.ravenwolf.nnypd.items.rings.RingOfAwareness;
import com.ravenwolf.nnypd.items.rings.RingOfConcentration;
import com.ravenwolf.nnypd.items.rings.RingOfEvasion;
import com.ravenwolf.nnypd.items.rings.RingOfFortune;
import com.ravenwolf.nnypd.items.rings.RingOfProtection;
import com.ravenwolf.nnypd.items.rings.RingOfFuror;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.items.rings.RingOfShadows;
import com.ravenwolf.nnypd.items.rings.RingOfSharpShooting;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.items.rings.RingOfVitality;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.sprites.AmbitiousImpSprite;
import com.ravenwolf.nnypd.visuals.windows.WndImp;
import com.ravenwolf.nnypd.visuals.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AmbitiousImp extends NPC {

	{
		name = "野心勃勃的小恶魔";
		spriteClass = AmbitiousImpSprite.class;
	}

	private static final String TXT_GOLEMS1	=
			"你是个冒险家吗？我爱冒险家！如果有什么东西需要被解决的话，他们永远都能把活干好。我说的对吗？当然是在有赏金的前提下；)\n" +
					"总之，我需要你杀一些_魔像_。你看，我要在这里开始搞点小本生意，但这些愚蠢的傀儡只会毁掉我的生意！跟这些发着光" +
					"的大个子魔像根本没法交流，真是该死！所以请帮我，杀死……我想想，_6个魔像_，然后奖励就是你的了。";

	private static final String TXT_MONKS1	=
			"你是个冒险家吗？我爱冒险家！如果有什么东西需要被解决的话，他们永远都能把活干好。我说的对吗？当然是在有赏金的前提下  ；)\n" +
					"总之，我需要你杀一些_武僧_。你看，我要在这里开始搞点小本生意，但这些疯子不买任何东西还会吓跑顾客。所以请帮我，杀死……我想想，_8个武僧_，然后奖励就是你的了。";

	private static final String TXT_GOLEMS2	=
			"你的魔像狩猎做的怎么样了？";

	private static final String TXT_MONKS2	=
			"喔，你还活着！我就知道你得功夫很好:) 只要别忘了拿来那些武僧的标记就好。";

	private static final String TXT_CYA	= "我们会再见的，%s!";
	private static final String TXT_HEY	= "喂喂喂，%s!";
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell( Utils.format( TXT_HEY, Dungeon.hero.className() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 1000;
//	}
	
//	@Override
//	public String defenseVerb() {
//		return "evaded";
//	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
	}
	
	@Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean immovable() {
        return true;
    }
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {

			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6))) {
				GameScene.show( new WndImp( this, tokens ) );
			} else {
				tell( Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1 );
			Quest.given = true;
			Quest.completed = false;
			
			Journal.add( Journal.Feature.IMP );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( 
			new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	public void flee() {
		
		yell(Utils.format(TXT_CYA, Dungeon.hero.className()));
		destroy();
        sprite.emitter().start(ElmoParticle.FACTORY, 0.03f, 60);
        sprite.killAndErase();
	}
	
	@Override
	public String description() {
		return
				"小恶魔是一种低等恶魔。没有强大的力量也没有魔法天赋，这些生物仅以其残忍品性和贪婪无餍而恶名昭彰。" +
						"不过其中少数个体相当聪明且善于交际。比如你遇到的这个，看起来就挺像回事的。";
	}
	
	public static class Quest {
		
		private static boolean alternative;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	= node.getBoolean( ALTERNATIVE );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			}
		}
		
		public static void spawn( Level level, Room room ) {
			if (!spawned && Dungeon.depth > 19 && Random.Int( 24 - Dungeon.depth ) == 0) {

				AmbitiousImp npc = new AmbitiousImp();
				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1 || level.heaps.get( npc.pos ) != null || !level.NPCSafePos(npc.pos));
				level.mobs.add(npc);
				Actor.occupyCell( npc );
				
				spawned = true;	
				alternative = true;
				
				given = false;

                int random = Random.Int( 3 );

                if( Dungeon.hero.heroClass == HeroClass.WARRIOR ) {
                    reward = random == 2 ? new RingOfSatiety() : random == 1 ? new RingOfVitality() : new RingOfFuror();
                } else if( Dungeon.hero.heroClass == HeroClass.BRIGAND) {
                    reward = random == 2 ? new RingOfFortune() : random == 1 ? new RingOfShadows() : new RingOfEvasion();
                } else if( Dungeon.hero.heroClass == HeroClass.SCHOLAR) {
                    reward = random == 2 ? new RingOfProtection() : random == 1 ? new RingOfConcentration() : new RingOfSorcery();
                } else if( Dungeon.hero.heroClass == HeroClass.ACOLYTE) {
                    reward = random == 2 ? new RingOfSharpShooting() : random == 1 ? new RingOfAwareness() : new RingOfAccuracy();
                }

				reward.bonus = Random.Int( 3 ) + 1;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof DwarfMonk) || (alternative && mob instanceof DwarfSeniorMonk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Journal.remove(Journal.Feature.IMP);
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
