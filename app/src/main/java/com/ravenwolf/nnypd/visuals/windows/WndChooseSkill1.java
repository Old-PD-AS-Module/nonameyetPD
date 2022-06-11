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

import com.ravenwolf.nnypd.actors.hero.HeroSkill;
import com.ravenwolf.nnypd.items.misc.TomeOfMasterySkill1;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndChooseSkill1 extends Window {

	private static final String TXT_MESSAGE	= "你准备好获得这份力量了吗？";
	private static final String TXT_DESC	= "这本书包含了一种特殊的力量，它将传递给阅读者，使他们能够运用特殊的技巧";
	private static final String TXT_YES	= "是的";
	private static final String TXT_CANCEL	= "稍后决定";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;

	private static SmartTexture icons;
	private static TextureFilm film;

	public WndChooseSkill1(final TomeOfMasterySkill1 tome, final HeroSkill skill) {

		super();

		icons = TextureCache.get(Assets.SKILLS);
		film = new TextureFilm(icons, 16, 16);

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		/*BitmapTextMultiline txtDesc = PixelScene.createMultiline( Utils.capitalize( TXT_DESC), 6 );
		txtDesc.maxWidth = WIDTH;
		txtDesc.x = titlebar.left();
		txtDesc.y = titlebar.bottom() + GAP;
		txtDesc.measure();
		add( txtDesc );*/
		RenderedTextMultiline txtDesc = PixelScene.renderMultiline( TXT_DESC, 6 );
		txtDesc.maxWidth(WIDTH);
		PixelScene.align(txtDesc);
		float x = titlebar.left();
		float y = titlebar.bottom() + GAP;
		txtDesc.setPos(x, y);
		add( txtDesc );

		/*BitmapText txtTitle = PixelScene.createText(  Utils.capitalize( skill.title()), 8);
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = WIDTH/2 -txtTitle.width()/2;
		txtTitle.y = txtDesc.y + txtDesc.height() + GAP;
		add( txtTitle );*/
		RenderedTextMultiline txtTitle = PixelScene.renderMultiline(skill.title(), 8);
		txtTitle.hardlight( Window.TITLE_COLOR );
		PixelScene.align(txtTitle);
		float x1 = WIDTH/2 -txtTitle.width()/2;
		float y1 = y + txtDesc.height() + GAP;
		txtTitle.setPos(x1, y1);
		add( txtTitle );

		Image icon = new Image( icons );
		icon.frame( film.get( skill.icon() ) );
		icon.y =  y1 + txtTitle.height() +GAP;
		icon.x =  WIDTH/2 -8;
		add( icon );

		Highlighter hl = new Highlighter( skill.desc() + "\n\n" + TXT_MESSAGE );
		
		/*BitmapTextMultiline normal = PixelScene.createMultiline( hl.text, 6 );
		normal.maxWidth = WIDTH;
		normal.measure();
		normal.x = titlebar.left();
		normal.y = icon.y + icon.height() + GAP;
		add( normal );*/
		RenderedTextMultiline normal = PixelScene.renderMultiline(skill.desc() + "\n\n" + TXT_MESSAGE, 6);
		normal.maxWidth(WIDTH);
		PixelScene.align(normal);
		float x2 = titlebar.left();
		float y2 = icon.y + icon.height() + GAP;
		normal.setPos(x2, y2);
		add(normal);

		/*if (hl.isHighlighted()) {
			normal.mask = hl.inverted();
			
			BitmapTextMultiline highlighted = PixelScene.createMultiline( hl.text, 6 );
			highlighted.maxWidth = normal.maxWidth;
			highlighted.measure();
			highlighted.x = normal.x;
			highlighted.y = normal.y;
			add( highlighted );
	
			highlighted.mask = hl.mask;
			highlighted.hardlight( TITLE_COLOR );
		}*/		
		RedButton btnWay1 = new RedButton( Utils.capitalize( TXT_YES ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( skill );
			}
		};
		btnWay1.setRect( 0, y2 + normal.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWay1 );
		

		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay1.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
}
