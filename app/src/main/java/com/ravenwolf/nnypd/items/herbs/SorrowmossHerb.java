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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfCorrosiveGas;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class SorrowmossHerb extends Herb {
    {
        name = "断肠苔";
        image = ItemSpriteSheet.HERB_SORROWMOSS;

        alchemyClass = PotionOfCorrosiveGas.class;
        message = "这东西尝起来很苦！.";
    }

    @Override
    public void onConsume( Hero hero ) {

        float duration= 6*hero.ringBuffs( RingOfSatiety.Satiety.class );

        //Enraged rage=BuffActive.add( hero, Enraged.class, duration);
        //if (rage!=null && rage.refreshDuration<duration) rage.refreshDuration=(int)duration;
        BuffActive.add( hero, Frenzy.class, duration*2 );
        Debuff.add( hero, Poisoned.class, duration );

        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "这些苔藓上面覆盖着一层薄薄的毒液，甚至具有一定的致幻效果，吃掉它或许会激发出自身狂暴的意识，它通常被用来炼制酸蚀药剂。";
              //  +"This is an elemental herb, if combined with any other elemental herb will create a hazard potion";
       /* return "Sorrowmoss herbs are used to brew potions of Corrosive Gas. Eating this herb " +
                "would probably not the best idea.";*/
    }
}

