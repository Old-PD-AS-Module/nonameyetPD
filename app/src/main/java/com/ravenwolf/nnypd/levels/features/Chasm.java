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
package com.ravenwolf.nnypd.levels.features;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Levitation;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.SpectralGuardian;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.potions.Potion;
import com.ravenwolf.nnypd.items.potions.PotionOfLevitation;
import com.ravenwolf.nnypd.levels.RegularLevel;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.scenes.InterlevelScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.MobSprite;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Chasm {

    // FIXME
    private static Chasm CHASM = new Chasm();

    private static final String TXT_CHASM	= "虚空";
    private static final String TXT_YES		= "是的，我知道自己在做什么";
    private static final String TXT_NO		= "不，我改变主意了";
    private static final String TXT_POTION	= "使用浮空药剂";
    private static final String TXT_JUMP 	=
            "你确定要跳下虚空吗？你可能会因此而死亡。";

    private static final String TXT_LANDS_SAFELY = "你安全地降落在地面上！";
    private static final String TXT_SHATTER_PACK = "你的%s被虚空的压力摧毁了！";

	public static boolean jumpConfirmed = false;
	public static boolean useLevitation = false;

	public static void heroJump( final Hero hero ) {

		GameScene.show(

            ( Potion.getKnown().contains(PotionOfLevitation.class) && hero.belongings.getItem(PotionOfLevitation.class) != null ?

                new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_POTION, TXT_NO) {

                    @Override
                    protected void onSelect(int index) {
                        if (index < 2) {

                            jumpConfirmed = true;

                            if (index == 1) {
                                useLevitation = true;
                            }

                            hero.resume();
                        }
                    }
                }

                : new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO) {

                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {

                            jumpConfirmed = true;

                            hero.resume();

                        }
                    }
                }
            )
        );
	}
	
	public static void heroFall( int pos ) {
		
		jumpConfirmed = false;

        if( !useLevitation ) {
            if( Dungeon.hero.heroClass == HeroClass.ACOLYTE) {
                Sample.INSTANCE.play(Assets.SND_FALLING, 1.0f, 1.0f, 1.2f);
            } else {
                Sample.INSTANCE.play(Assets.SND_FALLING);
            }
        }
		
		if (Dungeon.hero.isAlive()) {
			Dungeon.hero.interrupt();
			InterlevelScene.mode = InterlevelScene.Mode.FALL;
			if (Dungeon.level instanceof RegularLevel) {
				Room room = ((RegularLevel)Dungeon.level).room( pos );
				InterlevelScene.fallIntoPit = room != null && room.type == Room.Type.WEAK_FLOOR;
			} else {
				InterlevelScene.fallIntoPit = false;
			}
			Game.switchScene( InterlevelScene.class );
		} else {
			Dungeon.hero.sprite.visible = false;
		}
	}
	
	public static void heroLand() {
		
		Hero hero = Dungeon.hero;

        if( useLevitation ) {

            BuffActive.add( hero, Levitation.class, Math.round( Random.Float(PotionOfLevitation.DURATION / 5, PotionOfLevitation.DURATION / 4 ) ));
            GLog.p(TXT_LANDS_SAFELY );
            useLevitation = false;

        } else {

            hero.sprite.burst(hero.sprite.blood(), 10);
            Camera.main.shake(4, 0.2f);

            Item item = hero.belongings.randomUnequipped();
            if (item instanceof Potion) {

                item = item.detach(hero.belongings.backpack);
                GLog.w(TXT_SHATTER_PACK, item.toString());
//            Sample.INSTANCE.play(Assets.SND_SHATTER);
                ((Potion) item).shatter(hero.pos);

            }

            Heap heap = Dungeon.level.heaps.get(hero.pos);
            if (heap != null) {
                heap.shatter( "虚空的压力" );
            }

            int dmg;

            if (hero.belongings.armor != null) {
                dmg = hero.HT * hero.belongings.armor.str() / hero.STR();
            } else {
                dmg = hero.HT * 5 / hero.STR();
            }

            if ( hero.isAlive()) {
                hero.damage(hero.absorb(dmg), CHASM, Element.FALLING);
            } else {
                hero.die(CHASM, Element.FALLING);
            }

        }
	}

	public static void mobFall( Mob mob ) {
	    if (mob instanceof SpectralGuardian){
            ((SpectralGuardian)mob).blink();
        }
	    else {
            mob.destroy();
            ((MobSprite) mob.sprite).fall();
        }
	}
}
