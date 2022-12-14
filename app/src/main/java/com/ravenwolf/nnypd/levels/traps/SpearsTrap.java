package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.BaseTilemap;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.sprites.MissileSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SpearsTrap extends SideWallTrap {
    private static final String CHARGING = "charging";
    private final Emitter.Factory DIRECTED_PARTICLE = new SpearsTrap$3(this);
    private boolean charging;
    Emitter emitter1;
    Emitter emitter2;

    public SpearsTrap() {
        this.color = 7;
        this.canBeHidden = false;
        this.avoidsHallways = true;
    }

    public SpearsTrap(SpearsTrap$1 spearsTrap$1, Char ch, SpearsTrap$1 spearsTrap$11) {
        super();
    }

    public String name() {
        return "Spear trap";
    }

    public String description() {
        return "This trap will trigger a hidden mechanism on the walls, shooting spears with deadly force from each sidewall.\n\nThankfully, the worn mechanism will give you enough time to get out of the way.";
    }

    public void activateAtPos(int pos, int wall1, int wall2) {
        this.pos = pos;
        this.wall1 = wall1;
        this.wall2 = wall2;
        activate();
    }

    public void activate() {
        this.charging = true;
        chargeSpears(this.wall1, this.wall2);
    }

    public void chargeSpears(int wall1, int wall2) {
        if (Terrain.isRealWall(Dungeon.level.map[wall1])) {
            Emitter emitter = CellEmitter.get(wall1);
            this.emitter1 = emitter;
            emitter.visible = Dungeon.visible[wall1];
            emitter.pour(this.DIRECTED_PARTICLE, 0.2f);
        }
        if (Terrain.isRealWall(Dungeon.level.map[wall2])) {
            Emitter emitter2 = CellEmitter.get(wall2);
            this.emitter2 = emitter2;
            emitter2.visible = Dungeon.visible[wall2];
            emitter2.pour(this.DIRECTED_PARTICLE, 0.2f);
        }
        Actor.addDelayed(new SpearsTrap$1(this, wall1, wall2), 1.0f);
    }

    public void hitChar(Char ch) {
        int power = (Dungeon.chapter() * 4) + 6;
        int damage = ch.absorb(Random.IntRange(power / 2, power));
        Sample.INSTANCE.play("snd_hit.mp3");
        ch.damage(damage, this, Element.PHYSICAL);
    }

    public void storeInBundle(Bundle bundle) {
        SpearsTrap.super.storeInBundle(bundle);
        bundle.put(CHARGING, this.charging);
    }

    public void restoreFromBundle(Bundle bundle) {
        SpearsTrap.super.restoreFromBundle(bundle);
        boolean z = bundle.getBoolean(CHARGING);
        this.charging = z;
        if (z) {
            Actor.addDelayed(new SpearsTrap$2(this), -1.0f);
        }
    }

    private class SpearsTrap$3 extends Emitter.Factory {
        final SpearsTrap this$0;

        SpearsTrap$3(SpearsTrap this$0) {
            this.this$0 = this$0;
        }

        public void emit(Emitter emitter, int index, float x, float y) {
            PointF point = BaseTilemap.tileCenterToWorld(this.this$0.pos);
            Speck s = (Speck) emitter.recycle(Speck.class);
            s.reset(index, x, y, 122);
            s.speed.set(point.x - x, point.y - y);
            s.speed.normalize().scale(32.0f);
            float f = s.x;
            PointF pointF = s.speed;
            s.x = f + (pointF.x / 4.0f);
            s.y += pointF.y / 4.0f;
        }
    }

    private class SpearsTrap$1 extends Actor {
        private int shoots;
        final SpearsTrap this$0;
        final int val$wall1;
        final int val$wall2;

        SpearsTrap$1(SpearsTrap this$0, int i, int i2) {
            this.this$0 = this$0;
            this.val$wall1 = i;
            this.val$wall2 = i2;
        }

//        static int access$310(SpearsTrap$1 x0) {
//            int i = x0.shoots;
//            x0.shoots = i - 1;
//            return i;
//        }

        protected boolean act() {
            if (Terrain.isRealWall(Dungeon.level.map[this.val$wall1])) {
                launchSpear(this.val$wall1, this.val$wall2);
                SpearsTrap spearsTrap = this.this$0;
                Emitter emitter = spearsTrap.emitter1;
                emitter.visible = Dungeon.visible[this.val$wall1];
                //emitter.burst(SpearsTrap.access$000(spearsTrap), 6);
                //Todo DIRECTED_PARTICLE 被反编译为access$000
                emitter.burst(spearsTrap.DIRECTED_PARTICLE, 6);
            }
            if (Terrain.isRealWall(Dungeon.level.map[this.val$wall2])) {
                launchSpear(this.val$wall2, this.val$wall1);
                SpearsTrap spearsTrap2 = this.this$0;
                Emitter emitter2 = spearsTrap2.emitter2;
                emitter2.visible = Dungeon.visible[this.val$wall2];
                emitter2.burst(spearsTrap2.DIRECTED_PARTICLE, 6);
            }
            if (this.shoots != 0) {
                return false;
            }
            Actor.remove(this);

            // SpearsTrap.access$102(this.this$0, false); access$102
            //Todo charging 被反编译为access$102
            SpearsTrap.this.charging = false;



            return true;
        }

        private void launchSpear(int from, int to) {
            int target = Ballistica.cast(from, to, false, true);
            Char ch = Actor.findChar(target);
            if (Dungeon.visible[from]) {
                Sample.INSTANCE.play("snd_miss.mp3", 0.6f, 0.6f, 1.2f);
            }
            boolean[] zArr = Dungeon.visible;
            SpearsTrap spearsTrap = this.this$0;
            if (zArr[spearsTrap.pos] || zArr[from] || zArr[target]) {
                this.shoots++;
                ((MissileSprite) Game.scene().recycle(MissileSprite.class)).reset(from, target, 54,
                        (Callback) new SpearsTrap(this, ch,
                        this));
            } else if (ch != null) {
                 SpearsTrap.access$200(spearsTrap, ch);
            }
        }
    }

    static void access$200(SpearsTrap spearsTrap, Char r4) {
        spearsTrap.hitChar(r4);
    }


    private class SpearsTrap$2 extends Actor {
        final SpearsTrap this$0;

        SpearsTrap$2(SpearsTrap this$0) {
            this.this$0 = this$0;
        }

        public int actingPriority() {
            return 13;
        }

        protected boolean act() {
            SpearsTrap spearsTrap = this.this$0;
            spearsTrap.chargeSpears(spearsTrap.wall1, spearsTrap.wall2);
            Actor.remove(this);
            return true;
        }
    }
}

