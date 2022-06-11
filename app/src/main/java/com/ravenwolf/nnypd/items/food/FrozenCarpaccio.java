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
package com.ravenwolf.nnypd.items.food;

import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class FrozenCarpaccio extends Food {

	{
/*		name = "frozen carpaccio";
		image = ItemSpriteSheet.CARPACCIO;
		energy = Satiety.MAXIMUM * 0.25f;
        message = "That food tasted... kinda good.";*/
		name = "冻肉";
		image = ItemSpriteSheet.CARPACCIO;
		energy = Satiety.MAXIMUM * 0.25f;
		message = "吃起来不错";
	}
	
	@Override
    public void onConsume( Hero hero ) {

        super.onConsume( hero );

//        GLog.i( "Refreshing!" );

        if (hero.HP < hero.HT) {
            hero.HP = Math.min( hero.HP + hero.HT / 10, hero.HT );
            hero.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
        }
	}
	
	@Override
/*	public String desc() {
		return 
			"It's a piece of frozen raw meat. The only way to eat it is " +
			"by cutting thin slices of it. And this way it's suprisingly good.";
	}*/
	public String desc() {
		return
				"这是份速冻生肉，只能通过切片后食用，而且意外的好吃.";
	}
	
	public int price() {
		return 15 * quantity;
	}

    public static Food cook( MysteryMeat ingredient ) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}
}
