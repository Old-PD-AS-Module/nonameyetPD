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
package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.Journal.Feature;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.wands.WandUtility;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.windows.WndBag;

public class AltarEnchant extends AltarPower {


/*    private static final String TXT_ITEM_ENCHANT	= "your %s has turned into %s!";
    private static final String TXT_ITEM_RESTORED	= "the enchantment on your %s has been restored!";
    private static final String TXT_ITEM_UNCURSE	= "the curse from your %s has been removed!";   */
    private static final String TXT_ITEM_ENCHANT	= "您的%s已变成%s！";
    private static final String TXT_ITEM_RESTORED	= "您的%s上的附魔已复原！";
    private static final String TXT_ITEM_UNCURSE	= "您%s的诅咒已被移除！";

    @Override
    protected void evolve() {
        super.evolve();

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.ALTAR_OF_ENCHANTMENT);
        }
    }

    public WndBag.Mode getBagMode( ) {
        return WndBag.Mode.ENCHANTABLE;
    }
/*
    public  String getDescription( ){
        return "Select an enchantable item";
    }
*/

    public Item affectItem( Item item ) {

        Item newItem =enchantItem(item);
        if (newItem != null) {
            Hero hero = Dungeon.hero;
            item.detachAll(hero.belongings.backpack);
            if (hero.belongings.weap2 == item){
                QuickSlot.refresh();
                hero.belongings.weap2 = null;
            }else if (hero.belongings.ring1 == item){
                hero.belongings.ring1 = null;
            }else if (hero.belongings.ring2 == item){
                hero.belongings.ring2 = null;
            }
            if (!newItem.doPickUp(Dungeon.hero)) {
                Dungeon.level.drop(newItem, Dungeon.hero.pos).sprite.drop();
            }
        }
        volume = off[pos] = cur[pos] -= 1;


        if (cur[pos] <= 0) {
            GLog.i(AltarPower.TXT_NO_MORE_POWER);
            Journal.remove(Feature.ALTAR_OF_ENCHANTMENT);

        }

        return item;
    }

    public Item enchantItem( Item item ) {

        if (item instanceof Weapon) {

            Weapon weapon = (Weapon)item;

            if( !weapon.isCursed() ) {

                weapon.identify(Item.ENCHANT_KNOWN);
                weapon.enchant();
                GLog.i( TXT_ITEM_ENCHANT, weapon.simpleName(), weapon.name() );
                CellEmitter.get(pos).burst(EnergyParticle.FACTORY, 10);

            } else {

                weapon.cursed=false;
                CellEmitter.get(pos).start( ShadowParticle.UP, 0.05f, 10 );
                GLog.i( TXT_ITEM_RESTORED, weapon.simpleName(), weapon.name() );
                weapon.identify( Item.CURSED_KNOWN );
            }

        } else if (item instanceof Armour) {

            Armour armour = (Armour)item;

            if( !armour.isCursed()) {

                armour.identify(Item.ENCHANT_KNOWN);
                armour.inscribe();
                GLog.i( TXT_ITEM_ENCHANT, armour.simpleName(), armour.name() );

                CellEmitter.get(pos).burst(EnergyParticle.FACTORY, 10);
            } else {

                armour.cursed=false;
                CellEmitter.get(pos).start( ShadowParticle.UP, 0.05f, 10 );
                GLog.i( TXT_ITEM_RESTORED, armour.simpleName(), armour.name() );
                armour.identify( Item.CURSED_KNOWN );
            }

        } else if ( item instanceof Wand || item instanceof Ring) {

            item.identify( Item.CURSED_KNOWN );

            if( !item.isCursed() ) {

                CellEmitter.get(pos).burst(EnergyParticle.FACTORY, 10);
                if (item instanceof Wand)
                    return changeWand( (Wand)item );
                if (item instanceof Ring)
                    return changeRing( (Ring)item );

            } else {
                item.cursed=false;
                GLog.w( TXT_ITEM_UNCURSE, item.name() );
                CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
            }

        }
        return null;
    }

    private static Ring changeRing( Ring r ) {
        Ring n;
        do {
            n = (Ring) Generator.random(Generator.Category.RING);
        } while (n == null || n.getClass() == r.getClass());

        n.bonus = r.bonus;
        n.known = r.known;
        n.cursed = r.cursed;

        GLog.i( TXT_ITEM_ENCHANT, r.name(), n.name() );
        return n;
    }

    private static Wand changeWand( Wand w ) {

        Wand n;
        do {
            n = (Wand)Generator.random( Generator.Category.WAND );
        } while (n == null || n.getClass() == w.getClass() || n instanceof WandUtility != w instanceof WandUtility);

        n.bonus = w.bonus;
        n.cursed = w.cursed;
        n.known = w.known;
        n.curCharges = w.curCharges;

        GLog.i( TXT_ITEM_ENCHANT, w.name(), n.name() );

        return n;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);

        emitter.pour(  Speck.factory( Speck.CHANGE ), 0.4f );
    }

    @Override
    public String tileDesc() {
        return
                /*"Powerful magic of change radiates from this altar. " +
                        "Offer an item to remove maleficent magic or imbue it with random magical energy";*/
                "这座祭坛散发出强大的变化魔法。" +
                        "提供一件物品以移除诅咒或向其注入魔法";
    }
}
