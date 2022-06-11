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
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.weapons.enchantments.Tempered;

public class MeleeWeaponTHDual extends MeleeWeapon {

	{
		hitsToKnow*=dualWieldSpeedModifier();
	}

	public MeleeWeaponTHDual(int tier) {
		super( tier );
	}

	@Override
	public String descType() {
//        return "This is a _tier-" + appearance + " heavy two-handed weapon_. It can be used with wands and throwing weapons, " +
//                "but its strength requirement will increase if paired with shield or another melee weapon.";
		return "双持";
	}

	@Override
	public float speedFactor( Char ch) {
		if (ch instanceof Hero && ((Hero)ch).belongings.weap2 instanceof Shield)
			return super.speedFactor( ch );
		else
			return super.speedFactor( ch ) *dualWieldSpeedModifier();
	}

	protected float dualWieldSpeedModifier(){
		return 1.5f;// -(tier-1)*0.25f;
	}

	@Override
	public int min( int bonus ) {
		return Math.max( 0, 1 + tier + bonus + ( enchantment instanceof Tempered ? bonus : 0 ) ) ;
	}

	@Override
	public int max( int bonus ) {
		return Math.max( 0, 3+ 3*dmgMod()+tier
				+ ( enchantment instanceof Tempered || bonus >= 0 ? bonus * dmgMod() : 0 )
				+ ( enchantment instanceof Tempered && bonus >= 0 ? bonus + 1 : 0 )
		) ;
	}

	public int dmgMod() {
		return tier-1;
	}

	@Override
	public int penaltyBase() {
		return /*super.penaltyBase()-2*/4;
	}
/*
	@Override
	public int strShown( boolean identified ) {
		return super.strShown( identified ) + (
				this == Dungeon.hero.belongings.weap1 && incompatibleWith( Dungeon.hero.belongings.weap2 ) ?
						Dungeon.hero.belongings.weap2.str(
								Dungeon.hero.belongings.weap2.isIdentified() ?
										Dungeon.hero.belongings.weap2.bonus : 0
						) : 0 );
	}

	@Override
	public boolean incompatibleWith( EquipableItem item ) { return item instanceof MeleeWeapon || item instanceof Shield; }
	*/
}
