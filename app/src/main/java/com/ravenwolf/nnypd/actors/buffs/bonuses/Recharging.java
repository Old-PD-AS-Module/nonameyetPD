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
package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Recharging extends Bonus {

    @Override
    public int icon() {
        return BuffIndicator.RECHARGING;
    }

/*    @Override
    public String toString() {
        return "Recharging";
    }

    @Override
    public String statusMessage() { return "Recharging"; }

    @Override
    public String description() {
        return "Energy is coursing through you, improving the rate that your wands and charms charge.";
    }*/
    @Override
    public String toString() {
        return "充能";
    }

    @Override
    public String statusMessage() { return "充能"; }

    @Override
    public String description() {
        return "魔力在你的体内奔腾而过，显著提高你的法杖和魔咒的充能速率";
    }
    @Override
    public boolean act() {

        if (target instanceof Hero)
            recharge((Hero)target, 8);

        return super.act();
    }

    static public void recharge(Hero hero, int ammount){
        for (Item item : hero.belongings) {
            if (item instanceof Wand) {
                Wand wand = (Wand) item;
                if ( wand.curCharges < wand.maxCharges())
                    wand.recharge(ammount);
            }
        }
    }

}
