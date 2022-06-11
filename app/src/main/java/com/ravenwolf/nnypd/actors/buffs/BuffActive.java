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
package com.ravenwolf.nnypd.actors.buffs;

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public abstract class BuffActive extends Buff {

    private int duration;

    @Override
    public String status() {
        return duration > 0 ? Integer.toString( duration ) : null;
    }

    public Element buffType() {
        return null;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration( int value ) {
        duration = value;
    }

    public void add( float value ) {
        duration += value;
    }

    public void decrease() {
        decrease(1);
    }

    public void decrease(int duration) {
        this.duration-=duration;
        if( this.duration < 1 ) {
            detach();
        }
    }

	@Override
	public boolean act() {

        duration--;

        if( duration > 0 ) {
            spend( TICK );
        } else {
            detach();
        }

		return true;
	}

    public static<T extends BuffActive> T add( Char target, Class<T> buffClass, float duration ) {
        return add( target, buffClass, duration, false );
    }

    public static<T extends BuffActive> T add( Char target, Class<T> buffClass, float duration , boolean reset) {

        boolean newBuff = false;

        T buff = target.buff(buffClass);

        if (buff == null) {
            try{

                buff = buffClass.newInstance();
//                duration += buff.durationModifier();
                if (!reset)
                    buff.delay( TICK );
                newBuff = true;

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        if( buff != null ){

            if( buff.buffType() != null ) {
                duration=Element.Resist.doResistBuff( target, buff.buffType(), duration );
            }

            if( duration > 0 ) {
                 if( !newBuff || buff.attachTo( target ) ) {
                     if (reset ) {
                         if (duration > buff.getDuration())
                             buff.setDuration((int) duration);
                     }else
                        buff.add(duration);
                     BuffIndicator.refreshHero();
                 }
             }
        }

        return buff;
    }

    public static<T extends BuffActive> T addFromDamage( Char target, Class<T> buffClass, int value ) {

        return add( target, buffClass, (float)Math.ceil( value / Math.sqrt( target.totalHealthValue() ) ) );

    }

    private static final String DURATION	= "duration";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DURATION, duration );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        duration = bundle.getInt( DURATION );
    }
}
