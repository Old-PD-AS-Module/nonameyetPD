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

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Enraged;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.keys.GoldenKey;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.DM300Sprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM300 extends MobHealthy {

    protected int breaks = 0;

    public DM300() {

        super( 4, 20, true );

        // 450

        dexterity /= 2; // yes, we divide it again
        armorClass *= 2; // and yes, we multiply it back

        name = Dungeon.depth == Statistics.deepestFloor ? "DM-300" : "DM-400";
        spriteClass = DM300Sprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put(Element.Body.class, Element.Resist.IMMUNE );
        resistances.put(Element.Dispel.class, Element.Resist.IMMUNE );
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
    public float awareness(){
        return 1.0f;
    }

    @Override
    public float moveSpeed() {
        return buff( Enraged.class ) != null ? 1.0f : 0.75f;
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }
	
	@Override
	public void move( int step ) {
		super.move( step );

        if( buff( Enraged.class ) != null ) {

            dropBoulders( step + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ], damageRoll() / 2 );

            dropBoulders( step + Level.NEIGHBOURS12[ Random.Int( Level.NEIGHBOURS12.length ) ], damageRoll() / 3 );

            Camera.main.shake( 2, 0.1f );

        } else if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP /*&& HP < HT*/) {

            if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
                GLog.n( "DM-300修复了自己！" );
                sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
            }
            BuffActive.add(this, Shielding.class,  Random.IntRange(6, 8));
		}

	}

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i("DM-300停止了狂暴。");
        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            breaks++;

            BuffActive.add(this, Enraged.class, breaks * Random.Float(8.0f, 12.0f));

            if (Dungeon.visible[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, "狂暴!" );
                GLog.n( "DM-300被激怒了！" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        }

        return super.act();
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		
		super.die( cause, dmg );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new GoldenKey(), pos ).sprite.drop();
		
		Badges.validateBossSlain();

        yell( "任务失败，系统关闭。" );
	}
	
	@Override
	public void notice() {
		super.notice();
        if( enemySeen ) {
            yell("检测到未经授权的人员。");
        }
	}
	
	@Override
	public String description() {
        return
                "数个世纪前矮人们制造了这个机器。但此后，矮人们开始使用魔像、元素生物甚至是恶魔来替换机器，最终导致其文明的衰败。DM-300及类似的机器通常用于建设和挖掘，某些情况下，也可以用于城防。";
    }

    public void dropBoulders( int pos, int power ) {

        if( pos < 0 || pos >= Level.LENGTH )
            return;

        if( Level.solid[pos] )
            return;

        Char ch = Actor.findChar(pos);
        if (ch != null) {

            int dmg = ch.absorb( Random.IntRange( power / 2 , power ) );
//                    int dmg = Math.max(0, Random.IntRange(Dungeon.depth, Dungeon.depth + 10) - Random.NormalIntRange(0, ch.armorClass()));

            ch.damage(dmg, this, Element.PHYSICAL);

            if (ch.isAlive() ) {
                BuffActive.addFromDamage(ch, Dazed.class, dmg);
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.shatter( "落石" );
        }

        if (Dungeon.visible[pos]) {

            CellEmitter.get(pos).start( Speck.factory(Speck.ROCK), 0.1f, 4 );

        }
    }

    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
    }
}
