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
package com.ravenwolf.nnypd.actors.hero;

import com.ravenwolf.nnypd.Badges;
import com.ravenwolf.nnypd.items.armours.body.HuntressArmor;
import com.ravenwolf.nnypd.items.armours.body.MageArmor;
import com.ravenwolf.nnypd.items.armours.body.RogueArmor;
import com.ravenwolf.nnypd.items.armours.body.StuddedArmor;
import com.ravenwolf.nnypd.items.armours.shields.ArcaneShield;
import com.ravenwolf.nnypd.items.armours.shields.RoundShield;
import com.ravenwolf.nnypd.items.bags.Baldric;
import com.ravenwolf.nnypd.items.bags.Keyring;
import com.ravenwolf.nnypd.items.food.RationMedium;
import com.ravenwolf.nnypd.items.misc.OilLantern;
import com.ravenwolf.nnypd.items.misc.TomeOfMastery;
import com.ravenwolf.nnypd.items.misc.TomeOfMasterySkill;
import com.ravenwolf.nnypd.items.misc.TomeOfMasterySkill1;
import com.ravenwolf.nnypd.items.misc.Waterskin;
import com.ravenwolf.nnypd.items.potions.PotionOfMending;
import com.ravenwolf.nnypd.items.potions.PotionOfMindVision;
import com.ravenwolf.nnypd.items.rings.RingOfShadows;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfEnchantment;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfIdentify;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfRecharging;
import com.ravenwolf.nnypd.items.scrolls.ScrollOfUpgrade;
import com.ravenwolf.nnypd.items.wands.CharmOfShadows;
import com.ravenwolf.nnypd.items.wands.Wand;
import com.ravenwolf.nnypd.items.wands.WandOfMagicMissile;
import com.ravenwolf.nnypd.items.wands.WandOfSmiting;
import com.ravenwolf.nnypd.items.weapons.melee.Dagger;
import com.ravenwolf.nnypd.items.weapons.melee.Katar;
import com.ravenwolf.nnypd.items.weapons.melee.Quarterstaff;
import com.ravenwolf.nnypd.items.weapons.melee.Shortsword;
import com.ravenwolf.nnypd.items.weapons.ranged.ShortBow;
import com.ravenwolf.nnypd.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypd.items.weapons.throwing.Knives;
import com.ravenwolf.nnypd.visuals.Assets;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;


public enum HeroClass {

//	WARRIOR( "warrior" ), BRIGAND( "rogue" ), SCHOLAR( "mage" ), ACOLYTE( "huntress" );
    WARRIOR( "战士" ), BRIGAND( "盗贼" ), SCHOLAR( "法师"), ACOLYTE( "女猎" );

    private String title;
	
