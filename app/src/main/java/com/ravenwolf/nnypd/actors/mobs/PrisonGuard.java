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
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Stun;
import com.ravenwolf.nnypd.items.weapons.throwing.Bolas;
import com.ravenwolf.nnypd.items.weapons.throwing.Chakrams;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.sprites.PrisonKeeperSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PrisonGuard extends MobHealthy {

    private int netCooldown=Random.IntRange(2, 5);
    private MissileSprite netSprite;
    private boolean charged;
    private static final String NET_CD = "netCooldown";
    private static final String CHARGED = "charged";

    public PrisonGuard() {

        super( 7 );

        name = "监狱守卫";
		spriteClass = PrisonKeeperSprite.class;

        armorClass -= tier;

        minDamage -= tier;
        maxDamage -= tier*2;
        accuracy += tier*2;
	}

    @Override
    public float awareness(){
        return super.awareness() * ( 1.0f + tier * 0.05f );
    }

	@Override
	public String description() {
        return
                "作为曾经监狱的管理者，这些守卫和那些罪犯已经没什么区别了。它们蹒跚的步伐如同僵尸一般，毫无思绪地四处寻找不属于这片区域的生物，比如你！\n" +
                        "他们被不洁魔法污染的身体仍然保留他们以前的特性和技能，会优先使用远程和控制性武器。\n" ;
        //"\\nThey carry chains around their hip, possibly used to immobilize enemies. \n";
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();
        if (charged && netSprite == null){
            chargeNet();
        }
    }

    private void chargeNet(){

        ((PrisonKeeperSprite)sprite).chargeNet( pos, enemy==null?pos:enemy.pos);
        netSprite =(MissileSprite)sprite.parent.recycle( MissileSprite.class );

        int speed=540*(sprite.flipHorizontal?1:-1);
        netSprite.spin(pos, ItemSpriteSheet.HUNTING_BOLAS,speed,null);
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !charged && canThrowNet()) {
            charged = true;
            chargeNet();
            spend( TICK);
            return true;
        } else {
            resetNet();
            return super.doAttack( enemy );
        }
    }

    @Override
    public void die( Object cause, Element dmg ) {
        super.die(cause, dmg);
        resetNet();
    }

    private void resetNet(){
        charged = false;
        if (netSprite !=null) {
            netSprite.kill();
            netSprite =null;
        }

    }

    @Override
    public boolean add( Buff buff ) {
        if (buff instanceof Stun || buff instanceof Frozen)
            resetNet();
        return  super.add(buff);
    }
    @Override
    public void knockBack(int sourcePos, int damage, int amount, final Callback callback) {
        resetNet();
        super.knockBack(sourcePos,damage,amount,callback);
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

        boolean act=super.act();
        if (charged && !enemySeen) {
            //if cannot throw net, wait some turns before attempting to use it again
            netCooldown=Random.IntRange(2, 4);
            resetNet();
            sprite.idle();
        }
        return act;
    }

    public void activate() {
        state = HUNTING;
        enemySeen = true;
        yell( "我..必须..保护好...这些门..." );
        spend( TICK );
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {
        if (state == PASSIVE) {
            notice();
            state = HUNTING;
        }
        super.damage( dmg, src, type );
    }

    @Override
    public void beckon( int cell ) {
        if (state != PASSIVE) {
            super.beckon(cell);
        }
        // do nothing
    }

    @Override
    protected boolean canAttack( Char enemy ) {

        netCooldown--;
        return super.canAttack( enemy ) || (Level.distance(pos, enemy.pos) <= 3 || canThrowNet())
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    private boolean canThrowNet(){
        return netCooldown<=0 && !Level.adjacent( pos, enemy.pos ) &&
                enemy.buffs(Ensnared.class)!=null && Level.distance(pos, enemy.pos) <= 6;
    }

    @Override
    protected void onRangedAttack(final int cell ) {
         if (canThrowNet()) {
            netCooldown=Random.IntRange(10, 16);
            ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                    reset(pos, cell, new Bolas(), new Callback() {
                        @Override
                        public void call() {
                            BuffActive.add( enemy, Ensnared.class, 4 );
                            next();
                        }
                    });
        }else{
            ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                reset(pos, cell, new Chakrams(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                        ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                                reset(cell, pos, new Chakrams(), null);
                    }
                });
        }
        super.onRangedAttack( cell );
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( NET_CD, netCooldown );
        bundle.put( CHARGED, charged );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        netCooldown = bundle.getInt( NET_CD );
        charged=bundle.getBoolean(CHARGED);
    }

}
