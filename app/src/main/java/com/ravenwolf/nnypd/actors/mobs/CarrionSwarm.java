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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.visuals.sprites.SwarmSprite;

public class CarrionSwarm extends MobEvasive {

    public CarrionSwarm() {

        super( 5 );

        name = "苍蝇";
        spriteClass = SwarmSprite.class;

        flying = true;

        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

	}


    @Override
    public String description() {
        return
                "这些苍蝇正在烦人地嗡嗡作响，这些肮脏的敌人对任何可食用的东西都有不可思议的嗅觉。";
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked  ) {

        if( !blocked && damage > 0 ){
            Satiety hunger = enemy.buff( Satiety.class );
            if( hunger != null ){
                hunger.decrease( Satiety.POINT * 10 );
            }
        }

        return damage;
    }
}
