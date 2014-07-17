package org.mds.video.hls.utils;

/**
 * Extends org.apache.commons.lang3.StringUtils to add some methods
 *
 * @author Randall.mo
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String substringBefore(String str, char separator) {
        if (isEmpty(str)) {
            return str;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfter(String str, char separator) {
        if (isEmpty(str)) {
            return str;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + 1);
    }

    public static String substringBeforeLast(String str, char separator) {
        if (isEmpty(str)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfterLast(String str, char separator) {
        if (isEmpty(str)) {
            return str;
        }

        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - 1) {
            return EMPTY;
        }
        return str.substring(pos + 1);
    }
}
