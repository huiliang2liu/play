/*
 * Original work Copyright (c) 2016, Lody
 * Modified work Copyright (c) 2016, Alibaba Mobile Infrastructure (Android) Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xh.base.utils;

import android.util.Log;

import com.xh.base.log.Logger;

import java.lang.reflect.Method;

public class RuntimeUtil {

    private volatile static boolean g64 = false;
    private volatile static boolean isArt = true;
    private volatile static String archType = null;
    private static Method sSetHiddenApiExemptions;
    private static Object sVMRuntime;

    static {
        try {
            g64 = (boolean) Class.forName("dalvik.system.VMRuntime")
                    .getDeclaredMethod("is64Bit")
                    .invoke(Class.forName("dalvik.system.VMRuntime")
                            .getDeclaredMethod("getRuntime")
                            .invoke(null));
        } catch (Throwable th) {
            Logger.e(Logger.TAG, "get is64Bit failed, default not 64bit!", th);
            g64 = false;
        }

        try {
            Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethodMethod = Class.class.getDeclaredMethod(
                    "getDeclaredMethod", String.class, Class[].class);

            Class vmRuntimeClass = (Class) forNameMethod.invoke(null, "dalvik.system.VMRuntime");
            sSetHiddenApiExemptions = (Method) getDeclaredMethodMethod.invoke(vmRuntimeClass,
                    "setHiddenApiExemptions", new Class[]{String[].class});
            Method getVMRuntimeMethod = (Method) getDeclaredMethodMethod.invoke(vmRuntimeClass,
                    "getRuntime", null);
            sVMRuntime = getVMRuntimeMethod.invoke(null);
        } catch (Throwable th) {
        }
        isArt = System.getProperty("java.vm.version").startsWith("2");
        archType = CPUArchUtil.getArchType();
        Logger.i(Logger.TAG, "is64Bit: " + g64 + ", isArt: " + isArt + ", archType: " + archType);
    }

    public static boolean is64Bit() {
        return g64;
    }

    public static boolean isArt() {
        return isArt;
    }

    public static boolean setExemptions(String... methods) {
        if ((sSetHiddenApiExemptions == null) || (sVMRuntime == null)) {
            return false;
        }

        try {
            sSetHiddenApiExemptions.invoke(sVMRuntime, new Object[]{methods});
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean exemptAll() {
        Logger.i("HiddenApiWrapper", "Start execute exemptAll method ...");
        return setExemptions("L");
    }
}
