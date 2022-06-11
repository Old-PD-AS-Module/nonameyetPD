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

import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.ui.CheckBox;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

import static com.ravenwolf.nnypd.scenes.PixelScene.align;

public class WndSettings extends Window {

    private static final String TXT_ZOOM_IN			= "+";
    private static final String TXT_ZOOM_OUT		= "-";
    private static final String TXT_ZOOM_DEFAULT	= "默认缩放";

    private static final String TXT_SCALE_UP		= "缩放UI";
    private static final String TXT_IMMERSIVE		= "沉浸模式";

    private static final String TXT_MUSIC	        = "音乐";

    private static final String TXT_SOUND	        = "音效";

    private static final String TXT_BUTTONS         = "水袋/油灯: %s";

    private static final String[] TXT_BUTTONS_VAR  = {
            "右",
            "左",
    };

    private static final String TXT_BRIGHTNESS	    = "亮度";

    private static final String TXT_LOADING_TIPS  = "加载界面提示: %s";

    private static final String[] TXT_TIPS_DELAY  = {
            "关闭",
            "普通延迟",
            "双倍延迟",
            "点击跳过",
    };

    private static final String TXT_SEARCH_BTN  = "探索按钮: %s";

    private static final String[] TXT_SEARCH_VAR  = {
            "默认设置",
            "反向设置",
    };

    private static final String TXT_AUTO_THROWING  = "投武快速选择攻击";

    private static final String TXT_SWITCH_PORT 	= "竖屏模式";
    private static final String TXT_SWITCH_LAND 	= "横屏模式";

	private static final int WIDTH		= 112;
    private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;
	
	private RedButton btnZoomOut;
	private RedButton btnZoomIn;
	
	public WndSettings( final boolean inGame ) {
		super();
		
		CheckBox btnImmersive = null;
		
		if (inGame) {
			int w = BTN_HEIGHT;
			
			btnZoomOut = new RedButton( TXT_ZOOM_OUT ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom - 1 );
				}
			};
			add( btnZoomOut.setRect( 0, 0, w, BTN_HEIGHT) );
			
			btnZoomIn = new RedButton( TXT_ZOOM_IN ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom + 1 );
				}
			};
			add( btnZoomIn.setRect( WIDTH - w, 0, w, BTN_HEIGHT) );
			
			add( new RedButton( TXT_ZOOM_DEFAULT ) {
				@Override
				protected void onClick() {
					zoom( PixelScene.defaultZoom );
				}
			}.setRect(btnZoomOut.right(), 0, WIDTH - btnZoomIn.width() - btnZoomOut.width(), BTN_HEIGHT) );
			
			updateEnabled();

            RedButton btnSearchBtn = new RedButton( searchButtonsText( NoNameYetPixelDungeon.searchButton() ) ) {
                @Override
                protected void onClick(){

                    boolean val = !NoNameYetPixelDungeon.searchButton();

                    NoNameYetPixelDungeon.searchButton( val );

                    text.text( searchButtonsText( val ) );
                    //text.measure();
					align(text);
                    layout();
                }
            };
            btnSearchBtn.setRect( 0, BTN_HEIGHT + GAP, WIDTH, BTN_HEIGHT );
            add( btnSearchBtn );

            CheckBox btnBrightness = new CheckBox( TXT_BRIGHTNESS ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    NoNameYetPixelDungeon.brightness(checked());
                }
            };
            btnBrightness.setRect(0, btnSearchBtn.bottom()+ GAP, WIDTH, BTN_HEIGHT);
            btnBrightness.checked(NoNameYetPixelDungeon.brightness());
            add(btnBrightness);


        } else {

            RedButton btnOrientation = new RedButton( orientationText() ) {
                @Override
                protected void onClick() {
                    NoNameYetPixelDungeon.landscape(!NoNameYetPixelDungeon.landscape());
                }
            };
            btnOrientation.setRect(0, 0, WIDTH, BTN_HEIGHT);
            add(btnOrientation);

            CheckBox btnScaleUp = new CheckBox( TXT_SCALE_UP ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    NoNameYetPixelDungeon.scaleUp(checked());
                }
            };

            btnScaleUp.setRect(0, btnOrientation.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnScaleUp.checked(NoNameYetPixelDungeon.scaleUp());
            add( btnScaleUp );

            btnImmersive = new CheckBox( TXT_IMMERSIVE ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    NoNameYetPixelDungeon.immerse(checked());
                }
            };
            btnImmersive.setRect(0, btnScaleUp.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnImmersive.checked(NoNameYetPixelDungeon.immersed());
            btnImmersive.enable(android.os.Build.VERSION.SDK_INT >= 19);
            add(btnImmersive);
			
		}
		
		CheckBox btnMusic = new CheckBox( TXT_MUSIC ) {
			@Override
			protected void onClick() {
				super.onClick();
				NoNameYetPixelDungeon.music(checked());
			}
		};
		btnMusic.setRect( 0, ( BTN_HEIGHT + GAP ) * 3, WIDTH, BTN_HEIGHT );
		btnMusic.checked( NoNameYetPixelDungeon.music() );
		add(btnMusic);

        CheckBox btnSound = new CheckBox( TXT_SOUND ) {
            @Override
            protected void onClick() {
                super.onClick();
                NoNameYetPixelDungeon.soundFx(checked());
                Sample.INSTANCE.play(Assets.SND_CLICK);
            }
        };
        btnSound.setRect(0, btnMusic.bottom() + GAP, WIDTH, BTN_HEIGHT);
        btnSound.checked(NoNameYetPixelDungeon.soundFx());
        add(btnSound);

