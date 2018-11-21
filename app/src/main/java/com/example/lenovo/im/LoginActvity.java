package com.example.lenovo.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * @author lifan
 * @date 2018/11/21 12:15
 * @email 2224779926@qq.com
 * @desc
 */

public class LoginActvity extends Activity implements View.OnClickListener{
    EditText username,password;
    TextView btnLogin,btnExit;
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnExit = findViewById(R.id.btn_exit);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnExit.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        Log.v("Login",username.getText().toString()+"nn"+password.getText().toString());
        switch (v.getId()){

            case R.id.btn_login:
                NimUIKit.login(new LoginInfo(username.getText().toString(), password.getText().toString()), new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        Preferences.getInstance().setAccount(loginInfo.getAccount());
                        finish();
                        startActivity(new Intent(LoginActvity.this,MainActivity.class));

                        btnLogin.setText("登录成功");

                    }

                    @Override
                    public void onFailed(int i) {
                        btnLogin.setText("登录失败");
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
                break;
            case R.id.btn_exit:
                Log.v("Login","退出");
                System.exit(0);
                break;
        }
    }

    /***
     * 按两次返回键退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
