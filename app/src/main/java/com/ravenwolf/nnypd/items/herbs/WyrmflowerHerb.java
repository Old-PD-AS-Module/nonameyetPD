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
import com.ravenwolf.nnypd.actors.buffs.bonuses.Toughness;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfStrength;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class WyrmflowerHerb extends Herb {
    {
        name = "龙藤花";
        image = ItemSpriteSheet.HERB_WYRMFLOWER;

        alchemyClass = PotionOfStrength.class;
        message = "尝起来有种浓厚的味道。";
    }

    @Override
    public void onConsume( Hero hero ) {

        int hpBonus = 2;

        //hero.HP = hero.HT += hpBonus;
        hero.HT += hpBonus;
        hero.HP= (Math.min(hero.HT,  hero.HP+ hero.HT/4));

        hero.sprite.showStatus( CharSprite.POSITIVE, "+%d hp", hpBonus );

        float buff=hero.ringBuffs( RingOfSatiety.Satiety.class );
        BuffActive.add( hero, Toughness.class, 50 * buff );

        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "一种非常稀有的药草，强大的特性使得无数的炼金术士为止着迷。吃掉它会赋予你强大的力量，使你一定时间内刀枪不入！它通常被用来炼制力量药剂。";
                //+"This is a therapeutic herb, can be used to brew healing and mystical potions if combined with other therapeutic herb";
    }




}

