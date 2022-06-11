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
package com.ravenwolf.nnypd.actors.mobs;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.actors.Char;
import com.watabou.utils.Random;

public abstract class Bestiary {

	public static Mob mob( int depth ) {
		/*@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}*/
		return mutable( depth );
	}
	
	public static Mob mutable( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );

		if (cl == Rat.class && Random.Int( 20-depth )==0)
			cl = Albino.class;
		if (cl == SewerCrab.class && Random.Int( 16-depth )==0)
			cl = HermitCrab.class;
		if (cl == Skeleton.class && Random.Int( 18-(depth-7) )==0)
			cl = SkeletonWarrior.class;
		if (cl == CarrionSwarm.class && Random.Int( 22-(depth-7) )==0)
			cl = Bandit.class;
		if (cl == GnollShaman.class && Random.Int( 14-(depth-13) )==0)
			cl = GnollWitchDoctor.class;
		if (cl == GnollBrute.class && Random.Int( 16-(depth-13) )==0)
			cl = GnollBerserker.class;
		if (cl == VampireBat.class && Random.Int( 16-(depth-13) )==0)
			cl = ShadowBat.class;
		if (cl == DwarfMonk.class && Random.Int( 20-(depth-19) )==0)
			cl = DwarfSeniorMonk.class;
		if (cl == Elemental.class) {
			if( Random.Int( 20-(depth-19) )==0)
				cl = BlazingElemental.class;
			else if( Random.Int( 2 )==0)
				cl = FrostElemental.class;
		}

//		if (Random.Int( 30 ) == 0) {
//			if (cl == Rat.class) {
//				cl = Albino.class;
//			} else if (cl == Thief.class) {
//				cl = Bandit.class;
//			} else if (cl == Brute.class) {
//				cl = Shielded.class;
//			} else if (cl == Monk.class) {
//				cl = Senior.class;
//			} else if (cl == Scorpio.class) {
//				cl = Acidic.class;
//			}
//		}
		
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Class<?> mobClass( int depth ) {
		
		float[] chances;
		Class<?>[] classes;
		
		switch (depth) {
		case 1:
			chances = new float[]{ 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class };
			break;
		case 2:
			chances = new float[]{ 3, 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class };
			break;
		case 3:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class  };
			break;
		case 4:
			chances = new float[]{ 3, 2, 1, 1 };
			classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class };
			break;
        case 5:
            chances = new float[]{ 2, 1, 1, 1, 1, 0.05f };
            classes = new Class<?>[]{ Rat.class, Thief.class, GnollHunter.class, SewerCrab.class, GooSpawn.class,  Skeleton.class };
            break;

		case 6:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Goo.Mother.class };
			break;

		case 7:
			chances = new float[]{ 2, 1, 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, GnollHunter.class };
			break;
		case 8:
			chances = new float[]{ 3, 2, 1 , 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, GnollHunter.class  };
			break;
		case 9:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, GnollHunter.class };
			break;
		case 10:
			chances = new float[]{ 3, 2, 1, 1, 1, 0.02f };
			if (Dungeon.GRAVEYARD_OPTION.equals(Dungeon.prisonOption))
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, GiantSpider.class, GnollHunter.class, VampireBat.class };
			else
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, PrisonGuard.class, GnollHunter.class, VampireBat.class };
			break;
		case 11:
			chances = new float[]{ 2, 1, 1, 1, 1, 0.05f };
			if (Dungeon.GRAVEYARD_OPTION.equals(Dungeon.prisonOption))
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, GiantSpider.class, GnollHunter.class, VampireBat.class};
			else
				classes = new Class<?>[]{ CarrionSwarm.class, Skeleton.class, PlagueDoctor.class, PrisonGuard.class, GnollHunter.class, VampireBat.class};
			break;
		case 12:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Tengu.class };
			break;

		case 13:
			chances = new float[]{ 2, 1, 1, 1 };
			classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, GnollShaman.class, GnollHunter.class };
			break;

		case 14:
			chances = new float[]{ 2, 2, 1, 1};
			classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, GnollShaman.class, GnollHunter.class };
			break;
		case 15:
			chances = new float[]{ 3, 3, 2, 1, 1 };
			if (Dungeon.MINES_OPTION.equals(Dungeon.cavesOption))
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, Robot.class,  GnollShaman.class, GnollHunter.class };
			else
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class,  GnollShaman.class, GnollHunter.class };
			break;
		case 16:
			chances = new float[]{ 3, 2, 1, 1, /*1,*/ 1, 0.02f };
			if (Dungeon.MINES_OPTION.equals(Dungeon.cavesOption))
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class,Robot.class, CaveScorpion.class, GnollShaman.class, /*GnollHunter.class,*/ DwarfMonk.class };
			else
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, GiantFrog.class, GnollShaman.class,/* GnollHunter.class,*/ DwarfMonk.class };
			break;
		case 17:
			chances = new float[]{ 2, 1, 1, 1, /*1,*/ 1, 0.05f, 0.02f };
			if (Dungeon.MINES_OPTION.equals(Dungeon.cavesOption))
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, Robot.class, CaveScorpion.class, GnollShaman.class, /*GnollHunter.class,*/ DwarfMonk.class, Elemental.class };
			else
				classes = new Class<?>[]{ VampireBat.class, GnollBrute.class, EvilEye.class, GiantFrog.class, GnollShaman.class,/* GnollHunter.class,*/ DwarfMonk.class, Elemental.class };

			break;
		case 18:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ DM300.class };
			break;

		case 19:
			chances = new float[]{ 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class };
			break;
		case 20:
			chances = new float[]{ 3, 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class };
			break;
		case 21:
			chances = new float[]{ 4, 3, 2, 1 };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class };
			break;
		case 22:
			chances = new float[]{ 3, 2, 1, 1, 0.02f };
			classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class, Imp.class };
			break;
        case 23:
            chances = new float[]{ 2, 1, 1, 1, 0.05f, 0.02f };
            classes = new Class<?>[]{ DwarfMonk.class, Elemental.class, DwarfWarlock.class, Golem.class, Imp.class, Succubus.class };
            break;

		case 24:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ DwarvenKing.class };
			break;

		case 26:
			chances = new float[]{ 2, 1,0.25f };
			classes = new Class<?>[]{ Imp.class, Succubus.class, Fiend.class  };
			break;
		case 27:
			chances = new float[]{ 3, 2, 1, 1 };
			classes = new Class<?>[]{ Imp.class, Succubus.class, Fiend.class, Blackguard.class };
			break;
		case 28:
			chances = new float[]{ 4, 3, 2, 2, 1 };
			classes = new Class<?>[]{ Imp.class, Succubus.class, Demon.class, Fiend.class, Blackguard.class };
			break;
        case 29:
            chances = new float[]{ 1, 1, 1, 1, 1 };
            classes = new Class<?>[]{ Imp.class, Succubus.class, Demon.class, Fiend.class, Blackguard.class };
            break;

		case 30:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Yog.class };
			break;
			
		default:
			chances = new float[]{ 1 };
			classes = new Class<?>[]{ Rat.class };
		}
		
		return classes[ Random.chances( chances )];
	}
	
	public static boolean isBoss( Char mob ) {
		return mob instanceof Goo || mob instanceof Tengu || mob instanceof DM300 || mob instanceof Medusa || mob instanceof DwarvenKing
                || mob instanceof Yog || mob instanceof Yog.RottingFist || mob instanceof Yog.BurningFist || mob instanceof Necromancer || mob instanceof Necromancer.Abomination;
	}
}
