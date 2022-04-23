package com.daojie.domain;

import com.daojie.inter.*;
import org.daojie.game.CollsionUtils;
import org.daojie.game.DrawUtils;

import java.io.IOException;

public class MyTank extends Tank implements Moveable,Blockable,Hitable,Destroyable{
    // 成员变量
    // 1.定义一个变量用来记录最后一颗子弹的时间（全局变量）
    private long lastTime;
    private Direction badDirection; // 用来记录坦克和铁墙已经碰撞的方向
    private int speed = 50; // 定义一个坦克移动的速度变量，初始值为64/2
    private int badSpeed; // 定义碰撞的最小间隙
    private int blood = 6; // blood血量
    private String srcPath = "TankWar2/res/img/tank_u.gif";
    private String srcPath2 = "TankWar2/res/img/tank_d.gif";
    private String srcPath3 = "TankWar2/res/img/tank_l.gif";
    private String srcPath4 = "TankWar2/res/img/tank_r.gif";

    // 构造方法，赋值坐标
    public MyTank(int x, int y) {
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
        // 坦克向哪个方向移动，就绘制哪个方向的图片
        String sp = "";
        switch (direction){
            case UP:
                sp = srcPath;
                break;
            case DOWN:
                sp = srcPath2;
                break;
            case LEFT:
                sp = srcPath3;
                break;
            case RIGHT:
                sp = srcPath4;
                break;
        }
        try {
            DrawUtils.draw(sp,x,y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(Direction direction) {
        // 如果坦克和铁墙已经碰撞上了，如果传进来的方向和已经碰撞的方向一致，那么就不应该让坦克移动
        if(direction == this.badDirection){
            // 如果坦克距离墙壁还有一段距离的时候，坦克不能移动了，就需要移动最小间距
            switch (direction){
                case UP:
                    y -= badSpeed;
                    break;
                case DOWN:
                    y += badSpeed;
                    break;
                case LEFT:
                    x -= badSpeed;
                    break;
                case RIGHT:
                    x += badSpeed;
                    break;
            }
            return;
        }

        // 如果坦克的方向和传递进来的方向不一致的时候，仅仅改变坦克的方向，不要让坦克移动；
        // 如果坦克的方向和传递进来的方向一致的时候，才让坦克移动；
        if(this.direction != direction){
            // 记录坦克的方向
            this.direction = direction;
            return; // 退出方法，仅仅改变坦克的方向，不要让坦克移动
        }
        switch (direction){
            case UP:
                y -= speed; // 横坐标不变，纵坐标减少
                break;
            case DOWN:
                y += speed; // 横坐标不变，纵坐标增加
                break;
            case LEFT:
                x -= speed; // 横坐标减少，纵坐标不变
                break;
            case RIGHT:
                x += speed; // 横坐标增加，纵坐标不变
                break;
        }
        // 坦克边界处理
        // 左边界
        if(x < 0){
            x = 0;
        }
        // 上边界
        if(y < 0){
            y = 0;
        }
        // 右边界
        if(x > Config.WIDTH - 64){
            x = Config.WIDTH - 64;
        }
        // 下边界
        if(y > Config.HEIGHT - 64){
            y = Config.HEIGHT - 64;
        }
    }

    // 坦克发射子弹的方法
    /**
     * 解决不能连续发射子弹的问题
     * 如果这次按键和上次按键的时间间隔小于300ms，就不创建子弹，如果大于或者等于300ms就创建子弹
     */
    public Bullet shoot() {
        // 1.定义一个变量用来记录最后一颗子弹的时间（全局变量）
        // 2.定义一个变量用来记录当前马上要发射子弹的时间（当前时间）
        long nowTime = System.currentTimeMillis();
        // 3.计算2个时间差，判断该时间差是否大于300ms，如果小于300ms，就返回null，如果大于等于300毫秒，就返回一个Bullet对象
        if(nowTime - lastTime <300){
            return null;
        }else{
            lastTime = nowTime;
            return new Bullet(this);
        }
    }

    // 坦克的校验碰撞的方法，来校验是否和铁墙碰撞上
    // 只需要定义一个碰撞的接口，让砖墙和水墙以及铁墙都实现碰撞接口，校验坦克和实现了碰撞接口的事物进行校验是否碰撞
    public boolean checkHit(Blockable blockable) {
        Element e = (Element)blockable;
        // 假设坦克就是第一个矩形，铁墙是第二个矩形
        int x1 = this.x;
        int y1 = this.y;
        int w1 = this.width;
        int h1 = this.heigth;
        // 增加预判的功能，拿坦克的下一个方向位置和墙进行比较判断是否碰撞
        switch (direction){
            case UP:
                y1 -= speed;
                break;
            case DOWN:
                y1 += speed;
                break;
            case LEFT:
                x1 -= speed;
                break;
            case RIGHT:
                x1 += speed;
                break;
        }
        int x2 = e.x;
        int y2 = e.y;
        int w2 = e.width;
        int h2 = e.heigth;

        // 调用是否碰撞的方法，方法里使用等号，所以需要在调用之前添加预判的功能
        boolean flag = CollsionUtils.isCollsionWithRect(x1,y1,w1,h1,x2,y2,w2,h2);
        if (flag){
            // 计算坦克和墙之间的最小间距
            /**
             * 向上最小间距：min = ty-qy-qh
               向下最小间距：min = qy-ty-th
               向左最小间距：min = tx-qx-qw
               向右最小间距：min = qx-tx-tw
             */
            switch (direction){
                case UP:
                    badSpeed = this.y - y2 - h2;
                    break;
                case DOWN:
                    badSpeed = y2 - this.y - this.heigth;
                    break;
                case LEFT:
                    badSpeed = this.x - x2 - w2;
                    break;
                case RIGHT:
                    badSpeed = x2 - this.x - this.width;
                    break;
            }
            // 如果flag为true，说明碰撞上了，把坦克移动的方向赋值给badDirection，记录不能移动的方向
            this.badDirection = direction;
        }
        else{
            // 如果flag为false，说明没有碰撞上，就应该清除以前记录的不能移动的方向，否则坦克无法再朝这个方向移动了
            this.badDirection = null;
        }
        return flag;
    }

    @Override
    public boolean isDestroy() {
        return blood > 0 ? false : true;
    }

    @Override
    public Blast showBlast() {
        blood --;
        return new Blast(x,y,width,heigth,blood);
    }

    public int getBlood() {
        return blood;
    }
}
