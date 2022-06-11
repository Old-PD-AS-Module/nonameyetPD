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
package com.ravenwolf.nnypd.items;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.*;
import com.ravenwolf.nnypd.items.armours.shields.*;
import com.ravenwolf.nnypd.items.bags.Bag;
import com.ravenwolf.nnypd.items.food.OverpricedRation;
import com.ravenwolf.nnypd.items.herbs.*;
import com.ravenwolf.nnypd.items.misc.Ankh;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.potions.*;
import com.ravenwolf.nnypd.items.rings.*;
import com.ravenwolf.nnypd.items.scrolls.*;
import com.ravenwolf.nnypd.items.wands.*;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.*;
import com.ravenwolf.nnypd.items.weapons.ranged.*;
import com.ravenwolf.nnypd.items.weapons.throwing.*;
import com.watabou.utils.Random;

import java.util.HashMap;

public class Generator {

	public enum Category {

        WEAPON	( 50/*60*/,	Weapon.class ),
        ARMOR	( 30/*35*/,	Armour.class ),
        WAND	( 3,	Wand.class ),
        RING	( 2,	Ring.class ),

        POTION	( 40,	Potion.class ),
        SCROLL	( 35,	Scroll.class ),
        THROWING( 25,   Item.class ),
        MISC	( 6,	Item.class ),

        GOLD	( 0,	Gold.class ),
        HERB    ( 0,	Herb.class ),
        AMMO    ( 0,	Item.class ),
 //       KITS	( 0,	Item.class ),

        TIER1_WEAP ( 0,	Weapon.class ),
        TIER2_WEAP ( 0,	Weapon.class ),
        TIER3_WEAP ( 0,	Weapon.class ),
        TIER4_WEAP ( 0,	Weapon.class ),

        TIER1_ARMOR ( 0,	Armour.class ),
        TIER2_ARMOR ( 0,	Armour.class ),
        TIER3_ARMOR ( 0,	Armour.class ),
        TIER4_ARMOR ( 0,	Armour.class ),
        ;

		public Class<?>[] classes;
		public float[] probs;

		public float prob;
		public Class<? extends Item> superClass;
		
		Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	}

    private static HashMap<Category,Float> categoryProbs = new HashMap<>();
	private static HashMap<Category,Float> equipmentProbs = new HashMap<>();
	private static HashMap<Category,Float> comestibleProbs = new HashMap<>();

