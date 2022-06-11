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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Chakrams extends ThrowingWeaponLight {

    {
        name = "环刃";
        image = ItemSpriteSheet.CHAKRAM;
        critical=new BladeCritical(this, false, 1.5f);
    }

    //used to determine if break chance roll has to be made
    private boolean hitEnemy;

    public Chakrams() {
        this( 1 );
    }

    public Chakrams(int number) {
        super( 4 );
        quantity = number;
    }

    @Override
    public int baseAmount() {
        return 4;
    }

    @Override
    public int penaltyBase() {
        return 6;
    }


    @Override
    public int str( int bonus ) {
        return super.str(bonus)+1;
    }

/*
    @Override
    public boolean returnOnHit(Char enemy){
        return  true;
    }
*/
    @Override
    public String desc() {
        /*return "This razor-edged missile is made in such curious way that skilled user returns to " +
                "the hands of the thrower on successful hit.";*/
        return "在熟练的人手中，这个边缘带有锋利刀锋的圆盘能够在击中目标后直接返回其手中。"
                + "\n\n这件武器在对付未察觉你的敌人时候更为有效。";

    }

    private int maxRange(){
        return 9;
    }

    public boolean chakramShoot(int target){
        Ballistica.castToMaxDist(curUser.pos, target, maxRange());
        int maxRangePos=Ballistica.trace[Ballistica.distance];
        if (Dungeon.level.solid[maxRangePos])
            maxRangePos=Ballistica.trace[Ballistica.distance-1];

        final int finalPos = maxRangePos;

        if (finalPos == curUser.pos) {
            GLog.i(TXT_SELF_TARGET);
            return false;
        }
        curUser.sprite.cast(finalPos, new Callback() {
            @Override
            public void call() {
                curUser.busy();
                //throw chakram to max range. just visual effect
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(curUser.pos, finalPos, Chakrams.this, null);

                //search targets
                final ArrayList<Integer> targets = new ArrayList<>();
                for (int i=1; i <= Ballistica.distance; i++) {
                    int c = Ballistica.trace[i];
                    Char ch = Actor.findChar( c );
                    if ( ch != null ) {
                        targets.add( ch.pos );
                    }
                }
                if (!targets.contains(finalPos)){
                    targets.add( finalPos );
                }

                hitEnemy = false;
                for (int target : targets) {
                    final int curTarget = target;
                    ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                            reset(curUser.pos, curTarget, ItemSpriteSheet.INVISIBLE, new Callback() {
                                @Override
                                public void call() {
                                    Char ch= Actor.findChar( curTarget);
                                    //hit enemies
                                    if (ch!=null && ch!=curUser)
                                        Chakrams.this.hitEnemy = curUser.shoot(ch, Chakrams.this) || Chakrams.this.hitEnemy;
                                    //if is last target, return and calc break chances
                                    if (curTarget == targets.get(targets.size()-1)){
                                        curUser.spendAndNext(getAttackDelay() /*1/speedFactor( curUser ) */);
                                        curUser.next();
                                        if (! Chakrams.this.hitEnemy || Random.Float() > breakingRateWhenShot()) {
                                            ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                                                    reset(finalPos, curUser.pos, curItem.imageAlt(), null);
                                        }else{
                                            missileBreak(curUser);
                                        }
                                    }
                                }
                            });
                }

                curUser.buff(Satiety.class).decrease((float) str() / curUser.STR());

            }
        });
        return true;
    }


/*
    //Test new logic?
    @Override
    public void onShoot( int cell, final Weapon weapon ) {

        //determine max distance
        Ballistica.castToMaxDist(curUser.pos, cell, 7);
        int finalPos=Ballistica.trace[Ballistica.distance];
        if (Dungeon.level.solid[finalPos])
            finalPos=Ballistica.trace[Ballistica.distance-1];
        hitAll(finalPos,false );

        //visual
        MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
        visual.reset(curUser.pos,
                finalPos,
                this,
                new Callback() {
                    @Override
                    public void call() {
                        curUser.spendAndNext( 1/weapon.speedFactor( curUser ) );
                    }
                });

        if (quantity == 1) {
            doUnequip(curUser, false, false);
        }else
             this.detach(null);
        if( Random.Float() > weapon.breakingRateWhenShot() ) {
            //Buff.append(Dungeon.hero, CircleBack.class).setup(current, finalPos, curUser.pos, Dungeon.depth);
            CircleBack cb=Buff.append(Dungeon.hero, CircleBack.class);
            cb.setup(this, finalPos, curUser.pos, Dungeon.depth);
        } else {
            curUser.sprite.showStatus( CharSprite.DEFAULT, "ammo lost" );
        }
    }

    private int hitAll( final int destPos, final boolean returning){
        final Chakrams chakrams = this;
        for (int i=1; i < Ballistica.distance ; i++) {

            int pos = Ballistica.trace[i];

            final Char ch = Actor.findChar(pos);

            if (returning && ch == curUser) {
                if (this.doPickUp(curUser)) {
                    return pos;
                }
            } else if (ch != null && ch != curUser){
                ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
                        reset( curUser.pos, ch.pos, ItemSpriteSheet.THROWING_DART,1f,null,new Callback() {
                            @Override
                            public void call() {
                                curUser.shoot(ch, chakrams);
                            }
                        });
            }
        }

        if (returning)
            Dungeon.level.drop(this, destPos).sprite.drop();
        return -1;
    }
*/



    public void hitTargetCellAndContinue( int curTargetPos , final int destPos, boolean hit){

        boolean canContinue=true;
        Char ch= Actor.findChar( curTargetPos);
        //hit enemies
        if (ch!=null && ch!=curUser)
            hit = hit || curUser.shoot(ch, this);

        if (canContinue){
            //check if we reach destiny position
            if (curTargetPos==destPos ){
                canContinue=false;
            }

        }

        if (canContinue){
            final boolean hittedTarget=hit;
            //determine next position
            final int nextPos= Ballistica.cast( curTargetPos, destPos, false, true);
            ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).reset(curTargetPos, nextPos, curItem, new Callback() {
                @Override
                public void call() {
                    hitTargetCellAndContinue(nextPos,destPos, hittedTarget);
                }
            });
        }else{
            curUser.spendAndNext( 1/speedFactor( curUser ) );
            curUser.next();
            if (!hit || Random.Float() > breakingRateWhenShot()){
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(destPos, curUser.pos, curItem.imageAlt(), null);
            }else {
                missileBreak(curUser);
            }
        }
    }

    @Override
    public void onShoot( int cell, Weapon weapon ) {
        //returning=false;

        //determine max distance
        /*Ballistica.castToMaxDist(curUser.pos, cell, 8);
        int finalPos=Ballistica.trace[Ballistica.distance];
        if (Dungeon.level.solid[finalPos])
            finalPos=Ballistica.trace[Ballistica.distance-1];*/
         //get first target
        Ballistica.cast(curUser.pos, cell,false,true );
        /*ThrowingWeapon current;
        if (quantity == 1) {
            current = this;
            doUnequip(curUser, false, false);
        }else
            current = (ThrowingWeapon) this.detach(null);
        if( Random.Float() > weapon.breakingRateWhenShot() ) {
            CircleBack cb=Buff.append(Dungeon.hero, CircleBack.class);
            cb.setup(current, finalPos, curUser.pos, Dungeon.depth);
            cb.hitAndContinue(cell, finalPos,false);
        } else {
            curUser.sprite.showStatus( CharSprite.DEFAULT, "ammo lost" );
        }*/
        hitAndContinue(cell, cell);
    }


    private void hitAndContinue(int curPos , final int destPos){

        boolean canContinue=true;
        Char ch= Actor.findChar( curPos);
        //check if we have to collect it
        if (ch!=null && ch!=curUser)
            curUser.shoot(ch, this);

        if (canContinue){
            //check if we reach destiny position
            if (curPos==destPos ){
                canContinue=false;
            }

        }
        if (canContinue){
            //determine next position
            final int nextPos= Ballistica.cast( curPos, destPos, false, true);
            //Cant go forward because a solid object
            if (nextPos==curPos)
                Dungeon.level.drop(this, curPos).sprite.drop();
            else
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).reset(curPos, nextPos, curItem, new Callback() {
                    @Override
                    public void call() {
                        hitAndContinue(nextPos,destPos);
                    }
                });
        }else{
            curUser.spendAndNext( 1/speedFactor( curUser ) );
            curUser.next();
            if (Random.Float() > breakingRateWhenShot()){
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(destPos, curUser.pos, curItem.imageAlt(), null);
            }else {
                missileBreak(curUser);
            }
        }
    }


