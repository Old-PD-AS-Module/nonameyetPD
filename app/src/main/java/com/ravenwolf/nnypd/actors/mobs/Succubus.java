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

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.SuccubusSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Succubus extends MobPrecise {
	
	private static final int BLINK_DELAY = 6;
	
	private int delay = 0;

    public Succubus() {

        super( 18 );

        loot = Generator.random(Generator.Category.POTION);
        lootChance = 0.20f;

        name = "魅魔";
        spriteClass = SuccubusSprite.class;

        armorClass /= 3;
        maxDamage-=tier;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return ( super.canAttack( enemy ) || delay <= 0 &&
            Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos
            && !enemy.isMagical() && !enemy.hasBuff(Charmed.class)) && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.purpleLight(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_CHARMS);

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {
        if ( hit( this, enemy, true, true ) ) {
            int dmg=damageRoll();
            Charmed buff = BuffActive.addFromDamage( enemy, Charmed.class, damageRoll() );
            if( buff != null ) {
                buff.object = this.id();
                enemy.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
                //charm bolt trigger armor/shield enchantment
                enemy.defenseProc(this,dmg,false);
            }
        } else {
            enemy.missed();
        }

        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() ) {

            int healed=Element.Resist.doResist( enemy, Element.BODY,damage );

            if (healed > 0) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }

        return damage;
    }

	@Override
	protected boolean getCloser( int target ) {
		if (delay <= 0 && enemySeen && enemy != null && Level.fieldOfView[target]
            && Level.distance( pos, target ) > 1 && enemy.isCharmedBy( this )
            && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos ) {
			blink( target );
			spend( -2 / moveSpeed() );
			return true;
		} else {
			delay--;
			return super.getCloser( target );
		}
	}
	
	private void blink( int target ) {
		
		int cell = Ballistica.cast( pos, target, false, true );
		if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		CharmOfBlink.appear( this, cell );
		delay = BLINK_DELAY;
	}
	
	@Override
	public String description() {
		return
			"魅魔属于恶魔，她们看上去就像一个诱人的（哥特风格）女孩。她们那魔鬼般的妩媚总能轻松迷住普通人，使他们战斗乏力，很容易的被魅魔干掉。";
	}

    private static final String DELAY = "delay";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(DELAY, delay);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        delay = bundle.getInt(DELAY);
    }
}
