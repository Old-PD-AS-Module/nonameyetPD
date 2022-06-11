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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Controlled;
import com.ravenwolf.nnypd.actors.mobs.Wraith;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfRaiseDead extends Scroll {
	
	{
        name = "死灵卷轴";
        shortName = "Ra";

        spellSprite = SpellSprite.SCROLL_RAISEDEAD;
        spellColour = SpellSprite.COLOUR_DARK;
        icon=5;
	}

	@Override
	protected void doRead() {

        ArrayList<Wraith> summoned = Wraith.spawnAround( curUser.magicSkill() / 3, curUser.pos, 2/*Random.IntRange( 3, 4 )*/ );


            for( Wraith w : summoned ){

                //float duration = Random.Int( 16, 20 ) * ( 110 + curUser.magicSkill() ) / 100;
                float duration = Random.Int( 10, 16 ) * ( 110 + curUser.magicSkill() ) / 100;

                Controlled buff = BuffActive.add( w, Controlled.class, duration );

                if( buff != null ){
                    buff.object = curUser.id();
                }

            }


        Sample.INSTANCE.play(Assets.SND_DEATH);

		super.doRead();
	}

	@Override
	public String desc() {
        return
                "卷轴下隐藏的邪恶法术允许阅读者与亡灵之物交流，赋予其召唤并控制亡灵的能力。不过要注意的是，它迟早会将獠牙对向自己的主人！" +
                        "\n\n控制效果的持续时间取决于使用者的魔能属性";
    }

    @Override
    public int price() {
        return isTypeKnown() ? 50 * quantity : super.price();
    }
}
