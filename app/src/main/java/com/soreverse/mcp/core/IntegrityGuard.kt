/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (C) 2026 bilieebiliee1-design
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 */

package com.soreverse.mcp.core

import android.app.Activity
import android.content.Context
import android.os.Process
import kotlin.system.exitProcess

object IntegrityGuard {
    data class Result(
        val trusted: Boolean,
        val reason: String,
        val expected: String,
        val actual: List<String>,
        val threats: List<String> = emptyList()
    )

    /**
     * No-op. All signature/identity/enforcement checks have been removed so
     * that repackaged or modified builds can start without being blocked.
     */
    fun enforce(context: Context) {
        // Intentionally empty — no integrity enforcement.
    }

    /**
     * No-op. The early gate at attachBaseContext no longer performs any check.
     */
    fun enforceEarly(context: Context) {
        // Intentionally empty — no early gate.
    }

    /**
     * Always returns a trusted result. No identity or threat checks are
     * performed.
     */
    fun inspect(context: Context): Result = Result(
        trusted = true,
        reason = "integrity checks disabled",
        expected = "",
        actual = emptyList()
    )

    /**
     * Always returns true. No native or Java identity checks are performed.
     */
    fun isTrusted(context: Context): Boolean = true

    fun terminateWithContext(context: Context) {
        try {
            if (context is Activity) {
                context.finishAffinity()
            }
        } catch (_: Exception) {
        }
        Process.killProcess(Process.myPid())
        exitProcess(173)
    }

    fun terminate(activity: Activity) {
        runCatching { activity.finishAffinity() }
        exitProcess(173)
    }
}

internal fun normalizeFingerprint(value: String): String = value.filter {
    it.isLetterOrDigit()
}.uppercase()
