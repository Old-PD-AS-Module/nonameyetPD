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
package com.ravenwolf.nnypd.actors.mobs.npcs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.misc.Explosives;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.MobSprite;
import com.ravenwolf.nnypd.visuals.windows.WndVendingMachine;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class VendingMachine extends NPCSupplier {

    private static final String TXT_GREETINGS = "哔...欢迎光临";
    private static final String TXT_CLUNK = "咚！";

    private static final String TXT_BIP = "哔...";
    private static final String TXT_OUT_OF_STOCK = "错误！错误！缺货";

    public Item stuckItem;
    public boolean autoDestroy;
    public boolean food = true;  //true = foods, false = drinks

    private static String[][] LINES = {

            {
                    "未经授权的操作。",
                    "命令无效。",
            },
            {
                    "警告！警告！",
            }
    };


    {
        name = "自动售货机";
        spriteClass = VendingMachine1Sprite.class;
    }

    @Override
    protected Journal.Feature feature() {
        return Journal.Feature.VENDING_MACHINE;
    }

    protected String greetingsText(){
        return TXT_GREETINGS;
    }

    protected String[][] lines(){
        return LINES;
    }

    public CharSprite sprite() {
        if (!food)
            spriteClass = VendingMachine2Sprite.class;
        return super.sprite();
    }

    public static void spawn(Level level, boolean food ){
        VendingMachine npc = new VendingMachine();
        do {
            npc.pos = level.randomRespawnCell();
        } while (npc.pos == -1 || level.heaps.get( npc.pos ) != null || !level.NPCSafePos(npc.pos));

        npc.food = food;
        npc.stock = Random.Int(3)+2;
        level.mobs.add(npc);
        Actor.occupyCell( npc );
    }


    @Override
	protected boolean act() {
        throwItem();
        if (autoDestroy){
            spend( TICK );
            Explosives.explode(pos,Dungeon.chapter() * 14, 1, this);
            CellEmitter.get( pos ).burst(Speck.factory(Speck.WOOL), 10);
            runAway();
            return true;
        }else {

            if (Random.Int(20)==0  && Dungeon.visible[pos])
                yell(TXT_BIP);

            return super.act();
        }

	}

	//override to make it public
    @Override
    public void throwItem() {
        super.throwItem();
    }

	@Override
    public void damage( int dmg, Object src, Element type ) {
        if (stuckItem != null){
            Dungeon.level.drop( stuckItem, pos ).sprite.drop();
            throwItem();
            yell(TXT_CLUNK);
            stuckItem = null;
        }else if (!autoDestroy) {
            if (threatened >= 2 ) {
                autodestroy();
            }else
                react();
        }
	}

    protected void autodestroy() {

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos != pos) {
                mob.beckon( pos );
            }
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center( pos ).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
        }

        Sample.INSTANCE.play( Assets.SND_CHALLENGE );

        autoDestroy=true;
        spend( TICK*2 );
        ((VendingMachineSprite)sprite).charge();
        if (Dungeon.visible[pos]) {
            sprite.showStatus( CharSprite.NEGATIVE, "自动销毁协议激活！" );
        }
    }


	@Override
	public void interact() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if (outOfStock() )
            tell( TXT_OUT_OF_STOCK );
        else
            GameScene.show( new WndVendingMachine( this, food ));
	}

    @Override
    public String description() {
        return
                "一个由矮人制造的自动贩卖机，这些机器本应该被早已被回收为对抗恶魔的战斗机器了。" +
                "你很惊讶这个还可以使用。";
    }

    private static final String ITEM		    = "item";
    private static String AUTODESTROY           = "explode";
    private static String FOOD           = "food";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(ITEM, stuckItem);
        bundle.put( AUTODESTROY, autoDestroy );
        bundle.put( FOOD, food );

    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        stuckItem = (EquipableItem) bundle.get(ITEM);
        autoDestroy = bundle.getBoolean( AUTODESTROY );
        food = bundle.getBoolean( FOOD );
    }


    public static class VendingMachine1Sprite extends VendingMachineSprite {

        @Override
        public void initAnimations() {
            texture(Assets.NPC_VENDING_MACHINE);
            TextureFilm film = new TextureFilm(texture, 16, 14);

            idle = new Animation(10, true);
            idle.frames(film, 1, 1, 1, 1, 1, 0, 0, 0, 0);

            charging = new Animation(12, true);
            charging.frames(film, 0, 0);
        }
    }

    public static class VendingMachine2Sprite extends VendingMachineSprite {

        @Override
        public void initAnimations() {
            texture(Assets.NPC_VENDING_MACHINE);
            TextureFilm film = new TextureFilm(texture, 16, 14);

            idle = new Animation(10, true);
            idle.frames(film, 3, 3, 3, 3, 3, 2, 2, 2, 2);

            charging = new Animation(12, true);
            charging.frames(film, 2, 2);
        }
    }

}


abstract class VendingMachineSprite extends MobSprite {

    private PixelParticle coin;
    protected Animation charging;
    private Emitter chargeParticles;

    public VendingMachineSprite() {
        super();
        initAnimations();
        run = idle.clone();
        die = idle.clone();
        attack = idle.clone();

        idle();
    }

    public abstract void initAnimations();

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        if ( visible && anim == idle ) {

            if (coin != null) {
                coin.remove();
            }

            coin = new PixelParticle();
            parent.add( coin );

            coin.reset( x + 8, y , 0xFFFF00, 1, 0.5f );
            coin.speed.y = -40;
            coin.acc.y = +160;
        }

    }

    public void kill() {
        if (chargeParticles != null)
            chargeParticles.killAndErase();
        super.kill();
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        chargeParticles = centerEmitter();
        chargeParticles.autoKill = false;
        chargeParticles.pour(EnergyParticle.FACTORY, 0.05f);
        chargeParticles.on = false;
        if (ch instanceof VendingMachine && ((VendingMachine)ch).autoDestroy)
            play(charging);
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }

    @Override
    public void update() {
        super.update();
        if (chargeParticles!=null) {
            chargeParticles.pos(center());
            chargeParticles.visible = visible;
        }
    }

    public void charge( ){
        play(charging);
    }

    @Override
    public void play(Animation anim) {
        if (chargeParticles!=null)
            chargeParticles.on = anim == charging;
        super.play(anim);
    }


}

