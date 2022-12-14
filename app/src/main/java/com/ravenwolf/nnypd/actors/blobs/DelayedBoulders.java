package com.ravenwolf.nnypd.actors.blobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.buffs.BuffActive;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.MagicMissile;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class DelayedBoulders extends Blob {
    public DelayedBoulders() {
        this.name = "Falling boulder";
    }

    public void spend(float time) {
        DelayedBoulders.super.spend(time);
    }

    public int actingPriority() {
        return 10;
    }

    protected void evolve() {
        boolean heard = false;
        for (int i = 0; i < 1024; i++) {
            if (this.cur[i] > 0) {
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    int effect = this.cur[i];
                    ch.damage(ch.absorb(Random.Int((effect * 2) / 3, effect), false), this, Element.PHYSICAL);
                    if (ch.isAlive()) {
                        BuffActive.add(ch, Dazed.class, 3.0f);
                    }
                }
                Heap heap = Dungeon.level.heaps.get(i);
                if (heap != null) {
                    heap.shatter("Boulders");
                }
                Dungeon.level.press(i, null);
                if (Dungeon.visible[i]) {
                    CellEmitter.get(i).start(Speck.factory(8), 0.1f, 4);
                    heard = true;
                }
                this.cur[i] = 0;
            }
        }
        if (heard) {
            Sample.INSTANCE.play("snd_rocks.mp3", 0.5f, 0.5f, 1.5f);
        }
    }

    public void use(BlobEmitter emitter) {
        DelayedBoulders.super.use(emitter);
        emitter.pour(MagicMissile.EarthParticle.FALLING, 0.2f);
    }

    public String tileDesc() {
        return "Loose rocks are tumbling down from the ceiling here, it looks like its about to collapse!";
    }
}

