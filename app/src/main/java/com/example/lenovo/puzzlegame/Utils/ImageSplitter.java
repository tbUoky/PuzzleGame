package com.example.lenovo.puzzlegame.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.example.lenovo.puzzlegame.Beans.ImagePiece;

import java.util.ArrayList;
import java.util.List;

public class ImageSplitter {
    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {

        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = (j + i * xPiece)+1;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }
        pieces.remove(xPiece * yPiece-1);
        ImagePiece blank=new ImagePiece();
        blank.index = 0;
        Bitmap bitmapblank = Bitmap.createBitmap(pieceWidth, pieceHeight,
                Bitmap.Config.ARGB_8888);
        bitmapblank.eraseColor(Color.parseColor("#FFFFFF"));//填充颜色
        blank.bitmap=bitmapblank;
        pieces.add(blank);
        //设置初始列表中的空白块的数据
        GameUtils.imagePieceList = pieces;
        GameUtils.blankposition = xPiece * yPiece-1;
        return pieces;
    }

}
