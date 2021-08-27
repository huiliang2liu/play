package com.http.service.temp;

import com.http.service.HttpService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Default strategy for creating and cleaning up temporary files.
 * <p/>
 * <p>
 * This class stores its files in the standard location (that is, wherever
 * <code>java.io.tmpdir</code> points to). Files are added to an internal
 * list, and deleted when no longer needed (that is, when
 * <code>clear()</code> is invoked at the end of processing a request).
 * </p>
 */
public class DefaultTempFileManager implements TempFileManager {

    private final String tmpdir;

    private final List<TempFile> tempFiles;

    public DefaultTempFileManager() {
        this.tmpdir = System.getProperty("java.io.tmpdir");
        this.tempFiles = new ArrayList<TempFile>();
    }

    @Override
    public void clear() {
        for (TempFile file : this.tempFiles) {
            try {
                file.delete();
            } catch (Exception ignored) {
                HttpService.LOG.log(Level.WARNING, "could not delete file ", ignored);
            }
        }
        this.tempFiles.clear();
    }

    @Override
    public TempFile createTempFile() throws Exception {
        DefaultTempFile tempFile = new DefaultTempFile(this.tmpdir);
        this.tempFiles.add(tempFile);
        return tempFile;
    }
}