package com.daojie.inter;

public interface Moveable {
    // 移动功能的接口，凡是实现该接口的类都具备了移动功能（例如我方坦克、敌方坦克，都具有移动功能）
    boolean checkHit(Blockable blockable);
}
