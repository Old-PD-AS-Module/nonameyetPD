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
package com.ravenwolf.nnypd.levels.painters;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.npcs.Shopkeeper;
import com.ravenwolf.nnypd.actors.mobs.npcs.ShopkeeperDemon;
import com.ravenwolf.nnypd.actors.mobs.npcs.ShopkeeperDwarf;
import com.ravenwolf.nnypd.actors.mobs.npcs.ShopkeeperGhost;
import com.ravenwolf.nnypd.actors.mobs.npcs.ShopkeeperRobot;
import com.ravenwolf.nnypd.actors.mobs.npcs.ShopkeeperTroll;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.armours.body.BodyArmorCloth;
import com.ravenwolf.nnypd.items.armours.shields.RoundShield;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.bags.Bag;
import com.ravenwolf.nnypd.items.bags.HerbPouch;
import com.ravenwolf.nnypd.items.bags.PotionSash;
import com.ravenwolf.nnypd.items.bags.ScrollHolder;
import com.ravenwolf.nnypd.items.bags.WandHolster;
import com.ravenwolf.nnypd.items.food.Pasty;
import com.ravenwolf.nnypd.items.misc.ArmorerKit;
import com.ravenwolf.nnypd.items.misc.Battery;
import com.ravenwolf.nnypd.items.misc.CraftingKit;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.misc.Waterskin;
import com.ravenwolf.nnypd.items.misc.Whetstone;
import com.ravenwolf.nnypd.items.potions.PotionOfMending;
import com.ravenwolf.nnypd.items.potions.PotionOfStrength;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfIdentify;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfUpgrade;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.Arbalest;
import com.ravenwolf.nnypd.items.weapons.ranged.LongBow;
import com.ravenwolf.nnypd.items.weapons.ranged.CompositeBow;
import com.ravenwolf.nnypd.items.weapons.ranged.LightCrossbow;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.ShortBow;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.items.weapons.throwing.BluntedArrows;
import com.ravenwolf.nnypd.items.weapons.throwing.Bolas;
import com.ravenwolf.nnypd.items.weapons.throwing.Boomerangs;
import com.ravenwolf.nnypd.items.weapons.throwing.Chakrams;
import com.ravenwolf.nnypd.items.weapons.throwing.Hammers;
import com.ravenwolf.nnypd.items.weapons.throwing.Harpoons;
import com.ravenwolf.nnypd.items.weapons.throwing.Javelins;
import com.ravenwolf.nnypd.items.weapons.throwing.Knives;
import com.ravenwolf.nnypd.items.weapons.throwing.MoonGlaive;
import com.ravenwolf.nnypd.items.weapons.throwing.PoisonDarts;
import com.ravenwolf.nnypd.items.weapons.throwing.Quarrels;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingSpears;
import com.ravenwolf.nnypd.items.weapons.throwing.Shurikens;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.items.weapons.throwing.Tomahawks;
import com.ravenwolf.nnypd.levels.CavesLevel;
import com.ravenwolf.nnypd.levels.CityLevel;
import com.ravenwolf.nnypd.levels.LastShopLevel;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.PrisonLevel;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.SewerLevel;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopPainter extends Painter {

	private static int pasWidth;
	private static int pasHeight;

    private static ArrayList<Item> kits;
    private static ArrayList<Item> ammo1;
    private static ArrayList<Item> ammo2;

    private static Item[] defaultKits = { new Whetstone(), new ArmorerKit(), new CraftingKit(), new Battery() };
    private static Item[] defaultAmmo1 = { new Arrows(), new Arrows(), new Quarrels(), new BluntedArrows(),  new Quarrels() };
    //private static Item[] defaultAmmo1 = { new Arrows(), new Arrows(), new Quarrels(), new Quarrels() };
    //private static Item[] defaultAmmo2 = { new Bullets(), new Bullets(), new Explosives.Gunpowder(), new Explosives.Gunpowder() };

    private static final String KITS		= "shops_kits";
    private static final String AMMO1		= "shops_ammo1";
    private static final String AMMO2		= "shops_ammo2";

    public static void initAssortment() {

        kits = new ArrayList<>();
        kits.addAll( Arrays.asList( defaultKits ) );

        ammo1 = new ArrayList<>();
        ammo1.addAll( Arrays.asList( defaultAmmo1 ) );

        //ammo2 = new ArrayList<>();
        //ammo2.addAll( Arrays.asList( defaultAmmo2 ) );

    }

    public static void saveAssortment( Bundle bundle) {

        bundle.put( KITS, kits );
        bundle.put( AMMO1, ammo1 );
        //bundle.put( AMMO2, ammo2 );

    }

    public static void loadAssortment( Bundle bundle ) {

        kits = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( KITS )) {
            if( item != null ){
                kits.add( (Item)item );
            }
        }

        ammo1 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO1 )) {
            if( item != null ){
                ammo1.add( (Item)item );
            }
        }

        ammo2 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO2 )) {
            if( item != null ){
                ammo2.add( (Item)item );
            }
        }
    }

	public static void paint( Level level, Room room ) {
		
		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY_SP);

		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;
		int per = pasWidth * 2 + pasHeight * 2;
		
		Item[] range = range( level );
		
		int pos = xy2p( room, room.entrance() ) + (per - range.length) / 2;
		for (int i=0; i < range.length; i++) {
			
			Point xy = p2xy( room, (pos + per) % per );
			int cell = xy.x + xy.y * Level.WIDTH;
			
			if (level.heaps.get( cell ) != null) {
				do {
					cell = room.random();
				} while (level.heaps.get( cell ) != null);
			}
			
			level.drop( range[i], cell, true ).type = Heap.Type.FOR_SALE;
			
			pos++;
		}
		
		placeShopkeeper( level, room );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
	}
	
	private static Item[] range( Level level ) {
		
		ArrayList<Item> items = new ArrayList<Item>();

        if ( level instanceof LastShopLevel) {

            Weapon weap1= (Weapon) generateItemFromCategory(Generator.Category.TIER2_WEAP, Weapon.class);
            weap1.enchant();
            weap1.upgrade(6-weap1.lootChapter());
            weap1.identify();
            items.add(weap1);

            Weapon weap2= (Weapon)Random.oneOf(generateItemFromCategory(Generator.Category.TIER3_WEAP, MeleeWeapon.class),generateItemFromCategory(Generator.Category.TIER4_WEAP, MeleeWeapon.class));
            weap2.enchant();
            weap2.upgrade(6-weap2.lootChapter());
            weap2.identify();
            items.add(weap2);

            Armour armor= (Armour) Random.oneOf(generateItemFromCategory(Generator.Category.TIER2_ARMOR, BodyArmor.class), generateItemFromCategory(Generator.Category.TIER3_ARMOR, BodyArmor.class),generateItemFromCategory(Generator.Category.TIER4_ARMOR, BodyArmor.class));
            armor.inscribe();
            armor.upgrade(6-armor.lootChapter());
            armor.identify();
            items.add(armor);

            Item extra= (Item) Random.oneOf(generateItemFromCategory(Generator.Category.TIER2_ARMOR, Shield.class), generateItemFromCategory(Generator.Category.TIER3_ARMOR, Shield.class),generateItemFromCategory(Generator.Category.TIER4_ARMOR, Shield.class),
                    new CompositeBow(),new LongBow(), new LightCrossbow(), new Arbalest());
            if (extra instanceof Armour)
                ((Armour)extra).inscribe();
            else if (extra instanceof Weapon)
                ((Weapon)extra).enchant();

            extra.upgrade(6-armor.lootChapter());
            extra.identify();
            items.add(extra);

            items.add( Random.oneOf(
                    new PoisonDarts(), new Bolas(), new Boomerangs(), new ThrowingSpears(), new Hammers(),
                    new Shurikens(), new Chakrams(), new MoonGlaive(),
                    new Tomahawks(), new Harpoons()
            ).random());

            items.add(Random.oneOf(
                    new BluntedArrows(), new Arrows(), new Quarrels(), new Quarrels()
            ).random());

            items.add(Random.oneOf(new Explosives.BombStick(), new Explosives.BombBundle()
            ).random());

            items.add(Generator.random(Generator.Category.RING).uncurse().upgrade(2));
            items.add(Generator.random(Generator.Category.WAND).uncurse().upgrade(2));

            items.add(new PotionOfMending());
            items.add(new PotionOfStrength() );
            items.add(Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            items.add(new ScrollOfUpgrade());
            items.add(Generator.random(Generator.Category.SCROLL));
            items.add(new Pasty());

            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());

        } else {
            int bonus=0;
            Bag bag = null;
            Weapon weapon = null;
            Armour armour = null;
            Item ranged = null;
            ThrowingWeapon thrown = null;
            Item extra = null;

            if (level instanceof SewerLevel) {
                bonus=1;
                bag = new HerbPouch();
                weapon = (Weapon) generateItemFromCategory(Generator.Category.TIER1_WEAP, MeleeWeapon.class);
                armour = (Armour) generateItemFromCategory(Generator.Category.TIER1_ARMOR, BodyArmor.class);
                extra = Random.oneOf(new ShortBow(), new Bolas(), new RoundShield());
                thrown = Random.oneOf(new PoisonDarts(), new Knives());

            } else if (level instanceof PrisonLevel || Dungeon.chapter()==2) {

                bag = new PotionSash();

                weapon = (Weapon) generateItemFromCategory(Generator.Category.TIER2_WEAP, MeleeWeapon.class);
                armour = (Armour) generateItemFromCategory(Generator.Category.TIER2_ARMOR, BodyArmor.class);
                ranged = Random.oneOf(new LongBow(),new LongBow(), new LightCrossbow()/*,new Pistole()*/);
                thrown = Random.oneOf(new Javelins(), new Boomerangs(), new Shurikens());
                extra = Random.oneOf( generateItemFromCategory(Generator.Category.TIER1_ARMOR, Shield.class), generateItemFromCategory(Generator.Category.TIER1_WEAP, MeleeWeapon.class));
            } else if (level instanceof CavesLevel) {

                bag = new ScrollHolder();

                weapon = (Weapon) generateItemFromCategory(Generator.Category.TIER3_WEAP, MeleeWeapon.class);
                armour = (Armour) generateItemFromCategory(Generator.Category.TIER3_ARMOR, BodyArmor.class);
                ranged = Random.oneOf(new LightCrossbow(), new LightCrossbow(), /*new Arquebuse(),*/ new CompositeBow());
                thrown = Random.oneOf( new Tomahawks(), new Hammers(), new MoonGlaive());
                extra = Random.oneOf( generateItemFromCategory(Generator.Category.TIER2_ARMOR, Shield.class), generateItemFromCategory(Generator.Category.TIER2_WEAP, MeleeWeapon.class));

            } else if (level instanceof CityLevel || Dungeon.chapter()==4) {

                bag = new WandHolster();

                weapon = (Weapon) generateItemFromCategory(Generator.Category.TIER4_WEAP, MeleeWeapon.class);
                armour = (Armour) generateItemFromCategory(Generator.Category.TIER4_ARMOR, BodyArmor.class);
                ranged = Random.oneOf(/*new Handcannon(),*/ new Arbalest()/*new Explosives.BombStick()*/, new CompositeBow());
                thrown = Random.oneOf(new Harpoons(), new Chakrams(), new ThrowingSpears());
                extra = Random.oneOf( generateItemFromCategory(Generator.Category.TIER3_ARMOR, Shield.class), generateItemFromCategory(Generator.Category.TIER3_WEAP, MeleeWeapon.class));
            }

            if( bag != null ) {
                items.add(bag);
            }

            int maxUpgradeLvl=Math.min(Dungeon.chapter(),2);

            if( extra != null ) {
                extra.identify();

                if( extra instanceof BodyArmorCloth) {
                    if (Random.Int(6)+Dungeon.chapter()>3)
                        ((BodyArmorCloth) extra).inscribe();
                }
                //except the first chapter extra item have at least one upgrade
                extra.upgrade(Random.Int(1-bonus,maxUpgradeLvl+1));
                if (extra instanceof MeleeWeapon){
                    if (Random.Int(6)+Dungeon.chapter()+bonus>5) {
                        ((MeleeWeapon)extra).enchant();
                    }
                }
                items.add(extra);
            }

            if( weapon != null ) {
                weapon.identify();
                weapon.upgrade(Random.Int(maxUpgradeLvl));
                if (Random.Int(6)+Dungeon.chapter()+bonus>5) {
                    weapon.enchant();
                }
                items.add(weapon);
            }

            if( armour != null ) {
                armour.identify();
                armour.upgrade( Random.Int( maxUpgradeLvl ) );
                if (Random.Int(6)+Dungeon.chapter()>5)
                    armour.inscribe();
                items.add(armour);
            }

            if( ranged instanceof RangedWeapon ) {
                ranged.identify();
                ranged.upgrade(Random.Int(maxUpgradeLvl));
                if (Random.Int(6)+Dungeon.chapter()>5) {
                    ((RangedWeapon) ranged).enchant();
                    Random.IntRange( 0, Random.Int( Dungeon.chapter() ));
                }
                items.add(ranged);
            } else if( ranged instanceof ThrowingWeapon || ranged instanceof Explosives) {
                ranged.random();
                items.add(ranged);
            }

            if( thrown != null ) {
                thrown.random();
                items.add(thrown);
            }

            items.add( generateAmmo1() );

            Ring ring = (Ring)Generator.random(Generator.Category.RING);
            if( ring != null) {
                ring.bonus = Random.Int( Dungeon.chapter() );
                ring.uncurse(3);
                items.add(ring);
            }

            Wand wand = (Wand)Generator.random(Generator.Category.WAND);
            if( wand != null ) {
                wand.bonus = Random.Int( Dungeon.chapter() );
                wand.uncurse(3);
                wand.initialCharges();
                items.add(wand);
            }

            items.add(new PotionOfMending());
            items.add(Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            items.add(Generator.random(Generator.Category.SCROLL));


            items.add(new Pasty());
            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());
        }
		
		Item[] range = items.toArray( new Item[0] );
		
		return range;
	}

    private static Item generateAmmo1() {
        if( !ammo1.isEmpty() ) {
            return ammo1.remove( Random.Int( ammo1.size() ) ).random();
        } else {
            return Random.oneOf( defaultAmmo1 ).random();
        }
    }

    private static <T extends Item> Item generateItemFromCategory(Generator.Category cat, Class<T> c){
        Item item = null;
        while (item == null || !c.isInstance(item)) {
            try {
                item=  (Item) cat.classes[Random.chances(cat.probs)].newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return item;
    }

    private static Item generateKits() {
        if( !kits.isEmpty() ) {
            return kits.remove( Random.Int( kits.size() ) );
        } else {
            return Random.oneOf( defaultKits );
        }
    }
	
	private static void placeShopkeeper( Level level, Room room ) {

        int pos;
        do {
            pos = room.random(1);
        } while (level.heaps.get(pos) != null);

        Mob shopkeeper;

        if (level instanceof LastShopLevel) {
            shopkeeper = new ShopkeeperDemon();
        } else if( level instanceof CityLevel) {
            shopkeeper = new ShopkeeperDwarf();
        } else if( level instanceof CavesLevel) {
            if(Dungeon.cavesOption==Dungeon.MINES_OPTION)
                shopkeeper = new ShopkeeperRobot();
            else
                shopkeeper = new ShopkeeperTroll();
        } else if( level instanceof PrisonLevel) {
            shopkeeper = new ShopkeeperGhost();
        } else {
            shopkeeper = new Shopkeeper();
        }

        shopkeeper.pos = pos;
        level.mobs.add( shopkeeper );

		if (level instanceof LastShopLevel) {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				int p = shopkeeper.pos + Level.NEIGHBOURS9[i];
				if (level.map[p] == Terrain.EMPTY_SP) {
					level.map[p] = Terrain.WATER;
				}
			}
		}
	}
	
	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else /*if (xy.x == room.left)*/ {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {

			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
}
