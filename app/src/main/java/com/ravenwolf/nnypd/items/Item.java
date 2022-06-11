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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSubClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.bags.Bag;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Item implements Bundlable {

	private static final String TXT_PACK_FULL	= "你的背包放不下%s了";

	private static final String TXT_TO_STRING		= "%s";
	private static final String TXT_TO_STRING_X		= "%s x%d";
	private static final String TXT_TO_STRING_LVL	= "%s %+d";
	private static final String TXT_TO_STRING_LVL_X	= "%s %+d x%d";

	public static final int MAX_BONUS_LVL		=5;

    public static final float TIME_TO_THROW		= 1.0f;
//    public static final float TIME_TO_PICK_UP	= 1.0f;
    public static final float TIME_TO_DROP		= 0.5f;

	public static final String AC_DROP		= "放下";
	public static final String AC_THROW		= "扔出";

    public static final int ITEM_UNKNOWN = 0;
    public static final int CURSED_KNOWN = 1;
    public static final int ENCHANT_KNOWN = 2;
    public static final int UPGRADE_KNOWN = 3;

    public String shortName = null;

    protected String name = "smth";
	protected int image = 0;

	public boolean stackable = false;
	public int quantity = 1;

	public int bonus = 0;
	//public int state = 0;

	public boolean unique = false;
	public boolean cursed = false;
	public int icon = -10;
	public boolean bones=true;
	public boolean visible = true;

    public int known = ITEM_UNKNOWN;
    //public int durability = 0;

	private static Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare( Item lhs, Item rhs ) {
			return Generator.Category.order( lhs ) - Generator.Category.order( rhs );
		}
	};

	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add( AC_DROP );
		actions.add( AC_THROW );
		return actions;
	}


	public int BottomIcon() {
		return isTypeKnown()? icon :-1;
	}

	public boolean isCursed() {
		//return bonus < 0;
		return cursed;
	}

	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {

			GameScene.pickUp( this );

            QuickSlot.refresh();

//            hero.spendAndNext( TIME_TO_PICK_UP );

            return true;

		} else {
			return false;
		}
	}

	public void doDrop( Hero hero ) {
		hero.spendAndNext(TIME_TO_DROP);
		Dungeon.level.drop( detachAll( hero.belongings.backpack ), hero.pos ).sprite.drop(hero.pos);
	}

	public void doThrow( Hero hero ) {
		GameScene.selectCell(thrower);
	}

	public void execute( Hero hero, String action ) {

		curUser = hero;
		curItem = this;

		if (action.equals( AC_DROP )) {

			doDrop( hero );

		} else if (action.equals( AC_THROW )) {

			doThrow(hero);

		}
	}

	public void execute( Hero hero ) {
		execute( hero, quickAction() );
	}

	protected void onThrow( int cell ) {

		throwItem(cell);
	}

	protected Item throwItem( int cell ) {

		Item item = detach(curUser.belongings.backpack);

		Heap heap = Dungeon.level.drop( item, cell );

		if (!heap.isEmpty()) {
			heap.sprite.drop(cell);
		}

		//assassin wont alert nearby enemies
		if (curUser.subClass == HeroSubClass.ASSASSIN && this instanceof ThrowingWeapon)
			return item;

		for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
			Char n = Actor.findChar( cell + Level.NEIGHBOURS9[i] );
			if (n != null && n instanceof Mob ) {
				((Mob)n).inspect( cell );
			}
		}

		return item;
	}

    public boolean collect() {
        return collect( Dungeon.hero.belongings.backpack );
    }

