package com.luochan.spark_web.domain;

import java.util.ArrayList;
import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        CourseClickCount a=new CourseClickCount();
        System.out.println(a.getValue());
        System.out.println(a.hashCode());
        System.out.println(Arrays.hashCode(new int[10]));
        ArrayList<Integer> s=new ArrayList<Integer>();
        s.add(1);
        System.out.println(s.size());

    }
}
