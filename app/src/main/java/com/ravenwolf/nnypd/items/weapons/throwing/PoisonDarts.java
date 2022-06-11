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
package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class PoisonDarts extends ThrowingWeaponSpecial {

	{
		name = "毒液飞镖";
		image = ItemSpriteSheet.THROWING_DART;
	}

	public PoisonDarts() {
		this( 1 );
	}

	public PoisonDarts(int number) {
        super( 1 );
		quantity = number;
	}

	public boolean stick(Char enemy){
		return !enemy.isSolid() && !enemy.isEthereal();
	}

    @Override
    public int proc( Char attacker, Char defender, int damage ) {

		damage=super.proc(attacker, defender, damage);

		//BuffActive.addFromDamage(defender, Poisoned.class, defender.totalHealthValue());
		if (defender.hasBuff(Poisoned.class))
			BuffActive.add(defender, Poisoned.class, 2);
		else
			BuffActive.add(defender, Poisoned.class, 4);
		return damage;
    }

	@Override
	public int penaltyBase() {
		return 0;
	}

	protected int getDamageSTBonus(Hero hero){
		int exStr = (hero.STR() - strShown( true ))/2;

		if (exStr > 0) {
			return Random.IntRange( 0, exStr );
		}
		return 0;
	}

	@Override
	public float breakingRateWhenShot() {
		return 0.20f;

	}

	
	@Override
	public String desc() {
		return
				"这些飞镖上涂抹着某种致命的毒素，使被命中的目标进入中毒的状态。";
	}
}