//	public boolean collect( Bag container ) {
//        return collect( container, false );
//    }

	public boolean collect( Bag container ) {

		ArrayList<Item> items = container.items;

        if (items.contains( this )) {
            return true;
        }

		for (Item item:items) {
			if (item instanceof Bag && ((Bag)item).grab( this )) {
				return collect( (Bag)item );
			}
		}

		if (stackable) {

			Class<?>c = getClass();
			for (Item item:items) {
				if (item.getClass() == c) {
					item.quantity += quantity;
					item.updateQuickslot();
					return true;
				}
			}
		}

//        if (
//            Dungeon.loaded() && Dungeon.hero != null && Dungeon.hero.isAlive() &&
//            this instanceof MeleeWeapon && this.isIdentified() &&
//            this.bonus >= 0 && Dungeon.hero.belongings.weap1 == null
//        ) {
//
//            Dungeon.hero.belongings.weap1 = (Weapon)this;
//            return true;
//
//        }

//		if (items.size() < container.size) {
		if ( container.countVisibleItems() < container.size || !visible ) {

//			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
//				Badges.validateItemLevelAcquired(this);
//			}

			items.add( this );
			QuickSlot.refresh();
			Collections.sort( items, itemComparator );
			return true;

		} else {

			GLog.n(TXT_PACK_FULL, name());
			return false;

		}
	}

    public final Item detach( Bag container, int amount ) {

        if (quantity <= 0) {

            return null;

        } else if (stackable) {

            try {

                if( quantity > amount ) {

                    quantity -= amount;
                    onDetach();

                } else {

                    detachAll( container );

                }

                updateQuickslot();

                return getClass().newInstance();

            } catch (Exception e) {
                return null;
            }
        } else {

            return detachAll( container );

        }
    }

	public final Item detach( Bag container ) {

        return detach(container, 1 );

	}

	public final Item detachAll( Bag container ) {

		for (Item item : container.items) {
			if (item == this) {
				container.items.remove( this );
				item.onDetach();
				QuickSlot.refresh();
				return this;
			} else if (item instanceof Bag) {
				Bag bag = (Bag)item;
				if (bag.contains( this )) {
					return detachAll( bag );
				}
			}
		}

		return this;
	}

	protected void onDetach() {
	}

    public Item uncurse( int n ) {

        for (int i=0; i < n; i++) {
            uncurse();
        }

        return this;
    }

    public Item uncurse() {

		cursed=false;

        QuickSlot.refresh();
        return this;
    }

	public Item upgrade() {

		//if(bonus < MAX_BONUS_LVL) {
		//	bonus++;
		// }

            /*if (cursed) {
				uncurse() ;
			}else */if(bonus < MAX_BONUS_LVL)
				bonus++;


        QuickSlot.refresh();
		return this;
	}

	final public Item upgrade( int n ) {
		for (int i=0; i < n; i++) {
			upgrade();
		}

		return this;
	}

	public Item curse() {

        cursed=true;

        QuickSlot.refresh();
		return this;
	}

    public void use( int amount ){
    }


	public boolean isUpgradeable() {
		return false ;
	}

	public boolean isIdentified() {	return true; }

    public boolean isEnchantKnown() { return true; }

    public boolean isCursedKnown() { return true; }

    public boolean isTypeKnown() { return true; }

    public boolean isEquipped( Hero hero ) { return false; }


	public Item identify() {

        identify( UPGRADE_KNOWN );

		return this;
	}

    public Item identify( int value ) {

        identify( value, false );

        return this;
    }

    public Item identify( int value, boolean forced ) {

        if( forced || known < value ) {
            known = value;
        }

		QuickSlot.refresh();

        return this;
    }

    public void activate( Char ch ) {
    }

	public static void evoke( Hero hero ) {
		hero.sprite.emitter().burst( Speck.factory( Speck.EVOKE ), 5 );
	}

	@Override
	public String toString() {

		if (isIdentified() && isUpgradeable()) {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_LVL_X, name(), bonus, quantity );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, name(), bonus);
			}
		} else {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_X, name(), quantity );
			} else {
				return Utils.format( TXT_TO_STRING, name() );
			}
		}
	}

    public String equipAction() {
        return null;
    }

	public String quickAction() {
		return null;
	}

	public String name() {
		return name;
	}

	public int image() {
		return image;
	}

    public int imageAlt() {
        return image;
    }

	public ItemSprite.Glowing glowing() {
		return null;
	}

	public String info() {
		return desc();
	}

	public String desc() {
		return "";
	}

	public int quantity() {
		return quantity;
	}

	public Item quantity( int value ) {
		quantity = value;
        return this;
	}


    public String lootChapterAsString() {
        switch( lootChapter() ){
            case 1:
                return "普通的";
            case 2:
                return "常见的";
            case 3:
                return "稀有的";
            case 4:
                return "卓越的";
            case 5:
                return "传说级";
            default:
                return "未知";
        }
    }

    public int lootChapter() {
        return 0;
    }

	public int lootLevel() {
		return 0;
	}

	public int price() {
		return 0;
	}

    public int priceModifier() { return 5; }

	public static Item virtual( Class<? extends Item> cl ) {
		try {

			Item item = cl.newInstance();
			item.quantity = 0;
			return item;

		} catch (Exception e) {
			return null;
		}
	}

	public Item random() {
		return this;
	}

	public String status() {
		return quantity != 1 ? Integer.toString( quantity ) : null;
	}

	public void updateQuickslot() {

		if (stackable) {
			Class<? extends Item> cl = getClass();
			if (QuickSlot.quickslot0 != null && QuickSlot.quickslot0.value == cl ||
					QuickSlot.quickslotX != null && QuickSlot.quickslotX.value == cl ||
                QuickSlot.quickslot1 != null && QuickSlot.quickslot1.value == cl ||
                QuickSlot.quickslot2 != null && QuickSlot.quickslot2.value == cl ||
                QuickSlot.quickslot3 != null && QuickSlot.quickslot3.value == cl
            ) {
				QuickSlot.refresh();
			}
		} else if (
            QuickSlot.quickslot0 != null && QuickSlot.quickslot0.value == this ||
					QuickSlot.quickslotX != null && QuickSlot.quickslotX.value == this ||
            QuickSlot.quickslot1 != null && QuickSlot.quickslot1.value == this ||
            QuickSlot.quickslot2 != null && QuickSlot.quickslot2.value == this ||
            QuickSlot.quickslot3 != null && QuickSlot.quickslot3.value == this
        ) {
			QuickSlot.refresh();
		}
	}

	private static final String QUANTITY		= "quantity";
	private static final String BONUS           = "level";
	private static final String KNOWN       	= "identified";
	private static final String DURABILITY		= "durability";
	private static final String STATE		    = "condition";
	private static final String CURSED		    = "cursed";
	private static final String BONES		    = "bones";

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( QUANTITY, quantity );
        bundle.put( KNOWN, known );
        bundle.put( BONUS, bonus );
       // bundle.put( STATE, state );
       // bundle.put( DURABILITY, durability );
		bundle.put( CURSED, cursed );
		bundle.put( BONES, bones );
		QuickSlot.save(bundle, this);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		quantity = bundle.getInt(QUANTITY);
        known = bundle.getInt(KNOWN);
        bonus = bundle.getInt(BONUS);
       // state = bundle.getInt(STATE);
       // durability = bundle.getInt(DURABILITY);
        cursed = bundle.getBoolean(CURSED);
		bones = bundle.getBoolean(BONES);

		QuickSlot.restore( bundle, this );
	}

	public void throwAt(final Hero user, int dst) {
//		if( Dungeon.visible[dst] ) {

//        if( curUser == null )
            curUser = user;

        curItem = this;


        if( curUser.buff( Dazed.class ) != null ) {
            dst += Level.NEIGHBOURS8[Random.Int( 8 )];
        }

        int cell = Ballistica.cast( user.pos, dst, false, true );

//        Char ch = Actor.findChar( cell );
//
//        if( ch != null ) {
//
//            if ( curUser.isCharmedBy( ch ) ) {
//                GLog.i(TXT_TARGET_CHARMED);
//                return;
//            }
//        }
		if(Dungeon.level.map[cell]== Terrain.DOOR_CLOSED)
			cell=Ballistica.trace[Ballistica.distance-2];
		final int cellFinal = cell;


//            final int cell = dst;
        user.sprite.cast(cellFinal);
        user.busy();

        Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

        Char enemy = Actor.findChar(cellFinal);
        QuickSlot.target(this, enemy);

        float delay = TIME_TO_THROW;

        if (this instanceof Weapon) {
            delay /= ((Weapon) this).speedFactor(user);
        }

        final float finalDelay = delay;

        ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
                reset(user.pos, cellFinal, this, new Callback() {
                    @Override
                    public void call() {
                        onThrow(cellFinal);
                        user.spendAndNext(finalDelay);
                }
            });
//        }

        Invisibility.dispel();


	}

	public static Hero curUser = null;
	public static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curItem.throwAt(curUser, target);
			}
		}
		@Override
		public String prompt() {
			return "选择扔出的方向";
		}
	};
}
