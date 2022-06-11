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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.visuals.sprites.StatueSprite;

public class TestStatue extends MobPrecise {

    public TestStatue() {

        super( Dungeon.depth*3/4 + 2 /*Dungeon.depth + 1 */ );

        name = "animated statue";
        spriteClass = StatueSprite.class;

        minDamage += tier;
        maxDamage += tier;

        HP = HT += 200;

        armorClass += tier * 2;

        state = PASSIVE;
        enemySeen=true;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );

        resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

    }
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

		super.damage( dmg, src, type );
        state = PASSIVE;
	}
	
	@Override
	public void beckon( int cell ) {
//        if (state == PASSIVE) {
//            state = HUNTING;
//        }
        // do nothing
	}




    @Override
    public int minAC() {
        return super.minAC()+2;
    }


	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return
			"You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
            "Usually passive, these stony juggernauts are almost unstoppable once provoked, being very resistant to both " +
            "physical and magical damage. Besides being extremely reliable guardians, these automatons also may serve as a " +
            "pretty cool garden decorations.";
	}
}
