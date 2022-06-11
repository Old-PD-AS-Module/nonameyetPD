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
package com.ravenwolf.nnypd.items.wands;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Fire;
import com.ravenwolf.nnypd.actors.hazards.FieryRune;
import com.ravenwolf.nnypd.actors.hazards.Hazard;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.BlastParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFirebolt extends WandCombat {

	{
		name = "火球法杖";
		image = ItemSpriteSheet.WAND_FIREBRAND;
	}

	@Override
	protected void cursedProc(Hero hero){
		int dmg=hero.absorb( damageRoll(), true )/2;
		hero.damage(dmg, this, Element.FLAME);
	}

	@Override
	protected void onZap( int cell ) {

		Char ch = Char.findChar( cell );

		if( ch != null ) {

			ch.damage( damageRoll(), curUser, Element.FLAME );

			if (Level.flammable[ cell ]) {
				GameScene.add( Blob.seed( cell, 3, Fire.class ) );
			}

			if( Dungeon.visible[ cell ] ){
				CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 2 );
				CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 4 );
			}

			Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
			Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );
			Camera.main.shake( 1, 0.2f );

		} else if( Level.solid[ cell ] ){

			if( Level.flammable[ cell ] ){
				GameScene.add( Blob.seed( cell, 3, Fire.class ) );
			}

			if( Dungeon.visible[ cell ] ){
				CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 3 );
				CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 6 );
			}

			Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
			Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );

			Camera.main.shake( 1, 0.2f );

		} else {

			FieryRune rune = Hazard.findHazard( cell, FieryRune.class );

			int strength = damageRoll() * 3 / 2;

			if( rune == null ){

				rune = new FieryRune();
				rune.setValues( cell, strength, damageRoll()*2 );

				GameScene.add( rune );
				( (FieryRune.RuneSprite) rune.sprite ).appear();

			} else {

				rune.upgrade( strength / 2, damageRoll() );
				rune.explode();

			}
		}
	}

	protected void fx( int cell, Callback callback ) {
		MagicMissile.fire( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	

	@Override
	public String desc() {
		return
				"这个法杖可以对敌人释放出炙热的火焰，如果释放在空地上则会制造出一个临时的烈焰纹章，当其他物体接触时会触发严重的爆炸，对纹章第二次释放时，则会直接触发爆炸并增强它的效果";
	}

}
