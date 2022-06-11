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

package com.ravenwolf.nnypd.visuals.sprites.layers;

import com.ravenwolf.nnypd.items.EquipableItem;
import com.ravenwolf.nnypd.visuals.sprites.CharSprite;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;


public abstract class EquipmentSprite{

    protected Image image;
    protected TextureFilm film;
    protected TextureFilm equipments;
    int[][] drawData=null;
    CharSprite parentSprite=null;

    public void init(CharSprite parentSprite, String imageFile, int height){
        SmartTexture texture = TextureCache.get( imageFile );
        equipments = new TextureFilm( texture, texture.width, height );
        image =new Image(texture);
        this.parentSprite=parentSprite;
    }

    public void update(EquipableItem item,int id, int animationId, int curFrame) {

        if (id >= 0) {
            film = new TextureFilm(equipments, id, getWidth(), getHeight());
            updateFrame(curFrame,animationId, item);
        }
    }

    public void updateFrame(int curFrame, int animationId, EquipableItem item){

        drawData=getDrawData(animationId, item);
        if (drawData == null || film==null)
            return;

        image.flipHorizontal = parentSprite.flipHorizontal;
        image.frame(film.get(drawData[0][curFrame]));
    }

    public void draw(int curFrame){
        if (drawData!=null) {
            image.center(parentSprite.center());//centers the image at the hero center
            image.x += drawData[1][curFrame] * (image.flipHorizontal ? -1 : 1);
            image.y += drawData[2][curFrame];
            image.alpha(parentSprite.alpha());
            image.ra=parentSprite.ra;
            image.ba=parentSprite.ba;
            image.ga=parentSprite.ga;
            image.draw();
        }
    }

    protected abstract int getWidth();
    protected abstract int getHeight();
    protected abstract int[][] getDrawData(int animationId, EquipableItem item);

}
