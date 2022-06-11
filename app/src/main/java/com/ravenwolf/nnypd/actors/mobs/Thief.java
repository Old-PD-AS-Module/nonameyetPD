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
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.actors.mobs.npcs.Ghost;
import com.ravenwolf.nnypd.items.misc.Gold;
import com.ravenwolf.nnypd.items.weapons.throwing.Knives;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.sprites.ThiefSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Thief extends MobPrecise {


    private int ammo=3;

    private static final String AMMO = "ammo";

    public Thief() {

        super( 2 );

        name = "窃贼";
        spriteClass = ThiefSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;
	}

    public String getTribe() {
        return TRIBE_LONER;
    }

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() *4/5 : super.damageRoll();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        /*return super.canAttack( enemy ) || HP >= HT && Level.distance(pos, enemy.pos) <= 2
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );*/
        return super.canAttack( enemy ) || ammo > 0 && Level.distance(pos, enemy.pos) <= 3
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Knives(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });
        ammo--;
        super.onRangedAttack( cell );
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }

    @Override
    public String description(){

        return "这个下水道一直都是各种歹徒和罪犯的藏身之地。"

                + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                "他们通常装备各种不同的匕首和小刀，总是依靠的是肮脏的伎俩来袭击敌人，而不是依靠技巧和力量。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                "与他们打交道时最好谨慎一点，在他们贪婪的目光中，一个独行的老头子看起来就像个容易干掉的猎物。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                "看来是时候让这些菜鸟的见识一下谁才是这里的老大了，毕竟这些'不义之财'看起来可以帮助你完成那'崇高的追求'，不是么？" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                "是什么让他们走上了这条不归之路？贪婪还是不幸？抑或更邪恶的东西？不过，现在已经不重要了。" : "" )

                ;
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
