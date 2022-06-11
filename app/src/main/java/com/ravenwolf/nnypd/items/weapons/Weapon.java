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
package com.ravenwolf.nnypd.items.weapons;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.rings.RingOfAccuracy;
import com.ravenwolf.nnypd.items.rings.RingOfKnowledge;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.items.weapons.criticals.Critical;
import com.ravenwolf.nnypd.items.weapons.enchantments.Arcane;
import com.ravenwolf.nnypd.items.weapons.enchantments.Blazing;
import com.ravenwolf.nnypd.items.weapons.enchantments.Caustic;
import com.ravenwolf.nnypd.items.weapons.enchantments.Disrupting;
import com.ravenwolf.nnypd.items.weapons.enchantments.Eldritch;
import com.ravenwolf.nnypd.items.weapons.enchantments.Ethereal;
import com.ravenwolf.nnypd.items.weapons.enchantments.Freezing;
import com.ravenwolf.nnypd.items.weapons.enchantments.Heroic;
import com.ravenwolf.nnypd.items.weapons.enchantments.Shocking;
import com.ravenwolf.nnypd.items.weapons.enchantments.Unstable;
import com.ravenwolf.nnypd.items.weapons.enchantments.Vampiric;
import com.ravenwolf.nnypd.items.weapons.enchantments.Unholy;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Weapon extends EquipableItem {

	private static final int HITS_TO_KNOW	= 10;

    private static final String TXT_IDENTIFY		=
            "你对你的%s已经足够熟悉并且可以将其完全鉴定。它是%s。";
//	private static final String TXT_INCOMPATIBLE	=
//		"Interaction of different types of magic has negated the enchantment on this weapon!";
	private static final String TXT_TO_STRING		= "%s :%d";

//    private static final String TXT_BROKEN	= "Your %s has broken!";
	
	public int	tier = 1;

    private boolean increaseCombo=true;
    protected Critical critical=null;
//	public int		STR	= 10;
//	public float	ACU	= 1;
//	public float	DLY	= 1f;


    //weapon animations
    protected int[][] weapIddle() {

        return new int[][]{	{0, 0, 0, 0, 0, 0 ,0, 0 },	//frame
                {3, 3, 3, 3, 3, 3 ,3, 3 },	//x
                {0, 0, 0, 0, 0, 0 ,0, 0 }};
    }

    protected int[][] shieldSlam() {
        return new int[][]{	{0, 0, 0, 0, },	//frame
                {3, 3, 1, 3, },	//x
                {0, 0, 0, 0, }};
    }

    protected int[][] weapFly() {
        return new int[][]{	{0 },	//frame
                {3},	//x
                {0}};
    }

    protected int[][] weapRun() {
        return new int[][]{	{0, 0, 1, 1, 0, 0  },	//frame
                {2, 3, 5, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
    }

    @Override
    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_RUN)
            return weapRun();
        else if (action == HeroSprite.ANIM_FLY)
            return weapFly();
        else if (action == HeroSprite.ANIM_IDLE) {
            return weapIddle();
        }else if (action == HeroSprite.ANIM_SLAM) {
                return shieldSlam();
        }else
            return null;
    }

    public enum Type {
        M_SWORD, M_BLUNT, M_POLEARM,
        R_MISSILE, R_FLINTLOCK, UNSPECIFIED
    }
	
	protected int hitsToKnow = Random.IntRange(HITS_TO_KNOW, HITS_TO_KNOW * 2);
	
	public Enchantment enchantment;

    public Critical getCritical(){
        return critical;
    }


    public boolean increaseCombo(){
        return increaseCombo;
    }

    public void setIncreaseCombo(boolean value){
        increaseCombo=value;
    }

    public boolean penetrateAC(){
        return false;
    }

    public int dmgMod() {
        return tier ;
    }

    public int guardStrength(){
        return guardStrength( bonus);
    }

    public int guardStrength(int bonus){
        return 2 + tier + bonus;
    }


    public float counterBonusDmg(){
        return 0.3f;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap1 == this || hero.belongings.weap2 == this;
    }

    @Override
    public boolean doEquip( Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

            if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap1 == null || !hero.belongings.weap1.isCursed() ) )
                QuickSlot.quickslot1.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap1 == null || !hero.belongings.weap1.isCursed() ) )
                QuickSlot.quickslot2.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap1 == null || !hero.belongings.weap1.isCursed() ) )
                QuickSlot.quickslot3.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if ( ( hero.belongings.weap1 == null || hero.belongings.weap1.doUnequip(hero, true, false) ) &&
                    ( !isCursed() || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                if (incompatibleWithShield() && hero.belongings.weap2 instanceof Shield ){
                    if (!hero.belongings.weap2.doUnequip(hero, true, false)){
                        QuickSlot.refresh();
                        hero.spendAndNext(time2equip(hero) * 0.5f);

                        if ( !collect( hero.belongings.backpack ) ) {
                            Dungeon.level.drop( this, hero.pos ).sprite.drop();
                        }

                        return false;
                    }
                }

                hero.belongings.weap1 = this;
                activate(hero);

                onEquip( hero );

                QuickSlot.refresh();

                hero.spendAndNext(time2equip(hero));
                hero.updateWeaponSprite();
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

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.weap1 = ( hero.belongings.weap1 == this ? null : hero.belongings.weap1 );

            hero.belongings.weap2 = ( hero.belongings.weap2 == this ? null : hero.belongings.weap2 );

            hero.belongings.ranged = ( hero.belongings.ranged == this ? null : hero.belongings.ranged );

            hero.updateWeaponSprite();

            QuickSlot.refresh();

            return true;

        } else {

            return false;

        }
    }

 public int proc( Char attacker, Char defender, int damage ) {

        if (getCritical()!=null)
            damage=getCritical().proc( attacker, defender, damage );

		if ( enchantment != null ) {
			if( enchantment.proc( this, attacker, defender, damage ) && !isEnchantKnown() ) {
                identify(ENCHANT_KNOWN);
            }
		}
		
		if (!isIdentified() && attacker instanceof Hero) {

            float effect = attacker.ringBuffs(RingOfKnowledge.Knowledge.class);

            hitsToKnow -= 1 ;
            hitsToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

			if (hitsToKnow <= 0) {
				identify();
				GLog.i( TXT_IDENTIFY, name(), toString() );
//				Badges.validateItemLevelAcquired(this);
			}
		}
		return damage;
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String IMBUE			= "imbue";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( ENCHANTMENT, enchantment );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchant( (Enchantment) bundle.get( ENCHANTMENT ) );
	}

	public int damageRoll( Hero hero ) {

		int dmg = Math.max( 0, Random.NormalIntRange( min(), max() ) );

        Combo combo = hero.buff( Combo.class );
        if( combo != null && this instanceof MeleeWeapon){//ranged weapons dont benefit from combo
            int xComboBonus = (int) ((max() - min()) * combo.modifier2() * hero.ringBuffsHalved(RingOfAccuracy.Accuracy.class));
            if (xComboBonus>0) {
                //reroll increasing min damage
                int auxDmg =Math.max(0, Random.NormalIntRange(xComboBonus + min(), max()));
                if (auxDmg>dmg)
                    dmg = auxDmg;
            }
        }

        dmg +=getDamageSTBonus(hero);
        return dmg;
    }

    protected int getDamageSTBonus(Hero hero){
        int exStr = hero.STR() - strShown( true );

        if (exStr > 0) {
            return Random.IntRange( 0, exStr );
        }
        return 0;
    }

    public float getBackstabMod() {
        return 0.15f;
    }

    public boolean incompatibleWithShield() {
        return false;
    }

    @Override
    public boolean disarmable() {
        return super.disarmable() && !(enchantment instanceof Heroic);
    }

    public int strMod(int bonus) {
        return ( enchantment instanceof Ethereal & isCursedKnown()? !isCursed()? 1 + bonus/2 : -1 : 0 ) + ( bonus+1)/2;
    }

    public int min( int bonus ) {
        return 0;
    }

    public int max( int bonus ) {
        return 0;
    }

    public int min() {
        return min(bonus);
    }

    public int max() {
        return max(bonus);
    }

    @Override
    public Item uncurse() {

        if(isCursed()) {
            enchant(null);
        }

        return super.uncurse();
    }

    @Override
    public int lootChapter() {
        return tier;
    }

    //FIXME
    public float breakingRateWhenShot() {
        return 0f;
    }

    @Override
    public int priceModifier() { return 4; }

	@Override
	public String name() {
        return enchantment != null && isEnchantKnown() ? enchantment.name( this ) : super.name();
    }

    public String simpleName() {
        return super.name();
    }

    public Type weaponType() { return Type.UNSPECIFIED; }

	public Weapon enchant( Enchantment ench ) {

        enchantment = ench;

		return this;
	}
	
	public Weapon enchant() {
		
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while (ench.getClass() == oldEnchantment) {
			ench = Enchantment.random();
		}
		
		return enchant( ench );
	}
	
	public boolean isEnchanted() {
		return enchantment != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && isEnchantKnown() ? enchantment.glowing() : null;
	}


	public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[]{
            Blazing.class, Shocking.class, Freezing.class, Caustic.class,  /*Heroic.class,*/Disrupting.class,
            Arcane.class, Vampiric.class, /*Tempered.class, Ethereal.class,*/ Unholy.class, Unstable.class,  Eldritch.class };

		private static final float[] chances = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

        protected static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF5511 );
        protected static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
        protected static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x009900 );
        protected static ItemSprite.Glowing MUSTARD = new ItemSprite.Glowing( 0xBBBB33 );
        protected static ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0x00AAFF );
        protected static ItemSprite.Glowing GRAY = new ItemSprite.Glowing( 0x888888 );
        protected static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );
        protected static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x2244FF );
        protected static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
        protected static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0xAA00AA );
        protected static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF44 );
        protected static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x008080 );

        protected abstract String name_p();
        protected abstract String name_n();
        protected abstract String desc_p();
        protected abstract String desc_n();

        protected abstract boolean proc_p( Char attacker, Char defender, int damage );
        protected abstract boolean proc_n( Char attacker, Char defender, int damage );

        public boolean public_proc_p( Char attacker, Char defender, int damage ) {
            return proc_p(attacker, defender, damage);
        }

        public static boolean procced(Char attacker, int bonus ) {

            return Random.Float() < (0.1f + 0.05f * ( 1 + bonus ))
                    * ( attacker.ringBuffs( RingOfSorcery.Sorcery.class ) );
        }

        public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {

           /* boolean result = procced( weapon.isCursed()? -1 :weapon.bonus )
            && ( !weapon.isCursed()
                    ? proc_p(attacker, defender, damage)
                    : proc_n(attacker, defender, damage)
            );*/
            boolean result = procced(attacker, weapon.bonus )
                && ( !weapon.isCursed() || Random.Float() <0.4f*attacker.willpower()
                    ? proc_p(attacker, defender, damage)
                    : proc_n(attacker, defender, damage)
                );

            if( result ) {
                weapon.identify( ENCHANT_KNOWN );
            }

            return result;
        }

        public String name( Weapon weapon ) {
            return String.format( !weapon.isCursed() ? name_p() : name_n(), weapon.name );
        }

        public String desc( Weapon weapon ) {
            //return !weapon.isCursed() ? desc_p() : desc_n();
            return desc_p() + (weapon.isCursed() ? "。由于这个武器是被诅咒的，所以它经常会对穿戴者造成相反的效果" : "");
        }

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}

		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}

		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
	}

    public String descType() {
        return "";
    }

    @Override
    public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = currentPenalty(Dungeon.hero, strShown(isIdentified())) * 2.5f;

        StringBuilder info = new StringBuilder( desc() );


        info.append( p );

