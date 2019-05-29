package com.tianyi.eventbus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Uesr：92863 on 2019/5/29 16:29
 * Email：928636443@qq.com
 * Project: HermesEventbus
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.MainThread;
}