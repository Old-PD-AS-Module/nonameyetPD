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
package com.ravenwolf.nnypd.items.armours.body;

import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;

public class MageArmor extends BodyArmorCloth {
	
//	private static final String AC_SPECIAL = "MOLTEN EARTH";
//
//	private static final String TXT_NOT_MAGE	= "Only mages can use this armor!";
	
	{
//		name = "mystic robe";
        name = "法师长袍";
		image = ItemSpriteSheet.ARMOR_MAGE;

        appearance = 1;
	}

    public MageArmor() {
        super( 1 );
    }

    public int getHauntedIndex(){
        return 0;
    }

    @Override
/*    public String desc() {
        return "Combination of intricate magicks and rare materials sewn into fabric of this robe " +
                "allow it to ease your ability to concentrate, basically increasing your willpower.";
    }*/
    public String desc() {
        return "长袍所用的布料覆盖着一层稀有线材织造而出的精密术式，装备它会增强使用者自己的意志力。";
    }


//    public MageArmor() {
//        super( 1 );
//    }
	
//	@Override
//	public String special() {
//		return AC_SPECIAL;
//	}
//
//	@Override
//	public String desc() {
//		return
//			"Wearing this gorgeous robe, a mage can cast a spell of molten earth: all the enemies " +
//			"in his field of view will be set on fire and unable to move at the same time.";
//	}
//
//	@Override
//	public void doSpecial() {
//
//		for (Mob mob : Dungeon.bonus.mobs) {
//			if (Level.fieldOfView[mob.pos]) {
//				Buff.affect( mob, Burning.class ).reignite( mob );
//				Buff.prolong( mob, Roots.class, 3 );
//			}
//		}
//
//		curUser.HP -= (curUser.HP / 3);
//
//		curUser.spend( Actor.TICK );
//		curUser.sprite.operate( curUser.pos );
//		curUser.busy();
//
//		curUser.sprite.centerEmitter().start( ElmoParticle.FACTORY, 0.15f, 4 );
//		Sample.INSTANCE.play( Assets.SND_READ );
//	}
//
//	@Override
//	public boolean doEquip( Hero hero ) {
//		if (hero.heroClass == HeroClass.SCHOLAR) {
//			return super.doEquip( hero );
//		} else {
//			GLog.w( TXT_NOT_MAGE );
//			return false;
//		}
//	}
}