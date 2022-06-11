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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Controlled;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class CharmOfDomination extends WandUtility{

	{
		name = "支配魔咒";
		image = ItemSpriteSheet.CHARM_DOMINATION;
	}

	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		BuffActive.addFromDamage(hero, Charmed.class, damageRoll()*2);
	}

	public int basePower(){
		return super.basePower()+4;
	}

	@Override
	protected void onZap( int cell ) {
		super.onZap(cell );
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			Controlled buff = BuffActive.addFromDamage(ch, Controlled.class, damageRoll()*6 );
			if( buff != null ) {
				buff.object = curUser.id();
			}
		} else {
			GLog.i( "什么也没有发生" );
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.purpleLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
				"释放这个魔咒会催眠命中的敌人，使你暂时支配它的意志，" +
						"支配效果内如果你再次攻击这个敌人，那么它就会脱离你的控制。不过支配的效果依旧会削弱它反抗的能力";
	}

	protected String getEffectDescription(int min , int max, boolean known){
		return  "这个魔咒" +(known? "":"(可能) ")+"会支配敌人_" + min + "-" + max + "回合_";
	}
}
