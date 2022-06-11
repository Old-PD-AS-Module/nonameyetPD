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
package com.ravenwolf.nnypd.scenes;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.Statistics;
import com.ravenwolf.nnypd.actors.Actor;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.visuals.windows.WndError;
import com.ravenwolf.nnypd.visuals.windows.WndStory;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;

import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.5f;

	private static final String TXT_DESCENDING	= "下楼中...";
	private static final String TXT_ASCENDING	= "上楼中...";
	private static final String TXT_LOADING		= "少女祈祷中...";
	private static final String TXT_RESURRECTING= "复活中...";
	private static final String TXT_RETURNING	= "返回中...";
	private static final String TXT_FALLING		= "坠落中...";
	private static final String TXT_CONTINUE	= "点击继续!";

	private static final String ERR_FILE_NOT_FOUND	= "文件缺失";
	private static final String ERR_GENERIC			= "发生了一些意想不到的错误..."	;
	
	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
	}

    public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;
	
	public static boolean noStory = false;
	
	public static boolean fallIntoPit;

	private static final String[] TIPS = {

			// GENERAL

			"在地牢中每5层都会出现一个商店供你消费金币",
			"地牢中至少存在3个复活十字架，但仍有很低的概率找到更多",

			"陷阱房与食人鱼房间的物品中更容易出现未受诅咒的道具",
			//"Special rooms with tombs or animated statues will never have their prize cursed",

			"感知属性影响侦察陷阱和周边隐藏门所需的时间",
			"感知属性影响你被动发现周边隐藏门的概率",
			"感知属性影响你探索或睡眠时听见周遭敌人声音的几率",
			"感知属性影响你格挡和弹反敌人的概率",
			"感知属性影响你在攻击时打出暴击的概率",

			"潜行属性影响你被敌人发现的概率",
			"潜行属性影响你伏击敌人的概率",
			"潜行属性影响你伏击伤害的加成",

			"意志属性影响你所以法杖和魔咒的充能速率",
			"意志属性影响你识别诅咒物品的几率",
			"意志属性影响你抵抗诅咒触发的概率，比如法杖和魔咒的错误释放",
			//"Your chance to prevent equipping a cursed item depends on your Willpower",
			//"Willpower influences your chances to miscast with a wand or squeeze additional charge",
			"在饥饿状态下，你的意志力会降低",

			"魔能属性影响一些卷轴释放的效果",
			"魔能属性影响你法杖的伤害",
			"魔能属性影响你魔咒的强度",
			//"Your magic skill improves damage of your wand of Magic Missile",

			"较高的力量增加你在被缠绕时挣脱的机会",
			//"Your health regeneration rate grows with levels and potions of Strength used",

			"当你睡觉时，你的生命恢复速度是平时的3倍",
			"当饥饿，燃烧，或者中毒时，你的自然生命恢复会停止",

			"随着地牢的深入，掉落物和特殊房间的数量也会随之提高",
			"尽可能保证你的角色等级高于或等同于当前层数",

			"可升级物品的等级上限是+5，不用担心，这就已经足够了",
			"每个武器都可能会打出暴击，其暴击效果取决于武器类型",
			//"Upgraded items are more durable, but cursed items break much faster",

			"监狱和矿洞的区域可能会有其他分支延展",

			"不要忘了，你可以在游戏设置中关闭这些提示！",
			//"More tips will be added later!",

			// WEAPONS & ARMOURS

			"你可以在副手槽中使用近战武器来辅助主手槽中的远程武器",
			"盾牌可以用来猛击敌人，但不会造成太大伤害，但它会击退被弹反的敌人",

			"使用超重武器会降低你的攻击速度",
			"强大的武器通常会进一步降低你的精准和潜行属性",

			"使用超重的护甲或盾牌时会降低你的移动速度",
			"强大的护甲或盾牌通常会进一步降低你的灵巧和潜行属性",

			//"Your chance to block an attack depends on armor class of your shield and damage of your weapon",
			"格挡成功的概率取决于你持有的盾牌或武器的防御力",
			"成功格挡有概率弹反你的敌人，让你获得弹反敌人的空当",

			"额外的力量则会降低重型装备的惩罚",
			//"Stronger flintlock weapons require more gunpowder to reload",

			"在被雷电击中时可能会被麻痹一段时间",
			"你可以通过长时间使用武器，护甲，法杖和戒指来自然鉴定其性质",

			//"Flintlock weapons do greater and will penetrate the target's armor",
			//"You can craft makeshift bombs from excess gunpowder",

			"你可以将多枚炸药打包成炸药包，进一步增加其破坏力",
			//"You can dismantle bomb bundles or sticks to obtain some of their components",

			"升级特殊布甲则会增加相应地属性",
			"睡觉时你不会承受来自装备的潜行减益",

			// WANDS & RINGS

			//"Combat wands always have a chance to miscast, which mostly depends on their quality level",
			"诅咒的法杖有概率会施法失败，对自身释放和目标一样的效果",
			//"Combat wands have a chance to squeeze additional charge, which mostly depends on their quality level",

			"魔咒的充能速率受当前充能数的影响",
			"魔咒会一次性释放所有充能",

			//"Some rings can be kept only to equip them for certain occasions",
			"两个相同的戒指属性加成会叠加",

			"通常每区域至少存在1个法杖，但仍有很低的概率找到更多",
			"通常每区域至少存在1个戒指，但仍有很低的概率找到更多",

			// POTIONS

			"每个区域通常只有1瓶经验药剂，但仍有很低的概率找到更多",
			"经验药剂还能够提高等级上限，允许你将等级进一步的提高",

			"每个区域通常只有2瓶力量药剂，玩家找到更多的概率很低",
			"力量药剂可以用来恢复血量，并且是最有效的恢复方式",

			"每个商店至少会出售一瓶治疗药剂",
			"治疗药剂还能够治疗中毒，流血等大多数物理减益效果",

			"灵视药剂甚至可以让你无视低失明所带来的惩罚",
			"灵视药剂还会在效力期间提高你的感知属性",

			"浮空药剂在效力期间会提供额外的移动速度和潜行属性",
			"跳入虚空时，你甚至可以借助浮空药剂安全降落",

			"使用卷轴或者法杖之类的物品时同样会中断隐形效果",
			"敌人在移动时也可能会撞破你的隐形",

			"饮用圣盾药剂时会提高你的物抗和法抗",
			//"Throw a potion of Blessing on adjacent tile to weaken curses on items in your inventory",

			"液火药剂不会蔓延到水面上",
			"液火药剂会点燃附近的易燃单位",

			"你可用冰霜药剂迅速熄灭房间内的火焰",
			"冰霜药剂对站在水中的目标更强力",

			"使用酸蚀药剂时要小心，这些气体是高度易燃的",
			"腐蚀药剂更适合用来对付大群敌人",

			"再生药剂在对已经覆盖了植物的地面上使用会更有效",
			//"You can farm plants for alchemy with help of potions of Overgrowth",

			//"Using a potion of Thunderstorm can attract wandering monsters",
			//"Potions of Thunderstorm can be used to flood the dungeon floor or to extinguish fires",

			// SCROLLS

			//"There is usually 1 scroll of Enchantment per chapter but there is a low chance to find more",
			"注魔卷轴还会清除物品上的诅咒",

			"通常每区域至少有2张升级卷轴，玩家有极低的概率会找到更多",
			//"Uncursing an enchanted item with scroll of Upgrade or enchantment will clear the enchantment from the weapon",

			"合理的使用鉴定卷轴能节约你很多时间",
			"在每个商店中至少会提供一张鉴定卷轴",

			//"Scrolls of Transmutation will never change an item into the same item",
			//"Scrolls of Transmutation will always keep the rarity of the item",

			//"Scrolls of Sunlight can be used to counteract effect of a potion of Thunderstorm",
			//"Never forget that scroll of Sunlight can heal some of your enemies, too",
			"阳光卷轴会治疗你和你的盟友",

			"探地卷轴并不会显示出隐藏的门和陷阱，只会显示出各个房间和物品",
			//"Area revealed by a scroll of Clairvoyance cannot be erased by a scroll of Phase Warp",

			"放逐卷轴可以用来针对亡灵，元素和魔像类怪物",
			//"Scrolls of Banishment will dispel curses from all of the items in your inventory",

			//"Enemies blinded by a scroll of Darkness can fall into a chasm or step into a trap",
			"失明的敌人可能会跌入虚空或踩进陷阱",
			//"Scrolls of Darkness can be used to counteract effects of scrolls of Sunlight",

			"传送卷轴可以在危机时刻救你一命，同样也可能会把你带入危机之中",
			//"Using a scrolls of Phase Warp will confuse you for a short period",

			"死灵卷轴对付单个目标时效果非常致命，包括你在内",
			"由死灵卷轴召唤的恶灵，它的支配效果会在一段时间后消失",

			//"Using scrolls of Challenge is an effective, but risky way to farm experience faster",
			"盛怒卷轴可以用来引诱出拟型怪和护甲亡灵",

			"使用灵爆卷轴时，若视野内没有更多的敌人则会对自身造成严重的伤害",
			"灵爆卷轴无法对没有灵魂的单位生效",

			// FOOD

			"每个楼层至少会有一个口粮，不过也要留意隐藏的房间",
			"部分怪物会掉落生肉，甚至小份口粮",

			"饱腹状态会增加你的生命恢复速率",
			"饥饿时会大幅降低你的意志力",
			//"Eating raw meat can poison you - better cook it by burning or freezing it",

			//"Chargrilled meat doesn't have any additional advantages besides being edible",
			"冻肉吃起来非常美味，甚至可以恢复你的部分血量",

			"有时你可能会找到更多的食物，但概率很低",
			"你可以在商店里购买一些食物，而且它是很有价值的",

			"食用时药草时会有不同的特性，合理地使用它们可以增强你的生存能力",

			// BOSSES

			"大多数boss都会执行狂暴状态，且最多三次",
			"尽量避免和狂暴状态的boss战斗，逃跑或躲避会是更好的选择",
			"boss更容易受到爆炸、药剂和卷轴的攻击影响",

			"要注意的是，粘咕释放的不洁气体极度易燃",
			"矮人国王的仪式可以被某个卷轴打断...",

			"当受到更大的威胁时，天狗会更频繁的闪现",
			"DM-300既不是有机生物，也不是魔法生物",

			"美杜莎可以吸收雕像的能量",
			"当美杜莎凝视的时候，尽量避免直接看她",

			"死灵法师召唤的憎恶，不能离开骸骨堆，但是他会用链钩拖拽远处的敌人",

			// TERRAIN

			"如果你想悄悄接近某人，则尽量避免在水中移动",
			"如果你想悄悄接近某人，可以考虑躲在高草丛中",

			"飞行生物视野可以越过高草，并且不受地面的效果影响",
			"水会增强雷电和霜冻效果，同时也可以扑灭火焰，清洗酸性物质",

			"陷阱只会在普通的房间中出现，并不会出现在走廊或特殊房间中",
			"在地牢中居住的怪物对陷阱和隐藏门的位置非常了解",

			//"Sleeping in the water is much less efficient than sleeping anywhere else",
			"当所有的临近地面都被占用或不可通行时，你的闪避几率会被降低",

	};
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;
	
    //private BitmapText            message;
    //private ArrayList<BitmapText> tipBox;

    private RenderedText message;
    private ArrayList<RenderedText> tipBox;

    private Thread thread;
    private String error = null;
    private boolean pause = false;

	@Override
	public void create() {
		super.create();

		if (mode != Mode.CONTINUE && Dungeon.level != null && Dungeon.hero != null)
			Dungeon.hero.onLeaveLevel(Dungeon.level);
		
		String text = "";
//        int depth = Dungeon.depth;

		switch (mode) {
            case DESCEND:
                text = TXT_DESCENDING;
//                depth++;
                break;
            case ASCEND:
                text = TXT_ASCENDING;
//                depth--;
                break;
            case CONTINUE:
                text = TXT_LOADING;

//                GamesInProgress.Info info = GamesInProgress.check( StartScene.curClass );

//                if (info != null) {
//
//                    depth = info.depth;
//
//                }

//                depth = depth > 0 ? depth : 0 ;

                break;
            case RESURRECT:
                text = TXT_RESURRECTING;
                break;
            case RETURN:
                text = TXT_RETURNING;
//                depth = returnDepth;
                break;
            case FALL:
                text = TXT_FALLING;
//                depth++;
                break;
		}
		
		/*message = PixelScene.createText( text, 10 );
		message.measure();*/
		message = PixelScene.renderText( text, 10 );
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 2;
		add(message);
        align(message);

        tipBox = new ArrayList<>();

        if( NoNameYetPixelDungeon.loadingTips() > 0 ) {

            /*BitmapTextMultiline tip = PixelScene.createMultiline(TIPS[Random.Int(TIPS.length)], 6);
            tip.maxWidth = Camera.main.width * 9 / 10;
            tip.measure();

            for (BitmapText line : tip.new LineSplitter().split()) {
                line.measure();
                line.x = PixelScene.align(Camera.main.width / 2 - line.width() / 2);
                line.y = PixelScene.align(Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * line.height());
                tipBox.add(line);
                add(line);
            }*/
			RenderedTextMultiline tip = PixelScene.renderMultiline(TIPS[Random.Int(TIPS.length)], 6);
            tip.maxWidth(Camera.main.width * 9 / 10);
            tip.setPos(Camera.main.width / 2 - tip.width() / 2,Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * tip.height());
            align(tip);
            add(tip);
        }


		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;
		
		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();
					
					switch (mode) {
					case DESCEND:
						descend();
						break;
					case ASCEND:
						ascend();
						break;
					case CONTINUE:
						restore();
						break;
//					case RESURRECT:
//                        resurrect();
//                        break;
					case RETURN:
						returnTo();
						break;
					case FALL:
						fall();
						break;
					}

					if ((Dungeon.depth % 6) == 0 && Dungeon.depth == Statistics.deepestFloor ) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}

                    if( mode != Mode.CONTINUE ) {
                        Dungeon.saveAll();
                        Badges.saveGlobal();
                    }
					
				} catch (FileNotFoundException e) {
					
					error = ERR_FILE_NOT_FOUND;
					
				} catch (Exception e) {

					error = e.toString();
                    NoNameYetPixelDungeon.reportException(e);
					
				}

