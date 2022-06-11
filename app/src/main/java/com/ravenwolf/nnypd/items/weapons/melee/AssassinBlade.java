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


import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;


public class AssassinBlade extends MeleeWeaponLight{

	{
		name = "暗杀之刃";
		image = ItemSpriteSheet.ASSASSIN_BLADE;
		drawId= WeaponSprite.ASSASSIN_BLADE;
		critical=new PierceCritical(this, true, 1f);
	}

	protected int[][] weapAtk() {
        return new int[][]{	{4, 2, 3, 0 },	//frame
                {-2, 2, 5, 2 },	//x
                {0, 0, 0, 0}};
	}

	protected int[][] weapBackstab() {
		return new int[][]{	{1, 2,  3, 4 },	//frame
				{2, 2,  5, -1 },	//x
				{0, 0, 0, 0}};
	}

	protected int[][] weapIddle() {
		return new int[][]{	{4, 4, 4, 4, 4, 4 ,4, 4 },	//frame
				{-1, -1, -1, -1, -1, -1 ,-1, -1 },	//x
				{0, 0, 0, 0, 0, 0 ,0, 0 }};
	}

	protected int[][] weapRun() {
		return new int[][]{	{4, 1, 1, 1, 4, 4  },	//frame
				{-1, 2, 4, 2, -1, -2  },	//x
				{0, 0, 0, 0, 0, 0 }};
	}

	protected int[][] weapFly() {
		return new int[][]{	{4 },	//frame
				{-1},	//x
				{0}};
	}

	public int[][] getDrawData(int action){

		if (action == HeroSprite.ANIM_BACKSTAB)
			return weapBackstab();
		else
			return super.getDrawData(action);
	}

	public AssassinBlade() {
		super(2 );
	}

	@Override
	public float getBackstabMod(){
		return 0.75f;
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}

	@Override
	public String desc() {
		return "这是一把制作精良的匕首，波浪状的刀刃使它能够更好的穿透敌人的护甲, 在对付未察觉你的敌人时会更加的致命。"
				+"\n\n这件武器会造成更强大的暴击效果，并且在对付未察觉你的敌人时更为有效";
		//return "A fine crafted sacrificial blade. These blades are made from an enchanted material that offer better control to those who have magical abilities, its also a deadly weapon against unaware foes.";
	}
}
