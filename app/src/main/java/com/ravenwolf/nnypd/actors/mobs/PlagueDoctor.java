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

import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Miasma;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypd.visuals.sprites.PlagueDoctorSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;


public class PlagueDoctor extends MobCaster {

    private int plagueCD = 0;

    public PlagueDoctor() {

        super( 7);

        name = "瘟疫医生";
		spriteClass = PlagueDoctorSprite.class;

        loot = Generator.random(Generator.Category.POTION);
        lootChance =0.25f;
	}

    @Override
    public String getTribe() {
        return TRIBE_UNDEAD;
    }

    private boolean canUseFlask( Char enemy ) {
        return enemy!=null && plagueCD <=0 && !Level.adjacent( pos, enemy.pos )
                && Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos ;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return Level.adjacent( pos, enemy.pos ) &&  !isCharmedBy( enemy ) || canUseFlask( enemy );
    }

    @Override
    public boolean act() {

        if (plagueCD >0)
            plagueCD--;
        if (state==FLEEING && canUseFlask( enemy ))
            state = HUNTING;
        else if (state==HUNTING && (Random.Int(5)==0 && plagueCD <=0 || plagueCD >8))
            state = FLEEING;

        return super.act();
    }


    @Override
    protected void onRangedAttack( int cell ) {

        plagueCD =Random.IntRange(12, 17);
        final int targetCell = Ballistica.trace[Ballistica.distance-2];
        ((MissileSprite) sprite.parent.recycle(MissileSprite.class)).
                reset(pos, targetCell, ItemSpriteSheet.POTION_CHARCOAL, new Callback() {
                    @Override
                    public void call() {
                        GameScene.add( Blob.seed( targetCell, damageRoll()*12, Miasma.class ) );
                        Sample.INSTANCE.play( Assets.SND_SHATTER );
                        next();
                    }
                });

        state = FLEEING;
        super.onRangedAttack( targetCell );
    }


    @Override
    public String description() {
        return
                "当黑暗的瘴气到达监狱时，瘟疫医生被派往那里帮助病人，控制疾病。但即使是它们的喙形面具也不能保护它们免受这种瘟疫的侵袭……" +
                        "最后就连他们也被不洁的魔法扭曲了，现在正致力于传播腐化和衰败。";
    }

    private static final String PLAGUE_CD = "plagueCD";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(PLAGUE_CD, plagueCD);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        plagueCD = bundle.getInt(PLAGUE_CD);
    }
}
