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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;

public abstract class MeleeWeaponLightOH extends MeleeWeapon {

    private static final String TXT_EQUIP_TITLE = "装备副手武器";
    private static final String TXT_EQUIP_MESSAGE =
            "这是一个轻型单手武器，你可以把它装备到副手武器。" +
                    "你想把它当做主手武器还是副手武器？";

    private static final String TXT_EQUIP_PRIMARY = "主手武器";
    private static final String TXT_EQUIP_SECONDARY = "副手武器";

    public MeleeWeaponLightOH(int tier) {
        super(tier);
    }

    @Override
    public String descType() {
//        return "This is a _tier-" + appearance + " _. It can be used as off-hand weapon, " +
//                "but its strength requirement will be increased that way.";
        return "轻型单手";
    }

    @Override
    public int str(int bonus) {
        return super.str(bonus)-2;
    }


    @Override
    public int max( int bonus ) {
        return super.max(bonus) -(tier+1);
    }

    public int dmgMod() {
        return super.dmgMod()-1;
    }

    @Override
    public int guardStrength(int bonus){
        return (super.guardStrength(bonus)-1);
    }

    @Override
    public int penaltyBase() {
        return super.penaltyBase()-2;
    }

    public float getBackstabMod() {
        return 0.30f;
    }

    @Override
    public boolean doEquip( final Hero hero ) {

        if ( hero.belongings.weap1 != null ) {

            NoNameYetPixelDungeon.scene().add(
                    new WndOptions(TXT_EQUIP_TITLE, TXT_EQUIP_MESSAGE,
                            Utils.capitalize( TXT_EQUIP_PRIMARY ),
                            Utils.capitalize( TXT_EQUIP_SECONDARY ) ) {

                        @Override
                        protected void onSelect( int index ) {

                            detach( hero.belongings.backpack );

                            if (index == 0) {
//                            if (index == 0 && ( hero.belongings.weap1 == null || hero.belongings.weap1.doUnequip( hero, true, false ) ) ) {

                                doEquipPrimary(hero);

                            } else if (index == 1) {
//                            } else if (index == 1 && ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) ) {

                                doEquipSecondary( hero );

                            } else {

                                collect( hero.belongings.backpack );

                            }
                        }
                    } );

            return false;

        } else {

           return super.doEquip( hero );

        }
    }


    public boolean doEquipPrimary( Hero hero ) {
        return super.doEquip( hero );
    }

    public boolean doEquipSecondary( Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

            if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
                QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

            if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip(hero, true, false) ) &&
                ( !isCursed() || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                hero.belongings.weap2 = this;
                activate(hero);

                onEquip( hero );

                QuickSlot.refresh();

                hero.updateWeaponSprite();
                hero.spendAndNext(time2equip(hero));
                return true;

            } else {

                QuickSlot.refresh();
                hero.spendAndNext(time2equip(hero) * 0.5f);

                if ( !collect( hero.belongings.backpack ) ) {
                    Dungeon.level.drop( this, hero.pos ).sprite.drop();
                }

                return false;

            }
        } else {

            GLog.w(TXT_ISEQUIPPED, name());
            return false;

        }
    }

}
