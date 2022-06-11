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
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfMindVision;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class DreamweedHerb extends Herb {

    {
        name = "幻视草";
        image = ItemSpriteSheet.HERB_DREAMWEED;
        alchemyClass = PotionOfMindVision.class;
        message = "这种药草吃起来真的好甜！";
    }

    @Override
    public void onConsume( Hero hero ) {

        float duration =6*hero.ringBuffs( RingOfSatiety.Satiety.class );
        BuffActive.add( hero, MindVision.class, duration );
        Debuff.add( hero, Blinded.class, duration );

        super.onConsume( hero );
    }

    @Override
    public String desc() {

        return "人们说，咀嚼幻视草会使你感知到周围生物的思维，但同时它也会导致你暂时性的失明。它通常被用来炼制灵视药剂。";

        /*return "Chewing Dreamweed herbs will sharpen your senses, increasing concentration and mind resistance."+
                "This is a stimulant herb, combined with any elemental herb will create a buff potion";*/
        //return "Folks say that chewing Dreamweed herbs greatly helps to clear one's thinking capabilities.";
        /*return "Folks say that chewing Dreamweed herbs greatly helps to clear one's thinking " +
                "capabilities. Such herbs are also used to brew potions of Mind Vision.";*/
    }
}

