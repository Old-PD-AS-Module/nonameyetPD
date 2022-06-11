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
package com.ravenwolf.nnypd.items.weapons.melee;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class Knuckles extends MeleeWeaponLight {

	{
		name = "指虎";
		image = ItemSpriteSheet.KNUCKLEDUSTER;
        critical=new BluntCritical(this);
	}
	
	public Knuckles() {
		super( 1 );
	}

	@Override
	public int dmgMod() {
		return 1;
	}

	@Override
	public int max( int bonus ) {
		return super.max(bonus)-2;
	}

	@Override
	public int min( int bonus ) {
		return super.min(bonus)-1;
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)+1;
	}


	@Override
	public float speedFactor( Char ch) {
		return super.speedFactor( ch ) * 2f;
	}

    @Override
    public Type weaponType() {
        return Type.M_BLUNT;
    }
	
	@Override
	public String desc() {
		/*return "A piece of iron shaped to fit around the knuckles. This simple design allows " +
                "attacking with this weapon as fast as with fists, while being almost impossible " +
                "to knock it out of them."*/
		return "一副普通的指虎。简易的设计允许使用者如同正常挥舞拳头一样做出快速攻击，而且几乎不可能会脱手。”"
				+"\n\n这是一个非常快速的武器。";
	}
}
