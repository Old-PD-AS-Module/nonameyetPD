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
package com.ravenwolf.nnypd.visuals.ui.specialActions;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.buffs.special.skills.BuffSkill;
import com.ravenwolf.nnypd.actors.hero.HeroSkill;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.windows.WndTitledMessage;
import com.watabou.noosa.Image;

public class TagSkill extends TagSpecialAction{

	BuffSkill skill;
	int index;

	{
		visible=false;
	}

	@Override
	protected void onClick() {
		if (enabled() && Dungeon.hero.ready) {
			skill.doAction();
		}
	}

	@Override
	protected boolean onLongClick() {
		if (Dungeon.hero.ready) {

			String cooldownText="";
			if (skill.getCD()>0)
				cooldownText="\n\n这个技能需要_"+(int)skill.getCD()+"回合_后才能再次使用。";
			else
				cooldownText="\n\n这个技能使用后会有_"+(int)skill.getMaxCD()+"回合_的冷却时间。";
			Image icon = new Image( icons );
			if(index==1 && Dungeon.hero.skill1!=null && Dungeon.hero.skill1!=HeroSkill.NONE) {
				icon.frame( film.get( Dungeon.hero.skill1.icon() ) );
				GameScene.show(new WndTitledMessage(icon, Dungeon.hero.skill1.title(), Dungeon.hero.skill1.desc()+cooldownText));
			}else if(index==2 && Dungeon.hero.skill2!=null && Dungeon.hero.skill2!=HeroSkill.NONE) {
				icon.frame( film.get( Dungeon.hero.skill2.icon() ) );
				GameScene.show(new WndTitledMessage(icon, Dungeon.hero.skill2.title(), Dungeon.hero.skill2.desc()+cooldownText));
			}
			return true;
		}
		return false;
	}

	public TagSkill(int index){
		super();
		this.index=index;
	}



	@Override
	public void update() {

		super.update();
		if (visible && skill!=null){
			if (skill.getCD()!=0){
				cooldownRatio=skill.getCD()/skill.getMaxCD();
			}else
				cooldownRatio=0;

		}else{
			if(index==1 && Dungeon.hero.skill1!=null && Dungeon.hero.skill1!=HeroSkill.NONE){
				activate(Dungeon.hero.skill1);
			}else if(index==2 && Dungeon.hero.skill2!=null && Dungeon.hero.skill2!=HeroSkill.NONE){
				activate(Dungeon.hero.skill2);
			}
		}

	}

	private void activate(HeroSkill skill) {
		this.skill=Dungeon.hero.buff(skill.skillClass());
		setIcon(skill.icon());
		visible=true;
		bg.visible = true;
	}


}
