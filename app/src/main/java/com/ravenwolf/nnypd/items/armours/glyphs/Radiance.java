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
package com.ravenwolf.nnypd.items.armours.glyphs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Banished;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ShaftParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Radiance extends Armour.Glyph {


	
	@Override
	public Glowing glowing() {
		return YELLOW;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Unholy.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of radiance";
    }

    @Override
    protected String name_n() {
        return "%s of judgement";
    }

    @Override
    protected String desc_p() {
        return "banish any evil magical creature make you more resistant to unholy damage";
    }

    @Override
    protected String desc_n() {
        return "torment you on hit";
    }*/
    @Override
    protected String name_p() {
        return "光辉%s";
    }

    @Override
    protected String name_n() {
        return "放逐%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时会驱逐附近的不洁生物，并提高自身不洁属性抗性";
    }

    @Override
    protected String desc_n() {
        return "被击中时会放逐自身";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        if ( Level.adjacent(attacker.pos, defender.pos)) {
            if (attacker.isMagical() ) {
                CellEmitter.center(defender.pos).burst(ShaftParticle.FACTORY, 4);
                if (isShield)
                    BuffActive.addFromDamage(attacker, Banished.class, damage * 2);
                else
                    BuffActive.addFromDamage(attacker, Banished.class, damage);
                return true;
            }else if (attacker instanceof Hero) //for haunted armors effect
                BuffActive.addFromDamage( defender, Tormented.class, damage );
        }
        return false;
    }


    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        BuffActive.addFromDamage( defender, Tormented.class, damage );
        return true;
    }


}
