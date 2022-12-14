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

import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.RedButton;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;

import java.util.ArrayList;

public class WndOptions extends Window {
	private static final int BUTTON_HEIGHT = 20;
	private static final int DISABLED_COLOR = 13291458;
	private static final int MARGIN = 2;
	private static final int WIDTH = 120;
	public ArrayList<Integer> disabled;

	public WndOptions(String str, String str2, String... strArr) {
		this.disabled = new ArrayList<>();
		this.disabled = disabled();
		BitmapTextMultiline createMultiline = PixelScene.createMultiline(str, 9.0f);
		createMultiline.hardlight(Window.TITLE_COLOR);
		createMultiline.y = 2.0f;
		createMultiline.x = 2.0f;
		createMultiline.maxWidth = 116;
		createMultiline.measure();
		add(createMultiline);
		BitmapTextMultiline createMultiline2 = PixelScene.createMultiline(str2, 7.0f);
		createMultiline2.maxWidth = 116;
		createMultiline2.measure();
		createMultiline2.x = 2.0f;
		createMultiline2.y = createMultiline.y + createMultiline.height() + 2.0f;
		add(createMultiline2);
		float height = createMultiline2.y + createMultiline2.height() + 2.0f;

		for (int i = 0; i < strArr.length; i++) {
			final int index = i;
			RedButton redButton = new RedButton( strArr[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			ArrayList<Integer> arrayList = this.disabled;
			if (arrayList != null && arrayList.contains(i)) {
				redButton.textColor(DISABLED_COLOR);
			}
			redButton.setRect(2.0f, height, 116.0f, 20.0f);
			add(redButton);
			height += 22.0f;
		}
		resize(120, (int) height);
	}

	protected void onSelect(int i) {
	}

	protected ArrayList<Integer> disabled() {
		return null;
	}
}
