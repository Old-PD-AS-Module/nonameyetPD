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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.visuals.sprites.ScorpionSprite;
import com.watabou.utils.Random;

public class CaveScorpion extends MobHealthy {

    public CaveScorpion() {

        super( 12 );

        name = "巨型毒蝎";
		spriteClass = ScorpionSprite.class;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

	}

    @Override
    public String getTribe() {
        return TRIBE_BEAST;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Corrosion.class, damage );
        }

        return damage;
    }
/*
    @Override
	public void die( Object cause, Element dmg ) {

        GameScene.add(Blob.seed(pos, 50, CorrosiveGas.class));

        super.die(cause, dmg);
    }
*/
	@Override
	public String description() {
        return
                "这种巨大的蝎子生物由于能够通过尾刺注入酸液，所以冒险者们的威胁非常巨大。";
    }

}
