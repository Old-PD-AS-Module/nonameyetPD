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
package com.ravenwolf.nnypd.levels.traps;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Trap implements Bundlable {
    private static final String ACTIVE = "active";
    public static final int ARROWS_H = 6;
    public static final int ARROWS_V = 7;
    public static final int BLACK = 8;
    private static final String CAN_BE_SEARCHED = "canBeSearched";
    public static final int CROSS = 5;
    public static final int DIAMOND = 4;
    private static final String DISARMED_BY_ACTIVATION = "disarmedByActivation";
    public static final int DOTS = 0;
    public static final int GREEN = 3;
    public static final int GREY = 7;
    public static final int GRILL = 2;
    public static final int LINES = 1;
    public static final int ORANGE = 1;
    private static final String POS = "pos";
    public static final int RED = 0;
    public static final int STARS = 3;
    public static final int TEAL = 4;
    private static final String TXT_HIDDEN_PLATE_CLICKS = "A hidden pressure plate clicks!";
    private static final String TXT_NO = "No, I changed my mind";
    private static final String TXT_R_U_SURE = "You are aware of a trap on this tile. Once you step on it, the trap would be activated, which would most likely be quite a painful experience. Are you REALLY sure you want to step here?";
    private static final String TXT_TRAPPED = "This tile is trapped!";
    private static final String TXT_YES = "Yes, I know what I'm doing";
    public static final int VIOLET = 5;
    private static final String VISIBLE = "visible";
    public static final int WHITE = 6;
    public static final int YELLOW = 2;
    public static boolean stepConfirmed = false;
    public int color;
    public int pos;
    public int shape;
    public boolean visible;
    public boolean active = true;
    public boolean disarmedByActivation = true;
    public boolean canBeHidden = true;
    public boolean canBeSearched = true;
    public boolean avoidsHallways = false;

    public abstract void activate();

    public Trap set(int pos) {
        this.pos = pos;
        return this;
    }

    public void adjustTrap(Level level) {
    }

    public Trap reveal() {
        this.visible = true;
        GameScene.updateMap(this.pos);
        return this;
    }

    public Trap hide() {
        if (!this.canBeHidden) {
            return reveal();
        }
        this.visible = false;
        GameScene.updateMap(this.pos);
        return this;
    }

    public void trigger() {
        if (this.active) {
            if (Dungeon.visible[this.pos]) {
                Sample.INSTANCE.play(Assets.SND_TRAP);
                if ((Terrain.flags[Dungeon.level.map[this.pos]] & 8) != 0) {
                    GLog.i(TXT_HIDDEN_PLATE_CLICKS);
                }
                Char findChar = Actor.findChar(this.pos);
                Hero hero = Dungeon.hero;
                if (findChar == hero) {
                    hero.interrupt();
                }
            }
            if (this.disarmedByActivation) {
                disarm();
            }
            Dungeon.level.discover(this.pos);
            activate();
        }
    }


    public void disarm() {
        this.active = false;
        this.visible = true;
        Dungeon.level.disarmTrap(this.pos);
    }

    public static void askForConfirmation(Hero hero) {
        GameScene.show(new Trap$1(TXT_TRAPPED, TXT_R_U_SURE, new String[]{TXT_YES, TXT_NO}, hero));
    }

    public String name() {
        return null;
    }

    public String desc() {
        StringBuilder sb = new StringBuilder();
        sb.append(description());
        sb.append(this.disarmedByActivation ? "" : dontDisarmOnActivationText());
        return sb.toString();
    }

    public String description() {
        return "Stepping onto a hidden pressure plate will activate the trap.";
    }

    protected String dontDisarmOnActivationText() {
        return "\n\nThis trap is designed to only trigger when stepped in, and wont wer of upon activation.";
    }

    public void restoreFromBundle(Bundle bundle) {
        this.pos = bundle.getInt(POS);
        this.visible = bundle.getBoolean(VISIBLE);
        this.active = bundle.getBoolean(ACTIVE);
        this.disarmedByActivation = bundle.getBoolean(DISARMED_BY_ACTIVATION);
        this.canBeSearched = bundle.getBoolean(CAN_BE_SEARCHED);
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(POS, this.pos);
        bundle.put(VISIBLE, this.visible);
        bundle.put(ACTIVE, this.active);
        bundle.put(CAN_BE_SEARCHED, this.canBeSearched);
        bundle.put(DISARMED_BY_ACTIVATION, this.disarmedByActivation);
    }

    private static class Trap$1 extends WndOptions {
        final Hero val$hero;

        Trap$1(String arg0, String arg1, String[] arg2, Hero hero) {
            super(arg0, arg1, arg2);
            this.val$hero = hero;
        }

        protected void onSelect(int index) {
            if (index == 0) {
                Trap.stepConfirmed = true;
                this.val$hero.resume();
                Trap.stepConfirmed = false;
            }
        }
    }
}