	HeroClass(String title) {
		this.title = title;
	}

/*    public static final String[] WAR_ABOUT = {
//            "Your ancestors were many a man. Mad warlords and ruthless mercenaries, some. Noble knights and pious crusaders, others. Cowards? None of them.",
//            "Now, your family is broken and ruined, but blood of your fathers still flows strong in your veins. There is a way to remind all the world about your clan's former glory!",
//            "A way as simple as retrieving a single lost treasure hidden down there, below this city. How hard can that be, after all?",

            //"Warrior's main advantage is his greater physique - his amount of health is greater than anyone else's and he gets bonus strength with levels.",
            "Warrior's main advantage is his greater physique - his amount of health is greater than anyone else's and he start with higher strength.",
            "However, because of huge size and brash attitude, warrior is unreliable when it comes to stealth and evasion, making him somewhat vulnerable to ranged attacks and spells.",
            "The improved survivability and ability to use heavy equipment much earlier make this class the most fitting for beginners.",
    };

    public static final String[] ROG_ABOUT = {
//            "King's decree was simple - swift death at the hands of executioner, or a chance to redeem yourself by stealing one valuable trinket - a familiar work for someone like you.",
//            "Should've been a simple choice, normally. But... this one is in the Dungeon! There is a lots of rumors about this place - evil magics and stuff. You know how it goes with all these spellcasting bastards and their experiments.",
//            "Argh, to hell with that! You have always been a gambler. Nobody would say that you have let the Reaper claim you the easy way. And, who knows... After all of this is done, maybe you'll keep this trinket for yourself?",

            "Nobody can compete with Rogue in dexterity and overall sneakiness, even if he has to limit himself to light armor if he wants to utilize these talents to their fullest.",
            "And he is not lacking in physical fitness or weapon skills either. He would've been the strongest character if not for his disdain of everything magical, especially wands.",
            "Some knowledge of the game mechanics is required to play this class properly, so it can be considered a class for advanced players.",
    };

    public static final String[] MAG_ABOUT = {
//            "Your weary eyes stare down into the abyss. Fingers nervously clutching quarterstaff, searching for calmness in the familiar touch of its wood. This is it.",
//            "All threads lead here. All these years you've spent on seeking the Amulet weren't in vain. The key to all power imaginable, to all knowledge obtainable is hidden in this darkness.",
//            "You only need to brace yourself and make your first step. Your search has ended here. And here, it has only began.",

            "Mage is the expert wand user. His greater willpower allows him to recharge wands much faster, and he is the most skilled in the arcane arts than other character classes.",
            "However, decades of study have dulled his senses, decreasing his accuracy and perception, forcing him to rely on wands to progress. He is still somewhat strong and agile, though.",
            "Inability to properly use weapons and reliance on wands make him a bit tricky to play as, and therefore this class is only recommended for veteran players.",
    };

    public static final String[] HUN_ABOUT = {
//            "Holy mothers say that fey blood in your veins is a curse for your body but a blessing for your spirit. Frail in build, you always relied on your senses, intuition and faith in the Goddesses.",
//            "Beautiful Artemis, proud Athena and wise Gaia - they have always guided you, sending you insights and prophetic dreams. But, as time went, predictions started to became dark and foreboding.",
//            "They are crystal-clear now - something grows down there, under this City. Something wicked. And it must be nipped in the bud as soon as possible, or else... No gods would save us.",

            "The Huntress is blessed with extremely sensitive intuition. Her awareness of surroundings and precision of her strikes are unparalleled, allowing for more reliable critical hits and ranged attacks.",
            "Alas, the elven heritage made her body frail and sickly. Her starting strength is lower and her vitality grows slightly slower with levels when compared with others.",
            "This vulnerability makes Huntress a fairly challenging class, better fit for an expert player rather than someone who still learns how to play.",
    };

    public static final String[] WAR_DETAILS = {
            "\u007F shortsword",
            "\u007F round shield",
            "\u007F studded armor",
            "",
            "+ health",
            "+ strength",
            "",
            "- dexterity",
            "- stealth",
    };

    public static final String[] ROG_DETAILS = {
            "\u007F dagger",
            "\u007F throwing knives x6",
            "\u007F rogue garb",
            "\u007F ring of shadows",
            "",
            "+ dexterity",
            "+ stealth",
            "",
            "- magic skill",
            "- willpower",
    };

    public static final String[] MAG_DETAILS = {
            "\u007F short staff",
            "\u007F wand of magic missile",
            "\u007F mystic robe",
            "\u007F scroll of recharging",
            "",
            "+ magic skill",
            "+ willpower",
            "",
            "- accuracy",
            "- perception",
    };

    public static final String[] HUN_DETAILS = {
            "\u007F short bow",
            "\u007F arrows x30",
            "\u007F dagger",
            "\u007F elven cloak",
            "\u007F potion of mending",
            "",
            "+ accuracy",
            "+ perception",
            "",
            "- health",
            "- strength",
    };*/
    public static final String[] WAR_ABOUT = {
//            "Your ancestors were many a man. Mad warlords and ruthless mercenaries, some. Noble knights and pious crusaders, others. Cowards? None of them.",
//            "Now, your family is broken and ruined, but blood of your fathers still flows strong in your veins. There is a way to remind all the world about your clan's former glory!",
//            "A way as simple as retrieving a single lost treasure hidden down there, below this city. How hard can that be, after all?",

            //"Warrior's main advantage is his greater physique - his amount of health is greater than anyone else's and he gets bonus strength with levels.",
            "战士的优势在于其优秀的身体素质-相比于其他角色，他初始便拥有较高的力量和血量。不过因为他庞大的体型和急躁的性格，导致他的潜行和闪避方面略逊于其他角色，使其更容易受到远程攻击和魔法攻击的伤害。",
            "强大的生存能力和对重型武器的适应力使得该角色非常适合新人游玩。",
    };

