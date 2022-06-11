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
import com.ravenwolf.nnypd.actors.mobs.npcs.VendingMachine;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.food.FrozenCarpaccio;
import com.ravenwolf.nnypd.items.food.OverpricedRation;
import com.ravenwolf.nnypd.items.food.Pasty;
import com.ravenwolf.nnypd.items.food.RationMedium;
import com.ravenwolf.nnypd.items.herbs.EarthrootHerb;
import com.ravenwolf.nnypd.items.herbs.WyrmflowerHerb;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.misc.Waterskin;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.utils.Random;

public class WndVendingMachine extends Window {


	private static final String TXT_INTRO	=
			"这是一台矮人自动售货机。 " +
			"投入你的硬币并选择你想要的";

	private static final String TXT_BUY_FOOD		= "购买食物 (%dg)";
	private static final String TXT_BUY_DRINK		= "购买饮料 (%dg)";
	private static final String TXT_CANCEL			= "没关系";
	private static final String TXT_OUT_OF_STOCK	= "缺货";

	private static final String TXT_CLANK_CLUNK = "叮。。。叮。。。咚！";
	private static final String TXT_CLANK_NONE = "叮。。。";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	public WndVendingMachine(final VendingMachine machine, final boolean food) {
		
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.icon(machine.sprite());
		titlebar.label(Utils.capitalize(machine.name));
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
		//final int price =200;
		final int price =  50 * Dungeon.chapter();
		RedButton btnBuyFood = new RedButton(  food ? Utils.format( TXT_BUY_FOOD, price ):  Utils.format( TXT_BUY_DRINK, price ) ) {
			@Override
			protected void onClick() {
				if (food)
					buyFood( machine, price );
				else
					buyDrink(machine, price);
			}
		};
		btnBuyFood.enable(  price <= Dungeon.gold );
		btnBuyFood.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBuyFood );

		
		RedButton btnNo = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnNo.setRect( 0, btnBuyFood.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	

	private void buyFood(VendingMachine machine, int price ) {
		hide();
		Item item;

		switch (Random.Int(10)){
			case 0 :
				item = new WyrmflowerHerb();
				break;
			case 1 :
			case 2 :
				item = new EarthrootHerb().quantity(3);
				break;
			case 3 :
			case 4 :
			case 5 :
				item = Random.oneOf(new Pasty(), new RationMedium(), new OverpricedRation());
				break;
			default:
				item = new FrozenCarpaccio().quantity(2);
		}

		Dungeon.gold-=price;
		machine.reduceStock();
		if (machine.stuckItem == null && Random.Int(5)==0){
			machine.stuckItem = item;
			item.quantity++;
			machine.yell( TXT_CLANK_NONE );
			GLog.i("产品好像卡在机器上了。。。");
		}else{
			Dungeon.level.drop( item, machine.pos ).sprite.drop();
			machine.throwItem();
			machine.yell( TXT_CLANK_CLUNK );
		}

	}

	private void buyDrink(VendingMachine machine, int price ) {
		hide();
		Item item;

		switch (Random.Int(10)){
			case 0 :
				item = new Waterskin();
				break;
			case 1 :
			case 2 :
				item = new OilLantern.OilFlask();
				break;
			default:
				item = Generator.random(Generator.Category.POTION);
		}

		Dungeon.gold-=price;
		machine.reduceStock();
		if (machine.stuckItem == null && Random.Int(5)==0){
			machine.stuckItem = item;
			item.quantity++;
			machine.yell( TXT_CLANK_NONE );
			GLog.i("产品好像卡在机器上了。。。");
		}else{
			Dungeon.level.drop( item, machine.pos ).sprite.drop();
			machine.throwItem();
			machine.yell( TXT_CLANK_CLUNK );
		}

	}
}
