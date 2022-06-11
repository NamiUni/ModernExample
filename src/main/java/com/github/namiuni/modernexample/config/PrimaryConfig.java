package com.github.namiuni.modernexample.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Locale;

// とりあえず動くけど呼び出しタイミングがreloadコマンド実行した時だからリロードするまでプラグインディレクトリが作成されないｗ
// TODO: 起動したタイミングで作成されるようにする
@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class PrimaryConfig {

    @Comment("プラグインメッセージのデフォルト言語")
    private Locale defaultLocale = Locale.US;

    private SubSettings subSettings = new SubSettings();

    public Locale defaultLocale() {
        return this.defaultLocale;
    }
}
