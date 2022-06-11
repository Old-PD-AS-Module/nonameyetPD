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
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Miasma;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.ShopkeeperPlagueDocSprite;
import com.ravenwolf.nnypd.visuals.windows.WndPlagueDoctor;
import com.watabou.utils.Random;


public class PlagueDoctor extends NPCSupplier {

    private static final String TXT_GREETINGS = "终于来了！一个合格的测试者";

    private static final String TXT_OUT_OF_STOCK = "抱歉，我得整理下的的补给品，稍等...";


    private static String[][] LINES = {

            {
                    "哦不！我的瓶子坏了，这要收费的！",
                    "惹我的话你会受到诅咒的。",
            },
            {
                    "想要知道我的秘密，除非踏过我的尸体。",
                    "你无法理解我的想法，因为你害怕！",
            },
            {
                    "所有的肉体都会随着时间而消亡",
                    "我遇到过很多的瘟疫，但我们注定会失败",
                    "看着他们慢慢枯萎直至死亡！",
            },
    };

	{
		name = "瘟疫医生";
		spriteClass = ShopkeeperPlagueDocSprite.class;//导入创建人物数据
	}

    protected Journal.Feature feature(){
	    return Journal.Feature.PLAGUE_DOCTOR;
    }//日志中可以查看瘟疫医生

    protected String greetingsText(){
	    return TXT_GREETINGS;
    }//设置问候语

    protected String[][] lines(){
	    return LINES;
    }//人物语言

    //生成方法
    public static void spawn(Level level ){
        PlagueDoctor npc = new PlagueDoctor();
        do {
            npc.pos = level.randomRespawnCell();//坐标是随机赋值
        } while (npc.pos == -1 || level.heaps.get( npc.pos ) != null || !level.NPCSafePos(npc.pos));

        npc.stock = 2 + Random.Int(2);
        level.mobs.add(npc);
        Actor.occupyCell( npc );
    }

    public void runAway() {
	    super.runAway();
        GameScene.add( Blob.seed( pos, 600, Miasma.class ) );
        CellEmitter.get(pos).start( ShadowParticle.CURSE, 0.02f, 40 );
    }

    @Override
    public String description() {
        return
            "俗话说，哪里有瘟疫，哪里就有瘟医，哪里就会有死亡。不过，你并不知道这个瘟医为什么会在这里，看起来不太正常。但至少他看起来并没有受到瘟疫的影响";
    }

	@Override
	public void interact() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if (outOfStock() )
            tell( TXT_OUT_OF_STOCK );
        else
            GameScene.show( new WndPlagueDoctor( this ));
	}

}
