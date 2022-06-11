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
import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.ui.ItemSlot;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;

public class WndItem extends Window {

	private static final float BUTTON_WIDTH		= 36;
	private static final float BUTTON_HEIGHT	= 16;
	
	private static final float GAP	= 2;
	
	private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 240;

    //private BitmapTextMultiline normal;
    //private BitmapTextMultiline highlighted;
	private RenderedTextMultiline normal;
	private RenderedTextMultiline highlighted;
	
	public WndItem( final WndBag owner, final Item item ) {	
		
		super();

        int width = NoNameYetPixelDungeon.landscape() ? WIDTH_L : WIDTH_P ;

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), item.glowing() ) );
		titlebar.label( Utils.capitalize( item.toString() ) );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		if (item.isIdentified() && !item.isCursed()) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.isIdentified() && item.isCursed()) {
			titlebar.color( ItemSlot.DEGRADED );
		}	
		
//		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
//		info.maxWidth = WIDTH;
//		info.measure();
//		info.x = titlebar.left();
//		info.y = titlebar.bottom() + GAP;
//		add( info );

        Highlighter hl = new Highlighter( item.info() );

        /*normal = PixelScene.createMultiline( hl.text, 6 );
        normal.maxWidth = width;
        normal.measure();
        normal.x = titlebar.left();
        normal.y = titlebar.bottom() + GAP;*/
		normal = PixelScene.renderMultiline( item.info(), 5 );
        normal.maxWidth(width);
        PixelScene.align(normal);
        float wx = titlebar.left();
        float wy = titlebar.bottom() + GAP;
        normal.setPos(wx,wy);
        add( normal );

        /*if (hl.isHighlighted()) {
            normal.mask = hl.inverted();

            highlighted = PixelScene.createMultiline( hl.text, 6 );
            highlighted.maxWidth = normal.maxWidth;
            highlighted.measure();
            highlighted.x = normal.x;
            highlighted.y = normal.y;
            add( highlighted );

            highlighted.mask = hl.mask;
            highlighted.hardlight( TITLE_COLOR );
        }*/
	
		float y = (int)(wy + normal.height()) + GAP;
//		float y = info.y + info.height() + GAP;
		float x = 0;
		
		if (Dungeon.hero.isAlive() && owner != null) {
			for (final String action:item.actions( Dungeon.hero )) {
				
				RedButton btn = new RedButton( action ) {
					@Override
					protected void onClick() {
						item.execute( Dungeon.hero, action );
						hide();
						owner.hide();
					}
				};
				btn.setSize( Math.max( BUTTON_WIDTH, btn.reqWidth() ), BUTTON_HEIGHT );
				if (x + btn.width() > width) {
					x = 0;
					y += BUTTON_HEIGHT + GAP;
				}
				btn.setPos( x, y );
				add( btn );
				
				if (action == item.quickAction()) {
					btn.textColor( TITLE_COLOR );
				}
				
				x += btn.width() + GAP;
			}
		}
		
		resize( width, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}
}
