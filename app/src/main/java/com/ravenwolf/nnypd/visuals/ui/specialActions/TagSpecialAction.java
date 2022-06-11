/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
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
package com.ravenwolf.nnypd.visuals.ui.specialActions;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.ui.Tag;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class TagSpecialAction extends Tag {


	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	protected static final int ICON_SIZE=16;
	public static TagSpecialAction instance;

	protected static SmartTexture icons;
	protected static TextureFilm film;

	Image icon;

	protected ColorBlock cooldownBar;
	float cooldownRatio=0f;

	private boolean enabled = true;


	public TagSpecialAction() {
		super( 0x7C8072 );
		instance = this;
		setSize( WIDTH, HEIGHT );
		cooldownBar = new ColorBlock( ICON_SIZE, ICON_SIZE, 0xAA222222 );

		icons = TextureCache.get(Assets.SKILLS);
		film = new TextureFilm(icons, ICON_SIZE, ICON_SIZE);
	}


	public void setIcon(int img) {
		icon = new Image( icons );
		icon.frame( film.get( img ) );
		layout();
	}

	public boolean enabled( ) {
		return enabled;
	}

	private void enable( boolean value ) {
		enabled = value;
		if (icon != null) {
			icon.alpha( value ? ENABLED : DISABLED );
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}


	@Override
	protected void createChildren() {
		super.createChildren();
	}

	@Override
	protected void layout() {
		super.layout();

		bg.scale.x = -1.0f;
		bg.x += bg.width;

		if (icon != null){
			icon.x = x -1 + (width - icon.width()) / 2;
			//icon.x = x +1 + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
			if (!members.contains(icon))
				add(icon);
			if (cooldownBar!=null){
				cooldownBar.x = icon.x;
				cooldownBar.y = icon.y;
				if (!members.contains(cooldownBar))
					add(cooldownBar);
			}
		}

	}

	@Override
	public void update() {
		super.update();
		cooldownBar.size(ICON_SIZE ,ICON_SIZE*cooldownRatio);
		if (Dungeon.hero.isAlive() && cooldownRatio==0) {
			enable( Dungeon.hero.ready );
		} else {
			enable( false );
		}

	}


}
