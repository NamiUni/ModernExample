/*
 * CarbonChat
 *
 * Copyright (c) 2023 Josua Parks (Vicarious)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.namiuni.modernexample;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.ApiStatus;


@DefaultQualifier(NonNull.class)
public final class ModernExampleProvider {

    private static @Nullable ModernExample instance;

    private ModernExampleProvider() {
        throw new IllegalStateException("Instantiation is not permitted");
    }

    @ApiStatus.Internal
    public static void register(final ModernExample modernExample) {
        ModernExampleProvider.instance = modernExample;
    }

    public static ModernExample modernExample() {
        if (ModernExampleProvider.instance == null) {
            throw new IllegalStateException("ModernExample not initialized!");
        }

        return ModernExampleProvider.instance;
    }
}
