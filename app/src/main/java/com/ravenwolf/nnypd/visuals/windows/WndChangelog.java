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
import com.ravenwolf.nnypd.scenes.PixelScene;
import com.ravenwolf.nnypd.visuals.ui.ScrollPane;
import com.ravenwolf.nnypd.visuals.ui.Window;
import com.watabou.noosa.ui.Component;
import com.watabou.noosa.RenderedText;
import com.ravenwolf.nnypd.visuals.ui.RenderedTextMultiline;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

    private static final String TXT_TITLE	= "无名的像素地牢 中文版 Beta2";

    private static final String TXT_DESCR =

            "初步汉化的无名地牢，_基于0.4.1版本_。\n\n" +
                    "Beta2更新说明：\n-1.修复了_文本切屏错乱问题_，并_添加了错误报告_。\n\n-2.优化了主界面的滚动可能卡顿的问题\n\n" +
                    "若你在_无名地牢Beta2_遇到任何报错，都请复制错误报告到\n\n-_QQ:2735951230_\n-_CutelinXXX@126.com_(以上均可)" +
                    "\n并说明发生错误前详细情况。\n\n==========Beta1更新日志：===========\n" +
                    "目前大部分内容都已汉化，由于时间较紧，可能会存在诸多问题。" +
                    "如有任何意见和建议，_欢迎前来指正_！\n\n请将主要问题描述和截图发送至邮箱：\n_3458026637@qq.com_\n" +
                    "或者QQ:_3458026637_\n" +
                    "无名地牢交流群:_656189352_\n" +
                    "那么，祝你玩得开心！\n" +
                    "\n" +
                    "\n" +

        "你好，冒险家,新版汉化无名发布啦！" +
        "\n" +
        "\n" +
        "_版本要闻_\n\n" +

        "_转职重做！_\n " +
        "每个角色都有两个帅气转职.\n " +
        "转职还没做完, 下次更新会有更多内容, 例如每个转职都有不同技能.\n\n " +
        "_技能修改_ \n" +
        "技能可以在游戏前期获得，其中一些技能已被修改。\n\n "+
        "_新的地牢内容！_ \n" +
        "一些_新的NPC_将会随机出现在地牢中，你可以用钱来换取一些物品或BUFF。 \n" +
        "新的_祭坛_将会随机出现在地牢中，你可以把祭坛的力量转移到装备上。\n\n "+
        "多了一些新的房间，包括含有隐藏或被(陷阱)拦住了的奖励的几个房间。 \n" +
        "普通楼层可能会生成更大的房间或地形，这会让远程攻击更加有效，同时也让远程敌人更难处理。\n\n"+
        "_战斗修改！_ \n" +
        "格挡，反击和暴击(重击，斩击，穿甲)机制已修改。\n "+
        "为了简化用户的选择和更多的策略，大量装备都已修改。\n "+

        "------" +
        "\n" +
        "\n" +

        "_0.4.1更新列表_\n" +
        "\n" +
        "\n" +
        "_修复_\n"+
        "\n" +
        "- 修复了猎杀者在地图边缘使用暗影打击会崩溃的问题\n" +
        "- 修复了武僧在攻击中死亡时导致的崩溃（护甲刻印，反击）\n" +
        "- 修复了在快捷栏上使用箭矢和钝化箭矢的问题\n" +
        "- 圣裁之杖造成的失明比预期时间长\n" +
        "- 修复了环刃有时不会命中多个敌人的错误\n" +
        "- 修复了战斗法师的解离法杖不能触发特效\n" +
        "- 修复了战斗法师的魔咒不能触发特效（不是故意的）\n" +
        "- 修复了教程首页的图片\n" +
        "- 盟友和召唤物不会试图攻击无敌的敌人了\n" +
        "- 狂战士的狂暴在低血量时生效了两次\n" +
        "- 修复了几个小bug\n" +
        "\n" +
        "\n" +
        "_平衡性调整_\n"+
        "- 魔杖的法术加成从15%降低为10%。每级提供5%的法术加成（现在铁头棍和魔杖对于法杖/魔咒的法术加成相同）\n" +
        "- 战斗法师用法杖的连击加成从7.5%增加到10%\n" +
        "- 战斗法师的标记效果从6回合增加到8回合\n" +
        "- 咒术法师使用法杖时获得的护盾从5回合降低为4回合\n" +
        "- 哨位召唤物的持续时间略微增加\n" +
        "- 萨满和巫医的闪电攻击的最大伤害降低\n" +
        "- 猎杀者的暗影打击对附近敌人的伤害降低33%\n" +
        "- 提高某些四阶武器的精准和潜行惩罚\n" +
        "\n" +
        "\n" +
        "_矮人国王调整_\n"+
        "- 矮人国王现在闪现到祭坛开始仪式，而不是走路\n" +
        "- 前往祭坛时，他不会受到眩晕和击退效果的影响\n" +
        "- 矮人国王再开始仪式时不再牺牲他的所有仆从\n" +
        "- 矮人骷髅的生成率根据当前的骷髅数量而降低\n" +
        "\n" +
        "\n" +

        "_0.4 更新列表_\n" +
        "\n" +
        "\n" +

        "_技能_\n"+
        "\n" +
        "- 第一个技能是击败goo后获得的，第二个技能是再击败洞穴层boss后获得的\n"+
        "- 女猎技能的_荆棘之主_替换为_狼灵守护_：狼灵的移动速度和攻击速度很快，并且不会阻碍你的投掷物（”荆棘之主“可能在以后回归）\n" +
        "- 女猎技能的_幻影飞刃_小重做：女猎现在发射多达六把穿透墙体的刀刃，在锥形区域内伤害并标记敌人。被标记的敌人会被标记，其护甲和闪避减少一半。\n" +
        "- 女猎技能的”女神祝福“被替换为”灵能爆发“=保留原来的击退效果，现在增加攻击和移动速度，而不是护盾和浮空\n" +
        "- _奥术之球_现在攻击时可以触发法师近战武器的附魔效果，以及防御时触发法师护甲的刻印效果\n" +
        "- _过载_现在只能为当前装备的法杖或魔咒充能，而不是为全部法杖充能\n" +
        "- _暗影突袭_现在只能命中一个敌人，但冷却时间很短\n" +
        "- _毁灭打击_的攻击次数减少一次\n" +
        "-_熔岩地狱_重做为_奥术魔焰_，造成_灵魂灼伤_的负面状态而不是燃烧\n" +
        "\n" +
        "\n" +

        "_转职_\n"+
        "\n" +
        " 英雄们可以通过精通之书在两个转职之间选择一个\n"+
        " 每个转职都会提供一个被动加成，并提升英雄的第一个技能\n" +
        "\n" +
        "\n" +

        "_环境_\n"+
        "\n" +
        "- 添加几个新型的特殊大房间 \n" +
        "- 普通房间可以更大\n" +
        "- 在较深的楼层中，视野距离不会降低的太严重\n" +
        "- 可搜索的书架选择随机出现在以前的空书架房间中\n" +
        "- 重做了电击和致残这能可能会立刻导致人员暴毙的陷阱，造成的直接伤害减少，这样可以给玩家留出应对的空间\n" +
        "- 每个楼层会有更多陷阱，但会裸露更多陷阱，因此可以用来对付敌人\n" +
        "\n" +

        "_能量祭坛_\n"+
        "\n" +
        "- 新增了一个地牢建筑，能量祭坛\n" +
        "- 祭坛可以出现在特殊房间，让你的物品获得某些效果\n" +
        "- 祭坛的用途有限，效果取决于祭坛类型\n" +
        "- 知识祭坛能让你鉴定物品\n" +
        "- 嬗变祭坛会使你的物品变成黄金（获得的黄金比原售价多50%）\n" +
        "- 附魔祭坛的原理类似于附魔卷轴（有助于解开诅咒，添加或改变法杖戒指类型的附魔）\n" +
        "- 挑战祭坛是一个罕见的祭坛，可以让你升级或附魔任何一件护甲或近战武器。但是你必须打败你的幽灵装备才能获得它！\n" +
        "\n" +
        "\n" +

        "_NPCs_\n"+
        "\n" +
        "- 新的NPC将随机出现在地牢中！" +
        "\n" +
        "-他们会提供一些物品或增益来交换黄金" +
        "\n" +
        "\n" +

        "_特殊攻击调整_\n"+
        "\n" +
        "- 普通武器打出特殊攻击的概率更高（从8%到15%）\n" +
        "- 提高感知对特殊攻击概率的加成\n" +
        "- 特殊攻击现在会对任何类型的敌人生效（刀刃武器可以对持盾敌人打出暴击，对空灵的敌人打出重击）.\n\n" +
        "\n" +
        "\n" +


        "_持盾/反击的机制已经重做_\n"+
        "\n" +
        "- 现在盾牌的持盾状态可以持续6个回合。每次成功的格挡会减少一个回合的持盾状态持续时间\n" +
        "- 格挡概率取决于盾牌防御力+英雄力量vs伤害+敌人精准\n" +
        "- 盾牌对远程攻击有50%的防御力加成\n" +
        "- 力量对武器的影响小于盾牌对武器的影响，以确定格挡概率，且对远程攻击有50%的格挡惩罚\n" +
        "- 在成功格挡时，有概率使敌人被标记。角色有概率自动反击。\n" +
        "- 反击的概率取决于使用的盾牌/武器和角色的感知。\n" +
        "- 魔法攻击可以用盾牌格挡一部分\n" +
        "\n" +
        "\n" +

        "_可以装备物品_\n"+
        "\n" +
        "- 近战武器只能装备在主武器槽中\n" +
        "- 远程武器（弓箭和弩箭）现在装备在副手武器槽中\n" +
        "- 对于装备弓箭或盾牌的武器没有更多的限制/处罚，任何组合都是有效的\n" +
        "- 超出力量需求不再获得收益\n" +
        "\n" +
        "\n" +

        "_重做后的近战武器可分为3类_\n"+
        "\n" +
        "- 常规近战武器：平均伤害。对精准和潜行的中等惩罚\n" +
        "- 轻武器：低伤害和低惩罚。对精准和潜行的惩罚较低\n" +
        "- 重武器：高伤害和高惩罚。对精准和潜行的惩罚较高\n" +
        "\n" +
        "\n" +

        "_武器和盾牌_\n"+
        "\n" +
        "- 大多数武器数据和需求都进行了调整\n" +
        "- _环刃_现在可以对一条线上的敌人造成伤害并回到你的手中\n" +
        "- 增加长剑、巨剑、战斧和战锤的防御力\n" +
        "- 硬头锤不再是暴击时晕眩，但增加了暴击几率\n" +
        "- 回旋镖不再是通常武器，它们现在被视为轻型投掷武器并造成_重击_伤害\n" +
        "- 献祭之刃不再是双手武器。它现在是轻型单手武器，提高额外的偷袭伤害并增加暴击几率.\n" +
        "- _新月之刃被拳刃取代_ 一种新型后期近战武器，造成高额反击伤害并增加暴击几率\n" +
        "- 新增武器：_克赫帕什镰形刀_, 一种新型的游戏后期近战武器，造成高额反击伤害并增加暴击几率\n" +
        "- 新增武器：_太刀_, 一种新型近战武器，暴击几率很高，并造成额外的偷袭和反击伤害\n" +
        "- 新的盾牌:_奥术盾牌_，更容易格挡远程攻击且完全格挡魔法攻击。\n" +
        "- 新的盾牌:_圆盾_，与塔盾一样更容易阻挡远程攻击\n" +
        "- 风筝盾的持盾状态持续时间更长\n" +
        "\n" +
        "\n" +

        "_法杖 & 魔咒_\n"+
        "\n" +
        "- _魔弹法杖重做_\n" +
        " 它有额外的充能，充能速度更快 (从30回合变为25回合的基础充能速度)\n" +
        " 不再享有法术强度的加成\n" +
        " 它现在共享你装备的近战武器的附魔，触发附魔的几率取决于法杖等级+2\n" +
        "- 立场法杖的效果被YAPD的立场法杖替代：造成范围伤害，对魔法单位造成更多伤害，并有概率使敌人失明\n" +
        "- 对棘藤和哨位的召唤物属性进行了调整。现在召唤物的生命值取决于消耗的充能数+基于等级的其他属性（精准，伤害，护甲）和角色法术强度\n " +

        "\n" +
        "\n" +


        "_敌人_\n"+
        "\n" +
        "- 一些敌人将直接游荡而不是睡觉，数量根据章节增加\n" +
        "- 敌人不再能使英雄被标记\n" +
        "- 小咕咕成为下水道的新敌人，收到物理伤害时可以分裂并且在水中再生\n" +
        "- 骷髅的精准、闪避和护甲属性减低。\n"+
        "- 重新加入高级武僧作为精英怪，他们有更强的力量可以格挡你的攻击\n"+
        "- 现在特殊房间中的某些武器可以被幽灵附身（类似于幽灵铠甲）\n"+
        "- 亡灵巫师boss现在将受到的一半伤害转移给憎恶\n"+
        "- 如果亡灵巫师被杀，憎恶会直接死亡\n"+
        "- DM300踩到陷阱是不再是回血，而是获得数回合的护盾\n"+
        "\n" +
        "\n" +

        "_杂项_\n"+
        "\n" +
        "- 调整物品价格和金币掉落" +
        "\n" +
        "- 第四和第五章节有一个额外的升级卷轴" +
        "\n" +
        "- 每个章节不再能保证获得戒指/法杖" +
        "\n" +
        "- 太阳草、火焰花和漩涡花现在是常见的草药，这意味着它们更容易生成，特别是在第一章节中" +
        "\n" +
        "- 阳光卷轴将在治疗友军和眩晕敌人的基础上伤害魔法敌人." +
        "\n" +
        "- 调整了怒气增益公式，现在应该可以持续更长时间了" +
        "\n" +
        "- 黑色浓雾不再导致失明" +
        "\n" +
        "- 简单难度改为可用" +
        "\n" +
        "\n" +
        "\n" +

        "_修复_\n"+
        "\n" +
        "- 修复了使用召唤类技能的一些罕见错误和崩溃\n" +
        "- 修复游戏中持续时间不能正确更新\n" +
        "- 修复了无法从保存的游戏中获得正确的增益状态\n" +
        "- 修复了触发破坏附魔时的一些罕见错误\n" +
        "- 修复了憎恶钩向柱子导致的崩溃（现在你可以利用这一点了）\n" +
        "- 游戏中的提示已更新\n" +
        "- 几个小型的修复\n" +


        "\n" +
        "\n" +
        "------" +
        "\n" +
        "\n";


	//private BitmapText txtTitle;
	private RenderedText txtTitle;
	private ScrollPane list;

	public WndChangelog() {
		
		super();
		
		if (NoNameYetPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
            resize( WIDTH_P, HEIGHT_P );
		}
		
		//txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle = PixelScene.renderText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		//txtTitle.measure();
		PixelScene.align(txtTitle);
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
        add( txtTitle );

        list = new ScrollPane( new ChangelogItem( TXT_DESCR, width, txtTitle.height() ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );

	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        //private BitmapTextMultiline normal;
        //private BitmapTextMultiline highlighted;
		private RenderedTextMultiline normal;
		private RenderedTextMultiline highlighted;

        public ChangelogItem( String text, int width, float offset ) {
            super();

//            label.text( text );
//            label.maxWidth = width;
//            label.measure();

            //Highlighter hl = new Highlighter( text );

//            normal = PixelScene.createMultiline( hl.text, 6 );
            normal.text( text );
            //normal.maxWidth = width;
            //normal.measure();
			normal.maxWidth(width);
            PixelScene.align(normal);
//            normal.x = 0;
//            normal.y = offset;
//            add( normal );

            //if (hl.isHighlighted()) {
                //normal.mask = hl.inverted();

//                highlighted = PixelScene.createMultiline( hl.text, 6 );
                //highlighted.text( hl.text );
                //highlighted.maxWidth = normal.maxWidth;
                //highlighted.measure();
//                highlighted.x = normal.x;
//                highlighted.y = normal.y;
//                add( highlighted );

                //highlighted.mask = hl.mask;
                //highlighted.hardlight( TITLE_COLOR );
            //}

            height = normal.height() + GAP;
        }

        @Override
        protected void createChildren() {
            //normal = PixelScene.createMultiline( 6 );
			normal = PixelScene.renderMultiline( 5 );
            add( normal );
            //highlighted = PixelScene.createMultiline( 6 );
            //add( highlighted );
        }

        @Override
        protected void layout() {
            //normal.y = PixelScene.align( y + GAP );
            //highlighted.y = PixelScene.align( y + GAP );
			y = PixelScene.align( y + GAP );
        }
    }
}
