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

import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Miasma;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.DoomSkull;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class CharmOfDecay extends WandUtility {

	{
		name = "衰变魔咒";
		image = ItemSpriteSheet.CHARM_BONE;
	}

	@Override
	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		GameScene.add( Blob.seed( hero.pos, damageRoll()*6, Miasma.class ) );
	}

	@Override
	protected void onZap( int cell ) {
		super.onZap(cell );
		if( Level.solid[ cell ] ) {
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		}
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			BuffActive.addFromDamage(ch, Decay.class, damageRoll()*4 );
			Sample.INSTANCE.play(Assets.SND_DEGRADE);
			DoomSkull.createAtChar(ch);

		} else
			GameScene.add( Blob.seed( cell, damageRoll()*10, Miasma.class ) );
	}


	protected void fx( int cell, Callback callback ) {
		if( Level.solid[ cell ] ) {
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		}
		MagicMissile.miasma( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}


	@Override
	public String desc() {
		return 	"这个魔咒由一些骸骨和紫水晶制成，它会释放出衰变的能量，命中敌人后会持续的降低它们的生命，并且削弱它们的伤害和抗性。当对着一个空地释放时，则会造成一定范围内的恶毒气体";
	}

	protected String getEffectDescription(int min , int max, boolean known){
		return  "这个魔咒" +(known? "":"(可能) ")+"会造成_" + min + "-" + max + "_点的衰变伤害";
	}
}
