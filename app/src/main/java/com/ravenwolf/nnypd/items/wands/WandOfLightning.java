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
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Lightning;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfLightning extends WandCombat {

	{
		name = "雷霆法杖";
		image = ItemSpriteSheet.WAND_LIGHTNING;
	}

	private ArrayList<Char> affected = new ArrayList<Char>();
	
	private int[] points = new int[20];
	private int nPoints;

	@Override
	protected void cursedProc(Hero hero){
		int dmg=damageRoll()/2;
		hero.damage(dmg, this, Element.SHOCK);
		CellEmitter.center( hero.pos ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
	}

	@Override
	public int basePower() {
		return super.basePower() -4;
	}

	@Override
	protected void onZap( int cell ) {
		// Everything is processed in fx() method
//		if (!curUser.isAlive()) {
//			Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
//			GLog.n( "You killed yourself with your own Wand of Lightning..." );
//		}
	}
	
	private void hit( Char ch, int power ) {
		
		if (power < 1) {
			return;
		}

        ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, power / 2 + 1 );
        ch.sprite.flash();

        points[nPoints++] = ch.pos;
        affected.add(ch);

		ch.damage(power, curUser, Element.SHOCK);

		HashSet<Char> ns = new HashSet<Char>();

		for (int i : Level.NEIGHBOURS8) {
			Char n = Actor.findChar(ch.pos + i);
			if (n != null && !affected.contains(n)) {
				ns.add(n);
			}
		}

		//check possible targets at two range
		for (int i : Level.NEIGHBOURS16) {
			try {
				//Search range extended for enemies standing in water, hero excluded
				if (Level.water[ch.pos + i] && !ch.flying) {
					Char n = Actor.findChar(ch.pos + i);
					//has path to hit the character (no wall or other mob between)
					if (n != null && n!=curUser && !affected.contains(n) && n.pos == Ballistica.cast(ch.pos, n.pos, false, true))
						ns.add(n);
				}

			}catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
		}

		if (ns.size() > 0) {
			hit(Random.element(ns), Random.NormalIntRange(power / 2, power));
		}

	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		
		nPoints = 0;
		points[nPoints++] = Dungeon.hero.pos;

		Char ch = Actor.findChar( cell );
		if (ch != null) {
			
			affected.clear();
			affected.add(Dungeon.hero);
			hit( ch, damageRoll() );

		} else {

			points[nPoints++] = cell;
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 5 ) );
			
		}
		curUser.sprite.parent.add( new Lightning( points, nPoints, callback ) );
	}
	
	@Override
	public String desc() {
		return
				"这个法杖可以释放出致命的电弧，而且可以对临近的生物造成连锁的伤害";
	}
}
