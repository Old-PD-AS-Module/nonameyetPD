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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.BatSprite;

public class VampireBat extends MobEvasive {

    public VampireBat() {

        super( 9 );

        name = "吸血蝙蝠";
        spriteClass = BatSprite.class;

        flying = true;

//		loot = new PotionOfHealing();
//		lootChance = 0.125f;

        baseSpeed = 2f;
	}

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
            && Level.distance( pos, Dungeon.hero.pos ) <= 2
            && detected( Dungeon.hero )
        ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        damage=super.attackProc(enemy,damage,blocked);
        if ( !blocked && isAlive() ) {

            int healed=Element.Resist.doResist( enemy, Element.BODY,damage )/2;

            if (healed > 0) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }
		return damage;
	}

	@Override
	public String description() {
        return
                "这些迅捷而顽强的洞穴生物每次成功的攻击都会补充它的生命。所以导致它能够对抗更强大的对手，";
    }
}
