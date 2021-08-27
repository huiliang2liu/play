package com.xh.keepalive.component;

import android.app.Application;
import android.app.Instrumentation;
import android.os.Bundle;

import com.xh.base.log.Logger;
import com.xh.base.utils.Utils;

public class DaemonInstrumentation extends Instrumentation {
    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
        Logger.v(Logger.TAG, "callApplicationOnCreate");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Logger.v(Logger.TAG, "onCreate");
        Utils.bindService(getTargetContext(),DaemonService.class);
    }
}
