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
package com.ravenwolf.nnypd.items.misc;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.ConfusionGas;
import com.ravenwolf.nnypd.actors.blobs.CorrosiveGas;
import com.ravenwolf.nnypd.actors.blobs.Explosion;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.BArray;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.ui.QuickSlot;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Explosives extends Item {

    private static final String AC_COMBINE = "组合";
    private static final String AC_SALVAGE = "拆散";

    private static final float TIME_TO_COMBINE = 3.0f;
    private static final float TIME_TO_SALVAGE = 3.0f;

    private static final String TXT_MORE_POWDER_NEEDED = "你没有足够的火药进行制作。";
    private static final String TXT_MORE_BOMBS_NEEDED = "你没有足够的炸弹进行捆绑。";

    private static final String TXT_STICK_MADE = "你制作了一根土质炸药。";
    private static final String TXT_BUNDLE_MADE = "你将炸药合为了一个炸药包。";

    private static final String TXT_POWDER_SALVAGED = "你回收了%s份火药。";
    private static final String TXT_BOMBS_SALVAGED = "你回收了%s个炸药。";

    protected Explosives combineResult( Hero hero ) {
        return null;
    }

    protected Explosives salvageResult( Hero hero ) {
        return null;
    }

    protected Fuse fuse;

    private static final String FUSE = "fuse";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( FUSE, fuse );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains( FUSE ))
            Actor.add( fuse = ((Fuse)bundle.get(FUSE)).ignite(this) );
    }

    public int damage( int strength ) { return strength / 3; }

    public int radius( int strength ) { return strength < 50 ? 0 : strength < 150 ? 1 : strength < 300 ? 2 : strength < 500 ? 3 : 4 ; }

    @Override
    public int priceModifier() { return 2; }

    @Override
    public String quickAction() {
        return AC_THROW;
    }
	
	@Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        actions.add( AC_COMBINE );
        actions.add( AC_SALVAGE );

        return actions;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return fuse != null ? new ItemSprite.Glowing( 0xFF0000, 0.6f) : null;
    }


    @Override
    public boolean doPickUp(Hero hero) {
        if (fuse != null) {
            GLog.w( "你迅速掐灭了炸弹的引线。");
            fuse = null;
        }
        return super.doPickUp(hero);
    }

    @Override
    public void execute( Hero hero, String action ) {

        if (action == AC_SALVAGE) {

            curUser = hero;
            curItem = this;

            Explosives result = salvageResult( curUser );

            if( result != null ) {

                if ( !result.doPickUp( Dungeon.hero )) {
                    Dungeon.level.drop( result, Dungeon.hero.pos ).sprite.drop();
                }

                QuickSlot.refresh();

                Sample.INSTANCE.play(Assets.SND_OPEN, 0.6f, 0.6f, 0.5f);

                curUser.sprite.operate(curUser.pos);

                curUser.spend(TIME_TO_SALVAGE);

                curUser.busy();

            }

        } else if (action == AC_COMBINE) {

            curUser = hero;
            curItem = this;

            Explosives result = combineResult( curUser );

            if( result != null ) {

                if ( !result.doPickUp( Dungeon.hero )) {
                    Dungeon.level.drop( result, Dungeon.hero.pos ).sprite.drop();
                }

                updateQuickslot();

                Sample.INSTANCE.play(Assets.SND_OPEN, 0.6f, 0.6f, 0.5f);

                curUser.sprite.operate(curUser.pos);

                curUser.spend(TIME_TO_COMBINE);

                curUser.busy();

            }

        } else {

            super.execute( hero, action );

        }
    }

    @Override
    protected void onThrow( int cell ) {
        if (this instanceof Gunpowder)
            super.onThrow( cell );
        else {
            if (Level.chasm[cell]) {
                super.onThrow(cell);
            } else {

                Explosives bomb = (Explosives) super.throwItem(cell);

                bomb.fuse = new Fuse();

                Actor.addDelayed(bomb.fuse.ignite(bomb), 2);


//                Explosives bomb = (Explosives)detach(curUser.belongings.backpack);
//
//                if( bomb != null ) {
//
//                    int strength = bomb.price();
//
//                    explode(cell, bomb.damage(strength), bomb.radius(strength), curUser);
//
//                }

            }
        }
    }

    public static void explode( int cell, int damage, int radius, Object source ) {

        PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlockHigh, null ), radius );

        Blob[] blobs = {
                Dungeon.level.blobs.get( Fire.class ),
                Dungeon.level.blobs.get( CorrosiveGas.class ),
                Dungeon.level.blobs.get( ConfusionGas.class ),
        };

        Sample.INSTANCE.play( Assets.SND_BLAST, 1 + radius );
        Camera.main.shake( 3 + radius, 0.2f + radius * 0.1f );

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= ( 4 + 2 * radius ) ) {
                mob.beckon( cell );
            }
        }

        boolean terrainAffected = false;
        for (int c = 0; c < Level.LENGTH; c++ ) {

            if (PathFinder.distance[c] < Integer.MAX_VALUE) {

                int r = PathFinder.distance[ c ];

                terrainAffected = Explosion.affect( c, r, radius, damage, source ) || terrainAffected;

                for (Blob blob : blobs) {

                    if (blob == null) {
                        continue;
                    }

                    int value = blob.cur[c];
                    if (value > 0) {

                        blob.cur[c] = 0;
                        blob.volume -= value;
                    }
                }
            }
        }

        for (int n : Level.NEIGHBOURS9) {

            int c = cell + n;

            if (Level.flammable[c]) {
                Level.set(c, Terrain.EMBERS);
                GameScene.updateMap(c);
                terrainAffected = true;
            }

            Char ch = Actor.findChar(c);

            if( ch != null && !ch.immovable() ) {
                ch.knockBack(cell, damage, radius);
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }
    }

    public static class Gunpowder extends Explosives {

        {
            name = "火药";
            image = ItemSpriteSheet.GUNPOWDER;
            stackable = true;
        }

        @Override
        public Item random() {
            quantity = Random.IntRange( 10, 20 ) * 5;
            return this;
        }


        @Override
        public int price() {
            return quantity;
        }

        @Override
        protected Explosives combineResult( Hero hero ) {

            Gunpowder powder = curUser.belongings.getItem( Explosives.Gunpowder.class );

            if ( powder != null && powder.quantity >= BombStick.powderMax ) {

                if (powder.quantity <= BombStick.powderMax) {

                    powder.detachAll(Dungeon.hero.belongings.backpack);

                } else {

                    powder.quantity -= BombStick.powderMax;

                }

                GLog.i( TXT_STICK_MADE );

                return new BombStick();

            } else {

                GLog.n( TXT_MORE_POWDER_NEEDED );

                return null;

            }
        }

        @Override
        public String info() {
            return
                    "This is a container of black powder. Gunpowder can be used to reload flintlock " +
                    "weapons or to make some makeshift explosives.\n\n" +
                    "You need " + BombStick.powderMax + " portions of gunpowder to create a bomb.";
        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );

            actions.remove( AC_SALVAGE );
            actions.remove( AC_THROW );

            return actions;
        }

        @Override
        public String quickAction() {
            return AC_COMBINE;
        }
    }

    public static class BombStick extends Explosives {

        final protected static int powderMin = 50;
        final protected static int powderMax = 75;

        {
            name = "简易炸药";
            image = ItemSpriteSheet.BOMB_STICK;
            stackable = true;
        }


        @Override
        public String info() {
            return
                    "这是一个填满了黑火药的简易炸药。其引线的独特设计可使其在被点燃的同时扔出。\n\n" +
                            /*"回收该炸药将取回" + powderMin + "-" + powderMax + "份火药。\n" +*/
                            "可以把" + BombBundle.sticksMax + "份" + name + "组合捆绑可制作出一个炸药包" ;
        }

        @Override
        public Item random() {
            quantity = Random.IntRange( 2, 4 );
            return this;
        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );

            actions.remove( AC_SALVAGE );

            return actions;
        }

        @Override
        public int price() {
            return 50 * quantity;
        }

        @Override
        protected Explosives combineResult( Hero hero ) {

            BombStick sticks = curUser.belongings.getItem( BombStick.class );

           if ( sticks != null && sticks.quantity >= BombBundle.sticksMax ) {

               if (sticks.quantity <= BombBundle.sticksMax) {

                   sticks.detachAll(Dungeon.hero.belongings.backpack);

               } else {

                   sticks.quantity -= BombBundle.sticksMax;

               }

               GLog.i( TXT_BUNDLE_MADE );

               return new BombBundle();

            } else {

               GLog.n( TXT_MORE_BOMBS_NEEDED );

               return null;

            }
        }

        @Override
        protected Explosives salvageResult( Hero hero ) {

            Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

            if( bomb != null && bomb.quantity > 0 ) {

                Item powder = new Gunpowder().quantity(Random.IntRange(powderMin, powderMax));

                GLog.i(TXT_POWDER_SALVAGED, powder.quantity);

                return (Gunpowder) powder;
            }

            return null;

        }

    }

    public static class BombBundle extends Explosives {

        final protected static int sticksMin = 2;
        final protected static int sticksMax = 3;

        {
            name = "炸药包";
            image = ItemSpriteSheet.BOMB_BUNDLE;
            stackable = true;
        }

        @Override
        public String info() {
            return
                    "通过多个简易炸药捆绑制成的大号炸弹。制作方法简陋但效力十足。\n\n" +
                            "回收该炸药包将获得" + sticksMin + "-" + sticksMax + "个简易炸药。";
        }

        @Override
        public Item random() {
            quantity = 1;
            return this;
        }
/*
        @Override
        public int price() {
            return 150 * quantity;
        }
*/
        //nerfed bomb bundle damage
        @Override
        public int price() {
            return 125 * quantity;
        }

        @Override
        protected Explosives salvageResult( Hero hero ) {

            Explosives bomb = (Explosives)detach(curUser.belongings.backpack);

            if( bomb != null && bomb.quantity > 0 ) {

                Item sticks = new BombStick().quantity( Random.IntRange( sticksMin, sticksMax ) );

                GLog.i( TXT_BOMBS_SALVAGED, sticks.quantity );

                return (BombStick)sticks;
            }

            return null;

        }

        @Override
        public ArrayList<String> actions( Hero hero ) {
            ArrayList<String> actions = super.actions( hero );

            actions.remove( AC_COMBINE );

            return actions;
        }
    }


    public static class Fuse extends Actor{

        public int actingPriority(){
            return 1; //as if it were a buff
        }


        private Explosives bomb;

        public Fuse ignite(Explosives bomb){
            this.bomb = bomb;
            return this;
        }

        @Override
        protected boolean act() {

            //something caused our bomb to explode early, or be defused. Do nothing.
            if (bomb.fuse != this){
                Actor.remove( this );
                return true;
            }

            //look for our bomb, remove it from its heap, and blow it up.
            for (Heap heap : Dungeon.level.heaps.values()) {
                if (heap.items.contains(bomb)) {
                    heap.items.remove(bomb);
                    int strength = bomb.price();
                    explode(heap.pos,bomb.damage(strength), bomb.radius(strength), curUser);

                    Actor.remove(this);
                    return true;
                }
            }

            //can't find our bomb, something must have removed it, do nothing.
            bomb.fuse = null;
            Actor.remove( this );
            return true;
        }
    }

}
