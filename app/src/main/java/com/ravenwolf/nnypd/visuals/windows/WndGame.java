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

import com.ravenwolf.nnypd.Difficulties;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.scenes.InterlevelScene;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.scenes.TitleScene;
import com.ravenwolf.nnypd.visuals.ui.Icons;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.Game;

import java.io.IOException;

public class WndGame extends Window {

    private static final String TXT_SETTINGS	= "设置";
    private static final String TXT_JOURNAL		= "日志";
    private static final String TXT_CHALLENGES  = "挑战";
    private static final String TXT_RANKINGS	= "排行榜";
    private static final String TXT_TUTORIAL	= "指南";
    private static final String TXT_START		= "踏上征途";
    private static final String TXT_MENU		= "主菜单";
    private static final String TXT_EXIT		= "退出游戏";
    private static final String TXT_RETURN		= "继续游戏";


    private static final String TXT_VERSION   	= "版本: %s";
    private static final String TXT_DIFFICULTY 	= "难度: %s";

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	private int pos;
	
	public WndGame() {
		
		super();

		addButton(new RedButton(TXT_SETTINGS) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndSettings(true));
            }
        });

        addButton( new RedButton( TXT_JOURNAL ) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndCatalogus());
            }
        } );

        addButton( new RedButton( TXT_TUTORIAL ) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndTutorial());
            }
        } );
		
//		if (Dungeon.challenges > 0) {
//			addButton( new RedButton(TXT_CHALLENGES) {
//				@Override
//				protected void onClick() {
//					hide();
//					GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
//				}
//			} );
//		}
		
		if (!Dungeon.hero.isAlive()) {

			addButton(new RedButton(TXT_START) {

                {
                    icon = Icons.get(Dungeon.hero.heroClass);
                }

                @Override
                protected void onClick() {
                    Dungeon.hero = null;
                    NoNameYetPixelDungeon.challenges(Dungeon.challenges);
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.noStory = true;
                    Game.switchScene(InterlevelScene.class);
                }
            });
			
//			addButton( new RedButton( TXT_RANKINGS ) {
//				@Override
//				protected void onClick() {
//					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
//					Game.switchScene( RankingsScene.class );
//				}
//			} );
//		} else {



//            addButton(new RedButton(TXT_JOURNAL) {
//                @Override
//                protected void onClick() {
//                    hide();
//                    GameScene.show(new WndJournal());
//                }
//            });
        }
				
		addButtons(
                new RedButton(TXT_MENU) {
                    @Override
                    protected void onClick() {
                        try {
                            Dungeon.saveAll();
                        } catch (IOException e) {
                            // Do nothing
                        }
                        Game.switchScene(TitleScene.class);
                    }
                }, new RedButton(TXT_EXIT) {
                    @Override
                    protected void onClick() {
                        Game.instance.finish();
                    }
                }
        );
		
		addButton( new RedButton( TXT_RETURN ) {
			@Override
			protected void onClick() {
				hide();
			}
		} );


        /*BitmapTextMultiline showDifficulty = PixelScene.createMultiline(
                Utils.format(TXT_DIFFICULTY, Difficulties.NAMES[Dungeon.difficulty]), 6
        );*/
		RenderedTextMultiline showDifficulty = PixelScene.renderMultiline(
                Utils.format(TXT_DIFFICULTY, Difficulties.NAMES[Dungeon.difficulty]), 5
        );

        showDifficulty.hardlight( 0xAAAAAA );
        /*showDifficulty.maxWidth = WIDTH - GAP * 2;
        showDifficulty.measure();
        showDifficulty.x = ( WIDTH - showDifficulty.width() ) / 2 + GAP;
        showDifficulty.y = pos + ( BTN_HEIGHT / 2 - showDifficulty.height() ) / 2 + GAP * 2;*/
		showDifficulty.maxWidth(WIDTH - GAP * 2);
        PixelScene.align(showDifficulty);
        add(showDifficulty);

        /*BitmapTextMultiline showVersion = PixelScene.createMultiline(
                Utils.format( TXT_VERSION, Game.version ), 6
        );*/
		RenderedTextMultiline showVersion = PixelScene.renderMultiline(
                Utils.format( TXT_VERSION, Game.version ), 5
        );

        showVersion.hardlight( 0xAAAAAA );
        /*showVersion.maxWidth = WIDTH - GAP * 2;
        showVersion.measure();
        showVersion.x = ( WIDTH - showVersion.width() ) / 2 + GAP;
        showVersion.y = pos + ( BTN_HEIGHT - showVersion.height() / 2 ) / 2 + GAP * 2;*/
		showVersion.maxWidth(WIDTH - GAP * 2);
        PixelScene.align(showVersion);
        add(showVersion);

        float x1 = ( WIDTH  + showDifficulty.width()) / 2 + GAP;
        float y1 = pos + ( BTN_HEIGHT - showDifficulty.height() / 2 ) / 2 + GAP * 2;

        float x2 = ( WIDTH - showVersion.width() - showDifficulty.width()) / 2;
        float y2 = pos + ( BTN_HEIGHT - showVersion.height() / 2 ) / 2 + GAP * 2;

        showVersion.setPos(x2, y2);
        showDifficulty.setPos(x1, y1);

		resize( WIDTH, pos + BTN_HEIGHT + GAP );
	}
	
	private void addButton( RedButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
	
	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
}
