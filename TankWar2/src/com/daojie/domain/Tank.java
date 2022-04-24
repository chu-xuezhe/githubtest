package com.daojie.domain;

public class Tank extends Element {
    // 成员变量
    public Direction direction = Direction.UP; // 用来记录坦克的方向，需要有初始值，否则会有空指针异常

    public Tank() {
    }

    public Tank(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw() {

    }
}
