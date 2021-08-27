package com.http.http.listen;

import java.io.File;

/**
 * threadPool com.xh.http.listen 2018 2018-4-28 下午2:45:46 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public interface ProgressListen {
	void progress(float progress);
	void end(File file);
}
