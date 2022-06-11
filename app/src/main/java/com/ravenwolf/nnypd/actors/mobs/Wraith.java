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
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.WraithSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wraith extends MobRanged {

	private static final float SPAWN_DELAY	= 2.0f;

	private static final float BLINK_CHANCE	= 0.20f;

    public Wraith() {
        this( Dungeon.depth );
    }

    public Wraith( int depth ) {

        super( Dungeon.chapter(), depth*3/4 + 2, false );

        name = "怨灵";
        spriteClass = WraithSprite.class;

        minDamage /= 2;
        maxDamage /= 2;

        HP = HT /= 2;

        armorClass = 0;
        flying = true;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Physical.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

        resistances.put( Element.Dispel.class, Element.Resist.VULNERABLE );
    }

    @Override
    public String getTribe() {
        return TRIBE_UNDEAD;
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isEthereal() {
        return true;
    }

    @Override
    public boolean ignoresAC(Char enemy) {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.UNHOLY_PERIODIC;
    }

    private void blink() {

        int newPos;

        do {

            newPos = Random.Int( Level.LENGTH );

        } while ( Level.solid[newPos] || !Level.fieldOfView[newPos] ||
                 Actor.findChar(newPos) != null || pos == newPos ||
                Ballistica.cast(pos, newPos, false, false) != newPos );

        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
        }

        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
        }

        ((WraithSprite)sprite).blink(pos, newPos);

        move( newPos );

        spend( 1 / moveSpeed() );

    }

	@Override
	public String description() {
        return
                "怨灵是来自于墓穴中渴望复仇的罪人之魂。作为飘渺的非实体，攻击能穿透任何盔甲，并且对一些常规武器有部分免疫。";
    }



    @Override
    protected boolean canAttack( Char enemy ) {
        return  !isCharmedBy( enemy ) && (Level.adjacent( pos, enemy.pos ) || (!enemy.hasBuff(Withered.class) && canCastBolt(enemy)));
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.shadow(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);
        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {
        return castBolt(enemy,damageRoll(),true,Element.UNHOLY);
    }

    protected boolean doAttack( Char enemy ) {

        if ( !rooted && Random.Float() < BLINK_CHANCE && !enemy.hasBuff(Withered.class)) {
            blink();
            return true;
        } else {
            return super.doAttack( enemy );
        }
    }

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
                && Level.distance( pos, Dungeon.hero.pos ) <= 2
                && detected( Dungeon.hero ) && detected( Dungeon.hero )
                ) {

            beckon( Dungeon.hero.pos );
        }
        return super.act();
    }

    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() && distance( enemy ) <= 1 && isAlive() ) {

            int healed=Element.Resist.doResist( enemy, Element.BODY,damage )/2;

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
    public boolean reset() {
        state = HUNTING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

    public static ArrayList<Wraith> spawnAround( int pos, int amount ) {
        return spawnAround( Dungeon.depth, pos, amount );
    }

    public static ArrayList<Wraith> spawnAround( int depth, int pos, int amount ) {

        ArrayList<Wraith> wraiths = new ArrayList<>();

        if( amount > 0 ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            for (int i = 0; i < amount; i++) {
                if (candidates.size() > 0) {
                    int o = Random.Int( candidates.size() );
                    wraiths.add( spawnAt( depth, candidates.get(o) ) );
                    candidates.remove( o );
                } else {
                    break;
                }
            }
        }
        return wraiths;
    }

    public static Wraith spawnAt( int pos ) {
        return spawnAt( Dungeon.depth, pos );
    }

    public static Wraith spawnAt( int depth, int pos ) {

        if (!Level.solid[pos] && Actor.findChar( pos ) == null) {

            Wraith w = new Wraith( depth );

            w.pos = pos;
            w.special = true;
            w.enemySeen = true;
            w.state = w.HUNTING;

            GameScene.add( w, SPAWN_DELAY );

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );

            return w;
        } else {
            return null;
        }
    }
}
