package com.example.lenovo.im;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.ContactsFragment;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

import static android.content.ContentValues.TAG;

public class MainActivity extends FragmentActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener{
    private long mExitTime;
private TextView btnMore;
    TabLayout msgTab;




    private RecentContactsFragment recentContactsFragment;
    private ContactsFragment contactsFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        msgTab = findViewById(R.id.msg_tab);
        btnMore = findViewById(R.id.btn_more);
        btnMore.setOnClickListener(this);

        if(Preferences.getInstance().getAccount().equals("")){
            startActivity(new Intent(this,LoginActvity.class));
            finish();
        } else {
            Log.v("Main",Preferences.getInstance().getLoginInfo().getAccount());
            NimUIKit.login(Preferences.getInstance().getLoginInfo(), new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    initView();
                }

                @Override
                public void onFailed(int i) {

                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
        }


    }

    private void initView() {
        msgTab.addTab(msgTab.newTab().setText("最近"));
        msgTab.addTab(msgTab.newTab().setText("联系人"));


        msgTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                transaction = getSupportFragmentManager().beginTransaction();
                if (recentContactsFragment != null) {
                    transaction.hide(recentContactsFragment);
                }
                if (contactsFragment != null) {
                    transaction.hide(contactsFragment);
                }


                if (tab.getText().toString().equals("最近")) {

                    if (recentContactsFragment == null) {
                        recentContactsFragment = new RecentContactsFragment();
                        transaction.add(R.id.msg_contain, recentContactsFragment);
                    } else {
                        transaction.show(recentContactsFragment);
                    }

                } else {

                    if (contactsFragment == null) {
                        contactsFragment = new ContactsFragment();
                        transaction.add(R.id.msg_contain, contactsFragment);
                    } else {
                        transaction.show(contactsFragment);
                    }

                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        msgTab.getTabAt(1).select();
        msgTab.getTabAt(0).select();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_more:
                PopupMenu popup = new PopupMenu(this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.main, popup.getMenu());
                //绑定菜单项的点击事件
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this);
                popup.show(); //这一行代码不要忘记了
                break;

        }

    }


    //弹出式菜单的单击事件处理
   @Override
   public boolean onMenuItemClick(MenuItem item) {
       // TODO Auto-generated method stub
       switch (item.getItemId()) {
           case R.id.copy:
               View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
               final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
               editText.setHint("对方账号");
               AlertDialog dialog = new AlertDialog.Builder(this)
                       .setTitle("添加好友")
                       .setView(view)
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       })
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               final String content = editText.getText().toString();


                               final VerifyType verifyType = VerifyType.DIRECT_ADD;
                               String msg = "好友请求附言";
                               NIMClient.getService(FriendService.class).addFriend(new AddFriendData(content, verifyType, msg))
                                       .setCallback(new RequestCallback<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               Log.v("addFriend","------");
                                               Toast.makeText(getApplicationContext(),"添加好友成功",Toast.LENGTH_SHORT).show();
                                               NimUIKit.startP2PSession(getApplicationContext(),content);

                                           }
                                           @Override
                                           public void onFailed(int i) {
                                           }
                                           @Override
                                           public void onException(Throwable throwable) {
                                           }
                                       });

                               dialog.dismiss();
                           }
                       }).create();
               dialog.show();
               break;

           case R.id.delete:
               Preferences.getInstance().deleteAccount();
               startActivity(new Intent(this,LoginActvity.class));
               finish();
               Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
           default:
               break;
       }
           return false;
       }
   }

