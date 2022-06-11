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
package com.ravenwolf.nnypd.items.weapons.ranged;


import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.hero.Hero;

import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.items.weapons.criticals.Critical;
import com.ravenwolf.nnypd.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.items.weapons.throwing.BluntedArrows;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.watabou.noosa.audio.Sample;


public abstract class RangedWeaponMissile extends RangedWeapon {

	public RangedWeaponMissile(int tier) {

		super( tier );

	}

    protected int[][] meleeAtk() {
        return new int[][]{	{1, 1, 0, 0 },	//frame
                {4, 4, 3, 3 },	//x
                {-1, -1, 0, 0}};
    }

    protected int[][] weapRun() {
        return new int[][]{	{0, 0, 0, 0, 1, 1  },	//frame
                {3, 3, 3, 3, 4, 4  },	//x
                {0, 0, 1, 0, -1, -2 }};
    }

    protected int[][] weapRangedAtk() {
        return new int[][]{	{1, 2, 3, 4, 5, 0 },	//frame
                {3, 3, 3, 3, 3, 3 },	//x
                {0, 0, 0, 0, 0, 0}};
    }

    public int[][] getDrawData(int action){

        if (action == HeroSprite.ANIM_RANGED_ATTACK)
            return weapRangedAtk();
        else if (action == HeroSprite.ANIM_ATTACK)
            return meleeAtk();
        else
            return super.getDrawData(action);
    }

    public Critical getCritical(){
        //FIXME should rework criticals to be switcheable
        EquipableItem ammo= Dungeon.hero.belongings.ranged;
        if (ammo instanceof BluntedArrows){
            if (!(critical instanceof BluntCritical)){
                critical=new BluntCritical(this, critical.isBetterCriticals(),critical.getCriticalModifier());
            }
        }else if (ammo instanceof Arrows){
            if (!(critical instanceof PierceCritical)){
                critical=new PierceCritical(this, critical.isBetterCriticals(),critical.getCriticalModifier());
            }
        }

        return critical;
    }

    @Override
    public int min( int bonus ) {
        return 1 +tier + bonus /*+ ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? 1 + bonus : -1 : 0 )*/ ;
    }

    @Override
    public int max( int bonus ) {
        return  4+ 4 * tier
                +  bonus * dmgMod();
               // + ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? tier + bonus : -tier : 0 );
    }

    public int dmgMod() {
        return tier ;
    }

    @Override
    public int str(int bonus) {
        return 8 + tier * 2 - strMod(bonus);
    }
    @Override
    public int penaltyBase() {
        return 4;
    }

    @Override
    public float breakingRateWhenShot() {
        //return 0.1f / Dungeon.hero.ringBuffs( RingOfDurability.Durability.class );
        return 0.1f ;
    }

    @Override
    protected boolean ammoEquipped(EquipableItem ammo) {
        return ammo instanceof Arrows ||  ammo instanceof BluntedArrows;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_SHOOT) {

            curUser = hero;
            curItem = this;

            if ( checkAmmo( hero, true ) ) {

                GameScene.selectCell(shooter);

            }

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    public String descType() {
        return "远程";
    }


    @Override
    protected void afterShoot(int cell ) {
        Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
    }

}
