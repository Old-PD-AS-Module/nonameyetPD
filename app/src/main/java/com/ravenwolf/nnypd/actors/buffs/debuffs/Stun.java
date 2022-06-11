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

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.special.Guard;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.ui.BuffIndicator;

public class Stun extends Debuff {

/*	@Override
	public  Element buffType() {
		return Element.MIND;
	}
*/
/*	@Override
	public String toString() {
		return "Stunned";
	}

	@Override
	public String statusMessage() { return "stunned"; }

	@Override
	public String playerMessage() { return "you suddenly blackout!"; }*/
	@Override
	public String toString() {
		return "晕厥";
	}

	@Override
	public String statusMessage() { return "晕厥"; }

	@Override
	public String playerMessage() { return "你被击晕了！"; }

	@Override
	public int icon() {
		return BuffIndicator.VERTIGO;
	}

	@Override
	public void applyVisual() {
		target.sprite.add( CharSprite.State.VERTIGO );
	}

	@Override
	public void removeVisual() {
		if (!target.hasBuff(Dazed.class))
			target.sprite.remove( CharSprite.State.VERTIGO );
	}

	@Override
	public boolean attachOnLoad( Char target ) {
		target.stunned = true;
		return super.attachOnLoad(target);
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			freeze(target);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		unfreeze( target );
	}


	public static void freeze( Char ch ) {
		Buff.detach( ch, Guard.class );
		ch.stunned = true;
	}
	
	public static void unfreeze( Char ch ) {
		if (ch.buff( Stun.class ) == null &&
			ch.buff( Petrificated.class ) == null &&
			ch.buff( Frozen.class ) == null) {
			ch.stunned = false;
		}
	}

	@Override
/*	public String description() {
		return "You have lost your consciousness for a moment. Your cannot perform any action until recover.";
	}*/
	public String description() {
		return "你暂时失去了所有知觉，期间不能做任何行动，直至清醒";
	}


}
