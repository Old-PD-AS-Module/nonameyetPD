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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.special.Exposed;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.sprites.ShadowBatSprite;

public class ShadowBat extends MobEvasive {

    public ShadowBat() {

        super( 10 );

        name = "暗影蝙蝠";
        spriteClass = ShadowBatSprite.class;

        flying = true;

        baseSpeed = 2f;

        dexterity = dexterity * 2/3;
        HT =HT *2/3;
        HP=HT;
	}

    @Override
    public String getTribe() {
        return TRIBE_BEAST;
    }

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
            && Level.distance( pos, Dungeon.hero.pos ) <= 2
            && detected( Dungeon.hero )
        ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

    @Override
    public float moveSpeed() {
        //to prevent chasing you when retreating
        return ( Dungeon.hero.isAlive() && enemySeen && Level.distance( pos, Dungeon.hero.pos ) == 2 )?super.moveSpeed()*0.5f:super.moveSpeed();
    }


    public float awareness() {
        return super.awareness()* 0.8f ;
    }

    @Override
    public boolean isRanged() {
        return false;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return !isCharmedBy( enemy ) && (( Level.adjacent( pos, enemy.pos )
                || (Level.distance( pos, Dungeon.hero.pos ) == 2 && !hasBuff(Ensnared.class) && !hasBuff(Crippled.class) && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos )));
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if( Dungeon.hero.isAlive() && enemySeen && Level.distance( pos, Dungeon.hero.pos ) == 2 ) {
            int oldPos = pos;

            if (getCloser(enemy.pos)) {
                moveSprite(oldPos, pos);
            }
        }
        if( Level.adjacent( pos, enemy.pos )) {
            //Attack and play animation but dont spend time yet
            boolean visible = Dungeon.visible[pos] || Dungeon.visible[enemy.pos];
            if ( visible ) {
                Dungeon.visible[pos] = true;
                sprite.attack( enemy.pos );
            } else {
                attack( enemy );
            }
            return !visible;

        }else {
            spend(TICK / moveSpeed());
            return true;
        }
    }

    @Override
    public void onAttackComplete(){
        //super.onAttackComplete();
        attack( enemy );
        if (!hasBuff(Exposed.class) && !hasBuff(Ensnared.class) && !hasBuff(Crippled.class)) {
            int oldPos = pos;
            if (getFurther(enemy.pos)) {
                moveSprite(oldPos, pos);
            }
        }
        spend( attackDelay() );
        next();
    }

	@Override
	public String description() {
        return
                "我们并不清楚这些奇怪蝙蝠的起源，它们非常难以理解，并且总是与它的猎物保持一定距离。";
    }
}
