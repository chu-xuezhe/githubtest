package com.daojie.domain;

import com.daojie.inter.Blockable;
import org.daojie.game.DrawUtils;

import java.io.IOException;

public class Water extends Element implements Blockable{
    // 成员变量
    private String srcPath = "TankWar2/res/img/water.gif";

    // 构造方法，赋值坐标
    public Water(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            // 获取图片的宽和高，并赋值
            int[] arr = DrawUtils.getSize(srcPath);
            this.width = arr[0];
            this.heigth = arr[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 成员方法
    public void draw(){
        try {
            DrawUtils.draw(srcPath,x,y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
