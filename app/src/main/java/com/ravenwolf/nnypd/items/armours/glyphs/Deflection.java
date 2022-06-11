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
package com.ravenwolf.nnypd.items.armours.glyphs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.effects.Effects;
import com.ravenwolf.nnypd.visuals.effects.Lightning;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite.Glowing;

public class Deflection extends Armour.Glyph {

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Energy.class;
    }

/*    @Override
    protected String name_p() {
        return "%s of deflection";
    }

    @Override
    protected String name_n() {
        return "%s of discharge";
    }

    @Override
    protected String desc_p() {
        return "reflect ranged damage back to it's source in the form of an energy lighting and make you more resistant to energy damage";
    }

    @Override
    protected String desc_n() {
        return "increase ranged damage received";
    }*/
    @Override
    protected String name_p() {
        return "扭曲%s";
    }

    @Override
    protected String name_n() {
        return "泄能%s";
    }

    @Override
    protected String desc_p() {
        return "以能量伤害的方式远程反射给攻击者，并提高魔法属性伤害抗性";
    }

    @Override
    protected String desc_n() {
        return "将对自身造成魔法伤害";
    }

    //have additional chances to proc
    public boolean  proc( Armour armor, Char attacker, Char defender, int damage ) {
	    boolean result=super.proc(armor,attacker,defender,damage);
	    if (result==false)
            result= super.proc(armor,attacker,defender,damage);
        return result;
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        //cant proc on melee enemies
        if (!Level.adjacent(attacker.pos, defender.pos)){
            int reflected=damage;
            if (isShield) reflected=damage*2;
            if (reflected>0) {
                attacker.damage(attacker.absorb(reflected), this, Element.ENERGY);
                int[] points = new int[2];
                points[0] = defender.pos;
                points[1] = attacker.pos;
                defender.sprite.parent.add(new Lightning(points, 2, Effects.get(Effects.Type.JADE_SHOCK), null));
            }

            return true;
        }else
            return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        if (!Level.adjacent(attacker.pos, defender.pos) ){
            int reflected=damage/2;
            if (reflected>0) {
                defender.damage(defender.absorb(reflected), this, Element.ENERGY);
                int[] points = new int[2];
                points[0] = defender.pos;
                points[1] = defender.pos-Level.WIDTH;
                defender.sprite.parent.add(new Lightning(points, 2, Effects.get(Effects.Type.JADE_SHOCK), null));
            }
            return true;
        }else
            return false;
    }
}
