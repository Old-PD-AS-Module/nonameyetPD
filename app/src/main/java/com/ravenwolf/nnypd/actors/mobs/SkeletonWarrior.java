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
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.sprites.SkeletonWarriorSprite;
import com.watabou.utils.Bundle;

public class SkeletonWarrior extends MobPrecise {

    public MeleeWeapon weap;

    public SkeletonWarrior() {

        super( 8 );

        name = "骷髅骑士";
        spriteClass = SkeletonWarriorSprite.class;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

        Weapon weapTmp =null;
        while (!(weapTmp instanceof MeleeWeapon) ){
            weapTmp = (Weapon) Generator.random( Generator.Category.WEAPON );
            if ( /*weapTmp.cursed ||*/ weapTmp.lootChapter()>2)
                weapTmp=null;
        }

        weap=(MeleeWeapon)weapTmp;
        weap.identify(Item.ENCHANT_KNOWN);
        minDamage=weap.min();
        maxDamage=weap.max();

        //accuracy is affected by weapon penalty
        accuracy = (int)((accuracy+ tier) * (1- weap.penaltyBase()*0.025f));

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
    public float attackSpeed() {
        return super.attackSpeed() *  weap.speedFactor(this);
    }

    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy) || Level.distance( pos, enemy.pos ) <=meleeAttackRange()  &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    public String getTribe() {
        return TRIBE_UNDEAD;
    }

    public boolean isRanged() {
        return false;
    }

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
                "骷髅战士是从坟墓中被邪恶的力量所召唤出来的，这是一个强大的怪物，" +
                        "你能看到它的手上正拿着_"+weap.name()+"_。";
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
    }

}