    public static final String[] ROG_ABOUT = {
//            "King's decree was simple - swift death at the hands of executioner, or a chance to redeem yourself by stealing one valuable trinket - a familiar work for someone like you.",
//            "Should've been a simple choice, normally. But... this one is in the Dungeon! There is a lots of rumors about this place - evil magics and stuff. You know how it goes with all these spellcasting bastards and their experiments.",
//            "Argh, to hell with that! You have always been a gambler. Nobody would say that you have let the Reaper claim you the easy way. And, who knows... After all of this is done, maybe you'll keep this trinket for yourself?",

            "在潜行和敏捷行动领域里，没有人能与盗贼比肩，虽然他只有通过使用轻型护甲时才能更有效的利用自己的天赋。但他在武器运用和身体素质方面也毫不逊色。但是由于他非常的厌恶和魔法有关的事务，所以他对魔法，尤其是一些魔法道具的运用很差，不然游戏中最强的职业必定非他莫属。",
            "游玩本角色需要玩家对游戏机制的基本了解，因此他通常更适合经验丰富的玩家游玩。",
    };

    public static final String[] MAG_ABOUT = {
//            "Your weary eyes stare down into the abyss. Fingers nervously clutching quarterstaff, searching for calmness in the familiar touch of its wood. This is it.",
//            "All threads lead here. All these years you've spent on seeking the Amulet weren't in vain. The key to all power imaginable, to all knowledge obtainable is hidden in this darkness.",
//            "You only need to brace yourself and make your first step. Your search has ended here. And here, it has only began.",

            "法师精通于各种法杖和魔咒的运用之道。绝佳的魔能属性允许他以更高的速率赋予法杖充能，并且在法术领域无人能出其右。常年封闭于研究室的生活使它的感官变得迟钝，使它的命中和感知能力低于其他角色，迫使他更加依赖于魔法深入地牢。",
            "不过以同僚的标准来看，他仍然算得上是强壮且敏捷。武器的低适应性和对法杖的极度依赖使得这名角色更需要一定的技巧来游玩，因此该职业仅推荐熟练者游玩。",
    };

    public static final String[] HUN_ABOUT = {
//            "Holy mothers say that fey blood in your veins is a curse for your body but a blessing for your spirit. Frail in build, you always relied on your senses, intuition and faith in the Goddesses.",
//            "Beautiful Artemis, proud Athena and wise Gaia - they have always guided you, sending you insights and prophetic dreams. But, as time went, predictions started to became dark and foreboding.",
//            "They are crystal-clear now - something grows down there, under this City. Something wicked. And it must be nipped in the bud as soon as possible, or else... No gods would save us.",

            "女猎拥有与生俱来得强大直觉。她对周遭环境的感知和精准的战斗技巧是无与伦比的，使她更有效地弹反敌人和进行精准的远程打击。不过，精灵血统使她的身体脆弱且多病。她的起始力量低于其他角色，并且升级时得到的额外生命力成长略低于他人。",
            "脆弱的生命使女猎成为一个更具挑战性的职业，该职业更适合精通本游戏的专业玩家，而非仅了解游戏表层机制的玩家游玩。",
    };

    public static final String[] WAR_DETAILS = {
            "· 短剑",
            "· 圆盾",
            "· 皮甲",
            //   "\u007F armorer's kit",
            "",
            "+生命上限",
            "+力量",
            "",
            "-灵巧",
            "-潜行",
    };

    public static final String[] ROG_DETAILS = {
            "· 匕首",
            "· 飞刀x6",
            "· 盗贼风衣",
            // "\u007F whetstone",
            "· 暗影之戒",
            "",
            "+ 灵巧",
            "+ 潜行",
            "",
            "- 魔能",
            "- 意志",
    };

    public static final String[] MAG_DETAILS = {
            "· 魔杖",
            "· 魔弹法杖",
            "· 法师长袍",
            //   "\u007F arcane battery",
            "· 充能卷轴",
            "",
            "+ 魔能",
            "+ 意志",
            "",
            "- 精准",
            "- 感知",
    };

    public static final String[] HUN_DETAILS = {
            "· 短弓",
            "· 箭矢x30",
            "· 匕首",
            "· 精灵斗篷",
            //    "\u007F crafting kit",
            "· 治疗药剂",
            "",
            "+ 精准",
            "+ 感知",
            "",
            "- 生命上限",
            "- 力量",
    };

    public  ArrayList<HeroSkill>  randomizeSkills( HeroSkill ...skills  ) {
        ArrayList<HeroSkill> availableSkills=new ArrayList<>();
        Collections.addAll(availableSkills, skills);
        return availableSkills;
        /*ArrayList<HeroSkill> availableSkills=new ArrayList<>();

        while (skills.length!=availableSkills.size()) {
            HeroSkill next = Random.element(skills);
            if (!availableSkills.contains(next))
                availableSkills.add(next);
        }
        return availableSkills;*/
    }

	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		initCommon(hero);

