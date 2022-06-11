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

import com.ravenwolf.nnypd.visuals.windows.WndStory;
import com.watabou.noosa.Game;

public class IntroScene extends PixelScene {

	private static final String TEXT =
			"在你之前，曾经有很多来自上方城镇的英雄向这个地城进发。有的人带回了财宝和魔法道具，而大多数人彻底销声匿迹。\n\n不过，从未有人能成功到达过底层，染指Yendor护符，传说它被深渊中的远古邪物守卫着，哪怕是现在，黑暗之力也从地下辐射而来，一路渗透到城镇中。\n\n你认为你准备好接受挑战了，更重要的是，你觉得命运女神正对你微笑. 是时候开始你自己的冒险了！";
	
	@Override
	public void create() {
		super.create();
		
		add( new WndStory( TEXT ) {
			@Override
			public void hide() {
				super.hide();
				Game.switchScene( InterlevelScene.class );
			}
		} );
		
		fadeIn();
	}
}
