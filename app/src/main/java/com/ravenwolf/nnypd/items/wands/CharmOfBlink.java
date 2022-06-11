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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;


public class CharmOfBlink extends WandUtility {


	{
		name = "闪烁魔咒";
        hitChars = false;
		image = ItemSpriteSheet.CHARM_BLINK;
	}

	private int getBlinkDistance(){
		return curCharges+1;
	}


	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		int dmg=hero.absorb( damageRoll(), true )/2;
		hero.damage(dmg, this, Element.ENERGY);
		BuffActive.add(hero, Dazed.class, dmg);
	}

	@Override
	protected void onZap( int cell ) {

		super.onZap(cell );
		/*
		new Flare(8, 28, true, true,  Flare.NO_ANGLE).color(0xDDAA55, true).show(cell, 0.6f);
		Char ch;
		for (int n : Level.NEIGHBOURS8) {
			if ((ch = Actor.findChar(cell+n)) != null && ch !=Dungeon.hero ) {
				hitedEnemies.add(ch);
				BuffActive.addFromDamage(ch, Blinded.class, damageRoll() );
				ch.damage(ch.absorb(damageRoll() , true), this, Element.ENERGY);
				CellEmitter.center( cell+n ).burst( PurpleParticle.BURST, Random.IntRange( 3, 5 ) );
			}
		}
		*/
		cell =getDestinationPos( getBlinkDistance(),cell);

		Buff.detach( curUser, Ensnared.class );
		//Dungeon.hero.spend( -1 / TIME_TO_ZAP );
		Ballistica.cast(curUser.pos, cell, goThrough, hitChars);
		for (int i=1; i < Ballistica.distance; i++) {
			int c = Ballistica.trace[i];
			Char ch = Actor.findChar( c );
			if ( ch != null ) {
				ch.damage( ch.absorb( damageRoll(), true ), curUser, Element.ENERGY );
				CellEmitter.center( c ).start( Speck.factory( Speck.LIGHT ), 0.1f, 3 );
			}
		}
		
		curUser.sprite.visible = true;
		appear( Dungeon.hero, cell );
		Dungeon.observe();
	}

	private int getDestinationPos(int distance, int cell){
		int correction=1;
		if (Ballistica.distance > distance + 1 ) {
			cell = Ballistica.trace[ distance ];
		} else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - ++correction];
		}
		while (Actor.findChar( cell )!= null && correction<Ballistica.distance){
			cell = Ballistica.trace[Ballistica.distance - ++correction];
		}
		return cell;
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {

		Ballistica.cast(curUser.pos, cell, goThrough, hitChars);
		cell =getDestinationPos( getBlinkDistance(),cell);

		MagicMissile.whiteLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		curUser.sprite.visible = false;
	}

	public static void appear( Char ch, int pos ) {
		
		ch.sprite.interruptMotion();
		
		ch.move( pos );
		ch.sprite.place( pos );
		
		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}
		
		ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return
				"这个魔咒由两个扭曲的魔法枝干和闪烁水晶组成，使用它的力量可以使你同化成具有穿梭性的元素，" +
						"允许你快速向前穿梭，并伤害路上穿透过的敌人，不过一些墙壁等障碍物无法穿透。";

	}

	protected String getEffectDescription(int min , int max, boolean known){
		int reach=maxCharges(known? bonus : 0)*3;
		return  "这个魔咒的_最大闪烁范围" +(known? "":"(可能) ")+"是"+reach+"_，并且会造成 _" + min + "-" + max + "点伤害_";
	}
}
