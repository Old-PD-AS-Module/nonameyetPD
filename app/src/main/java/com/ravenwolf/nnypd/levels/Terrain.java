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
package com.ravenwolf.nnypd.levels;

public class Terrain {
	public static final int ALCHEMY = 37;
	public static final int AVOID = 32;
	public static final int BARRICADE = 38;
	public static final int BARRICADE_ISO = 105;
	public static final int BOOKSHELF = 39;
	public static final int BOOKSHELF_EMPTY_ISO = 87;
	public static final int BOOKSHELF_ISO = 86;
	public static final int CHASM = 25;
	public static final int CHASM_FLOOR = 26;
	public static final int CHASM_FLOOR_SP = 27;
	public static final int CHASM_WALL = 28;
	public static final int CHASM_WALL_FLAT = 109;
	public static final int CHASM_WATER = 29;
	public static final int CLOSED_DOOR_ISO_H = 96;
	public static final int CLOSED_DOOR_ISO_V = 100;
	public static final int DOOR_CLOSED = 48;
	public static final int DOOR_ILLUSORY = 17;
	public static final int EMBERS = 11;
	public static final int EMBERS_DOOR = 12;
	public static final int EMBERS_DOOR_ISO_H = 103;
	public static final int EMBERS_DOOR_ISO_V = 104;
	public static final int EMPTY = 0;
	public static final int EMPTY_DECO = 1;
	public static final int EMPTY_SP = 2;
	public static final int EMPTY_SP2 = 3;
	public static final int EMPTY_WELL = 33;
	public static final int ENTRANCE = 55;
	public static final int EXIT = 56;
	public static final int FLAMMABLE = 4;
	public static final int FLOOR_VARIATION_OFFSET = 4;
	public static final int GRASS = 10;
	public static final int HIGH_GRASS = 9;
	public static final int ILLUSORY = 1024;
	public static final int IMPORTANT = 512;
	public static final int INACTIVE_TRAP = 15;
	public static final int LIQUID = 64;
	public static final int LOCKED_DOOR = 50;
	public static final int LOCKED_DOOR_ISO_H = 98;
	public static final int LOCKED_DOOR_ISO_V = 102;
	public static final int LOCKED_EXIT = 53;
	public static final int LOCKED_EXIT_ISO = 107;
	public static final int LOS_BLOCKING = 2;
	public static final int OPEN_DOOR = 49;
	public static final int OPEN_DOOR_ISO_H = 97;
	public static final int OPEN_DOOR_ISO_V = 101;
	public static final int PASSABLE = 1;
	public static final int PEDESTAL = 32;
	public static final int PIT = 128;
	public static final int RAISED_BARRICADE = 119;
	public static final int RAISED_CLOSED_DOOR_H = 115;
	public static final int RAISED_CLOSED_DOOR_V = 117;
	public static final int RAISED_EMPTY = 120;
	public static final int RAISED_EXIT = 122;
	public static final int RAISED_HIGH_GRASS = 123;
	public static final int RAISED_LOCKED_DOOR_V = 118;
	public static final int RAISED_OPEN_DOOR_H = 116;
	public static final int RAISED_WALL = 113;
	public static final int RAISED_WALL_DOOR_V = 114;
	public static final int RAISED_WALL_FLAT = 112;
	public static final int SECRET_TRAP = 14;
	public static final int SHELF_EMPTY = 40;
	public static final int SIGN = 42;
	public static final int SOLID = 16;
	public static final int STATUE = 35;
	public static final int STATUE2 = 44;
	public static final int STATUE2_SP = 43;
	public static final int STATUE_BRUTE = 47;
	public static final int STATUE_FROG = 45;
	public static final int STATUE_SHAMAN = 46;
	public static final int STATUE_SP = 36;
	public static final int TRAP = 13;
	public static final int TRAPPED = 8;
	public static final int UNLOCKED_EXIT = 54;
	public static final int UNLOCKED_EXIT_ISO = 106;
	public static final int UNSTITCHABLE = 256;
	public static final int WALL = 16;
	public static final int WALL_DECO = 18;
	public static final int WALL_DECO_ISO = 83;
	public static final int WALL_DOOR_ISO_V = 99;
	public static final int WALL_GRATE = 59;
	public static final int WALL_GRATE_WATER_ISO = 108;
	public static final int WALL_ISO = 80;
	public static final int WALL_SIGN = 19;
	public static final int WALL_SIGN_ISO = 85;
	public static final int WATER = 79;
	public static final int WATER_TILES = 64;
	public static final int WELL = 34;
	public static final int[] flags;

	static {
		int[] iArr = new int[UNSTITCHABLE];
		flags = iArr;
		iArr[25] = 416;
		iArr[0] = 1;
		iArr[10] = 5;
		iArr[33] = 544;
		iArr[79] = 321;
		iArr[16] = 274;
		iArr[48] = 23;
		iArr[49] = 517;
		iArr[55] = 513;
		iArr[56] = 513;
		iArr[12] = 1;
		iArr[11] = 1;
		iArr[50] = 274;
		iArr[32] = 769;
		iArr[18] = iArr[16];
		iArr[38] = 22;
		iArr[2] = iArr[0] | UNSTITCHABLE;
		iArr[9] = 7;
		iArr[1] = iArr[0];
		iArr[53] = 16;
		iArr[54] = 513;
		iArr[42] = 517;
		iArr[19] = iArr[16];
		iArr[34] = 544;
		iArr[35] = 16;
		iArr[36] = 256 | iArr[35];
		iArr[43] = iArr[36];
		iArr[59] = iArr[36];
		iArr[39] = iArr[38];
		iArr[40] = iArr[39];
		iArr[37] = 545;
		iArr[44] = iArr[35];
		iArr[45] = iArr[35];
		iArr[47] = iArr[35];
		iArr[46] = iArr[35];
		iArr[3] = iArr[0];
		iArr[28] = iArr[25];
		iArr[26] = iArr[25];
		iArr[27] = iArr[25];
		iArr[29] = iArr[25];
		iArr[17] = 1302;
		iArr[13] = 32;
		iArr[14] = iArr[0] | 8;
		iArr[15] = iArr[0];
		for (int i = 64; i < 80; i++) {
			int[] iArr2 = flags;
			iArr2[i] = iArr2[79];
		}
	}

	public static int discover(int terr) {
		switch (terr) {
			case SECRET_TRAP:
				return 13;
			case DOOR_ILLUSORY:
				return 48;
			default:
				return terr;
		}
	}

	public static boolean isRealWall(int terrain) {
		return terrain == 16 || terrain == 18 || terrain == 19;
	}

	public static boolean isWall(int terrain) {
		return terrain == 16 || terrain == 18 || terrain == 19 || terrain == 59 || terrain == 17 || terrain == 53 || terrain == 54;
	}

	public static boolean isOpenDoor(int terrain) {
		return terrain == 49;
	}

	public static boolean isClosedDoor(int terrain) {
		return terrain == 48;
	}

	public static boolean isLockedDoor(int terrain) {
		return terrain == 50;
	}

	public static boolean isAnyDoor(int terrain) {
		return isOpenDoor(terrain) || isClosedDoor(terrain) || isLockedDoor(terrain);
	}

	public static boolean isChapterExit(int terrain) {
		return terrain == 53 || terrain == 54;
	}

	public static int getEmberTile(int oldTerrain) {
		if (isAnyDoor(oldTerrain)) {
			return 12;
		}
		return 11;
	}
}
