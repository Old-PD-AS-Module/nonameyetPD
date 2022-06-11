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
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.bonuses.Adrenaline;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.BlastWave;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.particles.ShaftParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;


public class ValkyrieSkill extends BuffSkill {

    {
        CD = 80f;
    }

    @Override
    public void doAction(){
        Hero hero=Dungeon.hero;
        int cell=hero.pos;

        Camera.main.shake( 3, 0.1f );
        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
        if( Dungeon.visible[ cell ] ){
            BlastWave.createAtPos( cell );
        }
        //first knock enemies at two range
        for (int i : Level.NEIGHBOURS16) {
            try {
                Char n = Actor.findChar(cell + i);
                //has path to the enemy (no wall or other mob in between)
                if ( n!=null && !(n instanceof NPC) && n.pos== Ballistica.cast(cell, n.pos, false, false)) {
                    n.knockBack(cell, (int) Math.sqrt(n.totalHealthValue() )*3, 2);
                    n.delay(Actor.TICK);
                }

            }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
        }

        Char n;
        for (int i : Level.NEIGHBOURS8) {
            n = Actor.findChar(cell + i);
            if (n!=null && !(n instanceof NPC)) {
                n.knockBack(cell, (int)Math.sqrt( n.totalHealthValue())*3, 3);
                n.delay(Actor.TICK);
            }
        }
        Camera.main.shake(2, 0.5f);
        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
        BlastWave.createAtPos( cell );

        //BuffActive.add( hero, Levitation.class, 25);
        //BuffActive.add( hero, Shielding.class, 25);
        //if (hero.subClass == HeroSubClass.SNIPER)
            BuffActive.add( hero, Adrenaline.class, 8);
        /*if (hero.subClass == HeroSubClass.WARDEN) {
            BuffActive.add(hero, Shielding.class, 20);
            if (hero.belongings.weap2 instanceof Shield) {
                BuffActive.add(hero, Guard.class, 21, true);
            }else if (hero.belongings.weap1 instanceof MeleeWeapon || hero.belongings.weap2 instanceof MeleeWeapon ) {
                BuffActive.add(hero, Guard.class, 21, true);
            }
        }*/
        CellEmitter.center(hero.pos).burst(ShaftParticle.FACTORY, 4);
        Sample.INSTANCE.play(Assets.SND_EVOKE,1,1,0.5f);
        Sample.INSTANCE.play(Assets.SND_TELEPORT,1,1,1.5f);

        Dungeon.hero.spendAndNext(1f);
        setCD(getMaxCD());
    }
}
