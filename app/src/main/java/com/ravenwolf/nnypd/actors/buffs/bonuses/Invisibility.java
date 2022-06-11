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
package com.ravenwolf.nnypd.actors.buffs.bonuses;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Invisibility extends Bonus {

    public int cooldown=0;

/*    private static final String TXT_DISPEL		=
            "Invisibility is disrupted!";

    @Override
    public String toString() {
        return "Invisible";
    }

    @Override
    public String statusMessage() { return "invisible"; }

    @Override
    public String playerMessage() { return "You see your hands turn invisible!"; }*/

    private static final String TXT_DISPEL		=
            "隐形被中断！";

    @Override
    public String toString() {
        return "隐形";
    }

    @Override
    public String statusMessage() { return "隐形"; }

    @Override
    public String playerMessage() { return "你看到自己的手正在失去形体！"; }

    @Override
    public int icon() {
        return BuffIndicator.INVISIBLE;
    }

    @Override
    public void tintIcon(Image icon) {
        if (cooldown >0 && cooldown <3)
            icon.hardlight(0.6f-cooldown/12f, 0.5f-cooldown/12f, 0.6f-cooldown/8f);
         else if (cooldown >0)
            icon.hardlight(0.35f, 0.25f, 0.2f);
         else
            icon.hardlight(1f, 1f, 1f);
    }

    @Override
    public void applyVisual(){
        if (cooldown==0) {
            if (target.sprite.visible) {
                Sample.INSTANCE.play(Assets.SND_MELD);
            }

            target.sprite.add(CharSprite.State.INVISIBLE);
        }
    }

    @Override
    public void removeVisual() {
        //if( target.invisible <= 0 ){
            target.sprite.remove( CharSprite.State.INVISIBLE );
        //}
    }

/*    @Override
    public String description() {
        return "Your body is almost transparent, so enemies are less likely to notice you (but they " +
                "still can try to find you). Attacking or being bumped into will disrupt this effect for a short period, " +
                "but invisibility will be regained if you avoid those actions.";
    }*/
    @Override
    public String description() {
        return "你和周围的地形完全融为一体，使你不可能被看到(但发现你的敌人仍会试图寻找你)" +
                "攻击或被撞到都会中断隐形状态，在你避免这些动作后就会恢复隐形状态";
    }

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
		    target.invisible++;
			return true;
		} else {
			return false;
		}
	}

    @Override
    public boolean attachOnLoad( Char target ) {
        target.invisible++;
        return super.attachOnLoad(target);
    }
	
	@Override
	public void detach() {
		if(target.invisible > 0) {
            target.invisible--;
        }
		super.detach();
	}

    @Override
    public boolean act() {
        if (getDuration() >1 && cooldown>0){
            cooldown--;
            if (cooldown==0){
                target.invisible++;
                applyVisual();
            }
        }
        return super.act();
    }

    public static void dispel() {
        Invisibility.dispel( Dungeon.hero );
    }

	public static void dispel( Char ch ) {
        boolean dispelled=false;
        Invisibility buff = ch.buff( Invisibility.class );
        if ( buff != null ) {
            if(ch.invisible > 0) {
                dispelled=true;
                ch.invisible--;
                buff.removeVisual();
            }
            buff.cooldown=4;
		}
        if(dispelled)
            GLog.w(TXT_DISPEL);
	}

    private static final String REFRESH_DURATION	= "refresh";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( REFRESH_DURATION, cooldown );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt( REFRESH_DURATION );
    }
}