//                error = ERR_FILE_NOT_FOUND;
				
				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE * 2;
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:

			message.alpha( 1 - p );

            /*for (BitmapText line : tipBox) {
                line.alpha( 1 - p );
            }*/

			if ((timeLeft -= Game.elapsed) <= 0) {
				if (thread.isAlive() || error != null || NoNameYetPixelDungeon.loadingTips() > 2 ) {
                    phase = Phase.STATIC;

                    if( !thread.isAlive() && error == null) {
                        message.text(TXT_CONTINUE);
                        //message.measure();
                        message.x = (Camera.main.width - message.width()) / 2;
                        message.y = (Camera.main.height - message.height()) / 2;
                        align(message);

                        TouchArea hotArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height) {
                            @Override
                            protected void onClick(Touchscreen.Touch touch) {
                                phase = Phase.FADE_OUT;
                                timeLeft = TIME_TO_FADE;
                                this.destroy();
                            }
                        };
                        add(hotArea);
                    }

                } else {
                    phase = Phase.FADE_OUT;
                    timeLeft = ( NoNameYetPixelDungeon.loadingTips() > 0 ?
                            TIME_TO_FADE * NoNameYetPixelDungeon.loadingTips() * 3 : TIME_TO_FADE );
                }
			}
			break;
			
		case FADE_OUT:

			message.alpha( p );

            /*for (BitmapText line : tipBox) {
                line.alpha( p );
            }*/

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p );
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:

            if (error != null) {

                add(new WndError(error) {
                    public void onBackPressed() {
                        super.onBackPressed();
                        Game.switchScene(StartScene.class);
                    }
                });

                error = null;

            }
			break;
		}
	}
	
	private void descend() throws Exception {
		
		Actor.fixTime();

		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
			Dungeon.saveAll();
			Dungeon.saveBackup();
		}
		
		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, level.entrance );
	}
	
	private void fall() throws Exception {
		
		Actor.fixTime();
		Dungeon.saveAll();
		Dungeon.saveBackup();
		
		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell() );
	}
	
	private void ascend() throws Exception {
		Actor.fixTime();
		
		Dungeon.saveAll();
		Dungeon.saveBackup();
		Dungeon.depth--;
		Level level = Dungeon.
                loadLevel( Dungeon.hero.heroClass );

		int exit=level.exit;

		if (Dungeon.depth==Dungeon.CAVES_PATHWAY && Dungeon.RUINS_OPTION.equals(Dungeon.cavesOption) ||
				Dungeon.depth==Dungeon.PRISON_PATHWAY && Dungeon.GRAVEYARD_OPTION.equals(Dungeon.prisonOption))
			exit=level.exitAlternative;

		Dungeon.switchLevel( level, exit );
	}

	private void returnTo() throws Exception {
		
		Actor.fixTime();
		
		Dungeon.saveAll();
		Dungeon.saveBackup();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
	}
	
	private void restore() throws Exception {

		Actor.fixTime();

		//if fail load main, try to load backup
		//backup is only saved when descending
		//and only last level is stored
		boolean failLoadMain=false;

		try{
			Dungeon.loadGame(StartScene.curClass);
		}catch (Exception e){
			failLoadMain= true;
			Dungeon.loadGame( Dungeon.BACKUP_PREFIX + Dungeon.gameFile( StartScene.curClass ), true );
		}

		Level level;
		if (failLoadMain){
			level=Dungeon.loadLevel(StartScene.curClass);
		}else {
			try{
				level=Dungeon.loadLevel(StartScene.curClass);
			}catch (Exception e){
				Dungeon.loadGame( Dungeon.BACKUP_PREFIX + Dungeon.gameFile( StartScene.curClass ), true );
				level=Dungeon.loadLevel(StartScene.curClass);
			}
		}
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( level, -1 );
		} else {
			Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}

//	private void resurrect() throws Exception {
//
//        Actor.fixTime();
//
//        if (Dungeon.bossLevel()) {
//
//            Dungeon.hero.resurrect( Dungeon.depth );
//            Dungeon.depth--;
//            Level level = Dungeon.newLevel();
//            Dungeon.switchLevel( level, level.entrance );
//
//        } else {
//
//            Dungeon.hero.resurrect(-1);
//            Actor.clear();
//            Arrays.fill(Dungeon.visible, false);
//            Dungeon.level.fail();
//            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
//
//        }
//    }

	@Override
	protected void onBackPressed() {
		// Do nothing
	}
}
