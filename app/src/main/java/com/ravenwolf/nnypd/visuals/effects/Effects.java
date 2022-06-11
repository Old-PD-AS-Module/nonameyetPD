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
package com.ravenwolf.nnypd.visuals.effects;

import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.Image;

public class Effects {

	public enum  Type {
		RIPPLE,
		LIGHTNING,
		WOUND,
		RAY,
		CHAIN,
		DRAIN,
        TONGUE,
        DOOM,
        CROSS,
        JADE_SHOCK
	}

    public static Image get( Type type ) {
		Image icon = new Image( Assets.EFFECTS );
		switch (type) {
            case RIPPLE:
                icon.frame( icon.texture.uvRect( 0, 0, 16, 16 ) );
                break;
            case CHAIN:
                icon.frame( icon.texture.uvRect( 0, 16, 8, 24 ) );
                break;
            case TONGUE:
                icon.frame( icon.texture.uvRect( 9, 18, 14, 21 ) );
                break;
            case LIGHTNING:
                icon.frame( icon.texture.uvRect( 16, 0, 32, 8 ) );
                break;
            case WOUND:
                icon.frame( icon.texture.uvRect( 16, 8, 32, 16 ) );
                break;
            case RAY:
                icon.frame( icon.texture.uvRect( 16, 16, 32, 24 ) );
                break;
            case DRAIN:
                icon.frame( icon.texture.uvRect( 16, 24, 32, 32 ) );
                break;
            case DOOM:
                icon.frame( icon.texture.uvRect( 32, 0, 48, 16 ) );
                break;
            case CROSS:
                icon.frame( icon.texture.uvRect( 48, 0, 64, 16 ) );
                break;
            case JADE_SHOCK:
                icon.frame( icon.texture.uvRect( 48, 16, 64, 24 ) );
                break;
		}


		return icon;
	}
}
