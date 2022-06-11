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
package com.ravenwolf.nnypd.actors.mobs.npcs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Rejuvenation;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class NPCSupplier extends NPC {


    protected int threatened = 0;
    protected boolean seenBefore = false;
    public int stock = 0;

    protected abstract Journal.Feature feature();
    protected abstract String greetingsText();
    protected abstract String[][] lines();
	
	@Override
	protected boolean act() {

        if( noticed ) {
            noticed = false;
        }

        if (!seenBefore && Dungeon.visible[pos]) {
            Journal.add(  feature() );
            seenBefore = true;
            greetings();
        }

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}

    @Override
    public boolean invulnerable(){
        return true;
    }

    protected void tell( String text ) {
        GameScene.show( new WndQuest( this, text ) );
    }

	@Override
    public void damage( int dmg, Object src, Element type ) {
        react();
	}

	@Override
    public boolean add( Buff buff ) {
        if( !( buff instanceof Rejuvenation ) && !( buff instanceof PinCushion)) {
            react();
        }

        return false;
    }

    protected void greetings() {
        yell(Utils.format(greetingsText()));
    }

    protected void react() {

        if( threatened < lines().length ) {
            yell(lines()[threatened][Random.Int(lines()[threatened].length)]);
        }
        if( threatened >= 2 ) {
            runAway();
        }
        threatened++;
    }

    public void reduceStock(){
        stock--;
        if ( outOfStock() )
            Journal.remove( feature() );
    }

    public boolean outOfStock(){
        return stock < 1 ;
    }

    public void runAway() {
        destroy();
        sprite.killAndErase();
        Journal.remove( feature());
    }

    @Override
    public boolean immovable() {
        return true;
    }
	
	@Override
	public boolean reset() {
		return true;
	}


    private static final String SEENBEFORE		= "seenbefore";
    private static final String THREATENED		= "threatened";
    private static final String STOCK		    = "stock";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( SEENBEFORE, seenBefore );
        bundle.put( THREATENED, threatened );
        bundle.put( STOCK, stock );
    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean( SEENBEFORE );
        threatened = bundle.getInt( THREATENED );
        stock = bundle.getInt( STOCK );
    }
}
