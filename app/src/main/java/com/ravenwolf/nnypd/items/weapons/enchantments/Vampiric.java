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
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Vampiric extends Weapon.Enchantment {
	
	@Override
	public Glowing glowing() {
		return RED;
	}

    @Override
    protected String name_p() {
        return "血饮%s";
    }

    @Override
    protected String name_n() {
        return "嗜血%s";
    }

    @Override
    protected String desc_p() {
        return "攻击时会从敌人身上吸取生命";
    }

    @Override
    protected String desc_n() {
        return "攻击时会从自己身上嗜取生命";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if ( attacker.isAlive() ) {

            //int effValue = Random.IntRange(damage / 3, damage / 2);

            //effValue=Element.Resist.doResist( defender, Element.BODY,effValue );

            int effValue=damage/4;
            effValue+=effValue*2*(1-attacker.HP/(float)attacker.HT);

            effValue=Element.Resist.doResist( defender, Element.BODY,effValue );

            if( effValue > defender.HP ) {
                effValue = defender.HP;
            }

            if ( effValue > 0 ) {

                attacker.heal( effValue );

                if( attacker.sprite.visible ) {
                    attacker.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1);
                }
                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        proc_p(defender,attacker,damage);
        /*int effValue=damage/5;
        effValue+=effValue*2*(1-defender.HP/(float)defender.HT);
        attacker.damage(effValue, this, Element.BODY);
        attacker.sprite.burst(0x660022, (int) Math.sqrt(damage / 2) + 1);*/
        return true;
    }
}
