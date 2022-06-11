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
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.Wraith;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.sprites.ShopkeeperEnchantressSprite;
import com.ravenwolf.nnypd.visuals.windows.WndEnchantress;
import com.watabou.utils.Random;

public class Enchantress extends NPCSupplier {

    private static final String TXT_GREETINGS = "嗨，想买一些魔法卷轴吗？";
    private static final String TXT_OUT_OF_STOCK = "抱歉不能提供更多卷轴了，我的魔法书正在充能";

    private static String[][] LINES = {

            {
                    "嘿，停下！",
                    "别这么做。",
                    "喂！快住手！",
            },
            {
                    "你会后悔的。",
            },
            {
                    "黑暗魔法会让你付出代价",
                    "这可不是一个明智之举",
            },
    };


	{
		name = "女巫";
		spriteClass = ShopkeeperEnchantressSprite.class;
	}

    public static void spawn(Level level ){
        Enchantress npc = new Enchantress();
        do {
            npc.pos = level.randomRespawnCell();
        } while (npc.pos == -1 || level.heaps.get( npc.pos ) != null || !level.NPCSafePos(npc.pos));

        npc.stock = 2 + Random.Int(3);
        level.mobs.add(npc);
        Actor.occupyCell( npc );
    }


    protected Journal.Feature feature(){
        return Journal.Feature.YOUNG_ENCHANTRESS;
    }

    protected String greetingsText(){
        return TXT_GREETINGS;
    }

    protected String[][] lines(){
        return LINES;
    }


	public void runAway() {
	    super.runAway();
        Wraith.spawnAround(pos, 3);
        CellEmitter.get(pos).start( ElmoParticle.FACTORY, 0.02f, 40 );
	}

	@Override
	public void interact() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if (outOfStock() )
            tell( TXT_OUT_OF_STOCK );
        else
            GameScene.show( new WndEnchantress( this ));
	}


    @Override
    public String description() {
        return
                "对于一个年轻的女巫来说，这里似乎是一个非常危险的地方。但你从她的书中感觉到了一股非常强大的力量，从而保护她不受到任何伤害";
    }

}
