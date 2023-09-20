package com.github.namiuni.modernexample.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.moonshine.annotation.Message;

public interface ModernMessages {

    /*
     * =============================================================
     * ========================== Reload ===========================
     * =============================================================
     */

    @Message("config.reload.success")
    void configReloaded(final Audience audience);

    @Message("config.reload.failed")
    void configReloadFailed(final Audience audience);

    /*
     * =============================================================
     * =================== Command Documentation ===================
     * =============================================================
     */

    @Message("command.help.argument.query")
    Component commandHelpArgumentQuery();

    @Message("command.help.description")
    Component commandHelpDescription();

    @Message("command.reload.description")
    Component commandReloadDescription();
}
