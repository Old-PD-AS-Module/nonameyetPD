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
package com.ravenwolf.nnypd.actors.hero;

import android.text.TextUtils;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Bones;
import com.ravenwolf.nnypd.Difficulties;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.GamesInProgress;
import com.ravenwolf.nnypd.ResultDescriptions;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.AltarPower;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Frenzy;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Overload;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Body_AcidResistance;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Electric_EnergyResistance;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Toughness;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Fire_ColdResistance;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Petrificated;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Revealed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Shocked;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.Combo;
import com.ravenwolf.nnypd.actors.buffs.special.Exposed;
import com.ravenwolf.nnypd.actors.buffs.special.Focused;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.actors.buffs.special.Light;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.buffs.special.skills.SummonSpiritWolfSkill;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Statue;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Heap.Type;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.armours.body.HuntressArmor;
import com.ravenwolf.nnypd.items.armours.body.MageArmor;
import com.ravenwolf.nnypd.items.armours.body.RogueArmor;
import com.ravenwolf.nnypd.items.armours.glyphs.Tenacity;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.keys.IronKey;
import com.ravenwolf.nnypd.items.keys.Key;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.items.misc.Amulet;
import com.ravenwolf.nnypd.items.misc.Ankh;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.potions.PotionOfStrength;
import com.ravenwolf.nnypd.items.potions.PotionOfWisdom;
import com.ravenwolf.nnypd.items.rings.RingOfAccuracy;
import com.ravenwolf.nnypd.items.rings.RingOfAwareness;
import com.ravenwolf.nnypd.items.rings.RingOfConcentration;
import com.ravenwolf.nnypd.items.rings.RingOfEvasion;
import com.ravenwolf.nnypd.items.rings.RingOfFuror;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.items.rings.RingOfSharpShooting;
import com.ravenwolf.nnypd.items.rings.RingOfProtection;
import com.ravenwolf.nnypd.items.rings.RingOfShadows;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.items.rings.RingOfVitality;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfClairvoyance;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfEnchantment;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfUpgrade;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.DoubleBlade;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeaponCrossbow;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeaponFlintlock;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeaponMissile;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.levels.features.AlchemyPot;
import com.ravenwolf.nnypd.levels.features.Altar;
import com.ravenwolf.nnypd.levels.features.Bookshelf;
import com.ravenwolf.nnypd.levels.features.Chasm;
import com.ravenwolf.nnypd.levels.features.Sign;
import com.ravenwolf.nnypd.levels.traps.Trap;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.scenes.InterlevelScene;
import com.ravenwolf.nnypd.scenes.SurfaceScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.CheckedCell;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.ravenwolf.nnypd.visuals.ui.TagAttack;
import com.ravenwolf.nnypd.visuals.windows.WndMessage;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.ravenwolf.nnypd.visuals.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class Hero extends Char {

/*    private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";
    private static final String TXT_LEAVE_WARRIOR = "There is no honor in running away. I must push on.";
    private static final String TXT_LEAVE_SCHOLAR = "It doesn't matter how foolish my decisions were, surrendering now would be even worse.";
    private static final String TXT_LEAVE_BRIGAND = "That is not the stairs I am looking for.\nI must go down, not up.";
    private static final String TXT_LEAVE_ACOLYTE = "I can't go back now. Everything is at stake. Retreating will only delay the inevitable!";

    private static final String TXT_EXP = "%+dEXP";
    private static final String TXT_LEVEL_UP = "level up!";
    private static final String TXT_NEW_LEVEL =
            "Welcome to level %d! You receive %s.";

    public static final String TXT_YOU_NOW_HAVE = "You pick up %s";

    private static final String TXT_SOMETHING_ELSE = "There is something else here";
    private static final String TXT_LOCKED_CHEST = "This chest is locked and you don't have matching key";
    private static final String TXT_LOCKED_DOOR = "You don't have a matching key";
    private static final String TXT_NOTICED_SMTH = "You noticed something";

    private static final String TXT_WOKEN_UP = "You were woken up by someone's presence!";

    private static final String TXT_BREAK_FREE_FAILED = "ensnared";
    private static final String TXT_BREAK_FREE_WORKED = "escaped!";

    private static final String TXT_WAIT = "...";
    private static final String TXT_SEARCH = "search";*/
    private static final String TXT_LEAVE = "没有人会轻易地离开像素地牢。";
    private static final String TXT_LEAVE_WARRIOR = "逃跑毫无荣誉可言，我必须前进。";
    private static final String TXT_LEAVE_SCHOLAR = "不论我所之前做的决定有多么愚蠢，现在这个地步放弃只会让事情变得更糟。";
    private static final String TXT_LEAVE_BRIGAND = "这不是我要去的地方。\n必须一直向下，我已无路可退！";
    private static final String TXT_LEAVE_ACOLYTE = "我不能选择离开，这世间已岌岌可危了。现在撤退也只是在拖延不可避免的宿命罢了！";

    private static final String TXT_EXP = "%+d经验";
    private static final String TXT_LEVEL_UP = "升级！";
    private static final String TXT_NEW_LEVEL =
            "你升到了%d级！你的%s。";

    public static final String TXT_YOU_NOW_HAVE = "你捡起了%s";

    private static final String TXT_SOMETHING_ELSE = "这里还有别的东西";
    private static final String TXT_LOCKED_CHEST = "箱子锁着而你没有对应的钥匙";
    private static final String TXT_LOCKED_DOOR = "你没有对应的钥匙";
    private static final String TXT_NOTICED_SMTH = "你注意到了什么";

    private static final String TXT_WOKEN_UP = "你被怪物的气息惊醒了！";

    private static final String TXT_BREAK_FREE_FAILED = "缠绕";
    private static final String TXT_BREAK_FREE_WORKED = "挣脱！";

    private static final String TXT_WAIT = "...";
    private static final String TXT_SEARCH = "探索";

    public static final int STARTING_STR = 10;

    private static final float TIME_TO_REST = 1f;
    private static final float TIME_TO_PICKUP = 1f;
    private static final float UNARMED_ATTACK_SPEED = 2.0f;
    private static final float TIME_TO_SEARCH = 4f;

    public HeroClass heroClass = HeroClass.WARRIOR;
    public HeroSubClass subClass = HeroSubClass.NONE;

    public ArrayList<HeroSkill> availableSkills;
    public HeroSkill skill1=HeroSkill.NONE;
    public HeroSkill skill2=HeroSkill.NONE;

    public int magicSkill = 10/*15*/;
    public int attackSkill = 10;
    public int defenseSkill = 10/*5*/;

    public int strBonus = 0;
    public int lvlBonus = 0;

    public boolean ready = false;

    public HeroAction curAction = null;
    public HeroAction lastAction = null;

    private Char enemy;

    public Armour.Glyph killerGlyph = null;

    private Item theKey;

    public boolean restoreHealth = false;

    public Weapon currentWeapon = null;
    public Armour currentArmour = null;
    public Weapon rangedWeapon = null;
    public Belongings belongings;

    public int STR;

    public int lvl = 1;
    public int exp = 0;

    private ArrayList<Mob> visibleEnemies;

    public Hero(){
        super();
        //name = "you";
		name = "你";

        HP = HT = 24;
        STR = STARTING_STR;

        belongings = new Belongings( this );

        visibleEnemies = new ArrayList<Mob>();
    }

    @Override
    public int STR(){
        return STR;
    }

    private static final String ATTACK = "accuracy";
    private static final String DEFENSE = "dexterity";
    private static final String MAGIC = "magicSkill";
    private static final String STRENGTH = "STR";
    private static final String STR_BONUS = "strBonus";
    private static final String LVL_BONUS = "lvlBonus";
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";
    private static final String SKILL1 = "skill1";
    private static final String SKILL2 = "skill2";
    private static final String AVAILABLE_SKILLS = "availableSkills";

    @Override
    public void storeInBundle( Bundle bundle ){
        super.storeInBundle( bundle );

        heroClass.storeInBundle( bundle );
        subClass.storeInBundle( bundle );

        bundle.put( ATTACK, attackSkill );
        bundle.put( DEFENSE, defenseSkill );
        bundle.put( MAGIC, magicSkill );

        bundle.put( STRENGTH, STR );

        bundle.put( STR_BONUS, strBonus );
        bundle.put( LVL_BONUS, lvlBonus );

        bundle.put( LEVEL, lvl );
        bundle.put( EXPERIENCE, exp );

        bundle.put( SKILL1, skill1.toString());
        bundle.put( SKILL2, skill2.toString());

        bundle.put( AVAILABLE_SKILLS, Arrays.toString(availableSkills.toArray()).replaceAll("^.|.$", "").split(", ") );

        belongings.storeInBundle( bundle );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ){
        super.restoreFromBundle( bundle );

        heroClass = HeroClass.restoreInBundle( bundle );
        subClass = HeroSubClass.restoreInBundle( bundle );

        attackSkill = bundle.getInt( ATTACK );
        defenseSkill = bundle.getInt( DEFENSE );
        magicSkill = bundle.getInt( MAGIC );

        STR = bundle.getInt( STRENGTH );
        strBonus = bundle.getInt( STR_BONUS );
        lvlBonus = bundle.getInt( LVL_BONUS );

        lvl = bundle.getInt( LEVEL );
        exp = bundle.getInt( EXPERIENCE );

        skill1=HeroSkill.valueOf(bundle.getString(SKILL1));
        skill2=HeroSkill.valueOf(bundle.getString(SKILL2));

        String[] xSkills=bundle.getStringArray(AVAILABLE_SKILLS);

        availableSkills=new ArrayList();
        for(String skill:xSkills) {
            availableSkills.add(HeroSkill.valueOf(skill));
        }
//        updateAwareness();

//		belongings.restoreFromBundle(bundle);
    }


    private final HashSet<String> perks=new HashSet();
    public boolean hasPerk(String perk){
        return perks.contains(perk);
    }

    public void removePerk(String perk){
        perks.remove(perk);
    }

    public void addPerk(String perk){
        perks.add(perk);
    }

    public HashSet<String>  perks(){
        return perks;
    }

    @Override
    public int actingPriority(){
        return 4;
    }
    public static void preview( GamesInProgress.Info info, Bundle bundle ){
        info.level = bundle.getInt( LEVEL );
    }

    public String className(){
        return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
    }

    public boolean isFriendly(){
        return true;
    }

    public int viewDistance(){

        if( restoreHealth )
            return 0;
        //int modifier=(awareness()>1?(int)((awareness()-1)/0.15f):0)-Dungeon.chapter();
        //int distance = buff( Light.class ) != null ? super.viewDistance() : super.viewDistance() +modifier ;

        int chapterPenalty=(Dungeon.chapter()+1)/2;
        if (subClass==HeroSubClass.SNIPER)
            chapterPenalty--;
        if( Dungeon.difficulty > Difficulties.NORMAL )
            chapterPenalty++;

        int distance = buff( Light.class ) != null ? super.viewDistance() : super.viewDistance() - chapterPenalty ;

        return GameMath.gate( 1, distance, 8 );
    }

    public int appearance(){
        return belongings.armor == null ? 0 : belongings.armor.appearance;
    }

    public boolean shoot( Char enemy, Weapon wep ){

        rangedWeapon = wep;
        boolean result = attack( enemy );
        rangedWeapon = null;

        return result;
    }

    public Char enemy(){
        return enemy;
    }

    @Override
    public boolean isRanged(){
        return rangedWeapon != null;
    }

    @Override
    public boolean ignoresDistancePenalty(){
        return hasBuff(MindVision.class);
    }

    @Override
    public boolean  penetrateAC(Char enemy){
        return currentWeapon!=null && currentWeapon.penetrateAC();
    }

    @Override
    public boolean hasShield(){
        return belongings.weap2 instanceof Shield;
    }

    @Override
    public int accuracy(){
        return baseAcc( rangedWeapon != null ? rangedWeapon : currentWeapon, true );
    }

    public int baseAcc( Weapon wep, boolean identified ){

        float modifier = ringBuffsHalved( RingOfAccuracy.Accuracy.class );
/*
        if( buff( Enraged.class ) != null )
            modifier *= 2.0f;
*/
        if( buff( Tormented.class ) != null )
            modifier *= 0.5f;

        if( buff( Charmed.class ) != null )
            modifier *= 0.5f;

        if ( buff( Blinded.class ) != null ) {
            modifier *= 0.25f;
        }

        if( buff( Chilled.class ) != null )
            modifier *= 0.75f;

        if( wep != null ){
            modifier *= wep.penaltyFactor( this, identified || wep.isIdentified() );
        }


        return (int) ( attackSkill * modifier );
    }


    public boolean attack( Char enemy, int damageRoll ){
        //attacking when ST requirement is not reached will alert enemies
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        if (enemy.dexterity()==0 && enemy instanceof Mob  && wep!=null && wep.str()>STR())
            ((Mob)enemy).beckon(pos);

        return super.attack(enemy, damageRoll);
    }

    @Override
    public int dexterity(){
        return baseDex( true );
    }

    public int baseDex( boolean identified ){

        if( restoreHealth || stunned )
            return 0;

        float modifier = moving ? ringBuffs( RingOfEvasion.Evasion.class ) : ringBuffsHalved( RingOfEvasion.Evasion.class );

        if( belongings.armor != null ){

            /*if(belongings.armor.glyph instanceof Featherfall) {
                int dodgeBonus = belongings.armor.bonus - belongings.armor.penaltyBase() / 2;
                if (dodgeBonus > 0)
                    modifier *= (1 + 0.1f * dodgeBonus);
            }else*/
                modifier *= belongings.armor.penaltyFactor(this, identified || belongings.armor.isIdentified());
        }

        if( belongings.weap2 instanceof Shield ){
            modifier *= this.belongings.weap2.penaltyFactor( this, identified || belongings.weap2.isIdentified() );
        }

        modifier *=dextModifier();


        return (int) ( defenseSkill * modifier );
    }

    @Override
    public int magicSkill(){
        float modifier = ringBuffsHalved( RingOfSorcery.Sorcery.class );

        if( buff( Shocked.class ) != null )
            modifier *= 0.5f;

        return (int) ( magicSkill * modifier );
    }

    @Override
    public int armourAC(){

        //int dr = Random.IntRange( 0, Math.max( 0, STR() - 5 ) ) +
        int dr=  (int)(STR() * ringBuffsBaseZero( RingOfProtection.Protection.class )/2);
        //dr+=STR()/4;

        if( belongings.armor != null ){

            dr += Math.max( belongings.armor.dr(), 0 );

/*
            int exStr = STR() - belongings.armor.str();

            if( exStr > 0 ){
                dr += Random.IntRange( 0, exStr );
            }
*/
            if( belongings.armor.glyph instanceof Tenacity ){

                //int origDr=dr;
                dr += !belongings.armor.isCursed()
                    ? dr * ( HT - HP ) * ( belongings.armor.bonus + 1 ) / HT / 4
                        :- dr * ( HT - HP ) / HT / 3;
                //GLog.i("tenacity bonus %d",dr-origDr);
            }
        }

        if( buff( Shielding.class ) != null ){
            dr += HT / 5;
        }
        if( buff( Petrificated.class ) != null ){
            dr += HT / 4;
        }

        return dr;
    }

    @Override
    public int shieldAC() {
        if( belongings.weap2 instanceof Shield){
            Shield sh=(Shield)belongings.weap2;
            return Random.IntRange(sh.dr()/4,sh.dr()/2);
        }
        return 0;
    }

    public int minAC() {
        if( belongings.armor != null ){
            return super.minAC() + belongings.armor.minDr();
        }
        else return super.minAC();
    }
    public float guardPenalty(){
        return !restoreHealth && !stunned ? super.guardPenalty() : 0.0f;
    }

    @Override
    public int guardStrength(boolean ranged){

        int result = Math.max( 0, (STR() - 10) );

        if( belongings.weap2 instanceof Shield ){

            Shield shd = ( (Shield) belongings.weap2 );
            result += shd.dr();
            if (ranged) {
                result += shd.dr() / 2;
                if (shd.bonusAgainstRanged())
                    result += shd.dr();
            }

            if (shd.str() > STR())
                result-=shd.str() -STR();


//            if ( shd.glyph instanceof Tenacity ){
//                result += ( shd.isCursed() ? -result * ( HT - HP )  / ( HT * 3 ) :
//                        result * ( HT - HP ) * ( shd.bonus+ 1 ) / ( HT * 4 ) );
//            }
        } else {
            if( belongings.weap1 instanceof MeleeWeapon ){
                if (belongings.weap1.str() > STR())
                    result-=belongings.weap1.str() -STR();

                result/=2; //strength bonus is halved for melee weapons
                result += belongings.weap1.guardStrength();
                if (ranged)
                    result -= result/2;
            }
        }
        return result;
    }

    @Override
    public int damageRoll(){
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;
        int dmg;
        Combo combo = buff( Combo.class );

        if( wep != null ){
            dmg = wep.damageRoll( this );

            if (wep instanceof ThrowingWeapon)
                dmg += (int) ( wep.damageRoll( this ) * ringBuffsBaseZero( RingOfSharpShooting.SharpShooting.class )) ;
            else if (wep instanceof RangedWeapon)
                dmg += (int) ( wep.damageRoll( this ) * ringBuffsBaseZero( RingOfSharpShooting.SharpShooting.class )/2) ;

            if( buff( Enraged.class ) != null ){
                dmg += wep.damageRoll( this )/2;
            }

            Frenzy frenzy=buff( Frenzy.class );
            if( frenzy != null ){
                dmg += wep.damageRoll( this )* frenzy.getDamageBonus();
            }


        } else {

            int strMod = Math.max( 0, STR() - 5 );

            dmg = Random.IntRange( 0, strMod );

            if( buff( Enraged.class ) != null ){
                dmg += Math.max( 0, strMod );
            }

            if( combo != null ){
                dmg += (int) ( Random.IntRange( 0, strMod ) * combo.modifier() * ringBuffs( RingOfAccuracy.Accuracy.class ) );
            }

        }

        if( buff( Poisoned.class ) != null )
            dmg -= dmg/4;

        if( buff( Withered.class ) != null )
            dmg-= dmg/4;

        if( hasBuff( Charmed.class ) )
            dmg /= 2;

        Decay decay=buff(Decay.class);
        if (decay!=null ){
            dmg -= dmg *decay.getReduction();
        }

        return dmg;
    }

    @Override
    public float moveSpeed(){

        float modifier = belongings.armor != null ? belongings.armor.speedFactor( this ) : 1.0f;

        if( belongings.weap2 instanceof Shield ){
            modifier *= belongings.weap2.speedFactor( this );
        }

        return super.moveSpeed() * modifier;
    }

    @Override
    public float attackSpeed(){
        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        float value = super.attackSpeed();

        if( wep != null ){

             value *= wep.speedFactor( this ) ;

        } else if( belongings.weap1 == null && belongings.weap2 == null ){

            value *= 2.0f;

        } else if( belongings.weap1 == null || belongings.weap2 == null ) {

            value *= 1.333f;

        }

        if(wep != rangedWeapon && buff(RingOfFuror.Furor.class)!=null) {
            float bonus=(1.5f*ringBuffsBaseZero( RingOfFuror.Furor.class )*(1-(float)HP/HT));
            value += value *bonus;
        }

        return value;
    }

    @Override
    protected float healthValueModifier() {
        return 0.5f;
    }


    public void spendAndNext( float time ){
        busy();
        spend( time );
        next();
    }

    @Override
    public boolean act(){

        super.act();

        if( stunned ){

            curAction = null;

            spendAndNext( TICK );
            return false;
        }

        Dungeon.observe();
        checkVisibleMobs();
        TagAttack.updateState();

        if( curAction == null ){

            if( restoreHealth ){

                boolean wakeUp = false;

                for( Mob mob : Dungeon.level.mobs ){
                    if( mob.hostile && Level.adjacent( pos, mob.pos ) && detected( mob ) ){
                        wakeUp = true;
                        break;
                    }
                }

                if( wakeUp ){
//					restoreHealth = false;
                    interrupt( TXT_WOKEN_UP );
                } else {
                    spend( TIME_TO_REST );
                    next();
                    return false;
                }
            }

            ready();
            return false;

        } else {

            restoreHealth = false;

            ready = false;

            if( curAction instanceof HeroAction.Move ){

                return actMove( (HeroAction.Move) curAction );

            } else if( curAction instanceof HeroAction.Talk ){

                return actTalk( (HeroAction.Talk) curAction );

            } else if( curAction instanceof HeroAction.Buy ){

                return actBuy( (HeroAction.Buy) curAction );

            } else if( curAction instanceof HeroAction.PickUp ){

                return actPickUp( (HeroAction.PickUp) curAction );

            } else if( curAction instanceof HeroAction.OpenChest ){

                return actOpenChest( (HeroAction.OpenChest) curAction );

            } else if( curAction instanceof HeroAction.Unlock ){

                return actUnlock( (HeroAction.Unlock) curAction );

            } else if( curAction instanceof HeroAction.Examine ){

                return actExamine( (HeroAction.Examine) curAction );

            } else if( curAction instanceof HeroAction.Read ){

                return actRead( (HeroAction.Read) curAction );

            } else if( curAction instanceof HeroAction.Descend ){

                return actDescend( (HeroAction.Descend) curAction );

            } else if( curAction instanceof HeroAction.Ascend ){

                return actAscend( (HeroAction.Ascend) curAction );

            } else if( curAction instanceof HeroAction.Attack ){

                return actAttack( (HeroAction.Attack) curAction );

            } else if( curAction instanceof HeroAction.Cook ){

                return actCook( (HeroAction.Cook) curAction );

            }else if( curAction instanceof HeroAction.Offer ){

                return actOffer( (HeroAction.Offer) curAction );

            }

        }

        return false;
    }

    public void busy(){
        ready = false;
    }

    public void ready(){
        if( sprite != null ){
            sprite.idle();
        }

        curAction = null;
        ready = true;

        Dungeon.observe();

        GameScene.ready();
    }

    public void interrupt( String awakening ){

        interrupt( awakening, false );

    }

    public void interrupt( String awakening, boolean positive ){

        if( restoreHealth ){

            if( positive ){
                GLog.i( awakening );
            } else {
                GLog.w( awakening );
            }

        }

        interrupt();
    }

    public void interrupt(){

        restoreHealth = false;

        OilLantern lantern = belongings.getItem( OilLantern.class );

        if( isAlive() && lantern != null && lantern.isActivated() && buff( Light.class ) == null ) {
            lantern.activate( this, false );
        } else {
            Dungeon.observe();
        }

        if( isAlive() && curAction != null && !( curAction instanceof HeroAction.Attack ) && curAction.dst != pos ){
            lastAction = curAction;
        }

        curAction = null;
    }

    public void resume(){
        if( isAlive() ){
            curAction = lastAction;
            lastAction = null;
            act();
        }
    }

    private boolean actMove( HeroAction.Move action ){

        if( getCloser( action.dst ) ){

            return true;

        } else {

            if( Dungeon.level.map[ pos ] == Terrain.SIGN ){
//				Sign.read( pos );
                Sign.read();
            }

            ready();

            return false;
        }
    }

    private boolean actTalk( HeroAction.Talk action ){

        NPC npc = action.npc;

        if( Level.adjacent( pos, npc.pos ) ){

            ready();
            sprite.turnTo( pos, npc.pos );
            npc.interact();
            return false;

        } else {

            if( Level.fieldOfView[ npc.pos ] && getCloser( npc.pos ) ){

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    private boolean actBuy( HeroAction.Buy action ){
        int dst = action.dst;
        if( pos == dst || Level.adjacent( pos, dst ) ){

            ready();

            Heap heap = Dungeon.level.heaps.get( dst );
            if( heap != null && heap.type == Type.FOR_SALE && heap.size() == 1 ){
                GameScene.show( new WndTradeItem( heap, true ) );

            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actCook( HeroAction.Cook action ){
        int dst = action.dst;
        if( Dungeon.visible[ dst ] && Level.adjacent( pos, dst ) ){

            ready();
            AlchemyPot.operate( this, dst );
          //  GameScene.show(new WndAlchemy());
            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actOffer( HeroAction.Offer action ){
        int dst = action.dst;
        if( Dungeon.visible[ dst ] && Level.adjacent( pos, dst ) ){

            ready();
            Altar.operate( this, dst );
            return false;

        } else if( getCloser( dst ) ){
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actPickUp( HeroAction.PickUp action ){
        int dst = action.dst;
        if( pos == dst  || ( ( Dungeon.level.map[ dst ] == Terrain.PEDESTAL
            || Level.solid[ dst ] ) && Level.adjacent( pos, dst ) ) ){

            Heap heap = Dungeon.level.heaps.get( dst );

            if( heap != null ){

                Item item = heap.pickUp();
                if( item.doPickUp( this ) ){

//                    if( item instanceof Waterskin ){
                        // Do nothing
//                    } else {
                        boolean important =
                                ( ( item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment ) && ( item ).isTypeKnown() ) ||
                                        ( ( item instanceof PotionOfStrength || item instanceof PotionOfWisdom ) && ( item ).isTypeKnown() );
                        if( important ){
                            GLog.p( TXT_YOU_NOW_HAVE, item.toString() );
                        } else {
                            GLog.i( TXT_YOU_NOW_HAVE, item.toString() );
                        }
//                    }

//					if (!heap.isEmpty()) {
//						GLog.i( TXT_SOMETHING_ELSE );
//					}

                    spend( TIME_TO_PICKUP );
                    Sample.INSTANCE.play( Assets.SND_ITEM );
                    sprite.pickup( dst );
//					curAction = null;

                    if( invisible <= 0 ){
                        for( int n : Level.NEIGHBOURS8 ){

                            Char ch = Actor.findChar( dst + n );

                            if( ch instanceof Statue ){
                                ( (Statue) ch ).activate();
                            }
                        }
                    }

                } else {
                    Dungeon.level.drop( item, dst ).sprite.drop();
                    ready();
                }
            } else {
                ready();
            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actOpenChest( HeroAction.OpenChest action ){
        int dst = action.dst;
        if( Level.adjacent( pos, dst ) || pos == dst ){

            Heap heap = Dungeon.level.heaps.get( dst );
            if( heap != null && ( heap.type != Type.HEAP && heap.type != Type.FOR_SALE ) ){

                theKey = null;

                if( heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST ){

                    theKey = belongings.getKey( GoldenKey.class, Dungeon.depth );

                    if( theKey == null ){
                        GLog.w( TXT_LOCKED_CHEST );
                        ready();
                        return false;
                    }
                }

                switch( heap.type ){
                    case TOMB:
                        Sample.INSTANCE.play( Assets.SND_TOMB );
                        Camera.main.shake( 1, 0.5f );
                        break;
                    case BONES:
                    case BONES_CURSED:
                        break;
                    default:
                        Sample.INSTANCE.play( Assets.SND_UNLOCK );
                }

                spend( Key.TIME_TO_UNLOCK );
                sprite.operate( dst );

            } else {
                ready();
            }

            return false;

        } else if( getCloser( dst ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actExamine( HeroAction.Examine action ){

        int dest = action.dst;
        if( Level.adjacent( pos, dest ) ){

            spend( Hero.TIME_TO_PICKUP );
            sprite.operate( dest );

            return false;

        } else if( getCloser( dest ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actRead( HeroAction.Read action ){

        int dest = action.dst;
        if( pos == dest + Level.WIDTH ){

            Sign.read();
            ready();
            return false;

        } else if( getCloser( dest + Level.WIDTH ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock( HeroAction.Unlock action ){
        int doorCell = action.dst;
        if( Level.adjacent( pos, doorCell ) ){

            theKey = null;
            int door = Dungeon.level.map[ doorCell ];

            if( door == Terrain.LOCKED_DOOR ){

                theKey = belongings.getKey( IronKey.class, Dungeon.depth );

            } else if( door == Terrain.LOCKED_EXIT ){

                theKey = belongings.getKey( SkeletonKey.class, Dungeon.depth );

            }

            if( theKey != null ){

                final boolean isAlternative;
                if (door == Terrain.LOCKED_EXIT  && (Dungeon.depth==Dungeon.CAVES_PATHWAY || Dungeon.depth==Dungeon.PRISON_PATHWAY)){
                    String msj="";
                    /*if (doorCell ==Dungeon.level.exitAlternative) {
                        if (Dungeon.depth==Dungeon.PRISON_PATHWAY)
                            msj="This path leads to the prison graveyard.";
                        else
                            msj = "This path seems to lead to some kind of ancient ruins.";
                        isAlternative=true;
                    }
                    else {
                        if (Dungeon.depth==Dungeon.PRISON_PATHWAY)
                            msj="This path leads to the high-security cell block.";
                        else
                            msj = "This path seems to lead to an abandoned mine.";
                        isAlternative=false;
                    }*/
                    if (doorCell ==Dungeon.level.exitAlternative) {
                        if (Dungeon.depth==Dungeon.PRISON_PATHWAY)
                            msj="这条路似乎通往监狱的墓地。";
                        else
                            msj = "这条路似乎通往某个遗迹。";
                        isAlternative=true;
                    }
                    else {
                        if (Dungeon.depth==Dungeon.PRISON_PATHWAY)
                            msj="这条路似乎通往高度戒备的监狱。";
                        else
                            msj = "这条路似乎通往一个废弃的矿洞。";
                        isAlternative=false;
                    }

                    ready();
                    final HeroAction.Unlock act =new  HeroAction.Unlock(doorCell);

                    NoNameYetPixelDungeon.scene().add(
/*                            new WndOptions("Do you want to unlock this door?", msj,
                                    Utils.capitalize( "unlock door" ),
                                    Utils.capitalize( "keep the key" ) ) {*/
                            new WndOptions("你想打开这扇门吗？", msj,
                                    Utils.capitalize( "开启" ),
                                    Utils.capitalize( "算了" ) ) {

                                @Override
                                protected void onSelect( int index ) {

                                    if (index == 0) {
                                        spend(Key.TIME_TO_UNLOCK);
                                        sprite.operate(act.dst);

                                        if (Dungeon.depth==Dungeon.PRISON_PATHWAY)
                                            Dungeon.prisonOption =isAlternative? Dungeon.GRAVEYARD_OPTION:Dungeon.PRISION_OPTION;
                                        else if (Dungeon.depth==Dungeon.CAVES_PATHWAY )
                                            Dungeon.cavesOption=isAlternative?Dungeon.RUINS_OPTION:Dungeon.MINES_OPTION;

                                        Sample.INSTANCE.play(Assets.SND_UNLOCK);
                                        curAction=act;
                                    }
                                }
                            } );
                }else {
                    spend(Key.TIME_TO_UNLOCK);
                    sprite.operate(doorCell);

                    Sample.INSTANCE.play(Assets.SND_UNLOCK);
                }

            } else {
                GLog.w( TXT_LOCKED_DOOR );
                ready();
            }

            return false;

        } else if( getCloser( doorCell ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actDescend( HeroAction.Descend action ){
        int stairs = action.dst;
        //if( pos == stairs && pos == Dungeon.level.exit ){
        if( pos == stairs && (pos == Dungeon.level.exit || pos == Dungeon.level.exitAlternative)){

            curAction = null;

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class );

            return false;

        } else if( getCloser( stairs ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAscend( HeroAction.Ascend action ){
        int stairs = action.dst;
        if( pos == stairs && pos == Dungeon.level.entrance ){

            if( Dungeon.depth == 1 ){

                if( belongings.getItem( Amulet.class ) == null ){

                    switch( Dungeon.hero.heroClass ){
                        case WARRIOR:
                            GameScene.show( new WndMessage( TXT_LEAVE_WARRIOR ) );
                            break;
                        case SCHOLAR:
                            GameScene.show( new WndMessage( TXT_LEAVE_SCHOLAR ) );
                            break;
                        case BRIGAND:
                            GameScene.show( new WndMessage( TXT_LEAVE_BRIGAND ) );
                            break;
                        case ACOLYTE:
                            GameScene.show( new WndMessage( TXT_LEAVE_ACOLYTE ) );
                            break;
                        default:
                            GameScene.show( new WndMessage( TXT_LEAVE ) );
                            break;
                    }

                    ready();
                } else {
                    Dungeon.win( ResultDescriptions.WIN );
                    Dungeon.deleteGame( Dungeon.hero.heroClass, true );
                    Game.switchScene( SurfaceScene.class );
                }

            } else {

                curAction = null;

                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene( InterlevelScene.class );
            }

            return false;

        } else if( getCloser( stairs ) ){

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actMeleeAttack(){
        if( !isScared() ){

            currentWeapon =
                    belongings.weap1 instanceof MeleeWeapon ? belongings.weap1 :
                            belongings.weap2 instanceof MeleeWeapon ? (Weapon) belongings.weap2 :
                                    null;

            //if dual wielding choose best weapon for backstab or counter attack
            if (currentWeapon!=null && currentWeapon!=belongings.weap2) {
                int dist=Level.distance(pos, enemy.pos);
                if (isDualWielding() && enemy instanceof Mob && dist<=((MeleeWeapon)belongings.weap2).reach()) {
                    if (enemy.dexterity() == 0) {

                        if (belongings.weap1.getBackstabMod() * belongings.weap1.max() < ((Weapon) belongings.weap2).getBackstabMod() * ((Weapon) belongings.weap2).max())
                            currentWeapon = (Weapon) belongings.weap2;
                    }
                    if (enemy.hasBuff(Exposed.class)) {
                        if (belongings.weap1.counterBonusDmg() * belongings.weap1.max() < ((Weapon) belongings.weap2).counterBonusDmg() * ((Weapon) belongings.weap2).max())
                            currentWeapon = (Weapon) belongings.weap2;
                    }
                }
            }

            buff( Satiety.class ).decrease(
                    ( currentWeapon != null ?
                            (float)currentWeapon.str()
                            : 5.0f ) / STR() / attackSpeed()
            );

            ((HeroSprite) sprite).attack(currentWeapon, enemy.pos, null);
            spend(attackDelay());

        } else {
            GLog.n( Tormented.TXT_CANNOT_ATTACK );
            ready();
        }

        return false;

    }

    private boolean actAttack( HeroAction.Attack action ){

        enemy = action.target;

        if( enemy.isAlive() ){

            if( canAttackWithMelee()) {

                return actMeleeAttack();

            }else {
                int cell = Ballistica.cast(pos, enemy.pos, false, true);
                boolean reachable=cell==enemy.pos;

                if (reachable && belongings.weap2 instanceof RangedWeaponMissile && ((RangedWeaponMissile) belongings.weap2).checkAmmo(this, true)) {

                    RangedWeaponMissile weap = (RangedWeaponMissile) belongings.weap2;

                    Item.curUser = this;
                    Item.curItem = weap;

                    RangedWeaponMissile.shooter.onSelect(enemy.pos);
                    curAction = null;
                    busy();

                    return false;

                } /*else if (belongings.weap1 instanceof RangedWeaponFlintlock && ((RangedWeaponFlintlock) belongings.weap1).ammunition().isInstance(belongings.ranged)) {

                    RangedWeaponFlintlock weap = (RangedWeaponFlintlock) belongings.weap1;

                    Item.curUser = this;
                    Item.curItem = weap;

                    if (!weap.loaded) {

                        weap.execute(this, RangedWeaponFlintlock.AC_RELOAD);

                    } else if (weap.checkAmmo(this, false)) {

                        busy();

                        RangedWeaponFlintlock.shooter.onSelect(enemy.pos);

                    } else {

                        ready();

                    }

                    curAction = null;

                    return false;

                }*/
                //Crossbow
                else if (reachable && belongings.weap2 instanceof RangedWeaponCrossbow && ((RangedWeaponCrossbow) belongings.weap2).checkAmmo(this, true)) {

                    RangedWeaponCrossbow weap = (RangedWeaponCrossbow) belongings.weap2;

                    Item.curUser = this;
                    Item.curItem = weap;

                    if (!weap.loaded) {

                        weap.execute(this, RangedWeaponCrossbow.AC_RELOAD);

                    } else {

                        busy();

                        RangedWeaponCrossbow.shooter.onSelect(enemy.pos);

                    }

                    curAction = null;

                    return false;

                }

                else if (Level.adjacent(pos, enemy.pos)) { //attack unarmed
                    return actMeleeAttack();
                }

                else if (reachable && NoNameYetPixelDungeon.autoThrow() && belongings.ranged instanceof ThrowingWeapon && !(belongings.ranged instanceof ThrowingWeaponAmmo)) {

                    ThrowingWeapon weap = (ThrowingWeapon) belongings.ranged;

                    Item.curUser = this;
                    Item.curItem = weap;

                    busy();

                    ThrowingWeapon.shooter.onSelect(enemy.pos);
                    curAction = null;

                    return false;

                } else {

                    if (Level.fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

                        return true;

                    } else {

                        ready();
                        return false;

                    }
                }
            }

        } else {

            if( Level.fieldOfView[ enemy.pos ] && getCloser( enemy.pos ) ){

                return true;

            } else {
                ready();
                return false;
            }
        }
    }

    private boolean canAttackWithMelee(){
        int dist=Level.distance(pos, enemy.pos);
        if( (belongings.weap1 instanceof MeleeWeapon && dist<= ((MeleeWeapon)belongings.weap1).reach() )||
                ( belongings.weap2 instanceof MeleeWeapon && dist<= ((MeleeWeapon)belongings.weap2).reach()) ) {
            int cell = Ballistica.cast(pos, enemy.pos, false, true);

            return Actor.findChar(cell) == enemy;

        }
        return false;
    }
    public void rest( boolean sleep ){

        spendAndNext( TIME_TO_REST );

        if( !sleep ){

            sprite.showStatus( CharSprite.DEFAULT, TXT_WAIT );
            Buff.affect( this, Focused.class ).reset( 1 );
            search( false );

        } else {

            Buff.detach( this, Guard.class );
            if( !isStarving() ){
                Buff.detach( this, Light.class );
                restoreHealth = true;
            } else {
                //GLog.n( "You are too hungry too sleep right now." );
				GLog.n( "你现在太饿了，还不能睡觉。" );
            }
        }

    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ){
        //attacking invulnerable enemies does not trigger combo, crits or enchantments
        if (enemy.invulnerable())
            return damage;

        Weapon wep = rangedWeapon != null ? rangedWeapon : currentWeapon;

        if( wep != null ){

            if( wep instanceof MeleeWeapon ){
                wep.use( 1 );
                if ( subClass == HeroSubClass.AMAZON && enemy.hasBuff(Revealed.class))
                    damage += GameMath.gate(1, damage / 3 ,wep.min());

                if (!blocked && subClass == HeroSubClass.BATTLEMAGE && hasBuff(Overload.class)){
                    int dmg= GameMath.gate(1, damage / 3 ,wep.min());
                    enemy.damage(dmg, this, Element.ENERGY);
                    CellEmitter.center( enemy.pos ).burst( PurpleParticle.BURST, 4 );
                }
            }

            if (!blocked && wep.increaseCombo())
                Buff.affect( this, Combo.class ).hit();

            if (!blocked)
                damage=wep.proc( this, enemy, damage );

        }

        return damage;
    }

    @Override
    public float counterChance() {
        if( belongings.weap2 instanceof Shield )
            return awareness() * ((Shield) belongings.weap2).counterMod();
                   //* (((Shield)belongings.weap2).glyph instanceof Tenacity? (1+( HT - HP ) * ( belongings.weap2.bonus + 1 ) / HT / 4) : 1);
        else
            return super.counterChance();
    }

    @Override
    public float counterBonusDmg() {
        Weapon weapon = rangedWeapon != null ? rangedWeapon : currentWeapon;
        if (weapon==null)//is attacking unarmed
            return super.counterBonusDmg() + ringBuffsBaseZero( RingOfAwareness.Awareness.class )/2 ;
        else
            return weapon.counterBonusDmg() + ringBuffsBaseZero( RingOfAwareness.Awareness.class )/2 ;
    }

    @Override
    public int defenseProc( Char enemy, int damage, boolean blocked ){

        if( blocked ){
            if( belongings.weap2 instanceof Shield ){
                currentArmour = (Shield) belongings.weap2;
                currentArmour.use( 2 );
            } else {
                currentArmour = null;

                if( currentWeapon instanceof MeleeWeapon ){
                    currentWeapon.use( 2 );
                }
            }
        } else {
            currentArmour = belongings.armor;
            //if is in guard stance identify both the shield and the armor
            if (hasBuff(Guard.class) && belongings.weap2 instanceof Shield){
                belongings.weap2.use(1);
                if( currentArmour != null )
                    currentArmour.use( 1 );

            }else if( currentArmour != null ) {
                currentArmour.use( 2 );
            }
        }

        return super.defenseProc( enemy, currentArmour != null ? currentArmour.proc( enemy, this, damage ) : damage, blocked );
    }

    @Override
    public void heal( int value ) {

        if( restoreHealth && HP < HT && ( HP + value >= HT ) ){
            interrupt();
        }

        super.heal( value );

    }

    @Override
    public void damage( int dmg, Object src, Element type ){
        interrupt();
        super.damage( dmg, src, type );

    }

    private void checkVisibleMobs(){
        ArrayList<Mob> visible = new ArrayList<>();

        boolean newMob = false;

        for( Mob m : Dungeon.level.mobs ){
            if( Level.fieldOfView[ m.pos ] && m.hostile ){
                visible.add( m );
                if( !visibleEnemies.contains( m ) ){
                    newMob = true;
                }
            }
        }

        Collections.sort( visible, new Comparator<Mob>() {
            @Override
            public int compare( Mob mob1, Mob mob2 ){

                int d1 = Level.distance( Dungeon.hero.pos, mob1.pos );
                int d2 = Level.distance( Dungeon.hero.pos, mob2.pos );

                return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
            }
        } );

        if( newMob && !restoreHealth ){
            interrupt();
        }

        visibleEnemies = visible;
    }

    public int visibleEnemies(){
        return visibleEnemies.size();
    }

    public Mob visibleEnemy( int index ){
        return visibleEnemies.size() > 0 ? visibleEnemies.get( index % visibleEnemies.size() ) : null;
    }

    private boolean getCloser( final int target ){

        if( rooted ){

            if( Random.Float() > 0.01f * STR() ){

                this.sprite.showStatus( CharSprite.WARNING, TXT_BREAK_FREE_FAILED );

                Camera.main.shake( 1, 1f );

                sprite.move( pos, pos );

                spendAndNext( TICK );

                return false;

            } else {

                this.sprite.showStatus( CharSprite.POSITIVE, TXT_BREAK_FREE_WORKED );

                Buff.detach( this, Ensnared.class );

            }
        }

        if( Level.adjacent( pos, target ) ){

            if( Actor.findChar( target ) == null ){

                if( Level.chasm[ target ] && !flying && !Chasm.jumpConfirmed ){

                    Chasm.heroJump( this );
                    interrupt();
                    return false;

                }

                if (Level.adjacent(this.pos, target)) {
                    if (Actor.findChar(target) == null) {
                        if (Level.chasm[target] && !this.flying && !Chasm.jumpConfirmed) {
                            Chasm.heroJump(this);
                            interrupt();
                            return false;
                        } else if (Level.passable[target] || (Level.avoid[target] && !Level.illusory[target])) {
                            if (Dungeon.level.map[target] != 13 || buff(Dazed.class) != null || this.flying || Trap.stepConfirmed) {
                                return makeStep(target);
                            }
                            Trap.askForConfirmation(this);
                            interrupt();
                        }
                    }
                    return false;
                }
            }

        } else {

            int len = Level.LENGTH;
            boolean[] p = Level.passable;
            boolean[] v = Dungeon.level.visited;
            boolean[] m = Dungeon.level.mapped;
            boolean[] w = Level.illusory;
            boolean[] passable = new boolean[ len ];
            for( int i = 0 ; i < len ; i++ ){
                passable[ i ] = p[ i ] && ( v[ i ] || m[ i ] ) && !w[ i ];
            }

            return makeStep( Dungeon.findPath( this, pos, target, passable, Level.fieldOfView ) );
        }

        return false;
    }

    private boolean makeStep( int step ) {

        if( step < 0 ) return false;

        int oldPos = pos;
        move( step );
        sprite.move( oldPos, pos );
        Satiety satiety=buff( Satiety.class );
        if (satiety!=null)
            satiety.decrease(
            ( belongings.armor != null ?
            (float)belongings.armor.str()
            : 5.0f ) / STR()
        );

        if( belongings.weap2 instanceof RangedWeaponCrossbow && ((RangedWeaponCrossbow) belongings.weap2).checkAmmo(this, false)) {

            RangedWeaponCrossbow weap = (RangedWeaponCrossbow)belongings.weap2;

            if( !weap.loaded ) {
                weap.reload( this );
            }

        }

        if( belongings.weap2 instanceof RangedWeaponFlintlock && ((RangedWeaponFlintlock) belongings.weap2).checkAmmo(this, false) ) {

            RangedWeaponFlintlock weap = (RangedWeaponFlintlock)belongings.weap2;

            if( !weap.loaded ) {
                weap.reload( this );
            }

        }
        if(belongings.lastEquipedAmmo !=null ) {

            Heap heap = Dungeon.level.heaps.get( step );

            if(
                heap != null && heap.type == Type.HEAP && heap.peek() != null
                && heap.peek().getClass().equals( belongings.lastEquipedAmmo/*ranged.getClass()*/ )
            ){
                Item item = heap.pickUp();
                item.doPickUp( this );
                if(belongings.ranged==null) {
                    item.detachAll(belongings.backpack);
                    belongings.ranged=(EquipableItem) item;
                }
                Sample.INSTANCE.play( Assets.SND_ITEM );
                GLog.i( TXT_YOU_NOW_HAVE, item.toString() );
            }

        }

        spend( 1 / moveSpeed() );

        return true;
    }

    public boolean handle( int cell ){

        if( cell == -1 ){
            return false;
        }

        Char ch;
        Heap heap;

        if( Level.fieldOfView[ cell ] && ( ch = Actor.findChar( cell ) ) instanceof Mob ){

            if( ch instanceof NPC ){
                curAction = new HeroAction.Talk( (NPC) ch );
            } else {
                curAction = new HeroAction.Attack( ch );
            }

        } else if( Dungeon.level.map[ cell ] == Terrain.ALCHEMY && cell != pos ){

            curAction = new HeroAction.Cook( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.PEDESTAL && cell != pos && AltarPower.isMagicAltar(cell)){

            curAction = new HeroAction.Offer( cell );

        } else if( Level.fieldOfView[ cell ] && ( heap = Dungeon.level.heaps.get( cell ) ) != null &&
            (visibleEnemies.size() == 0 || cell == pos || (heap.type != Type.HEAP && heap.type != Type.FOR_SALE ) )
        ) {

            switch( heap.type ){
                case HEAP:
                    curAction = new HeroAction.PickUp( cell );
                    break;
                case FOR_SALE:
                    curAction = heap.size() == 1 && heap.peek().price() > 0 ?
                            new HeroAction.Buy( cell ) :
                            new HeroAction.PickUp( cell );
                    break;
                default:
                    curAction = new HeroAction.OpenChest( cell );
            }

        } else if( Dungeon.level.map[ cell ] == Terrain.LOCKED_DOOR || Dungeon.level.map[ cell ] == Terrain.LOCKED_EXIT ){

            curAction = new HeroAction.Unlock( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.BOOKSHELF ){

            curAction = new HeroAction.Examine( cell );

        } else if( Dungeon.level.map[ cell ] == Terrain.WALL_SIGN ){

            curAction = new HeroAction.Read( cell );

        } else if( cell == Dungeon.level.exit ){

            curAction = new HeroAction.Descend( cell );

        }else if (cell ==Dungeon.level.exitAlternative){
            curAction = new HeroAction.Descend( cell );
        }

        else if( cell == Dungeon.level.entrance ){

            curAction = new HeroAction.Ascend( cell );

        } else {

            curAction = new HeroAction.Move( cell );
            lastAction = null;

        }

        return act();
    }

    public void earnExp( int exp ){

        this.exp += exp;

        if( sprite != null )
            sprite.showStatus( CharSprite.POSITIVE, TXT_EXP, exp );

        while( this.exp >= maxExp() ){
            this.exp -= maxExp();

            lvl++;

            int hpBonus = 1;
            int attBonus = 0;
            int defBonus = 0;
            int magBonus = 0;
            int strBonus = 0;
            int stlBonus = 0;
            int detBonus = 0;
            int wilBonus = 0;

            if( heroClass != HeroClass.ACOLYTE || lvl % 6 != 1 )
                hpBonus++;

            if( heroClass != HeroClass.SCHOLAR || lvl % 3 != 1 )
                attBonus++;

            if( heroClass != HeroClass.WARRIOR || lvl % 3 != 1 )
                defBonus++;

            if( heroClass != HeroClass.BRIGAND || lvl % 3 != 1 )
                magBonus++;


            if( heroClass == HeroClass.WARRIOR && lvl % 6 == 1 )
                hpBonus++;

            if( heroClass == HeroClass.ACOLYTE && lvl % 3 == 1 )
                attBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 3 == 1 )
                defBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 3 == 1 )
                magBonus++;


            if( heroClass == HeroClass.ACOLYTE && lvl % 2 == 1 )
                detBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 4 == 1 )
                detBonus++;

            if(( heroClass == HeroClass.BRIGAND || heroClass == HeroClass.SCHOLAR) && lvl % 3 == 1 )
                detBonus++;


            if( heroClass == HeroClass.BRIGAND && lvl % 2 == 1 )
                stlBonus++;

            if( heroClass == HeroClass.WARRIOR && lvl % 4 == 1 )
                stlBonus++;

            if( (heroClass == HeroClass.ACOLYTE || heroClass == HeroClass.SCHOLAR) && lvl % 3 == 1 )
                stlBonus++;

            if( heroClass == HeroClass.SCHOLAR && lvl % 2 == 1 )
                wilBonus++;

            if( heroClass == HeroClass.BRIGAND && lvl % 4 == 1 )
                wilBonus++;

            if( (heroClass == HeroClass.ACOLYTE || heroClass == HeroClass.WARRIOR) && lvl % 3 == 1 )
                wilBonus++;


            STR += strBonus;

            HT += hpBonus;
            HP += hpBonus;

            attackSkill += attBonus;
            defenseSkill += defBonus;
            magicSkill += magBonus;

            ArrayList bonusList = new ArrayList();

/*            if( hpBonus > 0 )
                bonusList.add( Utils.format( "+%d hp", hpBonus ) );
            if( attBonus > 0 )
                bonusList.add( Utils.format( "+%d acc", attBonus ) );
            if( defBonus > 0 )
                bonusList.add( Utils.format( "+%d dex", defBonus ) );
            if( magBonus > 0 )
                bonusList.add( Utils.format( "+%d mag", magBonus ) );

            if( strBonus > 0 )
                bonusList.add( Utils.format( "+%d strength", strBonus ) );
            if( detBonus > 0 )
                bonusList.add( Utils.format( "+%d%% perception", detBonus ) );
            if( stlBonus > 0 )
                bonusList.add( Utils.format( "+%d%% stealth", stlBonus ) );
            if( wilBonus > 0 )
                bonusList.add( Utils.format( "+%d%% willpower", wilBonus ) );*/
            if( hpBonus > 0 )
                bonusList.add( Utils.format( "生命上限+%d", hpBonus ) );
            if( attBonus > 0 )
                bonusList.add( Utils.format( "精准+%d", attBonus ) );
            if( defBonus > 0 )
                bonusList.add( Utils.format( "灵巧+%d", defBonus ) );
            if( magBonus > 0 )
                bonusList.add( Utils.format( "魔能+%d", magBonus ) );

            if( strBonus > 0 )
                bonusList.add( Utils.format( "力量+%d", strBonus ) );
            if( detBonus > 0 )
                bonusList.add( Utils.format( "感知+%d%%", detBonus ) );
            if( stlBonus > 0 )
                bonusList.add( Utils.format( "潜行+%d%%", stlBonus ) );
            if( wilBonus > 0 )
                bonusList.add( Utils.format( "意志+%d%%", wilBonus ) );

            if( sprite != null ){
                GLog.p( TXT_NEW_LEVEL, lvl, TextUtils.join( ", ", bonusList ) );
                sprite.showStatus( CharSprite.POSITIVE, TXT_LEVEL_UP );

                sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );

                Sample.INSTANCE.play( Assets.SND_LEVELUP );

                QuickSlot.refresh();
            }

            Badges.validateLevelReached();
        }
    }

    public int maxExp(){
        return ( lvl + 4 ) * ( lvl + 3 ) / 2;
    }


    public boolean isStarving(){
        return ( buff( Satiety.class ) ).isStarving();
    }

    public boolean isDualWielding(){
        return belongings.weap1 instanceof MeleeWeapon && belongings.weap2 instanceof MeleeWeapon/*||
                belongings.weap1 instanceof Knuckles && belongings.weap2 == null ||
                belongings.weap2 instanceof Knuckles && belongings.weap1 == null*/;
    }

//    public boolean isOneHandEmpty(){
//        return belongings.weap1 instanceof MeleeWeapon && belongings.weap2 == null ||
//                belongings.weap2 instanceof MeleeWeapon && belongings.weap1 == null;
//    }

    @Override
    public boolean add( Buff buff ){

        boolean result = super.add( buff );

        interrupt();

        BuffIndicator.refreshHero();

        return result;
    }

    @Override
    public void remove( Buff buff ){

        super.remove( buff );

        BuffIndicator.refreshHero();

    }


    @Override
    public float awareness(){

        float result = super.awareness();

        if( heroClass == HeroClass.SCHOLAR){
            result *= 0.8f + 0.01f * ( ( lvl - 1 ) / 4 );
        } else if( heroClass == HeroClass.ACOLYTE){
            result *= 1.1f + 0.01f * ( ( lvl - 1 ) / 2 );
        }//added awareness grow to other classes
        else{
            result *= 1f + 0.01f * ( ( lvl - 1 ) / 3 );
        }

        if( belongings.armor instanceof HuntressArmor && belongings.armor.bonus >= 0 ){
            result += ( 0.05f + 0.05f * belongings.armor.bonus );
        }

        result +=  ringBuffsBaseZero(RingOfAwareness.Awareness.class)/2;

        Satiety satiety=buff(Satiety.class);
        if (satiety!=null && satiety.energy()>Satiety.DEFAULT)
            result +=  ringBuffsBaseZero( RingOfSatiety.Satiety.class )/4;

        if( restoreHealth ){
            result *= 0.5f;
        }

        return result;
    }

    @Override
    public float stealth(){

        return baseStealth( true );

    }

    public float baseStealth( boolean identified ){

        float result = super.stealth();

        if( heroClass == HeroClass.WARRIOR ){
            //result *= 0.75f;
            result *= 0.8f + 0.01f * ( ( lvl - 1 ) / 4 );
        } else if( heroClass == HeroClass.BRIGAND){
            result *= 1.1f + 0.01f * ( ( lvl - 1 ) / 2 );
        }//added stealth grow to other classes
        else{
            result *= 1f + 0.01f * ( ( lvl - 1 ) / 3 );
        }

        if( belongings.armor instanceof RogueArmor && belongings.armor.bonus >= 0 ){
            result += ( 0.05f + 0.05f * belongings.armor.bonus );
        }

        result +=  ringBuffsBaseZero( RingOfShadows.Shadows.class )/2;

        Satiety satiety=buff(Satiety.class);
        if (satiety!=null && satiety.energy()>Satiety.DEFAULT)
            result +=  ringBuffsBaseZero( RingOfSatiety.Satiety.class )/4;

        if( !restoreHealth ){

            int penalty=0;
            if( belongings.armor != null ){
                penalty+= belongings.armor.currentPenalty( this, belongings.armor.strShown( identified || belongings.armor.isIdentified()));
            }

            if( belongings.weap1 != null ){
                penalty+= belongings.weap1.currentPenalty( this, belongings.weap1.strShown( identified || belongings.weap1.isIdentified()));
            }

            if( belongings.weap2 != null ){
                penalty+= belongings.weap2.currentPenalty( this, belongings.weap2.strShown( identified || belongings.weap2.isIdentified()));
            }

            result-=penalty*0.025f;
        }
        return result;
    }

    public float baseWillpower(){
        float result=super.willpower();

        if( heroClass == HeroClass.BRIGAND){
            result *= 0.8f + 0.01f * ( ( lvl - 1 ) / 4 );
        } else if( heroClass == HeroClass.SCHOLAR){
            result *= 1.1f + 0.01f * ( ( lvl - 1 ) / 2 );
        }//added Willpower grow to other classes
        else{
            result *= 1f + 0.01f * ( ( lvl - 1 ) / 3 );
        }
        return result;
    }

    public float willpower(){
        float result=baseWillpower();

        result +=  ringBuffsBaseZero( RingOfConcentration.Concentration.class )/2;

        if (subClass == HeroSubClass.CONJURER)
            result +=0.1f;

        Satiety satiety=buff(Satiety.class);
        if (satiety!=null ) {
            if (satiety.energy() > Satiety.DEFAULT)
                result += ringBuffsBaseZero(RingOfSatiety.Satiety.class) / 4;
            else if (satiety.energy() <= Satiety.STARVING)
                result *= 0.5f;
            else if (satiety.energy() <= Satiety.PARTIAL)
                result *= 0.75f;
        }

        if( belongings.armor instanceof MageArmor && belongings.armor.bonus >= 0 ){
            result += ( 0.05f + 0.05f * belongings.armor.bonus );
        }
        if( restoreHealth ){
            result *= 0.5f;
        }

        return result;
    }

    @Override
    public void die( Object cause, Element dmg ){

        curAction = null;

        boolean rezzed = false;

        Ankh ankh = belongings.getItem( Ankh.class );

        if( ankh != null ){

            Ankh.resurrect( this );
            Statistics.ankhsUsed++;
            ankh.detach( Dungeon.hero.belongings.backpack );
            rezzed = true;

        }

        if( !rezzed ){
            //updates time statics
            Actor.fixTime();
            super.die( cause, dmg );
            reallyDie( cause, dmg );
        }
    }

    public static void reallyDie( Object cause, Element dmg ){
        Camera.main.shake( 4, 0.3f );
        GameScene.flash( 0xBB0000 );

        int length = Level.LENGTH;
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Level.discoverable;

        for( int i = 0 ; i < length ; i++ ){

            int terr = map[ i ];

            if( discoverable[ i ] ){

                visited[ i ] = true;
                if( ( Terrain.flags[ terr ] & Terrain.TRAPPED ) != 0 ){
                    Level.set( i, Terrain.discover( terr ) );
                    GameScene.updateMap( i );
                }
            }
        }

        Bones.leave();

        Dungeon.observe();

        Dungeon.hero.belongings.identify();

        int pos = Dungeon.hero.pos;

        ArrayList<Integer> passable = new ArrayList<Integer>();
        for( Integer ofs : Level.NEIGHBOURS8 ){
            int cell = pos + ofs;
            if( ( Level.passable[ cell ] || Level.avoid[ cell ] ) && Dungeon.level.heaps.get( cell ) == null ){
                passable.add( cell );
            }
        }
        Collections.shuffle( passable );

        ArrayList<Item> items = new ArrayList<Item>( Dungeon.hero.belongings.backpack.items );
        for( Integer cell : passable ){
            if( items.isEmpty() ){
                break;
            }

            Item item = Random.element( items );
            Dungeon.level.drop( item, cell ).sprite.drop( pos );
            items.remove( item );
        }

        GameScene.gameOver();

        Dungeon.fail( ResultDescriptions.generateResult( cause, dmg ) );

        GLog.n( ResultDescriptions.generateMessage( cause, dmg ) );

        Dungeon.deleteGame( Dungeon.hero.heroClass, true );
    }

//	@Override
//	public void move( int step ) {
//		super.move(step);
//
//		if (!flying) {
//
//			if (Level.water[pos]) {
//				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
//			} else {
//				Sample.INSTANCE.play( Assets.SND_STEP );
//			}
//		}
//
//        Dungeon.energy.press(pos, this);
//	}

    @Override
    public void onMotionComplete(){
        Dungeon.observe();
        search( false );

        super.onMotionComplete();
    }

    //FIXME
    public void onSlamComplete(Char enemy, Item curItem){
         if( enemy instanceof Mob ){
            TagAttack.target( (Mob) enemy );
        }

        Shield shield=(Shield)curItem;

        currentWeapon=new Shield.ShieldSlam(shield);
        attack(enemy,damageRoll());

        curAction = null;

        Invisibility.dispel();

        super.onAttackComplete();
    }



    @Override
    public void onAttackComplete(){

        // For a good measure...
        if( enemy instanceof Mob ){
            TagAttack.target( (Mob) enemy );
        }

        if (currentWeapon instanceof DoubleBlade) {
            int pos;
            //attacking nearby enemies dont increase combo
            currentWeapon.setIncreaseCombo(false);
            for (int n : Level.NEIGHBOURS8) {
                pos = this.pos + n;

                if (Level.solid[pos] || pos == enemy.pos)//ignore main target
                    continue;

                Char ch = Actor.findChar(pos);
                if (ch != null && !(ch instanceof NPC)) {
                    attack( ch );
                }
            }
            currentWeapon.setIncreaseCombo(true);
        }
        attack( enemy );
        curAction = null;

        Invisibility.dispel();
        super.onAttackComplete();
    }

    @Override
    public void onOperateComplete(){

        if( curAction instanceof HeroAction.Unlock ){

            if( theKey != null ){
                theKey.detach( belongings.backpack );
                theKey = null;
            }

            int doorCell = curAction.dst;
            int door = Dungeon.level.map[ doorCell ];

            Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR_CLOSED : Terrain.UNLOCKED_EXIT );
            GameScene.updateMap( doorCell );

        } else if( curAction instanceof HeroAction.Examine ){

            int cell = curAction.dst;

            Bookshelf.examine( cell );

        } else if( curAction instanceof HeroAction.OpenChest ){

            if( theKey != null ){
                theKey.detach( belongings.backpack );
                theKey = null;
            }

            Heap heap = Dungeon.level.heaps.get( curAction.dst );
//			if (heap.type == Type.BONES || heap.type == Type.BONES_CURSED) {
//				Sample.INSTANCE.play( Assets.SND_BONES );
//			}

            heap.open();
        }
        curAction = null;

        super.onOperateComplete();
    }

    public void search( boolean intentional ){

        boolean smthFound = false;

        for( Integer ofs : Level.NEIGHBOURS8 ){
            int p = pos + ofs;

            try {
                if (Dungeon.visible[p]) {

                    if (intentional) {
                        sprite.parent.addToBack(new CheckedCell(p));
                    }

                    if ((Level.trapped[p] || Level.illusory[p]) && (intentional
                            || Level.illusory[p] && buff(Light.class) != null
                            || Random.Float() < ((0.45f - Dungeon.chapter() * 0.05f) * awareness()))
                            ) {

                        int oldValue = Dungeon.level.map[p];

                        GameScene.discoverTile(p, oldValue);

                        Level.set(p, Terrain.discover(oldValue));

                        GameScene.updateMap(p);

                        ScrollOfClairvoyance.discover(p);

                        smthFound = true;
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
        }

        if( intentional ){
            sprite.showStatus( CharSprite.DEFAULT, TXT_SEARCH );
            spendAndNext( TIME_TO_SEARCH / ( 1.0f + awareness() ) );
            sprite.search();
        }

        if( smthFound ){
            GLog.w( TXT_NOTICED_SMTH );
            Sample.INSTANCE.play( Assets.SND_SECRET );
            interrupt();
        }

//		return smthFound;
    }


    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();

        if( buff( Shielding.class ) != null ){
            for( Class<? extends Element> type : Shielding.RESISTS) {
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + Shielding.RESISTANCE );
            }
        }

        if( buff( Frozen.class ) != null ){
            resistances.put( Element.Frost.class, ( resistances.containsKey( Element.Frost.class ) ? resistances.get( Element.Frost.class ) : 0.0f )+Element.Resist.PARTIAL );
        }

        if( buff( Petrificated.class ) != null ){
            resistances.put( Element.Body.class, ( resistances.containsKey( Element.Body.class ) ? resistances.get( Element.Body.class ) : 0.0f )+Element.Resist.IMMUNE );
        }

        if( buff( RingOfProtection.Protection.class ) != null ){

            float value = Dungeon.hero.ringBuffsBaseZero( RingOfProtection.Protection.class ) / 2 ;

            for( Class<? extends Element> type : RingOfProtection.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        if( buff( RingOfConcentration.Concentration.class ) != null ){

            float value = Dungeon.hero.ringBuffsBaseZero( RingOfConcentration.Concentration.class ) ;

            for( Class<? extends Element> type : RingOfConcentration.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        if( buff( RingOfVitality.Vitality.class ) != null ){

            float value = Dungeon.hero.ringBuffsBaseZero( RingOfVitality.Vitality.class ) ;

            for( Class<? extends Element> type : RingOfVitality.RESISTS ){
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + value );
            }
        }

        BodyArmor a = this.belongings.armor;
        if( a != null && a.glyph != null && a.glyph.resistance() != null ){

            Class<? extends Element> type = a.glyph.resistance();

            if( !a.isCursed() ){
                //resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.1f + a.magicBonus() * 0.05f );
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.2f + a.bonus * 0.1f );
            } else {
                resistances.put( type, (resistances.containsKey( type ) ? resistances.get( type ) : 0.0f )- a.bonus * 0.05f );
            }
        }

        Shield s = this.belongings.weap2 instanceof Shield ? (Shield) this.belongings.weap2 : null;
        if( s != null && s.glyph != null && s.glyph.resistance() != null ) {

            Class<? extends Element> type = s.glyph.resistance();

            if( !s.isCursed()){
                //resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.1f + s.magicBonus() * 0.05f );
                resistances.put( type, ( resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) + 0.2f + s.bonus * 0.1f );
            } else {
                resistances.put( type, (resistances.containsKey( type ) ? resistances.get( type ) : 0.0f ) - s.bonus * 0.05f );
            }
        }

        //herbs resistances
        if (hasBuff(Electric_EnergyResistance.class)){
            resistances.put(Element.Shock.class,( resistances.containsKey( Element.Shock.class ) ? resistances.get( Element.Shock.class ) : 0.0f )+0.25f);
            resistances.put(Element.Energy.class,( resistances.containsKey( Element.Energy.class ) ? resistances.get( Element.Energy.class ) : 0.0f )+0.25f);
        }
        if (hasBuff(Body_AcidResistance.class)){
            resistances.put(Element.Body.class,( resistances.containsKey( Element.Body.class ) ? resistances.get( Element.Body.class ) : 0.0f )+0.25f);
            resistances.put(Element.Acid.class,( resistances.containsKey( Element.Acid.class ) ? resistances.get( Element.Acid.class ) : 0.0f )+0.25f);
        }
        if (hasBuff(Fire_ColdResistance.class)){
            resistances.put(Element.Flame.class,( resistances.containsKey( Element.Flame.class ) ? resistances.get( Element.Flame.class ) : 0.0f )+0.25f);
            resistances.put(Element.Frost.class,( resistances.containsKey( Element.Frost.class ) ? resistances.get( Element.Frost.class ) : 0.0f )+0.25f);
        }
        if (hasBuff(Toughness.class)){
            resistances.put(Element.Physical.class,( resistances.containsKey( Element.Physical.class ) ? resistances.get( Element.Physical.class ) : 0.0f )+0.25f);
        }

        return resistances;
    }

    @Override
    public void next(){
        super.next();
    }

   public void updateWeaponSprite() {
        ((HeroSprite)sprite).updateEquipment();
    }

    public void onLeaveLevel(Level level) {
        SummonSpiritWolfSkill spiritWolffSkill = buff(SummonSpiritWolfSkill.class);
        if (spiritWolffSkill != null){
            spiritWolffSkill.wolfInstance = spiritWolffSkill.getSpiritWolf();
            if (spiritWolffSkill.wolfInstance != null )
                Dungeon.level.mobs.remove( spiritWolffSkill.wolfInstance  );
        }
    }

    public void onEnterLevel(Level level) {
        SummonSpiritWolfSkill spiritWolffSkill = buff(SummonSpiritWolfSkill.class);
        if (spiritWolffSkill != null){

            if (spiritWolffSkill.wolfInstance != null){

                ArrayList<Integer> candidates = new ArrayList<Integer>();
                for (int n : Level.NEIGHBOURS8) {
                    int pos = this.pos + n;
                    if( !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ) {
                    spiritWolffSkill.wolfInstance.pos =  candidates.get( Random.Int( candidates.size() ) );
                    level.mobs.add( spiritWolffSkill.wolfInstance );
                    Actor.add(spiritWolffSkill.wolfInstance);

                    spiritWolffSkill.wolfInstance= null;

                }
            }
        }

    }


}