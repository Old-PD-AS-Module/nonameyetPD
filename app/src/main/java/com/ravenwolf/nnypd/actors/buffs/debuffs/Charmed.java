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

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Charmed extends Debuff {

    @Override
    public Element buffType() {
        return Element.MIND;
    }

/*    @Override
    public String toString() {
        return "Charmed";
    }

    @Override
    public String statusMessage() { return "charmed"; }

    @Override
    public String playerMessage() { return "You are charmed!"; }*/
    @Override
    public String toString() {
        return "魅惑";
    }

    @Override
    public String statusMessage() { return "魅惑"; }

    @Override
    public String playerMessage() { return "你被魅惑了！"; }

    @Override
    public int icon() {
        return BuffIndicator.CHARMED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.CHARMED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.CHARMED );
    }

    @Override
/*    public String description() {
        return "Someone's trying to control your mind. You cannot bring yourself to attack your " +
            "enemies with full strength. Still, weaker minds would be completely enslaved by now.";
    }*/
    public String description() {
        return "有人正在试图控制你的思想。使你不能用尽全力去攻击这位敌人。但如果意志力不够强的话，恐怕早已沦为施法者的傀儡。";
    }

    public int object = 0;

    private static final String OBJECT	= "object";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( OBJECT, object );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        object = bundle.getInt( OBJECT );
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {

            if( target instanceof Mob) {
                Mob mob =(Mob)target;
                if( mob.hostile && !Bestiary.isBoss( mob ) ) {
                    mob.resetEnemy();
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
