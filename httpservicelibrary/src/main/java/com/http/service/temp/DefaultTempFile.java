package com.http.service.temp;

import com.http.service.HttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Default strategy for creating and cleaning up temporary files.
 * <p/>
 * <p>
 * By default, files are created by <code>File.createTempFile()</code> in
 * the directory specified.
 * </p>
 */
public class DefaultTempFile implements TempFile {

    private final File file;

    private final OutputStream fstream;

    public DefaultTempFile(String tempdir) throws IOException {
        this.file = File.createTempFile("NanoHTTPD-", "", new File(tempdir));
        this.fstream = new FileOutputStream(this.file);
    }

    @Override
    public void delete() throws Exception {
        HttpService.safeClose(this.fstream);
        if (!this.file.delete()) {
            throw new Exception("could not delete temporary file");
        }
    }

    @Override
    public String getName() {
        return this.file.getAbsolutePath();
    }

    @Override
    public OutputStream open() throws Exception {
        return this.fstream;
    }
}
