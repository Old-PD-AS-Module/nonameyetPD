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
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.BodyArmorCloth;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ShopkeeperGhostSprite;
import com.ravenwolf.nnypd.visuals.windows.WndMysteriousGuy;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MysteriousGuy extends NPCSupplier {

    private static final String TXT_GREETINGS = "嘿！我这里有个特别的优惠活动";

    public EquipableItem item;

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
                    "你跑不掉的，朋友",
                    "这可不是一个明智之举",
            },
    };

	{
		name = "神秘商人";
		spriteClass = ShopkeeperGhostSprite.class;
	}

    public static void spawn(Level level ){
        MysteriousGuy npc = new MysteriousGuy();
        do {
            npc.pos = level.randomRespawnCell();
        } while (npc.pos == -1 || level.heaps.get( npc.pos ) != null || !level.NPCSafePos(npc.pos) );

        npc.generateLoot();
        level.mobs.add(npc);
        Actor.occupyCell( npc );

    }

    protected Journal.Feature feature(){
        return Journal.Feature.MYSTERIOUS_FELLOW;
    }

    protected String greetingsText(){
        return TXT_GREETINGS;
    }

    protected String[][] lines(){
        return LINES;
    }


	public void runAway() {
		super.runAway();
        if( threatened >= 2 )
            Wraith.spawnAround(pos, 3);

		CellEmitter.get( pos ).burst(Speck.factory(Speck.WOOL), 10);
	}



    public void generateLoot( ) {

        switch (Random.Int(4)){
            case 0:
                do {
                    item = (Weapon) Generator.random(Generator.Category.WEAPON);
                } while (item instanceof ThrowingWeapon || item.lootChapter()+item.bonus < Dungeon.chapter() +1 );
                break;
            case 1:
                do {
                    item = (Armour)Generator.random( Generator.Category.ARMOR );
                } while (item instanceof BodyArmorCloth || item.lootChapter()+item.bonus < Dungeon.chapter() +1 );
                break;
            case 2:
                item = (Wand)Generator.random( Generator.Category.WAND ).random();
                break;
            case 3:
                item = (Ring)Generator.random( Generator.Category.RING ).random();
                break;
        }
        item.identify();
        item.upgrade();
        item.upgrade();
    }

	@Override
	public void interact() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        GameScene.show( new WndMysteriousGuy( this, item ));
	}

    @Override
    public String description() {
        return
                "虽然他看起来并不吓人，但是你的潜意识时刻提醒着你，这家伙非常危险，你想知道他在这里做什么";
    }


    private static final String ITEM		    = "item";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(ITEM, item);

    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        item = (EquipableItem) bundle.get(ITEM);
    }
}
