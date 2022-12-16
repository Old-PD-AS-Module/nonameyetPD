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

package com.ravenwolf.nnypd.actors.buffs.special.skills;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.wands.CharmOfBlink;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.CellSelector;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;

public class SmokeBomb extends BuffSkill {

    {
        CD = 80f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {

            if (target != null && Ballistica.cast(Dungeon.hero.pos, target, false, false)==target) {

                //can't occupy the same cell as another char, so move back one.
                if (Actor.findChar( target ) != null && target != Dungeon.hero.pos)
                    target =  Ballistica.trace[ Ballistica.distance-2 ];

                Hero hero = Dungeon.hero;

                if (Actor.findChar(target) != null || Dungeon.level.solid[target] || !Level.fieldOfView[target]) {
//                    GLog.w("You can only blink to an empty location in your field of view");
                    GLog.w("你只能闪现到视野内的位置");
                    return;
                }

                CellEmitter.get(hero.pos).burst(Speck.factory(Speck.WOOL), 4);
                //new Flare(5, 32, true, true,Flare.NO_ANGLE).color(0xFFFFFF, true).show(hero.pos, 2f);
                for (Mob mob : Dungeon.level.mobs) {
                    if (Level.distance(hero.pos, mob.pos) <= 6 &&
                            mob.pos == Ballistica.cast(hero.pos, mob.pos, false, false)) {
                        BuffActive.add(mob, Blinded.class, 6f);
                    }
                }

                BuffActive.add(hero, Invisibility.class, 6f);
               /* if (hero.subClass == HeroSubClass.FREERUNNER) {
                    BuffActive.add(hero, Adrenaline.class, 6f);
                }
                if (hero.subClass == HeroSubClass.ASSASSIN) {
                    BuffActive.add(hero, Invisibility.class, 6f);
                }
*/

                Buff.detach(hero, Ensnared.class);
                hero.sprite.turnTo(hero.pos, target);
                CharmOfBlink.appear(hero, target);
                CellEmitter.get(hero.pos).burst(Speck.factory(Speck.WOOL), 2);

                SmokeBomb skill = Dungeon.hero.buff(SmokeBomb.class);
                if (skill != null)
                    skill.setCD(skill.getMaxCD());
                hero.busy();
                hero.spendAndNext(Actor.TICK);
            }else {
//                GLog.w("You cant cast that here");
                GLog.w("你不能闪现到那里");
            }

        }

        @Override
//        public String prompt() {
//            return "Choose direction to blink";
//        }
        public String prompt() {
            return "选择闪现的位置";
        }
    };
}
