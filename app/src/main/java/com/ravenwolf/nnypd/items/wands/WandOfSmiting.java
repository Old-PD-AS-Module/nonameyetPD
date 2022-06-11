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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.HolyLight;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfSmiting extends WandCombat {

	{
//		name = "Wand of Smiting";
		name = "圣裁之杖";
		image = ItemSpriteSheet.WAND_SMITING;

		goThrough = false;
		hitChars = false;
	}


	@Override
	public int basePower() {
		return super.basePower() -4;
	}

	@Override
	protected void cursedProc(Hero hero){
		int dmg=hero.absorb( damageRoll())/2;
		hero.damage(dmg, this, Element.ENERGY);
		if (hero.isAlive() ) {
			BuffActive.addFromDamage(hero, Blinded.class, dmg);
			hero.sprite.emitter().start( Speck.factory( Speck.HOLY ), 0.05f, 3 );
		}
	}

	@Override
	protected void onZap( int cell ) {

		if( Level.solid[ cell ] ) {
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		}

		for ( int n : Level.NEIGHBOURS9 ){

			Char ch = Actor.findChar( cell + n );

			if( ch != null && ch != curUser ){

				int power = n == 0 ? damageRoll() : damageRoll() / 2 ;

				ch.damage( power, curUser, Element.ENERGY );

				if( ch.isMagical() ) {
					power += ( power / 2 + Random.Int( power % 2 + 1 ) );
				}

				if( Random.Int( 4 ) == 0 ){
					BuffActive.addFromDamage( ch, Blinded.class, power );
				}

				if( ch.sprite.visible ){
					ch.sprite.emitter().start( Speck.factory( Speck.HOLY ), 0.05f, n == 0 ? 4 : 2 );
				}
			}
		}

	}

	@Override
	protected void fx( int cell, Callback callback ) {
		if( Level.solid[ cell ] ) {
			cell = Ballistica.trace[ Ballistica.distance - 1 ];
		}

		if( Dungeon.visible[ cell ] ){

			Sample.INSTANCE.play( Assets.SND_LEVELUP, 1.0f, 1.0f, 0.5f );

			new Flare( 6, 24 ).color( SpellSprite.COLOUR_HOLY, true ).show( cell, 1f );
			HolyLight.createAtPos( cell );

		}

		Sample.INSTANCE.play( Assets.SND_ZAP );

		callback.call();
	}

	@Override
/*	public String desc() {
		return
				"This gilded piece of wood allows its user to channel and release bursts of hallowed " +
						"energy, harming and sometimes even blinding any wrongdoer caught in its area of " +
						"effect. Its effects are even stronger against undead or magical foes.";
	}*/
	public String desc() {
		return
				"这根金光闪闪的法杖能够引导和释放神圣能量，对指定范围内的敌人造成伤害甚至是失明。它对不死生物或魔法敌人的效果更加强大。";
	}

}
