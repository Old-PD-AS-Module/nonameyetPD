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
package com.ravenwolf.nnypd.items.herbs;


import com.ravenwolf.nnypd.actors.buffs.Buff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Charmed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Debuff;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Decay;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Shocked;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Tormented;
import com.ravenwolf.nnypd.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypd.actors.buffs.special.Satiety;
import com.ravenwolf.nnypd.actors.hero.Hero;
import com.ravenwolf.nnypd.items.potions.PotionOfOvergrowth;
import com.ravenwolf.nnypd.visuals.sprites.ItemSpriteSheet;


public class EarthrootHerb extends Herb {
    {
        name = "地缚根";
        image = ItemSpriteSheet.HERB_EARTHROOT;

        alchemyClass = PotionOfOvergrowth.class;
        message = "这种药草尝起来很清淡，但味道还不错";

        energy = Satiety.MAXIMUM * 0.25f;
    }

    @Override
    public void onConsume( Hero hero ) {


        Debuff.remove(hero, Dazed.class);
        Debuff.remove(hero, Tormented.class);
        Debuff.remove(hero, Charmed.class);
        Debuff.remove(hero, Decay.class);

        Buff.detach(hero, Poisoned.class);
        Buff.detach(hero, Bleeding.class);
        Buff.detach(hero, Crippled.class);
        Buff.detach(hero, Withered.class);
        Buff.detach(hero, Corrosion.class);
        Buff.detach(hero, Burning.class);
        Buff.detach(hero, Chilled.class);
        Buff.detach(hero, Blinded.class);
        Buff.detach(hero, Shocked.class);

        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "某些地方会把这些树根当作为食物，但它们实际上存在着某种强大的魔力。食用它将立即清除任何负面状态！并增加一定的的饱腹度 ";
                //+"This is a therapeutic herb, can be used to brew healing and mystical potions if combined with other therapeutic herb";
    }
}

