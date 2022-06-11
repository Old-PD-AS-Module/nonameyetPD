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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Body_AcidResistance;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfFrigidVapours;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class IcecapHerb extends Herb {
    {
        name = "冰冠花";
        image = ItemSpriteSheet.HERB_ICECAP;

        alchemyClass = PotionOfFrigidVapours.class;
        message = "尝起来像薄荷一样清爽！";
    }

    @Override
    public void onConsume( Hero hero ) {

        float buff=hero.ringBuffs( RingOfSatiety.Satiety.class );
        BuffActive.add( hero, Body_AcidResistance.class, 80*buff );


        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "这个冰冠花摸起来很冷，具有有一定的麻醉作用。吃掉会减缓你的新陈代谢，并且提高你对腐蚀性气体的抵抗力。它通常被用来炼制冰霜药剂。";

       /* return "Icecap herbs feel cold to touch and have some numbing capabilities. Eating it will slow down your metabolism, providing more resistance to chemicals and body affections. "
                +"This is an elemental herb, if combined with any other elemental herb will create a hazard potion";*/

    }



}

