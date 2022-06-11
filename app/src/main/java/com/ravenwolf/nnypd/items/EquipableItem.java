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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.rings.Ring;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public abstract class EquipableItem extends Item {

    protected static final String TXT_EQUIP = "你装备上了 %s。";
    protected static final String TXT_UNEQUIP = "你卸下了%s。";
    protected static final String TXT_ISEQUIPPED	= "%s已装备";

    private static final String TXT_UNEQUIP_CURSED = "这个%s是诅咒的, 所以你无法取下它。";
    private static final String TXT_DETECT_CURSED = "这个%s是诅咒的, 但你在诅咒生效前及时取下了它。";

    protected static final String TXT_EQUIP_CURSED_HAND = "你的手不受控制地紧紧握住%s";
    protected static final String TXT_EQUIP_CURSED_BODY = "%s紧紧地勒住了你";
    protected static final String TXT_EQUIP_CURSED_RING = "%s突然紧缩，箍住了你的手指！";

    public static final String AC_EQUIP		= "装备";
    public static final String AC_UNEQUIP	= "取下";

    private static final String TXT_ITEM_IS_CURSED = "这个物品被诅咒了！";

    private static final String TXT_R_U_SURE =
            "你很清楚这个物品已经被诅咒了。一旦装备该物品，直到诅咒清除之前都无法被移除，你确认要装备它吗？";

    private static final String TXT_YES			= "是的，我知道我在做什么";
    private static final String TXT_NO			= "不，我改变主意了";

    @Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

    protected int drawId=-1;

    public int getDrawId(){
        return drawId;
    }

    public int getAdditionalDrawId(){
        return -1;
    }

    public int[][] getDrawData(int action){
            return null;
    }

    public int str(int bonus) {
        return 0;
    }

    public int str() {
        return str(bonus);
    }

    public int strShown( boolean identified ) {
        return identified ? str() : str(0) ;
    }

    public float penaltyFactor(Char ch, boolean identified ) {

        return 1.0f - 0.025f * currentPenalty(ch, strShown( identified ));
    }

    public int currentPenalty(Char ch, int str) {

        int delta = str - ch.STR();

        return GameMath.gate( 0,
                (delta < 0 ?  0 : delta * 4) + penaltyBase(), 20 );
        /*return GameMath.gate( 0,
                (delta < 0 ? Math.max(delta,-penaltyBase()/2) : delta * 2) + penaltyBase()
                        - ( this instanceof Weapon && ((Weapon)this).enchantment instanceof Ethereal && !isCursed() ? bonus : 0 )
                        - ( this instanceof Armour && ((Armour)this).glyph instanceof Featherfall && !isCursed() ? bonus : 0 )
                , 20 );*/

    }

    public int penaltyBase() {
        return 0;
    }

    public float speedFactor( Char ch ) {

        return ch.STR() < strShown( true ) ?  1.0f / ( 2.0f - penaltyFactor( ch, true ) ) : 1.0f;

    }
    public boolean incompatibleWith( EquipableItem item ) { return false ; }
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EQUIP )) {
            doEquipCarefully( hero );
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
		} else {
			super.execute( hero, action );
		}
	}
	
	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}

	@Override
	public void onThrow( int cell ) {

		if (isEquipped( curUser ) ) {

            if (quantity == 1 && !this.doUnequip( curUser, false, false )) {
				return;
			}
        }

        super.onThrow( cell );
	}

//    @Override
//    protected void onThrow( int cell ) {
//
//        if (isEquipped( curUser )) {
//            if (quantity == 1 && !this.doUnequip( curUser, false, false )) {
//                return;
//            }
//        }
//
//        super.onThrow(cell);
//    }

    protected static boolean detectCursed( Item item, Hero hero ) {
        return false;
/*
        float chance = 0.2f;
        chance += item.bonus * 0.1f ;

        if( Random.Float() < chance * hero.willpower() ) {

            item.identify(CURSED_KNOWN);
            GLog.w( TXT_DETECT_CURSED, item.name() );

            Sample.INSTANCE.play( Assets.SND_CURSED, 0.8f, 0.8f, 0.8f );
            Camera.main.shake( 1, 0.1f );

            return true;

        } else {

            return false;

        }*/
    }

	protected float time2equip( Hero hero ) {
		return 1.0f / speedFactor( hero );
	}

    public boolean disarmable() {
        return !isCursed();
    }
	
	public abstract boolean doEquip( Hero hero );

    public void doEquipCarefully( Hero hero ) {

        if( isCursed() && isCursedKnown() ) {

            final Hero heroFinal = hero;

            GameScene.show(
                new WndOptions( TXT_ITEM_IS_CURSED, TXT_R_U_SURE, TXT_YES, TXT_NO ) {

                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            doEquip( heroFinal );
                        }
                    }
                }
            );

        } else {

            doEquip( hero );

        }
    }

    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {

        if (isCursed()) {
//            int dmg = hero.HP / 5;
//
//            if( dmg > Random.Int( hero.HT / ( 4 + bonus ) ) ) {
//                hero.damage(dmg, null, null);
//                GLog.p(TXT_UNEQUIP_CURSED_SUCCESS, name() );
//            } else {
//                hero.damage(dmg, null, null);
//                GLog.w(TXT_UNEQUIP_CURSED_FAIL, name() );
                GLog.w(TXT_UNEQUIP_CURSED, name() );
                return false;
//            }
        } else if (single) {
            hero.spendAndNext( time2equip( hero ) );
            GLog.i(TXT_UNEQUIP, name());
        }
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.pos );
		}

		return true;
	}

    public final boolean doUnequip( Hero hero, boolean collect ) {
        return doUnequip( hero, collect, true );
    }

    public final void onEquip( Hero hero ) {

        if( isCursed() ) {

            if( this instanceof BodyArmor ) {
                GLog.n( TXT_EQUIP_CURSED_BODY, name() );
            } else if( this instanceof Ring ) {
                GLog.n( TXT_EQUIP_CURSED_RING, name() );
            } else {
                GLog.n( TXT_EQUIP_CURSED_HAND, name() );
            }

            hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

            Sample.INSTANCE.play( Assets.SND_CURSED );

        } else {

            GLog.i(TXT_EQUIP, name());

        }

        identify( CURSED_KNOWN );
    }

    @Override
    public int lootLevel() {
        // return ( lootChapter() - 1 ) * 6 + state * 2 + bonus * 2 + ( isEnchanted() ? 3 + bonus : 0 );
        return  (isCursed() ? lootChapter() : lootChapter() * 2 ) + bonus * 2 + ( isEnchanted() ? 1 + bonus : 0 )  ;
    }

    public boolean isEnchanted() { return false; }

    @Override
    public int price() {
        return price(25 + 25*lootChapter());
    }

    protected int price(int price){

        if ( isIdentified() ) {
            price += price * bonus / 3 ;
            if (isCursed())
                price -= price / 3;
        } else if( isCursedKnown() && !isCursed() ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if( isEnchanted() && isEnchantKnown() && !isCursed() ) {
            price += price / 3;
        }

        return price;

    }
}
