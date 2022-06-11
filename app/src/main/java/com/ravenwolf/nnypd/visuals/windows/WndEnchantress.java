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
package com.ravenwolf.nnypd.visuals.windows;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.mobs.npcs.Enchantress;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.scrolls.InventoryScroll;
import com.ravenwolf.nnypd.items.scrolls.Scroll;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfIdentify;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.utils.Random;

public class WndEnchantress extends Window {


	private static final String TXT_INTRO	=
			"我的魔法书上有很多卷轴， " +
			"但我无法选择它将提供哪一个卷轴，这个有时候是 \"不稳定\"  的" +
			"“你说呢，想买一幅卷轴吗？";
	private static final String TXT_BUY		= "买一个 %dg 卷轴";

	private static final String TXT_UPS	= "噢，这不应该发生的";
	private static final String TXT_CANCEL		= "没关系";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	public WndEnchantress(final Enchantress ench) {
		
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.icon(ench.sprite());
		titlebar.label(Utils.capitalize(ench.name));
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		/*BitmapTextMultiline message = PixelScene.createMultiline(TXT_INTRO, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );*/
		RenderedTextMultiline message = PixelScene.renderMultiline(TXT_INTRO, 6 );
		message.maxWidth(WIDTH);
		PixelScene.align(message);
		float y = titlebar.bottom() + GAP;
		message.setPos(0, y);
		add(message);
		final int price =75 + 50 * Dungeon.chapter();
		RedButton btnBuy = new RedButton(  Utils.format( TXT_BUY, price ) ) {
			@Override
			protected void onClick() {
				buyScroll( ench, price );
			}
		};
		btnBuy.enable( ench.stock > 0 && price <= Dungeon.gold );
		btnBuy.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBuy );
		
		RedButton btnNo = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnNo.setRect( 0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	
	private void buyScroll(Enchantress ench, int price ) {
		hide();

		Scroll scroll = (Scroll) Generator.random(Generator.Category.SCROLL);

		Dungeon.gold-=price;

		int n;
		do {
			n = ench.pos + Level.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Level.passable[n] && !Level.avoid[n] && !Level.chasm[n]);
		Dungeon.level.drop( scroll, n ).sprite.drop();
		CellEmitter.get( n ).start( ElmoParticle.FACTORY, 0.2f, 6 );

		if (Random.Int(3)==0 ) {
			if (!(scroll instanceof InventoryScroll)) {
				scroll.activateScroll(scroll, Dungeon.hero);
				ench.yell(TXT_UPS);
			} else {
				for (int i = 0; i<5; i++) {
					Item item = Dungeon.hero.belongings.randomVisibleUnequipped();
					if (!item.isIdentified() && scroll instanceof ScrollOfIdentify){
						((ScrollOfIdentify)scroll).onItemSelected(item);
						break;
					} else if (item instanceof EquipableItem && !(scroll instanceof ScrollOfIdentify)){
						((InventoryScroll)scroll).onItemSelected(item);
						break;
					}
				}

			}
		}
		ench.reduceStock();
	}
}
