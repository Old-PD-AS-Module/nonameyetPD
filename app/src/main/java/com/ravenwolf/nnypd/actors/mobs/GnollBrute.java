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
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.BruteSprite;
import com.watabou.utils.Random;

public class GnollBrute extends MobPrecise {


    public GnollBrute() {

        super( 10 );

        name = "持盾豺狼";
		spriteClass = BruteSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;

        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
	}

    @Override
    public String getTribe() {
        return TRIBE_GNOLL;
    }

    @Override
    public int minAC() {
        return super.minAC()+1;
    }

    @Override
    public int guardStrength(boolean ranged){
        //better block chance against ranged attacks
        if (ranged)
            return 10+tier*4;
        return 7+tier*3;
    }


    @Override
    public boolean hasShield() {
        return true;
    }

    @Override
    protected boolean act() {

        Guard guarded = buff( Guard.class );
        if( guarded==null && enemySeen && enemy!=null && state == HUNTING
                && (Level.distance( pos, enemy.pos ) > 2 || (HP<HT/2 && enemy.buff( Exposed.class ) ==null && Random.Int(2)==0))
                && detected( enemy )
                ) {
            BuffActive.add(this, Guard.class, 6, true);
            spend(TICK);
            return true;

        }

        return super.act();
    }


    @Override
    public String description() {
        return
                "持盾豺狼是豺狼人中体型最庞大，力量最强壮且生命力最顽强的精英。虽然智力不高，但它们可以进行非常凶猛的近战攻击。还可以用盾牌有效地阻挡攻击，特别是远程攻击。";
    }
}
