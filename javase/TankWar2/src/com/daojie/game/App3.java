package com.daojie.game;

import com.daojie.inter.Config;

public class App3 {
    public static void main(String[] args) {
        // 创建窗体
        GameWindow gw = new GameWindow(Config.TITLE,Config.WIDTH,Config.HEIGHT,Config.FPS);
        // 启动窗体
        gw.start();
    }
}
