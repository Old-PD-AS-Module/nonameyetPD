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
package com.ravenwolf.nnypd.visuals.windows;

import com.ravenwolf.nnypd.NoNameYetPixelDungeon;
import com.ravenwolf.nnypd.visuals.Assets;
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.ScrollPane;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

public class WndTutorial extends WndTabbed {

	private static final int WIDTH_P	= 112;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 128;
	private static final int HEIGHT_L	= 128;

    private static SmartTexture icons;
    private static TextureFilm film;

    private static final String TXT_TITLE	= "指南";

	private static final String[] TXT_LABELS = {
        "I", "II", "III", "IV", "V",
    };

    private static final String[] TXT_TITLES = {
            "指南 - 基本界面",
            "指南 - 游戏机制",
            "指南 - 装备介绍",
            "指南 - 消耗物品",
            "指南 - 怪物类别",
    };

    private static final String[][] TXT_POINTS = {
            {
                    "在游戏中，几乎所有操作都可以通过直接点击目标来完成。这包括但不限于移动，攻击，拾起物品以及与NPC和其他地牢元素的互动。",
                    "通过点击左上角的角色头像来查看你当前拥有的属性和效果。如果其中一个属性正受到未鉴定物品的影响，它的值将显示为\"??\"。",
                    "点击左下角的时钟按钮可以跳过一个回合。长按该按钮可以进行睡眠——这会让你以更快地速度跳过回合，但也会大幅提高生命回复速度。",
                    "取决于你在游戏中的设置，这个按钮允许你点击两次或长按以进行一次周边搜索，让你发现周边的隐藏暗门或陷阱。当然，你也可以轻点按钮并选中任何地牢中的物品，以阅读它们的描述并获知其当前状态。",
                    "这个按钮位于屏幕右下角。点击它即可查看你的背包(想必你已经知道了)。长按它可以查看你当前持有的各类钥匙。",
                    "在背包按钮左侧有三个快捷栏，你可以利用快捷栏省略打开背包的步骤直接使用物品。长按快捷栏以添加或切换物品。",
                    "在屏幕最右侧有三个操作按钮，分别是格挡按钮(只有装备盾牌和近战武器时才会显示，长按则会拿起盾牌猛击敌人)，远程攻击按钮(只有装备了投武或具有相应箭矢的弓弩时才会显示，点击即可进行一次射击)，以及魔具攻击按钮(只有当你装备了法杖或魔咒时才会显示，点击即可进行一次施法)。",
                    /*"There is an offhand quickslot right above the inventory button. Its effect depends on the combination of weapons you have equipped at the moment. For example, it will shoot if wands or ranged weapons are equipped.",*/
                    "还有几个默认隐藏的按钮，分别是危险指示器，攻击，拾取和继续按钮。危险标记上会用数字标识出当前视野内的敌人数量。",
                    "点击危险指示器可以选取一名敌人，然后点击攻击按钮就可以在不点击怪物贴图的情况下攻敌人。以及，长按攻击按钮会显示目标的描述和当前状态。",
                    "只有当人物站在可拾取物品的正上方时才会出现拾取提示。点击拾取按钮即可直接拾取这些物品。而不需要再去点击角色",
                    "角色头像的下方可以看到一个按钮，这个按钮可以在不打开背包的情况下直接喝下水袋中的水。你也可以长按此按钮，将水从水袋中倾倒出来。",
                    "水袋按钮下方还有一个油灯相关的按钮。它的使用方法相对智能——点击它即可点亮，熄灭和添加灯油等操作。长按则会让你把油灯倒在周围一格并点燃。",
            },
            {
                    "游戏中绝大多数动作都只消耗一个回合，这意味着一次攻击和移动一格所花费的时间完全一致，绝大多数生物的移动速度适用此规则。记住，指令一旦下达动作就会执行，你需要在所有动作都结算完成后才能继续行动。",
                    "敌人有一定几率注意到你的存在。该几率取决于它们的感知与你的潜行属性值。同时也取决于你和敌人的距离，不过处于追击状态的敌人不论如何都会发现视野中的你。",
                    "不过，敌人在失去你视野的时候，它们可能会无法继续追击，并给予你伏击它们的机会。这类情况发生的概率很大程度上取决于你的潜行值。你可以(并且应当)利用墙角，房门或茂密植被来实现这个战术。不过别忘记飞行生物可以看穿茂密植被内的隐蔽。",
                    "执行攻击或受到攻击时，游戏决定的第一件事是该攻击是否命中。通常情况下，命中的概率取决于攻击者命中属性和防御者的敏捷属性。这些属性在不同的职业和怪物间都有着不同的成长速率。",
                    "防御方身侧每拥有一个无法通行的地格，它的敏捷属性就会降低1/16。这意味着如果你想让自己的攻击更易命中，你应当将敌人引诱到狭长的走廊上；若想让自己更容易闪躲敌人的攻击，你就应当坚守空旷的区域。",
                    "所有远程攻击的实际命中率都会因攻击距离的增加而每格减少1/8。法杖中唯一可以被闪避的法杖便是魔弹法杖。其命中率由你的魔能属性决定。",
                    "玩家角色(及部分后期怪物)，在第二次攻击连续命中后，每次连击都会略微增加攻击所造成伤害。会以\"连击\"字样显示在角色的上方及状态栏，并且显著提高你在对抗大量怪物时的效率。",
                    "你的感知属性会影响你发现周围陷阱或隐藏门的几率，以及通过墙壁感知到敌人行动的几率。要注意的是，随着你进一步深入地牢时，隐藏的陷阱和门会变得更加难以发现。",
                    "高草是最适合伏击的理想地形，因其既能阻挡视野，也能提高你的潜行。而在水面上行动时则正好相反，会使敌人更容易发现你，并减少你的潜行值。",
                    "睡眠是最佳的恢复手段。在你长按时钟睡眠时，你会获得平时三倍的自然恢复速率。然而，在水面上睡眠不会获得任何加成，不过你依然可以在需要的时快速跳过回合。",
                    "主动探索或点亮油灯可以确保你对周围的隐藏环境的感知(如陷阱和隐藏门，现版本无名中点燃油灯只能侦测到隐藏门，但是提高的感知会让你更容易发现陷阱)。需要注意的是，在地牢中点亮灯光也会让敌人更轻易的注意到你的存在。",
                    "你的意志属性能够显著影响法杖的充能速度，并且影响在装备未识别物品时，察觉到诅咒并防止装备的几率。魔能属性影响法杖的效果和伤害，同时也会影响部分攻击性卷轴的效果。",

            },
            {
                    "近战武器被分为多种类别。其中最基本的类别就是轻型单手武器，它们没有任何特殊效果或减益，并且与其他装备搭配使用时不会获得额外的力量惩罚。作为副手武器与主手的远程武器搭配时，在近战时，允许使用它进行近战攻击，而不是使用在近战中有额外惩罚的远程武器进行攻击",
                    "重型单手武器与轻型单手武器非常相似，但如果作为副手武器使用，它们就会获得额外的力量惩罚",
                    "轻型双手武器基本上是各不同类型的长柄武器。大多数都具有额外的攻击距离。并不能装备到副手，但是仍可以和盾牌一起使用。",
                    "重型双手武器并不推荐与盾牌和其他武器使用，因为想要有效的使用它们，就需要更多额外的力量。但他们依旧是最强大的武器",
                    "双持型武器的目的是双手使用，所以它的攻击速度比任何其他武器都要快，但是它们仍然可以使用盾牌，不过装备盾牌后，它的攻击速度加成就会被抵消",
                    "投掷武器可以装备在你的投掷槽。它们不能被升级，但是可以让你快速的进行远程攻击。它们会在使用时概率损坏(同样适用于箭矢类)。",
                    "远程武器需要用双手使用-这意味着他们并不能装备盾牌，但你仍然可以使用你的副手插槽装备法杖或近战武器在近战范围内战斗。如果没有箭矢，你的攻击就会像没有武器一样。每个远程武器都需要特定类型的箭矢。",
                    "弩是最强大的远程武器，但是每次攻击后都需要消耗半个回合来重新装填弹药。不过要注意的是，你可以在移动时候重新加载",
                    //"Flintlock weapons require bullets to shoot and gunpowder in your inventory to reload. Also, loud noises tend to draw unnecessary attention. However, firearms are equally accurate on any distance and they penetrate target's armor.",
                    "不论任何时候，请务必装备着一件护甲。合适的护甲能够大幅增加你的生存几率，减少大多数来源的伤害。但是护甲并不能抵抗非物理伤害，如燃烧，电击，射线等。",
                    "布甲提供的防护非常有限，不过它可以增强你的一个次要属性——潜行，感知或意志。增强的数值会随着护甲升级而进一步提高，并能够创造出一些强大（但具有一定风险）的玩法。",
                    "盾牌会占用你的副手栏，使用盾牌会让你进入一个防守状态，一定回合内增加你的护甲等级，并概率格挡敌人的攻击。当成功格挡或招架敌人的时攻击，概率使其弹反，且下次攻击必定命中。盾牌也可以用来猛击敌人，造成一些伤害，并且晕眩和击退被盾牌弹反的敌人",
                    //"Wands can be very powerful, but you need to equip them and they have a limited number of charges. Utility wands spend all of their charges on use, and their effect depends on amount of charges used.",
                    "法杖(wands)和魔咒(charms)有着非常强大的效果，并且效果和角色的魔能和意志有关，你需要装备上它们才可以正常使用，而且他们的充能数量有限。法杖的远程输出较为稳定。而魔咒在使用时会消耗所有充能，最终的效果则取决于所消耗的充能数。",
            },
            {
                    "大多数的装备都可以升级，升级后的装备会比原先的更强大，武器提高伤害，防具提高防护，还会降低力量需求，使得法杖和魔咒拥有更多的充能上限和充能速度。不过要注意的是，每个物品最多只能提升至5级。",
                    "武器和护甲都可被附魔。附魔会赋予装备一些特殊的效果，比如附带火焰伤害或提高酸蚀抗性，其触发几率取决于你的装备等级。同时，受到诅咒的装备会逆转其上的魔法效果，使它们对你自身造成负面影响",
                    "有些物品可能会携带诅咒，这意味着在你着装备它们后，直到清除诅咒之前都无法卸下(可以通过特定卷轴来清除诅咒)。诅咒物品与非诅咒物品有着相同的伤害和防护效果，在每一章节中越是强大的物品越是容易受到诅咒",
                    "戒指是一种稀有的饰品，当你装备它的时候会提升你的某项属性。它们本身并不强大，但类似的戒指效果相互叠加则会变得很强大。被诅咒的戒指反而降低你的某项能力",
                    /*    "Most equipment has condition level. It slowly decreases as the item is used, but can be restored with the corresponding repair tools or scrolls of Upgrade. Every condition level affects item performance just as much as upgrade level does, but it doesn't affects the strength requirement of the weapon or the number of charges of the wand.",*/
                    "不管是谁，都需要足够的食物才能在地牢生存。每层至少会生成一个口粮；你也可以在商店里购买食物，部分怪物有时也会掉落食物。当饱食度超过75%时，自然恢复速率会增加；当饱食度低于25%时，自然恢复速率减半。当的饱食度达到0%时，就会因饥饿而受到周期性伤害。",
                    "最易于使用且数量充足的治疗方式就是你的水袋。喝掉水袋中的水时，你会恢复部分生命值（根据以损失生命值来恢复），并且可以在地牢中偶尔出现的水井里补充水袋。每个章节都至少拥有两口水井。如果发现少了，则可能是在隐藏房间。",
                    "在地牢的无尽黑暗之中，自然的光源几乎无法见到，这极大地限制了你的视野。你可以点亮油灯来驱散视野上的黑暗。但这基本等同于放弃任何潜行行为，建议慎重使用。",
                    "如果你有一些多余的火药，那么可以利用它们制成简易炸药。这些炸药可被拆除并回收包裹其中的一部分火药，你也可以将它们捆绑在一起，组合成更加强力的炸药包。（PS.无名地牢现已移除火药）",
                    "在合适的情况下，卷轴可以发挥出强大的功用。但如果使用不当，有些卷轴甚至会将你引入死亡之中。你没有办法通过观察确认卷轴的种类，除非你试着去读或者在商店里见到一个相同的卷轴。",
                    "在地牢中你会遇到各种各样的药剂。药水根据效果不同有益有害。有益药剂能够使你获得增益，而有害药剂通常更适合被扔向敌人。",
                    "有时在高草中会出现一些药草。你可以选择直接吃掉它们，食用这些药草会获得一定的减益或增益效果，可以在炼金釜中炼制药剂。药剂的类型取决于你使用的药草(药草被扔进锅里的顺序不影响结果)。",
                    "如果发现背包格太少，可以在商店里购买包裹。不同的药草、药剂、卷轴或法杖魔咒分别可以被收纳在一个专属的包裹中。此外，它还能保护这些物品免受环境的影响（比如火、冰等)。",
            },
            {
                    "探索地牢时，你会遇见诸多敌人。击败敌人是经验的主要来源，可用于提高你的等级，不过只有在敌人拥有足够威胁时你才能从战斗中得到经验。",
                    "地牢里最不缺的就是怪物，即使你杀死了眼前的怪物，但依旧会有更多的怪物会出现并追击你。有些怪物会在死亡时会掉落物品，但最好不要对掉落物抱有太高期待。每当地牢中出现一只新生物时，本楼层出现生物的间隔时长就会提高一点。",
                    "地牢中的每只怪物都有一些特别的能力，但通常你可以将它们分类对待。最常见的敌人，比如老鼠和苍蝇，通常具有较高的敏捷和潜行能力，不过它们的攻击能力很弱，并且没办法承受沉重的打击。",
                    "一些相对常见的敌人，如窃贼，骷髅或豺狼暴徒通常没有明显的缺陷和优势。有些怪物甚至能从中短距离攻击你，不过这些攻击通常效果不佳，不过在一定条件下它们也会立即切换回近战武器攻击你。",
                    "部分敌人有着更加可靠的远程攻击手段，并且它们不论何时都会使用这些手段。更糟糕的是这种敌人通常都造成无视护甲的魔法伤害。但是，有些敌人在攻击前也需要花费一定的回合去充能。",
                    "有些怪物则远比其他敌人具有危险性，它们强壮，结实，并且出手精准。它们唯一的弱点是较低的敏捷属性。同时它们也更容易被伏击，行动时产生的声响也更容易被你听见。",
                    "通常来说，多数敌人都只归属于自己的章节，不会出现在其他章节之中，但有些敌人会出现在地牢的所有角落之中。它们的能力会一直匹配当前层数的标准。不过此类型中绝大多数的敌人也都有某种弱点，使得应对它们时会相对简单一些。",
                    "在地牢之中，boss才是最大的威胁。它们非常强大，并且拥有独特的能力。最棘手的一点是你无法回避和boss的战斗，必须击败它们才能继续深入地牢。击败它们需要你做好充足准备并全神贯注，不过在对抗特定boss时也存在着相应技巧，可以略微降低战斗的难度。",
                    "不过地牢之中的生物并非全部都想置你于死地。地牢中有一些友好住民，甚至有些会请求你完成一个小任务。显然按照它们的要求去做你就会得到一定的奖励，但你也完全可以忽略它们，这并不会对你后续的游戏产生任何其他影响。",
                    "有些NPC嘛...对你没有任何要求，除了钱。每隔五层都会出现一个小商店，你可以在那里出售自己获取的多余物品并购置一些补给。商店出售的商品种类和质量取决于当前所处章节，但有些商品必定出现在店里。",
                    "最后请记住，地牢之中的有些敌人是非常神秘的，并非自然的造物，因此可以免疫一些针对肉体和灵魂的负面效果。不过这也使一些不对寻常生物起作用的效果使用在它们身上时会产生未知的影响。",
                    "那么，这就是全部了。如果你从头到尾阅读完了这篇教程，那这就意味着你已经具备了游玩这款游戏所需的全部知识。一些更加细节的内容会在游戏加载界面出现(还请多多注意)，你也可以通过阅读像素地牢wiki上NNYPD的部分文章来了解更多游戏的机制。祝你好运，注意小心那些隐藏的拟型怪！",

                    /*"Well, that's it for now. If you read this tutorial from the beginning to the end, then you now know everything what you need to start playing this game. Some of details are gonna be explained in loading tips (pay attention to them) and you can learn more about inner workings of the game by reading the YAPD article on the Pixel Dungeon wikia. Good luck, and watch out for mimics!"*/
            },
    };

