package com.nameless.base.map;

import java.util.HashMap;

/**
 * Created by boysz on 2018/9/5.
 */
public class HashMapTest {


    public static void main(String[] args) {


        HashMap hashmap = new HashMap<String,String>();

        hashmap.put("A","stanley");
        hashmap.put("B","loop");

        System.out.println(hashmap.get("A"));


    }
}



