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
package com.ravenwolf.nnypd.visuals.sprites;


import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.visuals.sprites.layers.AdditionalSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;
import com.watabou.utils.Random;

public abstract class MobLayeredSprite extends MobSprite {

	protected WeaponSprite weapSprite;
	//protected ShieldSprite shieldSprite;
	protected AdditionalSprite addWeapSprite;

	protected Animation stab;
	protected Animation backstab;


	public MobLayeredSprite() {
		super();
		weapSprite = new WeaponSprite(this);
		addWeapSprite= new AdditionalSprite(this);
	}

	public int getAnimationId(){
		//FIXME
		if (curAnim == run)
			return HeroSprite.ANIM_RUN;
		else if (curAnim == attack)
			return HeroSprite.ANIM_ATTACK;
		else if (curAnim == stab)
			return HeroSprite.ANIM_STAB;
		else if (curAnim == backstab)
			return HeroSprite.ANIM_BACKSTAB;
		else if (curAnim == cast)
			return HeroSprite.ANIM_CAST;
		else if (curAnim == idle || curAnim == sleep)
			return HeroSprite.ANIM_IDLE;
		return -1;
	}

	@Override
	public void onComplete( Animation anim ) {

		if (animCallback == null) {
			if (anim == stab || /*anim == doubleHit ||*/ anim == backstab) {
				idle();
				ch.onAttackComplete();
			} /*else if (anim == shootEnd || anim == shoot2handsEnd || anim == bow || anim == sling) {
				idle();
				ch.onCastComplete();
			}*/
		}
		super.onComplete(anim);
	}


	@Override
	public void attack( int cell ) {
		super.attack( cell );
		if(ch!=null) {
			Weapon curWep=getWeaponToDraw();
			if(ItemSpriteSheet.ASSASSIN_BLADE == curWep.image())
				play(backstab);
			else if(ItemSpriteSheet.SPEAR ==curWep.image() || ItemSpriteSheet.GLAIVE ==curWep.image()
					|| ItemSpriteSheet.HALBERD == curWep.image() && Random.Int(2)==0)
				play(stab);

		}
	}

	@Override
	public void place( int cell ) {
		super.place(cell);
		//to avoid weapon not updating flipHorizontal
		updateGearFrames();
	}

	@Override
	protected void updateAnimation() {
		int lastFrame = curFrame;
		super.updateAnimation();
		if (curFrame != lastFrame) {
			updateGearFrames();
		}
	}

	private void updateGearFrames() {
		if(ch!=null) {
			int anim = getAnimationId();
			Weapon currWeap=getWeaponToDraw();
			weapSprite.updateFrame(curFrame, anim, currWeap);
			//shieldSprite.updateFrame(curFrame,anim,getWeaponToDraw());
			addWeapSprite.updateFrame(curFrame,anim,currWeap);
		}
	}

	protected abstract Weapon getWeaponToDraw();

	@Override
	public void link( Char ch ) {
		super.link( ch );
		int anim = getAnimationId();
		Weapon currWeap=getWeaponToDraw();
		int weapID=getWeaponId(currWeap);
		if (weapID>=0)
			weapSprite.update(currWeap,weapID,anim,curFrame);

		int addWeapID=getAdditionalSpriteId(currWeap);
		if (addWeapID>=0)
			addWeapSprite.update(currWeap,addWeapID,anim,curFrame);
	}

	protected int getWeaponId(Weapon weapToDraw){
		int id=-1;
		if (ch.isAlive() && weapToDraw instanceof MeleeWeapon) {
			id=weapToDraw.getDrawId();
		}
		return id;
	}

	private int getAdditionalSpriteId(Weapon weapToDraw) {
		int id = -1;
		if (ch.isAlive() )
			id =weapToDraw.getAdditionalDrawId();
		return id;
	}

	@Override
	public void draw() {
		if(ch!=null) {
			if (getAdditionalSpriteId(getWeaponToDraw()) >= 0)
				addWeapSprite.draw(curFrame);
		}

		super.draw();
		if(ch!=null) {
			if(getWeaponId(getWeaponToDraw()) >= 0) {
				weapSprite.draw(curFrame);
			}
		}
	}
}
