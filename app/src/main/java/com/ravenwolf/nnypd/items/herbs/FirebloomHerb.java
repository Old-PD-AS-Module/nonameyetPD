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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Fire_ColdResistance;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfLiquidFlame;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class FirebloomHerb extends Herb {
    {
        name = "烈焰花";
        image = ItemSpriteSheet.HERB_FIREBLOOM;

        alchemyClass = PotionOfLiquidFlame.class;
        message = "喔！好辣";
    }

    @Override
    public void onConsume( Hero hero ) {

        Debuff.remove( hero, Chilled.class );

        float buff=hero.ringBuffs( RingOfSatiety.Satiety.class );
        BuffActive.add( hero, Fire_ColdResistance.class, 80*buff );

        super.onConsume( hero );
    }

    @Override
    public String desc() {
       /* return "Northern tribes sometimes use Firebloom herbs as a food to shortly warm themselves " +
                "in extreme situations. Is also valuable for its thermal-reactive properties when combined with other components. "
                +"This is an elemental herb, if combined with any other elemental herb will create a hazard potion";*/
        return "在北方的寒冷地区人们有时会靠食用烈焰花来短时间的取暖。吃掉它会获得一定时间的冰火抗性，这些药草可以用来炼制液火药剂。";
    }

}
