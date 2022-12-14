package com.ravenwolf.nnypd;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public abstract class BaseTilemap extends Tilemap {
    public static final int SIZE = 16;

    public BaseTilemap(String tex) {
        super(tex, new TextureFilm(tex, 16, 16));
    }

    public BaseTilemap(String tex, TextureFilm tileset) {
        super(tex, tileset);
    }

    public int screenToTile(int x, int y) {
        int i;
        Point p = camera().screenToCamera(x, y).offset(point().negate()).invScale(16.0f).floor();
        int i2 = p.x;
        if (i2 < 0 || i2 >= 32 || (i = p.y) < 0 || i >= 32) {
            return -1;
        }
        return i2 + (i * 32);
    }

    @Override // com.watabou.noosa.Visual
    public boolean overlapsPoint(float x, float y) {
        return true;
    }

    public static PointF tileToWorld(int pos) {
        return new PointF(pos % 32, pos / 32).scale(16.0f);
    }

    public static PointF tileCenterToWorld(int pos) {
        return new PointF(((pos % 32) + 0.5f) * 16.0f, (((pos / 32) + 0.5f) * 16.0f) - Dungeon.isometricOffset());
    }

    @Override // com.watabou.noosa.Visual
    public boolean overlapsScreenPoint(int x, int y) {
        return true;
    }

    public int getTileVariation(int pos) {
        return Dungeon.getVarianceFactor(pos) > 75 ? 1 : 0;
    }

    public int getTileVariationWitRare(int pos) {
        if (Dungeon.getVarianceFactor(pos) < 8) {
            return 2;
        }
        return Dungeon.getVarianceFactor(pos) > 75 ? 1 : 0;
    }
}
