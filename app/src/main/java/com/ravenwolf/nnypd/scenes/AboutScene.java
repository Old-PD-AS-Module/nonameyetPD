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
package com.ravenwolf.nnypd.scenes;

import android.content.Intent;
import android.net.Uri;

import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.ui.Archs;
import com.ravenwolf.nnypd.visuals.ui.ExitButton;
import com.ravenwolf.nnypd.visuals.ui.Icons;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;

public class AboutScene extends PixelScene {

    //private static final int LINK_COLOR =0x666666;

    private static final String CAP_WATA =
            "Original Pixel Dungeon";

    private static final String TXT_WATA =
            "Author: Watabou\n" +
            "Music: Cube_Code" ;

    private static final String LNK_WATA =
            "pixeldungeon.watabou.ru";

    private static final String CAP_YAPD =
            "Yet Another Pixel Dungeon";

    private static final String TXT_YAPD =
            "Author: ConsideredHamster\n"+
            "Music: Jivvy";


    private static final String LNK_YAPD =
            "consideredhamster@gmail.com";

    private static final String CAP_NNYPD =
            "No Name Yet Pixel Dungeon";

    private static final String TXT_NNYPD =
            "Author: RavenWolf\n"+
            "Based on: YAPD\n"+
            "Additional Sprites:\n"+
            "ShatteredPD and MoonshinePD" ;

    private static final String LNK_NNYPD =
            "ravenwolflisk@gmail.com";


    @Override
    public void create() {
        super.create();

        final boolean landscape = NoNameYetPixelDungeon.landscape();
        final float colWidth = Camera.main.width / (landscape ? 3 : 1);
        final float colTop = (Camera.main.height / 3) - (landscape ? 30 : 90);
        final float yapdOffset = landscape ? colWidth : 0;
        final float wataOffset = landscape ? 2 * colWidth : 0;

        final int textMarging=landscape? 14: 10;
        final int titleMarging=landscape? 8: 4;
        final int textSize=landscape ? 8 : 6;

        Image nnyp = Icons.RAVEN.get();
        nnyp.x = (colWidth - nnyp.width()) / 2;
        nnyp.y = NoNameYetPixelDungeon.landscape() ? colTop: colTop + 15;
        add( nnyp );

        new Flare( 7, 64 ).color( 0x228822, true ).show( nnyp, 0 ).angularSpeed = +20;

        BitmapTextMultiline nnyptitle = createMultiline( CAP_NNYPD, 8 );
        nnyptitle.hardlight( Window.TITLE_COLOR );
        nnyptitle.measure();
        add( nnyptitle );

        nnyptitle.x = (colWidth - nnyptitle.width()) / 2;
        nnyptitle.y = nnyp.y + nnyp.height + 5;

        BitmapTextMultiline nnyptext = createMultiline( TXT_NNYPD, textSize );
        nnyptext.maxWidth=((int)Math.min(colWidth, 120));
        nnyptext.measure();

        nnyptext.x=(colWidth - nnyptext.width()) / 2;
        nnyptext.y=nnyptitle.y + nnyptitle.height() + textMarging;
        add( nnyptext );

        BitmapTextMultiline nnyplink = createMultiline( LNK_NNYPD, textSize );
        nnyplink.maxWidth=nnyptext.maxWidth;
        nnyplink.hardlight( Window.LINK_COLOR );
        nnyplink.measure();

        nnyplink.x=(colWidth - nnyplink.width()) / 2;
        nnyplink.y=nnyptext.y + nnyptext.height() + titleMarging;
        add( nnyplink );

        TouchArea nnyphotArea = new TouchArea( nnyplink ) {
            @Override
            protected void onClick( Touch touch ) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
                        + LNK_NNYPD + "?subject=" + "NoNameYetPD" + "&body=" + "" )
                );

                Game.instance.startActivity(intent);
            }
        };


        add( nnyphotArea );

        Image yapd = Icons.YAPD.get();
        yapd.x = yapdOffset + (colWidth - yapd.width()) / 2;
        yapd.y = NoNameYetPixelDungeon.landscape() ?
                colTop :
                nnyplink.y + yapd.height + 5;
        add( yapd );

        new Flare( 7, 64 ).color( 0x332211, true ).show( yapd, 0 ).angularSpeed = +20;

        BitmapTextMultiline yapdtitle = createMultiline( CAP_YAPD, 8 );
        yapdtitle.hardlight( Window.TITLE_COLOR );
        add( yapdtitle );

        yapdtitle.measure();
        yapdtitle.x = yapdOffset + (colWidth - yapdtitle.width()) / 2;
        yapdtitle.y = yapd.y + yapd.height + 5;

        BitmapTextMultiline yapdtext = createMultiline( TXT_YAPD, textSize );
        yapdtext.maxWidth=(int)Math.min(colWidth, 120);
        yapdtext.measure();

        yapdtext.x=yapdOffset + (colWidth - yapdtext.width()) / 2;
        yapdtext.y=yapdtitle.y + yapdtitle.height() + textMarging;
        add( yapdtext );

        BitmapTextMultiline yapdlink = createMultiline( LNK_YAPD, textSize );
        yapdlink.maxWidth=yapdtext.maxWidth;
        yapdlink.hardlight( Window.LINK_COLOR );
        yapdlink.measure();

        yapdlink.x=yapdOffset + (colWidth - yapdlink.width()) / 2;
        yapdlink.y=yapdtext.y  +yapdtext.height() + titleMarging;
        add( yapdlink );

        TouchArea yapdhotArea = new TouchArea( yapdlink ) {
            @Override
            protected void onClick( Touch touch ) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
                        + LNK_YAPD + "?subject=" + "YetAnotherPD" + "&body=" + "" )
                );

                Game.instance.startActivity(intent);
            }
        };
        add( yapdhotArea );

        Image wata = Icons.WATA.get();
        wata.x = wataOffset + (colWidth - wata.width()) / 2;
        wata.y = NoNameYetPixelDungeon.landscape() ?
                colTop:
                yapdlink.y + wata.height + 5;
        add( wata );

        new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;

        BitmapTextMultiline wataTitle = createMultiline( CAP_WATA, 8 );
        wataTitle.hardlight(Window.TITLE_COLOR);
        wataTitle.measure();
        add( wataTitle );

        wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
        wataTitle.y = wata.y + wata.height + 5/*11*/;

        BitmapTextMultiline wataText = createMultiline( TXT_WATA, textSize );
        wataText.maxWidth=((int)Math.min(colWidth, 120));
        wataText.measure();

        wataText.x=wataOffset + (colWidth - wataText.width()) / 2;
        wataText.y=wataTitle.y + wataTitle.height() + textMarging;

        add( wataText );

        BitmapTextMultiline wataLink = createMultiline( LNK_WATA, textSize );
        wataLink.maxWidth=((int)Math.min(colWidth, 120));
        wataLink.hardlight(Window.LINK_COLOR);
        wataLink.measure();

        wataLink.x=wataOffset + (colWidth - wataLink.width()) / 2;
        wataLink.y=wataText.y+ wataText.height()+ titleMarging;
        add(wataLink);

        TouchArea hotArea = new TouchArea( wataLink ) {
            @Override
            protected void onClick( Touch touch ) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK_WATA ) );
                Game.instance.startActivity( intent );
            }
        };
        add( hotArea );


        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

	
	@Override
	protected void onBackPressed() {
		NoNameYetPixelDungeon.switchNoFade(TitleScene.class);
	}
}
