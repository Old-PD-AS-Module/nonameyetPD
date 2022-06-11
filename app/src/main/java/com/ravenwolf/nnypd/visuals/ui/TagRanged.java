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
package com.ravenwolf.nnypd.visuals.ui;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeaponAmmo;

public class TagRanged extends Tag {

	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	public static TagRanged instance;

	private ItemSlot slot;

	private Item item = null;


	public TagRanged() {
		super( 0x7C8072 );
		instance = this;
		setSize( WIDTH, HEIGHT );

	}


	@Override
	protected void createChildren() {

		super.createChildren();

		slot = new ItemSlot() {

			protected void onClick() {
				QuickSlot.quickslot0.onClick();

			}

		};

		slot.setScale(0.8f);
		add( slot );

	}


	@Override
	protected void layout() {
		super.layout();
		slot.setRect( x + 2, y + 3, width - 2, height - 6 );

	}


	@Override
	public void update() {

		super.update();

		if(Dungeon.hero.belongings.ranged instanceof ThrowingWeapon )
			//item =Dungeon.hero.belongings.ranged;
			item =Dungeon.hero.belongings.weap2 instanceof RangedWeapon &&
					((RangedWeapon) Dungeon.hero.belongings.weap2).checkAmmo(Dungeon.hero, false) ?
					Dungeon.hero.belongings.weap2 : Dungeon.hero.belongings.ranged instanceof ThrowingWeaponAmmo ? null : Dungeon.hero.belongings.ranged;

		else
			item = null;


		slot.item( item );
		slot.showStatus( item != null );
		//item(item);

		if (item!=null) {
			visible = true;
			slot.enable(Dungeon.hero.isAlive() && Dungeon.hero.ready);
		}else{
			visible = false;
		}

	}

}
