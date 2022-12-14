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

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.actors.blobs.Alchemy;
import com.ravenwolf.nnypd.actors.blobs.AltarPower;
import com.ravenwolf.nnypd.actors.blobs.Blob;
import com.ravenwolf.nnypd.actors.blobs.Darkness;
import com.ravenwolf.nnypd.actors.blobs.ShroudingFog;
import com.ravenwolf.nnypd.actors.blobs.WellWater;
import com.ravenwolf.nnypd.actors.buffs.bonuses.MindVision;
import com.ravenwolf.nnypd.actors.hazards.Hazard;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.actors.mobs.Bestiary;
import com.ravenwolf.nnypd.actors.mobs.Mob;
import com.ravenwolf.nnypd.actors.mobs.Wraith;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.items.Heap;
import com.ravenwolf.nnypd.items.Item;
import com.ravenwolf.nnypd.items.food.RationMedium;
import com.ravenwolf.nnypd.items.misc.Ankh;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.potions.PotionOfStrength;
import com.ravenwolf.nnypd.items.potions.PotionOfWisdom;
import com.ravenwolf.nnypd.items.rings.RingOfFortune;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfEnchantment;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfRemoveCurse;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfUpgrade;
import com.ravenwolf.nnypd.levels.features.Chasm;
import com.ravenwolf.nnypd.levels.features.Door;
import com.ravenwolf.nnypd.levels.painters.Painter;
import com.ravenwolf.nnypd.levels.traps.Trap;
import com.ravenwolf.nnypd.misc.mechanics.ShadowCaster;
import com.ravenwolf.nnypd.misc.utils.BArray;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.effects.CellEmitter;
import com.ravenwolf.nnypd.visuals.effects.Speck;
import com.ravenwolf.nnypd.visuals.effects.particles.FlowParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.LeafParticle;
import com.ravenwolf.nnypd.visuals.effects.particles.WindParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Level implements Bundlable {
	private static final String ALT_EXIT = "alt_exit";
	private static final String BLOBS = "blobs";
	private static final String ENTRANCE = "entrance";
	private static final String EXIT = "exit";
	private static final String FEELING = "feeling";
	private static final String HAZARDS = "hazards";
	private static final String HEAPS = "heaps";
	public static final int HEIGHT = 32;
	public static final int LENGTH = 1024;
	private static final String MAP = "map";
	private static final String MAPPED = "mapped";
	private static final String MOBS = "mobs";
	private static final String MOBS_SPAWNED = "mobs_killed";
	protected static final float TIME_TO_RESPAWN = 65.0f;
	private static final String TRAPS = "traps";
	private static final String VISITED = "visited";
	public static final int WIDTH = 32;
	public static int loadedMapSize;
	public static boolean resizingNeeded;
	public HashMap<Class<? extends Blob>, Blob> blobs;
	public int entrance;
	public int exit;
	public int exitAlternative;
	public HashSet<Hazard> hazards;
	public SparseArray<Heap> heaps;
	public int[] map;
	public boolean[] mapped;
	public HashSet<Mob> mobs;
	private int[] tileVariance;
	public SparseArray<Trap> traps;
	public boolean[] visited;
	public static final int[] NEIGHBOURS1 = {0};
	public static final int[] NEIGHBOURS4 = {-32, 1, 32, -1};
	public static final int[] NEIGHBOURS5 = {0, -32, 1, 32, -1};
	public static final int[] NEIGHBOURSX = {0, -33, -31, 31, 33};
	public static final int[] NEIGHBOURS8 = {1, -1, 32, -32, 33, -31, 31, -33};
	public static final int[] NEIGHBOURS9 = {0, 1, -1, 32, -32, 33, -31, 31, -33};
	public static final int[] NEIGHBOURS12 = {2, 34, -30, -2, 30, -34, 64, 65, 63, -64, -63, -65};
	public static final int[] NEIGHBOURS16 = {2, 34, -30, -2, 30, -34, 64, 65, 63, -64, -63, -65, 66, -62, 62, -66};
	public static boolean[] fieldOfView = new boolean[1024];
	public static boolean[] harmful = new boolean[1024];
	public static boolean[] passable = new boolean[1024];
	public static boolean[] mob_passable = new boolean[1024];
	public static boolean[] losBlockHigh = new boolean[1024];
	public static boolean[] losBlockLow = new boolean[1024];
	public static boolean[] flammable = new boolean[1024];
	public static boolean[] trapped = new boolean[1024];
	public static boolean[] solid = new boolean[1024];
	public static boolean[] avoid = new boolean[1024];
	public static boolean[] water = new boolean[1024];
	public static boolean[] chasm = new boolean[1024];
	public static boolean[] quiet = new boolean[1024];
	public static boolean[] important = new boolean[1024];
	public static boolean[] illusory = new boolean[1024];
	public static boolean[] discoverable = new boolean[1024];
	protected static boolean pitRoomNeeded = false;
	protected static boolean weakFloorCreated = false;
	protected static boolean wellNeeded = false;
	protected static boolean altarNeeded = false;
	protected static boolean specialNPCNeeded = false;
	public int mobsSpawned = 0;
	public Feeling feeling = Feeling.NONE;
	private final Random randGenerator = new Random();
	protected ArrayList<Item> itemsToSpawn = new ArrayList<>();
	public int color1 = 17408;
	public int color2 = 8965188;
	public boolean altarSpawned = false;

	/* loaded from: Level$Feeling.class */
	public enum Feeling {
		NONE,
		CHASM,
		HAUNT,
		TRAPS,
		WATER,
		GRASS,
		PERMAFROST
	}

	protected abstract boolean build();

	protected abstract void createItems();

	protected abstract void createMobs();

	protected abstract void decorate();

	public void create() {
		resizingNeeded = false;
		this.map = new int[1024];
		boolean[] zArr = new boolean[1024];
		this.visited = zArr;
		Arrays.fill(zArr, false);
		boolean[] zArr2 = new boolean[1024];
		this.mapped = zArr2;
		Arrays.fill(zArr2, false);
		this.heaps = new SparseArray<>();
		this.traps = new SparseArray<>();
		this.hazards = new HashSet<>();
		this.mobs = new HashSet<>();
		this.blobs = new HashMap<>();
		boolean z = true;
		if (!Dungeon.bossLevel()) {
			addItemToSpawn(new RationMedium());
			if (Dungeon.posNeeded()) {
				addItemToSpawn(new PotionOfStrength());
				Dungeon.potionOfStrength++;
			}
			if (Dungeon.poeNeeded()) {
				addItemToSpawn(new PotionOfWisdom());
				Dungeon.potionOfExperience++;
			}
			if (Dungeon.souNeeded()) {
				addItemToSpawn(new ScrollOfUpgrade());
				Dungeon.scrollsOfUpgrade++;
			}
			if (Dungeon.soeNeeded()) {
				addItemToSpawn(new ScrollOfEnchantment());
				Dungeon.scrollsOfEnchantment++;
			}
			if (Dungeon.sorcNeeded()) {
				addItemToSpawn(new ScrollOfRemoveCurse());
				Dungeon.scrollsOfRemoveCurse++;
			}
			if (Dungeon.ankhsNeeded()) {
				addItemToSpawn(new Ankh().identify());
				Dungeon.ankhs++;
			}
			if (Dungeon.wandsNeeded()) {
				addItemToSpawn(Generator.random(Generator.Category.WAND));
				Dungeon.wands++;
			}
			if (Dungeon.ringsNeeded()) {
				addItemToSpawn(Generator.random(Generator.Category.RING));
				Dungeon.rings++;
			}
			if (Dungeon.ammosNeeded()) {
				addItemToSpawn(Generator.random(Generator.Category.AMMO));
				Dungeon.ammos++;
			}
			if (Dungeon.torchesNeeded()) {
				addItemToSpawn(new OilLantern.OilFlask());
				Dungeon.torches++;
			}
			int i = Dungeon.depth;
			if (i % 6 != 1 && i < 24) {
				int chapter = Dungeon.chapter();
				switch (com.watabou.utils.Random.Int(12)) {
					case 0:
						if (chapter == 1) {
							this.feeling = Feeling.WATER;
							break;
						} else {
							this.feeling = Feeling.HAUNT;
							break;
						}
					case 1:
						if (chapter == 2) {
							this.feeling = Feeling.HAUNT;
							break;
						} else {
							this.feeling = Feeling.GRASS;
							break;
						}
					case 2:
						if (chapter == 3) {
							this.feeling = Feeling.PERMAFROST;
							break;
						} else {
							this.feeling = Feeling.TRAPS;
							break;
						}
					case 3:
						if (chapter == 4) {
							this.feeling = Feeling.TRAPS;
							break;
						} else {
							this.feeling = Feeling.WATER;
							break;
						}
					case 4:
						if (chapter == 1 || chapter == 4) {
							this.feeling = Feeling.TRAPS;
							break;
						} else {
							this.feeling = Feeling.PERMAFROST;
							break;
						}
				}
			}
		}
		if (Dungeon.depth <= 1 || !weakFloorCreated) {
			z = false;
		}
		wellNeeded = Dungeon.wellNeeded();
		altarNeeded = Dungeon.altarNeeded();
		specialNPCNeeded = Dungeon.specialNPCNeeded();
		do {
			Arrays.fill(this.map, 16);
			pitRoomNeeded = z;
			weakFloorCreated = false;
		} while (!build());
		decorate();
		buildFlagMaps();
		cleanWalls();
		adjustTraps();
		createItems();
		createMobs();
		initTileVariance();
	}

	private void initTileVariance() {
		Random.seed(this.exit + this.entrance);
		this.tileVariance = new int[1024];
		int i = 0;
		while (true) {
			int[] iArr = this.tileVariance;
			if (i < iArr.length) {
				iArr[i] = (byte) Random.Int(100);
				i++;
			} else {
				return;
			}
		}
	}

	public int getTileVariance(int i) {
		return this.tileVariance[i];
	}

	public void reset() {
		Mob[] mobArr;
		for (Mob mob : this.mobs.toArray(new Mob[0])) {
			if (!mob.reset()) {
				this.mobs.remove(mob);
			}
		}
		for (Class<? extends Blob> cls : this.blobs.keySet()) {
			if (cls != WellWater.class && cls != Alchemy.class) {
				this.blobs.remove(cls);
			}
		}
		createMobs();
	}

	/* JADX WARN: Multi-variable type inference failed */
	@Override // com.watabou.utils.Bundlable
	public void restoreFromBundle(Bundle bundle) {
		this.heaps = new SparseArray<>();
		this.hazards = new HashSet<>();
		this.mobs = new HashSet<>();
		this.blobs = new HashMap<>();
		this.traps = new SparseArray<>();
		this.map = bundle.getIntArray(MAP);
		this.visited = bundle.getBooleanArray(VISITED);
		this.mapped = bundle.getBooleanArray(MAPPED);
		this.mobsSpawned = bundle.getInt(MOBS_SPAWNED);
		this.entrance = bundle.getInt(ENTRANCE);
		this.exit = bundle.getInt(EXIT);
		this.exitAlternative = bundle.getInt(ALT_EXIT);
		weakFloorCreated = false;
		String string = bundle.getString(FEELING);
		this.feeling = string.length() > 0 ? Feeling.valueOf(string) : Feeling.NONE;
		Iterator<Bundlable> it = bundle.getCollection(HEAPS).iterator();
		while (it.hasNext()) {
			Heap heap = (Heap) it.next();
			this.heaps.put(heap.pos, heap);
		}
		Iterator<Bundlable> it2 = bundle.getCollection(HAZARDS).iterator();
		while (it2.hasNext()) {
			Hazard hazard = (Hazard) it2.next();
			if (hazard != null) {
				this.hazards.add(hazard);
			}
		}
		Iterator<Bundlable> it3 = bundle.getCollection(MOBS).iterator();
		while (it3.hasNext()) {
			Mob mob = (Mob) it3.next();
			if (mob != null) {
				this.mobs.add(mob);
			}
		}
		Iterator<Bundlable> it4 = bundle.getCollection(BLOBS).iterator();
		while (it4.hasNext()) {
			Blob blob = (Blob) it4.next();
			this.blobs.put(blob.getClass(), blob);
		}
		Iterator<Bundlable> it5 = bundle.getCollection(TRAPS).iterator();
		while (it5.hasNext()) {
			Trap trap = (Trap) it5.next();
			this.traps.put(trap.pos, trap);
		}
		buildFlagMaps();
		cleanWalls();
		initTileVariance();
	}

	@Override // com.watabou.utils.Bundlable
	public void storeInBundle(Bundle bundle) {
		bundle.put(MAP, this.map);
		bundle.put(VISITED, this.visited);
		bundle.put(MAPPED, this.mapped);
		bundle.put(ENTRANCE, this.entrance);
		bundle.put(EXIT, this.exit);
		bundle.put(ALT_EXIT, this.exitAlternative);
		bundle.put(HEAPS, this.heaps.values());
		bundle.put(HAZARDS, this.hazards);
		bundle.put(MOBS, this.mobs);
		bundle.put(BLOBS, this.blobs.values());
		bundle.put(TRAPS, this.traps.values());
		bundle.put(MOBS_SPAWNED, this.mobsSpawned);
		bundle.put(FEELING, this.feeling.toString());
	}

	public int tunnelTile() {
		return 0;
	}

	public int adjustPos(int i) {
		int i2 = loadedMapSize;
		return ((i / i2) * 32) + (i % i2);
	}

	public String tilesTex() {
		return null;
	}

	public String waterTex() {
		return null;
	}

	public String currentTrack() {
		int chapter = Dungeon.chapter();
		String str = Assets.TRACK_CHAPTER_4;
		switch (chapter) {
			case 1:
				return Assets.TRACK_CHAPTER_1;
			case 2:
				return Assets.TRACK_CHAPTER_2;
			case 3:
				return Assets.TRACK_CHAPTER_3;
			case 4:
				return Assets.TRACK_CHAPTER_4;
			case 5:
				if (Dungeon.depth > 25) {
					str = Assets.TRACK_CHAPTER_5;
				}
				return str;
			default:
				return Assets.TRACK_CHAPTER_1;
		}
	}

	public void addVisuals(Scene scene) {
		for (int i = 0; i < 1024; i++) {
			if (chasm[i]) {
				scene.add(new WindParticle.Wind(i));
				if (i >= 32 && water[i - 32]) {
					scene.add(new FlowParticle.Flow(i - 32));
				}
			} else if (this.map[i] == 34) {
				scene.add(new Fountain(i));
			}
			if (this.map[i] == 11 && Dungeon.getVarianceFactor(i) > 70) {
				GameScene.visualOverTerrain(new CityLevel.Smoke(i));
			}
		}
	}

	/* loaded from: Level$Fountain.class */
	private static class Fountain extends Emitter {
		private final int pos;
		private float rippleDelay = 0.0f;

		public Fountain(int i) {
			this.pos = i;
			this.on = true;
		}

		@Override // com.watabou.noosa.particles.Emitter, com.watabou.noosa.Group, com.watabou.noosa.Gizmo
		public void update() {
			boolean[] zArr = Dungeon.visible;
			int i = this.pos;
			boolean z = zArr[i];
			this.visible = z;
			if (z) {
				float f = this.rippleDelay - Game.elapsed;
				this.rippleDelay = f;
				if (f <= 0.0f) {
					GameScene.ripple(i);
					this.rippleDelay = 1.0f;
				}
			}
		}
	}

	public int nMobs() {
		return 0;
	}

	public Actor respawner() {
		return new Level$1(this);
	}

	private class Level$1 extends Actor {
		final Level this$0;

		Level$1(Level this$0) {
			this.this$0 = this$0;
		}

		protected boolean act() {
			Level level = null;
			int i = 0;
			if (this.this$0.mobs.size() < this.this$0.nMobs()) {
				Wraith wraith = (this.this$0.feeling == Feeling.HAUNT && Random.Int(5) == 0) ? new Wraith() : (Wraith) Bestiary.mob(Dungeon.depth);
				wraith.state = wraith.WANDERING;
				wraith.pos = this.this$0.randomRespawnCell(wraith.flying, false);
				if (Dungeon.hero.isAlive() && wraith.pos != -1) {
					this.this$0.mobsSpawned++;
					GameScene.add(wraith);
					if (Statistics.amuletObtained) {
						wraith.beckon(Dungeon.hero.pos);
					}
				}
			}
			float chapter = 65.0f - (Dungeon.chapter() * 2);
			if (this.this$0.feeling == Level.Feeling.TRAPS) {
				i = 10;
			}
			spend((chapter - i) + level.mobsSpawned);
			return true;
		}
	}

	public boolean NPCSafePos(int i) {
		int i2;
		int i3 = 0;
		int i4 = 0;
		while (i4 < NEIGHBOURS8.length) {
			int i5 = i + i4;
			if (!solid[i5] && !chasm[i5]) {
				i2 = i3;
				if (passable[i5]) {
					i4++;
					i3 = i2;
				}
			}
			i2 = i3 + 1;
			i4++;
			i3 = i2;
		}
		return i3 < 5;
	}

	public int randomRespawnCell() {
		return randomRespawnCell(false, false);
	}

	public int randomRespawnCell(boolean z, boolean z2) {
		ArrayList<Integer> passableCellsList = getPassableCellsList();
		ArrayList<Integer> arrayList = passableCellsList;
		if (!z) {
			arrayList = filterTrappedCells(passableCellsList);
		}
		ArrayList<Integer> arrayList2 = arrayList;
		if (!z2) {
			arrayList2 = filterVisibleCells(arrayList);
		}
		return !arrayList2.isEmpty() ? Random.element(arrayList2).intValue() : -1;
	}

	public ArrayList<Integer> getPassableCellsList() {
		ArrayList<Integer> arrayList = new ArrayList<>();
		for (int i = 32; i < 1024; i++) {
			if (!solid[i] && passable[i] && Actor.findChar(i) == null) {
				arrayList.add(Integer.valueOf(i));
			}
		}
		return arrayList;
	}

	public ArrayList<Integer> filterTrappedCells(ArrayList<Integer> arrayList) {
		ArrayList<Integer> arrayList2 = new ArrayList<>();
		Iterator<Integer> it = arrayList.iterator();
		while (it.hasNext()) {
			Integer next = it.next();
			if (mob_passable[next.intValue()]) {
				arrayList2.add(next);
			}
		}
		return arrayList2;
	}

	public ArrayList<Integer> filterVisibleCells(ArrayList<Integer> arrayList) {
		ArrayList<Integer> arrayList2 = new ArrayList<>();
		Iterator<Integer> it = arrayList.iterator();
		while (it.hasNext()) {
			Integer next = it.next();
			if (!Dungeon.visible[next.intValue()] && distance(Dungeon.hero.pos, next.intValue()) > 8) {
				arrayList2.add(next);
			}
		}
		return arrayList2;
	}

	public Integer getRandomCell(ArrayList<Integer> arrayList) {
		return Random.element(arrayList);
	}

	public int randomDestination() {
		int Int;
		do {
			Int = com.watabou.utils.Random.Int(1024);
		} while (!mob_passable[Int]);
		return Int;
	}

	public void addItemToSpawn(Item item) {
		if (item != null) {
			this.itemsToSpawn.add(item);
		}
	}

	public Item itemToSpawnAsPrize() {
		if (com.watabou.utils.Random.Int(this.itemsToSpawn.size() + 1) > 0) {
			Item item = Random.element(this.itemsToSpawn);
			this.itemsToSpawn.remove(item);
			return item;
		}
		return null;
	}

	public Item itemToSpawnAsPrize(Class<? extends Item> cls) {
		Iterator<Item> it = this.itemsToSpawn.iterator();
		while (it.hasNext()) {
			Item next = it.next();
			if (cls.isInstance(next)) {
				this.itemsToSpawn.remove(next);
				return next;
			}
		}
		return null;
	}

	public void updateNearbyWater(int i) {
		int i2 = 0;
		while (true) {
			int[] iArr = NEIGHBOURS4;
			if (i2 < iArr.length) {
				int i3 = iArr[i2] + i;
				if (water[i3]) {
					int i4 = 64;
					int i5 = 0;
					while (true) {
						int[] iArr2 = NEIGHBOURS4;
						if (i5 >= iArr2.length) {
							break;
						}
						int i6 = i4;
						if ((Terrain.flags[this.map[iArr2[i5] + i3]] & Terrain.UNSTITCHABLE) != 0) {
							i6 = i4 + (1 << i5);
						}
						i5++;
						i4 = i6;
					}
					this.map[i3] = i4;
					GameScene.updateMap(i3);
				}
				i2++;
			} else {
				return;
			}
		}
	}

	private void buildFlagMaps() {
		for (int i = 0; i < 1024; i++) {
			int[] iArr = Terrain.flags;
			int[] iArr2 = this.map;
			int i2 = iArr[iArr2[i]];
			boolean[] zArr = passable;
			zArr[i] = (i2 & 1) != 0;
			mob_passable[i] = (zArr[i] && (i2 & 8) == 0) || (i2 & 1024) != 0;
			boolean[] zArr2 = losBlockLow;
			zArr2[i] = (i2 & 2) != 0;
			losBlockHigh[i] = zArr2[i] && (i2 & 16) != 0;
			flammable[i] = (i2 & 4) != 0;
			trapped[i] = (i2 & 8) != 0;
			solid[i] = (i2 & 16) != 0;
			avoid[i] = (i2 & 32) != 0;
			water[i] = (i2 & 64) != 0;
			chasm[i] = (i2 & 128) != 0;
			important[i] = (i2 & Terrain.IMPORTANT) != 0;
			illusory[i] = (i2 & 1024) != 0;
			boolean[] zArr3 = quiet;
			boolean z = iArr2[i] == 9;
			zArr3[i] = z;
		}
		for (int i3 = 0; i3 < 32; i3++) {
			boolean[] zArr4 = passable;
			boolean[] zArr5 = mob_passable;
			boolean[] zArr6 = avoid;
			zArr6[i3] = false;
			zArr5[i3] = false;
			zArr4[i3] = false;
			zArr6[992 + i3] = false;
			zArr5[992 + i3] = false;
			zArr4[992 + i3] = false;
		}
		for (int i4 = 32; i4 < 992; i4 += 32) {
			boolean[] zArr7 = passable;
			boolean[] zArr8 = mob_passable;
			boolean[] zArr9 = avoid;
			zArr9[i4] = false;
			zArr8[i4] = false;
			zArr7[i4] = false;
			zArr9[(i4 + 32) - 1] = false;
			zArr8[(i4 + 32) - 1] = false;
			zArr7[(i4 + 32) - 1] = false;
		}
		for (int i5 = 32; i5 < 992; i5++) {
			if (water[i5]) {
				int i6 = 64;
				int i7 = 0;
				while (true) {
					int[] iArr3 = NEIGHBOURS4;
					if (i7 >= iArr3.length) {
						break;
					}
					int i8 = i6;
					if ((Terrain.flags[this.map[iArr3[i7] + i5]] & Terrain.UNSTITCHABLE) != 0) {
						i8 = i6 + (1 << i7);
					}
					i7++;
					i6 = i8;
				}
				this.map[i5] = i6;
			}
			boolean[] zArr10 = chasm;
			if (zArr10[i5] && !zArr10[i5 - 32]) {
				int[] iArr4 = this.map;
				int i9 = iArr4[i5 - 32];
				if (i9 == 2 || i9 == 36) {
					iArr4[i5] = 27;
				} else if (water[i5 - 32]) {
					iArr4[i5] = 29;
				} else if ((Terrain.flags[i9] & Terrain.UNSTITCHABLE) != 0) {
					iArr4[i5] = 28;
				} else {
					iArr4[i5] = 26;
				}
			}
		}
	}

	private void adjustTraps() {
		for (Trap trap : this.traps.values()) {
			trap.adjustTrap(this);
		}
	}

	private void cleanWalls() {
		boolean z;
		for (int i = 0; i < 1024; i++) {
			int i2 = 0;
			while (true) {
				int[] iArr = NEIGHBOURS9;
				z = false;
				if (i2 >= iArr.length) {
					break;
				}
				int i3 = iArr[i2] + i;
				if (i3 >= 0 && i3 < 1024) {
					int[] iArr2 = this.map;
					if (iArr2[i3] != 16 && iArr2[i3] != 18 && iArr2[i3] != 19) {
						z = true;
						break;
					}
				}
				i2++;
			}
			boolean z2 = z;
			if (z) {
				int i4 = 0;
				while (true) {
					int[] iArr3 = NEIGHBOURS9;
					z2 = false;
					if (i4 >= iArr3.length) {
						break;
					}
					int i5 = iArr3[i4] + i;
					if (i5 < 0 || i5 >= 1024 || chasm[i5]) {
						i4++;
					} else {
						z2 = true;
						break;
					}
				}
			}
			discoverable[i] = z2;
		}
	}

	public static void set(int i, int i2) {
		int i3 = Terrain.flags[i2];
		boolean[] zArr = passable;
		zArr[i] = (i3 & 1) != 0;
		mob_passable[i] = (zArr[i] && (i3 & 8) == 0) || (i3 & 1024) != 0;
		boolean[] zArr2 = losBlockLow;
		zArr2[i] = (i3 & 2) != 0;
		losBlockHigh[i] = zArr2[i] && (i3 & 16) != 0;
		flammable[i] = (i3 & 4) != 0;
		trapped[i] = (i3 & 8) != 0;
		solid[i] = (i3 & 16) != 0;
		avoid[i] = (i3 & 32) != 0;
		chasm[i] = (i3 & 128) != 0;
		water[i] = (i3 & 64) != 0;
		important[i] = (i3 & Terrain.IMPORTANT) != 0;
		illusory[i] = (i3 & 1024) != 0;
		boolean[] zArr3 = quiet;
		boolean z = i2 == 9;
		zArr3[i] = z;
		Painter.set(Dungeon.level, i, i2);
	}

	public Heap drop(Item item, int i) {
		return drop(item, i, false);
	}

	/* JADX WARN: Code restructure failed: missing block: B:27:0x0092, code lost:
        if ((r5 instanceof com.ravenwolf.nonameyetpixeldungeon.items.bags.ScrollHolder) != false) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
	public com.ravenwolf.nnypd.items.Heap drop(com.ravenwolf.nnypd.items.Item r5, int r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 389
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
		throw new UnsupportedOperationException("Method not decompiled: com.ravenwolf.nonameyetpixeldungeon.levels.Level.drop(com.ravenwolf.nonameyetpixeldungeon.items.Item, int, boolean):com.ravenwolf.nonameyetpixeldungeon.items.Heap");
	}

	public int pitCell() {
		return randomRespawnCell();
	}

	public float stealthModifier(int i) {
		return water[i] ? 0.75f : quiet[i] ? 1.25f : 1.0f;
	}

	public Trap setTrap(Trap trap, int i) {
		if (this.traps.get(i) != null) {
			this.traps.remove(i);
		}
		trap.set(i);
		this.traps.put(i, trap);
		GameScene.updateMap(i);
		return trap;
	}

	public void disarmTrap(int i) {
		set(i, 15);
		GameScene.updateMap(i);
	}

	public void discover(int i) {
		set(i, Terrain.discover(this.map[i]));
		Trap trap = this.traps.get(i);
		if (trap != null) {
			trap.reveal();
		} else {
			GameScene.updateMap(i);
		}
	}

	public void press(int i, Char r7) {
		press(i, r7, false);
	}

	public void press(int i, Char r9, boolean z) {
		switch (this.map[i]) {
			case 17:
				Door.discover(i);
				break;
			case 32:
				if (AltarPower.isMagicAltar(i)) {
					AltarPower.affectCell(i);
					break;
				}
				break;
			case 34:
				WellWater.affectCell(i);
				break;
			case 37:
				if (r9 == null) {
					Alchemy.transmute(i);
					break;
				}
				break;
			case 48:
				Door.enter(i);
				break;
		}
		Iterator<Hazard> it = Hazard.findHazards(i).iterator();
		while (it.hasNext()) {
			it.next().press(i, r9);
		}
		if (r9 == null || !r9.flying) {
			if (chasm[i]) {
				if (r9 == Dungeon.hero) {
					Chasm.heroFall(i);
					return;
				} else if (r9 instanceof Mob) {
					Chasm.mobFall((Mob) r9);
					return;
				} else {
					return;
				}
			}
			switch (this.map[i]) {
				case 9:
					if (Dungeon.visible[i]) {
						CellEmitter.get(i).burst(LeafParticle.LEVEL_SPECIFIC, com.watabou.utils.Random.IntRange(2, 4));
						break;
					}
					break;
				case 10:
					if (Dungeon.visible[i]) {
						CellEmitter.get(i).burst(LeafParticle.LEVEL_SPECIFIC, com.watabou.utils.Random.IntRange(1, 3));
						break;
					}
					break;
				case 13:
				case 14:
					Trap trap = this.traps.get(i);
					if (trap != null) {
						if (!z && r9 == null && !trap.canBeSearched) {
							return;
						}
						if (r9 == Dungeon.hero && com.watabou.utils.Random.Float() < Dungeon.hero.ringBuffsBaseZero(RingOfFortune.Fortune.class) / 2.0f) {
							GLog.i("For some reason this trap fails to trigger.");
							CellEmitter.get(r9.pos).burst(Speck.factory(7), 4);
							trap.disarm();
							return;
						}
						trap.trigger();
						break;
					}
					break;
			}
			if (Dungeon.visible[i]) {
				if (water[i]) {
					GameScene.ripple(i);
					if (r9 != null) {
						Sample.INSTANCE.play(Assets.SND_WATER, 0.8f, 0.8f, com.watabou.utils.Random.Float(0.7f, 1.0f));
					}
				} else if (r9 != null) {
					Sample.INSTANCE.play(Assets.SND_STEP, 0.5f, 0.5f, 0.8f);
				}
			}
		}
	}

	public boolean[] updateFieldOfView(Char r7) {
		Blob blob;
		int i = r7.pos;
		int i2 = i / 32;
		boolean[] clone = r7.flying ? losBlockHigh.clone() : losBlockLow.clone();
		Blob blob2 = Dungeon.level.blobs.get(Darkness.class);
		if (!r7.ignoreDarkness() && blob2 != null) {
			BArray.or(clone, blob2.bln, clone);
		}
		if (!r7.isFriendly() && (blob = Dungeon.level.blobs.get(ShroudingFog.class)) != null) {
			BArray.or(clone, blob.bln, clone);
		}
		ShadowCaster.castShadow(i % 32, i2, fieldOfView, r7.viewDistance(), clone);
		if (r7.isAlive() && (r7 instanceof Hero)) {
			Iterator<Mob> it = this.mobs.iterator();
			while (true) {
				if (!it.hasNext()) {
					break;
				}
				Mob next = it.next();
				if (next.sharedVision()) {
					for (int i3 : NEIGHBOURS9) {
						fieldOfView[next.pos + i3] = true;
					}
				}
			}
			if (r7.buff(MindVision.class) != null) {
				Iterator<Mob> it2 = this.mobs.iterator();
				while (it2.hasNext()) {
					Mob next2 = it2.next();
					if (next2.mindVision()) {
						for (int i4 : NEIGHBOURS9) {
							fieldOfView[next2.pos + i4] = true;
						}
					}
				}
				for (Heap heap : this.heaps.values()) {
					if (heap.type == Heap.Type.CHEST_MIMIC) {
						for (int i5 : NEIGHBOURS9) {
							fieldOfView[heap.pos + i5] = true;
						}
					}
				}
			}
		}
		return fieldOfView;
	}

	public static boolean insideMap(int i) {
		return i >= 32 && i < 992 && i % 32 != 0 && i % 32 != 31;
	}

	public Point cellToPoint(int i) {
		return new Point(i % 32, i / 32);
	}

	public int pointToCell(Point point) {
		return point.x + (point.y * 32);
	}

	public static int distance(int i, int i2) {
		return Math.max(Math.abs((i % 32) - (i2 % 32)), Math.abs((i / 32) - (i2 / 32)));
	}

	/* JADX WARN: Code restructure failed: missing block: B:9:0x0024, code lost:
        if (r0 != 33) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
	public static boolean adjacent(int r3, int r4, boolean r5) {
        /*
            r0 = r3
            r1 = r4
            int r0 = r0 - r1
            int r0 = java.lang.Math.abs(r0)
            r3 = r0
            r0 = 1
            r6 = r0
            r0 = r6
            r7 = r0
            r0 = r3
            r1 = 1
            if (r0 == r1) goto L3a
            r0 = r6
            r7 = r0
            r0 = r3
            r1 = 32
            if (r0 == r1) goto L3a
            r0 = r5
            if (r0 == 0) goto L27
            r0 = r6
            r7 = r0
            r0 = r3
            r1 = 33
            if (r0 == r1) goto L3a
        L27:
            r0 = r5
            if (r0 == 0) goto L37
            r0 = r3
            r1 = 31
            if (r0 != r1) goto L37
            r0 = r6
            r7 = r0
            goto L3a
        L37:
            r0 = 0
            r7 = r0
        L3a:
            r0 = r7
            return r0
        */
		throw new UnsupportedOperationException("Method not decompiled: com.ravenwolf.nonameyetpixeldungeon.levels.Level.adjacent(int, int, boolean):boolean");
	}

	public static boolean adjacent(int i, int i2) {
		return adjacent(i, i2, true);
	}

	public String tileName(int i) {
		return tileNames(i);
	}

	public String tileDesc(int i) {
		return tileDescs(i);
	}

	public static String tileNames(int i) {
		if (i >= 64) {
			return tileNames(79);
		}
		if (i != 25 && (Terrain.flags[i] & 128) != 0) {
			return tileNames(25);
		}
		switch (i) {
			case 0:
			case 1:
			case 2:
			case 14:
			case 15:
				return "Floor";
			case 3:
				return "Bone pit";
			case 9:
				return "High grass";
			case 10:
				return "Grass";
			case 11:
			case 12:
				return "Embers";
			case 16:
			case 17:
			case 18:
				return "Wall";
			case 19:
			case 42:
				return "Sign";
			case 25:
				return "Chasm";
			case 32:
				return "Pedestal";
			case 33:
				return "Empty well";
			case 34:
				return "Well";
			case 35:
			case 36:
			case 43:
			case 45:
			case 46:
			case 47:
				return "Statue";
			case 37:
				return "Alchemy pot";
			case 38:
				return "Barricade";
			case 39:
			case 40:
				return "Bookshelf";
			case 48:
				return "Closed door";
			case 49:
				return "Open door";
			case 50:
				return "Locked door";
			case 53:
				return "Locked depth exit";
			case 54:
				return "Unlocked depth exit";
			case 55:
				return "Depth entrance";
			case 56:
				return "Depth exit";
			case 59:
				return "Iron grate";
			case 79:
				return "Water";
			default:
				return "???";
		}
	}

	public static String tileDescs(int i) {
		switch (i) {
			case 9:
				return "Dense vegetation blocks the view and hushes your steps, making it easier to move undetected through it.";
			case 11:
			case 12:
				return "Embers cover the floor.";
			case 16:
			case 17:
			case 18:
				return "Just a wall, nothing special. Mind that fighting in close spaces restricts ability to dodge.";
			case 19:
				return "There is something written on this wall.";
			case 25:
				return "You can't see the bottom. Fighting near chasms limits movement, restricting ability to dodge (unless you are flying).";
			case 33:
				return "The well has run dry.";
			case 35:
			case 36:
			case 43:
				return "Someone wanted to adorn this place, but failed, obviously.";
			case 37:
				return "Drop some herbs here to cook a potion.";
			case 38:
				return "The wooden barricade is firmly set but has dried over the years. Might it burn?";
			case 42:
				return "Somebody placed a sign here..";
			case 50:
				return "This door is locked, you need a matching key to unlock it.";
			case 53:
				return "Heavy bars block the stairs leading down.";
			case 54:
			case 56:
				return "Stairs lead down to the lower depth.";
			case 55:
				return "Stairs lead up to the upper depth.";
			case 59:
				return "Heavy bars block the path, you can see throw it though.";
			case 79:
				return "Step in the water to extinguish fire. However, don't forget that walking in the water is noisy and may attract unwanted attention!";
			default:
				if (i >= 64) {
					return tileDescs(79);
				}
				if ((Terrain.flags[i] & 128) != 0) {
					return tileDescs(25);
				}
				return "";
		}
	}
}
