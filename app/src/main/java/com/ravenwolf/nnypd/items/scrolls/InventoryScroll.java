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
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;

public abstract class InventoryScroll extends Scroll {

	protected String inventoryTitle = "请选择一个物品";
	protected WndBag.Mode mode = WndBag.Mode.ALL;

	private static final String TXT_WARNING	= "你真的要终止这张卷轴的效果吗？它依然会被消耗掉。";
	private static final String TXT_YES		= "是的，我知道自己在做什么";
	private static final String TXT_NO		= "不，我改变主意了";
	
	@Override
	protected void doRead() {
		
		if (!isTypeKnown()) {
			setKnown();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		
		GameScene.selectItem( itemSelector, mode, inventoryTitle );
	}
	
	private void confirmCancelation() {
		GameScene.show( new WndOptions( name(), TXT_WARNING, TXT_YES, TXT_NO ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
				case 0:
					curUser.spendAndNext( TIME_TO_READ );
					identifiedByUse = false;
					break;
				case 1:
					GameScene.selectItem( itemSelector, mode, inventoryTitle );
					break;
				}
			}
			public void onBackPressed() {}
        } );
	}
	
	public abstract void onItemSelected(Item item);
	
	protected static boolean identifiedByUse = false;

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {

				((InventoryScroll)curItem).onItemSelected(item);

                isUsed(curUser, (Scroll)curItem );
				
			} else if (identifiedByUse) {
				
				((InventoryScroll)curItem).confirmCancelation();
				
			} else {

				curItem.collect( curUser.belongings.backpack );
				
			}
		}
	};
}
