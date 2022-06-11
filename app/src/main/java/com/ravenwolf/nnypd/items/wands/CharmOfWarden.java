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
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.Effects;
import com.ravenwolf.nnypd.visuals.effects.Lightning;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.particles.EnergyParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypd.visuals.sprites.JadeWardenSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class CharmOfWarden extends WandUtility {

	{
        name = "哨位魔咒";
        image = ItemSpriteSheet.CHARM_JADE;
	}

    private static int HP_MODIFIER = 2;

    @Override
    protected void cursedProc(Hero hero){
        curCharges = (curCharges+1)/2;
        int dmg=hero.absorb( damageRoll(), true )/2;
        hero.damage(dmg, this, Element.ENERGY);
    }

	@Override
	protected void onZap( int cell ) {
        spawnWarden(cell, Warden.class);
	}

    public int basePower(){
        return super.basePower()-8;
    }

    @Override
    public int damageRoll() {
        if( curUser != null ) {
            float eff = effectiveness();
            return getMaxDamage(curUser.magicSkill, eff);
        } else {
            return 0;
        }
    }

    protected <T extends Warden> void spawnWarden(int cell, Class<T> c) {

        int stats = damageRoll();

        // first we check the targeted tile
        if( Warden.spawnAt( stats, curCharges, cell, c ) == null ) {

            // then we check the previous tile
            int prevCell = Ballistica.trace[ Ballistica.distance - 1 ];

            if( Warden.spawnAt( stats, curCharges,  prevCell, c ) == null ) {

                // and THEN we check all tiles around the targeted
                ArrayList<Integer> candidates = new ArrayList<Integer>();

                for (int n : Level.NEIGHBOURS8) {
                    int pos = cell + n;
                    if( Level.adjacent( pos, prevCell ) && !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ){
                    Warden.spawnAt( stats, curCharges, candidates.get( Random.Int( candidates.size() ) ), c );
                } else {
                    GLog.i( "什么也没有发生" );
                }
            }
        }

        Sample.INSTANCE.play( Assets.SND_LIGHTNING );
    }

	protected void fx( int cell, Callback callback ) {
		MagicMissile.coldLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
        return
                "这个由水晶和翡翠组成的魔咒，具有召唤翡翠哨位的能力，这些哨位通常被设置用来守护殿堂，它会攻击视野内任何想要接近的敌人";
    }

    protected String getEffectDescription(int min , int max, boolean known){
        int charges = known ? 3 + bonus : 3;
        int HP = (2  + charges *2 ) * HP_MODIFIER;

        return  "被召唤的哨位" +(known? "":"(可能) ")+"会造成_" + max * 2 / 5 + "-" + max * 3 / 4 + "点伤害_，" +
                "并且拥有_" + HP+ "的血量_";
    }


    public static class Warden extends NPC {

        private int stats;
        private int charges;
        private boolean charged = false;

        public Warden(){

            name = "翡翠哨位";
            spriteClass = JadeWardenSprite.class;

            resistances.put( Element.Physical.class, Element.Resist.PARTIAL);
            resistances.put( Element.Shock.class, Element.Resist.PARTIAL);
            resistances.put( Element.Acid.class, Element.Resist.PARTIAL);
            resistances.put( Element.Flame.class, Element.Resist.PARTIAL);
            resistances.put( Element.Frost.class, Element.Resist.PARTIAL);
            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Body.class, Element.Resist.IMMUNE );

            PASSIVE = new Guarding();
            hostile = false;
            friendly = true;
            flying=true;
        }

        @Override
        public boolean isMagical() {
            return true;
        }

        @Override
        public boolean isEthereal() {
            return true;
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }


        @Override
        protected boolean act() {
            HP-=HP_MODIFIER;
            if( HP <= 0 ){
                die( this );
                return true;
            }

            return super.act();
        }

        @Override
        public boolean add( Buff buff ) {
            if (buff instanceof Ensnared || buff instanceof Burning || buff instanceof Corrosion)
                return false;
            else
                return super.add( buff );
        }

        protected int meleeAttackRange() {
            return -1;//forces to always use ranged attack
        }

        @Override
        public void interact(){
            Dungeon.hero.sprite.operate( pos );

            if (Dungeon.hero.belongings.weap2 instanceof CharmOfWarden) {
                CharmOfWarden charm = (CharmOfWarden) Dungeon.hero.belongings.weap2;
                // we restore at least one charge less than what was spent on the vine
                charm.recharge((int) ((HP  * HP_MODIFIER / HT) * charm.rechargeRate())
                );
                GLog.i("你把哨位回收到了魔咒中");
            } else {
                GLog.i("你没能把哨位的能量完全回收");
            }

            Sample.INSTANCE.play( Assets.SND_LIGHTNING );

            Dungeon.hero.spend( TICK );
            Dungeon.hero.busy();

            die( this );
        }

        protected boolean canAttack( Char enemy ){
            return !hasBuff(Disrupted.class)  && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
        }

        @Override
        protected boolean doAttack( Char enemy ) {

            if(!charged ) {
                charged = true;
                if( Dungeon.visible[ pos ] ) {
                    sprite.centerEmitter().burst(EnergyParticle.FACTORY_WHITE, 20);
                }
                HP+=HP_MODIFIER;//dont loose health the turns that charge
                spend( attackDelay() );
                return true;

            } else {
                charged = false;
                return super.doAttack( enemy );
            }
        }

        @Override
        protected void onRangedAttack( int cell ) {

            for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
                if (mob!=this && mob.hostile && !mob.isFriendly() && !mob.invulnerable() && Level.distance(pos,mob.pos) <= viewDistance() &&
                        mob.pos == Ballistica.cast(pos, mob.pos, false, true)) {

                    int[] points = new int[2];

                    points[0] = pos;
                    points[1] = mob.pos;

                    sprite.parent.add( new Lightning( points, 2, Effects.get( Effects.Type.JADE_SHOCK ),null ) );

                    castBolt(mob, damageRoll(), true, Element.ENERGY);
                    HP-=HP_MODIFIER;
                    if( HP <= 0 )
                        break;
                }
            }
            next();
            if( HP <= 0 )
                die( this );
            //super.onRangedAttack(cell);
            sprite.idle();
        }

        @Override
        protected Char chooseEnemy() {

            // wardens attack your enemies by default
            if ( enemy == null || !enemy.isAlive() || enemy.invulnerable() ) {

                HashSet<Mob> enemies = new HashSet();

                for ( Mob mob: Dungeon.level.mobs ) {
                    if ( mob.hostile && !mob.isFriendly() && !mob.invulnerable() && Level.fieldOfView[mob.pos] ) {
                        enemies.add( mob );
                    }
                }

                return enemies.size() > 0 ? Random.element( enemies ) : null;

            } else {

                return enemy;

            }
        }

        protected void adjustStats( int stats, int charges ) {

            HT = (2  + charges * 2 ) * HP_MODIFIER;

            armorClass = stats * 2 / 5;

            maxDamage = stats * 3 / 4;
            minDamage = stats * 2 / 5;

            accuracy = stats * 3 / 2;
            dexterity = stats / 2;

            this.stats = stats;
            this.charges = charges;
        }

        @SuppressWarnings("unchecked")
        public static <T extends Warden> Warden spawnAt(int stats, int charges, int pos, Class<T> c ){

            // cannot spawn on walls, or already occupied tiles
            if ( !Level.solid[pos] && Actor.findChar( pos ) == null ){

                Warden warden = null;
                try {
                    warden = c.newInstance();
                } catch (Exception e) {
                    return null;
                }

                warden.pos = pos;
                warden.enemySeen = true;
                warden.state = warden.PASSIVE;

                GameScene.add( warden, 0f );

                warden.sprite.emitter().burst( SparkParticle.FACTORY, 5 );
                warden.sprite.spawn();

                warden.adjustStats( stats, charges );
                warden.HP = warden.HT;

                return warden;

            } else {

                return null;

            }
        }

        private class Guarding extends Passive {

            @Override
            public boolean act( boolean enemyInFOV, boolean justAlerted ){

                if (enemyInFOV && canAttack( enemy ) && enemy != Dungeon.hero ) {

                    return doAttack( enemy );

                } else {
                    //reset enemy if cannot attack
                    resetEnemy();
                    spend( TICK );
                    return true;

                }
            }

            @Override
            public String status(){
                return "防守";
            }
        }

        @Override
        public String description() {
            return
                    "这些远古的哨位，由一些翡翠的碎片完美组合而成。它们会攻击视野内任何想要接近的敌人";
        }

        private static final String STATS	= "stats";
        private static final String CHARGES	= "charges";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( STATS, stats );
            bundle.put(CHARGES, charges);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            adjustStats( bundle.getInt( STATS ), bundle.getInt(CHARGES) );
        }
    }


}