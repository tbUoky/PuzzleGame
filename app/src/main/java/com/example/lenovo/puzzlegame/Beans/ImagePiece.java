package com.example.lenovo.puzzlegame.Beans;

import android.graphics.Bitmap;

public class ImagePiece {
    public int index ;
    public Bitmap bitmap ;

    public int getIndex() {
        return index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public ImagePiece(){
        index = 0;
        bitmap = null;
    }
    public ImagePiece(Bitmap bitmap, int index){
        this.index=index;
        this.bitmap=bitmap;
    }
}