		switch (this) {
            case WARRIOR:
                initWarrior( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.RELENTLESS_ASSAULT,HeroSkill.HEROIC_LEAP,HeroSkill.WAR_CRY);
                break;

            case BRIGAND:
                initRogue( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.SHADOW_STRIKE,HeroSkill.SMOKE_BOMB,HeroSkill.CLOAK_OF_SHADOWS);
                break;

            case SCHOLAR:
                initMage( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.OVERLOAD,HeroSkill.MOLTEN_EARTH,HeroSkill.ARCANE_ORB);
                break;

            case ACOLYTE:
                initHuntress( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.SPECTRAL_BLADES,HeroSkill.REPEL,HeroSkill.SPIRIT_WOLF);
                break;
		}
	}
	
	private static void initCommon( Hero hero ) {

		new Keyring().collect();
        new RationMedium().collect();

        new Waterskin().setLimit( 5 ).fill().collect();
        new OilLantern().collect();

        new Baldric().collect();
    }
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
        case BRIGAND:
            return Badges.Badge.MASTERY_BRIGAND;
		case SCHOLAR:
			return Badges.Badge.MASTERY_SCHOLAR;
		case ACOLYTE:
			return Badges.Badge.MASTERY_ACOLYTE;
		}
		return null;
	}


    public Badges.Badge victoryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.VICTORY_WARRIOR;
            case BRIGAND:
                return Badges.Badge.VICTORY_BRIGAND;
            case SCHOLAR:
                return Badges.Badge.VICTORY_SCHOLAR;
            case ACOLYTE:
                return Badges.Badge.VICTORY_ACOLYTE;

        }
        return null;
    }
	
	private static void initWarrior( Hero hero ) {

		hero.STR++;

        hero.HP = (hero.HT += 4);
        hero.defenseSkill -= 4;

		(hero.belongings.weap1 = new Shortsword()).identify();
        (hero.belongings.weap2 = new RoundShield()).identify();
        (hero.belongings.armor = new StuddedArmor()).identify();

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.armor.bones=false;

    }

    private static void initRogue( Hero hero ) {

        hero.defenseSkill += 4;
        hero.magicSkill -= 4;

        (hero.belongings.weap1 = new Dagger()).identify();
        (hero.belongings.ranged = new Knives()).quantity(6).activate(hero);
        (hero.belongings.armor = new RogueArmor()).identify();
        (hero.belongings.ring1 = new RingOfShadows()).identify();

        hero.belongings.ring1.activate( hero );

        hero.belongings.weap1.bones=false;
        hero.belongings.ranged.bones=false;
        hero.belongings.armor.bones=false;
        hero.belongings.ring1.bones=false;

    }
	
	private static void initMage( Hero hero ) {

        hero.magicSkill += 4;
        hero.attackSkill -= 4;

		(hero.belongings.weap1 = new Quarterstaff()).identify();
		(hero.belongings.weap2 = new WandOfMagicMissile()).identify();
        (hero.belongings.armor = new MageArmor()).identify();

        ((Wand)hero.belongings.weap2).initialCharges().charge(hero);

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.armor.bones=false;

        new ScrollOfRecharging().identify().collect();

	}
	
	private static void initHuntress( Hero hero ) {

        hero.STR--;

        hero.HP = (hero.HT -= 4);
        hero.attackSkill += 4;

        (hero.belongings.weap2 = new ShortBow()).identify();
        (hero.belongings.weap1 = new Dagger()).identify();
        (hero.belongings.ranged = new Arrows()).quantity( 30 ).activate(hero);
        (hero.belongings.armor = new HuntressArmor()).identify();

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.ranged.bones=false;
        hero.belongings.armor.bones=false;

        new PotionOfMending().identify().collect();

    }
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case BRIGAND:
                return Assets.ROGUE;
            case SCHOLAR:
                return Assets.MAGE;
            case ACOLYTE:
                return Assets.HUNTRESS;
		}
		
		return null;
	}

    public String[] history() {

        switch (this) {
            case WARRIOR:
                return WAR_ABOUT;
            case BRIGAND:
                return ROG_ABOUT;
            case SCHOLAR:
                return MAG_ABOUT;
            case ACOLYTE:
                return HUN_ABOUT;
        }

        return null;
    }

	public String[] details() {
		
		switch (this) {
            case WARRIOR:
                return WAR_DETAILS;
            case BRIGAND:
                return ROG_DETAILS;
            case SCHOLAR:
                return MAG_DETAILS;
            case ACOLYTE:
                return HUN_DETAILS;
        }
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : WARRIOR;
	}
}
