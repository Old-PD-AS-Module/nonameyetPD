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

import com.ravenwolf.nnypd.Chrome;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;
	
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();
	
	static {
		CHAPTERS.put( ID_SEWERS,
				"这片地牢位于城市的正下方，它的最上层其实是由城市的下水道系统组成的。\n\n" +
						"由于下方不断渗透的黑暗能量，这些本应无害的下水道生物变得越来越危险。" +
						"城市向这里派出巡逻队并试图以此保护其上方的区域，但他们的影响也在逐渐式微。" +
						"\n\n这片区域已经算是足够危险了，不过至少大部分邪恶魔法还不足以对这里造成影响。" );

		CHAPTERS.put( ID_PRISON,
				"多年以前一座地下监狱为了收容危险的犯罪者而建立于此。" +
						"由于其严格的监管和高安全性，地表各处的囚犯都被带到这里经受时间的折磨。\n\n" +
						"但不久之后下方充斥着黑暗的瘴气在这里弥漫开来，扭曲了罪犯和狱卒的心智。\n\n" +
						"监狱里充斥开来的混乱使其彻底失去了秩序，于是城市封锁了整个监狱。现今已经没有人知道这些高墙之下经历过无数死亡的生物究竟会是什么样子..." );

		CHAPTERS.put( ID_CAVES,
				"这座从废弃监狱延伸而下的洞穴，早已空无一人。这里对于城市而言过于深入且很难开发，" +
						"其中极度匮乏的矿物也无法激起矮人的兴趣。曾经在这里有一个为两个势力建立的贸易站，" +
						"但在矮人国度衰落之后，只有无所不在的豺狼人和地下生物还居住在这里。" );

		CHAPTERS.put( ID_METROPOLIS,
				"矮人都市曾经是最宏伟的矮人城邦。在其鼎盛时期矮人的机械化部队曾成功击退过古神及其恶魔军队的入侵。" +
						"但是也有传闻说，凯旋而归的战士同时带来了腐坏的种子，矮人王国的胜利正是其毁灭的开端。" );

		CHAPTERS.put( ID_HALLS,
				"很久以前这片区域曾是矮人都市的郊区。在与古神的战争中取得惨胜的矮人已经无力掌控并清理这片地区。" +
						"于是存活的恶魔逐渐巩固了对这里的控制，现在，这里被称作恶魔大厅。\n\n" +
						"很少有冒险者能够深入到这种地方..." );
	}

    //private BitmapTextMultiline tf;
	private RenderedTextMultiline tf;
	
	private float delay;
	
	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );
		
		/*tf = PixelScene.createMultiline( text, 7 );
		tf.maxWidth = WIDTH - MARGIN * 2;
		tf.measure();
		tf.ra = bgR;
		tf.ga = bgG;
		tf.ba = bgB;
		tf.rm = -bgR;
		tf.gm = -bgG;
		tf.bm = -bgB;
		tf.x = MARGIN;*/
		tf = PixelScene.renderMultiline( text, 5 );
		tf.maxWidth(WIDTH - MARGIN * 2);
		PixelScene.align(tf);
		tf.invert();
		tf.setPos(MARGIN, 0);
		add( tf );
		
		add( new TouchArea( chrome ) {
			@Override
			protected void onClick( Touch touch ) {
				hide();
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height(), 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
	
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		
		String text = CHAPTERS.get( id );
		if (text != null) {
			WndStory wnd = new WndStory( text );
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
			}
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
}
