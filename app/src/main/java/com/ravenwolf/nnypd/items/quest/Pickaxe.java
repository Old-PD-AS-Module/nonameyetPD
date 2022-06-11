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
package com.ravenwolf.nnypd.items.quest;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Pickaxe extends Item {

	public static final String AC_MINE	= "挖掘";
	
	public static final float TIME_TO_MINE = 2;

	private static final String TXT_NO_VEIN = "附近没有可供挖掘的暗金矿脉";
	
//	private static final Glowing BLOODY = new Glowing( 0x550000 );
	
	{
		name = "稿子";
		image = ItemSpriteSheet.PICKAXE;
		
		unique = true;
		
//		defaultAction = AC_MINE;
		
//		STR = 14;
//		MIN = 3;
//		MAX = 12;
	}
	
	public boolean bloodStained = false;

    @Override
    public String quickAction() {
        return AC_MINE;
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_MINE );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {
		
		if (action == AC_MINE) {
			
			if ( Dungeon.chapter() != 3 ) {
				GLog.w( TXT_NO_VEIN );
				return;
			}
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				
				final int pos = hero.pos + Level.NEIGHBOURS8[i];
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {
				
					hero.spend( TIME_TO_MINE );
					hero.busy();
					
					hero.sprite.cast( pos, new Callback() {
						
						@Override
						public void call() {

							CellEmitter.center( pos ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.SND_EVOKE );
							
							Level.set( pos, Terrain.WALL );
							GameScene.updateMap( pos );
							
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( Dungeon.hero )) {
								GLog.i( Hero.TXT_YOU_NOW_HAVE, gold.name() );
							} else {
								Dungeon.level.drop( gold, hero.pos ).sprite.drop();
							}
							
							Satiety hunger = hero.buff( Satiety.class );
							if (hunger != null && !hunger.isStarving()) {
								hunger.decrease( Satiety.POINT * 50 );
								BuffIndicator.refreshHero();
							}
							
							hero.onOperateComplete();
						}
					} );
					
					return;
				}
			}
			
			GLog.w( TXT_NO_VEIN );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
//	@Override
//	public void proc( Char attacker, Char defender, int damage ) {
//		if (!bloodStained && defender instanceof Bat && (defender.HP <= damage)) {
//			bloodStained = true;
//			updateQuickslot();
//		}
//	}
	
//	private static final String BLOODSTAINED = "bloodStained";
//
//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//
//		bundle.put( BLOODSTAINED, bloodStained );
//	}
//
//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle( bundle );
//
//		bloodStained = bundle.getBoolean( BLOODSTAINED );
//	}
	
//	@Override
//	public Glowing glowing() {
//		return bloodStained ? BLOODY : null;
//	}
	
	@Override
	public String info() {
		return
			"这是一件巨大且耐用的凿岩工具。";
	}
}