	//private BitmapText txtTitle;
	private RenderedText txtTitle;
	private ScrollPane list;

    private enum Tabs {

        INTERFACE,
        MECHANICS,
        CONSUMABLES,
        EQUIPMENT,
        DENIZENS,

    }

//	private ArrayList<Component> items = new ArrayList<>();

	private static Tabs currentTab;

	public WndTutorial() {
		
		super();

        icons = TextureCache.get( Assets.HELP );
        film = new TextureFilm( icons, 24, 24 );
		
		if (NoNameYetPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		//txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle = PixelScene.renderText( TXT_TITLE, 8 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		//txtTitle.measure();
		PixelScene.align(txtTitle);
		add( txtTitle );
		
		list = new ScrollPane( new Component() );
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );

		Tab[] tabs = {
            new LabeledTab( TXT_LABELS[0] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.INTERFACE;
                        updateList( TXT_TITLES[0] );
                    }
                }
            },
            new LabeledTab( TXT_LABELS[1] ) {
                @Override
				protected void select( boolean value ) {
					super.select( value );

                    if( value ) {
                        currentTab = Tabs.MECHANICS;
                        updateList( TXT_TITLES[1] );
                    }
				}
            },
            new LabeledTab( TXT_LABELS[2] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.EQUIPMENT;
                        updateList( TXT_TITLES[2] );
                    }
                }
            },
            new LabeledTab( TXT_LABELS[3] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.CONSUMABLES;
                        updateList( TXT_TITLES[3] );
                    }
                }
            },
            new LabeledTab( TXT_LABELS[4] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.DENIZENS;
                        updateList( TXT_TITLES[4] );
                    }
                }
            },
		};

        int tabWidth = ( width + 12 ) / tabs.length ;

		for (Tab tab : tabs) {
			tab.setSize( tabWidth, tabHeight() );
			add( tab );
		}
		
		select( 0 );
	}
	
	private void updateList( String title ) {

		txtTitle.text( title );
		//txtTitle.measure();
		PixelScene.align(txtTitle);
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		
//		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		int index = 0;
		float pos = 0;

        switch( currentTab ) {

            case INTERFACE:
                for (String text : TXT_POINTS[0]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case MECHANICS:

                index += 12;

                for (String text : TXT_POINTS[1]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case EQUIPMENT:

                index += 24;

                for (String text : TXT_POINTS[2]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case CONSUMABLES:

                index += 36;

                for (String text : TXT_POINTS[3]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case DENIZENS:

                index += 48;

                for (String text : TXT_POINTS[4]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

        }
		
		content.setSize( width, pos );
	}
	
	private static class TutorialItem extends Component {

        private final int GAP = 4;
        private Image icon;
		//private BitmapTextMultiline label;
		private RenderedTextMultiline label;
		
		public TutorialItem( String text, int index, int width ) {
			super();

            icon.frame( film.get( index ) );

            label.text( text );
            //label.maxWidth = width - (int)icon.width() - GAP;
            //label.measure();
			label.maxWidth(width - (int)icon.width() - GAP);
            PixelScene.align(label);

            height = Math.max( icon.height(), label.height() ) + GAP;
		}
		
		@Override
		protected void createChildren() {

            icon = new Image( icons );
            add( icon );
			
			//label = PixelScene.createMultiline( 6 );
			label = PixelScene.renderMultiline( 5 );
			add( label );
		}
		
		@Override
		protected void layout() {
			icon.y = PixelScene.align( y );
			
			//label.x = icon.x + icon.width;
			//label.y = PixelScene.align( y );
			float x1 = icon.x + icon.width;
			float y1 = PixelScene.align( y );
            label.setPos(x1,y1);
		}
	}
}
