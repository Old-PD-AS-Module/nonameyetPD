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
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.particles.HauntedParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.sprites.MobLayeredSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SpectralGuardian extends MobPrecise {

    public MeleeWeapon weap;

    public SpectralGuardian(MeleeWeapon weap) {
        this( Dungeon.depth );
        this.weap=weap;
        if ( weap!= null) {
            accuracy = (int) ((accuracy + tier) * (1 - weap.penaltyBase() * 0.025f));
            minDamage = weap.min();
            maxDamage = weap.max();
        }
    }

    //empty constructor used to restore from bundle
    public SpectralGuardian() {
        this( Dungeon.depth );
    }


    public SpectralGuardian(int depth) {

        super( Dungeon.chapter(), depth*3/4 + 2, false );

        name = "幽灵守护者";
        spriteClass = GuardianSprite.class;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

	}

	//less chance to trigger cursed enchantments
    @Override
    public float willpower(){
        return super.willpower()*2;
    }

    //less critical chance, and less detection
    @Override
    public float awareness(){
        return super.awareness() * 0.8f;
    }

    @Override
    protected int meleeAttackRange() {
        return weap.reach();
    }

    @Override
    public int STR() {
        return weap.str();
    }

    @Override
    public float attackSpeed() {
        return super.attackSpeed() *  weap.speedFactor(this);
    }

    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy) || Level.distance( pos, enemy.pos ) <=meleeAttackRange()  &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    public String getTribe() {
        return TRIBE_LONER;
    }

    @Override
    public boolean isRanged() {
        return false;
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }


	@Override
	public String description() {
		return
			"强大的魔法可以将灵魂困在物体内，赋予它目标和模糊的物理形态。 " +
            "这些生物被称为幽灵守护者，与这些物体有着紧密的联系，并将保护它们直到灭绝。"+
            "这位守护者极有可能是 _"+weap.name()+"_.";
	}

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( !blocked) {
            damage=weap.proc(this,enemy,damage);
        }
        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        super.die( cause, dmg );
        if (weap != null) {
            Dungeon.level.drop( weap, pos ).sprite.drop();
        }
    }


    private static final String WEAPON = "weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(WEAPON, weap );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        weap = (MeleeWeapon)bundle.get(WEAPON);
        if ( weap!= null) {
            accuracy = (int) ((accuracy + tier) * (1 - weap.penaltyBase() * 0.025f));
            minDamage = weap.min();
            maxDamage = weap.max();
        }
    }

    public static class GuardianSprite extends MobLayeredSprite {

        private Emitter hauntedParticles;

        public GuardianSprite(){
            super();
            texture( Assets.STATUE );
            TextureFilm frames = new TextureFilm( texture, 12, 15 );

            idle = new Animation( 2, true );
            idle.frames( frames, 0, 0, 0, 0, 0, 1, 1 );

            run = new Animation( 15, true );
            run.frames( frames, 2, 3, 4, 5, 6, 7 );

            attack = new Animation( 12, false );
            attack.frames( frames, 0, 8, 9, 10 );

            stab = new Animation( 14, false );
            stab.frames( frames, 8, 10, 10, 0 );

            backstab = new Animation( 12, false );
            backstab.frames( frames, 0, 9, 10, 0 );

            die = new Animation( 5, false );
            die.frames( frames, 11, 12, 13, 14, 15, 15 );

            play( idle );
            alpha(0.6f);
            tint(0.2f, 0, 1, 0.4f);
        }

        public void blink( int from, int to ) {
            place( to );
            play( idle );
            turnTo( from , to );
        }

        @Override
        public void link(Char ch) {
            super.link(ch);

            hauntedParticles = emitter();
            hauntedParticles.autoKill = false;
            hauntedParticles.pour(HauntedParticle.FACTORY, 0.15f);
            hauntedParticles.on = true;
        }

        @Override
        protected Weapon getWeaponToDraw() {
            if (ch instanceof SpectralGuardian) {
                SpectralGuardian mob = (SpectralGuardian) ch;
                return mob.weap;
            }
            return null;
        }

        @Override
        public void resetColorOnly() {
            super.resetColorOnly();
            alpha(0.6f);
            tint(0.2f, 0, 1, 0.4f);
        }

        @Override
        public void update() {
            super.update();
            if(hauntedParticles !=null) {
                hauntedParticles.visible = visible;
            }
        }

        @Override
        public void die() {
            super.die();
            emitter().burst( HauntedParticle.FACTORY, 10 );
            emitter().burst( ShadowParticle.UP, 8 );
            hauntedParticles.killAndErase();
        }

        @Override
        public int blood() {
            return 0x664488EE;
        }
    }


    public void blink() {
        int newPos;
        do {
            newPos = Random.Int( Level.LENGTH );
        } while ( Level.solid[newPos] || !Level.fieldOfView[newPos] ||
                Actor.findChar(newPos) != null || pos == newPos ||  Level.chasm[newPos] ||
                Ballistica.cast(pos, newPos, false, false) != newPos );

        if (Dungeon.visible[newPos]) {
            CharmOfBlink.appear( this, newPos );
        }

        move( newPos );
        ((GuardianSprite)sprite).blink(pos,newPos);
        spend( 1 / moveSpeed() );

    }

    public void knockBack( int sourcePos, int damage, int amount ) {
        knockBack(sourcePos, damage, amount, new Callback() {
            @Override
            public void call() {
                ((GuardianSprite)sprite).blink(pos,pos);
            }
        });
    }

}
