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
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.buffs.special.Exposed;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.mobs.npcs.AmbitiousImp;
import com.ravenwolf.nnypd.items.food.Pasty;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.sprites.SeniorSprite;

public class DwarfSeniorMonk extends MobEvasive {


    public DwarfSeniorMonk() {

        super( 15 );

        name = "圣僧";

        spriteClass = SeniorSprite.class;

		loot = new Pasty();
		lootChance = 0.50f;

        armorClass+=tier;
        minDamage++;
        maxDamage++;


        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);

	}

    @Override
    public int minAC() {
        return super.minAC()+1;
    }

    @Override
    public float awareness(){
        return super.awareness() * ( 1.0f + tier * 0.1f );
    }

    @Override
    public float attackSpeed() {
        return super.attackSpeed() * 2.0f;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		AmbitiousImp.Quest.process( this );
		super.die( cause, dmg );
	}

    @Override
    public int guardStrength(boolean ranged){
        return 8+tier*4;
    }

    @Override
    public boolean hasShield() {
        return true;
    }

    @Override
    protected boolean act() {

        Guard guarded = buff( Guard.class );
        if( guarded==null && state == HUNTING && enemySeen && enemy!=null && enemy.buff( Exposed.class ) ==null
                && Level.distance( pos, enemy.pos ) <= 2
                && detected( enemy )
        ) {
            BuffActive.add(this, Guard.class, 6, true);
            spend(TICK);
            return true;
        }

        return super.act();
    }
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        if (!blocked && isAlive())
            Buff.affect(this, Combo.class).hit();
		
		return damage;
	}

    @Override
    public int damageRoll() {

        int dmg = super.damageRoll();

        Combo buff = buff( Combo.class );

        if( buff != null ) {
            dmg += (int) (dmg * buff.modifier());
        }
        return dmg;
    }
	
	@Override
	public String description() {
		return
			"这些僧侣是极端分子，他们致力于保护他们城市的秘密不受任何入侵者的侵犯。" +
                "这位僧侣掌握了肉搏战的高超技艺，能够徒手阻挡物理攻击。";
	}
}
