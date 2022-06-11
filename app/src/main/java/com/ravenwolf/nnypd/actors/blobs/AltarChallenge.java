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
package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Journal;
import com.ravenwolf.nnypd.Journal.Feature;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.SpectralGuardian;
import com.ravenwolf.nnypd.actors.mobs.HauntedArmor;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.AltarParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ElmoParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.ShadowParticle;
import com.ravenwolf.nnypd.visuals.ui.TagAttack;
import com.ravenwolf.nnypd.visuals.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class AltarChallenge extends AltarPower {

    private static final String TXT_PROCCED =
//            "Your item has been possessed!.";
            "你的物品已经占据了这里";

    @Override
    protected void evolve() {
        super.evolve();

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.ALTAR_OF_CHALLENGE);
        }
    }

    public Item affectItem( Item item ) {

        Mob mob = null;
        if (item instanceof MeleeWeapon){
            mob = hauntWeapon((MeleeWeapon) item);
        }else if (item instanceof BodyArmor){
            mob = hauntArmor((BodyArmor) item);
        }

        if (mob != null){

            volume = off[pos] = cur[pos] =0;
            GLog.i(TXT_PROCCED);
            Journal.remove(Feature.ALTAR_OF_CHALLENGE);

            mob.state = mob.HUNTING;
            mob.aggro(Dungeon.hero);
            mob.beckon(Dungeon.hero.pos);

            mob.pos = pos;
            CellEmitter.get(pos).burst(ShadowParticle.CURSE, 8);
            Sample.INSTANCE.play(Assets.SND_CURSED, 1, 1, 0.5f);

            GameScene.add(mob);
            Actor.occupyCell(mob);
            TagAttack.target(mob);
        }

        return item;
    }

    public Mob hauntWeapon( MeleeWeapon weap ) {
        weap.enchant();
        weap.upgrade();

        if( Dungeon.hero.belongings.weap1 == weap ) {
            Dungeon.hero.belongings.weap1 = null;
            weap.updateQuickslot();
        } else
            weap.detachAll(Dungeon.hero.belongings.backpack);

        weap.identify(Item.ENCHANT_KNOWN);

        return new SpectralGuardian(weap);
    }

    public Mob hauntArmor( BodyArmor armor ) {
        armor.inscribe();
        armor.upgrade();

        if( Dungeon.hero.belongings.armor == armor ) {
            Dungeon.hero.belongings.weap1 = null;
            armor.updateQuickslot();
        } else
            armor.detachAll(Dungeon.hero.belongings.backpack);

        armor.identify(Item.ENCHANT_KNOWN);

        return new HauntedArmor(armor);
    }

    public WndBag.Mode getBagMode( ) {
        return WndBag.Mode.WEAPON_ARMOR;
    }
/*
    public  String getDescription( ){
        return "Select a weapon or armor to challenge";
    }
*/

    protected boolean affect() {
        Heap heap;
        if ((heap = Dungeon.level.heaps.get( pos )) != null) {
            Item item = heap.peek();
            if (!item.isIdentified()) {
                item.identify();
                volume = off[pos] = cur[pos] -= 1;

                GLog.i(TXT_PROCCED);
                Sample.INSTANCE.play(Assets.SND_SECRET);
                CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);

                if (cur[pos] <= 0) {
                    GLog.i(AltarPower.TXT_NO_MORE_POWER);
                    Journal.remove(Feature.ALTAR_OF_CHALLENGE);
                    return true;
                }
            }
            //throw item away
            int n;
            do {
                n = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Level.passable[n] && !Level.avoid[n]);
            Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
        }
        return false;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);
        emitter.pour( AltarParticle.FACTORY, 0.04f );
    }

    @Override
    public String tileDesc() {
        return
               /* "An ancient spirit is trapped here waiting for a challenger to be free: " +
                "Offer a weapon or armor that will be possessed and empower by the spirit. "
                + "If wou defeat the spirit, you can keep your weapon imbued with the its power."; */
                "一个古老的灵魂被困在这里，等待一个挑战者获得自由：" +
                "提供一件被圣灵附身并赋予力量的武器或盔甲。 " +
                "若你们打败了这家伙，你们的武器将会获得它的力量。";

    }
}
