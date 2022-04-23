package com.daojie.inter;

import com.daojie.domain.Blast;

public interface Hitable {
    // 阻挡的接口，凡是实现该接口的类就具有阻挡的功能
    public Blast showBlast();
}
