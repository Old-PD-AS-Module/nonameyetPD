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
package com.ravenwolf.nnypd.visuals.ui;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;

import java.util.ArrayList;

public class TagAttack extends Tag {
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;
	
	public static TagAttack instance;
	
	private CharSprite sprite = null;
	
	private static Mob currentTarget = null;
	private static Mob lastAttackedTarget = null;
	private ArrayList<Mob> candidates = new ArrayList<>();
	
	public TagAttack() {
		super( TagDanger.COLOR );
		
		instance = this;
		
		setSize( WIDTH, HEIGHT );
		visible( false );
		enable( false );
	}

	public static Mob getCurrentTarget(){
		return currentTarget;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		if (sprite != null) {
			sprite.x = x + (width - sprite.width()) / 2;
			sprite.y = y + (height - sprite.height()) / 2;
			PixelScene.align( sprite );
		}
	}	
	
	@Override
	public void update() {
		super.update();
		
		if (Dungeon.hero.isAlive()) {
            enable( Dungeon.hero.ready );
		} else {
			visible( false );
			enable( false );
		}
	}

	private void checkEnemies() {

//		int heroPos = Dungeon.hero.pos;

		candidates.clear();

		int v = Dungeon.hero.visibleEnemies();

		for (int i=0; i < v; i++) {
			Mob mob = Dungeon.hero.visibleEnemy( i );
			if (mob != null) {
				candidates.add( mob );
			}
		}
		if (lastAttackedTarget != null){
			if (!lastAttackedTarget.isAlive()){
				lastAttackedTarget=null;
			}
		}

		if (!candidates.contains( lastAttackedTarget )) {
			if (candidates.isEmpty()) {
				lastAttackedTarget = null;
				currentTarget = null;
			} else {
				Mob target=candidates.get(0);
				if (target!= currentTarget) {
					currentTarget = target;
					updateImage();
					flash();
				}
			}
		} else {
			int cell = Ballistica.cast(Dungeon.hero.pos, lastAttackedTarget.pos, false, true);
			Char target= Actor.findChar(cell);
			if (target != lastAttackedTarget){ //if other char is blocking the path
				if (candidates.contains( target ))
					currentTarget = (Mob)target;
				else
					currentTarget = candidates.get(0);
				updateImage();
				flash();
			}
			if (!bg.visible) {
				flash();
			}
		}
		
		visible( currentTarget != null );
		enable( Dungeon.hero.ready );
	}
	
	private void updateImage() {
		
		if (sprite != null) {
			sprite.killAndErase();
			sprite = null;
		}
		
		try {
			//sprite = currentTarget.spriteClass.newInstance();
			sprite = currentTarget.sprite();
			sprite.idle();
			sprite.paused = true;
			add( sprite );

			sprite.x = x + (width - sprite.width()) / 2;
			sprite.y = y + (height - sprite.height()) / 2;
			PixelScene.align( sprite );
			
		} catch (Exception e) {
		}
	}
	
	private boolean enabled = true;

	private void enable( boolean value ) {
		enabled = value;
		if (sprite != null) {
			sprite.alpha( value ? ENABLED : DISABLED );
		}
	}
	
	private void visible( boolean value ) {
		bg.visible = value;
		if (sprite != null) {
			sprite.visible = value;
		}
	}
	
	@Override
	protected void onClick() {
		if ( visible && enabled && Dungeon.hero.ready && currentTarget != null ) {
			Dungeon.hero.handle( currentTarget.pos );
		}
	}

    @Override
    protected boolean onLongClick() {

        if (visible && enabled && Dungeon.hero.ready && currentTarget != null) {
            Toolbar.examineMob( currentTarget.pos );
            return true;
        }

        return false;
    }
	
	public static void target( Mob target ) {
		currentTarget = target;
		lastAttackedTarget=target;
		instance.updateImage();
		
		HealthIndicator.instance.target( target );
	}
	
	public static void updateState() {

        if( instance != null ) {
            instance.checkEnemies();
        }
	}
}
