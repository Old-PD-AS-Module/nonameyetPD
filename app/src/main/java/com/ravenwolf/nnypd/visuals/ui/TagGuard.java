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
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;

public class TagGuard extends Tag {

	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	public static TagGuard instance;

	private ItemSlot slot;

	private EquipableItem item = null;



	public TagGuard() {
		super( 0x7C8072 );
		instance = this;
		setSize( WIDTH, HEIGHT );

	}
	
	@Override
	protected void createChildren() {

		super.createChildren();

		slot = new ItemSlot() {

			protected void onClick() {
				if (item!=null) {
					item.execute(Dungeon.hero, item.equipAction());
					flash();
				}
			}

            protected boolean onLongClick() {
				if (item instanceof Shield) {

					item.execute(Dungeon.hero, item.quickAction());
					flash();
					return true;
				}
				return false;
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

		if(Dungeon.hero.belongings.weap2 instanceof Shield)
			item =Dungeon.hero.belongings.weap2;
		else if(Dungeon.hero.belongings.weap1 instanceof MeleeWeapon)
			item =Dungeon.hero.belongings.weap1;
		else if(Dungeon.hero.belongings.weap2 instanceof MeleeWeapon)
			item =Dungeon.hero.belongings.weap2;
		else
			item = null;

		slot.item(item);
		slot.showParams(false);

		bg.visible = item!=null;

		if (item!=null) {
			slot.enable(Dungeon.hero.isAlive() && Dungeon.hero.ready);
		}

	}

}
