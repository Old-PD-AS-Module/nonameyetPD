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

import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.windows.WndBag;

public class ScrollOfRemoveCurse extends InventoryScroll {


	{
        name = "袪邪卷轴";
        shortName = "Rc";

        inventoryTitle = "选择一件需要净化的物品";
		mode = WndBag.Mode.CURSED;

        spellSprite = SpellSprite.SCROLL_EXORCISM;
        spellColour = SpellSprite.COLOUR_RUNE;
        icon=8;
	}
	
	@Override
    public void onItemSelected(Item item) {

        item.identify( CURSED_KNOWN );

        item.uncurse();

        curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

        QuickSlot.refresh();
	}

	
	@Override
	public String desc() {
        return
                "卷轴上的咒语可以瞬间清除任何一件武器、护甲、戒指、法杖、魔咒上的诅咒";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 40 * quantity : super.price();
    }
}
