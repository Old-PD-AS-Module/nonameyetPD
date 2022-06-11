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

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Unholy extends Weapon.Enchantment {
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

    @Override
    protected String name_p() {
        return "不洁%s";
    }

    @Override
    protected String name_n() {
        return "恶咒%s";
    }

    @Override
    protected String desc_p() {
        return "每次攻击对敌人造成不洁的伤害，血量越低，造成的伤害就越多";
    }

    @Override
    protected String desc_n() {
        return "每次对自己造成不洁的伤害，血量越低，造成的伤害就越多";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

	    int bonus=(int)(damage*(1-(float)defender.HP/defender.HT));

	    if (bonus>0) {
            defender.damage(bonus, this, Element.UNHOLY);
            defender.sprite.emitter().burst(ShadowParticle.UP, bonus / 2 + 1);

            return true;
        }else
            return false;

    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        int bonus=(int)(damage*(1-(float)defender.HP/defender.HT)) *2/3;

        if (bonus>0) {
            attacker.damage( bonus, this, Element.UNHOLY);
            attacker.sprite.emitter().burst(ShadowParticle.UP, bonus/2 + 1);

            return true;
        }else
            return false;

    }
}
