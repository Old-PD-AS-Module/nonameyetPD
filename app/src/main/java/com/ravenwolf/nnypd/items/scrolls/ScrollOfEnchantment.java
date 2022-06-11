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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.windows.WndBag;

public class ScrollOfEnchantment extends InventoryScroll {

    private static final String TXT_ITEM_ENCHANT	= "卷轴将你的%s转换为%s！";
    private static final String TXT_ITEM_UPGRADE	= "因%s已经被附魔，卷轴对其进行了升级！";
    private static final String TXT_ITEM_RESTORED	= "卷轴恢复了%s上的诅咒！";
    private static final String TXT_ITEM_UNCURSE	= "卷轴移除了%s上的诅咒！";
    private static final String TXT_ITEM_UNKNOWN	= "卷轴对%s没能起到任何作用！";

    {
        name = "注魔卷轴";
        shortName = "En";

        inventoryTitle = "选择一个可注魔的物品";
		mode = WndBag.Mode.ENCHANTABLE;

        spellSprite = SpellSprite.SCROLL_ENCHANT;
        spellColour = SpellSprite.COLOUR_RUNE;
        icon=2;
	}
	
	@Override
    public void onItemSelected(Item item) {

        if (item instanceof Weapon) {

            Weapon weapon = (Weapon)item;
            
            if( !weapon.isCursed() ) {

                weapon.identify(ENCHANT_KNOWN);
                weapon.enchant();
                GLog.i( TXT_ITEM_ENCHANT, weapon.simpleName(), weapon.name() );
                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

            } else {

                weapon.cursed=false;
                curUser.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
                GLog.i( TXT_ITEM_RESTORED, weapon.simpleName(), weapon.name() );
                weapon.identify( CURSED_KNOWN );
            }

        } else if (item instanceof Armour) {

            Armour armour = (Armour)item;

            if( !armour.isCursed()) {

                armour.identify(ENCHANT_KNOWN);
                armour.inscribe();
                GLog.i( TXT_ITEM_ENCHANT, armour.simpleName(), armour.name() );

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
            } else {

                armour.cursed=false;
                curUser.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
                GLog.i( TXT_ITEM_RESTORED, armour.simpleName(), armour.name() );
                armour.identify( CURSED_KNOWN );
            }


        } else if ( item instanceof Wand || item instanceof Ring) {

            item.identify( CURSED_KNOWN );

            if( !item.isCursed() ) {

                ScrollOfTransmutation.transmute( item );
                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

            } else {

                item.cursed=false;
                GLog.w( TXT_ITEM_UNCURSE, item.name() );
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);
            }

        } else {

            GLog.w( TXT_ITEM_UNKNOWN, item.name() );
		
		}
	}
	
	@Override
	public String desc() {
        return
                "这张卷轴可以为未附魔的武器护甲赋予一个随机附魔，法杖和戒指则会被转换成其他类型。若使用在被诅咒的物品上，则会清除诅咒";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 100 * quantity : super.price();
    }
}
