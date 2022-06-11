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

import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.rings.RingOfSatiety;
import com.ravenwolf.nnypd.items.rings.RingOfVitality;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Satiety extends Buff {

    public static final float POINT	= 1f;
    public static final float REGENERATION_RATE	= 0.002f;

    public static final float MAXIMUM	= 1000f;
    public static final float STARVING  = 0f;

    public static final float DEFAULT	= MAXIMUM * 0.75f;
    public static final float PARTIAL   = MAXIMUM * 0.25f;
/*
    public static final float STARVING_HALF = MAXIMUM * -0.25f;
    public static final float STARVING_FULL = MAXIMUM * -0.50f;
*/
    public static final float STARVING_HALF = MAXIMUM * -0.05f;
    public static final float STARVING_FULL = MAXIMUM * -0.30f;

/*	private static final String TXT_NOT_SATIATED	= "You are not satiated anymore.";
	private static final String TXT_NOT_HUNGRY		= "You are not hungry anymore.";
	private static final String TXT_NOT_STARVING	= "You are not starving anymore.";

	private static final String TXT_SATIATED		= "You are stuffed!";
	private static final String TXT_HUNGRY		    = "You are hungry.";
	private static final String TXT_STARVING	    = "You are starving!";
	private static final String TXT_STARVING_HALF   = "Starving gets worse!";
	private static final String TXT_STARVING_FULL   = "Starving becomes unbearable!";

	private static final String TXT_AWAKE_HUNGRY	= "You were awoken by a rumbling in your stomach.";
	private static final String TXT_AWAKE_STARVING	= "You were awoken by a pain in your stomach.";*/
    private static final String TXT_NOT_SATIATED	= "你不再感到饱腹。";
    private static final String TXT_NOT_HUNGRY		= "你不再感到饥饿。";
    private static final String TXT_NOT_STARVING	= "你不再感到饥肠辘辘。";

    private static final String TXT_SATIATED		= "你吃饱了！";
    private static final String TXT_HUNGRY		    = "你有点饿了。";
    private static final String TXT_STARVING	    = "你已经饥肠辘辘！";
    private static final String TXT_STARVING_HALF   = "你感觉更饿了！";
    private static final String TXT_STARVING_FULL   = "你感觉要被饿死了！";

    private static final String TXT_AWAKE_HUNGRY	= "你被肚里传来的咕咕声惊醒。";
    private static final String TXT_AWAKE_STARVING	= "你被肚里传来的饥饿的疼痛惊醒。";

	private float remaining = MAXIMUM;
    private float surplus = 0.0f;

	private static final String LEVEL	= "remaining";
    private static final String SURPLUS	= "surplus";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, remaining );
		bundle.put( SURPLUS, surplus );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		remaining = bundle.getFloat( LEVEL );
        surplus = bundle.getFloat( SURPLUS );
	}
	
	@Override
	public boolean act() {

		if (target.isAlive()) {
			
			Hero hero = (Hero)target;

            float modifier = REGENERATION_RATE * hero.HT;
            //  *  ( 1.0f + ( hero.lvl - 1 ) * 0.1f + hero.strBonus * 0.1f );

			if ( remaining <= STARVING ) {

                if ( remaining <= STARVING_FULL ){
                    modifier *= 2.0f;
                } else if ( remaining <= STARVING_HALF ){
                    modifier *= 1.0f;
                } else {
                    modifier *= 0.0f;
                }

                surplus -= modifier;

            } else {

                modifier *= target.ringBuffsHalved(RingOfVitality.Vitality.class);

                if( hero.restoreHealth /*&& !Level.water[target.pos]*/ ) {
                    modifier *= 3.0f;
                }

                if ( remaining > DEFAULT ) {
                    modifier *= 1.5f;
                } else if ( remaining > PARTIAL ) {
                    modifier *= 1.0f;
                } else {
                    modifier *= 0.5f;
                }

                surplus += modifier;

			}

            if( surplus >= 1.0f ) {

                if( target.HP < target.HT ){

                    target.HP = Math.min( target.HT, target.HP + (int) surplus );

                    if( target.HP == target.HT && ( (Hero) target ).restoreHealth ){

/*                        ( (Hero) target ).interrupt(
//                                Level.water[ target.pos ] ?
//                                "You don't feel well. Better not sleep in the water next time." :
                                "You feel well rested."
//                                , !Level.water[ target.pos ]
                        );*/
                        ( (Hero) target ).interrupt(
//                                Level.water[ target.pos ] ?
//                                "You don't feel well. Better not sleep in the water next time." :
                                "你感觉浑身轻松"
//                                , !Level.water[ target.pos ]
                        );

                    }

                }

                surplus = surplus % 1.0f;

            } else if( surplus <= -1.0f ) {

                GameScene.flash(0x110000);

                hero.HP = Math.max( 0, hero.HP + (int)surplus );
                surplus = surplus % 1.0f;

                if( !target.isAlive() ) {

                    target.die( this, null );

                }

            }

            decrease( POINT );
			spend( TICK );
			
		} else {
			
			deactivate();
			
		}

		return true;
	}

    public void setValue( float value ) {
        remaining = value;
        surplus = 0.0f;
    }

    public void increase( float value ) {

        if( remaining < STARVING ){
            remaining = STARVING;
        }

        float newLevel = Math.min( MAXIMUM, remaining + value /** target.ringBuffsThirded( RingOfSatiety.Satiety.class )*/ );

        if ( remaining <= DEFAULT && newLevel > DEFAULT ) {

            GLog.i( TXT_SATIATED );

        } else if ( remaining <= PARTIAL && newLevel > PARTIAL ) {

            GLog.i( TXT_NOT_HUNGRY );

        } else if ( remaining <= STARVING && newLevel > STARVING ) {

            GLog.i( TXT_NOT_STARVING );

        }

        remaining = newLevel;

    }

    public void decrease( float value ) {

        float newLevel = Math.max( STARVING_FULL, remaining - value / target.ringBuffs( RingOfSatiety.Satiety.class ) );

        if ( remaining > DEFAULT && newLevel <= DEFAULT ) {

            GLog.i( TXT_NOT_SATIATED );

        } else if ( remaining > PARTIAL && newLevel <= PARTIAL ) {

            ((Hero)target).interrupt( TXT_AWAKE_HUNGRY );
            GLog.w( TXT_HUNGRY );

        } else if ( remaining > STARVING && newLevel <= STARVING ) {

            ((Hero)target).interrupt( TXT_AWAKE_STARVING );
            GLog.n( TXT_STARVING );

        } else if ( remaining > STARVING_HALF && newLevel <= STARVING_HALF ) {

            ((Hero)target).interrupt();
            GLog.n( TXT_STARVING_HALF );

        } else if ( remaining > STARVING_FULL && newLevel <= STARVING_FULL ) {

            ((Hero)target).interrupt();
            GLog.n( TXT_STARVING_FULL );

        }

        remaining = newLevel;

    }

	public boolean isStarving() {
		return remaining <= STARVING;
	}

	@Override
	public int icon() {
        if ( remaining <= STARVING ) {
            return BuffIndicator.STARVATION;
        } else if ( remaining <= PARTIAL ) {
            return BuffIndicator.HUNGER;
        } else if ( remaining > DEFAULT ) {
			return BuffIndicator.OVERFED;
		} else {
			return BuffIndicator.NONE;
		}
	}

    @Override
