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
package com.ravenwolf.nnypd.visuals.ui;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.misc.Waterskin;
import com.ravenwolf.nnypd.items.potions.Potion;
import com.ravenwolf.nnypd.items.scrolls.Scroll;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeaponFlintlock;
import com.ravenwolf.nnypd.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int WARNING		= 0xFF8800;

	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;
	
	protected ItemSprite icon;
	protected BitmapText topLeft;
	protected BitmapText topRight;
	protected BitmapText bottomRight;
	protected BitmapText bottomLeft;
	protected boolean    iconVisible=true;
	protected Image      bottomRightIcon;

	public  Item item;

	private static final String TXT_STRENGTH	= ":%d";
	private static final String TXT_TYPICAL_STR	= "%d?";
	
	private static final String TXT_LEVEL	= "%+d";
	private static final String TXT_NOTHING = "";//"-";
	
	// Special "virtual items"
	public static final Item CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CHEST; }
    };
	public static final Item LOCKED_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.LOCKED_CHEST; }
    };
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; }
    };
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; }
    };
	
	public ItemSlot() {
		super();
	}
	
	public ItemSlot( Item item ) {
		this();
		item(item);
	}

	public void click(){
		this.onClick();
	}

	public void longClick(){
		this.onLongClick();
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		icon = new ItemSprite();
		add( icon );
		
		topLeft = new BitmapText( PixelScene.font1x );
		add( topLeft );
		
		topRight = new BitmapText( PixelScene.font1x );
		add( topRight );
		
		bottomRight = new BitmapText( PixelScene.font1x );
		add(bottomRight);

        bottomLeft = new BitmapText( PixelScene.font1x );
        add(bottomLeft);
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width) / 2;
		icon.y = y + (height - icon.height) / 2;
		
		if (topLeft != null) {
			topLeft.x = x;
			topLeft.y = y;
		}
		
		if (topRight != null) {
			topRight.x = x + (width - topRight.width());
			topRight.y = y;
		}
		
		if (bottomRight != null) {
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height());
		}

        if (bottomLeft != null) {
            bottomLeft.x = x;
            bottomLeft.y = y + (height - bottomLeft.height());
        }
		if (bottomRightIcon != null) {
			bottomRightIcon.x = x + (width - bottomRightIcon.width()) -1;
			bottomRightIcon.y = y + (height - bottomRightIcon.height())-1;
			//PixelScene.align(bottomRightIcon);
		}
	}
	
	public void item( Item item ) {
		this.item=item;

		if (bottomRightIcon != null){
			remove(bottomRightIcon);
			bottomRightIcon = null;
		}

		if (item == null) {
			
			active = false;
			icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = bottomLeft.visible = false;
			
		} else {
			
			active = true;
			icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = bottomLeft.visible = true;
			
			icon.view( item.image(), item.glowing() );
			
			topLeft.text( item.status() );
            bottomLeft.text( "" );

            if ( item instanceof RangedWeapon ) {

                topLeft.visible = false;
                bottomLeft.visible = false;

				if ( item instanceof RangedWeaponFlintlock ) {

                    Item powder = Dungeon.hero.belongings.getItem( Explosives.Gunpowder.class );

                    bottomLeft.text( powder != null ? Integer.toString( powder.quantity ) : "" );
                    bottomLeft.measure();
                }
            }

            if ( item instanceof RangedWeaponFlintlock && !((RangedWeaponFlintlock) item).loaded ) {

                bottomLeft.hardlight(WARNING);
                topLeft.hardlight(WARNING);

            } else {

                bottomLeft.resetColorAlpha();
                topLeft.resetColorAlpha();

            }

            topLeft.measure();

			if (item instanceof Weapon && !(item instanceof ThrowingWeaponAmmo) || item instanceof Armour) {

                EquipableItem equip = (EquipableItem)item;
				
				if ( equip.isIdentified() ) {
					
					int str = equip.strShown( true );
					topRight.text( Utils.format(
                            equip == Dungeon.hero.belongings.weap1 && equip.incompatibleWith(Dungeon.hero.belongings.weap2) && !Dungeon.hero.belongings.weap2.isIdentified() ||
                            equip == Dungeon.hero.belongings.weap2 && equip.incompatibleWith(Dungeon.hero.belongings.weap1) && !Dungeon.hero.belongings.weap1.isIdentified() ?
                                    TXT_TYPICAL_STR : TXT_STRENGTH, str ) );

					if (str > Dungeon.hero.STR()) {
						topRight.hardlight( DEGRADED );
					} else {
						topRight.resetColorAlpha();
					}
					
				} else {
					
					topRight.text( Utils.format(TXT_TYPICAL_STR, equip.strShown(false)) );
					topRight.hardlight( WARNING );
					
				}
				topRight.measure();
				
			}
			 else if (item instanceof Scroll || item instanceof Potion) {
				bottomRight.text( null );

				Integer iconInt=item.BottomIcon();

				if (iconInt != -1 && iconVisible) {
					bottomRightIcon = new Image("consumable_icons.png");
					int left = iconInt*7;
					int top = item instanceof Potion ? 0 : 8;
					bottomRightIcon.frame(left, top, 7, 8);
					add(bottomRightIcon);
				}

			}

			else if( item.shortName != null && item.isTypeKnown() ) {

                topRight.text( Utils.format( "%s", item.shortName ) );
                topRight.resetColorAlpha();
                topRight.measure();

            } else if( item instanceof Waterskin ) {

                topLeft.text( "" );

                bottomLeft.text( item.status() );
                bottomLeft.measure();

            } else if( item instanceof OilLantern ) {

                int flasks = ((OilLantern)item).getFlasks();
                topLeft.text( flasks > 0 ? Utils.format( "%d", flasks ) : "" );

                bottomLeft.text( item.status() );
                bottomLeft.measure();

            } else {

                topRight.text( null );

            }
	
			int level = item.isIdentified() ? item.bonus : 0;
			if (level != 0 /*|| (item.isCursed() && item.isCursedKnown())*/) {
				bottomRight.text( item.isIdentified() ? Utils.format( TXT_LEVEL, level ) : TXT_NOTHING);
				bottomRight.measure();
				bottomRight.hardlight( level > 0 ? UPGRADED : DEGRADED );
			} else {
				bottomRight.text( null );
            }

            layout();
		}
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;

		icon.alpha( alpha );
		topLeft.alpha( alpha );
		topRight.alpha( alpha );
		bottomRight.alpha( alpha );
		bottomLeft.alpha( alpha );
		if (bottomRightIcon != null) bottomRightIcon.alpha( alpha );
	}
	
	public void showParams( boolean value ) {
		if (value) {
			add( topRight );
			add( bottomRight );
			add( bottomLeft );
		} else {
			remove( topRight );
			remove( bottomRight );
			remove( bottomLeft );
			iconVisible=false;
		}
	}

    public void setScale( float scale ) {
        topLeft.scale.scale(scale);
        topRight.scale.scale(scale);
        bottomRight.scale.scale(scale);
        bottomLeft.scale.scale(scale);
    }

    public void showStatus( boolean value ) {
        topLeft.visible = value;
        bottomLeft.visible = value;
    }

	public void clearIcon(){
		icon.view(ItemSpriteSheet.SMTH,null);
	}
}
