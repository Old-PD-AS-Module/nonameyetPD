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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Sunlight;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.watabou.noosa.audio.Sample;

public class ScrollOfSunlight extends Scroll {

	private static final String TXT_MESSAGE	= "整片区域瞬间被沐浴在温暖的阳光之中。";

	{
		name = "阳光卷轴";
        shortName = "Su";

        spellSprite = SpellSprite.SCROLL_SUNLIGHT;
        spellColour = SpellSprite.COLOUR_HOLY;
        icon=15;
	}
	
	@Override
	protected void doRead() {

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_LULLABY );

        GameScene.add( Blob.seed( curUser.pos, 250 * ( 110 + curUser.magicSkill() ) / 50, Sunlight.class ) );

        GLog.i( TXT_MESSAGE );

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
				"阅读这张卷轴将使整片区域被温暖的阳光照耀。在这深不见底的地牢中，阳光所代表的不仅仅只是光源。所有被" +
						"照射的单位都能够在极短时间内恢复活力。而那些不洁的生物，则会受到圣光洗礼并降低其战斗能力。" +
						"\n\n效果持续时间取决于阅读者的魔能属性。";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 60 * quantity : super.price();
	}
}