	static {
/*
        Category.WEAPON.probs = null;
		Category.WEAPON.classes = null;
*/

        Category.TIER1_WEAP.probs = new float[]{ 20, 12, 20, 14, 10, 16/*, 8*/};
        Category.TIER1_WEAP.classes = new Class<?>[]{
                Dagger.class,
                Knuckles.class,
                Quarterstaff.class,
                Shortsword.class,
                Axe.class,
                ShortBow.class,
                //Sling.class,
                //Pistole.class,
        };

        Category.TIER2_WEAP.probs = new float[]{ 16, 18, 14, 14, 10, 14, 14};
        Category.TIER2_WEAP.classes = new Class<?>[]{
                Spear.class,
                Mace.class,
                Scimitar.class,

                LongStaff.class,
                AssassinBlade.class,
                Battleaxe.class,

                LongBow.class,
        };

        Category.TIER3_WEAP.probs = new float[]{ 20, 14, 14, 16, 10, 14, 12};
        Category.TIER3_WEAP.classes = new Class<?>[]{
                Broadsword.class,
                Katar.class,
                //DeerHornBlade.class,
                DoubleBlade.class,
                Glaive.class,
                Katana.class,

                CompositeBow.class,
                LightCrossbow.class,
        };

        Category.TIER4_WEAP.probs = new float[]{ 16, 12, 10, 10, 12, 16, 12, 12/*, 6*/};
        Category.TIER4_WEAP.classes = new Class<?>[]{
                Greataxe.class,
                Flail.class,
                ObsidianBlade.class,
                Kopesh.class,

                Halberd.class,
                Greatsword.class,
                Warhammer.class,
                Arbalest.class,
                //Handcannon.class,
        };

        Category.TIER1_ARMOR.probs = new float[]{ 22, 22, 22, 14, 20 };
        Category.TIER1_ARMOR.classes = new Class<?>[]{
                MageArmor.class,
                RogueArmor.class,
                HuntressArmor.class,
                RoundShield.class,
                StuddedArmor.class,
        };

        Category.TIER2_ARMOR.probs = new float[]{ 12, 8, 32, 48 };
        Category.TIER2_ARMOR.classes = new Class<?>[]{
                LargeShield.class,
                WarShield.class,
                StuddedArmor.class,
                DiscArmor.class,
        };

        Category.TIER3_ARMOR.probs = new float[]{ 40, 40, 12, 8 };
        Category.TIER3_ARMOR.classes = new Class<?>[]{
                MailArmor.class,
                SplintArmor.class,
                KiteShield.class,
                ArcaneShield.class,
        };

        Category.TIER4_ARMOR.probs = new float[]{  40, 40, 12, 8 };
        Category.TIER4_ARMOR.classes = new Class<?>[]{
                ScaleArmor.class,
                PlateArmor.class,
                GreatShield.class,
                TowerShield.class,
        };

        Category.WAND.probs = null;
        Category.WAND.classes = new Class<?>[]{
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

        Category.RING.probs = null;
        Category.RING.classes = new Class<?>[]{
                RingOfShadows.class,
                RingOfAccuracy.class,
                RingOfEvasion.class,
                RingOfAwareness.class,
                RingOfVitality.class,
                RingOfSatiety.class,
                RingOfConcentration.class,
                RingOfProtection.class,
                RingOfFortune.class,
                //RingOfKnowledge.class,
               // RingOfDurability.class,
                RingOfFuror.class,
                RingOfSorcery.class,
                RingOfSharpShooting.class,
        };

        Category.SCROLL.probs = new float[]{ 60, 40, 40, 15, 20, 25, 15, 15, 20, 20, 5, 2 };
        Category.SCROLL.classes = new Class<?>[]{
                ScrollOfIdentify.class,
                ScrollOfRemoveCurse.class,

                //ScrollOfTransmutation.class,
                //ScrollOfDarkness.class,
                ScrollOfRecharging.class,
                ScrollOfSunlight.class,

                ScrollOfPhaseWarp.class,
                ScrollOfClairvoyance.class,

                ScrollOfBanishment.class,
                ScrollOfChallenge.class,

                ScrollOfTorment.class,
                ScrollOfRaiseDead.class,

                ScrollOfEnchantment.class,
                ScrollOfUpgrade.class,
        };

        Category.POTION.probs = new float[]{ 50, 30, 25, 25, 20, 20, 20, 20, /*20, 20,*/ 15, 15, 0, 2 };
        Category.POTION.classes = new Class<?>[]{
                PotionOfMending.class,
                PotionOfLiquidFlame.class,

                PotionOfMindVision.class,
                PotionOfLevitation.class,

                PotionOfFrigidVapours.class,
                PotionOfCorrosiveGas.class,

                PotionOfInvisibility.class,
                PotionOfSparklingDust.class,

   //             PotionOfConfusionGas.class,
   //             PotionOfMiasma.class,

                PotionOfOvergrowth.class,
                PotionOfBlessing.class,

                PotionOfStrength.class,
                PotionOfWisdom.class,
        };

        Category.THROWING.probs = new float[]{ 5, 8, 8, 10, 10, 10, 10, 10, 9, 8, 7, 6 , 8, 7, 6};
        Category.THROWING.classes = new Class<?>[]{
                //Bullets.class,
                BluntedArrows.class,
                Arrows.class,
                Quarrels.class,

                PoisonDarts.class,
                Knives.class,
                Bolas.class,

                Shurikens.class,
                Javelins.class,
                Tomahawks.class,

                Boomerangs.class,
                Chakrams.class,
                Harpoons.class,

                Hammers.class,
                ThrowingSpears.class,
                MoonGlaive.class,
        };

        Category.MISC.probs = new float[]{ 5, 2, 2, 2, 4, 4, 4, /*3, 3, 3, 3,*/ 0.5f };
        Category.MISC.classes = new Class<?>[]{
            Gold.class,

            //Explosives.Gunpowder.class,
                ScrollOfIdentify.class,
                ScrollOfRemoveCurse.class,
                PotionOfMending.class,
            Explosives.BombStick.class,

            OverpricedRation.class,
            OilLantern.OilFlask.class,

            /*
            Whetstone.class,
            ArmorerKit.class,
            CraftingKit.class,
            Battery.class,
            */

            Ankh.class,
        };

        Category.HERB.probs = new float[]{ 8, 8, 8, 8, 8, 8, 8, 8, 5, 2 };
        Category.HERB.classes = new Class<?>[]{
            FirebloomHerb.class,
            IcecapHerb.class,
            SorrowmossHerb.class,
            StormvineHerb.class,
            DreamweedHerb.class,
            SungrassHerb.class,
            WhirlvineHerb.class,
            FadeleafHerb.class,
            EarthrootHerb.class,
            WyrmflowerHerb.class,
        };

        Category.AMMO.probs = null;
        Category.AMMO.classes = new Class<?>[]{
            //Bullets.class,
                BluntedArrows.class,
            Arrows.class,
            Quarrels.class,
            //Explosives.Gunpowder.class,
            Explosives.BombStick.class,
            Explosives.BombBundle.class,
        };

        Category.GOLD.probs = null;
        Category.GOLD.classes = new Class<?>[]{
            Gold.class
        };
/*
        Category.KITS.probs = null;
        Category.KITS.classes = new Class<?>[]{
//            Whetstone.class,
//            ArmorerKit.class,
//            CraftingKit.class,
//            Battery.class,
        };*/
	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );

            if(
                    cat.superClass == Weapon.class
                    || cat.superClass == Armour.class
                    || cat.superClass == Wand.class
                    || cat.superClass == Ring.class
            ) {
                equipmentProbs.put( cat, cat.prob );
            } else {
                comestibleProbs.put( cat, cat.prob );
            }
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}

