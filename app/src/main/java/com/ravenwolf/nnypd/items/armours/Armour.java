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
package com.ravenwolf.nnypd.items.armours;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.glyphs.AcidWard;
import com.ravenwolf.nnypd.items.armours.glyphs.Deflection;
import com.ravenwolf.nnypd.items.armours.glyphs.FlameWard;
import com.ravenwolf.nnypd.items.armours.glyphs.FrostWard;
import com.ravenwolf.nnypd.items.armours.glyphs.Petrification;
import com.ravenwolf.nnypd.items.armours.glyphs.Protection;
import com.ravenwolf.nnypd.items.armours.glyphs.Radiance;
import com.ravenwolf.nnypd.items.armours.glyphs.Repulsion;
import com.ravenwolf.nnypd.items.armours.glyphs.Retribution;
import com.ravenwolf.nnypd.items.armours.glyphs.ShadowWard;
import com.ravenwolf.nnypd.items.armours.glyphs.StormWard;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.rings.RingOfKnowledge;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Armour extends EquipableItem {

	private static final int HITS_TO_KNOW	= 15;

/*	private static final String TXT_EQUIP_CURSED	= "your %s constricts around you painfully";

	private static final String TXT_IDENTIFY	= "you are now familiar enough with your %s to identify it. It is %s.";*/
    private static final String TXT_EQUIP_CURSED	= "这件%s紧紧地勒住了你。";

    private static final String TXT_IDENTIFY	= "你对身上的%s已经足够了解，它是%s。";

	public int tier;
	public int appearance;

	protected int hitsToKnow = Random.IntRange(HITS_TO_KNOW, HITS_TO_KNOW * 2);

	public Glyph glyph;

	public Armour(int tier) {

		this.tier = tier;

	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(UNFAMILIRIARITY, hitsToKnow);
		bundle.put(GLYPH, glyph);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		inscribe((Glyph) bundle.get(GLYPH));
	}

//	@Override
//	public ArrayList<String> actions( Hero hero ) {
//		ArrayList<String> actions = super.actions( hero );
//		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
//		return actions;
//	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isEnchantKnown() {
        return known >= ENCHANT_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.armor == this;
    }


    public int dr( int bonus ) {
        return 0;
    }

    public int dr() {
        return dr(bonus);
    }

    @Override
    public int lootChapter() {
        return tier;
    }


    @Override
    public int priceModifier() { return 4; }

    public String descType() {
        return "";
    }

	public int proc( Char attacker, Char defender, int damage ) {

		if (glyph != null) {
			glyph.proc( this, attacker, defender, damage );
		}

        updHitsToKnow(defender);

		return damage;
	}

	public void updHitsToKnow(Char owner){
        if (!isIdentified() && owner instanceof Hero) {

            float effect = owner.ringBuffs(RingOfKnowledge.Knowledge.class);

            hitsToKnow -= (int) effect;
            hitsToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

            if (hitsToKnow <= 0) {
                identify();
                GLog.w( TXT_IDENTIFY, name(), toString() );
            }
        }
    }

	@Override
	public String name() {
		return ( glyph != null && isEnchantKnown() ? glyph.name( this ) : super.name() );
	}

    public String simpleName() {
        return name;
    }

    @Override
    public Item uncurse() {

        if(isCursed()) {
            inscribe(null);
        }

        return super.uncurse();
    }

//	@Override
//	public Item random() {

//        bonus = Random.NormalIntRange( -3, +3 );
//
//        if (Random.Int( 7 + bonus ) == 0) {
//			inscribe();
//		}
//
//        randomize_state();

//		return this;
//	}

    public boolean isEnchanted() { return glyph != null; }


	public Armour inscribe( Glyph glyph ) {

        this.glyph = glyph;

		return this;

	}

	public Armour inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random();
		while (gl.getClass() == oldGlyphClass) {
			gl = Armour.Glyph.random();
		}
		
		return inscribe( gl );
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null && isEnchantKnown() ? glyph.glowing() : null;
	}
	
	public static abstract class Glyph implements Bundlable {
		
		private static final Class<?>[] glyphs = new Class<?>[]{ 
			FlameWard.class, FrostWard.class, StormWard.class, AcidWard.class,
            Protection.class,Petrification.class, Retribution.class,
			Deflection.class, Radiance.class, Repulsion.class, ShadowWard.class};

		private static final float[] chances= new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

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

        protected abstract String name_p();
        protected abstract String name_n();
        protected abstract String desc_p();
        protected abstract String desc_n();


        public abstract boolean proc_n( Char attacker, Char defender, int damage, boolean isShield );
        public abstract boolean proc_p( Char attacker, Char defender, int damage, boolean isShield );


        public Class<? extends Element> resistance() {
            return null;
        }

        public static boolean procced( Char defender, int bonus ) {


            return Random.Float() < (0.1f + 0.05f * ( 1 + bonus ))
                    * ( defender.ringBuffs( RingOfSorcery.Sorcery.class ) );

        }

        public boolean  proc( Armour armor, Char attacker, Char defender, int damage ) {

            boolean isShield=armor instanceof Shield;

            boolean proc = procced( defender, armor.bonus );
            //shields deal two rolls to proc enchantments
            if (!proc && isShield)
                proc = procced( defender, armor.bonus );

            boolean result = proc
                && ( !armor.isCursed() || Random.Float() < 0.4f * attacker.willpower()
                ?  proc_p(attacker, defender, damage, isShield)
                : proc_n(attacker, defender, damage, isShield) );


            if( result ) {
                armor.identify( ENCHANT_KNOWN );
            }

            return result;
        }

        public String name( Armour armor ) {
            return String.format( !armor.isCursed() ? name_p() : name_n(), armor.name );
        }

        public String desc( Armour armor ) {
            //return !armor.isCursed() ? desc_p() : desc_n();
            return desc_p() + (armor.isCursed() ? "。由于这个盔甲是被诅咒的，所以它经常会对穿戴者造成相反的效果" : "");
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
		
		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner instanceof Hero) {
				
				((Hero)owner).killerGlyph = this;
				Badges.validateDeathFromGlyph();
				return true;
				
			} else {
				return false;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
	}
}
