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
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.Lightning;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.sprites.WarlockSprite;
import com.watabou.utils.Bundle;

public class DwarfWarlock extends MobCaster {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public DwarfWarlock() {

        super( 15 );

        name = "矮人术士";
		spriteClass = WarlockSprite.class;
		
		loot = Gold.class;
        lootChance = 0.25f;


        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

	}

    @Override
    public boolean act() {

        if( !enemySeen )
            charged = false;

        return super.act();

    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !Level.adjacent( pos, enemy.pos ) && !charged ) {

            charged = true;

            if( Dungeon.visible[ pos ] ) {
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_BLUE, 20);
            }

            spend( attackDelay() );

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }


    @Override
    protected void onRangedAttack( int cell ) {

        int[] points = new int[2];

        points[0] = pos;
        points[1] = cell;

        sprite.parent.add( new Lightning( points, 2, null ) );

        onCastComplete();

        super.onRangedAttack(cell);

    }

    @Override
	public boolean cast( Char enemy ) {

        return castBolt(enemy,damageRoll(),true,Element.SHOCK);

	}

	@Override
	public String description() {
        return
                "当矮人的兴趣从工程建设转向奥秘学术时，术士们开始在城市中掌权。它们从元素魔法起步，但很快就开始研究恶魔学和死灵术。";
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
    }
}
