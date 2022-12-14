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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.actors.hazards.Hazard;
import com.ravenwolf.nnypd.actors.hazards.SubmergedPiranha;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.sprites.PiranhaSprite;
import com.ravenwolf.nnypd.visuals.ui.HealthIndicator;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Piranha extends MobEvasive {
	
	public Piranha() {

		super( Dungeon.depth*3/4 + 2/*Dungeon.depth + 1 */ );

		name = "巨型食人鱼";
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        minDamage += tier * 2;
        maxDamage += tier * 2;


        loot = new MysteryMeat();
        lootChance = 0.3f;

		resistances.put(Element.Shock.class, Element.Resist.VULNERABLE );
		resistances.put(Element.Flame.class, Element.Resist.PARTIAL );
		resistances.put(Element.Acid.class, Element.Resist.PARTIAL );

	}

	@Override
	public String getTribe() {
		return TRIBE_LONER;
	}

	public boolean ignoresMissiles(){
		return true;
	}

	public int dodgeValue(boolean ranged){
		if (ranged )
			return super.dodgeValue(ranged)*2;

		return super.dodgeValue(ranged);
	}

	public void submerge(){
		if ((Dungeon.hero.isAlive() && Level.water[ pos ] &&  Dungeon.hero.buff(MindVision.class)==null)) {
			Buff.detach(this, PinCushion.class);
			if (isAlive() && buffs().isEmpty()) {
				if (HealthIndicator.instance.target() == this)
					HealthIndicator.instance.target(null);
				sprite.killAndErase();
				Dungeon.level.mobs.remove(this);
				Actor.remove(this);
				Actor.freeCell(pos);

				int cell = pos;
				if (Hazard.findHazard( pos, SubmergedPiranha.class ) != null ){
					ArrayList<Integer> candidates = new ArrayList<Integer>();
					for (int n : Level.NEIGHBOURS8) {
						int pos = cell + n;
						if( Level.water[ pos ] && Hazard.findHazard( pos, SubmergedPiranha.class ) == null )
							candidates.add( pos );
					}
					if ( candidates.size() > 0 )
						cell= candidates.get(Random.Int(candidates.size()));
				}

				GameScene.ripple(cell);

				SubmergedPiranha submergedPiranha = new SubmergedPiranha();
				submergedPiranha.setStats(cell, HT, HP);
				GameScene.add(submergedPiranha);
			}
		}
	}
	
	@Override
	protected boolean act() {
		if (state == FLEEING && !hasBuff(Tormented.class)) {
			if (nextStepTo(enemy)!=-1)
				state = HUNTING;
		}

		if ((state==WANDERING || state==SLEEPING || state==FLEEING)) {
			spend(TICK);
			submerge();
			return true;
		}

		if (isAlive() && !Level.water[pos]) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}


	@Override
	public boolean reset() {
        state = SLEEPING;
        return true;
	}

	@Override
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			submerge();
			return false;
		}
	}
	
	@Override
	protected boolean getFurther( int target ) {
		if (rooted) {
			return false;
		}
		int dist = Level.distance(pos, target);
		for (int n : Level.NEIGHBOURS8) {
			int newPos = pos + n;
			if (Level.water[newPos] && Level.distance(newPos, target) > dist && Actor.findChar(newPos) == null) {
				move(newPos);
				return true;
			}
		}

		return false;
	}

    @Override
    protected int nextStepTo( Char enemy ) {
        return Dungeon.findPath( this, pos, enemy.pos,
                Level.water,
                Level.fieldOfView );
    }

    //if cant attack back, retreat
	@Override
	public void damage( int dmg, Object src, Element type ) {
		super.damage(dmg,src,type);
		if ( enemy!=null && !Level.adjacent(pos,enemy.pos) && nextStepTo(enemy)==-1) {
			state=FLEEING;
		}
	}


    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {
        if ( !blocked && Random.Int( enemy.HT ) < damage *2) {
			BuffActive.addFromDamage( enemy, Bleeding.class, damage*2 );
        }
        return damage;
    }

	@Override
	public String description() {
		return
				"这些肉食性鱼类不是地下水池中的天然生物。它们被专门培育用来保护被水淹没的储藏室。不管出身如何，它们都同样凶残和嗜血。";
	}

}
