package com.dmgorbunov.frustaj.tools;

import java.time.Duration;

public class StringUtils {

    public static String formatDuration(Duration duration) {
        if (duration.toMinutes() < 60) {
            return String.format("%d minutes", duration.toMinutes());
        } else {
            if (duration.toHours() < 24) {
                return String.format("%d hours %d minutes", duration.toHours(), duration.toMinutesPart());
            } else {
                return String.format("%d days %d hours %d minutes", duration.toDays(), duration.toHoursPart(), duration.toMinutesPart());
            }
        }
    }
}
