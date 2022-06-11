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
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Rejuvenation;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ShopkeeperHumanSprite;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.ravenwolf.nnypd.visuals.windows.WndTradeItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shopkeeper extends NPC {

    private static final String TXT_GREETINGS = "你好！要来看看我的商品吗？";

    private static String[][] LINES = {

            {
                    "嘿，停下。",
                    "别这么做。",
                    "我们店里禁止这种行为。",
                    "请停止你的行为。",
                    "喂！快住手！",
            },
            {
                    "再让我看见我就叫人来了！",
                    "快停下！我要叫人了！",
                    "我建议你不要惹我。",
            },
            {
                    "守卫！守卫！",
                    "抢劫啦！救命啊！",
                    "救命啊！有人打我！",
            },
            {
                    "算了，我要走了。",
                    "就是这样，我不干了。",
                    "你为什么就不能让我自己静一静！",
            },
    };

    private int threatened = 0;
    private boolean seenBefore = false;

	{
        name = "商人";
		spriteClass = ShopkeeperHumanSprite.class;
	}

    @Override
    public boolean invulnerable(){
        return true;
    }
	
	@Override
	protected boolean act() {

        if( noticed ) {

            noticed = false;

        }

        if (!seenBefore && Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.SHOP );
            seenBefore = true;
            greetings();
        }

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
        react();
	}

	@Override
    public boolean add( Buff buff ) {
        if( !( buff instanceof Rejuvenation ) && !( buff instanceof PinCushion) ) {
            react();
        }

        return false;
    }

    protected void greetings() {
        yell(Utils.format(TXT_GREETINGS));
    }
	
	protected void react() {

        if( threatened < LINES.length ) {
            yell(LINES[threatened][Random.Int(LINES[threatened].length)]);
        }

        if( threatened >= 3 ) {
            runAway();
        } else if( threatened >= 2 ) {
            callForHelp();
        }

        threatened++;
    }

    protected void callForHelp() {

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos != pos) {
                mob.beckon( pos );
            }
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center( pos ).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
        }

        Sample.INSTANCE.play( Assets.SND_CHALLENGE );
    }

	protected void runAway() {

        ArrayList<Heap> forSale = new ArrayList<>();

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.FOR_SALE) {

                forSale.add( heap );

            }
        }

        int amount = forSale.size();

		for ( Heap heap : forSale ) {

            if( Random.Int( amount + 1 ) > 0 ) {

                CellEmitter.get(heap.pos).burst( Speck.factory( Speck.WOOL ), 5 );
                heap.destroy();

            } else {

                heap.type = Heap.Type.HEAP;

            }
		}
		
		destroy();
		sprite.killAndErase();
		
		Journal.remove( Journal.Feature.SHOP );
		CellEmitter.get( pos ).burst(Speck.factory(Speck.WOOL), 10);
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
	public String description() {
        return
                "这个矮胖的家伙看起来更适合在某些大城市里做买卖而不是这种地牢黑市。不过对你来说倒算是个好消息。";
    }
	
	public static WndBag sell() {
        return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, "选择一件要出售的物品" );
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public void interact() {
		sell();
	}

    private static final String SEENBEFORE		= "seenbefore";
    private static final String THREATENED		= "threatened";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( SEENBEFORE, seenBefore );
        bundle.put( THREATENED, threatened );
    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean( SEENBEFORE );
        threatened = bundle.getInt( THREATENED );
    }
}
