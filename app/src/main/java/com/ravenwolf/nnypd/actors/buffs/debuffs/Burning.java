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
package com.ravenwolf.nnypd.actors.buffs.debuffs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.food.ChargrilledMeat;
import com.ravenwolf.nnypd.items.food.MysteryMeat;
import com.ravenwolf.nnypd.items.herbs.Herb;
import com.ravenwolf.nnypd.items.scrolls.Scroll;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Debuff {

//	private static final String TXT_BURNS_UP		= "%s burns up!";
    private static final String TXT_BURNS_UP		= "%s被烧毁了！";

	//power represents the time that the buff is attached on the target
    //scrolls require at least 3 power to be burn down
    private int power=0;

    @Override
    public Element buffType() {
        return Element.FLAME;
    }

/*    @Override
    public String toString() {
        return "Burning";
    }

    @Override
    public String statusMessage() { return "burning"; }

    @Override
    public String playerMessage() { return "You catch fire! Quickly, run to the water!"; }*/
    @Override
    public String toString() {
        return "燃烧";
    }

    @Override
    public String statusMessage() { return "燃烧"; }

    @Override
    public String playerMessage() { return "你身上着火了！快去找水！"; }

    @Override
    public int icon() {
        return BuffIndicator.BURNING;
    }

    @Override
    public void applyVisual() {

        if (target.sprite.visible) {
            Sample.INSTANCE.play( Assets.SND_BURNING );
        }

        target.sprite.add( CharSprite.State.BURNING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BURNING );
    }

    @Override
/*    public String description() {
        return "It really burns! While burning, you constantly receive damage and can lose some of " +
                "the flammable items in your inventory. Also enemies are more likely to notice you.";
    }*/
    public String description() {
        return "火焰覆盖了你的身体! 在燃烧的状态下你会持续受到伤害，并且它还可能会烧毁你身上的易燃物品，敌人同样也更容易发现你";
    }
	
	@Override
	public boolean act() {
        Invisibility.dispel(target);
        target.damage(
                Random.Int( (int)Math.sqrt(
                        target.totalHealthValue() * 1.5f
                ) ) + 1, this, Element.FLAME_PERIODIC
        );

        Blob blob = Dungeon.level.blobs.get( Burning.class );
//            Blob blob2 = Dungeon.level.blobs.get( Miasma.class );

        if (Level.flammable[target.pos] && ( blob == null || blob.cur[ target.pos ] <= 0 )) {
//            if (Level.flammable[target.pos] || blob1 != null && blob1.cur[target.pos] > 0 || blob2 != null && blob2.cur[target.pos] > 0) {
            GameScene.add(Blob.seed(target.pos, 1, Fire.class));
        }

        if (power > 0 && target instanceof Hero ) {

            Item item = ((Hero) target).belongings.randomUnequipped();

            if (item instanceof Scroll && power > 2 || item instanceof Herb) {

                item = item.detach(((Hero) target).belongings.backpack);
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            } else if (item instanceof MysteryMeat) {

                item = item.detach(((Hero) target).belongings.backpack);
                ChargrilledMeat steak = new ChargrilledMeat();
                if (!steak.collect(((Hero) target).belongings.backpack)) {
                    Dungeon.level.drop(steak, target.pos).sprite.drop();
                }
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            }
        }

        if ( !target.isAlive() || Level.water[ target.pos ] && !target.flying ) {
            detach();
            return true;
        }

        power++;

		return super.act();
	}

    @Override
    public boolean attachTo( Char target ) {

        if (super.attachTo( target )) {

            Invisibility.dispel(target);
            Buff.detach( target, Ensnared.class );
            Buff.detach( target, Chilled.class );

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put( "POWER" , power );
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        power = bundle.getInt("POWER");
        super.restoreFromBundle( bundle );
    }

}
