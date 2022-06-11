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

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Overload;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Shocked;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.ItemStatusHandler;
import com.ravenwolf.nnypd.items.bags.Bag;
import com.ravenwolf.nnypd.items.rings.RingOfKnowledge;
import com.ravenwolf.nnypd.items.weapons.melee.LongStaff;
import com.ravenwolf.nnypd.items.weapons.melee.Quarterstaff;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public abstract class Wand extends EquipableItem {

	private static final int USAGES_TO_KNOW	= 8;
    protected static final float CURSE_PROC_CHANCE = 0.2f;


    public static final String AC_ZAP	= "释放";

    private static final String TXT_WOOD	= "这个%s法杖摸起来很温暖，谁知道它在使用时会发生什么呢？";

    protected static final String TXT_FIZZLES		= "失能";
    private static final String TXT_SQUEEZE		= "你从法杖中抽出了额外的充能";
    private static final String TXT_MISCAST		= "施法错误！";
    protected static final String TXT_SELF_TARGET	= "你不能瞄准自己！";

    private static final String TXT_IDENTIFY	= "你对手上的%s已经足够熟悉";
    private static final String TXT_UNEQUIPPED	= "你不能使用未装备的法杖";

    public static final float TIME_TO_ZAP	= 1f;

    public int curCharges = maxCharges();
    private float partialCharge=0f;
	
	protected Charger charger;
	

    protected int usagesToKnow = Random.IntRange(USAGES_TO_KNOW, USAGES_TO_KNOW * 2);

	protected boolean hitChars = true;
	protected boolean goThrough = false;

    private static final String[] woods =
            {"holly", "yew", "ebony", "cherry", "teak", "rowan", "willow", "mahogany", "bamboo", "purpleheart", "oak", "birch", "birch", "birch", "birch", "birch"};

    private static final Integer[] images = {
            ItemSpriteSheet.WAND_MAGICMISSILE,
            ItemSpriteSheet.WAND_DISINTEGRATION,
            ItemSpriteSheet.WAND_LIFEDRAIN,
            ItemSpriteSheet.WAND_LIGHTNING,
            ItemSpriteSheet.WAND_ACIDSPRAY,
            ItemSpriteSheet.WAND_FIREBRAND,
            ItemSpriteSheet.WAND_ICEBARRIER,
            ItemSpriteSheet.WAND_BLAST_WAVE,

            ItemSpriteSheet.CHARM_BLESS,
            ItemSpriteSheet.CHARM_BONE,
            ItemSpriteSheet.CHARM_BLINK,
            ItemSpriteSheet.CHARM_SHADOWS,
            ItemSpriteSheet.CHARM_THORN,
            ItemSpriteSheet.CHARM_BLAST,
            ItemSpriteSheet.CHARM_DOMINATION,
            ItemSpriteSheet.CHARM_JADE,
    };

    private static final Class<?>[] wands = {
            WandOfMagicMissile.class,
            WandOfFirebolt.class,
            WandOfLightning.class,
            WandOfFreezing.class,
            WandOfAcidSpray.class,
            WandOfAvalanche.class,
            WandOfDisintegration.class,
            //WandOfDisruptionField.class,
            WandOfSmiting.class,

            CharmOfDecay.class,
            CharmOfBlink.class,
            CharmOfDomination.class,
            CharmOfSanctuary.class,
            CharmOfWarden.class,
            CharmOfThorns.class,
            CharmOfShadows.class,
            CharmOfBlastWave.class
    };

    private static ItemStatusHandler<Wand> handler;

    public boolean dud = false;

    public static boolean allKnown() {
        return handler.known().size() == wands.length;
    }
    @Override
    public String equipAction() {
        return AC_ZAP;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? equipAction() /*AC_UNEQUIP*/ : AC_EQUIP;
    }

    @SuppressWarnings("unchecked")
    public static void initWoods() {
        handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images );
    }

    public static void save( Bundle bundle ) {
        handler.save( bundle );
    }

    @SuppressWarnings("unchecked")
    public static void restore( Bundle bundle ) {
        handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images, bundle );
    }

	public Wand() {
		super();
	}

    @Override
    public boolean doEquip( Hero hero ) {

        detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) &&
                ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

            hero.belongings.weap2 = this;
            activate(hero);

            onEquip( hero );

            QuickSlot.refresh();

            hero.spendAndNext(time2equip(hero));
            return true;

        } else {

            QuickSlot.refresh();
            hero.spendAndNext(time2equip(hero) * 0.5f);

            if ( !collect( hero.belongings.backpack ) ) {
                Dungeon.level.drop( this, hero.pos ).sprite.drop();
            }

            return false;

        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

        onDetach();

        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.weap2 = null;
            QuickSlot.refresh();

            return true;

        } else {

            activate( hero );
            return false;

        }
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }
	
	@Override
	public void activate( Char ch ) {
		charge(ch);
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_ZAP )) {
            if (isEquipped(hero)) {
                curUser = hero;
                curItem = this;
                GameScene.selectCell(zapper);
            } else {
                GLog.n( TXT_UNEQUIPPED, getType() );
            }
		} else {
			super.execute( hero, action );
		}
	}

    protected abstract void cursedProc(Hero hero);

	protected abstract void onZap( int cell );
	
	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null) {
				charge(container.owner);
			}
			return true;
		} else {
			return false;
		}
	}

    public void charge( Char owner ) {
		if (charger == null) {
			(charger = new Charger()).attachTo(owner);
		}
	}
	
	@Override
	public void onDetach() {
		stopCharging();
	}
	
	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}

    public int getCharges() {
        return curCharges;
    }

    public abstract int maxCharges( int bonus );
    public int maxCharges() {
        return maxCharges( bonus );
    };


    protected float rechargeRate() {
        return rechargeRate(bonus);
    }

    protected float rechargeRate(int bonus) {
        return 30f;
    }

    public float effectiveness() {
        return effectiveness(bonus);
    }

	public float effectiveness(int bonus) {

        float power = 0.5f + bonus * 0.1f ;

        if (charger != null) {

            Hero hero = (Hero)charger.target;

            if( hero.belongings.weap1 instanceof Quarterstaff || hero.belongings.weap1 instanceof LongStaff) {
                power += 0.05f * (hero.belongings.weap1.bonus + 2);
            }
            if(hero.hasBuff( Overload.class ))
                power += 0.15f;

            if( hero.hasBuff( Withered.class ))
                power *= 0.5f;

		}

        return power;
	}

    public int basePower(){
        return 14;
    }

    protected int getMaxDamage(int magicPower, float effectiveness){
        return (int)( (magicPower/2f +basePower()) * effectiveness);
    }

    //@Override
    public int damageRoll() {

        if( curUser != null ) {

            float eff = effectiveness();

            if (curUser.subClass == HeroSubClass.BATTLEMAGE){
                Combo combo = curUser.buff(Combo.class);
                if (combo !=null && combo.count > 2)
                    eff += 0.1f * combo.count;
            }

            int max = getMaxDamage(curUser.magicSkill, eff);
            int min = max * 3 / 5;

            return Random.IntRange( min, max );

        } else {

            return 0;

        }
    }

	@Override
	public Item identify() {
        super.identify();
        setKnown();

		updateQuickslot();
		
		return this;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder( super.toString() );
		
		String status = status();
		if (status != null) {
			sb.append(" (" + status + ")");
		}
		
		return sb.toString();
	}

    protected int distance(){
        return -1;
    }

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
    public boolean isTypeKnown() {
        return handler.isKnown( this );
    }

    public void setKnown() {
        if (!isTypeKnown()) {
            handler.know(this);
        }

        Badges.validateAllWandsIdentified();
    }

    public static HashSet<Class<? extends Wand>> getKnown() {
        return handler.known();
    }
	@Override
	public String status() {
		if (isIdentified()) {
			return getCharges() + "/" + maxCharges();
		} else {
			return null;
		}
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();

		curCharges = Math.min( curCharges + 1, maxCharges() );
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item curse() {
		super.curse();

        curCharges = curCharges > maxCharges() ? maxCharges() : curCharges ;
		updateQuickslot();
		
		return this;
	}

    public Wand initialCharges() {
        curCharges = maxCharges();
        updateQuickslot();

        return this;
    }

	protected void fx( int cell, Callback callback ) {
		MagicMissile.blueLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

    public void recharge( int turns ){

        if (charger != null) {
            Hero hero=Dungeon.hero;
            float turnsToCharge = rechargeRate() / hero.willpower();
            if (!isEquipped(hero))
                turnsToCharge*=2;

            partialCharge += turns / turnsToCharge;
            while (partialCharge >= 1) {
                curCharges = Math.min(maxCharges(), curCharges + 1);
                partialCharge--;
                updateQuickslot();
            }
            while (partialCharge <= -1) {
                curCharges = Math.max(0, curCharges - 1);
                partialCharge++;
                updateQuickslot();
            }
        }
    }

    //Fixme move to perks/subclasses
    protected void onChargesSpent(int charges){
        if (curUser.subClass == HeroSubClass.CONJURER){
            int duration = 4 * charges;
            BuffActive.add(curUser, Shielding.class, duration, false);
            for (Mob mob : Dungeon.level.mobs) {
                if (mob.isFriendly()) {
                    BuffActive.add(mob, Shielding.class, duration, false);
                }
            }
        }
    }

    protected void spendCharges() {
        onChargesSpent(1);
        if( curCharges > 0 ) curCharges--;
    }

	protected void wandUsed() {

        spendCharges();

		if (!isIdentified() ) {

            float effect = curUser.ringBuffs(RingOfKnowledge.Knowledge.class);

            usagesToKnow -= (int) effect;
            usagesToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

            if ( usagesToKnow <= 0 ) {
                identify();
                GLog.w(TXT_IDENTIFY, name());
            }
		}

        updateQuickslot();

		curUser.spendAndNext(TIME_TO_ZAP);
	}

    @Override
    public Item random() {

        float[][] chances = {
                { 90, 10, 0, 0, 0 },
                { 68, 24, 8, 0, 0 },
                { 50, 30, 16, 4, 0 },
                { 25, 40, 25, 10, 0 },
                { 0, 20, 45, 25, 5 },
        };
        int n= Random.chances( chances[ Dungeon.chapter() - 1 ] ) ;

        upgrade(n);
        if ( Random.Int( Dungeon.chapter() * 8  )+4 < Random.Int( n * 6 )+Dungeon.chapter() * 6  ) {
            cursed=true;
        }

        curCharges = maxCharges();
        return this;
    }

	
	@Override
	public int price() {
        return price(100);
	}

    public int priceModifier() { return 4; }
	
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {

                if (target == curUser.pos) {
                    GLog.i(TXT_SELF_TARGET);
                    return;
                }

                final Wand curWand = (Wand) Wand.curItem;

                if (curUser.buff(Dazed.class) != null && Random.Int(3)==0) {
                    target += Level.NEIGHBOURS8[Random.Int(8)];
                }

                final int cell;

                if (curWand.distance()>0) {
                     Ballistica.castToMaxDist(curUser.pos, target, curWand.distance()+1);
                    cell=Ballistica.trace[Ballistica.distance];
                }else{
                    cell = Ballistica.cast(curUser.pos, target, curWand.goThrough, curWand.hitChars);
                }

                if (cell == curUser.pos) {
                    GLog.i(TXT_SELF_TARGET);
                    return;
                }

                Char ch = Actor.findChar(cell);
                if (ch != null && curUser != ch && Dungeon.visible[cell]) {

                    QuickSlot.target(curItem, ch);
                    TagAttack.target((Mob) ch);
                }

                curUser.sprite.cast(cell);

                curUser.busy();


                if (curWand.curCharges > 0 ){
                    curWand.use(curWand instanceof WandUtility ? curWand.curCharges : 2);
                    curUser.buff( Satiety.class ).decrease( 1.0f );

                    if(curWand.isCursed()  && Random.Float()*curUser.willpower() <  CURSE_PROC_CHANCE){
                        GLog.w(TXT_MISCAST, curWand.getType());
                        curWand.cursedProc(curUser);
                    }

                    curWand.fx(cell, new Callback() {
                        @Override
                        public void call() {
                        curWand.onZap(cell);
                        curWand.wandUsed();

                            if (curUser.subClass == HeroSubClass.BATTLEMAGE){
                                Char c = Actor.findChar(cell );
                                if (c != null && curWand instanceof WandCombat){
                                    BuffActive.add(c, Revealed.class, 8, true);
                                }
                            }
                        }
                    });

                } else {
                    curUser.spendAndNext(TIME_TO_ZAP);
                    curUser.sprite.showStatus(CharSprite.WARNING, TXT_FIZZLES);
                }

                Invisibility.dispel();

                curWand.setKnown();
            }
		}
		
		@Override
		public String prompt() {
			return "选择要释放的方向";
		}
	};

    protected class Charger extends Buff {

        @Override
        public boolean act() {

            if (!target.hasBuff(Shocked.class) && !target.hasBuff(Disrupted.class) ) {
                if (curCharges < maxCharges()) {
                    recharge(1);

                    if (partialCharge >= 1) {
                        partialCharge--;
                        curCharges++;
                        updateQuickslot();
                    }
                }
            }

            spend( TICK );

            return true;

        }

    }

    protected String getType(){
//        return "wand";
		return "法杖";
    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder( desc() );
        info.append( p );

        if( !dud ){

            info.append( wandInfo() );
            info.append( p );

            if( isEquipped( Dungeon.hero ) ){
                info.append( "你正在拿着这个"+getType()+"。" );
            } else if( Dungeon.hero.belongings.backpack.contains( this ) ){
                info.append( "这个"+getType()+"正在你的背包里。" );
            } else {
                info.append( "这个"+getType()+"正在地面上。" );
            }

            if( isCursed() && isCursedKnown() ){

                info.append( " " );

                if( isEquipped( Dungeon.hero ) ){
                    info.append( "邪恶的魔法阻止了你将它卸下。" );
                } else {
                    info.append( "你能感受到它似乎充满了_恶意_的魔力。" );
                }
            }
        }

        return info.toString();
    }


	private static final String UNFAMILIRIARITY		= "unfamiliarity";
	private static final String CUR_CHARGES			= "curCharges";
    private static final String PARTIAL_CHARGE			= "partialCharge";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( CUR_CHARGES, curCharges );
        bundle.put( PARTIAL_CHARGE, partialCharge );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt( CUR_CHARGES );
        partialCharge = bundle.getFloat( PARTIAL_CHARGE );
	}

    protected abstract String wandInfo();

}