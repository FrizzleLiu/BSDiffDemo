package com.jni.bsdiffdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tVersion = findViewById(R.id.tv_version);
        tVersion.setText(BuildConfig.VERSION_NAME);
        final Button btUpdate = findViewById(R.id.bt_update);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerms();
            }
        });
    }

    private void requestPerms() {
        //权限,简单处理下
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
            String[] perms= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms,200);
            }else {
                update();
            }
        }
    }

    //从本地内存取patch差分文件和旧的apk文件合并
    private void update() {
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                //私有目录下的apk文件
                String oldApk = getApplicationInfo().sourceDir;
                //获取差分包的绝对路径
                String patch= new File(Environment.getExternalStorageDirectory(),"patch").getAbsolutePath();
                //合成的新apk的存放路径
                String output=createNewApk().getAbsolutePath();
                bsPatch(oldApk,patch,output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                //合成后安装
                InstallUtils.installApk(MainActivity.this,file);
            }
        }.execute();
    }

    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(),"bspatch.apk");
        if (!newApk.exists()) {
            try {
                //占位
                newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newApk;
    }

    public native void bsPatch(String oldApk,String patch,String output);

}
