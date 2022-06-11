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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.misc.utils.Utils;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.sprites.ImpSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Imp extends MobEvasive {

    public Item item;

    private static final String ITEM = "item";

    protected static final String TXT_STOLE	= "%s 从你的身上偷走了 %s !";
    protected static final String TXT_CARRY	= "\n\n这只小恶魔正携带着从你身上偷走的 _%s_ 。";

    public Imp() {

        super( 17 );

        name = "小恶魔";
        spriteClass = ImpSprite.class;

        flying = true;

        item = null;

        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.ENERGY;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( ITEM, item );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        item = (Item)bundle.get( ITEM );
    }

    @Override
    protected boolean getFurther( int target ) {

        if( enemySeen ) {
            int newPos = -1;

            for (int i = 0; i < 10; i++) {
                newPos = Dungeon.level.randomRespawnCell( true, false );
                if (newPos != -1) {
                    break;
                }
            }

            if (newPos != -1) {

                Actor.freeCell(pos);

                CellEmitter.get(pos).start(ElmoParticle.FACTORY, 0.03f, 2 + Level.distance(pos, newPos));

                pos = newPos;
                sprite.place(pos);
                sprite.visible = Dungeon.visible[pos];

                return true;

            } else {

                return false;

            }

        } else {

            return super.getFurther( target );

        }
    }

    @Override
    public void die( Object cause, Element dmg ) {

        super.die(cause, dmg);

        if (item != null) {
            Dungeon.level.drop( item, pos ).sprite.drop();
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if ( HP >= HT && item == null && enemy instanceof Hero && ((Hero)enemy).belongings.backpack.countVisibleItems() > 0 ) {

            final int enemyPos = enemy.pos;

            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemyPos];

            if ( visible ) {

                sprite.cast( enemyPos, new Callback() {
                    @Override
                    public void call() { onRangedAttack( enemyPos ); }
                }  );

            } else {

                cast(enemy);

            }

            spend( attackDelay() );

            return !visible;


        } else {

            return super.doAttack( enemy );

        }
    }

    @Override
    protected void onRangedAttack( int cell ) {

        onCastComplete();

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        if (hit( this, enemy, false, false )) {

            if (item == null && enemy instanceof Hero) {

                Hero hero = (Hero)enemy;

                Item item = hero.belongings.randomVisibleUnequipped();

                if (item != null ) {

                    Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 1.5f);
                    GLog.w(TXT_STOLE, this.name, item.name());

                    state = FLEEING;

                    int amount = Random.IntRange( 1, item.quantity() );
                    this.item = item.detach( hero.belongings.backpack, amount );

                    if( this.item != null ) {
                        this.item.quantity(amount);
                    }

                    ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                    reset(enemy.pos, pos, item, null);

//                    spend( attackDelay() * (-1) );

                }
            }

            return true;

        } else {

            enemy.missed();

            return false;
        }
    }

	@Override
    public String description() {
        return
                "小恶魔是恶魔中的下等生物，它们不以力量和魔法天赋著称，而是残忍和贪婪。不过它们中的一部分实际上是好心和善于社交的..." +
                        ( item != null ? Utils.format( TXT_CARRY, item.name() ) : "" );
    }

}