    public static Item randomEquipment() {
        return random( Random.chances( equipmentProbs ) );
    }

    public static Item randomComestible() {
        return random( Random.chances( comestibleProbs ) );
    }

    public static Item random( Category cat ) {
            return random( cat, true );
    }

    public static Item random( Category cat, Boolean weighted ) {
        try {

            if (weighted) {
                switch (cat) {
                    case ARMOR:
                        return randomArmor();
                    case WEAPON:
                        return randomWeapon();
                    case THROWING:
                        return randomThrowing();
                    case HERB:
                        return randomHerb();
                    default:

                }
            }

            return ( cat.probs != null ? ((Item) cat.classes[Random.chances(cat.probs)].newInstance()).random()
                    : ((Item) Random.element( cat.classes ).newInstance()).random() );

        } catch (Exception e) {

            return null;

        }
    }
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return cl.newInstance().random();
			
		} catch (Exception e) {

			return null;
			
		}
	}

    private static float[][] equipmentUpgradeChances = {
            { 90, 10 }, //relativeStrength 1
            { 76, 20,  4 }, //relativeStrength 0
            { 50, 30, 16, 4 }, //relativeStrength -1
            { 20, 45, 25, 10 }, //relativeStrength -2
            {  0,  5, 45, 25, 15, 5 }, //relativeStrength -3
            {  0,  0,  5, 35, 50, 10 }, //relativeStrength -4
            {  0,  0,  0, 25, 55, 20 }, //relativeStrength -5
    };

    private static float[][] equipmentTierChances = {
            { 80, 15, 5, 0 },
            { 30, 56, 12, 2 },
            { 14, 30, 48, 8 },
            { 5, 20, 45, 30 },
            { 0, 10, 45, 45 },
    };
	
	public static Armour randomArmor() throws Exception {

        int chapter = Dungeon.chapter();

        Category [] armors ={Category.TIER1_ARMOR,Category.TIER2_ARMOR,Category.TIER3_ARMOR,Category.TIER4_ARMOR,};
        Category tier = armors[Random.chances(equipmentTierChances[chapter-1])];

        Armour a = (Armour)tier.classes[Random.chances(tier.probs)].newInstance();

        int relativeStrength=a.lootChapter()-chapter;
        //cloth armors have better chance to be upgraded/enchanted
        if (a instanceof ClothArmor)
            relativeStrength--;

        int powerLevel=(relativeStrength > 0 ? 0 :1-relativeStrength);
        int upgradeValue=Random.chances(equipmentUpgradeChances[powerLevel]);

        float chanceToBeCursed = chapter*0.05f+0.05f +upgradeValue*0.075f + relativeStrength * (relativeStrength>0? 0.05f: -0.1f);
        float chanceToBeEnchanted = chapter*0.025f+0.1f -upgradeValue*0.075f - relativeStrength *0.05f;

        if (Random.Float()<chanceToBeCursed){
            a.inscribe();
            a.curse();
        }else{
            if (Random.Float()<chanceToBeEnchanted){
                a.inscribe();
            }
        }
        a.upgrade( upgradeValue );

        return a;

	}


	public static Weapon randomWeapon() throws Exception {

        Category [] weapons ={Category.TIER1_WEAP,Category.TIER2_WEAP,Category.TIER3_WEAP,Category.TIER4_WEAP,};


        int chapter = Dungeon.chapter();
        Category tier = weapons[Random.chances(equipmentTierChances[chapter-1])];

        Weapon w =(Weapon)tier.classes[Random.chances(tier.probs)].newInstance();

        int relativeStrength=w.lootChapter()-chapter;
        int powerLevel=(relativeStrength > 0 ? 0 :1-relativeStrength);
        int upgradeValue=Random.chances(equipmentUpgradeChances[powerLevel]);

        float chanceToBeCursed = chapter*0.05f+0.05f +upgradeValue*0.075f + relativeStrength * (relativeStrength>0? 0.05f: -0.1f);
        float chanceToBeEnchanted = chapter*0.025f+0.1f -upgradeValue*0.075f - relativeStrength *0.05f;

        if (Random.Float()<chanceToBeCursed){
            w.enchant();
            w.curse();
        }else{
            if (Random.Float()<chanceToBeEnchanted){
                w.enchant();
            }
        }
        w.upgrade( upgradeValue );

        return w;
	}

    public static Weapon randomThrowing() throws Exception {

        Category cat = Category.THROWING;
        int chapter = Dungeon.chapter();

        Weapon w =(Weapon)cat.classes[Random.chances(cat.probs)].newInstance();
        w.random();
        int r = Random.Int( 20-chapter );
        int allowedDelta = (r < 1 ? 3 : r < 3 ? 2 : r < 6 ? 1 : 0);

        if( chapter > 4 ) {
            allowedDelta += chapter - 4;
        }

        while ( ( chapter - w.lootChapter() > allowedDelta ) || ( w.lootChapter() - chapter > allowedDelta ) ) {
            w =(Weapon)cat.classes[Random.chances(cat.probs)].newInstance();
            w.random();
        }

        return w;
    }

    //early chapters have more chances to spawn common herbs
    //75% chapter 1, 50% chapter 2, 25% chapter 3

    public static Class<?>[] commonHerbs = new Class<?>[]{SungrassHerb.class, FirebloomHerb.class, WhirlvineHerb.class};

    private static Herb randomHerb() throws Exception {

        Category cat = Category.HERB;
        int chapter = Dungeon.chapter();

        if( Random.Int( chapter +2 ) < 2 ) {
            return (Herb)Random.element( commonHerbs).newInstance();
        } else {
            return (Herb)cat.classes[Random.chances( cat.probs ) ].newInstance();
        }
    }
}
