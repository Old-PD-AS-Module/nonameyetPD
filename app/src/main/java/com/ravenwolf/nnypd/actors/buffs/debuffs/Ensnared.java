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
package com.ravenwolf.nnypd.actors.buffs.debuffs;

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class    Ensnared extends Debuff {

/*    @Override
    public String toString() {
        return "Ensnared";
    }

    @Override
    public String statusMessage() { return "ensnared"; }

    @Override
    public String playerMessage() { return "You are ensnared! Struggle to escape!"; }*/
    @Override
    public String toString() {
        return "缠绕";
    }

    @Override
    public String statusMessage() { return "缠绕"; }

    @Override
    public String playerMessage() { return "你被缠绕了! 尝试移动挣脱它!"; }

    @Override
    public int icon() {
        return BuffIndicator.ENSNARED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.BURNING );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.BURNING );
//    }

    @Override
/*    public String description() {
        return "You are ensnared and cannot move. Evading attacks is more difficult, and you are " +
                "more likely to be noticed. You can try to break out, but this can attract unwanted " +
                "attention.";
    }*/
    public String description() {
        return "你被缠绕了，这种状态下无法移动，并且会降低你的闪避，怪物们也会更轻易的注意到你，你可以尝试着挣脱它，但挣脱产生的声响会引起更多的注意";
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
//		if (!target.flying && super.attachTo( target )) {
            target.rooted = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.rooted = false;
        super.detach();
    }

}