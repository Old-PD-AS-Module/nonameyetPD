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
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.weapons.throwing.Quarrels;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.AdditionalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;


public abstract class RangedWeaponCrossbow extends RangedWeapon {

    public boolean loaded = false;

	public RangedWeaponCrossbow(int tier) {

		super( tier );

	}
    protected int[][] weapRangedAtk() {
        return new int[][]{	{4, 4, 5/*, 2, 0*/ },	//frame
                {3, 3, 3/*, 3, 3 */},	//x
                {0, 0, 0/*, 0, 0*/}};
    }
    protected int[][] weapAtkEnd() {
        return new int[][]{	{5, 5, 0 },	//frame
                {3, 3, 3 },	//x
                {0, 0, 0}};
    }


    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_RANGED_ATTACK)
            return weapRangedAtk();
        else if (action == HeroSprite.ANIM_RANGED_ATTACK_END)
            return weapAtkEnd();
        else
            return super.getDrawData(action);
    }

    public int getAdditionalDrawId(){
        if ( Dungeon.hero.belongings.ranged instanceof Quarrels)
            return AdditionalSprite.ADD_QUIVER;
        else
            return super.getAdditionalDrawId();
    }


    public static final String AC_RELOAD = "装弹";

    protected static final String TXT_ALREADY_LOADED = "这把弩已经装弹了";
    protected static final String TXT_RELOADING = "重新装弹...";

    @Override
    public boolean incompatibleWithShield() {
        return true;
    }

    @Override
    public boolean penetrateAC(){
        return true;
    }

    @Override
    public int min( int bonus ) {
        return tier + 3 + bonus /*+ ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? 1 + bonus : -1 : 0 )*/ ;
    }

    @Override
    public int max( int bonus ) {
        return 7+ tier * 5
                +  bonus * dmgMod();
                //+ ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? tier + bonus*2 : -dmgMod() : 0 );
    }

    public int dmgMod() {
        return tier /*+ 1*/;
    }


    @Override
    public int str(int bonus) {
        return 8 + tier * 2 - strMod(bonus);
    }

    @Override
    public int penaltyBase() {
        return 8/*super.penaltyBase()+tier * 2 + 2*/;
    }


    @Override
    public float breakingRateWhenShot() {
        //return 0.2f / Dungeon.hero.ringBuffs( RingOfDurability.Durability.class );
        return 0.075f;
    }

    @Override
    public String descType() {
        return "重弩";
    }


    @Override
    public String equipAction() {
        return loaded ? AC_SHOOT : AC_RELOAD ;
    }

    @Override
    protected boolean ammoEquipped(EquipableItem ammo) {
        return ammo instanceof Quarrels;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_SHOOT) {

            curUser = hero;
            curItem = this;

            if( !loaded ){

                execute( hero, AC_RELOAD );

            } else if ( checkAmmo( hero, true ) ){

                GameScene.selectCell(shooter);

            }

        } else if (action == AC_RELOAD) {

            curUser = hero;

            if( reload(hero ) ) {

                curUser.sprite.operate( curUser.pos );

                curUser.spend( getAttackDelay() * 0.5f );

                hero.buff( Satiety.class ).decrease( (float)str() / hero.STR() * 0.5f );

                hero.busy();

            } else if (!isEquipped(hero)) {

                GLog.n( TXT_NOTEQUIPPED );
                hero.ready();

            } else if ( loaded ) {

                GLog.n( TXT_ALREADY_LOADED );
                hero.ready();

            }

        } else {

            super.execute( hero, action );

        }
    }

    public boolean reload( Hero hero){

            curItem = this;

            loaded = true;

            curItem.updateQuickslot();

            Sample.INSTANCE.play( Assets.SND_TRAP, 0.6f, 0.6f, 0.5f );

            hero.sprite.showStatus( CharSprite.DEFAULT, TXT_RELOADING );

            return true;

    }

    @Override
    protected float bulletSpeed(){
        return 3f;
    }

    @Override
    protected void afterShoot(int cell ) {
        loaded = false;
        //play end animation
        ((HeroSprite) curUser.sprite).shootEnd(this);
        Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
    }


    private static final String LOADED	= "loaded";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LOADED, loaded );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        loaded = bundle.getBoolean( LOADED );
    }
}