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
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.sprites.StatueSprite;

public class Statue extends MobPrecise {

    public Statue() {

        super( Dungeon.depth*3/4 + 2 /*Dungeon.depth + 1 */ );

        name = "活化石像";
        spriteClass = StatueSprite.class;

        minDamage += tier;
        maxDamage += tier;

        //HP = HT += Random.IntRange(4, 8);

        armorClass += tier * 2;

        state = PASSIVE;

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
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

		if (state == PASSIVE) {
            notice();
			state = HUNTING;
		}
		
		super.damage( dmg, src, type );
	}
	
	@Override
	public void beckon( int cell ) {
//        if (state == PASSIVE) {
//            state = HUNTING;
//        }
        // do nothing
	}


    @Override
    protected boolean act() {

        if( state == PASSIVE ) {
            if ( enemy != null && Level.adjacent( pos, enemy.pos ) && enemy.invisible <= 0) {
                activate();
                return true;
            }
        }

        return super.act();
    }

    @Override
    public int minAC() {
        return super.minAC()+2;
    }

    public void activate() {

        state = HUNTING;
        enemySeen = true;

        GLog.w( "这座石像被激活了！" );

        spend( TICK );
    }

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
        return
                "你以为这只是地牢里的一尊丑陋雕像，但它发出红光的眼睛让你放弃了刚刚的想法。它通常不会行动，但是一旦被激活，这些石像几乎势不可挡，它们对物理和魔法伤害都有这很高的抗性。这些活化石像是一种非常可靠的守护者，如果拿来装饰花园应该会很酷吧。";
    }
}
