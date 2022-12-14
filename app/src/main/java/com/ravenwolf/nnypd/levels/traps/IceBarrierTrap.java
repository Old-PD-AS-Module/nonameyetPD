package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Frozen;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.IceBlockSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashMap;

public class IceBarrierTrap extends SideWallTrap {
    public IceBarrierTrap() {
        this.color = 6;
        this.canBeHidden = true;
        this.avoidsHallways = true;
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap
    public String name() {
        return "Ice barrier trap";
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap
    public String description() {
        return "When activated, this trap will send out waves of a powerful chemical that will freeze everything in its path, creating a solid barrier of ice.";
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap
    public void activate() {
        spawnWall(this.pos, this.wall1);
        spawnWall(this.pos, this.wall2);
    }

    private void spawnWall(int from, int to) {
        Ballistica.cast(from, to, false, false);
        int power = power();
        for (int i = 0; i < Ballistica.distance; i++) {
            int pos = Ballistica.trace[i];
            Char ch = Actor.findChar(pos);
            if (ch != null && !(ch instanceof IceBlock)) {
                hitChar(ch);
            } else {
                IceBlock.spawnAt(power, pos);
            }
            Heap heap = Dungeon.level.heaps.get(i);
            if (heap != null) {
                heap.freeze(power);
            }
        }
    }

    private int power() {
        return (Dungeon.chapter() * 4) + 6;
    }

    private void hitChar(Char ch) {
        int power = power();
        BuffActive.addFromDamage(ch, Chilled.class, power);
        BuffActive.addFromDamage(ch, Frozen.class, power);
        Sample.INSTANCE.play(Assets.SND_HIT);
    }

    /* loaded from: D:\SXDS\0.5.dex */
    public static class IceBlock extends Mob {
        public IceBlock() {
            this.name = "ice block";
            this.spriteClass = IceBlockSprite.class;
            HashMap<Class<? extends Element>, Float> hashMap = this.resistances;
            Float valueOf = Float.valueOf(0.5f);
            hashMap.put(Element.Flame.class, valueOf);
            this.resistances.put(Element.Shock.class, valueOf);
            this.resistances.put(Element.Acid.class, valueOf);
            this.resistances.put(Element.Physical.class, valueOf);
            HashMap<Class<? extends Element>, Float> hashMap2 = this.resistances;
            Float valueOf2 = Float.valueOf(1.0f);
            hashMap2.put(Element.Frost.class, valueOf2);
            this.resistances.put(Element.Body.class, valueOf2);
            this.resistances.put(Element.Mind.class, valueOf2);
            this.resistances.put(Element.Dispel.class, valueOf2);
            this.hostile = false;
        }

        @Override
        protected boolean canBeBackstabed() {
            return false;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob, com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public boolean add(Buff buff) {
            if (buff instanceof Burning) {
                int i = this.HT;
                damage(Random.NormalIntRange(i / 4, i / 5), null, Element.FLAME);
                return false;
            }
            return false;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
        public boolean mindVision() {
            return false;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public boolean immovable() {
            return true;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public boolean isMagical() {
            return false;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public boolean isSolid() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
        public boolean getCloser(int target) {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
        public boolean getFurther(int target) {
            return true;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob, com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public int viewDistance() {
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob, com.ravenwolf.nonameyetpixeldungeon.actors.Char, com.ravenwolf.nonameyetpixeldungeon.actors.Actor
        public boolean act() {
            int[] iArr;
            Chilled chilled;
            if (Dungeon.level.feeling != Level.Feeling.PERMAFROST) {
                int i = this.HP - 1;
                this.HP = i;
                if (i <= 0) {
                    die(null);
                    return true;
                }
            }
            for (int i2 : Level.NEIGHBOURS8) {
                int cell = this.pos + i2;
                Char c = Actor.findChar(cell);
                if (c != null && !c.moving && !(c instanceof IceBlock) && c.buff(Frozen.class) == null && ((chilled = (Chilled) c.buff(Chilled.class)) == null || chilled.getDuration() < 3)) {
                    BuffActive.add(c, Chilled.class, 1.0f);
                }
            }
            this.state = this.PASSIVE;
            return super.act();
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob, com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public void damage(int dmg, Object src, Element type) {
            super.damage(dmg, src, type);
            if (this.HP == 1) {
                die(src);
            }
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
        public Char chooseEnemy() {
            return null;
        }

        public void adjustStats(int level) {
            this.HT = (level * 3) / 2;
            this.armorClass = 0;
            this.minDamage = level / 2;
            this.maxDamage = (level * 2) / 3;
            this.accuracy = 0;
            this.dexterity = 0;
        }

        public static IceBlock spawnAt(int level, int pos) {
            if (!Level.solid[pos] && !Level.chasm[pos] && Actor.findChar(pos) == null) {
                IceBlock block = create(Dungeon.level, level, pos);
                GameScene.add(block, 1.0f);
                Dungeon.level.press(block.pos, block);
                block.sprite.spawn();
                return block;
            }
            return null;
        }

        public static IceBlock create(Level level, int power, int pos) {
            IceBlock block = new IceBlock();
            block.adjustStats(power);
            block.HP = block.HT;
            block.enemySeen = true;
            block.state = block.PASSIVE;
            block.pos = pos;
            boolean[] zArr = Level.losBlockHigh;
            zArr[pos] = true;
            zArr[pos] = true;
            return block;
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob, com.ravenwolf.nonameyetpixeldungeon.actors.Char
        public void die(Object cause, Element dmg) {
            int[] iArr;
            super.die(cause, dmg);
            Level level = Dungeon.level;
            boolean[] zArr = Level.losBlockHigh;
            int i = this.pos;
            zArr[i] = false;
            Level level2 = Dungeon.level;
            Level.losBlockHigh[i] = Dungeon.level.map[i] == 9;
            if (cause != null) {
                if (Dungeon.visible[i]) {
                    this.sprite.kill();
                    Sample.INSTANCE.play(Assets.SND_SHATTER, 1.0f, 1.0f, 0.5f);
                    CellEmitter.get(this.pos).burst(Speck.factory(20), 16);
                }
                for (int i2 : Level.NEIGHBOURS8) {
                    int cell = this.pos + i2;
                    Char c = Actor.findChar(cell);
                    if (c != null && !(c instanceof IceBlock)) {
                        c.damage(c.absorb(damageRoll(), false), this, Element.FROST);
                    }
                }
            }
        }

        @Override // com.ravenwolf.nonameyetpixeldungeon.actors.mobs.Mob
        public String description() {
            return "A solid block of ice, you can get cold if you stand near it without moving. It will melt eventually, but if is damaged enough, it will break apart and shatter, damaging nearby beings.";
        }
    }
}
