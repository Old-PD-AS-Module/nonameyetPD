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
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.special.UnholyArmor;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Tengu;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;

public class ScrollOfBanishment extends Scroll {

	private static final String TXT_PROCCED	=
			"你被吞没在在一片净化的光芒之中，周边的邪恶能量正在逐步退散！";
	private static final String TXT_NOT_PROCCED	=
			"你被吞没在在一片净化的光芒之中，但什么也没有发生。";

	{
		name = "放逐卷轴";
        shortName = "Ba";

        spellSprite = SpellSprite.SCROLL_EXORCISM;
        spellColour = SpellSprite.COLOUR_HOLY;
		icon=16;
	}
	
	@Override
	protected void doRead() {
		/*
		boolean procced = Ankh.uncurse( curUser, curUser.belongings.backpack.items.toArray( new Item[0] ) );

		procced = procced | Ankh.uncurse( curUser,
			curUser.belongings.weap1,
			curUser.belongings.weap2,
			curUser.belongings.armor,
			curUser.belongings.ring1, 
			curUser.belongings.ring2 );
*/
        boolean affected = false;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Level.fieldOfView[mob.pos] ) {

                //FIXME

                if( mob.isMagical() || mob.buff( UnholyArmor.class ) != null || mob instanceof Tengu) {

                    new Flare(6, 24).color(SpellSprite.COLOUR_HOLY, true).show(mob.sprite, 2f);

                    Buff.detach( mob, UnholyArmor.class );

                    if (mob instanceof Tengu){
						((Tengu)mob).killClones();
					}

                    if( mob.isMagical() ) {

                        //BuffActive.add( mob, Banished.class, (float)(10 + curUser.magicSkill()) );
                        BuffActive.addFromDamage( mob, Banished.class, 10 + curUser.magicSkill() );

                    }

                    affected = true;
                }
            }
        }
		
		if (/*procced || */affected) {
			GLog.p( TXT_PROCCED );
		} else {		
			GLog.i( TXT_NOT_PROCCED );		
		}

        GameScene.flash(SpellSprite.COLOUR_HOLY - 0x555555);

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
				"撰写在这张卷轴上的咒文能够驱散使用者周边的所有不洁魔法" +
						//"happen to exist near the reader, weakening curses on carried items, banishing " +
						"，并且放逐附近的生物，甚至于清除一些魔法效果。" +
						"\n\n这个卷轴的效果将会受到使用者的魔能影响";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 60 * quantity : super.price();
    }
}
