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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Miasma;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.sprites.FiendSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Fiend extends MobCaster {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public Fiend() {

        super( 19 );

        name = "恶魔巫师";
		spriteClass = FiendSprite.class;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        loot = Generator.random(Generator.Category.SCROLL);
        lootChance = 0.20f;
	}

    public String getTribe() {
        return TRIBE_LONER;
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
    public int damageRoll() {
        int dmg=super.damageRoll();
        return isRanged() ? dmg *2/3 : dmg;
    }

    @Override
    public boolean act() {

        if( !enemySeen )
            charged = false;

        return super.act();

    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !Level.adjacent( pos, enemy.pos ) && !charged ) {

            charged = true;

            sprite.centerEmitter().burst( EnergyParticle.FACTORY_BLACK, 25 );

            spend(attackDelay());

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 12) < tier ) {
            BuffActive.addFromDamage( enemy, Tormented.class, damage );
        }

        return damage;
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

    @Override
    public void die( Object cause, Element dmg ) {

        GameScene.add(Blob.seed(pos, 100, Miasma.class));

        super.die( cause, dmg );
    }

    @Override
    public String description() {
        return
                "这些恶魔似乎从肉体中超脱，以纯粹的黑暗作为自己的身形，朦胧而危险。这些邪性可恶的东西是由恶意而生，是纯粹的邪恶化身，以死亡和痛苦为乐。";
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
    }
}
