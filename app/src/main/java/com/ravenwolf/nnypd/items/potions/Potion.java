
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
 */package com.ravenwolf.nnypd.items.potions;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.ItemStatusHandler;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Splash;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Potion extends Item {

	public static final String AC_DRINK	= "饮用";

	private static final String TXT_HARMFUL		= "有害药剂！";
	private static final String TXT_BENEFICIAL	= "增益药剂";
	private static final String TXT_YES			= "没错，我知道自己在做什么";
	private static final String TXT_NO			= "不，我改变主意了";
	private static final String TXT_R_U_SURE_DRINK =
			"你确定要喝掉它吗？通常来讲这瓶药剂应当被扔向敌人而非直接饮用。";
	private static final String TXT_R_U_SURE_THROW =
			"你确定要扔出它吗？通常来说这瓶药剂应当被直接饮用而不是扔出去。";
	
	private static final float TIME_TO_DRINK = 1f;
	
	private static final Class<?>[] potions = {
		PotionOfMending.class,
		PotionOfWisdom.class,
		PotionOfCorrosiveGas.class,
		PotionOfLiquidFlame.class,
		PotionOfStrength.class,
		PotionOfSparklingDust.class,
		PotionOfLevitation.class,
		PotionOfMindVision.class, 
		PotionOfBlessing.class,
		PotionOfInvisibility.class,
		PotionOfOvergrowth.class,
		PotionOfFrigidVapours.class,
	/*		PotionOfConfusionGas.class,
			PotionOfMiasma.class,
			PotionOfRage.class,
			PotionOfBlastwave.class*/
	};
/*	private static final String[] colors = {
		"turquoise", "crimson", "azure", "jade", "golden", "magenta", "charcoal",
			"ivory", "amber", "bistre", "indigo", "silver", *//*"pink", "emerald", "lime", "bordeaux"*//*};*/
	private static final String[] colors = {
			"青绿", "猩红", "湛蓝", "翠绿", "金黄", "品红", "炭黑",
			"乳白", "琥珀", "深褐", "靛紫", "银灰", /*"粉红", "翡翠", "橙绿", "lala"*/};
	private static final Integer[] images = {
		ItemSpriteSheet.POTION_TURQUOISE, 
		ItemSpriteSheet.POTION_CRIMSON, 
		ItemSpriteSheet.POTION_AZURE, 
		ItemSpriteSheet.POTION_JADE,
		ItemSpriteSheet.POTION_GOLDEN, 
		ItemSpriteSheet.POTION_MAGENTA,
		ItemSpriteSheet.POTION_CHARCOAL, 
		ItemSpriteSheet.POTION_IVORY, 
		ItemSpriteSheet.POTION_AMBER, 
		ItemSpriteSheet.POTION_BISTRE,
		ItemSpriteSheet.POTION_INDIGO, 
		ItemSpriteSheet.POTION_SILVER,
		/*ItemSpriteSheet.POTION_PINK,
		ItemSpriteSheet.POTION_EMERALD,
		ItemSpriteSheet.POTION_LIME,
		ItemSpriteSheet.POTION_EMPTY,*/
	};

	
	private static ItemStatusHandler<Potion> handler;

	private String color;
	public boolean dud = false;

    protected boolean harmful;

	{
		stackable = true;
        harmful = false;
        shortName = "??";

    }
	
	@SuppressWarnings("unchecked")
	public static void initColors() {
		handler = new ItemStatusHandler<Potion>( (Class<? extends Potion>[])potions, colors, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Potion>( (Class<? extends Potion>[])potions, colors, images, bundle );
	}
	
	public Potion() {
		super();
//        if( !( this instanceof EmptyBottle ) ){
			image = handler.image( this );
			color = handler.label( this );
//		}
	}

	@Override
	public int image() {
		if ( dud ){
			if (handler.isKnown( this ))
				return super.image();
			else
				return ItemSpriteSheet.POTION_UNKNOWN;
		}else
			return super.image();
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero );
		actions.add( AC_DRINK );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {
		if (action.equals( AC_DRINK )) {
			
			if (isTypeKnown() && harmful) {
				
                GameScene.show(
                    new WndOptions( TXT_HARMFUL, TXT_R_U_SURE_DRINK, TXT_YES, TXT_NO ) {
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
			
			super.execute(hero, action);
			
		}
	}
	
	@Override
	public void doThrow( final Hero hero ) {

		if (isTypeKnown() && !harmful) {
		
			GameScene.show( 
				new WndOptions( TXT_BENEFICIAL, TXT_R_U_SURE_THROW, TXT_YES, TXT_NO ) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Potion.super.doThrow( hero );
						}
					}
                }
			);
			
		} else {
			super.doThrow(hero);
		}
	}
	
	protected void drink( Hero hero ) {
		
		detach( hero.belongings.backpack );
		
		hero.spend( TIME_TO_DRINK );
		hero.busy();
        apply( hero );
		
		Sample.INSTANCE.play( Assets.SND_DRINK );
		
		hero.sprite.operate( hero.pos );
	}
	
	@Override
	protected void onThrow( int cell ) {
        if (Level.chasm[cell]) {

            super.onThrow(cell);

        } else {

            detach(curUser.belongings.backpack);
			
			shatter( cell );
			
		}
	}
	
	protected void apply( Hero hero ) {
		shatter( hero.pos );
	}
	
	public void shatter( int cell ) {
		if (Dungeon.visible[cell]) {
			GLog.i( "药瓶碎裂开来，" + color() + "的液体从中溅出" );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			splash( cell );
		}
	}

    @Override
	public boolean isTypeKnown() {
		return handler.isKnown( this ) || dud;
	}
	
	public void setKnown() {
		if ( !isTypeKnown() && !dud ) {
			handler.know( this );
		}
		
		Badges.validateAllPotionsIdentified();
	}
	
	@Override
	public Item identify() {
		setKnown();
		return this;
	}

    @Override
    public String quickAction() {

        if( !isTypeKnown() )
            return null;

        return harmful ? AC_THROW : AC_DRINK;
    }
	
	protected String color() {
		return color;
	}
	
	@Override
	public String name()  {
		return isTypeKnown() ? name : color + "药剂";
	}
	
	@Override
	public String info() {
		return isTypeKnown() ?
				desc() :
				"瓶子里装着些不断打旋的"+color+"色液体。谁知道饮用或投掷它时会有什么效果呢？";
	}
	
	@Override
	public boolean isIdentified() {
		return isTypeKnown();
	}
	
	@Override
	public boolean isUpgradeable() {
		return false;
	}
	
	public static HashSet<Class<? extends Potion>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Potion>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == potions.length;
	}

    public static int alchemySkill() {

        int result = handler.known().size();

        if( handler.isKnown( PotionOfStrength.class ) )
            result--;

        if( handler.isKnown( PotionOfWisdom.class ) )
            result--;

        return result;
     }
	
	protected void splash( int cell ) {
		Splash.at( cell, ItemSprite.pick( image,  8, 10 ), 10 );
	}

	@Override
	public int price() {
		return 25 * quantity;
	}
}
