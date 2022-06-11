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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.mobs.MobHealthy;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Bolas extends ThrowingWeaponSpecial {

	{
		name = "流星锤";
		image = ItemSpriteSheet.HUNTING_BOLAS;
	}

	public Bolas() {
		this( 1 );
	}

	public Bolas(int number) {
        super( 2 );
		quantity = number;
	}

	public boolean stick(Char enemy){
		return !enemy.isEthereal();
	}

    @Override
    public int  proc( Char attacker, Char defender, int damage ) {
		damage= super.proc(attacker, defender, damage);

		int duration=1;
		if(!defender.hasBuff(Ensnared.class))
			duration= defender instanceof MobHealthy? 2 : 3 ;

		BuffActive.add(defender, Ensnared.class, duration );
		//BuffActive.addFromDamage(defender, Ensnared.class, damage * 2 );
        return  damage;
    }
	
	@Override
	public String desc() {
		return
				"流星锤通常用于狩猎，其伤害较低，但是能够捆绑住目标。使其在一定时间内无法移动。这种武器无法对大型目标造成有效的影响";
	}
}
