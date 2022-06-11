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
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;

public class ScrollOfPhaseWarp extends Scroll {

	public static final String TXT_NO_TELEPORT =
            "传送失败！";

    {
        name = "传送卷轴";
        shortName = "Ph";

        spellSprite = SpellSprite.SCROLL_TELEPORT;
        spellColour = SpellSprite.COLOUR_WILD;
        icon=9;
	}
	
	@Override
	protected void doRead() {

        int pos;

        pos = Dungeon.level.randomRespawnCell( false, true );

        if (pos == -1) {

            GLog.w( TXT_NO_TELEPORT );

        } else {

            CharmOfBlink.appear(curUser, pos);
            Dungeon.level.press(pos, curUser);
            Dungeon.observe();

        }

        super.doRead();
	}

    @Override
	public String desc() {
        return
                "卷轴上的咒语能立刻让阅读者传送到本层的随机位置。危急时刻可以拿来当做逃脱手段";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 40 * quantity : super.price();
    }
}