//    @Override
//    public void onShoot( int cell, Weapon weapon ) {
//        //returning=false;
//
//        //determine max distance
//        Ballistica.castToMaxDist(curUser.pos, cell, 7);
//        int finalPos=Ballistica.trace[Ballistica.distance];
//        if (Dungeon.level.solid[finalPos])
//            finalPos=Ballistica.trace[Ballistica.distance-1];
//         //get first target
//        Ballistica.cast(curUser.pos, finalPos,false,true );
//        //hitAndContinue(cell, finalPos,false);
//        ThrowingWeapon current;
//        if (quantity == 1) {
//            current = this;
//            doUnequip(curUser, false, false);
//        }else
//            current = (ThrowingWeapon) this.detach(null);
//        if( Random.Float() > weapon.breakingRateWhenShot() ) {
//            //Buff.append(Dungeon.hero, CircleBack.class).setup(current, finalPos, curUser.pos, Dungeon.depth);
//            CircleBack cb=Buff.append(Dungeon.hero, CircleBack.class);
//            cb.setup(this, finalPos, curUser.pos, Dungeon.depth);
//            cb.hitAndContinue(cell, finalPos,false);
//        } else {
//            curUser.sprite.showStatus( CharSprite.DEFAULT, "ammo lost" );
//        }
//    }
/*
    private void hitAndContinue(int curPos , final int destPos, final boolean returning){

        boolean canContinue=true;
        Char ch= Actor.findChar( curPos);
        //check if we have to collect it
        if (returning && ch==curUser) {
            if (doPickUp(curUser)){
                //grabbing the chakram takes no time
                //curUser.spend(-1f);
                canContinue=false;
            }
        }else if (ch!=null && ch!=curUser)
            curUser.shoot(ch, Chakrams.this);

        if (canContinue){
            //check if we reach destiny position
            if (curPos==destPos ){
                canContinue=false;
                if (returning)
                    Dungeon.level.drop(this, curPos).sprite.drop();
            }

        }
        if (canContinue){
            //determine next position
            final int nextPos= Ballistica.cast( curPos, destPos, false, true);
            //Cant go forward because a solid object
            if (nextPos==curPos)
                Dungeon.level.drop(this, curPos).sprite.drop();
            else
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).reset(curPos, nextPos, curItem, new Callback() {
                    @Override
                    public void call() {
                        hitAndContinue(nextPos,destPos,returning);
                    }
                });
        }else if (!returning){
            //spend the time when reach destiny pos
            //curUser.spendAndNext(TIME_TO_THROW);
            curUser.spendAndNext( 1/this.speedFactor( curUser ) );
        }
    }
*/
    public static class CircleBack extends Buff {

        private ThrowingWeapon boomerang;
        private int thrownPos;
        private int returnPos;
        private int returnDepth;

        private int left;

        public void setup(ThrowingWeapon boomerang, int thrownPos, int returnPos, int returnDepth) {
            this.boomerang = boomerang;
            this.thrownPos = thrownPos;
            this.returnPos = returnPos;
            this.returnDepth = returnDepth;
            left = 3;
        }

        public int returnPos() {
            return returnPos;
        }

        public ThrowingWeapon cancel() {
            detach();
            return boomerang;
        }


//        @Override
//        public boolean act() {
//            if (returnDepth == Dungeon.depth) {
//                left--;
//                if (left <= 0) {
//                    //boomerang.returning=true;
//                    ((Chakrams)boomerang).quantity=1;
//                    final int nextPos = Ballistica.cast( thrownPos, returnPos, false, true);
//                    MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
//                    visual.reset(thrownPos,
//                            nextPos,
//                            boomerang,
//                            new Callback() {
//                                @Override
//                                public void call() {
//                                    /*((Chakrams)boomerang).*/hitAndContinue(nextPos, returnPos,true);
//                                }
//                            });
//
//                    visual.alpha(0f);
//                    Ballistica.cast(thrownPos, nextPos, false, true);
//                    float duration = Ballistica.distance / 20f;
//                    target.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
//
//                    detach();
//                    //next();
//                    return false;
//                }
//            }
//            spend(TICK);
//            return true;
//        }



    @Override
        public boolean act() {
            if (returnDepth == Dungeon.depth) {
                left--;
                if (left <= 0) {
                    //boomerang.returning=true;
                    final int finalPos = Ballistica.cast( thrownPos, returnPos, false, false);

                    //visual
                    MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
                    visual.reset(thrownPos,
                            curUser.pos,
                            boomerang,
                            new Callback() {
                                @Override
                                public void call() {
                                    next();
                                    boomerang.doPickUp(curUser);
                                }
                            });

                    visual.alpha(0f);
                    float duration = Ballistica.distance / 20f;
                    target.sprite.parent.add(new AlphaTweener(visual, 1f, duration));

                    detach();
                    //next();
                    return false;
                }
            }
            spend(TICK);
            return true;
        }

    private void hitAndContinue(int curPos , final int destPos, final boolean returning){

        boolean canContinue=true;
        Char ch= Actor.findChar( curPos);
        //check if we have to collect it
        if (returning && ch==curUser) {
            if (boomerang.doPickUp(curUser)){
                //grabbing the chakram takes no time
                //curUser.spend(-1f);
                canContinue=false;
            }
        }else if (ch!=null && ch!=curUser)
            curUser.shoot(ch, boomerang);

        if (canContinue){
            //check if we reach destiny position
            if (curPos==destPos ){
                canContinue=false;
                if (returning)
                    Dungeon.level.drop(boomerang, curPos).sprite.drop();
            }

        }
        if (canContinue){
            //determine next position
            final int nextPos= Ballistica.cast( curPos, destPos, false, true);
            //Cant go forward because a solid object
            if (nextPos==curPos)
                Dungeon.level.drop(boomerang, curPos).sprite.drop();
            else
                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).reset(curPos, nextPos, curItem, new Callback() {
                    @Override
                    public void call() {
                        hitAndContinue(nextPos,destPos,returning);
                    }
                });
        }else{
            if (!returning){
                //spend the time when reach destiny pos
                //curUser.spendAndNext(TIME_TO_THROW);
                curUser.spendAndNext( 1/boomerang.speedFactor( curUser ) );
            }else
                next();//finish buff act
        }
    }

        private static final String BOOMERANG = "boomerang";
        private static final String THROWN_POS = "thrown_pos";
        private static final String RETURN_POS = "return_pos";
        private static final String RETURN_DEPTH = "return_depth";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BOOMERANG, boomerang);
            bundle.put(THROWN_POS, thrownPos);
            bundle.put(RETURN_POS, returnPos);
            bundle.put(RETURN_DEPTH, returnDepth);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            boomerang = (ThrowingWeapon) bundle.get(BOOMERANG);
            thrownPos = bundle.getInt(THROWN_POS);
            returnPos = bundle.getInt(RETURN_POS);
            returnDepth = bundle.getInt(RETURN_DEPTH);
        }
    }

}
