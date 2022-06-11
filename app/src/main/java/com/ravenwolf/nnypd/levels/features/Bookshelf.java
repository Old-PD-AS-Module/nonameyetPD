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
package com.ravenwolf.nnypd.levels.features;

import com.ravenwolf.nnypd.Dungeon;
import com.ravenwolf.nnypd.items.Generator;
import com.ravenwolf.nnypd.levels.Level;
import com.ravenwolf.nnypd.levels.Terrain;
import com.ravenwolf.nnypd.misc.utils.GLog;
import com.ravenwolf.nnypd.scenes.GameScene;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

	private static final String TXT_FOUND_SCROLL	= "你找到了一张卷轴！";
	private static final String TXT_FOUND_NOTHING	= "这里没什么值得注意的。";
	private static final String TXT_FOUND_READING	= "你找到了%s";

	private static final String[] BOOKS = {

			// LORE

			"一些宗教手稿，其上讲述了一位名为Watabou的神，这位强大却又谜团重重的神创建了这个世界，但后来又弃之离去。",

			"一则传说故事，描绘了Watabou使用了一个奇迹魔方创造了这个世界的韵律法则，但这个神之器物如今已无迹可寻。",

			"一些圣经手稿，上面记述着如今的世界也是无数世界中的一个小小分支，每一个世界都是独一无二的。",

			"一些上古石刻板，板上刻画着一个残忍且恶毒的神蚕食着这个世界的故事，不知为何这个神似乎是被认作一只仓鼠。",

			"一本古旧的书籍，上面描述了一名强大的Yendor巫师与黑暗之神英勇战斗的故事，故事的最后Yendor以生命为代价取得了这场胜利。",

			// DENIZENS

			"一本古老的书籍，上面记载了豺狼人的野蛮习性和一些古老的仪式。显而易见，豺狼们认为自己可以吸收所食猎物的全部精华。",

			"一本记录了各种与巨魔接触事件的书，不过书的另一半被撕扯下来了。这些生物平时幽静隐居，但在受到威胁时会变得异常凶猛。",

			"一本小册子，其上记录着矮人铁匠与工匠们的光辉成就。书面上残留着一些斑点，就像是干涸的血迹。",

			"一本记录着世界深处各类恐怖恶魔生物的魔典，根据书中所述，其中最为强大的个体名为Yog-Dzewa。",

			"一个破旧的典籍，似乎讲述了召唤各种秘法生物的仪式-死灵，魔像，元素等等...但它已经破旧不堪，没有办法找出有用的信息了。",

			// CHARACTERS

			"一本历史书，它讲述了极北之地暴躁却又勇敢正直的人民。这片大陆之上的勇武之士和海贸商才举世闻名。受人尊敬。",

			"一本地理书，讲述了南部沙漠之中生活的一支野蛮且狡诈恶毒的部落。",

			"一本哲学著作，是由东部帝国的巫师们所撰写的,他们以“知识高于一切”的思想闻名于世。",

			"一本描述西部住民们简朴却又充实的生活的书，他们一直以来都遵循着自然之神的意志并因此受到庇护。 ",

			// CREDITS

			"一本讲述了烈焰女巫Inevielle的书，她以高超的语言知识和奇特的动物驯服技巧而闻名。",

			"一本知名画家Logodum的传记，其出色的绘画作品点缀着整个世界。直到如今其作品仍然广为人知。",

			"一本描述着一名疲惫不堪的冒险者的故事片段。靠着的指虎和抛网，他能冲破任何困境。",

			"一本B'gnu-Thun的旧笔记。作为一名优秀的猎人兼匠人，他凭借自己的才华创造了坚固盾牌，华美皮革和不计其数的其他物件。",

			"一则关于狡诈的巨魔军阀R'byj的故事，它凭借自己的战术策略极大地影响了当今世界的格局。",

			"由远东诗人Jivvy编撰的乐谱，他演奏的出色乐曲理应传遍世界。",

			"一本由破碎之Evan写下的典籍，这位大贤者为了自己的世界孜孜不倦地努力。如此事迹启发了无数后人，并被推崇膜拜。",

			// CONTEST WINNERS

			"一部关于传奇冒险家Connor-Johnson的书，凭借着坚持不懈的意志和无与伦比的天赋，战胜了那些常人几乎不可能战胜的困境。",

			"一卷关于一名高阶祭司，惊世元祖Heimdazell的传说故事。聪慧过人的头脑，坚如磐石的身体。强大的传说影响了一代又一代的人。",

			"一则关于Nero的神话，对于这位手持巨斧，性格暴躁的矮人战士来说，任何挑战他都不在话下。",

			"一则关于盗贼Krautwich的传说故事，专一的使用匕首和弓弩类武器，使得他步入了一些无人能及的领域。",

			"一个讲述了残暴的魔神创造危险迷宫的古老传说。看上去很有趣，但是它位于世界的另一端。",

			"一则关于大千之Illion的寓言，讲述了他为了寻求终焉之锤而踏上旅途的故事。",


			// MISC

			"一些由远古哲学家们留下的笔记，其上记述着他们的观念：这个世界仍处在不平衡的状态之中，因其仍未被完成。",

			"一些怪异的预言，其中提到了死者吹奏号角，血红服饰恶魔造访，以及身着盛装的巨蟹等等。",

	};

	public static void examine( int cell ) {

        if (Random.Float() < ( 0.15f + 0.05f * Dungeon.chapter() ) ) {

            Dungeon.level.drop( Generator.random( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
            GLog.i( TXT_FOUND_SCROLL );

        } else if (Random.Float() < 0.05f ) {

            GLog.i( TXT_FOUND_READING, BOOKS[ Random.Int( BOOKS.length ) ] );

        } else {

            GLog.i( TXT_FOUND_NOTHING );

        }

        Level.set( cell, Terrain.SHELF_EMPTY );
        Dungeon.observe();
		
		if (Dungeon.visible[cell]) {
            GameScene.updateMap( cell );
			Sample.INSTANCE.play(Assets.SND_OPEN);
		}
	}
}
