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
import com.ravenwolf.nnypd.items.misc.TomeOfMasterySkill;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.TextureFilm;

public class WndChooseSkill extends Window {

	private static final String TXT_MESSAGE	= "你要选择哪项技能";
	private static final String TXT_DESC	= "这本书包含了一种特殊的力量，它将传递给阅读者，使他们能够运用特殊的技巧。" +
			"\n\n 你要选择哪项技能?";
	private static final String TXT_CANCEL	= "稍后决定";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;

	private static SmartTexture icons;
	private static TextureFilm film;

	public WndChooseSkill(final TomeOfMasterySkill tome, final HeroSkill skill1, final HeroSkill skill2 ) {
		
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
		/*RenderedTextMultiline txtDesc = PixelScene.renderMultiline(TXT_DESC, 6);
		txtDesc.maxWidth(WIDTH);
		PixelScene.align(txtDesc);
		float x1 = titlebar.left();
		float y1 = titlebar.bottom() + GAP;
		txtDesc.setPos(x1, y1);
		add(txtDesc);*/

		RenderedText txtTitle = PixelScene.renderText( TXT_MESSAGE, 8 );
		PixelScene.align(txtTitle);
		txtTitle.x = titlebar.left()+ WIDTH/2 -txtTitle.width()/2;
		txtTitle.y = titlebar.bottom() + GAP;
		add( txtTitle );
		/*BitmapText txtTitle = PixelScene.createText( TXT_MESSAGE, 8 );
		txtTitle.measure();
		txtTitle.x = titlebar.left()+ WIDTH/2 -txtTitle.width()/2;
		txtTitle.y = titlebar.bottom() + GAP;
		add( txtTitle );*/

		//float y= txtDesc.y+txtDesc.height();
		float y = txtTitle.y+txtTitle.height();

		float length1=showSkill(skill1,titlebar.left() +GAP,y + GAP);

		float length2=showSkill(skill2,titlebar.left() +WIDTH/2 +GAP*2,y + GAP);
		//get max length
		if (length1<length2)
			y=length2;
		else
			y=length1;

		RedButton btnWay1 = new RedButton( Utils.capitalize( skill1.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( skill1 );
			}
		};
		btnWay1.setRect( 0, y  + GAP, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btnWay1 );
		
		RedButton btnWay2 = new RedButton( Utils.capitalize( skill2.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( skill2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}


	public float showSkill(HeroSkill skill, float x, float y){

		//BitmapText txtTitle = PixelScene.createText( Utils.capitalize( skill.title()), 8 );
		RenderedText txtTitle = PixelScene.renderText( Utils.capitalize( skill.title()), 8 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		//txtTitle.measure();
		PixelScene.align(txtTitle);
		//txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
		txtTitle.x = x+ WIDTH/4 -txtTitle.width()/2;
		txtTitle.y = y + GAP;
		add( txtTitle );

		Image icon;

		icon = new Image( icons );
		icon.frame( film.get( skill.icon() ) );
		icon.y =  txtTitle.y + txtTitle.height() +GAP;
		icon.x =  x+ WIDTH/4 -8;
		add( icon );

		Highlighter hl = new Highlighter( skill.desc() );

		/*BitmapTextMultiline normal = PixelScene.createMultiline( hl.text, 6 );
		normal.maxWidth = WIDTH/2- (int)GAP*2;
		normal.measure();
		normal.x = x;
		normal.y = icon.y + icon.height() +GAP;*/
		RenderedTextMultiline normal = PixelScene.renderMultiline( hl.text, 6 );
		normal.maxWidth(WIDTH/2- (int)GAP*2);
		PixelScene.align(normal);
		x = x;
		y = icon.y + icon.height() +GAP;
		normal.setPos(x, y);
		add( normal );

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

		//return normal.y + normal.height();
		return y + normal.height();

	}

}
