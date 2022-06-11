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
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.SkeletonSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Skeleton extends MobPrecise {

//	private static final String TXT_HERO_KILLED = "You were killed by the explosion of bones...";

    public Skeleton() {

        super( 6 );

        name = "骷髅";
        spriteClass = SkeletonSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;

        armorClass -= tier;
        accuracy -= tier;
        dexterity -= tier;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

	}

    @Override
    public String getTribe() {
        return TRIBE_UNDEAD;
    }

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Withered.class, damage );
        }

        return damage;
    }

    @Override
    public float awareness(){
        return super.awareness() * 0.8f;
    }

    @Override
    public String description() {
        return
                "这些骷髅是由一些不幸的冒险家和地牢居民的尸骨组成的，它被地牢深处散发的邪恶魔法所唤起，要注意的是，在与这种邪恶存在近距离接触时，它们的攻击会削弱受害者的生命力。";
    }

	@Override
	public void die( Object cause, Element dmg) {

		super.die( cause,dmg );

		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
	}

}
