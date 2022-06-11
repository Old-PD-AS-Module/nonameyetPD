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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.items.weapons.throwing.Boomerangs;
import com.ravenwolf.nnypd.items.weapons.throwing.Chakrams;
import com.ravenwolf.nnypd.items.weapons.throwing.Hammers;
import com.ravenwolf.nnypd.items.weapons.throwing.Harpoons;
import com.ravenwolf.nnypd.items.weapons.throwing.MoonGlaive;
import com.ravenwolf.nnypd.items.weapons.throwing.Tomahawks;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.sprites.GnollBerserkerSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GnollBerserker extends MobPrecise {

    private static final String TXT_ENRAGED = "%s被激怒了！";

    private int ammo=2;
    private static final String AMMO = "ammo";

    public GnollBerserker() {

        super( 12 );

        name = "豺狼暴徒";
		spriteClass = GnollBerserkerSprite.class;

        minDamage += 4;
        maxDamage += 4;
		
		loot =   Random.oneOf(Tomahawks.class,Harpoons.class,Boomerangs.class,Chakrams.class,MoonGlaive.class, Hammers.class);
		lootChance = 0.5f;

        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);

	}

    @Override
    public String getTribe() {
        return TRIBE_GNOLL;
    }

    @Override
    public int damageRoll() {
        int dmg=super.damageRoll();
        return isRanged() ? dmg *4/5 : dmg;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || ammo > 0 && Level.distance(pos, enemy.pos) <= 2
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Tomahawks(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });
        ammo--;
        super.onRangedAttack( cell );
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {
        super.damage( dmg, src, type );
        if ( isAlive() && buff( Enraged.class ) == null && HP < HT / 3 ) {
            BuffActive.add(this, Enraged.class, Random.Float( 5.0f, 10.0f ) );
            spend( TICK );

            if (Dungeon.visible[pos]) {
                GLog.w( TXT_ENRAGED, name );
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( isRanged()) {

            if ( !blocked && Random.Int( enemy.HT ) < damage *2) {
                BuffActive.addFromDamage( enemy, Bleeding.class, damage );
            }
        }

        return damage;
    }

    @Override
    public String description() {
        return
                "暴徒是豺狼人中体型最庞大，力量最强壮且生命力最顽强的怪物。血量较低时，它们会进入狂暴状态，对敌人造成更高的伤害。";
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( AMMO, ammo );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        ammo = bundle.getInt( AMMO );
    }

}
