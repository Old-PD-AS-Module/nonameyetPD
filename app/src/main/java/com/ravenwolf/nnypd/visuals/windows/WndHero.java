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
package com.ravenwolf.nnypd.visuals.windows;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.armours.body.HuntressArmor;
import com.ravenwolf.nnypd.items.armours.body.MageArmor;
import com.ravenwolf.nnypd.items.armours.body.RogueArmor;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.rings.RingOfAccuracy;
import com.ravenwolf.nnypd.items.rings.RingOfAwareness;
import com.ravenwolf.nnypd.items.rings.RingOfConcentration;
import com.ravenwolf.nnypd.items.rings.RingOfEvasion;
import com.ravenwolf.nnypd.items.rings.RingOfShadows;
import com.ravenwolf.nnypd.items.rings.RingOfSorcery;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.ravenwolf.nnypd.visuals.ui.ScrollPane;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.RenderedText;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

public class WndHero extends WndTabbed {

	private static final String TXT_STATS	= "属性";
	private static final String TXT_BUFFS	= "状态";

	private static final String TXT_EXP		= "经验";
	private static final String TXT_STR		= "力量";
	private static final String TXT_HEALTH	= "生命";
	private static final String TXT_SATIETY	= "饥饿度";
	private static final String TXT_STEALTH	= "潜行";
	private static final String TXT_WILLPWR = "意志";
	private static final String TXT_AWARNSS	= "感知";
	private static final String TXT_OFFENSE = "精准";
	private static final String TXT_DEFENSE	= "灵巧";
	private static final String TXT_WNDSKLL = "魔能";
	private static final String TXT_GOLD	= "金币收集数";
	private static final String TXT_DEPTH	= "最高层数";
	
	private static final int WIDTH		= 100;
    private static final int HEIGHT	    = 100;
	private static final int TAB_WIDTH	= 40;
	
	private StatsTab stats;
	private BuffsTab buffs;
	
	private static SmartTexture icons;
	private static TextureFilm film;
	
	public WndHero() {
		
		super();
		
		icons = TextureCache.get( Assets.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );

        resize( WIDTH, HEIGHT );
		
		stats = new StatsTab();
		add( stats );
		
		buffs = new BuffsTab( new Component() );
        add( buffs );
        buffs.setRect( 0, 0, WIDTH, stats.height() - 4 );

		add( new LabeledTab( TXT_STATS ) {
			protected void select( boolean value ) {
				super.select( value );
				stats.visible = stats.active = selected;
			}
        } );

		add( new LabeledTab( TXT_BUFFS ) {
			protected void select( boolean value ) {
				super.select( value );
				buffs.visible = buffs.active = selected;
			}
        } );

		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
		}
		
		resize( WIDTH, (int)stats.height() );
		
		select( 0 );
	}
	
	private class StatsTab extends Group {

		private static final String TXT_TITLE		= "%d级%s";
//		private static final String TXT_CATALOGUS	= "Knowledge";
//		private static final String TXT_JOURNAL		= "Journal";
		
		private static final int GAP = 3;
		
		private float pos;
		
		public StatsTab() {
			
			Hero hero = Dungeon.hero; 

			/*BitmapText title = PixelScene.createText( 
				Utils.format( TXT_TITLE, hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ), 9 );*/
			RenderedText title = PixelScene.renderText(
				Utils.format( TXT_TITLE, hero.lvl, hero.className() ), 9 );
			title.hardlight( TITLE_COLOR );
			//title.measure();
			PixelScene.align(title);
			add(title);
			
//			RedButton btnCatalogus = new RedButton( TXT_CATALOGUS ) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndCatalogus() );
//				}
//			};
//			btnCatalogus.setRect( 0, title.y + title.height(), btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
//			add(btnCatalogus);
//
//			RedButton btnJournal = new RedButton( TXT_JOURNAL ) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndJournal() );
//				}
//			};
//			btnJournal.setRect(
//                    btnCatalogus.right() + 1, btnCatalogus.top(),
//                    btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2);
//			add(btnJournal);
			
			pos = title.y + title.height() + GAP;

            statSlot( TXT_EXP, hero.exp + "/" + hero.maxExp() );
            statSlot( TXT_HEALTH, hero.HP + "/" + hero.HT );

            pos += GAP;

            statSlot( TXT_STR, hero.STR() + " (" + hero.STR + ")" );

            if( hero.isDualWielding() ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1 != null && !hero.belongings.weap1.isIdentified() ||
                        hero.belongings.weap2 != null && !hero.belongings.weap2.isIdentified() ?
                    "??" : ( ( hero.baseAcc( hero.belongings.weap1, false ) + hero.baseAcc( (Weapon)hero.belongings.weap2, false ) ) / 2 ) ) + " (" + hero.attackSkill + ")" );

            } else if ( hero.belongings.weap1 != null ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1.incompatibleWith( hero.belongings.weap2 ) && !hero.belongings.weap2.isIdentified() ||
                        !hero.belongings.weap1.isIdentified() ?
                    "??" : hero.baseAcc( hero.belongings.weap1, false ) ) + " (" + hero.attackSkill + ")" );

            } else if ( hero.belongings.weap2 instanceof Weapon ) {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap2.incompatibleWith( hero.belongings.weap1 ) && !hero.belongings.weap1.isIdentified() ||
                        !hero.belongings.weap2.isIdentified() ?
                    "??" : hero.baseAcc( (Weapon)hero.belongings.weap2, false ) ) + " (" + hero.attackSkill + ")" );

            } else {

                statSlot( TXT_OFFENSE, (
                        hero.belongings.ring1 instanceof RingOfAccuracy && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAccuracy && !hero.belongings.ring2.isIdentified() ?
                    "??" : hero.baseAcc( null, false ) ) + " (" + hero.attackSkill + ")" );

            }

            statSlot(TXT_DEFENSE, (
                        hero.belongings.ring1 instanceof RingOfEvasion && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfEvasion && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.armor != null && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.weap2 instanceof Shield && ( !hero.belongings.weap2.isIdentified() ||
                        hero.belongings.weap2.incompatibleWith( hero.belongings.weap1 ) && !hero.belongings.weap1.isIdentified() )
                    ? "??" : hero.baseDex(false) ) + " (" + hero.defenseSkill + ")");

			statSlot(TXT_WNDSKLL, (
                        hero.belongings.ring1 instanceof RingOfSorcery && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfSorcery && !hero.belongings.ring2.isIdentified() ?
                    "??" : hero.magicSkill() ) + " (" + hero.magicSkill + ")" );

