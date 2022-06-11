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
package com.ravenwolf.nnypd.items.herbs;

import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Recharging;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfLevitation;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class WhirlvineHerb extends Herb {
    {
        name = "旋涡草";
        image = ItemSpriteSheet.HERB_WHIRLVINE;

        alchemyClass = PotionOfLevitation.class;
        message = "这东西尝起来...有点酸";
    }

    @Override
    public void onConsume( Hero hero ) {

        float buff=hero.ringBuffs( RingOfSatiety.Satiety.class );
        //BuffActive.add( hero, Concentration.class, 80*buff );
        BuffActive.add( hero, Recharging.class, 6*buff);
        BuffActive.add( hero, Dazed.class, 6*buff);


        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "这个植物中存储着大量的魔法，吃掉它会使你全省充满能量，回复你法杖和魔咒的充能，但是同时这股能量会使你感到晕厥。它通常被用来炼制浮空药剂。";
                //+"This is a therapeutic herb, can be used to brew healing and mystical potions if combined with other therapeutic herb";
    }
}

