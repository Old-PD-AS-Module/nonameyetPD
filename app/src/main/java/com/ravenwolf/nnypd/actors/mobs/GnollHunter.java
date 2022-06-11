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
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.actors.mobs.npcs.Ghost;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.items.weapons.throwing.BluntedArrows;
import com.ravenwolf.nnypd.items.weapons.throwing.Javelins;
import com.ravenwolf.nnypd.items.weapons.throwing.Quarrels;
import com.ravenwolf.nnypd.visuals.sprites.GnollSprite;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GnollHunter extends MobRanged {

    public GnollHunter() {

        super( Dungeon.chapter()*3/*3*/ );

        name = "豺狼猎手";
		spriteClass = GnollSprite.class;

        switch( Dungeon.chapter() ) {
            case 1:
                loot = Random.oneOf( Arrows.class, Arrows.class, Arrows.class, BluntedArrows.class);
                lootChance = 0.3f;
                break;
            case 2:
                loot = Random.oneOf(Arrows.class,Arrows.class,BluntedArrows.class,Quarrels.class);
                lootChance = 0.4f;
                break;
            case 3:
                loot = Random.oneOf(BluntedArrows.class,Arrows.class,Arrows.class,Quarrels.class,Quarrels.class,Quarrels.class);
                lootChance = 0.5f;
                break;
            default:
                loot = Random.oneOf(BluntedArrows.class,Arrows.class,Quarrels.class);
                lootChance = 0.5f;
                break;
        }

	}

    @Override
    public String getTribe() {
        return TRIBE_GNOLL;
    }

//    @Override
//    public int attackProc( Char enemy, int damage ) {
//
//        if ( distance(enemy) > 1 && Random.Int( enemy.HT ) < damage ) {
//            Buff.affect( enemy, Poison.class ).set(Random.IntRange( damage / 2 , damage ));
//            enemy.sprite.burst( 0x00AAAA, 5 );
//        }
//
//        return damage;
//    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
            reset(pos, cell, new Javelins(), new Callback() {
                @Override
                public void call() {
                    onAttackComplete();
                }
            });

        super.onRangedAttack( cell );
    }

//    @Override
//    public void damage( int dmg, Object src, Element type ) {
//        super.damage(dmg, src, type);
//
//        if ( isAlive() && src != null && HP >= HT / 2 && HP + dmg < HT / 2 ) {
//
//            state = FLEEING;
//
//            if (Dungeon.visible[pos]) {
//                sprite.showStatus(CharSprite.NEGATIVE, "fleeing");
////                spend( TICK );
//            }
//
//        }
//    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {
//		return
//			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
//			"Gnoll hunters are regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.";

        return "豺狼人是有着土狼模样的人形生物。"

                + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                "这一只豺狼人看起来像是猎人之类的，它们所使用的木棍并不能对你造成太大威胁。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                "说来奇怪，它们很少被发现在人类的居住点附近，反而是更喜欢在荒郊野外游荡。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                "这就是关于这个生物的的一切了，不论如何，谁会去在意这些呢？" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                "它似乎与周围的野兽和地下的强盗们达成了结盟，也许，又或者是...领导它们？！" : "" )

                ;
    }

}
