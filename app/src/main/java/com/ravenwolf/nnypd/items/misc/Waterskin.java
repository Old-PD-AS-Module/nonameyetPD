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
package com.ravenwolf.nnypd.items.misc;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Elemental;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.Splash;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Waterskin extends Item {

    public static final String AC_DRINK	= "饮用";
    public static final String AC_POUR = "倾倒";

    private static final float TIME_TO_DRINK = 1f;

    private static final String TXT_VALUE	= "%+dHP";
    private static final String TXT_STATUS	= "%d/%d";

    private static final String TXT_FULL		= "你的水袋已经满了！";
    private static final String TXT_EMPTY		= "你的水袋已经空了！";

    private static final String TXT_POUR_SELF	= "你把水袋中的水倾倒在自己身上。";
    private static final String TXT_POUR_TILE	= "你把水袋中的水倾倒在一格地面上。";

    private static final String TXT_HEALTH_FULL = "你的生命值已满。";

    private static final String TXT_HEALTH_HALF = "你的状态尚好！";

    private static final String TXT_R_U_SURE =
            "饮用水袋中的水只能够恢复你一部分的缺失生命值，所以一般情况下生命值较低时使用更加合适。你确定现在就要喝水吗？";

    private static final String TXT_YES			= "是的，我知道自己在做什么";
    private static final String TXT_NO			= "不，我改变主意了";

	{
        name = "水袋";
		image = ItemSpriteSheet.WATERSKIN;
		
		visible = false;
		unique = true;
	}
	
	private int value = 1;
	private int limit = 1;

	private static final String VALUE = "power";
	private static final String LIMIT = "limit";

    @Override
    public String quickAction() {
        return AC_DRINK;
    }
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(VALUE, value);
		bundle.put(LIMIT, limit);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		value = bundle.getInt(VALUE);
        limit = bundle.getInt(LIMIT);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

        actions.add( AC_DRINK );
        actions.add( AC_POUR );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {
        if (action.equals( AC_DRINK )) {

            if( value > 0 ){

                if( hero.HT > hero.HP ){

                    if ( hero.HT < hero.HP * 2 ) {

                        GameScene.show(
                            new WndOptions( TXT_HEALTH_HALF, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        drink( hero );
                                    }
                                }
                            }
                        );

                    } else {
                        drink( hero );
                    }

                } else {
                    GLog.w( TXT_HEALTH_FULL);
                }
            } else {
                GLog.w( TXT_EMPTY );
            }

        } else if( action.equals( AC_POUR ) ){

            if( value > 0 ){

                curUser = hero;
                curItem = this;

                GameScene.selectCell( pourer );

            } else {
                GLog.w( TXT_EMPTY );
            }

        } else {

			super.execute(hero, action);
			
		}
	}

    private void drink( Hero hero ) {

         int healthLost = hero.HT - hero.HP;

        hero.heal( Random.IntRange( healthLost / 2, healthLost * 2 / 3 ) );

        hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), Math.max( 1, (int) Math.sqrt( healthLost/3 ) ) );

        this.value--;

        hero.spend( TIME_TO_DRINK );
        hero.busy();

        Sample.INSTANCE.play( Assets.SND_DRINK );
        hero.sprite.operate( hero.pos );

        updateQuickslot();

    }
	
	public boolean isFull() {
		return value >= limit;
	}
	
	public void collectDew( Dewdrop dew ) {

		value += dew.quantity;
		if (value >= limit) {
			value = limit;
			GLog.p( TXT_FULL );
		}

		updateQuickslot();
	}

    public int space() {
        return limit - value;
    }

    public Waterskin setLimit( int quantity ) {
        limit = quantity;
        return this;
    }

    public Waterskin fill( int quantity ) {
        value = Math.min( limit, value + quantity );
        updateQuickslot();

        return this;
    }
	
	public Waterskin fill() {
		value = limit;
		updateQuickslot();

        return this;
	}

    public Waterskin improve( Waterskin waterskin ) {

        limit += waterskin.limit;
        value += waterskin.value;

//        power = limit;

        updateQuickslot();

        return this;
    }

    @Override
    public boolean doPickUp( Hero hero ) {

        Waterskin vial = hero.belongings.getItem( Waterskin.class );

        if (vial != null) {

            vial.improve( this );
            GameScene.pickUp(this);

//            GLog.p(TXT_NEW_SKIN);

            Sample.INSTANCE.play(Assets.SND_ITEM);

            return true;

        }

        return super.doPickUp(hero);
    }


    @Override
    public int price() {
        return 30 * quantity;
    }

	@Override
	public String status() {
		return Utils.format( TXT_STATUS, value, limit );
	}
	
	@Override
	public String info() {
        return
                "这是一件用来存储水源的储水容器。大口喝下其中的净水能够恢复使用者失去的部分生命值。" +
                        "井水可以灌到水袋之中，每额外获取一次水袋都会进一步提高你可携带的净水上限。";
    }
	
	@Override
	public String toString() {
		return super.toString() + " (" + status() +  ")" ;
	}


    protected static CellSelector.Listener pourer = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                Ballistica.cast( curUser.pos, target, false, true );

                int cell = Ballistica.trace[ 0 ];

                if( Ballistica.distance > 1 ){
                    cell = Ballistica.trace[ 1 ];
                }

                Blob blob = Dungeon.level.blobs.get( Fire.class );

                if (blob != null) {

                    int cur[] = blob.cur;

                    if ( cur[ cell ] > 0) {
                        blob.volume -= cur[ cell ];
                        cur[ cell ] = 0;
                    }
                }

                boolean mapUpdated = false;
                int oldTile = Dungeon.level.map[cell];

                if (oldTile == Terrain.GRASS) {

                    Level.set(cell, Terrain.HIGH_GRASS);
                    mapUpdated = true;

                } else if (oldTile == Terrain.HIGH_GRASS ) {

                    Dungeon.level.drop( Generator.random(Generator.Category.HERB), cell, true).type = Heap.Type.HEAP;

                }

                if ( mapUpdated ) {

                    GameScene.discoverTile( cell, oldTile );

                    GameScene.updateMap();
                    Dungeon.observe();

                }

                Char ch = Actor.findChar( cell );

                if (ch != null) {
                    if( ch instanceof Elemental) {
                        ch.damage( Random.IntRange( 1, (int)Math.sqrt( ch.HT / 2 + 1 ) ), this, null );
                    } else {
                        Buff.detach(ch, Burning.class);
                        Buff.detach(ch, Corrosion.class);
                    }
                }

                ((Waterskin)curItem).value--;
                Invisibility.dispel();

                if( curUser.pos == cell ) {
                    GLog.i( TXT_POUR_SELF );
                } else {
                    GLog.i( TXT_POUR_TILE );
                }

                Splash.at( cell, 0xFFFFFF, 5 );
                Sample.INSTANCE.play(Assets.SND_WATER, 0.6f, 0.6f, 1.5f);

                curUser.spend( TIME_TO_DRINK );
                curUser.sprite.operate(cell);
                curUser.busy();


            }
        }
        @Override
        public String prompt() {
            return "选择周围的地面倾倒净水";
        }
    };
}