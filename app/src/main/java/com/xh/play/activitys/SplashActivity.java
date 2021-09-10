package com.xh.play.activitys;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.http.ResponseObject;
import com.http.listen.ResponseObjectListener;
import com.xh.base.adapter.FragmentAdapter;
import com.xh.base.log.Logger;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.ViewPager;
import com.xh.image.IImageLoad;
import com.xh.paser.IPlatform;
import com.xh.paser.IVip;
import com.xh.play.BuildConfig;
import com.xh.play.HttpManager;
import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.entities.Entities;
import com.xh.play.entities.IP;
import com.xh.play.entities.Movies;
import com.xh.play.entities.Splash;
import com.xh.play.entities.Update;
import com.xh.play.fragments.SplashFragment;
import com.xh.play.utils.Constants;
import com.xh.play.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import dalvik.system.BaseDexClassLoader;


public class SplashActivity extends BaseActivity {
    @BindView(R.id.activity_splash_vp)
    ViewPager viewPager;
    CountDownLatch countDownLatch = new CountDownLatch(4);
    private FragmentAdapter adapter;
    private List<Fragment> fragments;
    private File saveFile;

    @Override
    protected int layout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void bindData() {
        super.bindData();
        saveFile = new File(getCacheDir(), "movies.dex");
        if (application.splash) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        PoolManager.longTime(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    PoolManager.runUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((SplashFragment) fragments.get(fragments.size() - 1)).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        getIP();
        getUpdate();
        getSplash();
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                getMovies();
            }
        });
    }

    private void getMovies() {
        Map<String, Object> params = new HashMap<>();
        params.put("appName", getPackageName());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            params.put("appVer", info.versionName);
            params.put("appVerCode", info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences sp = application.cache;
        int ver = sp.getInt(Constants.MOVIE_PLUG, BuildConfig.MOVIE_PLUG);
        params.put("moviePlug", ver);
        ResponseObject responseObject = (ResponseObject) httpManager.request(params);
        if (responseObject != null) {
            Movies movies = (Movies) responseObject.response;
            if (movies != null && movies.url != null && !movies.url.isEmpty() && movies.verCode > ver) {
                downMovies(movies);
                return;
            }
        }
        downMovies(null);
    }

    private void downMovies(Movies movies) {
        int times = 0;
        boolean success = false;
        while (movies != null) {
            times++;
            try {
                Logger.e("下载plug");
                HttpURLConnection connection = (HttpURLConnection) new URL(movies.url).openConnection();
                connection.setUseCaches(false);
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(5000);
                connection.connect();
                success = Util.stream2file(connection.getInputStream(), saveFile);
                if (success) {
                    SharedPreferences sp = application.cache;
                    sp.edit().putInt(Constants.MOVIE_PLUG, movies.verCode).commit();
                    break;
                }
                if (times > 20)
                    break;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!success)
            success = down2assets();
        if (!success) {
            PoolManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "下载插件失败", Toast.LENGTH_SHORT).show();
                }
            });
            countDownLatch.countDown();
            return;
        }
        PoolManager.runUiThread(new Runnable() {
            @Override
            public void run() {
                File optimizedDirectory = getDir("dex", Context.MODE_PRIVATE);
                BaseDexClassLoader baseDexClassLoader = new BaseDexClassLoader(saveFile.getAbsolutePath(),
                        optimizedDirectory, null, getClassLoader());
                try {
                    Class clazz = baseDexClassLoader.loadClass("com.xh.movies.PlatformsManager");
                    application.setPlatformsManager(clazz);
                } catch (Exception e) {
                    e.printStackTrace();
                    PoolManager.runUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "加载插件失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                countDownLatch.countDown();
            }
        });
    }

    private boolean down2assets() {
        if (saveFile.exists())
            return true;
        try {
            InputStream is = getAssets().open("movies.dex");
            return Util.stream2file(is, saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getSplash() {
        Map<String, Object> params = new HashMap<>();
        params.put("appName", getPackageName());
        httpManager.request(params, new ResponseObjectListener() {
            @Override
            public void success(ResponseObject response) {
                images((Splash) response.response);
                countDownLatch.countDown();
            }

            @Override
            public void start() {

            }

            @Override
            public void failure() {
                images(null);
                countDownLatch.countDown();
            }
        });
    }

    private void images(Splash splash) {
        String[] images = null;
        if (splash != null)
            images = splash.images;
        fragments = new ArrayList<>();
        if (images == null || images.length <= 0) {
            fragments.add(new SplashFragment());
        } else {
            for (String img : images)
                fragments.add(new SplashFragment().setUrl(img));
        }
        adapter = new FragmentAdapter(this, fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, false);
    }

    private void getIP() {
        httpManager.request(new ResponseObjectListener() {
            @Override
            public void success(ResponseObject response) {
                application.ip = (IP) response.response;
                Logger.e(Logger.TAG, String.valueOf(application.ip));
                countDownLatch.countDown();
            }

            @Override
            public void start() {

            }

            @Override
            public void failure() {
                countDownLatch.countDown();
            }
        });
    }

    private void getUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("appName", getPackageName());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            params.put("appVer", info.versionName);
            params.put("appVerCode", info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        httpManager.request(params, new ResponseObjectListener() {
            @Override
            public void success(ResponseObject response) {
                application.update = (Update) response.response;
                Logger.e(Logger.TAG, String.valueOf(application.update));
                countDownLatch.countDown();
            }

            @Override
            public void start() {

            }

            @Override
            public void failure() {
                countDownLatch.countDown();
            }
        });
    }
}
