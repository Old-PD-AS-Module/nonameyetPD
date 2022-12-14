package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.mechanics.Ballistica;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class SideWallTrap extends Trap {
    private static final String SHAPE = "shape";
    private static final String WALL1 = "wall1";
    private static final String WALL2 = "wall2";
    protected int wall1;
    protected int wall2;

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap
    public void adjustTrap(Level level) {
        boolean validHorizontal = false;
        boolean validVertical = false;
        int i = this.pos;
        int leftWall = Ballistica.cast(i, i - 1, true, false) - 1;
        int i2 = this.pos;
        int rightWall = Ballistica.cast(i2, i2 + 1, true, false) + 1;
        int horizontalDist = Level.distance(leftWall, rightWall);
        if (horizontalDist < 9 && (Terrain.isRealWall(level.map[leftWall]) || Terrain.isRealWall(level.map[rightWall]))) {
            validHorizontal = true;
        }
        int i3 = this.pos;
        int topWall = Ballistica.cast(i3, i3 - 32, true, false) - 32;
        int i4 = this.pos;
        int bottomWall = Ballistica.cast(i4, i4 + 32, true, false) + 32;
        int verticalDist = Level.distance(topWall, bottomWall);
        if (verticalDist < 9 && (Terrain.isRealWall(level.map[topWall]) || Terrain.isRealWall(level.map[bottomWall]))) {
            validVertical = true;
        }
        if (validHorizontal || validVertical) {
            if (!validVertical || (validHorizontal && Random.Float() < 0.5f)) {
                this.wall1 = leftWall;
                this.wall2 = rightWall;
                this.shape = 6;
                return;
            }
            this.wall1 = topWall;
            this.wall2 = bottomWall;
            this.shape = 7;
            return;
        }
        level.traps.remove(this.pos);
        int[] iArr = level.map;
        int i5 = this.pos;
        iArr[i5] = 0;
        Level.trapped[i5] = false;
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap, com.watabou.utils.Bundlable
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        this.shape = bundle.getInt(SHAPE);
        this.wall1 = bundle.getInt(WALL1);
        this.wall2 = bundle.getInt(WALL2);
    }

    @Override // com.ravenwolf.nonameyetpixeldungeon.levels.traps.Trap, com.watabou.utils.Bundlable
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SHAPE, this.shape);
        bundle.put(WALL1, this.wall1);
        bundle.put(WALL2, this.wall2);
    }
}