package com.daojie.domain;

import com.daojie.inter.Destroyable;
import org.daojie.game.DrawUtils;
import org.daojie.game.SoundUtils;

import java.io.IOException;

public class Blast extends Element implements Destroyable {

    // 定义一个字符串数组,存储所有爆炸物图片的路径
    private String[] arr = {"TankWar2/res/img/blast_1.gif","TankWar2/res/img/blast_2.gif",
            "TankWar2/res/img/blast_3.gif","TankWar2/res/img/blast_4.gif","TankWar2/res/img/blast_5.gif",
            "TankWar2/res/img/blast_6.gif","TankWar2/res/img/blast_7.gif","TankWar2/res/img/blast_8.gif"};

    private  int index;// 定义一个索引表示arr数组的索引

    private int len = arr.length;// 定义一个len变量,用来记录数组的长度 默认是8

    private boolean flag ;//用来记录爆炸物是否显示完了

    public Blast() {
    }

    /*
    爆炸物的坐标
	bx = qx - (bw-qw)/2
	by = qy - (bh-qh)/2
     */
    public Blast(int qx, int qy, int qw, int qh,int blood) {
        // 计算爆炸物的宽和高
        try {
            int[] arr = DrawUtils.getSize("TankWar2/res/img/blast_1.gif");
            this.width = arr[0];
            this.heigth = arr[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 计算爆炸物的x坐标和y坐标
        this.x = qx - (this.width - qw)/2;
        this.y = qy - (this.heigth - qh)/2;

        // 如果血量大于0,显示小爆炸物
        // 如果血量小于0，显示大爆炸物
        if(blood > 0){
            len = 4;
        }
        // 添加爆炸物的声音
        try {
            SoundUtils.play("TankWar2/res/snd/blast.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 如果墙还有血量,就显示一部分爆炸物,如果墙没有了血量,那么就显示大爆炸物
    @Override
    public void draw() { // 一直在调用
        // 防止数组下标越界
        if(index > len -1){
            index = 0;
            flag = true;// 显示一轮完了,应该移除爆炸物
        }
        // 绘制一个爆炸物对象
        try {
            DrawUtils.draw(arr[index],x,y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 切换数组的图片
        index ++;
    }

    /*
    判断是否需要移除爆炸物，返回flag，如果flag为true，表示需要移除爆炸物
     */
    public boolean isDestroy(){
        return flag;
    }

    @Override
    public Blast showBlast() {
        return null;
    }
}
