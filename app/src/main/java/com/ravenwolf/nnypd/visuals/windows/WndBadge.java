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

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.effects.BadgeBanner;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.noosa.Image;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;

public class WndBadge extends Window {
	
	private static final int WIDTH = 120;
	private static final int MARGIN = 4;
	
	public WndBadge( Badges.Badge badge ) {
		
		super();
		
		Image icon = BadgeBanner.image( badge.image );
		icon.scale.set( 2 );
		add( icon );
		
		/*BitmapTextMultiline info = PixelScene.createMultiline( badge.description, 8 );
		info.maxWidth = WIDTH - MARGIN * 2;
		info.measure();*/
		RenderedTextMultiline info = PixelScene.renderMultiline( badge.description, 6 );
		info.maxWidth(WIDTH - MARGIN * 2);
		
		float w = Math.max( icon.width(), info.width() ) + MARGIN * 2;
		
		icon.x = (w - icon.width()) / 2;
		icon.y = MARGIN;
		
		float pos = icon.y + icon.height() + MARGIN;
		/*for (BitmapText line : info.new LineSplitter().split()) {
			line.measure();
			line.x = PixelScene.align( (w - line.width()) / 2 );
			line.y = PixelScene.align( pos );
			add( line );
			
			pos += line.height(); 
		}*/
		info.setPos((w - info.width()) / 2 ,pos);
		add(info);
		pos += info.height();
		PixelScene.align(info);

		resize( (int)w, (int)(pos + MARGIN) );
		
		BadgeBanner.highlight( icon, badge.image );
	}
}
