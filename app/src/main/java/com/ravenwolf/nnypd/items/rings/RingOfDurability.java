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
package com.ravenwolf.nnypd.items.rings;

public class RingOfDurability extends Ring {

	{
		name = "Ring of Durability";
        shortName = "Du";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Durability();
	}
	
	@Override
	public String desc() {
        return isTypeKnown() ?
            ( isCursed() && isIdentified() ? "Normally, this ring " : "This ring " ) +
                "decreases rate at which everything around it degrades with use. It will make your " +
                "wands, weapons, shields and armour last longer, make your ammunition break less often and " +
                "even allow you to use your repair tools more effectively." +
                ( isCursed() && isIdentified() ? " However, because this ring is cursed, its effects are reversed." : "" ) :
            super.desc();
	}
	
	public class Durability extends RingBuff {
        @Override
        public String desc() {
            return !isCursed() ?
                    "You feel how some kind of protective aura surrounds your equipment." :
                    "You feel how some kind of disruptive aura surrounds your equipment." ;
        }
	}
}
