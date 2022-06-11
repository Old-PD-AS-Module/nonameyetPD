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
package com.ravenwolf.nnypd.items.armours.shields;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class Shield extends Armour {


    private final int[][] idleFrames = {	{0, 0, 0, 0, 0, 0 ,0, 0, 0, 0 },	//frame
            {4, 4, 4, 4, 4, 4 ,4, 4, 4, 4 },	//x
            {1, 1, 1, 1, 1, 1 ,1, 1, 1, 1 }};//y

    private final int[][] runFrames = {	{0, 1, 1, 0, 0, 0  },	//frame
            {4, 4, 4, 4, 4, 5  },	//x
            {1, 1, 1, 1, 1, 1 }};//y

    private final int[][] flyFrames = {	{0, 0, 0, 0 },	//frame
            {5 },	//x
            {1}};//y

    private final int[][] slamFrames = {	{0, 0, 0, 0 },	//frame
            {4, 3, 6, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] atkFrames = {	{0, 0, 1, 0 },	//frame
            {5, 5, 4, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] stabFrames = {	{0, 1, 1, 0 },	//frame
            {5, 4, 4, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] doubleHitFrames = {	{0, 1, 1, 1, 0 },	//frame
            {5, 4, 4, 3, 4 },	//x
            {1, 1, 1, 1 , 1}};//y


    @Override
    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_IDLE )
            return idleFrames;
        else if (action == HeroSprite.ANIM_RUN)
            return runFrames;
        else if (action == HeroSprite.ANIM_ATTACK || action==HeroSprite.ANIM_CAST || action==HeroSprite.ANIM_BACKSTAB  || action ==  HeroSprite.ANIM_RANGED_ATTACK )
            return atkFrames;
        else if (action == HeroSprite.ANIM_STAB )
            return stabFrames;
        else if (action == HeroSprite.ANIM_DOUBLEHIT )
            return doubleHitFrames;
        else if (action == HeroSprite.ANIM_SLAM)
            return slamFrames;
        else if (action == HeroSprite.ANIM_FLY)
            return flyFrames;
        else
            return null;
    }

    public Shield(int tier) {

        super(tier);
        hitsToKnow/=2;

    }

/*    private static final String TXT_NOTEQUIPPED = "You have to equip this shield first.";
    private static final String TXT_SLAM_FAIL = "There is no nearby enemy to slam.";
    private static final String TXT_GUARD = "guard";

    private static final String AC_GUARD = "GUARD";

    private static final String AC_SHIELD_SLAM = "SLAM";*/
    private static final String TXT_NOTEQUIPPED = "你需要先装备该盾牌";
    private static final String TXT_SLAM_FAIL = "附近没有可以进行猛击的敌人";
    private static final String TXT_GUARD = "格挡";

    private static final String AC_GUARD = "格挡";

    private static final String AC_SHIELD_SLAM = "猛击";


    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    public float counterMod() {
        return 0.3f;
    }

    public boolean bonusAgainstRanged() {
        return false;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_SHIELD_SLAM /*AC_UNEQUIP */: AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_GUARD );
//        return actions;
//    }

    public int guardTurns(){
        return 6;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {

                GLog.n(TXT_NOTEQUIPPED);

            }  else {

                hero.buff( Satiety.class ).decrease( (float)str() / hero.STR() );
                //Buff.affect( hero, Guard.class).reset(guardTurns());
                int duration = guardTurns();
                if (hero.subClass == HeroSubClass.AMAZON)
                    duration++;
                BuffActive.add(hero, Guard.class, duration, true);
                hero.spendAndNext( Actor.TICK );

            }

        }else if (action.equals(AC_SHIELD_SLAM)) {

                curUser = hero;
                curItem = this;
                GameScene.selectCell(slamer);

        }else {

            super.execute( hero, action );

        }
    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) &&
                ( !isCursed() || isCursedKnown() || !detectCursed( this, hero ) ) ) {

			if (hero.belongings.weap1!=null && hero.belongings.weap1.incompatibleWithShield()){
                if (!hero.belongings.weap1.doUnequip(hero, true, false)){
                    QuickSlot.refresh();
                    hero.spendAndNext(time2equip(hero) * 0.5f);

                    if ( !collect( hero.belongings.backpack ) ) {
                        Dungeon.level.drop( this, hero.pos ).sprite.drop();
                    }

                    return false;
                }
            }

			hero.belongings.weap2 = this;

            onEquip( hero );
            if (hero.subClass == HeroSubClass.AMAZON){
                BuffActive.add(hero, Guard.class, guardTurns()+1, true);
            }

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
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			hero.remove(Guard.class);
			hero.belongings.weap2 = null;
			//update if it have a dual wielding weapon
            hero.updateWeaponSprite();
            QuickSlot.refresh();

			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }

    @Override
    public int dr( int bonus ) {
        return 3 + tier * 3
                +(tier+1) * bonus;
    }

    @Override
    public int penaltyBase() {
        return super.penaltyBase()+ 2 + tier * 2;
    }

    @Override
    public int str( int bonus ) {
        return 6 + tier * 3 - ( bonus+1)/2;
    }


	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = currentPenalty(Dungeon.hero, strShown(isIdentified())) * 2.5f;
        //float penalty = GameMath.gate(0, currentPenalty(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;
        int armor = Math.max(0, isIdentified() ? dr() : dr(0) );

        StringBuilder info = new StringBuilder( desc() );


        info.append( p );

        if (isIdentified()) {
/*            info.append( "This _tier-" + tier + " shield_ requires _" + itemStr + " points of strength_ to use effectively and" +
                    //( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    //"will occasionally increase your _armor class by " + armor + " points_.");
                    " when used to block, will provide a _defensive power of " + armor + "_." );*/
            info.append( "这个_" + tier + "阶盾牌_需要_" + itemStr + "点力量_才能正常使用" +
                    //( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    //"will occasionally increase your _armor class by " + armor + " points_.");
                    "当用来格挡时，它会提供_" + armor + "_点防御力。" );

            info.append( p );

            if (itemStr > heroStr) {
/*                info.append(
                        "Because of your inadequate strength, your stealth and dexterity with this shield " +
                                "will be _decreased by " + penalty + "%_ and your movement will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else {
                info.append(
                        "While you are using this shield, your stealth and dexterity will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_": "_not be decreased_" ) + "." );
            }
        } else {
            info.append(  "Usually _tier-" + tier + " shields_ require _" + itemStr + " points of strength_ to be used effectively and" +
                    //( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    //"will occasionally increase your _armor class by " + armor + " points_." );
                    " when used to block, will provide a _defensive power of " + armor + "_." );*/
            info.append(
                    "由于你的力量不足，装备该盾牌时会使你的 " +
                            "潜行和灵巧_降低" + penalty + "%_ ，同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
        } else {
            info.append(
                    "当你装备该盾牌时，你的潜行和灵巧" + (  penalty > 0 ? "会_降低" + penalty + "%_" : "_不会降低_" ) + "。");
        }
    } else {
        info.append(  "通常这个_" + tier + "阶盾牌_需要_" + itemStr + " 点力量_ 才能正常使用" +
                //( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                //"will occasionally increase your _armor class by " + armor + " points_." );
                    "当用来格挡时，它会提供_" + armor + "_点防御力。" );

            info.append( p );

/*            if (itemStr > heroStr) {
                info.append(
                        "Because of your inadequate strength, your stealth and dexterity with this shield " +
                                "probably will be _decreased by " + penalty + "%_ and your movement will be _" + (int)(100 - 10000 / (100 + penalty)) + "% slower_." );
            } else {
                info.append(
                        "While you are using this shield, your stealth and dexterity will " + ( penalty > 0 ? "be _decreased by " + penalty + "%_": "_not be decreased_" ) + "." );
            }
        }*/
            if (itemStr > heroStr) {
                info.append(
                        "由于你的力量不足，装备该盾牌时可能会使你的潜行和灵巧_降低" + penalty + "%_，同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
            } else {
                info.append(
                        "当你装备该盾牌时，你的潜行和灵巧可能" +
                                ( penalty > 0 ? "会_降低" + penalty + "%_" : "_不会降低_" )  + "。" );
            }
        }

        info.append( p );

/*        if (isEquipped( Dungeon.hero )) {

            info.append( "You hold the " + name + " at the ready." );

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "The " + name + " is in your backpack." );

        } else {

            info.append( "The " + name + " lies on the dungeon's floor." );

        }*/
        if (isEquipped( Dungeon.hero )) {

            info.append( "你正装备着" + name + "。" );

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "这件" + name + "正在你的背包里。" );

        } else {

            info.append( "这件" + name + "正在地面上。" );

        }

        info.append( s );

/*        if( isIdentified() && bonus > 0 ) {
            info.append( "It appears to be _upgraded_." );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "It appears to be _non-cursed_." :
                    "A malevolent _curse_ seems to be lurking within this " + name +"." );
        } else {
            info.append( " This " + name + " is _unidentified_." );
        }

        info.append( s );

        if( isEnchantKnown() && glyph != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "Also" : "However" ) + ", it seems to be _enchanted to " + glyph.desc(this) + "_." );
        }

        info.append( " This is a _" + lootChapterAsString() +"_ shield." );*/
        if( isIdentified() && bonus > 0 ) {
            info.append( "它似乎已被_升级_。" );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "它看起来并_没有被诅咒_。" :
                    "你能感受到它似乎充满了_恶意_的魔力"/* + name */ + "。" );
        } else {
            info.append( "这件" + name + "尚_未被鉴定_。" );
        }

        info.append( s );

        if( isEnchantKnown() && glyph != null ) {
            info.append( "" + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) + "，它携带着_" + glyph.desc(this) + "_的附魔。" );
        }

        info.append( "这是一件_" + lootChapterAsString() +"_盾牌。" );

        return info.toString();

	}

    @Override
    public int lootChapter() {
        return super.lootChapter() ;
    }

    protected static CellSelector.Listener slamer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                Ballistica.cast(curUser.pos, target, false, true);

                int cell = Ballistica.trace[0];

                if (Ballistica.distance >= 1) {
                    cell = Ballistica.trace[1];
                }

                final Char enemy = Actor.findChar(cell);

                if( !(enemy instanceof Mob)) {
                    GLog.i(TXT_SLAM_FAIL);
                    return;
                }


                ((HeroSprite)curUser.sprite).shieldSlam(cell, new Callback() {
                    @Override
                    public void call() {
                        curUser.onSlamComplete(enemy,curItem);

                    }
                });
                curUser.busy();
                curUser.spend(Actor.TICK);
            }
        }

        @Override
        public String prompt() {
//            return "Select a nearby enemy to slam";
            return "选择一个附近的敌人进行猛击";
        }
    };

    public static class ShieldSlam extends MeleeWeapon {
        {
            name = "盾击";
            critical=new BluntCritical(this, false, 2f);
        }
        Shield shield;

        public ShieldSlam(Shield shield) {
            super( shield.tier );
            this.shield=shield;
        }

        @Override
        public boolean isIdentified() {	return true; }

        @Override
        public int str(int bonus) {
            return shield.str(bonus);
        }

        @Override
        public int penaltyBase() {
            return shield.penaltyBase();
        }

        @Override
        public int min( int bonus ) {
            return 1+shield.tier + shield.bonus;
        }

        @Override
        public int max( int bonus ) {
            return 5 * shield.tier + (shield.bonus * shield.tier);
        }


        @Override
        public int proc( Char attacker, Char defender, int damage ) {
            damage=super.proc(attacker, defender, damage);

            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            Camera.main.shake(2, 0.1f);

            return damage;
        }

    }

}
