package com.daojie.domain;

import com.daojie.inter.Attackable;
import com.daojie.inter.Config;
import com.daojie.inter.Destroyable;
import com.daojie.inter.Hitable;
import org.daojie.game.CollsionUtils;
import org.daojie.game.DrawUtils;
import org.daojie.game.SoundUtils;

import java.io.IOException;

public class Bullet extends Element implements Attackable,Destroyable,Hitable{
    // 子弹的血量
    private int blood = 1;

    // 记录子弹所属的坦克
    public Tank tank;
    // 子弹移动的速度
    private int speed = 6;

    private String srcPath = "TankWar2/res/img/bullet_u.gif";
    private String srcPath2 = "TankWar2/res/img/bullet_d.gif";
    private String srcPath3 = "TankWar2/res/img/bullet_l.gif";
    private String srcPath4 = "TankWar2/res/img/bullet_r.gif";

    // 定义子弹的方向
    Direction direction;

    // 构造方法，赋值坐标
    public Bullet(Tank myTank) {
        this.tank = myTank;
        direction = myTank.direction;
        // 注释掉，需要修改子弹的坐标
        // super(x,y);
        // 定义坦克的x坐标，坦克的y坐标，坦克的宽度，坦克的高度
        int tx = myTank.x;
        int ty = myTank.y;
        int tw = myTank.width;
        int th = myTank.heigth;

        // 使用坦克的方向，用来决定是向上发射子弹，还是向下发射子弹，还是向左发射子弹，还是向右发射子弹
        /**
         * 向上发射子弹
             zx = tx + (tw - zw)/2;
             zy = ty - zh;
         向下发射子弹
             zx = tx + (tw - zw)/2;
             zy = ty + zh;
         向左发射子弹
             zx = tx - zw;
             zy = ty + (th - zh)/2;
         向右发射子弹
             zx = tx + tw;
             zy = ty + (th - zh)/2;
         */
        switch (myTank.direction){
            case UP:
                try {
                    // 获取图片的宽和高，并赋值
                    int[] arr = DrawUtils.getSize(srcPath);
                    this.width = arr[0];   // 初始化子弹的宽度
                    this.heigth = arr[1];  // 初始化子弹的高度
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.x = tx + (tw - this.width)/2;
                this.y = ty - this.heigth;
                break;
            case DOWN:
                try {
                    // 获取图片的宽和高，并赋值
                    int[] arr = DrawUtils.getSize(srcPath2);
                    this.width = arr[0];   // 初始化子弹的宽度
                    this.heigth = arr[1];  // 初始化子弹的高度
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.x = tx + (tw - this.width)/2;
                this.y = ty + th;
                break;
            case LEFT:
                try {
                    // 获取图片的宽和高，并赋值
                    int[] arr = DrawUtils.getSize(srcPath3);
                    this.width = arr[0];   // 初始化子弹的宽度
                    this.heigth = arr[1];  // 初始化子弹的高度
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.x = tx - this.width;
                this.y = ty + (th - this.heigth)/2;
                break;
            case RIGHT:
                try {
                    // 获取图片的宽和高，并赋值
                    int[] arr = DrawUtils.getSize(srcPath4);
                    this.width = arr[0];   // 初始化子弹的宽度
                    this.heigth = arr[1];  // 初始化子弹的高度
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.x = tx + tw;
                this.y = ty + (th - this.heigth)/2;
                break;
        }
        // 添加发射子弹的声音
        try {
            SoundUtils.play("TankWar2/res/snd/fire.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        switch (direction){
            case UP:
                // 绘制子弹
                try {
                    DrawUtils.draw(srcPath,x,y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case DOWN:
                // 绘制子弹
                try {
                    DrawUtils.draw(srcPath2,x,y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LEFT:
                // 绘制子弹
                try {
                    DrawUtils.draw(srcPath3,x,y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RIGHT:
                // 绘制子弹
                try {
                    DrawUtils.draw(srcPath4,x,y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // 子弹根据子弹的方向，进行移动
    public void move() {
        switch (this.direction){
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
    }
    /**
     * 判断子弹是否销毁的方法
     */
    public boolean isDestroy(){
        if(x<0 || y<0 || x> Config.WIDTH || y>Config.HEIGHT || blood<=0){
            return true;
        }
        return false;
    }

    @Override
    public Blast showBlast() {
        blood--;
        return new Blast(x,y,width,heigth,blood);
    }

    // 校验子弹是否打中的铁墙
    public boolean checkAttack(Hitable hitable) {
        Element e = (Element)hitable;
        // 假设子弹是第一个矩形，铁墙是第二个矩形
        int x1 = this.x;
        int y1 = this.y;
        int w1 = this.width;
        int h1 = this.heigth;

        int x2 = e.x;
        int y2 = e.y;
        int w2 = e.width;
        int h2 = e.heigth;

        // 调用是否碰撞的方法
        boolean flag = CollsionUtils.isCollsionWithRect(x1,y1,w1,h1,x2,y2,w2,h2);
        return flag;
    }
}