//			statSlot( TXT_GOLD, Statistics.goldCollected );
//			statSlot( TXT_DEPTH, Statistics.deepestFloor );
			
			pos += GAP;

            statSlot(TXT_WILLPWR, (
                        hero.belongings.armor instanceof MageArmor && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.ring1 instanceof RingOfConcentration && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfConcentration && !hero.belongings.ring2.isIdentified() ?
                    "??" : (int)( hero.willpower() * 100 ) ) + "%" );

            statSlot( TXT_AWARNSS, (
                        hero.belongings.armor instanceof HuntressArmor && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.ring1 instanceof RingOfAwareness && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfAwareness && !hero.belongings.ring2.isIdentified() ?
                    "??" : (int)( hero.awareness() * 100 ) ) + "%" );

            statSlot( TXT_STEALTH, (
                        hero.belongings.armor instanceof RogueArmor && !hero.belongings.armor.isIdentified() ||
                        hero.belongings.ring1 instanceof RingOfShadows && !hero.belongings.ring1.isIdentified() ||
                        hero.belongings.ring2 instanceof RingOfShadows && !hero.belongings.ring2.isIdentified() ||
                        hero.belongings.weap1 != null && !hero.belongings.weap1.isIdentified() ||
                        hero.belongings.weap2 != null && !hero.belongings.weap2.isIdentified() && !(hero.belongings.weap2 instanceof Wand) ||
                        hero.belongings.armor != null && !hero.belongings.armor.isIdentified() ?
                    "??" : (int)( hero.baseStealth(false) * 100 ) ) + "%" );

            statSlot( TXT_SATIETY, ( hero.isAlive() ?
                    (int)Math.ceil( hero.buff( Satiety.class ).energy() / Satiety.MAXIMUM * 100.0f ) : 0 ) + "%" );

            pos += GAP;
		}
		
		private void statSlot( String label, String value ) {
			
			//BitmapText txt = PixelScene.createText( label, 6 );
			RenderedText txt = PixelScene.renderText( label, 6 );
			txt.y = pos;
			add( txt );
			
			//txt = PixelScene.createText( value, 6 );
			//txt.measure();
			txt = PixelScene.renderText( value, 6 );
			PixelScene.align(txt);
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			add( txt );
			
			pos += GAP + txt.baseLine();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}
	
	private class BuffsTab extends ScrollPane {
		
		private static final int GAP_X = 2;
		private static final int GAP_Y = 4;

		private float pos = 0;
		
		public BuffsTab( Component content ) {

            super( content );

            pos = GAP_Y;

            for (Buff buff : Dungeon.hero.buffs()) {
                buffSlot( buff );
            }

            content.setSize( WIDTH, pos );
            content.setPos( 0, 0 );

        }
		
		private void buffSlot( Buff buff ) {

			int index = buff.icon();

			if (index != BuffIndicator.NONE) {

                Image icon = new Image( icons );
                icon.frame( film.get( index ) );
                icon.y = pos;
				buff.tintIcon(icon);
                content.add( icon );

                String title = buff instanceof BuffActive ? String.format( "%s (%s)", buff.toString(), buff.status() ) : buff.toString();

                //BitmapText label = PixelScene.createText( title, 8 );
				RenderedText label = PixelScene.renderText( title, 8 );
                label.x = icon.width + GAP_X;
                label.y = pos + (int)(icon.height - label.baseLine()) / 2;
                content.add( label );

                Highlighter hl = new Highlighter( buff.description() );

                //BitmapTextMultiline basicDesc = PixelScene.createMultiline( hl.text, 6 );
				RenderedTextMultiline basicDesc = PixelScene.renderMultiline( buff.description(), 6 );
                //basicDesc.x = GAP_X;
                //basicDesc.y = pos + icon.height + GAP_X;
				float x = GAP_X;
                float y = pos + icon.height + GAP_X;

                //basicDesc.maxWidth = WIDTH - GAP_Y;
                //basicDesc.measure();
				basicDesc.maxWidth(WIDTH - GAP_Y);
                PixelScene.align(basicDesc);
                basicDesc.setPos(x,y);

                content.add( basicDesc );

                /*if (hl.isHighlighted()) {

                    basicDesc.mask = hl.inverted();

                    BitmapTextMultiline colorDesc = PixelScene.createMultiline( hl.text, 6 );

                    colorDesc.x = basicDesc.x;
                    colorDesc.y = basicDesc.y;

                    colorDesc.maxWidth = basicDesc.maxWidth;
                    colorDesc.measure();

                    colorDesc.mask = hl.mask;
                    colorDesc.hardlight( TITLE_COLOR );

                    content.add( colorDesc );

                }*/

				pos += GAP_Y + icon.height + basicDesc.height();

			}
		}
		
		public float height() {
			return pos;
		}


	}
}
