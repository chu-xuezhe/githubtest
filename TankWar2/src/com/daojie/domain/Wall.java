package com.daojie.domain;

import com.daojie.inter.Blockable;
import com.daojie.inter.Destroyable;
import com.daojie.inter.Hitable;
import org.daojie.game.DrawUtils;

import java.io.IOException;

public class Wall extends Element implements Blockable,Hitable,Destroyable{
    // 成员变量
    private int blood = 3; // blood血量
    private String srcPath = "TankWar2/res/img/wall.gif";

    // 构造方法，赋值坐标
    public Wall(int x, int y) {
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

    @Override
    public boolean isDestroy() {
        return blood > 0 ? false : true;
    }

    @Override
    public Blast showBlast() {
        blood--; // 只要攻击上了墙，墙就应该掉一颗血
        return new Blast(x,y,width,heigth,blood);
    }
}
