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
package com.ravenwolf.nnypd.items.wands;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.visuals.effects.Splash;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfMagicMissile extends WandCombat {


	{
		name = "魔弹法杖";
        //shortName = "Ma";
		image = ItemSpriteSheet.WAND_MAGICMISSILE;
	}


	@Override
	public int maxCharges( int bonus ) {
		return super.maxCharges(bonus) +1 ;
	}

	protected float rechargeRate(int bonus) {
		return 25f;
	}


	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		return actions;
	}

    @Override
    protected void cursedProc(Hero hero){
        int dmg=hero.absorb( damageRoll())/2;
        hero.damage(dmg, this, Element.ENERGY);
    }

	@Override
	protected void onZap( int cell ) {

        int power = curUser.magicSkill() / 3;

        Splash.at( cell, 0x33FFFFFF, (int) Math.sqrt(power) + 2 );

        Char ch = Actor.findChar( cell );

        if (ch != null) {


			int dmg=ch.absorb(damageRoll());

			if (curUser.belongings.weap1 instanceof MeleeWeapon && curUser.belongings.weap1.isEnchanted()){
				int currBonus=curUser.belongings.weap1.bonus;
				curUser.belongings.weap1.bonus=bonus+2;//bonus determine chance to proc, so we use this shitty hack
				curUser.belongings.weap1.enchantment.proc(curUser.belongings.weap1,curUser, ch, dmg);
				curUser.belongings.weap1.bonus=currBonus;
			}

			ch.damage(dmg, curUser, Element.ENERGY);

			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, Random.IntRange( 2 + power / 10 , 4 + power / 5 ) );

		}
	}


	@Override
	public String desc() {
		return
				"这个法杖的效果十分简单，它会释放出更纯粹的法术能量。它的效果很大程度上取决于你的意志和魔能，使它在一个强大的法师手里会变得更加强大";
	}


}
