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
package com.ravenwolf.nnypd.items.wands;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.CorrosiveGas;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;


public class WandOfAcidSpray extends WandCombat {

	{
		name = "酸蚀法杖";
		hitChars = false;
		image = ItemSpriteSheet.WAND_ACIDSPRAY;
	}

	@Override
	protected void cursedProc(Hero hero){
		int dmg=hero.absorb( damageRoll(), true )/2;
		hero.damage(dmg, this, Element.ACID);
	}

	@Override
	public int basePower() {
		return super.basePower() -2;
	}

	@Override
	protected void onZap( int cell ) {
		if( Level.solid[ cell ] )
			cell = Ballistica.trace[ Ballistica.distance - 1 ];

		int dmg=damageRoll();
		Char ch = Actor.findChar(cell);
		if (ch != null) {
			ch.damage( ch.absorb(dmg ,true ), curUser, Element.ACID );
			GameScene.add( Blob.seed( cell, dmg * 8, CorrosiveGas.class ) );
		}else
			GameScene.add( Blob.seed( cell, dmg * 16, CorrosiveGas.class ) );

	}

	protected void fx( int cell, Callback callback ) {
		if( Level.solid[ cell ] )
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		MagicMissile.acid(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return
				"这个法杖邪恶的力量会释放出一团致命的酸液，在目标范围留下一片强烈的酸蚀性气体，迅速溶解其中的任何单位";
	}
}
