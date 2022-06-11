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


import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Sanctuary;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.HolyLight;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;


public class CharmOfSanctuary extends WandUtility {

	{
		name = "神圣魔咒";
		hitChars = false;
		image = ItemSpriteSheet.CHARM_BLESS;
	}


	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		BuffActive.addFromDamage(hero, Tormented.class, damageRoll());
	}

	@Override
	protected void onZap( int cell ) {
		super.onZap(cell );
		if( Level.solid[ cell ] )
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		int dmg=damageRoll();
		for (int i : Level.NEIGHBOURS9) {
			int pos=cell+i;
			if (!Level.solid[pos])
				GameScene.add( Blob.seed( pos, dmg /2, Sanctuary.class ) );
		}
	}

	@Override
	protected void fx( int cell, Callback callback ) {

		if( Level.solid[ cell ] )
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		if( Dungeon.visible[ cell ] ){
			Sample.INSTANCE.play( Assets.SND_LEVELUP, 1.0f, 1.0f, 0.5f );
			HolyLight.createAtPos( cell );
		}
		Sample.INSTANCE.play( Assets.SND_ZAP );
		curUser.sprite.cast(cell,callback);
	}
	
	@Override
	public String desc() {
		return
				"这个魔咒可以制造出一片神圣的光明区域，并驱散一些不洁的魔法和怪物，而使用者则会受到圣光的庇护，提高自身的魔抗和物抗";
	}

	protected String getEffectDescription(int min , int max, boolean known){
		return  "这个魔咒" +(known? "":"(可能) ")+"会制造出_" + min/2 + "-" + max/2 + "回合的_圣光区域 ";
	}

}
