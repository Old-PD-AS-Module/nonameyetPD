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
import com.ravenwolf.nnypd.actors.blobs.Darkness;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.watabou.noosa.audio.Sample;

public class ScrollOfDarkness extends Scroll {

    private static final String TXT_MESSAGE	= "你被一团暗无止境的阴影吞噬了！";

    {
        name = "黑暗卷轴";
        shortName = "Da";

        spellSprite = SpellSprite.SCROLL_DARKNESS;
        spellColour = SpellSprite.COLOUR_WILD;
        icon=12;
	}
	
	@Override
	protected void doRead() {

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.DARKNESS ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_GHOST );

        GameScene.add( Blob.seed( curUser.pos, 1000 * ( 110 + curUser.magicSkill() ) / 100, Darkness.class ) );

        GLog.i( TXT_MESSAGE );

        super.doRead();
	}
	
	@Override
	public String desc() {
        return
                "这张平淡无奇的纸张，一旦念出其上的文字就会唤出无底深渊之中的诡秘黑暗。这股黑暗深不可测，非利用法术不可看穿。\n\n效果持续时长取决于阅读者的魔能。";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 45 * quantity : super.price();
    }
}
