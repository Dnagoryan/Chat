package com.reflection.and.annotation;

import com.reflection.and.annotation.interfaces.AfterSuite;
import com.reflection.and.annotation.interfaces.BeforeSuite;
import com.reflection.and.annotation.interfaces.Test;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;


public class ReflectionTest {

    public static void start(Class someTest) {
        beforeSuiteMethod(someTest);
        testMethod(someTest);
        afterSuiteMethod(someTest);

    }

    private static void afterSuiteMethod(Class someTest) {
        Method AfterSuiteMeth = counterAfterSuite(someTest.getMethods());
        if (AfterSuiteMeth != null) {
            try {
                AfterSuiteMeth.invoke(someTest.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static Method counterAfterSuite(Method[] allMethod) {
        int count = 0;
        Method result = null;
        for (Method method : allMethod) {
            if (method.isAnnotationPresent(AfterSuite.class) && count < 1) {
                result = method;
                count++;
            } else if (method.isAnnotationPresent(AfterSuite.class) && count > 1) {
                throw new RuntimeException();
            }
        }
        return result;
    }

    private static void beforeSuiteMethod(Class someTest) {

        Method beforeSuiteMeth = counterBeforeSuite(someTest.getMethods());
        if (beforeSuiteMeth != null) {
            try {
                beforeSuiteMeth.invoke(someTest.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }


    private static Method counterBeforeSuite(Method[] allMethod) {
        int count = 0;
        Method result = null;
        for (Method method : allMethod) {
            if (method.isAnnotationPresent(BeforeSuite.class) && count < 1) {
                result = method;
                count++;
            } else if (method.isAnnotationPresent(BeforeSuite.class) && count > 1) {
                throw new RuntimeException();
            }
        }
        return result;
    }

    private static void testMethod(Class someTest) {
        Method[] allMethod = Arrays.stream(someTest.getMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .toArray(Method[]::new);
        bubbleSorted(allMethod);
        for (Method m :
                allMethod) {
            try {
                m.invoke(someTest.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bubbleSorted(Method[] allMethod) {
        for (int i = allMethod.length - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                if (allMethod[j].getAnnotation(Test.class).priority() >
                        allMethod[j + 1].getAnnotation(Test.class).priority()) {
                    toSwap(j, j + 1, allMethod);
                }
            }
        }
    }

    private static void toSwap(int first, int second, Method[] allMethod) {
        Method swapMethod = allMethod[first];
        allMethod[first] = allMethod[second];
        allMethod[second] = swapMethod;
    }


}



