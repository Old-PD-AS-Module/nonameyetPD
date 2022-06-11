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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.hazards.HauntedArmorSleep;
import com.ravenwolf.nnypd.actors.hazards.HauntedWeaponSleep;
import com.ravenwolf.nnypd.actors.hazards.Hazard;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mimic;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.watabou.noosa.audio.Sample;

public class ScrollOfChallenge extends Scroll {

    private static final String TXT_MESSAGE	= "卷轴产生的激怒咆哮在地牢中回荡！";

    {
        name = "盛怒卷轴";
        shortName = "Ch";

        spellSprite = SpellSprite.SCROLL_CHALLENGE;
        spellColour = SpellSprite.COLOUR_DARK;
        icon=13;
	}
	
	@Override
	protected void doRead() {

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.CHEST_MIMIC) {
                Mimic m = Mimic.spawnAt( heap.hp, heap.pos, heap.items );
                if (m != null) {
//                    m.beckon( curUser.pos );
                    heap.destroy();
                }
            }
        }

        //wake up haunted armors
        for (Hazard hazard : Dungeon.level.hazards) {
            if (hazard instanceof HauntedArmorSleep) {
                HauntedArmorSleep h = (HauntedArmorSleep)hazard;
                h.challenged=true;
            }
            if (hazard instanceof HauntedWeaponSleep) {
                HauntedWeaponSleep h = (HauntedWeaponSleep)hazard;
                h.challenged=true;
            }
        }

        int amount = 1 + Dungeon.chapter();

        if (!Dungeon.bossLevel() && Dungeon.level.nMobs() > 0) {

            while( amount > 0) {

                Mob mob = Bestiary.mob(Dungeon.depth);
                mob.state = mob.HUNTING;
                mob.pos = Dungeon.level.randomRespawnCell();
                if (mob.pos != -1) {
                    GameScene.add(mob);
                    amount--;
                }
            }
        }

        for (Mob mob : Dungeon.level.mobs) {
            if( mob.hostile && mob.state != mob.PASSIVE ) {
                mob.beckon(curUser.pos);
            }
        }
		
		GLog.w(TXT_MESSAGE);

        /*int duration=(int)(10.0f * ( 110 + curUser.magicSkill() ) / 100);

        Enraged rage= BuffActive.add( curUser, Enraged.class, duration);
        if (rage!=null && rage.refreshDuration<duration) rage.refreshDuration=duration;*/
        BuffActive.add( curUser, Frenzy.class, 30 );

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        super.doRead();
	}
	
	@Override
	public String desc() {
        return
                "大声阅读其上的符文，这张卷轴将会赐予你一股强大的怒火，短时间内大幅提高你的伤害。并且它会激怒本层所有的生物，同时将你的位置暴露无遗。";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 60 * quantity : super.price();
    }
}