/*    public String toString() {
        if ( remaining <= STARVING_FULL ) {
            return "Starving (unbearable)";
        } else if ( remaining <= STARVING_HALF ) {
            return "Starving (harsh)";
        } else if ( remaining <= STARVING ) {
            return "Starving (mild)";
        } else if ( remaining <= PARTIAL ) {
            return "Hungry";
        } else if ( remaining > DEFAULT ) {
            return "Satiated";
        } else {
            return "";
        }
    }*/
    public String toString() {
        if ( remaining <= STARVING_FULL ) {
            return "极度饥饿(致命)";
        } else if ( remaining <= STARVING_HALF ) {
            return "极度饥饿(严重)";
        } else if ( remaining <= STARVING ) {
            return "极度饥饿(轻度)";
        } else if ( remaining <= PARTIAL ) {
            return "饥饿";
        } else if ( remaining > DEFAULT ) {
            return "饱腹";
        } else {
            return "";
        }
    }

    @Override
/*    public String description() {
        if ( remaining <= STARVING_FULL ) {
            return "You desperately crave for food! Your willpower is reduced by half and your health is drained even faster, and it will " +
                    "eventually kill you if you don't eat something. Even herbs will do.";
        } else if ( remaining <= STARVING_HALF ) {
            return "Starvation got worse!  Your willpower is reduced by half and you slowly lose your health and if you will not find any " +
                    "food soon, starvation will become unbearable.";
        } else if ( remaining <= STARVING ) {
            return "You are starving! Your willpower is reduced by 25% and your health regeneration is stopped completely and soon it " +
                    "will start to become painful if you will not find something to eat.";
        } else if ( remaining <= PARTIAL ) {
            return "You are hungry, which decreases your health regeneration rate by half. Better " +
                    "stop to have some quick snack if you have any.";
        } else if ( remaining > DEFAULT ) {
            return "Your stomach is full! This makes you feel good, and increases your health " +
                    "regeneration rate by half.";
        } else {
            return "";
        }
    }*/
    public String description() {
        if ( remaining <= STARVING_FULL ) {
            return "你现在极度渴望任何食物!并且你的意志力会降低一半，你的生命值流失的更快了，在不吃点东西的话，真的会饿死的。哪怕是再难吃的药草你也吃得下！";
        } else if ( remaining <= STARVING_HALF ) {
            return "你已经饿的无法忍受了，并且你的意志力会降低一半，你的生命值开始缓慢减少，再不找点吃的，你将会进入更致命的饥饿状态！";
        } else if ( remaining <= STARVING ) {
            return "你感觉现在非常饿，并且意志力降低25%，生命的自然恢复彻底停止，再不赶紧找些吃的，饥饿会给你带来更多的痛苦！";
        } else if ( remaining <= PARTIAL ) {
            return "你感觉现在有点饿了，你的自然恢复速率降低了一半，最好找点吃的东西填饱肚子。";
        } else if ( remaining > DEFAULT ) {
            return "你感觉自己吃的非常饱，饱腹状态会使你的生命恢复速度增加一半";
        } else {
            return "";
        }
    }

    public float energy() {
        return remaining > 0 ? remaining : 0;
    }

}
