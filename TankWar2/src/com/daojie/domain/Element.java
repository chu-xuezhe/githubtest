package com.daojie.domain;

public abstract class Element {
    protected int x; // x坐标
    protected int y; // y坐标
    protected int width; // 图片的宽
    protected int heigth; // 图片的高

    public Element() {
    }

    public Element(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw();

    /**
     * 定义一个方法，获取对象的渲染级别，默认所有元素的渲染级别是0
     */
    public int getLevel(){
        return 0;
    }

}
