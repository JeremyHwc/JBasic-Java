package com.basic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSubList {
    @Test(expected = ClassCastException.class)
    public void testClassCast() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(0);
        integerList.add(1);
        integerList.add(2);
        List<Integer> subList = integerList.subList(0, 1);

        // 强转
        ArrayList<Integer> cast = (ArrayList<Integer>) subList;
    }

    @Test
    public void testSubList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("赵");
        stringList.add("钱");
        stringList.add("孙");
        stringList.add("李");
        stringList.add("周");
        stringList.add("吴");
        stringList.add("郑");
        stringList.add("王");

        List<String> subList = stringList.subList(2, 4);
        System.out.println("子列表：" + subList.toString());
        System.out.println("子列表长度：" + subList.size());

        subList.set(1, "慕容");
        System.out.println("子列表：" + subList.toString());
        System.out.println("原始列表：" + stringList.toString());
    }
}
