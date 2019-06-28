package com.example.lenovo.puzzlegame.Utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

// 图像工具类：实现图像的分割与自适应
public class ImagesUtil {
     /**
      * 处理图片 放大、缩小到合适位置
      *
      * @param newWidth
      * @param newHeight
      * @param bitmap
      * @return
      */
     public Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
         Matrix matrix = new Matrix();
         matrix.postScale(newWidth / bitmap.getWidth(), newHeight / bitmap.getHeight());
         Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
         return newBitmap;
     }

}
