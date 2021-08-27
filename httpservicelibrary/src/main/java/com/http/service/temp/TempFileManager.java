package com.http.service.temp;

/**
 * Temp file manager.
 * <p/>
 * <p>
 * Temp file managers are created 1-to-1 with incoming requests, to create
 * and cleanup temporary files created as a result of handling the request.
 * </p>
 */
public interface TempFileManager {

    void clear();

    TempFile createTempFile() throws Exception;
}
