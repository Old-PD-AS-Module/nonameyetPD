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
import com.ravenwolf.nnypd.DungeonTilemap;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.DeathRay;
import com.ravenwolf.nnypd.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.RobotSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Robot extends MobRanged {

    public boolean autoDestroy;
    public Robot() {

        super( 11 );

        name = "机械哨兵";
		spriteClass = RobotSprite.class;

        resistances.put(Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put(Element.Body.class, Element.Resist.IMMUNE );
        resistances.put(Element.Dispel.class, Element.Resist.IMMUNE );
	}

    public String getTribe() {
        return TRIBE_LONER;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return Ballistica.castToMaxDist( pos, enemy.pos, 5);
    }

    //FIXME copied base method and remove the adjacent check
    protected boolean doAttack( Char enemy ) {
        final int enemyPos = enemy.pos;
        boolean visible = Dungeon.visible[pos] || Dungeon.visible[enemyPos];

        if (visible) {

            Dungeon.visible[pos] = true;
            sprite.cast(enemyPos, new Callback() {
                @Override
                public void call() {
                    onRangedAttack(enemyPos);
                }
            });

        } else {

            cast(enemy);

        }

        if (enemy == Dungeon.hero) {
            noticed = true;
        }

        spend(attackDelay());
        return !visible;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        cell=Ballistica.trace[Ballistica.distance];

        Sample.INSTANCE.play(Assets.SND_RAY);

        PointF robotEye=sprite.center();
        robotEye.y-=1;
        sprite.parent.add( new DeathRay( robotEye, DungeonTilemap.tileCenterToWorld( cell ) ) );

        onCastComplete();

        super.onRangedAttack( cell );

    }

    @Override
    public boolean cast( Char enemy ) {

        boolean terrainAffected = false;

        for (int i=1; i <= Ballistica.distance ; i++) {

            int pos = Ballistica.trace[i];

            int terr = Dungeon.level.map[pos];

            if (terr == Terrain.DOOR_CLOSED) {

                Level.set(pos, Terrain.EMBERS);
                GameScene.updateMap(pos);
                terrainAffected = true;

            } else if (terr == Terrain.HIGH_GRASS) {

                Level.set( pos, Terrain.GRASS );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

            Char ch = Actor.findChar( pos );

            if (ch != null) {

                if (castBolt(ch , damageRoll(), false, Element.ENERGY  )){
                    if (Dungeon.visible[pos]) {
                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                    }

                } else {
                    enemy.missed();
                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        return true;
    }


    @Override
    public void damage( int dmg, Object src, Element type ) {

        super.damage( dmg, src, type );
        if (state == PASSIVE) {
            notice();
            state = HUNTING;
        }

        if (  isAlive() && enemy!=null && Level.adjacent(enemy.pos,pos) && !autoDestroy && HP < HT / 3 ) {
            autoDestroy=true;
            spend( TICK*2 );
            ((RobotSprite)sprite).charge( enemy.pos );
            if (Dungeon.visible[pos]) {
                Sample.INSTANCE.play(Assets.SND_BEACON,0.5f);
                sprite.showStatus( CharSprite.NEGATIVE, "自动销毁协议激活！" );
            }
        }
    }

    @Override
    public int armorClass() {
        return autoDestroy ? super.armorClass()*2 : super.armorClass();
    }

    @Override
    public boolean act() {
        //passive state is only for subchapters key guardian
        if( state == PASSIVE ) {
            if ( enemy != null && Dungeon.visible[pos] && Level.distance(pos,enemy.pos) <= enemy.viewDistance() &&
                    enemy.pos== Ballistica.cast(pos, enemy.pos, false, true)){
                activate();
                return true;
            }
        }

        if (autoDestroy){

            spend( TICK );
            die( this, null );

            sprite.kill();
            Explosives.explode(pos,damageRoll()*3, 1, this);
            return true;
        }else
            return super.act();
    }


    public void activate() {
        state = HUNTING;
        enemySeen = true;
        if( enemySeen && Dungeon.depth == Dungeon.CAVES_PATHWAY) {
            Sample.INSTANCE.play(Assets.SND_ALERT,0.5f);
            yell( "检测到未经授权人员！" );
        }
        spend( TICK );
    }


    @Override
    public void beckon( int cell ) {
        if (state != PASSIVE) {
            super.beckon(cell);
        }
        // do nothing
    }

	
	@Override
	public String description() {
        return
                "矮人在几个世纪前创造了这样的机器，用来保护它们的矿山免受外部威胁。" +
                        "然而现在这些破旧的机器还在这个废弃的矿洞中游荡，试图达到它们的目的。";
    }


	private static String AUTODESTROY= "AUTO";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( AUTODESTROY, autoDestroy );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        autoDestroy = bundle.getBoolean( AUTODESTROY );

    }
}
