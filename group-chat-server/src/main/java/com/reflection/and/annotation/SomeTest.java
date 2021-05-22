package com.reflection.and.annotation;

import com.reflection.and.annotation.interfaces.AfterSuite;
import com.reflection.and.annotation.interfaces.BeforeSuite;
import com.reflection.and.annotation.interfaces.Test;

public class SomeTest {
    @BeforeSuite
    public void beforeSuite(){
        System.out.println("Перед всеми");
    }

    @Test(priority = 1)
    public void test1(){
        System.out.println("Тест 1");
    }
    @Test(priority = 2)
    public void test2(){
        System.out.println("Тест 2");
    }
    @Test(priority = 3)
    public void test3(){
        System.out.println("Тест 3");
    }
    @Test(priority = 2)
    public void test1Two(){
        System.out.println("Тест 2 Два");
    }

    @AfterSuite
    public void afterSuite(){
        System.out.println("После всех");
    }

}
