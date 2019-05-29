package com.tianyi.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Uesr：92863 on 2019/5/29 16:28
 * Email：928636443@qq.com
 * Project: HermesEventbus
 */
public class Eventbus {

    private static Eventbus instance = new Eventbus();

    private ExecutorService executorService;
    //总表
    private Map<Object, List<SubscribleMethod>> cacheMap;

    private Handler handler;
    public static Eventbus getDefault() {
        return instance;
    }

    private Eventbus( ) {
        this.cacheMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
    }

    public void register(Object activity) {

        Class<?> clazz = activity.getClass();
        List<SubscribleMethod> list = cacheMap.get(activity);
//        如果已经注册  就不需要注册
        if (list == null) {
            list = getSubscribleMethods(activity);
            cacheMap.put(activity, list);
        }

    }


    public void unregister(Object activity) {
        Class<?> clazz = activity.getClass();
        List<SubscribleMethod> list = cacheMap.get(activity);
        if (list != null) {
            cacheMap.remove(activity);
        }
    }

    //寻找能够接受事件的方法
    private List<SubscribleMethod> getSubscribleMethods(Object activity) {
        List<SubscribleMethod> list = new ArrayList<>();

        Class clazz = activity.getClass();

        while (clazz != null) {

            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
//                监测这个方法 合不合格
                Class[] paratems = method.getParameterTypes();
                if (paratems.length != 1) {
                    throw new RuntimeException("eventbus只能接收到一个参数");
                }
//                符合要求
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method
                        , threadMode, paratems[0]);
                list.add(subscribleMethod);

            }
            clazz = clazz.getSuperclass();
        }

        return list;

    }

    //通知
    public void post(final Object friend) {

        //遍历 找到  Android岗位
        Set<Object> set=cacheMap.keySet();
        Iterator iterator=set.iterator();
        while (iterator.hasNext()) {
//            拿到公司
            final Object activity=iterator.next();
//            拿到岗位集合
            List<SubscribleMethod> list=cacheMap.get(activity);
            for (final SubscribleMethod subscribleMethod : list) {
//                判断 这个方法是否应该接受事件
                if (subscribleMethod.getEventType().isAssignableFrom(friend.getClass())) {
//1
                    switch (subscribleMethod.getThreadMode()) {
//                        接受方法在子线程执行的情况
                        case Async:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
//                                post方法  执行在主线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });

                            }else {
//                                post方法  执行在子线程

                                invoke(subscribleMethod, activity, friend);
                            }
                            break;
                        //                        接受方法在主线程执行的情况
                        case MainThread:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
//                                需要  1  不需要2
                                invoke(subscribleMethod, activity, friend);
                            }else {
//                                post方法  执行在子线程     接受消息 在主线程
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });
                            }
                            break;
                        case PostThread:

                    }


                }


            }




        }

    }

    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object friend) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(activity, friend);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
