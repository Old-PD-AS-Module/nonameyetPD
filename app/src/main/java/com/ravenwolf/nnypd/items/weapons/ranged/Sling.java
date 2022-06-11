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
package com.ravenwolf.nnypd.items.weapons.ranged;

import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.RangedWeaponSprite;

public class Sling extends RangedWeaponMissile {

	{
		name = "投索";
		image = ItemSpriteSheet.SLING;
		drawId= RangedWeaponSprite.SLING;
		critical=new BluntCritical(this);
	}

	protected int[][] weapRangedAtk() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {3, 2, 7, 3 },	//x
                {0, 0, 0, 0}};
	}
	protected int[][] weapRun() {

        return new int[][]{	{0, 0, 1, 1, 0, 0  },	//frame
                {2, 3, 5, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}

	protected int[][] meleeAtk() {
		//by default ranged weapons dont have animation for melee attacks (with offhand), but bows still display graphics on melee attacks
		return null;
	}

	public Sling() {
		super( 1 );
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)-2;
	}
/*
    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Bullets.class;
    }
*/
	@Override
	public Type weaponType() {
		return Type.R_MISSILE;
	}
	
	@Override
	public String desc()  {
		return "一个以多条皮制套带制成的简易投弹索。就算是小小的铅弹在投索上也能成为致命的投射物。";
	}
}
