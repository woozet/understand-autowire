package com.woozet.components;

import com.woozet.components.annotations.Autowired;

import java.util.ArrayList;

public class DITarget {
    @Autowired
    ClassWhichHasName classWhichHasName;

    // example for failed to wire
    @Autowired
    ArrayList<String> arrayList;

    public DITarget() {}

    public void printPrettyName() {
        System.out.println("============================");
        System.out.println("My name is here below..");
        System.out.println(classWhichHasName.getName());
        System.out.println("============================");

    }
}