/*        if (isIdentified()) {
            info.append( "This _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )
                    *//*+ ( critical!=null ? critical.name() + " " : "" ) *//* + "weapon_ " +
                    "requires _" + itemStr + " strength_ to use effectively and" +
                    //( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    "will deal _" + min() + "-" + max() + " damage_ per hit.");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and accuracy with it " +
                        "will be _decreased by " + penalty + "%_ and attacking with it will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else
//                if (itemStr < heroStr) {
//                info.append(
//                        "Because of your excess strength, your stealth and accuracy with it " +
//                        "will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
//                        "and attacking with it will deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
//            } else {
//                info.append(
//                        "When wielding this weapon, your stealth and accuracy with it will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_, " +
//                        "but with additional strength this penalty can be reduced" : "_not be decreased_" ) + "." );
//            }
            {
                info.append(
                        "When wielding this weapon, your stealth and accuracy with it will " + (penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_") + ".");
                if (itemStr < heroStr)
                    info.append(
                            "Because of your excess strength attacking with it will deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
            }
            if (this instanceof MeleeWeapon)
                info.append(" When used to block, it will provide a _defensive power of " + guardStrength() + "_." );
        } else {
            info.append(  "Usually _tier-" + tier + " " + ( !descType().isEmpty() ? descType() + " " : "" )  + "weapons_ require _" + itemStr + " points of strength_ to use effectively and " +
                   // ( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    "deal _" + min(0) + "-" + max(0) + " points of damage_ per hit." );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and accuracy with it " +
                                "probably will be _decreased by " + penalty + "%_ and attacking with it will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else
//                if (itemStr < heroStr) {
//                info.append(
//                        "Because of your excess strength, your stealth and accuracy with it " +
//                                "probably will " + ( penalty > 0 ? "be _decreased only by " + penalty + "%_" : "_not be decreased_" ) + " " +
//                                "and attacking with it will deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
//            } else {
//                info.append(
//                        "When wielding this weapon, your stealth and accuracy with it probably will " +
//                                ( penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_" ) +
//                                ", unless your strength will be different from this weapon's actual strength requirement." );
//            }

            {
                info.append(
                        "When wielding this weapon, your stealth and accuracy with it will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_" : "_not be decreased_" ) + "." );
                if (itemStr < heroStr)
                    info.append(
                            " Because of your excess strength  attacking with it will probably deal additional _0-" + (heroStr - itemStr) + " points of damage_." );
            }
            if (this instanceof MeleeWeapon)
                info.append(" When used to block, it will usually provide a _defensive power of " + guardStrength(0) + "_." );
        }

        if (this instanceof ThrowingWeapon || this instanceof RangedWeapon){
            info.append( p );
            info.append(" This weapon is designed to be used at a distance, it is much less accurate at melee range." );
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append("You hold the " + name + " at the ready.");

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "The " + name + " is in your backpack." );

        } else {

            info.append( "The " + name + " lies on the dungeon's floor." );

        }

        info.append( s );

        if( isIdentified() && bonus > 0 ) {
            info.append( "It appears to be _upgraded_." );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "It appears to be _non-cursed_." :
                    "A malevolent _curse_ seems to be lurking within this " + name +"." );
        } else {
            info.append( " This " + name + " is _unidentified_." );
        }

        info.append( s );

        if( isEnchantKnown() && enchantment != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) +
                    ", it seems to be _enchanted to " + enchantment.desc(this) + "_." );
        }

        info.append( " This is a _" + lootChapterAsString() +"_ weapon." );*/
        if (isIdentified()) {
            info.append( "这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + " " : "" )  + "武器_" +
                    "需要_" + itemStr + "点力量_才能正常使用，" +
                    //( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    "每次攻击可以造成_" + min() + "-" + max() + "点伤害_。");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "由于你的力量不足，装备该武器时会使你的潜行和命中_降低 " + penalty + "%_同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的攻击速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "由于你拥有额外的力量，所以装备该武器时你的潜行和命中" + ( penalty > 0 ? "只会_降低 " + penalty + "%_" : "_不会降低_" ) + "，" +
                                "并且额外增加_0-" + (heroStr - itemStr) + "点伤害_。" );
            } else {
                info.append(
                        "当你装备该武器时，你的潜行和命中" + ( penalty > 0 ? "会_降低" + penalty + "%_, " +
                                "当你的力量超过装备所需力量时，则会减轻该惩罚" : "_不会降低_" ) + "。" );
            }
            if (this instanceof MeleeWeapon)
                info.append("当用来格挡时, 它会提供_" + guardStrength() + "点防御力_。" );
        } else {
            info.append(  "通常这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + " " : "" )  + "武器_需要_" + itemStr + "点力量_才能正常使用，" +
                    // ( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    "每次攻击可以造成_" + min(0) + "-" + max(0) + "点伤害_。" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "由于你的力量不足，装备该武器时会使你的潜行和命中_降低 " + penalty + "%_同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的攻击速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "由于你拥有额外的力量，所以装备该武器时你的潜行和命中" + ( penalty > 0 ? "只会_降低" + penalty + "%_" : "_不会降低_" ) + "，" +
                                "并且额外增加_0-" + (heroStr - itemStr) + "点伤害_。" );
                info.append(
                        "当你装备该武器时，你的潜行和命可能" + ( penalty > 0 ? "会_降低" + penalty + "%_, " : "_不会降低_" + ", " ) +
                                "除非你的力量不同于装备的力量需求" + "。" );
            }
            if (this instanceof MeleeWeapon)
                info.append("当用来格挡时, 它通常会提供_" + guardStrength(0) + "点防御力_。" );
        }

        if (this instanceof ThrowingWeapon){
            info.append( p );
            info.append("这种武器被设计用于远距离使用，在近战范围内使用精度会降低很多。" );
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append("你正装备着" + name + "。");

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "这件" + name + "正在你的背包里。" );

        } else {

            info.append( "这件" + name + "正在地面上。" );

        }

        info.append( s );

        if( isIdentified() && bonus > 0 ) {
            info.append( "它似乎已被_升级_。" );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "它看起来并_没有被诅咒_。" :
                    "你能感受到它似乎充满了_恶意_的魔力"/* + name */+"。" );
        } else {
            info.append( "这件" + name + "尚_未被鉴定_。" );
        }

        info.append( s );

        if( isEnchantKnown() && enchantment != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) +
                    "，它携带着_" + enchantment.desc(this) + "_的附魔。" );
        }

        info.append( "这是一件_" + lootChapterAsString() +"_武器。" );

        return info.toString();
    }
}
