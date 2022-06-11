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
import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;

public class Katar extends MeleeWeaponLight {

	{
		name = "拳刃";
		image = ItemSpriteSheet.KATAR;
		drawId= WeaponSprite.KATAR;
		critical=new PierceCritical(this);
	}

	public Katar() {
		super( 3 );
	}

	@Override
	public int dmgMod() {
		return 2;
	}

	@Override
	public int max( int bonus ) {
		return super.max(bonus)-4;
	}

	@Override
	public int min( int bonus ) {
		return super.min(bonus)-1;
	}

	@Override
	public float speedFactor( Char ch) {
		return super.speedFactor( ch ) * 2f;
	}

	@Override
	public int guardStrength(int bonus){
		return (super.guardStrength(bonus)-2);
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.60f;
	}


    @Override
    public Type weaponType() {
        return Type.M_SWORD;
    }
	
	@Override
	public String desc() {
		return "一种锋利的刀刃，这种武器适合快速攻击，速度与拳头一样快，同时不可能被击落。"
				+"\n\n 这是一种非常快的武器。";
	}
}
