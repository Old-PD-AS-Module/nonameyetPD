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

package com.ravenwolf.nnypd.items.weapons.throwing;

import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class MoonGlaive extends ThrowingWeaponLight {

    {
        name = "新月之刃";
        image = ItemSpriteSheet.MOON_GLAIVE;
        critical=new BladeCritical(this, false, 1.5f);
    }

    public MoonGlaive() {
        this( 1 );
    }

    public MoonGlaive(int number) {
        super( 3 );
        quantity = number;
    }

    @Override
    public int baseAmount() {
        return 4;
    }

    @Override
    public int penaltyBase() {
        return 4;
    }

    @Override
    public int str( int bonus ) {
        return super.str(bonus)+1;
    }

    @Override
    public boolean returnOnHit(Char enemy){
        return  true;
    }


    @Override
    protected boolean returnOnMiss(){
        return true;
    }

    @Override
    public String desc() {
        return "一把由三片锋利的刀刃组成的武器。" +
                "在熟练的人手中，甚至可以一次性连续击中多个目标";
    }

    private boolean canBounceTo(Char enemy){
        return (enemy != null && enemy != curUser && !(enemy instanceof NPC) && Level.fieldOfView[enemy.pos]);
    }


    protected boolean bounce(Char enemy, final boolean hit){
        int cell = enemy.pos;
        HashSet<Char> ns = new HashSet<Char>();

        //check possible targets at one range
        for (int i : Level.NEIGHBOURS8) {
            Char n = Actor.findChar(cell + i);
            if (canBounceTo(n)) {
                ns.add(n);
            }
        }
        //check possible targets at two range
        for (int i : Level.NEIGHBOURS16) {
            try {
                Char n = Actor.findChar(cell + i);
                //has path to the enemy (no wall or other mob in between)
                if (canBounceTo(n) && n.pos== Ballistica.cast(cell, n.pos, false, true))
                    ns.add(n);

            }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
        }

        if (ns.size() > 0) {
            final Char newEnemy = Random.element(ns);
            final Weapon weap=this;
            final int enemyPos=newEnemy.pos;
            ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                    reset(cell, newEnemy.pos, curItem.imageAlt(),0.75f, null,new Callback() {
                        @Override
                        public void call() {
                            boolean enemyHit = hit || curUser.shoot(newEnemy, weap);

                            curUser.spendAndNext( getAttackDelay()/*1/weap.speedFactor( curUser )*/ );

                            if ( enemyHit && Random.Float() < breakingRateWhenShot()) {
                                missileBreak(newEnemy);
                            }else {
                                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                                        reset(enemyPos, curUser.pos, curItem.imageAlt(), null);
                            }
                            QuickSlot.refresh();
                        }
                    });

            //Changes the returning cell to the new target
            //if bounce and dont return uncomment the following lines
            //super.onThrow(newEnemy.pos);
            //curUser.spendAndNext( 1/weapon.speedFactor( curUser ) );
            //QuickSlot.refresh();
            return true;
        }
        if (hit && Random.Float() < breakingRateWhenShot())
            missileBreak(enemy);
        return false;

    }
}
