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
package com.ravenwolf.nnypd.items.food;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.CarrionSwarm;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public abstract class Food extends Item {

/*    private static final String TXT_STUFFED		= "You are overfed... Can't eat anymore.";

    private static final String TXT_NOT_THAT_HUNGRY = "Don't waste your food!";

    private static final String TXT_R_U_SURE =
        "Your satiety cannot be greater than 100% anyway, so probably it would be a better idea to " +
        "spend some more time before eating this piece of food. Are you sure you want to eat it now?";

    private static final String TXT_YES			= "Yes, I know what I'm doing";
    private static final String TXT_NO			= "No, I changed my mind";
	
	public static final String AC_EAT	= "EAT";*/
    private static final String TXT_STUFFED		= "你吃的太多了..不能再吃了";

    private static final String TXT_NOT_THAT_HUNGRY = "不要浪费食物！";

    private static final String TXT_R_U_SURE =
            "你的饱食度无法超过100%，所以在吃下这份食物前最好能再多等一阵。你确定要食用它吗？";

    private static final String TXT_YES			= "是的，我知道我在做什么";
    private static final String TXT_NO			= "不，我改变主意了";

    public static final String AC_EAT	= "食用";

	public float time;
	public float energy;
	public String message;

	{
		stackable = true;
        time = 3f;
	}

    @Override
    public String quickAction() {
        return AC_EAT;
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		if ( action.equals( AC_EAT ) && hero != null ) {

            final Satiety hunger = hero.buff(Satiety.class);

                if( hunger.energy() + energy > Satiety.MAXIMUM ){

                    GameScene.show(
                        new WndOptions( TXT_NOT_THAT_HUNGRY, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                            @Override
                            protected void onSelect( int index ){
                                if( index == 0 ){
                                    consume( hunger, hero );
                                }
                            }
                        }
                    );

                } else {
                    consume( hunger, hero );
                    }
		} else {
			super.execute( hero, action );
		}
	}

    private void consume( Satiety hunger, Hero hero ) {

        hunger.increase(energy);
        detach(hero.belongings.backpack);
        onConsume( hero );

        hero.sprite.operate( hero.pos );
        hero.busy();
        SpellSprite.show( hero, SpellSprite.FOOD );
        Sample.INSTANCE.play( Assets.SND_EAT );

        hero.spend( time );

        for (Mob mob : Dungeon.level.mobs) {
            if ( mob instanceof CarrionSwarm ) {
                mob.beckon( hero.pos );
            }
        }

        Statistics.foodEaten++;
        Badges.validateFoodEaten();
        updateQuickslot();
    }
	
	@Override
/*	public String desc() {
		return 
			"Nothing fancy here: dried meat, " +
			"some biscuits - things like that.";
	}
	
	@Override
	public int price() {
		return 30 * quantity;
	}

    public void onConsume( Hero hero ) {
        GLog.i( message );
    }

    @Override
    public String info() {
        return desc() + "\n\n" +
            "Eating this piece of food will take _" + (int)time + "_ turns and " +
            "restore _" + (int)( energy / 10 ) + "%_ of your satiety.";
    }*/
    public String desc() {
        return
                "这里没什么有意思的东西：肉干和一些饼干--之类的东西";
    }

    @Override
    public int price() {
        return 30 * quantity;
    }

    public void onConsume( Hero hero ) {
        GLog.i( message );
    }

    @Override
    public String info() {
        return desc() + "\n\n" +
                "吃掉它会消耗 _" + (int)time + "_回合，并回复" +
                "_" + (int)( energy / 10 ) + "%_饱食度.";
    }

}