//        RedButton btnTracks = new RedButton( buttonsText( NoNameYetPixelDungeon.buttons() ) ) {
//            @Override
//            protected void onClick() {
//                super.onClick();
//
//                boolean val = !NoNameYetPixelDungeon.buttons();
//
//                NoNameYetPixelDungeon.buttons( val );
//
//                Sample.INSTANCE.play( Assets.SND_CLICK );
//
//                text.text( buttonsText( val ) );
//                text.measure();
//                layout();
//            }
//        };
//
//        btnTracks.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
//        add(btnTracks);

        RedButton btnTipsDelay = new RedButton( loadingTipsText( NoNameYetPixelDungeon.loadingTips() ) ) {
            @Override
            protected void onClick() {

                int val = NoNameYetPixelDungeon.loadingTips();

                val = val < 3 ? val + 1 : 0;
                NoNameYetPixelDungeon.loadingTips(val);

                text.text( loadingTipsText( val ) );
                //text.measure();
				align(text);
                layout();
            }
        };

//        btnTipsDelay.setRect(0, btnTracks.bottom() + GAP, WIDTH, BTN_HEIGHT);
        btnTipsDelay.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
        add(btnTipsDelay);

       // resize(WIDTH, (int) btnTipsDelay.bottom());

        CheckBox btnAutoThrow = new CheckBox( TXT_AUTO_THROWING ) {
            @Override
            protected void onClick() {
                super.onClick();
                NoNameYetPixelDungeon.autoThrow(checked());
                Sample.INSTANCE.play(Assets.SND_CLICK);
            }
        };
        btnAutoThrow.setRect(0, btnTipsDelay.bottom() + GAP, WIDTH, BTN_HEIGHT);
        btnAutoThrow.checked(NoNameYetPixelDungeon.autoThrow());
        add(btnAutoThrow);

        resize(WIDTH, (int) btnAutoThrow.bottom());

//			CheckBox btnQuickslot = new CheckBox( TXT_QUICKSLOT ) {
//				@Override
//				protected void onClick() {
//					super.onClick();
//					Toolbar.secondQuickslot(checked());
//				}
//			};
//			btnQuickslot.setRect( 0, btnBrightness.bottom() + GAP, WIDTH, BTN_HEIGHT );
//			btnQuickslot.checked( Toolbar.secondQuickslot() );
//			add( btnQuickslot );

//			resize( WIDTH, (int)btnQuickslot.bottom() );

	}
	
	private void zoom( float value ) {

		Camera.main.zoom( value );
		NoNameYetPixelDungeon.zoom((int) (value - PixelScene.defaultZoom));

		updateEnabled();
	}
	
	private void updateEnabled() {
		float zoom = Camera.main.zoom;
		btnZoomIn.enable(zoom < PixelScene.maxZoom);
		btnZoomOut.enable(zoom > PixelScene.minZoom);
	}
	
	private String orientationText() {
		return NoNameYetPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND;
	}

    private String searchButtonsText( boolean val ) {
        return Utils.format( TXT_SEARCH_BTN, TXT_SEARCH_VAR[ val ? 1 : 0 ] );
    }

    private String loadingTipsText( int val ) {
        return Utils.format( TXT_LOADING_TIPS, TXT_TIPS_DELAY[ val ] );
    }

    private String buttonsText( boolean val ) {
        return Utils.format( TXT_BUTTONS, TXT_BUTTONS_VAR[ val ? 1 : 0 ] );
    }
}
