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
package com.ravenwolf.nnypd.levels.painters;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.PrisonGuard;
import com.ravenwolf.nnypd.actors.mobs.Robot;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.keys.IronKey;
import com.ravenwolf.nnypd.items.keys.SkeletonKey;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Room;
import com.ravenwolf.nnypd.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class KeyKeeperPainter extends Painter {

	public static void paint( Level level, Room room ) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY);

        Room.Door door = room.entrance();

        door.set(Room.Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey());

        int x = -1;
        int y = -1;

        Point a = null;
        Point exitA = null;

        Point b = null;
        Point exitB = null;

        x = room.left + room.width() / 2;
        y = room.top + 1;

        a = new Point(x + 2, y);
        b = new Point(x - 2, y);

        exitA = new Point(x + 1, room.top);
        exitB = new Point(x - 1, room.top);


        if (a != null) {
            set(level, a, Terrain.PEDESTAL);
        }
        if (b != null) {
            set(level, b, Terrain.STATUE);
        }

        int pos = x + y * Level.WIDTH;

        level.drop(new SkeletonKey(), pos, true).type = Heap.Type.CHEST;

        Mob enemy = null;
        if (Dungeon.depth == Dungeon.CAVES_PATHWAY)
            enemy = new Robot();
        if (Dungeon.depth == Dungeon.PRISON_PATHWAY)
            enemy = new PrisonGuard();
        if (enemy != null){
            enemy.pos = pos;

            level.mobs.add(enemy);
                enemy.state=enemy.PASSIVE;
            Actor.occupyCell(enemy);
        }

        level.exit = exitA.x+exitA.y * Level.WIDTH;
        level.exitAlternative = exitB.x+exitB.y * Level.WIDTH;

        level.map[level.exit] = Terrain.LOCKED_EXIT;
        level.map[level.exitAlternative] = Terrain.LOCKED_EXIT;
        if(Random.Int(3)>0){
                if(Random.Int(2)==0)
                        level.map[level.exit] = Terrain.WALL_SIGN;
                else
                        level.map[level.exitAlternative] = Terrain.WALL_SIGN;
        }

	}
}
