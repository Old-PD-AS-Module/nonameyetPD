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
package com.ravenwolf.nnypd.actors.buffs.special;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.buffs.BuffPassive;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Light extends BuffPassive {

    private final static float DELAY = 3f;//5f;

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.ILLUMINATED );
    }

    @Override
    public void removeVisual() { target.sprite.remove( CharSprite.State.ILLUMINATED ); }
	
	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

/*    @Override
    public String toString() {
        return "Light";
    }

    @Override
    public String description() {
        return "Your lantern shines brightly, increasing your field of view and your " +
                "chance to find traps, but making it easier to notice you as well." ;
    }*/
    @Override
    public String toString() {
        return "发光";
    }

    @Override
    public String description() {
        return "你的油灯发出了耀眼的光芒，增加了你的视野范围和发现陷阱的概率，但也让敌人更容易发现你的行踪" ;
    }

    @Override
    public boolean act() {

        OilLantern lantern = Dungeon.hero.belongings.getItem( OilLantern.class );

        if( lantern != null && lantern.isActivated() && lantern.getCharge() > 0 ){

            lantern.spendCharge();
            spend( DELAY );

        } else {

            lantern.deactivate( Dungeon.hero, false );
            detach();

        }

        return true;
    }
}
