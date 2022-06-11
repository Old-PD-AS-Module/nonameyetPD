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
package com.ravenwolf.nnypd.items.scrolls;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Flare;
import com.ravenwolf.nnypd.visuals.effects.SpellSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTorment extends Scroll {

	{
        name = "灵爆卷轴";
        shortName = "To";

        spellSprite = SpellSprite.SCROLL_MASSHARM;
        spellColour = SpellSprite.COLOUR_DARK;
        icon=10;
	}
	
	@Override
	protected void doRead() {

		int count = 0;

		Mob affected = null;

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {

			if (Level.fieldOfView[mob.pos] ) {

                new Flare( 6, 32 ).color( SpellSprite.COLOUR_DARK, true ).show(mob.sprite, 2f);

                int dmg = 10 + curUser.magicSkill();

                float resist = Element.Resist.getResistance( mob, Element.MIND );

                if ( !Element.Resist.checkIfNegated( resist ) ) {

                    mob.damage(mob.currentHealthValue() * dmg / 100, curUser, Element.MIND);
                    BuffActive.addFromDamage(mob, Tormented.class, dmg);

                    affected = mob;
                    count++;
                }

            }
		}

        int dmg = Math.min( curUser.HP - 1, curUser.HP * ( 190 - curUser.magicSkill() ) / 400 );

        curUser.damage( dmg, curUser, Element.MIND );

        GameScene.flash(SpellSprite.COLOUR_DARK - 0x660000);
        Sample.INSTANCE.play(Assets.SND_FALLING);
        Camera.main.shake(4, 0.3f);

		switch (count) {
            case 0:
                GLog.i( "突然之间你的思维被强大的灵能所吞噬！" );
                break;
            case 1:
                GLog.i( "突然间你和" + affected.name + "的思维被强大的灵能所吞噬!" );
                break;
            default:
                GLog.i( "突然间你和周围生物的意识都被强大的灵能所吞噬!" );
        }



        super.doRead();
	}
	
	@Override
	public String desc() {
        return
                "阅读这张卷轴将会对可视范围内的所有生物造成足以吞噬其意识的疼痛，造成伤害并使它们逃离你。" +
                        "不过使用者同样也会承受强大灵能的反噬，更强魔能属性可以进一步降低反噬的效果。" +
                        "\n\n卷轴造成的伤害取决于阅读者的魔能属性和目标对象的当前生命值。";
    }
	
	@Override
	public int price() {
		return isTypeKnown() ? 60 * quantity : super.price();
	}
}
