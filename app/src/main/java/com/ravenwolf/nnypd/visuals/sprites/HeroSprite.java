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

import android.graphics.RectF;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.hero.HeroClass;
import com.ravenwolf.nnypd.items.armours.shields.Shield;
import com.ravenwolf.nnypd.items.weapons.Weapon;
import com.ravenwolf.nnypd.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.Pistole;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypd.items.weapons.ranged.RangedWeaponMissile;
import com.ravenwolf.nnypd.items.weapons.ranged.Sling;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.layers.AdditionalSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.RangedWeaponSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.ShieldSprite;
import com.ravenwolf.nnypd.visuals.sprites.layers.WeaponSprite;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class HeroSprite extends CharSprite {
	
	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	
	private static final int RUN_FRAMERATE	= 16;//20;

	public static final int ANIM_IDLE	= 0;
	public static final int ANIM_RUN	= 1;
	public static final int ANIM_ATTACK	= 2;
	public static final int ANIM_STAB 	=3;
	public static final int ANIM_RANGED_ATTACK 	=4;
	public static final int ANIM_RANGED_ATTACK_END 	=14;
	public static final int ANIM_DOUBLEHIT =5;
	public static final int ANIM_FLY =6;
	public static final int ANIM_CAST	= 7;
	public static final int ANIM_SLAM	= 8;
	public static final int ANIM_BACKSTAB	= 40;
	public static final int ANIM_OPERATE	= 20;
	public static final int ANIM_PICK	= 21;
	
	private static TextureFilm tiers;

	//used to store the current mele weapon (for dual wiled or offhands attacks)
	Weapon curDrawWep=null;

	private WeaponSprite weapSprite;
	private ShieldSprite shieldSprite;
	private RangedWeaponSprite rngWeapSprite;
	private AdditionalSprite addWeapSprite;
	private AdditionalSprite addOffhandSprite;
/*
	//added to make enchanted items glow
	private boolean glowUp;
	private float phase;
*/
	private Animation shoot;
	private Animation shootEnd;
	private Animation doubleHit;
	private Animation shoot2hands;
	private Animation shoot2handsEnd;
	private Animation stab;
	private Animation backstab;
	private Animation shieldSlam;
	private Animation bow;
	private Animation sling;
	private Animation fly;

	public HeroSprite() {
		super();

        link( Dungeon.hero );

        texture( Dungeon.hero.heroClass.spritesheet() );
        updateArmor();

		weapSprite= new WeaponSprite(this);
		rngWeapSprite= new RangedWeaponSprite(this);
		shieldSprite= new ShieldSprite(this);
		addWeapSprite=new AdditionalSprite(this);
		addOffhandSprite=new AdditionalSprite(this);

		updateEquipment();
		
		idle();
	}
	
	public void updateEquipment() {

		int weapID=-1;
		int addID = -1;
		curDrawWep = null;
		int animationId=getAnimationId();
		Hero hero= (Hero)ch;
		if(hero.belongings.weap1 instanceof MeleeWeapon) {
			weapID = getWeaponId(hero.belongings.weap1);
			weapSprite.update(((Hero)ch).belongings.weap1,weapID,animationId,curFrame);
			addID=((Hero)ch).belongings.weap1.getAdditionalDrawId();
			if (addID>=0) {
				addWeapSprite.update(((Hero)ch).belongings.weap1,addID,animationId,curFrame);
			}
		}

		weapID=getRngWeaponId();
		if (weapID>=0) {
			rngWeapSprite.update(((Hero)ch).belongings.weap2,weapID,animationId,curFrame);
			addID=((Hero)ch).belongings.weap2.getAdditionalDrawId();
			addOffhandSprite.update(((Hero)ch).belongings.weap2,addID,animationId,curFrame);
		}
		int shieldID=getShieldId();
		if (shieldID>=0) {
			shieldSprite.update(((Hero)ch).belongings.weap2,shieldID,animationId,curFrame);
		}


	}

	private int getShieldId() {
		int id = -1;
		if (ch.isAlive() && ((Hero)ch).belongings.weap2 instanceof Shield) {
			id =((Hero)ch).belongings.weap2.getDrawId();
		}
		return id;
	}

	private int getAdditionalSpriteId() {
		int id = -1;
		if (ch.isAlive()) {
			if (((Hero) ch).belongings.weap1 != null)
				id = ((Hero) ch).belongings.weap1.getAdditionalDrawId();
		}
		return id;
	}

	private int getAdditionalOffhandSpriteId() {
		int id = -1;
		if (ch.isAlive()) {
			if (((Hero) ch).belongings.weap2 instanceof RangedWeapon)
				return ((Hero) ch).belongings.weap2.getAdditionalDrawId();

		}
		return id;
	}

	private int getRngWeaponId() {

		int id = -1;
		if (ch.isAlive() && ((Hero)ch).belongings.weap2 instanceof RangedWeapon ) {
			id =((Hero)ch).belongings.weap2.getDrawId();
		}
		return id;
	}

	private int getMeleeWeaponId(){
		Hero hero= (Hero)ch;
		if(hero.belongings.weap1 instanceof MeleeWeapon)
			return getWeaponId(hero.belongings.weap1);
		else
			return -1;
	}

	private int getWeaponId(Weapon weapToDraw){
		int id=-1;
		if (ch.isAlive() && weapToDraw instanceof MeleeWeapon) {
			id=weapToDraw.getDrawId();
		}
		return id;
	}


	public void attack( Weapon curWep, int cell, Callback callback ) {
		animCallback=callback;
		if (curWep!=null){
			Animation anim=attack;
			//FIXME
			if(ItemSpriteSheet.ASSASSIN_BLADE == curWep.image())
				anim=backstab;
			else if(ItemSpriteSheet.SPEAR ==curWep.image() || ItemSpriteSheet.GLAIVE ==curWep.image()
					|| ItemSpriteSheet.HALBERD == curWep.image() && Random.Int(2)==0)
				anim=stab;
			else if (ItemSpriteSheet.DOUBLE_BLADE == curWep.image())
				anim=doubleHit;


			int wepID=getWeaponId(curWep);
			if (wepID>=0)
				weapSprite.update(curWep,wepID,getAnimationId(),curFrame);

			curDrawWep=curWep;

			turnTo( ch.pos, cell );
			play(anim);

		}else
			attack( cell );

	}

	public void charge( Weapon curWep, int from, int to, Callback callback ) {

		attack( curWep,  to, callback );
		int distance = Level.distance( from, to );

		motion = new PosTweener( this, worldToCamera( to ), distance*0.05f );
		motion.listener = this;
		parent.add( motion );

	}

	public void updateArmor() {

		TextureFilm film = new TextureFilm( tiers(), ((Hero)ch).appearance(), FRAME_WIDTH, FRAME_HEIGHT );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );
		
		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10, 11, 12, 11 );
		
		attack = new Animation( 12, false );
		attack.frames( film, 13, 14, 15, 0 );

		backstab = new Animation( 12, false );
		backstab.frames( film, 0, 14, /*14,*/ 15, 0 );

		doubleHit = new Animation( 12, false );
		doubleHit.frames( film, 13, 14, 15, 19, 0 );

		stab = new Animation( 14, false );
		stab.frames( film, 13, 15, 15, 0 );

		shoot = new Animation( 12, false );
		shoot.frames( film, 15, 15 );

		shootEnd = new Animation( 10, false );
		shootEnd.frames( film, 15, 15, 0 );

		shoot2hands = new Animation( 10, false );
		shoot2hands.frames( film, 13, 19, 19/*, 19/, 0*/ );

		shoot2handsEnd = new Animation( 10, false );
		shoot2handsEnd.frames( film,  19, 19, 0 );

		shieldSlam = new Animation( 10, false );
		shieldSlam.frames( film, 0, 0, 13, 0 );

		bow = new Animation( 12, false );
		bow.frames( film, 13, 14, 15, 19, 19, 0 );

		cast = attack.clone();
		sling = attack.clone();
		
		operate = new Animation( 8, false );
		operate.frames( film, 16, 17, 16, 17 );

        pickup = new Animation( 15, false );
        pickup.frames( film, 0, 15, 16, 17, 0 );

        search = new Animation( 2, false );
        search.frames( film, 0, 1, 0, 1  );
		
		fly = new Animation( 1, true );
		fly.frames( film, 18 );
	}

	@Override
	public void onComplete( Animation anim ) {

		if (animCallback == null) {
			 if (anim == stab || anim == doubleHit || anim == backstab) {
				idle();
				ch.onAttackComplete();
			} else if (anim == shootEnd || anim == shoot2handsEnd || anim == bow || anim == sling) {
				idle();
				ch.onCastComplete();
			}
		}
		super.onComplete(anim);
	}

	public void shieldSlam( int cell, Callback callback ) {
		animCallback = callback;
		turnTo( ch.pos, cell );
		play( shieldSlam);
	}

	private void updateGearFrames() {
		int anim = getAnimationId();
		if (anim==ANIM_RANGED_ATTACK)
			curDrawWep=(Weapon) ((Hero)ch).belongings.weap2;

		weapSprite.updateFrame(curFrame,anim,((Hero)ch).belongings.weap1);
		shieldSprite.updateFrame(curFrame,anim,((Hero)ch).belongings.weap2);
		rngWeapSprite.updateFrame(curFrame,anim,((Hero)ch).belongings.weap2);
		addWeapSprite.updateFrame(curFrame,anim,((Hero)ch).belongings.weap1);
		addOffhandSprite.updateFrame(curFrame,anim,((Hero)ch).belongings.weap2);
	}

	@Override
	public void play( Animation anim, boolean force ) {
		super.play( anim, false );
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

	@Override
	public void draw() {
		//FIXME
		if(ch!=null) {
			if ( !(curDrawWep instanceof RangedWeapon) && getAdditionalSpriteId() >= 0)
				addWeapSprite.draw(curFrame);
			if (getAdditionalOffhandSpriteId() >= 0)
				addOffhandSprite.draw(curFrame);

			super.draw();

			if (getShieldId() >= 0) {
				shieldSprite.draw(curFrame);
			}

			if (getRngWeaponId() >= 0 && curDrawWep instanceof RangedWeapon) {
				rngWeapSprite.draw(curFrame);
			}else if (getMeleeWeaponId() >= 0) {
				weapSprite.draw(curFrame);
			}
		}
	}

	public int getAnimationId(){
		//FIXME
		if (curAnim == run)
			return ANIM_RUN;
		else if (curAnim == attack)
			return ANIM_ATTACK;
		else if (curAnim == stab)
			return ANIM_STAB;
		else if (curAnim==shoot || curAnim==bow || curAnim==shoot2hands || curAnim== sling)
			return ANIM_RANGED_ATTACK;
		else if (curAnim==shoot2handsEnd)
			return ANIM_RANGED_ATTACK_END;
		else if (curAnim==doubleHit)
			return ANIM_DOUBLEHIT;
		else if (curAnim==fly)
			return ANIM_FLY;
		else if (curAnim == cast)
			return ANIM_CAST;
		else if (curAnim == shieldSlam)
			return ANIM_SLAM;
		else if (curAnim == operate)
			return ANIM_OPERATE;
		else if (curAnim == pickup)
			return ANIM_PICK;
		else if (curAnim == backstab)
			return ANIM_BACKSTAB;
		else if (curAnim == idle)
			return ANIM_IDLE;
		return -1;
	}

	
	@Override
	public void place( int p ) {
		super.place( p );
		Camera.main.target = this;
	}

	@Override
	public void move( int from, int to ) {		
		super.move( from, to );
		if (ch.flying) {
			play( fly );
		}
		Camera.main.target = this;
	}

	@Override
	public void idle() {
		if (ch.flying) {
			play( fly );
		}else
			super.idle();
	}
	
	@Override
	public void jump( int from, int to, Callback callback ) {	
		super.jump( from, to, callback );
		play( fly );
	}

    public void shoot(int cell) {
        turnTo( ch.pos, cell );
        play( shoot );
    }

    public void shoot(int cell, RangedWeapon weap, Callback callback ) {
		animCallback = callback;
		turnTo( ch.pos, cell );
		//FIXME
		if (weap instanceof Sling)
			play( sling );
		else if (weap instanceof RangedWeaponMissile /*Bow || weap instanceof Arbalest*/)
			play( bow );
		else if (weap instanceof Pistole)
			play( shoot );
		else //flintlock arballest
			play( shoot2hands ); //arquebuse,  cannon
	}

	public void shootEnd( RangedWeapon weap ) {
		if (weap instanceof Pistole)
			play( shootEnd );
		else
			play( shoot2handsEnd ); //arquebuse,  cannon
	}
	
	@Override
	public void update() {
		sleeping = ((Hero)ch).restoreHealth;
		super.update();
	}

	public boolean sprint( boolean on ) {
		run.delay = on ? 0.625f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
		return on;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.ROGUE);
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		
		return tiers;
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
		
		RectF patch = tiers().get( armorTier );
		Image avatar = new Image( cl.spritesheet() );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.offset( patch.left, patch.top );
		avatar.frame( frame );
		
		return avatar;
	}
}
