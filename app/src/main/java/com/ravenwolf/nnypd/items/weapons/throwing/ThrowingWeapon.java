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
package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.special.PinCushion;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Chains;
import com.ravenwolf.nnypd.visuals.effects.Effects;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class ThrowingWeapon extends Weapon {

//	private static final String TXT_MISSILES	= "Missile weapon";

//	private static final String TXT_YES			= "Yes, I know what I'm doing";
//	private static final String TXT_NO			= "No, I changed my mind";
//	private static final String TXT_R_U_SURE	=
//		"Do you really want to equip it as a melee weapon?";

    private static final String TXT_TARGET_CHARMED	= "You can't bring yourself to harm someone so... charming.";
    protected static final String TXT_SELF_TARGET	= "你不能把自己当做目标";
    private static final String AC_SHOOT = "SHOOT";

    public ThrowingWeapon(int tier) {
        super();

        this.tier = tier;

        stackable = true;
    }

    @Override
    public boolean increaseCombo(){
        return false;
    }

    @Override
    public String equipAction() {
        return AC_SHOOT;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_SHOOT/*AC_UNEQUIP*/ : AC_EQUIP;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.ranged != null && getClass().equals(hero.belongings.ranged.getClass());

    }

    @Override
    public int penaltyBase() {
        return 0;
    }

    @Override
    public int lootChapter() {
        return tier;
    }

    @Override
    public int lootLevel() {
        return ( lootChapter() ) * 2 + 4 * quantity / baseAmount();
    }

    public int baseAmount() {
        return 1;
    }

    @Override
    public int priceModifier() { return 2; }

    @Override
    public Item random() {

        quantity = baseAmount() * 2/3;
        int bonusAmount=Random.Int( quantity );
        quantity +=  bonusAmount * ( 2 + Dungeon.chapter() - lootChapter() ) / 4;


        return this;
    }

    public boolean stick(Char enemy){
        return false;
    }

    @Override
    public float breakingRateWhenShot() {
        return 0.06f*quantity/(baseAmount()*lootChapter());
    }


    public void activate( Char ch ) {
        ((Hero)ch).belongings.lastEquipedAmmo = this.getClass();
    }

    @Override
    public boolean doEquip( final Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

            if( QuickSlot.quickslot1.value == getClass() && ( hero.belongings.ranged == null || hero.belongings.ranged.bonus >= 0 ) )
                QuickSlot.quickslot1.value = hero.belongings.ranged != null && hero.belongings.ranged.stackable ? hero.belongings.ranged.getClass() : hero.belongings.ranged ;

            if( QuickSlot.quickslot2.value == getClass() && ( hero.belongings.ranged == null || hero.belongings.ranged.bonus >= 0 ) )
                QuickSlot.quickslot2.value = hero.belongings.ranged != null && hero.belongings.ranged.stackable ? hero.belongings.ranged.getClass() : hero.belongings.ranged ;

            if( QuickSlot.quickslot3.value == getClass() && ( hero.belongings.ranged == null || hero.belongings.ranged.bonus >= 0 ) )
                QuickSlot.quickslot3.value = hero.belongings.ranged != null && hero.belongings.ranged.stackable ? hero.belongings.ranged.getClass() : hero.belongings.ranged ;

            if (hero.belongings.ranged == null || hero.belongings.ranged.doUnequip(hero, true, false)) {

                hero.belongings.ranged = this;
                hero.updateWeaponSprite();

                activate(hero);

                QuickSlot.refresh();
                GLog.i(TXT_EQUIP, name());
                hero.spendAndNext(time2equip(hero));
                return true;

            } else {

                collect(hero.belongings.backpack);
                return false;

            }
        } else {

            GLog.w(TXT_ISEQUIPPED, name());
            return false;

        }
    }

    public boolean returnOnHit(Char enemy){
        return false;
    }

    protected boolean returnOnMiss(){
        return false;
    }

    @Override
    public boolean doPickUp( Hero hero ) {

        Class<?>c = getClass();

        if ((hero.belongings.ranged != null && hero.belongings.ranged.getClass() == c) || c.equals( hero.belongings.lastEquipedAmmo) ){


            if(hero.belongings.ranged==null) {
                //doPickUp( hero );
                //detachAll(hero.belongings.backpack);
                hero.belongings.ranged=this;
            }else {
                hero.belongings.ranged.quantity += quantity;
            }
            GameScene.pickUp( this );
            Sample.INSTANCE.play(Assets.SND_ITEM);

//            hero.spendAndNext(TIME_TO_PICK_UP);

            QuickSlot.refresh();

            return true;

        } else {

            return super.doPickUp(hero);

        }

    }

    @Override
    public String info() {

        final String p = "\n\n";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = currentPenalty(Dungeon.hero, strShown(isIdentified())) * 2.5f;
        //float penalty = GameMath.gate(0, currentPenalty(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;

        StringBuilder info = new StringBuilder( desc() );

        info.append( p );

        info.append( "这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + " " : "" )  + "武器_需要_" + itemStr + "点力量_才能正常使用" +

                "，每次攻击可以造成_" + min() + "-" + max() + "点伤害_。");

        info.append( p );

        if (itemStr > heroStr) {
            info.append(
                    // "Because of your inadequate strength, your stealth and accuracy with it " +
                    "由于你的力量不足，使用该武器时，你的命中" +
                            "会降低_" + penalty + "%_同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的攻击速度_。" );
        } else if (itemStr < heroStr) {
            info.append(
                    //"Because of your excess strength, your stealth and accuracy with it " +
                    "由于你拥有额外的力量，所以在使用该武器时你的命中"
                            + ( penalty > 0 ? "只会_降低" + penalty + "%_" : "_不会降低_" ) + " " +
                            "并且额外增加_" + (float)(heroStr - itemStr) / 2 + "点伤害_。" );
        } else {
            info.append(
                    //"When wielding this weapon, your stealth and accuracy with it will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_, " +
                    "当你使用该武器时，你的命中" + ( penalty > 0 ? "会_降低" + penalty + "%_, " +
                            "当你的力量超过装备所需力量时，则会减轻该惩罚" : "_不会降低_" ) + "。" );
        }

        info.append( p );
        info.append("这种武器被设计用于远距离使用，在近战范围内使用精度会降低很多。" );


        info.append( p );

        /*
        //removed as there is no way of identifying items on the ground
        if (isEquipped( Dungeon.hero )) {

            info.append( "You hold these " + name + " at the ready." );

        } else if( Dungeon.hero.belongings.backpack.contains(this) ) {

            info.append( "These " + name + " are in your backpack. " );

        } else {

            info.append( "These " + name + " are on the dungeon's floor." );

        }*/

        info.append( "这是一件_" + lootChapterAsString() +"_ 武器。" );

        return info.toString();
    }

    protected float getAttackDelay() {
        curUser.rangedWeapon = this;
        float delay=curUser.attackDelay();
        curUser.rangedWeapon = null;
        return delay;
    }

    @Override
    public void execute( Hero hero, String action ) {

        if (action == AC_SHOOT) {

            curUser = hero;
            curItem = this;

            if (!isEquipped(hero)) {

                super.execute(hero, AC_THROW);

            } else {

                GameScene.selectCell( shooter );

            }

        } else {

            super.execute( hero, action );

        }
    }

    public static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                final ThrowingWeapon curWeap = (ThrowingWeapon)ThrowingWeapon.curItem;

