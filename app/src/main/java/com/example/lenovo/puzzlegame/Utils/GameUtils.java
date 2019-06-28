package com.example.lenovo.puzzlegame.Utils;

import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.puzzlegame.Activity.SignupActivity;
import com.example.lenovo.puzzlegame.Beans.ImagePiece;
import com.example.lenovo.puzzlegame.Activity.HomeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 拼图工具类：实现拼图的交换与生成算法
 *
 */
public class GameUtils {

    // 游戏信息单元格Bean
    public static List<ImagePiece> imagePieceList = new ArrayList<ImagePiece>();
    // 空格单元格
//    public static ImagePiece blankItem = new ImagePiece();

    //空白格位置
    public static int blankposition ;
    //生成随机数组



    /**
     * 判断点击的Item是否可移动
     *
     * @param position position
     * @return 能否移动
     */
    public static boolean isMoveable(int position) {
        int type = HomeActivity.type;
        // 获取空格Item
        int blankId = GameUtils.blankposition;
        // 不同行 相差为type
        if (Math.abs(blankId - position) == type) {
            return true;
        }
        // 相同行 相差为1
        return (blankId / type == position / type) &&
                Math.abs(blankId - position) == 1;//返回绝对值
    }

    /**
     * 交换空格与点击Item的位置
     *
     * @param from  交换图
     * @param blank 空白图
     */
    public static void swapBlank(int from, int blank) {

        ImagePiece imagePiece ;
        imagePiece = GameUtils.imagePieceList.get(from);
        GameUtils.imagePieceList.set(from,GameUtils.imagePieceList.get(blank)) ;
        GameUtils.imagePieceList.set(blank,imagePiece);
        GameUtils.blankposition =  from;
    }
    /**
     * 是否拼图成功
     *
     * @return 是否拼图成功
     */
    public static boolean isSuccess() {
        int size = GameUtils.imagePieceList.size();//9
        int flag = 0;
        for (int i = 0 ; i<size -1;i++)//0-7
        {
            if (GameUtils.imagePieceList.get(i).getIndex()==i+1)
            {
                flag++;
            }
        }
        if(GameUtils.imagePieceList.get(size-1).getIndex()==0)
        {
            flag++;
        }
        if (flag==size){
            return true;
        }
        return false;
    }

    // -----------------排序，打乱，校验是否可解，最后得到可解的队列--------------------
// ---------------------------------start--------------------------------
    private static Random rand = new Random();

    public static <T> void swap(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;

        ImagePiece imagePiece = imagePieceList.get(i);
        imagePieceList.set(i,imagePieceList.get(j));
        imagePieceList.set(j,imagePiece);

    }

    public static <T> void shuffle(T[] arr) {
        int length = arr.length;
        for (int i = length; i > 0; i--) {
            int randInd = rand.nextInt(i);
            swap(arr, randInd, i - 1);
        }
    }

    public static void Sort(int level) {
        //原逆序数 1,2,3,4,5,6,7,8,0   其逆序数 为
        //现打乱后，x,x,x,x,x,x,x,x,x  其逆序数为 得到的数+size*size-1

        Integer[] right = new Integer[level * level];
        for (int i = 1; i < level * level; i++) {
            right[i - 1] = i;
        }
        right[level * level-1]=0;
        int reverse = InverseNumber(right)%2;//原逆序数


        int size = level * level-1;
        Integer[] ri = new Integer[size];
        for (int i = 1; i <= size; i++) {
            ri[i - 1] = i;
        }
        System.out.println(Arrays.toString(ri));
        shuffle(ri);
        System.out.println("-------------------乱序------------------");
        System.out.println(Arrays.toString(ri));
        int count = (InverseNumber(ri)+level*level-1)%2;
        System.out.println("----------逆序数： " + count + "-----------------");
        if (count ==reverse) {
            System.out.println("可解为：" + Arrays.toString(ri));
            Log.w("打乱排序","原逆序数为："+reverse+"  ; 打乱后的逆序数为："+count+"随机排序为："+ Arrays.toString(ri));
            return  ;
            // return "可解为：" + Arrays.toString(ri);
        } else {
//            int jh = ri[0];
//            ri[0] = ri[1];
//            ri[1] = jh;
            swap(ri, 0, 1);
            System.out.println("不可解，修复为：" + Arrays.toString(ri));
            Log.w("打乱排序","原逆序数为："+reverse+"  ; 打乱后的逆序数为："+count+"修正排序为："+ Arrays.toString(ri));

            return ;            // return "不可复原，修复为：" + Arrays.toString(ri);
        }
        // 乱序后，需校验是否可以复原
        // 判断复原与否，获取0的下标，得到数据的逆序数
    }

    public static int InverseNumber(Integer[] ri) {
        int Count = 0;
        for (int i = 0; i < ri.length; i++) {
            for (int j = 0; j < i; j++) {
                int a = ri[i];
                int b = ri[j];
                if (a < b) {
                    Count++;
                }
            }
        }
        return Count;
    }






}
