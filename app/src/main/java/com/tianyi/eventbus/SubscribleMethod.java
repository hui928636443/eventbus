package com.tianyi.eventbus;

import java.lang.reflect.Method;

/**
 * Uesr：92863 on 2019/5/29 16:30
 * Email：928636443@qq.com
 * Project: HermesEventbus
 */

//职位
public class SubscribleMethod {

    //职位做什么事情
    private Method method;
    //    室内 还是室外
    private ThreadMode threadMode;
    /**
     * Frined 工作岗位类型
     */
    private Class<?>  eventType;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
