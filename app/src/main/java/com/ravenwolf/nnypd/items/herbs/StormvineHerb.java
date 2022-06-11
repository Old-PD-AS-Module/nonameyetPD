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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Electric_EnergyResistance;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Shocked;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfSparklingDust;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class StormvineHerb extends Herb {

    {
        name = "风暴藤";
        image = ItemSpriteSheet.HERB_STORMVINE;
        alchemyClass = PotionOfSparklingDust.class;
        message = "这东西吃起来..很神奇.";
    }

    @Override
    public String desc() {
        /*return"Its say that kobolds eat Stormvine herbs to enter a battle stance. "+
                "Such herbs are used to brew potions of Lighting.";*/
        return "这些药草似乎在它的顶端储存了一些电能。吃掉它会增加你对雷电能量的抗性。它通常被用来炼制雷暴药剂。";
                //+"This is an elemental herb, if combined with any other elemental herb will create a hazard potion";
    }

    @Override
    public void onConsume( Hero hero ) {

//        GLog.i("Your mind is cleared.");

        Debuff.remove( hero, Shocked.class );

        float buff=hero.ringBuffs( RingOfSatiety.Satiety.class );
        BuffActive.add( hero, Electric_EnergyResistance.class, 80*buff );

        super.onConsume( hero );
    }




}

