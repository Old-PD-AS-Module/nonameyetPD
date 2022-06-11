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
package com.ravenwolf.nnypd.items.herbs;

import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.items.food.Food;
import com.ravenwolf.nnypd.items.potions.Potion;

public abstract class Herb extends Food {
		
//    public static final String AC_PLANT	= "PLANT";

//    private static final String TXT_INFO = "Throw this seed to the place where you want to grow %s.\n\n%s";

//    private static final float TIME_TO_PLANT = 1f;

    {
        energy = Satiety.MAXIMUM * 0.05f;
        time = 1f;
//        defaultAction = AC_THROW;
    }

//    protected Class<? extends Plant> plantClass;
//    protected String plantName;

    public Class<? extends Potion> alchemyClass;

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.addFromDamage( AC_PLANT );
//        return actions;
//    }

//    @Override
//    protected void onThrow( int cell ) {
//        if (Dungeon.bonus.map[cell] == Terrain.ALCHEMY || Level.chasm[cell]) {
//            super.onThrow( cell );
//        } else {
//            Dungeon.bonus.plant( this, cell );
//        }
//    }

//    @Override
//    public void execute( Hero hero, String action ) {
//        if (action.equals( AC_PLANT )) {
//
//            hero.spend( TIME_TO_PLANT );
//            hero.busy();
//            ((Seed)detach( hero.belongings.backpack )).onThrow( hero.pos );
//
//            hero.sprite.operate( hero.pos );
//
//        } else {
//
//            super.execute (hero, action );
//
//        }
//    }

//    public Plant couch( int pos ) {
//        try {
//            if (Dungeon.visible[pos]) {
//                Sample.INSTANCE.play( Assets.SND_PLANT );
//            }
//            Plant plant = plantClass.newInstance();
//            plant.pos = pos;
//            return plant;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @Override
    public int price() {
        return 10 * quantity;
    }

//    @Override
//    public String info() {
//        return String.format( TXT_INFO, Utils.indefinite( plantName ), desc() );
//    }
}

