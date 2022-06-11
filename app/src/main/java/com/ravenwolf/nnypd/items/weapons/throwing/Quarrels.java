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
package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Quarrels extends ThrowingWeaponAmmo {

	{
		name = "弩箭";
		image = ItemSpriteSheet.QUARREL;
		critical=new PierceCritical(this);
	}

	public Quarrels() {
		this( 1 );
	}

	public Quarrels(int number) {
        super( 3 );
		quantity = number;
	}

	public boolean stick(Char enemy){
		return !enemy.isSolid() && !enemy.isEthereal();
	}
/*
    @Override
    public int image() {
        return quantity > 1 ? ItemSpriteSheet.QUARRELS : image;
//        return (isEquipped( Dungeon.hero ) || Dungeon.hero.belongings.backpack.items.contains(this) ) ? ItemSpriteSheet.QUARRELS : image;
    }
	*/
	@Override
	public String desc() {
		return
				"适用于重弩类的专属弹药！";
	}
}