//                int tmp_cell = target;

                if( curUser.buff( Dazed.class ) != null && Random.Int(3)==0) {
                    target += Level.NEIGHBOURS8[Random.Int( 8 )];
                }

                if (curWeap instanceof Chakrams){
                    if (((Chakrams)curWeap).chakramShoot(target)) {
                        Invisibility.dispel();
                        Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
                    }
                    return;
                }

                final int cell = Ballistica.cast(curUser.pos, target, false, true);

                if (cell == curUser.pos) {
                    GLog.i(TXT_SELF_TARGET);
                    return;
                }

                final Char ch = Actor.findChar( cell );

                if( ch != null && curUser != ch && Dungeon.visible[ cell ] ) {

//                    if ( curUser.isCharmedBy( ch ) ) {
//                        GLog.i( TXT_TARGET_CHARMED );
//                        return;
//                    }

                    QuickSlot.target(curItem, ch);
                    TagAttack.target( (Mob)ch );
                }


                curUser.sprite.cast(cell, new Callback() {
                    @Override
                    public void call() {

                    curUser.busy();

                    if (curWeap instanceof Harpoons) {
                        curUser.sprite.parent.add(new Chains(curUser.pos, cell, ch != null && ch.isHeavy(), Effects.Type.CHAIN));
                    }

                    ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                            reset(curUser.pos, cell, curWeap, new Callback() {
                                @Override
                                public void call() {
                                    ( curWeap).onShoot(cell, curWeap);
                                }
                            });

                    curUser.buff( Satiety.class ).decrease( (float)curWeap.str() / curUser.STR() );

                    }
                });

                Invisibility.dispel();
                Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

            }
        }

        @Override
        public String prompt() {
            return "选择投掷目标";
        }
    };

    public void onShoot( int cell, Weapon weapon ) {
        Char enemy = Actor.findChar(cell);
        if( enemy == curUser ) {
            super.onThrow(cell);
        } else if( enemy == null || !curUser.shoot(enemy, weapon) ) {
            //moonglaive bounce even if miss
            if (enemy != null && this instanceof MoonGlaive && ((MoonGlaive)this).bounce(enemy, false) ) {
                return;
            }
            if (returnOnMiss()) {
                ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
                        reset(cell, curUser.pos, this instanceof Harpoons ? ItemSpriteSheet.HARPOON_RETURN : curItem.imageAlt(), null);
                curUser.belongings.ranged = this;
            } else {
                super.onThrow(cell);
            }
        } else if( Random.Float() > weapon.breakingRateWhenShot() || this instanceof MoonGlaive) {
            curUser.belongings.ranged = this;
            //moonglaive break chance is calculated on bounce method bouncing
            if (this instanceof MoonGlaive && ((MoonGlaive)this).bounce(enemy, true) ) {
                return;
            }
            if (returnOnHit(enemy)) {
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(cell, curUser.pos, this instanceof Harpoons ? ItemSpriteSheet.HARPOON_RETURN : curItem.imageAlt(), null);
            } else {
                if(this.stick(enemy) && enemy.isAlive()){
                    PinCushion pin=Buff.affect(enemy, PinCushion.class);
                    if(pin!=null) {
                        ThrowingWeapon drop;
                        if (quantity == 1) {
                            drop = this;
                            doUnequip(curUser, false, false);
                        } else
                            drop = (ThrowingWeapon) this.detach(null);
                        pin.stick(drop);
                    }else
                        super.onThrow(cell);
                }else
                    super.onThrow(cell);
            }

        } else {
            missileBreak(enemy);
        }

        curUser.rangedWeapon = weapon;
        curUser.spendAndNext(curUser.attackDelay()/*1/weapon.speedFactor( curUser )*/ );
        curUser.rangedWeapon = null;
        QuickSlot.refresh();
    }

    protected void missileBreak( Char enemy ) {
        enemy.sprite.showStatus( CharSprite.DEFAULT, "投武损耗" );
        if (quantity == 1) {
            doUnequip( curUser, false, false );
        } else {
            detach( null );
        }
    }
}
