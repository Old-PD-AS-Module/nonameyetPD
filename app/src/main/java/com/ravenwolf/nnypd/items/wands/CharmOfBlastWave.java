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
package com.ravenwolf.nnypd.items.wands;



import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlastWave;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;


public class CharmOfBlastWave extends WandUtility {

	{
		name = "冲击魔咒";
		image = ItemSpriteSheet.CHARM_BLAST;
	}

	public int basePower(){
		return super.basePower()+4;
	}

	public void blast(int cell, int damage ) {
		Dungeon.level.press(cell, null);
		Camera.main.shake( 3, 0.1f );
		Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
		if( Dungeon.visible[ cell ] ){
			BlastWave.createAtPos( cell );
		}
		//first knock enemies around target cell
		Char n;
		for (int i : Level.NEIGHBOURS8) {
			n = Actor.findChar(cell + i);
			if (n!=null) {
				//FIXME
				//hero takes half damage
				if(n instanceof Hero)
					damage/=2;
				n.damage(n.absorb(damage *2/3, true), this, Element.PHYSICAL);
				n.knockBack(cell, damage, damage * 3 / n.totalHealthValue() + 1);
			}
		}
		//then search in the targeted cell
		n = Actor.findChar(cell );
		if (n!=null){
			n.damage(n.absorb(damage , true), this, Element.PHYSICAL);
			n.knockBack( curUser.pos, damage, damage * 3 / n.totalHealthValue() + 1);
		}
	}

	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		int dmg=hero.absorb( damageRoll()/2, true )/2;
		hero.damage(dmg, this, Element.PHYSICAL);
		hero.knockBack( Ballistica.trace[1], dmg, dmg * 3 / hero.totalHealthValue() + 1);
	}

	@Override
	protected void onZap( int cell ) {
		super.onZap(cell);
		blast(cell, damageRoll());
	}

	protected void fx( int cell, Callback callback ) {
		MagicMissile.blast( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return
				"这个魔咒的核心处有个金属水晶在缓慢旋转，可以在目标位置引发一个强烈的爆炸，将会击飞并伤害周围的多数敌人。";
	}

}
