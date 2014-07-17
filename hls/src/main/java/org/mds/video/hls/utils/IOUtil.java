package org.mds.video.hls.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.*;


public final class IOUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final Charset UTF8_CHARSET = Charset.forName("utf-8");

    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static int copy(final InputStream input, final OutputStream output, int bufferSize) throws IOException {
        int avail = input.available();
        if (avail > 262144) {
            avail = 262144;
        }
        if (avail > bufferSize) {
            bufferSize = avail;
        }
        final byte[] buffer = new byte[bufferSize];
        int n = 0;
        n = input.read(buffer);
        int total = 0;
        while (-1 != n) {
            if (n == 0) {
                throw new IOException("0 bytes read in violation of InputStream.read(byte[])");
            }
            output.write(buffer, 0, n);
            total += n;
            n = input.read(buffer);
        }
        return total;
    }

    /**
     * Get the system path to a package
     *
     * @param clazz The clazz within the package
     * @return The full system path to a package containing the class specified
     */
    public static String getAbsolutePath(Class clazz) {
        return clazz.getResource(".").getPath();
    }

    /**
     * Returns a {@see Reader} for the specified file
     *
     * @param filename filename to load
     * @param clazz    The class
     * @return The enriched Reader for the underlying file
     * @throws java.io.IOException If the file doesnt exist or some other error occurs
     */
    public static Reader getReader(String filename, Class clazz) throws IOException {
        return new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(filename)));
    }

    public static String toString(final InputStream input) throws IOException {
        return toString(new InputStreamReader(input, UTF8_CHARSET));
    }

    public static String toString(final Reader input) throws IOException {
        StringBuilder builder = new StringBuilder();
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        int n = 0;
        n = input.read(buffer);
        while (-1 != n) {
            if (n == 0) {
                throw new IOException("0 bytes read in violation of InputStream.read(byte[])");
            }
            builder.append(new String(buffer, 0, n));
            n = input.read(buffer);
        }
        input.close();
        return builder.toString();
    }

    /**
     * Unzips fileToUnzip and returns a list of the unzipped files.  This method doesn't handle zipped directories.
     *
     * @param fileToUnzip zipped file
     * @return list of uncompressed files
     * @throws java.io.IOException exception
     */
    public static final List<File> unzipFile(File fileToUnzip) throws IOException {
        ZipFile zipFile = new ZipFile(fileToUnzip);
        List<File> unzipped = new ArrayList<File>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
            String s = fileToUnzip.getParent();
            File file = new File(s, entry.getName());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = -1;
            byte[] data = new byte[1024];
            while ((len = bis.read(data)) > -1) {
                bos.write(data, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            unzipped.add(file);
        }
        return unzipped;
    }

    /**
     * Zips fileToZip and places it in targetFile.
     *
     * @param fileToZip  file to zip
     * @param targetFile zipped file
     * @throws java.io.IOException exception
     */
    public static void zipFile(File fileToZip, File targetFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile));
        FileInputStream fis = new FileInputStream(fileToZip);
        zos.putNextEntry(new ZipEntry(fileToZip.getName()));
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = fis.read(buf)) > -1) {
            zos.write(buf, 0, len);
        }
        fis.close();
        zos.close();
    }
}
