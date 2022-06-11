package com.github.namiuni.modernexample.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

// TODO: もっとちゃんとそれっぽく書く
@ConfigSerializable
public class SubSettings {

    private String foo = "bar";

    public String foo() {
        return this.foo;
    }
}
