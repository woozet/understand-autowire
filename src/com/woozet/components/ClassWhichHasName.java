package com.woozet.components;

import com.woozet.components.annotations.Name;

@Name("default name")
public class ClassWhichHasName {
    private String name;

    public ClassWhichHasName() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
