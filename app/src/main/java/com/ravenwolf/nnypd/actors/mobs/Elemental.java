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
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.ElementalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Elemental extends MobPrecise {

    private static String TXT_ENSNARED = "元素轻易挣脱了缠绕！";
    private static String TXT_FROZEN = "冰霜严重的伤害了它";

    private int rangedCD = 0;

    public Elemental() {

        super( 14 );

        name = "火元素";
        spriteClass = ElementalSprite.class;

        flying = true;
        armorClass = 0;

        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Frost.class, Element.Resist.VULNERABLE );

    }

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isEthereal() {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.FLAME;
    }

    @Override
    public int damageRoll() {
        return super.damageRoll() * 3/4 ;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || rangedCD==0 && Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    public boolean act() {
        if (rangedCD >0)
            rangedCD--;
        return super.act();
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.fire(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        rangedCD = Random.Int(4, 6);
        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {
        return castBolt(enemy,damageRoll(),false,Element.FLAME);
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if ( type == Element.FLAME ) {

            if (HP < HT) {
                int reg = Math.min( dmg / 2, HT - HP );

                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                }
            }
        } else {
            super.damage(dmg, src, type);
        }
    }
	
	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Burning) {
			if (HP < HT) {
                int reg = Math.max( 1, (HT - HP) / 5 );

                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), (int)Math.sqrt( reg ) );
                }
			}

            return false;

		} else if (buff instanceof Chilled) {

            damage( Random.NormalIntRange( HT / 4 , HT/2 ), null, null );

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_FROZEN );
            }

            return false;

        } else if (buff instanceof Ensnared) {

            GameScene.add(Blob.seed(pos, 1, Fire.class));

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_ENSNARED );
            }

            return false;

        } else {

            return super.add( buff );

        }
	}

    @Override
    public void die( Object cause, Element dmg ) {

        if (Level.flammable[pos]) {
            GameScene.add(Blob.seed(pos, 1, Fire.class));
        }

        super.die( cause, dmg );
    }
	
	@Override
	public String description() {
        return
                "火元素是一种召唤强大存在时出现的副产物，这些元素的本质太过混乱，即使是强大的恶魔术士也不能将其完全控制。";
    }

    private static final String RANGED_CD = "rangedCD";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(RANGED_CD, rangedCD);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        rangedCD = bundle.getInt(RANGED_CD);
    }

}
