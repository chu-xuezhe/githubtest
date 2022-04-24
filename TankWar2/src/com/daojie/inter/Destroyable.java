package com.daojie.inter;

import com.daojie.domain.Blast;

public interface Destroyable {
    // 具有销毁能力的接口。只要实现该接口，具有销毁能力。
    public abstract boolean isDestroy(); // 用于校验销毁事物是否 需要销毁
    public abstract Blast showBlast(); // 用于销毁事物销毁时, 返回一个爆炸物对象
}
