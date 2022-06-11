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
import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.DeathRay;
import com.ravenwolf.nnypd.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfDisintegration extends WandCombat {

	{
		name = "解离法杖";
		hitChars = false;
		image = ItemSpriteSheet.WAND_DISINTEGRATION;
	}

	protected void cursedProc(Hero hero){
		int bonus=(int)(damageRoll()*(1-(float)hero.HP/hero.HT)/2f);
		int dmg=hero.absorb( damageRoll()+bonus, true )/2;
		hero.damage(dmg, this, Element.UNHOLY_PERIODIC);

	}


	@Override
	protected void onZap( int cell ) {
		
		boolean terrainAffected = false;

		ArrayList<Char> chars = new ArrayList<Char>();
		
		for (int i=1; i <= Ballistica.distance; i++) {
			
			int c = Ballistica.trace[i];
			
			Char ch = Actor.findChar( c );
			if ( ch != null ) {
				chars.add( ch );
			}

            if (Dungeon.level.map[c] == Terrain.DOOR_CLOSED) {
                Level.set( c, Terrain.EMBERS );
                GameScene.updateMap( c );
                terrainAffected = true;
            } else if (Dungeon.level.map[c] == Terrain.HIGH_GRASS ) {
                Level.set( c, Terrain.GRASS );
                GameScene.updateMap( c );
                terrainAffected = true;
            }

            CellEmitter.center( c ).burst( PurpleParticle.BURST, terrainAffected ? Random.IntRange( 6, 8 ) : Random.IntRange( 3, 5 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}


		for (Char ch : chars) {
			//deals bonus damage based on missing life
			int bonus=(int)(damageRoll()*(1-(float)ch.HP/ch.HT)/2f);
			ch.damage( ch.absorb( damageRoll()+bonus, true ), curUser, Element.UNHOLY_PERIODIC );
			if (curUser.subClass == HeroSubClass.BATTLEMAGE) {
				BuffActive.add(ch, Revealed.class, 8, true);
			}


        }
	}

	protected int distance() {
		return 8 ;
	}

	@Override
	protected void fx(  int cell, final Callback callback ) {
		curUser.sprite.cast(cell,new Callback() {
			@Override
			public void call() {
				int cell = Ballistica.trace[Ballistica.distance];
				curUser.sprite.parent.add(new DeathRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(cell)));
				callback.call();
			}});
	}
	
	@Override
	public String desc() {
		return
				"这根法杖能够放射出毁灭性的射线，穿透其路径上的所有生物，这个射线会对生命值较低的生物造成更高的伤害";
	}

}
