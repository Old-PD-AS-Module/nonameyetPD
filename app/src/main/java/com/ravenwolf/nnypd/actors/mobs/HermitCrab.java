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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.special.Exposed;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.mobs.npcs.Ghost;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.CrabKingSprite;

public class HermitCrab extends MobHealthy {

    public HermitCrab() {

        super( 6 );

		name = "寄居蟹";
		spriteClass = CrabKingSprite.class;
		
		loot = new MysteryMeat();
		lootChance = 1f;

		resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
		resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
		resistances.put( Element.Shock.class, Element.Resist.PARTIAL );

		resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
		resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
		resistances.put( Element.Frost.class, Element.Resist.PARTIAL );

		resistances.put( Element.Body.class, Element.Resist.PARTIAL );

	}

	@Override
	public String getTribe() {
		return TRIBE_ACUATIC;
	}

	@Override
	public int guardStrength(boolean ranged){
    	return 6+tier*3;
	}

	@Override
	public boolean hasShield() {
		return true;
	}


	@Override
	protected boolean act() {

		Guard guarded = buff( Guard.class );
		if( guarded==null && state == HUNTING && enemySeen && enemy!=null && enemy.buff( Exposed.class ) ==null
				&& Level.distance( pos, enemy.pos ) <= 2
				&& detected( enemy )
				) {

			//Buff.affect( this, Guard.class).reset(5);
			BuffActive.add(this, Guard.class, 8, true);
			spend(TICK);
			return true;

		}

		return super.act();
	}


	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}


	@Override
	public String description() {

		return "这些螃蟹身上覆盖着一层厚厚的壳，它们比普通的螃蟹速度慢很多，不过比它们拥有更高的防御，它们的壳可以为它们抵抗大多数的攻击。";
	}
}
