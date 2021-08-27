package com.xh.base.utils;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.xh.base.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final String COLON_SEPARATOR = ":";

    public static String getProcessName() {
        BufferedReader mBufferedReader = null;
        try {
            File file = new File("/proc/self/cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            return mBufferedReader.readLine().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (mBufferedReader != null) {
                try {
                    mBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<String> readFileByLines(String fileName) {
        List<String> allSOLists = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.endsWith(".so")) {
                    int index = tempString.indexOf("/");
                    if (index != -1) {
                        String str = tempString.substring(index);
                        if (!allSOLists.contains(str)) {
                            Logger.v(Logger.TAG, "str: " + str);
                            // 所有so库（包括系统的，即包含/system/目录下的）
                            allSOLists.add(str);
                        }
                    }
                }
            }
//            Logger.v(Logger.TAG, "allSOLists: " + allSOLists);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return allSOLists;
    }

    public static void cmd(File dir, Map map, String[] cmds) {
        if (cmds.length == 0) {
            return;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(new String[0]);
            String envPath = System.getenv("PATH");
            Logger.v(Logger.TAG, "ENV PATH: " + envPath);
            if (envPath != null) {
                String[] split = envPath.split(COLON_SEPARATOR);
                int length = split.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    File f = new File(split[i], "sh");
                    if (f.exists()) {
                        builder.command(new String[]{f.getPath()}).redirectErrorStream(true);
                        break;
                    }
                    i++;
                }
            }
            builder.directory(dir);
            Map<String, String> environment = builder.environment();
            environment.putAll(System.getenv());
            if (map != null) {
                environment.putAll(map);
            }
            StringBuilder sb = new StringBuilder();
            for (String append : cmds) {
                sb.append(append);
                sb.append("\n");
            }

            Process proc = builder.start();
            OutputStream os = proc.getOutputStream();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream(),
                        "utf-8"));
                for (String cmd : cmds) {
                    if (cmd.endsWith("\n")) {
                        os.write(cmd.getBytes());
                    } else {
                        os.write((cmd + "\n").getBytes());
                    }
                }
                os.write("exit 156\n".getBytes());
                os.flush();
                proc.waitFor();
                read(br);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static String read(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String readLine;
        while ((readLine = br.readLine()) != null) {
            sb.append(readLine);
            sb.append("\n");
        }
        Logger.v(Logger.TAG, "read: " + sb);
        return sb.toString();
    }

    public static boolean startService(Context context, Class<? extends Service> ser) {
        return startService(context, ser.getName());
    }

    public static boolean startService(Context context, String ser) {
        return bindService(context, null, ser);
    }

    public static void bindService(Context context, Class<? extends Service> ser) {
        bindService(context, ser.getName());
    }

    public static void bindService(Context context, String name) {
        bindService(context, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, name);
    }

    public static void bindService(Context context, ServiceConnection cnn, Class<? extends Service> clazz) {
        bindService(context, cnn, clazz.getName());
    }

    public static boolean bindService(Context context, ServiceConnection cnn, String name) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), name);
        if (cnn == null) {
            try {
                context.startService(intent);
                return true;
            } catch (Throwable e) {
                Logger.d("keepalive2-daemon", "ddd", e);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        context.startForegroundService(intent);
                        return true;
                    } catch (Throwable exception) {
                        Logger.d("keepalive2-daemon", "ddd", exception);
                        return false;
                    }
                }
            }
        }
        boolean isSuccess = false;
        try {
            isSuccess = context.bindService(intent, cnn, Context.BIND_AUTO_CREATE);
            return true;
        } catch (Throwable e) {
            Logger.d("keepalive2-daemon", "ddd", e);
        }
        if (!isSuccess) {
            try {
                context.startService(intent);
                return true;
            } catch (Throwable e) {
                Logger.d("keepalive2-daemon", "ddd", e);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        context.startForegroundService(intent);
                        return true;
                    } catch (Throwable exception) {
                        Logger.d("keepalive2-daemon", "ddd", exception);
                    }
                }
            }
        }
        return false;
    }

    public static boolean startBindService(Context context, Class<? extends Service> clazz) {
        return startBindService(context, clazz.getName());
    }

    public static boolean startBindService(Context context, String name) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), name);
        try {
            context.startService(intent);
        } catch (Throwable e) {
            Logger.d("keepalive2-daemon", "ddd", e);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    context.startForegroundService(intent);
                } catch (Throwable exception) {
                    Logger.d("keepalive2-daemon", "ddd", exception);
                }
            }
        }
        try {
            return context.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Throwable e) {
            Logger.d("keepalive2-daemon", "ddd", e);
        }
        return false;
    }

}
