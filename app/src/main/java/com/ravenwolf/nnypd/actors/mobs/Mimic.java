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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Pushing;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.MimicSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Mimic extends MobHealthy {
	
//	private int bonus;

    private static final float TIME_TO_DEVOUR	= 1.5f;

    public Mimic() {

        super( Dungeon.depth*3/4 + 2/*Dungeon.depth + 1 */ );

		name = "拟型怪";
        spriteClass = MimicSprite.class;

        items = new ArrayList<>();

        minDamage += tier;
        maxDamage += tier;

        HP = HT += Random.IntRange( tier, tier*2 );
        armorClass/=2;

        baseSpeed = 0.75f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
    }
	
	public ArrayList<Item> items;
	
//	private static final String LEVEL	= "bonus";
	private static final String ITEMS	= "items";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( ITEMS, items );
//		bundle.put( LEVEL, bonus );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		items = new ArrayList<Item>( (Collection<? extends Item>) (Object) bundle.getCollection( ITEMS ) );
//		adjustStats( bundle.getInt( LEVEL ) );
	}

//    @Override
//    public float attackDelay() {
//        return 1f;
//    }

//
//	@Override
//	public int accuracy( Char target ) {
//		return 9 + bonus;
//	}
	
//	@Override
//	public int attackProc( Char enemy, int damage ) {
//		if (enemy == Dungeon.hero && Random.Int( 3 ) == 0) {
//			Gold gold = new Gold( Random.Int( Dungeon.gold / 10, Dungeon.gold / 2 ) );
//			if (gold.quantity() > 0) {
//				Dungeon.gold -= gold.quantity();
//				Dungeon.bonus.drop( gold, Dungeon.hero.pos ).sprite.drop();
//			}
//		}
//		return super.attackProc( enemy, damage );
//	}
//
//	public void adjustStats( int bonus ) {
//		this.bonus = bonus;
//
//		HT = (3 + bonus) * 3;
//		EXP = 2 + 2 * (bonus - 1) / 5;
//		dexterity = accuracy( null ) / 2;
//
//		enemySeen = true;
//	}

    @Override
    public boolean cast( Char enemy ) {
        return false;
    }

    @Override
    protected boolean act() {

        Heap heap = Dungeon.level.heaps.get( pos );

        if (heap != null && heap.type == Heap.Type.HEAP && !enemySeen) {

            ((MimicSprite)sprite).devour();

            Item item = heap.pickUp();

            devour(item);

            if (Dungeon.visible[pos]) {
				GLog.w("宝箱怪吞掉了地上的%s！", item.toString());
            }

            spend( TIME_TO_DEVOUR );

            return true;

//        } else if( heap == null && items != null && state != HUNTING && !enemySeen && Random.Int( Dungeon.chapter() + 1 ) == 0 ) {
//
//            for( Item item : items) {
//                heap = Dungeon.level.drop(item, pos);
//            }
//
//            if( heap != null ) {
//                heap.type = Heap.Type.CHEST_MIMIC;
//                heap.sprite.link();
//                heap.sprite.drop();
//                heap.hp = HT;
//            }
//
//            HP = 0;
//            sprite.killAndErase();
//            Dungeon.level.mobs.remove(this);
//            Actor.remove(this);
//            Actor.freeCell(pos);
//
//            return true;

        } else {

            return super.act();

        }
    }

    private void devour( Item item ) {

        if ( items.contains( item ) ) {
            return;
        }

        if (item.stackable) {
            Class<?> c = getClass();
            for (Item i : items) {
                if (i.getClass() == c) {
                    i.quantity += item.quantity;
                    return;
                }
            }
        }

        items.add(item);
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

		super.die( cause, dmg );
		
		if (items != null) {
			for (Item item : items) {
				Dungeon.level.drop( item, pos ).sprite.drop();
			}
		}
	}
	
	@Override
	public boolean reset() {
		state = WANDERING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

	@Override
	public String description() {
		return
				"拟型怪是一种可以随心所欲变成任何形状的魔法生物，在地牢里它们总是会变成一种宝箱的样子。因为他们知道如何吸引冒险者，但是它本身速度较慢，稍有不慎就会放跑猎物。";
	}
	
	public static Mimic spawnAt( int hp, int pos, List<Item> items ) {
		Char ch = Actor.findChar( pos );

		if (ch != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int n : Level.NEIGHBOURS8) {
				int cell = pos + n;
				if ((Level.passable[cell] || Level.avoid[cell]) && Actor.findChar( cell ) == null) {
					candidates.add( cell );
				}
			}
			if (candidates.size() > 0) {
				int newPos = Random.element( candidates );
				Actor.addDelayed( new Pushing( ch, ch.pos, newPos ), -1 );
				
				ch.pos = newPos;

//				if (ch instanceof Mob) {
//					Dungeon.level.mobPress( (Mob)ch );
//				} else {
					Dungeon.level.press( newPos, ch );
//				}
			} else {
				return null;
			}
		}
		
		Mimic m = new Mimic();
		m.items = new ArrayList<Item>( items );
//		m.adjustStats( Dungeon.depth );
//		m.HP = m.HT;
		m.pos = pos;
        m.enemySeen = true;
        m.special = true;
        m.state = m.HUNTING;

        if( hp > 0 ) {
            m.HT = m.HP = hp;
        }


        //mimics spawn with an additional consumible
		m.items.add(Generator.randomComestible());
/*
        //identify equipable items
		if (m.items != null) {
			for (Item item : m.items) {
				if (item instanceof EquipableItem);
					item.identify();
			}
		}
*/
		GameScene.add( m, 0.5f );
		
		m.sprite.turnTo( pos, Dungeon.hero.pos );
		
		if (Dungeon.visible[m.pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.STAR ), 10 );
			Sample.INSTANCE.play( Assets.SND_MIMIC );
		}
		
		return m;
	}
}
