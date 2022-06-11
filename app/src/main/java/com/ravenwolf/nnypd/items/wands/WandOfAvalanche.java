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
package com.ravenwolf.nnypd.items.wands;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.BArray;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfAvalanche extends WandCombat {

	{
        name = "落石法杖";
        image = ItemSpriteSheet.WAND_BLAST_WAVE;
	}


    @Override
    public int basePower() {
        return super.basePower() +12;
    }

    protected void cursedProc(Hero hero){

        int dmg = hero.absorb( damageRoll() )/2;
        hero.sprite.flash();
        hero.damage(dmg, curUser, Element.PHYSICAL);
        if (hero.isAlive() ) {
            BuffActive.addFromDamage(hero, Dazed.class, dmg);
        }
        CellEmitter.get(hero.pos).start(Speck.factory(Speck.ROCK), 0.1f, 4);
    }

    @Override
	protected void onZap( int cell ) {

        int size = 2;

        Sample.INSTANCE.play( Assets.SND_ROCKS );
        Camera.main.shake(3, 0.07f * (3 + size));

        PathFinder.buildDistanceMap( cell, BArray.not( Level.solid, null ), size );
        Ballistica.distance = Math.min( Ballistica.distance, 2 );

		for (int i=0; i < Level.LENGTH; i++) {
			
			int d = PathFinder.distance[i];
			
			if ( d < Integer.MAX_VALUE ) {

                boolean wall = false;

                for(int n : Level.NEIGHBOURS4) {
                    if( Level.solid[ i + n ] ) {
                        wall = true;
                    }
                }

                if( wall || Random.Int( d * 2 + 1 ) == 0) {

                    Char ch = Actor.findChar(i);
                    if (ch != null ) {

                        int dmg = ch.absorb( damageRoll() / ( d + 1 ));

                        ch.sprite.flash();

                        ch.damage(dmg, curUser, Element.PHYSICAL);

                        if (ch.isAlive() ) {
                            BuffActive.addFromDamage(ch, Dazed.class, dmg);
                        }

                    }

                    Heap heap = Dungeon.level.heaps.get( i );
                    if (heap != null) {
                        heap.shatter( "法杖制造的落石" );
                    }

                    Dungeon.level.press(i, null);

                    if (Dungeon.visible[i])
                        CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.1f, 3 + (size - d));
                }
			}
		}

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= 6 ) {
                mob.beckon( cell );
            }
        }

	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.earth( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
        return
                "当这个法杖释放的能量击中墙壁(或任何其他坚固的的障碍)时，它会造成一定范围内落石，伤害并眩晕范围内的所有生物。";
    }
}
