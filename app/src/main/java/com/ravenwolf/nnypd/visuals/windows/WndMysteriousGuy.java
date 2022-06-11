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
import com.ravenwolf.nnypd.actors.mobs.npcs.MysteriousGuy;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;

public class WndMysteriousGuy extends Window {


	private static final String TXT_INTRO	=
			"我有一件非常特别的东西给你， " +
			"先给我钱，它就属于你了。 " +
			"你想要它吗？";
	private static final String TXT_BUY		= "用 %dg 购买";
	private static final String TXT_CANCEL		= "没关系";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	public WndMysteriousGuy(final MysteriousGuy guy, final EquipableItem item) {
		
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.icon(guy.sprite());
		titlebar.label(Utils.capitalize(guy.name));
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
		final int price =item.price() * Dungeon.chapter() * 2;
		RedButton btnBuy = new RedButton(  Utils.format( TXT_BUY, price ) ) {
			@Override
			protected void onClick() {
				buyItem( guy, item, price );
			}
		};
		btnBuy.enable( price <= Dungeon.gold );
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
	
	private void buyItem(MysteriousGuy guy, EquipableItem reward, int price ) {
		hide();
		Dungeon.gold-=price;
		guy.yell( "再见" );
		guy.runAway();
		Dungeon.level.drop( reward, guy.pos ).sprite.drop();

	}
}
