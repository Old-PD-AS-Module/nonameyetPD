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

import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.hazards.FellRune;
import com.ravenwolf.nnypd.actors.mobs.npcs.AmbitiousImp;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.sprites.GolemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Golem extends MobHealthy {

    public Golem() {

        super( 16 );

        name = "魔像";
		spriteClass = GolemSprite.class;

        dexterity /= 2;
        armorClass+=tier;
        minDamage += tier/2;
        maxDamage += tier;

        resistances.put(Element.Flame.class, Element.Resist.PARTIAL);
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL);
        resistances.put(Element.Shock.class, Element.Resist.PARTIAL);
        resistances.put(Element.Energy.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
        resistances.put(Element.Body.class, Element.Resist.IMMUNE);

	}

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int armourAC() {
        return buffs( Burning.class ) == null ? super.armourAC() : 0 ;
    }

	@Override
	public float attackSpeed() {
        return 0.75f;
    }

    @Override
    public float moveSpeed() {
        return 0.75f;
    }

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( !blocked && Random.Int( enemy.HT ) < damage *2) {
            BuffActive.addFromDamage( enemy, Dazed.class, damage );
        }
        return damage;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		AmbitiousImp.Quest.process( this );
		
		super.die( cause, dmg );
	}


    private int runeCD = Random.IntRange(1,3);

    @Override
    protected boolean canAttack( Char enemy ) {
        if (runeCD > 0)
            runeCD--;

        return (super.canAttack(enemy) || runeCD <= 0 && Random.Int(3) == 0 && getPosibleRuneTargets(enemy).size() > 0 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos);
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.fellFire(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        runeCD =Random.IntRange(7,11);
        ArrayList<Integer> candidates = getPosibleRuneTargets(enemy);

        int ammountOfRunes=Math.min(4,candidates.size());

        for (int i =0; i<ammountOfRunes; i++) {
            Integer targetCell = candidates.get(Random.Int(candidates.size()));
            candidates.remove(targetCell);

            FellRune rune = new FellRune();
            rune.setValues( targetCell, damageRoll()*2/3, Random.IntRange(6,9) );

            GameScene.add( rune );
            ( (FellRune.RuneSprite) rune.sprite ).appear();
        }

        return true;
    }

    private ArrayList<Integer> getPosibleRuneTargets(Char enemy){

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        for (int i : Level.NEIGHBOURS8) {
            int auxPos=enemy.pos + i;
            if (!Level.solid[auxPos] && !Level.chasm[auxPos] && Actor.findChar(auxPos) ==null && Level.distance(pos,auxPos)>=Level.distance(pos,enemy.pos)) {
                candidates.add( auxPos );
            }
        }
        return candidates;

    }
	
	@Override
	public String description() {
        return
                "矮人们尝试将他们关于机械的知识与新发现的元素力量结合起来。土地之灵作为公认的最容易掌控的元素之灵，被用来当作机械的\"灵魂\"。尽管如此，仪式中最细微的失误都会造成严重的爆炸。";
    }

    private static final String RUNE_CD = "rune_cd";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(RUNE_CD, runeCD);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        runeCD = bundle.getInt(RUNE_CD);
    }
}
