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
import com.ravenwolf.nnypd.Element;
import com.ravenwolf.nnypd.actors.Char;
import com.ravenwolf.nnypd.items.armours.Armour;
import com.ravenwolf.nnypd.items.armours.body.BodyArmor;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.ravenwolf.nnypd.visuals.sprites.HauntedArmorSprite;
import com.watabou.utils.Bundle;

import java.util.HashMap;

public class HauntedArmor extends MobPrecise {

    public BodyArmor armor;

    public HauntedArmor(BodyArmor armor) {
        this( Dungeon.depth );
        this.armor=armor;
    }

    //empty constructor used to restore from bundle
    public HauntedArmor() {
        this( Dungeon.depth );
    }

    public HauntedArmor(int depth ) {

        super( Dungeon.chapter(), depth*3/4 + 2, false );

        name = "护甲亡灵";
        spriteClass = HauntedArmorSprite.class;
        flying = true;

        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

        resistances.put( Element.Dispel.class, Element.Resist.VULNERABLE );
    }

    @Override
    public CharSprite sprite() {
        CharSprite s = super.sprite();
        ((HauntedArmorSprite)s).updateArmor( armor.getHauntedIndex() );
        return s;
    }

    @Override
    public String getTribe() {
        return TRIBE_UNDEAD;
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }


    private static final String ARMOR	= "armor";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ARMOR, armor );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        armor = (BodyArmor) bundle.get( ARMOR );
    }

    @Override
    public int armourAC() {
        return super.armourAC()+armor.dr();
    }

    @Override
    public int minAC() {
        return super.minAC() + armor.minDr();
    }

    @Override
    public int defenseProc( Char enemy, int damage,  boolean blocked ) {
        //FIXME
        //armor.glyph.proc(armor,enemy,this,damage);
        if (armor != null && armor.glyph != null) {
            if (Armour.Glyph.procced(this,armor.bonus))
                 //always proc with the uncursed enchantment
                armor.glyph.proc_p(enemy, this, damage, false);
        }

        return super.defenseProc(enemy,damage,blocked);
    }

    //get resistances from armor
    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> resistances = super.resistances();

        if (armor != null && armor.glyph != null && armor.glyph.resistance() != null) {
            Class<? extends Element> type = armor.glyph.resistance();
            resistances.put(type, (resistances.containsKey(type) ? resistances.get(type) : 0.0f) + 0.2f + armor.bonus * 0.1f);
        }
        return resistances;
    }
    //get a dodge penalty based on armor
    @Override
    public float dextModifier() {

        float penalty= 1.0f;
        if (armor != null) {
            penalty-= 0.025f * armor.penaltyBase() * super.dextModifier();
        }
        return penalty * super.dextModifier();
    }

    @Override
    public void die( Object cause, Element dmg ) {

        super.die( cause, dmg );

        if (armor != null) {
            Dungeon.level.drop( armor, pos ).sprite.drop();
        }
    }

	@Override
	public String description() {
        return
                "这件古老的_" +armor.name()+"_上依附着一些邪恶的灵魂，它会为了护甲而战斗，击败它之后就护甲就会被解放，同时保留了一些魔力";
        //"it is fully animated and will fight until the spirit is killed. " +
        //"Haunted armors are often inhabited by the ghost of its previous wearer.";
    }

}
