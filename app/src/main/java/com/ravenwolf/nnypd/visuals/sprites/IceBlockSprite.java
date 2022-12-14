package com.ravenwolf.nnypd.visuals.sprites;

import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

/* loaded from: D:\SXDS\0.5.dex */
public class IceBlockSprite extends MobSprite {
    protected Emitter sparkles;

    public IceBlockSprite() {
        texture(Assets.ICEBLOCK);
        TextureFilm frames = new TextureFilm(this.texture, 16, 16);
        MovieClip.Animation animation = new MovieClip.Animation(5, true);
        this.idle = animation;
        animation.frames(frames, 4, 5, 6, 7, 4, 4, 4, 4);
        this.run = this.idle.clone();
        this.attack = this.idle.clone();
        MovieClip.Animation animation2 = new MovieClip.Animation(10, false);
        this.spawn = animation2;
        animation2.frames(frames, 0, 1, 3, 4);
        MovieClip.Animation animation3 = new MovieClip.Animation(10, false);
        this.die = animation3;
        animation3.frames(frames, 4, 3, 2, 1, 0, 8);
        play(this.idle);
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.visuals.sprites.CharSprite
    public int blood() {
        return CharSprite.DEFAULT;
    }
}
