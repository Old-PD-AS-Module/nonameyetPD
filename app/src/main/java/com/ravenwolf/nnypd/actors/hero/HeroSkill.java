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


import com.ravenwolf.nnypd.actors.buffs.special.skills.*;
import com.watabou.utils.Bundle;

public enum HeroSkill {

    NONE( null, null ),

/*    HEROIC_LEAP( "Heroic leap",
            "Leap towards a targeted location, slamming down to damage and stun all neighbouring enemies. " +
                    "Damage dealt is based on your max health.", HeroicLeap.class,1 ),
    WAR_CRY( "War cry",
            "Bellow a mighty war cry, alerting nearby enemies and inducing a state of wild fury that "+
                    "significantly increase your attack damage. The fury increases when receiving damage. ", WarCry.class,  0 ),
    RELENTLESS_ASSAULT( "Relentless assault",
            "A devastating attack with your melee weapon that hit an enemy several times in a row with exceptional accuracy and speed. " +
                    "While this attack cannot miss, the damage dealt will be reduced based on weapon's penalty.", Fury.class, 2 ),*/
    HEROIC_LEAP( "英勇跳跃",
            "跳跃到目标位置，对周围造成猛烈的伤害，并击晕附近的敌人。 " +
                    "伤害取决于你的最大生命值", HeroicLeap.class,1 ),
    WAR_CRY( "战争怒吼",
            "你冲着周围大吼了一声，并获得狂暴状态，显著增加你的攻击伤害。当受到伤害时会增加狂暴状态的效果。 " +
                    "还会吸引附近的敌人", WarCry.class,  0 ),
    RELENTLESS_ASSAULT( "毁灭打击",
            "用你的近战武器对敌人进行毁灭性的打击，以特殊的精度和速度连续对同一目标攻击数次。" +
                    "这个攻击不会被闪避，造成的伤害将受到武器影响", Fury.class, 2 ),

/*    MOLTEN_EARTH( "Arcane fire",
            "Conjure the powers of arcane elements, immobilizing and burning nearby enemies with magical fire."
                    ,MolterEarthSkill.class,9 ),
    OVERLOAD( "Overload",
            "Imbue yourself with arcane energy, recharging and increasing power of your currently equipped wand or charm for a short period."
            , OverloadSkill.class,11 ),
    ARCANE_ORB( "Arcane Orb",
            "Summons an orb of pure energy that will chase enemies releasing waves of arcane energy that damage any surrounding foe. It can trigger enchants and glyphs " +
                    "of its owner weapon and armors when fighting. Its power is based on chapter and hero level." , SummonArcaneOrbSkill.class,10 ),*/
MOLTEN_EARTH( "奥术魔焰",
            "使用大地元素之力，缠绕并燃烧附近的敌人"
            ,MolterEarthSkill.class,9 ),
    OVERLOAD( "过载",
            "奥术之力充满了你的全身，短时间加强你的魔法道具并恢复充能"
            , OverloadSkill.class,11 ),
    ARCANE_ORB( "奥术之球",
            "召唤一个由奥术之力组成的球状体，它会持续的追击敌人，并释放奥术能量，对周围的敌人造成伤害。它在战斗中可以触发召唤者武器和盔甲的附魔和刻印。" +
                    "它的力量基于你的等级和当前所在的区域。" , SummonArcaneOrbSkill.class,10 ),

/*    SMOKE_BOMB( "Smoke bomb",
            "Throws down a smoke bomb making you invisible for some turns and blinking to target position. Nearby enemies will be blinded. " ,SmokeBomb.class,6),
    SHADOW_STRIKE( "Shadow strike",
            "Phases out into the shadows to perform a devastating sneak attack on an enemy. Damage dealt will be reduced based on weapon's penalty." +
                    "" ,ShadowStrike.class,8),
    CLOAK_OF_SHADOWS( "Cloak of shadow",
                    "Summon a dense fog that will follow you providing a constant concealment." +
                            " You are able to see over it, but it will block vision for every other creature." , CloakOfShadowsSkill.class,7),*/
    SMOKE_BOMB( "烟幕爆炸",
            "在原地制造一片迷雾，并致盲附近的敌人，随后闪现到视野内的指定位置并暂时遁入隐身。" ,SmokeBomb.class,6),
    SHADOW_STRIKE( "暗影突袭",
            "瞬间对周围的所有敌人造成毁灭性的伏击。造成的伤害将受到武器影响。" +
                    "" ,ShadowStrike.class,8),
    CLOAK_OF_SHADOWS( "暗影迷雾",
            //"Grant an invisibility buff. When an enemy is killed, invisibility its restored instantly and the duration of the buff is increased. " +
            "召唤出跟随你行动的浓厚迷雾。能够阻挡敌人的视线，但你依然可以看透这些迷雾。" , CloakOfShadowsSkill.class,7),

    /*SPECTRAL_BLADES( "Spectral blades",
            "Hurls a fan of spectral blades that damage and reveal enemies in a cone area. Revealed enemies will have its evasion and armor reduced at half." ,SpectralBladesSkill.class,3),
    REPEL( "Repel",
            "Release a blast of energy, pushing away any neighbouring enemy and gaining extraordinary speed for a short time.",ValkyrieSkill.class,4 ),
    SPIRIT_WOLF( "Spirit wolf",
                         "Summons a Spirit wolf familiar, that will chase and attack enemies. Due to its ethereal nature it will not block projectiles. It stats and health are based on chapter and hero level.", SummonSpiritWolfSkill.class,5 );*/
    SPECTRAL_BLADES( "幻影飞刃",
                             "投掷一片幻影之刃，对扇形范围内敌人造成伤害并使其被标记，被标记的敌人护甲和闪避减少会降低一半" ,SpectralBladesSkill.class,3),
    REPEL( "灵能爆发",
                      "对周围释放一股强大的能量，击退邻近的敌人，并在短时间内激发自身的潜能。",ValkyrieSkill.class,4 ),
    SPIRIT_WOLF( "幽魂之狼",
                           "召唤出一只幽魂之狼，它会以极快的速度追捕你的敌人,飞行物会穿透它由灵魂组成的身体。它的力量基于你的等级和当前所在的区域.", SummonSpiritWolfSkill.class,5 );
    /*
    THORN_SPITTER( "Thorn spitter",
                           "Summons a Thorn spitter, that will spit poisoned spikes at nearby enemies. Its power is based on chapter and hero level, receiving additional health " +
                           "when created on grass-covered areas.", SummonThornspitterSkill.class,5
    VALKYRIE__OLD( "Valkyrie",
                      "Call the blessing of the valkyries, receiving a protective shield and the ability to fly. Upon receiving the bless, neighbouring enemies will be pushed back.",ValkyrieSkill.class,4 ),

*/
    private String title;
    private String desc;
    private int icon;
    private Class<? extends BuffSkill> skillClass;

    HeroSkill(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    HeroSkill(String title, String desc, Class<? extends BuffSkill> skillClass, int icon) {
        this.title = title;
        this.desc = desc;
        this.skillClass= skillClass;
        this.icon= icon;
    }

    public String title() {
        return title;
    }

    public String desc() {
        return desc;
    }

    public int icon() {
        return icon;
    }

    public Class<? extends BuffSkill> skillClass() {
        return skillClass;
    }

    private static final String SKILL	= "skill";

    public void storeInBundle( Bundle bundle ) {
        bundle.put( SKILL, toString() );
    }

    public static HeroSkill restoreInBundle(Bundle bundle ) {
        String value = bundle.getString( SKILL );
        try {
            return valueOf( value );
        } catch (Exception e) {
            return NONE;
        }
    }
}
