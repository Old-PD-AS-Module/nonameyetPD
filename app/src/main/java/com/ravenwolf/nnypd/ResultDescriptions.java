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
package com.ravenwolf.nnypd;

import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.DwarfMonk;
import com.ravenwolf.nnypd.actors.mobs.GnollBrute;
import com.ravenwolf.nnypd.actors.mobs.Golem;
import com.ravenwolf.nnypd.actors.mobs.Mimic;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Piranha;
import com.ravenwolf.nnypd.actors.mobs.Rat;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.levels.features.Chasm;
import com.ravenwolf.nnypd.levels.traps.BoulderTrap;
import com.ravenwolf.nnypd.levels.traps.Trap;
import com.ravenwolf.nnypd.misc.utils.Utils;

public abstract class ResultDescriptions {

//    public static final String FAIL	= "%s";
    public static final String WIN	= "获得Yendor护身符";

    public static String generateResult( Object killedBy, Element killedWith ) {

        /*return Utils.capitalize( killedBy == Dungeon.hero ? killedWith( killedBy, killedWith ) +
                ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ? " 她自己" : " 他自己" ) :
                killedWith( killedBy, killedWith ) + " 死于 " + killedBy( killedBy ) );*/
		return Utils.capitalize( killedBy == Dungeon.hero ? "死于：自杀" :
                "死于：" + killedBy( killedBy ) );
    }

    public static String generateMessage( Object killedBy, Element killedWith ) {

        /*return ( killedBy == Dungeon.hero ? "你 " + killedWith( killedBy, killedWith ) + " 你自己" :
                "你的 " + killedWith( killedBy, killedWith ) + " 死于 " + killedBy( killedBy ) ) + "...";*/
		return ( killedBy == Dungeon.hero ? "你用自己的力量杀害了自己" :
                killedBy( killedBy ) + killedWith( killedBy, killedWith ) +  "了你" ) + "...";

    }

    private static String killedWith( Object killedBy, Element killedWith ) {

        String result = "杀害";

        if( killedWith == null ) {

            if( killedBy instanceof Mob) {

                Mob mob = (Mob)killedBy;

                if( Bestiary.isBoss( mob ) || mob instanceof Rat ) {

                    result = "击败";

                } else if ( mob instanceof GnollBrute ) {

                    result = "残忍的杀害";

                } else if ( mob instanceof DwarfMonk ) {

                    result = "用拳头击垮";

                } else if ( mob instanceof Golem ) {

                    result = "无情的碾压";

                } else if ( mob instanceof Piranha ) {

                    result = "活活吃掉";

                } else if ( mob instanceof Mimic ) {

                    result = "偷袭杀害";

                }

            } if( killedBy instanceof BoulderTrap ) {

                result = "压碎";

            }


//        } else if( killedWith instanceof DamageType.Flame) {
//            result = "burned to crisp";
//        } else if( killedWith instanceof DamageType.Frost) {
//            result = "chilled to death";
        } else if( killedWith instanceof Element.Shock) {
            result = "电死";
        } else if( killedWith instanceof Element.Acid) {
            result = "溶解";
//        } else if( killedWith instanceof DamageType.Mind) {
//            result = "";
//        } else if( killedWith instanceof DamageType.Body) {
//            result = "drained";
//        } else if( killedWith instanceof DamageType.Unholy) {
//            result = "withered";
//        } else if( killedWith instanceof DamageType.Energy) {
//            result = "disintegrated";
        }

        return result;
    }

    private static String killedBy( Object killedBy ) {

        String result = "未知的力量";

        if( killedBy instanceof Mob ) {
            Mob mob = ((Mob)killedBy);
            result = ( !Bestiary.isBoss( mob ) ? Utils.indefinite( mob.name ) : mob.name );
        } else if( killedBy instanceof Blob ) {
            Blob blob = ((Blob)killedBy);
            result = Utils.indefinite( blob.name );
        } else if( killedBy instanceof Weapon.Enchantment ) {
            result = "诅咒武器";
        } else if( killedBy instanceof Armour.Glyph ) {
            result = "诅咒护甲";
        } else if( killedBy instanceof Wand) {
            result = "诅咒法杖";
        } else if( killedBy instanceof Buff ) {
            if( killedBy instanceof Crippled ) {
                result = "流血";
            } else if( killedBy instanceof Poisoned ) {
                result = "中毒";
            } else if( killedBy instanceof Satiety ) {
                result = "饥饿";
            } else if( killedBy instanceof Burning ) {
                result = "火焰";
            } else if( killedBy instanceof Corrosion ) {
                result = "腐蚀淤泥";
            }
        } else if( killedBy instanceof Trap ) {
            result = "陷阱";
        } else if( killedBy instanceof Chasm ) {
            result = "虚空";
        }

        return result;
    }

}
