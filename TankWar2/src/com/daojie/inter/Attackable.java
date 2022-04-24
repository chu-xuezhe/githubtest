package com.daojie.inter;

public interface Attackable {
    // 攻击的接口，凡是实现该接口的类就具有攻击的功能
    public boolean checkAttack(Hitable hitable);
}
