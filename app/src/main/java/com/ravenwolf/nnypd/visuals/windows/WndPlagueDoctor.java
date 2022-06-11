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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Body_AcidResistance;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Mending;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Recharging;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Toughness;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.npcs.PlagueDoctor;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.utils.Random;

public class WndPlagueDoctor extends Window {


	private static final String TXT_INTRO	=
			"你好，冒险家，你似乎需要一位医生。 " +
			"“我有最好的药物和调味品来根除瘟疫和减轻疼痛。" +
			"你想成为我的测试。。我是说，病人？";


	private static final String TXT_HEALING = "治疗 (%dg)";
	private static final String TXT_BUFF = "增益效果 (%dg)";
	private static final String TXT_CANCEL			= "没关系";

	private static String[][] LINES = {

			{
					"吸入这个。这会让你感觉好多了.",
					"你看？没有活着的医生能比得上我的作品",
			},
			{
					"水蛭会净化你的血液.",
					"对我的特殊调料正在生效。（咯咯咯）"
			}
	};

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	public WndPlagueDoctor(final PlagueDoctor doc) {
		
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.icon(doc.sprite());
		titlebar.label(Utils.capitalize(doc.name));
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
		final int price =50 + 50 * Dungeon.chapter();
		RedButton btnHeal = new RedButton( Utils.format(TXT_HEALING, price )) {
			@Override
			protected void onClick() {
				Heal( doc, price );
			}
		};
		btnHeal.enable(  price <= Dungeon.gold );
		btnHeal.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnHeal );

		/*RedButton btnBuff = new RedButton(  Utils.format(TXT_BUFF, price ) ) {
			@Override
			protected void onClick() {
				Buff( doc, price );
			}
		};
		btnBuff.enable(  price <= Dungeon.gold );
		btnBuff.setRect( 0, btnHeal.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnBuff );
*/
		RedButton btnNo = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnNo.setRect( 0, btnHeal.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );

		resize( WIDTH, (int)btnNo.bottom() );
	}


	private void Heal(PlagueDoctor doc, int price ) {
		hide();
		Dungeon.gold-=price;
		doc.reduceStock();
		Hero hero = Dungeon.hero;
		BuffActive.add( hero, Mending.class, Random.IntRange(28, 40));
		switch (Random.Int(10)){
			case 0 :
				BuffActive.add( hero, Blinded.class, Random.IntRange(6, 10));
				break;
			case 1 :
				BuffActive.add( hero, Frenzy.class, Random.IntRange(16, 24));
				break;
			case 2 :
				BuffActive.add( hero, Recharging.class, Random.IntRange(8, 12));
				break;
			case 3 :
			case 4 :
				BuffActive.add( hero, Dazed.class, Random.IntRange(8, 12));
				break;
			default:
				BuffActive.add( hero, Withered.class, Random.IntRange(12, 18));
		}

		doc.yell(LINES[0][Random.Int(LINES[0].length)]);
	}


	private void Buff(PlagueDoctor doc, int price ) {
		hide();
		Dungeon.gold-=price;
		doc.reduceStock();
		Hero hero = Dungeon.hero;
		switch (Random.Int(10)){
			case 0 :
				BuffActive.add( hero, Petrificated.class, Random.IntRange(4, 6));
				break;
			case 1 :
				BuffActive.add( hero, Toughness.class, Random.IntRange(50, 80));
				break;
			case 2 :
			case 3 :
				BuffActive.add( hero, Dazed.class, Random.IntRange(8, 12));
				break;
			default:
				BuffActive.add( hero, Poisoned.class, Random.IntRange(8, 12));
		}

		int statBonus= 1;
		int HPBonus = 2;
		switch (Random.Int(10)){
			case 0 :
				GLog.p( "你获得了 %d %s", statBonus,"acc" );
				hero.attackSkill+=statBonus;
				break;
			case 1 :
				GLog.p( "你获得了  %d %s", statBonus,"dex" );
				hero.defenseSkill+=statBonus;
				break;
			case 2 :
				GLog.p( "你获得了  %d %s", statBonus,"mag" );
				hero.magicSkill+=statBonus;
				break;
			case 3 :
			case 4 :
			case 5 :
				GLog.p( "你获得了  %d %s", HPBonus,"hp" );
				hero.HT+=HPBonus;
				break;
			default:
				BuffActive.add( hero, Body_AcidResistance.class, Random.IntRange(150, 200));

		}
		doc.yell(LINES[1][Random.Int(LINES[1].length)]);

	}
}
