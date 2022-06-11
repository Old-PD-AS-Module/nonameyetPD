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
package com.ravenwolf.nnypd.items.weapons.enchantments;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Recharging;
import com.ravenwolf.nnypd.actors.buffs.special.skills.SummonArcaneOrbSkill;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Arcane extends Weapon.Enchantment {

	@Override
	public Glowing glowing() {
		return TEAL;
	}

    @Override
    protected String name_p() {
        return "奥术%s";
    }

    @Override
    protected String name_n() {
        return "反噬%s";
    }

    @Override
    protected String desc_p() {
        return "攻击时会为法杖充能";
    }

    @Override
    protected String desc_n() {
        return "攻击时会消耗法杖能量";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if( attacker instanceof SummonArcaneOrbSkill.ArcaneOrb) {
            attacker = Dungeon.hero;
        }

        if( attacker instanceof Hero ) {
            Recharging.recharge((Hero)attacker, damage);
            attacker.sprite.centerEmitter().burst(EnergyParticle.FACTORY, (int) Math.sqrt(damage / 2) + 2);
        }
        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        if( attacker instanceof Hero ) {

            Hero hero = (Hero)attacker;

            for (Item item : hero.belongings) {
                if (item instanceof Wand) {
                    Wand wand = (Wand) item;
                    if ( wand.curCharges < wand.maxCharges())
                        wand.recharge(-damage*2);
                }
            }

            attacker.sprite.centerEmitter().burst(SparkParticle.FACTORY, (int) Math.sqrt(damage / 2) + 1);
        }

        return true;
    }
}
