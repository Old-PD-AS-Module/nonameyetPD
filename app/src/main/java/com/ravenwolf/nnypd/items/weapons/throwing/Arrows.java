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

public class Arrows extends ThrowingWeaponAmmo {

	{
		name = "箭矢";
		image = ItemSpriteSheet.ARROW;
		critical=new PierceCritical(this);
	}

	public Arrows() {
		this( 1 );
	}

	public Arrows(int number) {
        super( 2 );
		quantity = number;
	}

	public boolean stick(Char enemy){
		return !enemy.isSolid() && !enemy.isEthereal();
	}

	@Override
	public String desc() {
		return
				"一根带有金属尖头和羽尾的箭矢，箭出如龙，弦无虚发。";
	}
}
