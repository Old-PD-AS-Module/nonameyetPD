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

import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroSkill;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.windows.WndChooseSkill1;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TomeOfMasterySkill1 extends Item {

	public static final float TIME_TO_READ = 2;

//	public static final String AC_READ	= "READ";
	public static final String AC_READ	= "阅读";

	{
		stackable = false;
//		name = "Ancient Spell Book";
		name = "天赋之书";
		image = ItemSpriteSheet.TOME_SPELL;

		unique = true;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			if (hero.buff( Blinded.class ) != null)
				GLog.w( Blinded.TXT_CANNOT_READ );
			else {
				curUser = hero;
				GameScene.show(new WndChooseSkill1(this, hero.availableSkills.get(0)));
			}
		} else {
			super.execute( hero, action );
		}
	}

	@Override
/*	public String info() {
		return
			"This worn leather book is not that thick, but you feel somehow, " +
			"that you can gather a lot from it.";
	}*/
	public String info() {   //放大镜信息
		return
				"这本破旧的书籍没多厚，但是你能隐约感觉到自己能丛总学到不少东西，不过，阅读它可能需要耗费一些时间";
	}

	public void choose( HeroSkill skill ) {

		detach( curUser.belongings.backpack );

		curUser.spend( TomeOfMasterySkill1.TIME_TO_READ );
		curUser.busy();

		Buff.affect(curUser, skill.skillClass());
		if (curUser.skill1==HeroSkill.NONE)
			curUser.skill1=skill;
		else
			curUser.skill2=skill;
		curUser.availableSkills.remove(skill);
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_MASTERY );

		SpellSprite.show( curUser, SpellSprite.MASTERY );
		curUser.sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );
//		GLog.w( "You have learned the %s skill!", Utils.capitalize( skill.title() ) );
		GLog.w( "你选择了学习%s!", Utils.capitalize( skill.title() ) );


	}
}
