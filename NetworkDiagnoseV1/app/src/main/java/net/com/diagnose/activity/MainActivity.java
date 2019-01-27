package net.com.diagnose.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.security.KeyChain;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.com.diagnose.R;
import net.com.diagnose.bean.Logurl;
import net.com.diagnose.fragment.BackHandledInterface;
import net.com.diagnose.fragment.BaseFragment;
import net.com.diagnose.fragment.LoginFregment;
import net.com.diagnose.fragment.PreviewFrgUrl;
import net.com.diagnose.fragment.SettingFragment;
import net.com.diagnose.fragment.WebViewFragment;
import net.com.diagnose.utils.Contans;
import net.com.diagnose.utils.SharedPreferenceUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
343bfb27-7f7a-4ac0-a5aa-aea646a796d6|10.130.212.240|2018-10-29 00:01:50|/thirdLogin|||wechat||
343bfb27-7f7a-4ac0-a5aa-aea646a796d6|112.38.127.69|2018-10-29 00:03:27|/login/v1|||android||

* */
public class MainActivity extends AppCompatActivity implements BackHandledInterface{

public final static String HOME_URL = "http://h5.darkal.cn/har/guide/widget.basic2.html";//"http://iamtop.net";
public final static String GUIDE_URL = "http://h5.darkal.cn/har/guide/widget.guide2.html";

public SharedPreferences shp;

public Set<String> disablePages = new HashSet<>();
public StringBuffer consoleLog = new StringBuffer();


private long exitTime = 0;



    private BaseFragment mBackHandedFragment;

//@BindView(R.id.fl_contain)
public View rootView;

@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

  setContentView(R.layout.activity_main);
    rootView = (View)findViewById(R.id.fl_contain);
  ButterKnife.bind(this);
  getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    //Contans.setActivity(Ma);
  String language =getIntent().getStringExtra("lang");
  Locale locale=null;
  if(language!=null ){
      locale = new Locale(language);
  }else{
      locale = new Locale("zh");
  }
    
  Locale.setDefault(locale);
  Configuration config = new Configuration();
  config.locale = locale;
  getBaseContext().getResources().updateConfiguration(config,
          getBaseContext().getResources().getDisplayMetrics());



  String sty =getIntent().getStringExtra("urlP");

  if(sty!=null ){
      WebViewFragment webViewFragment = WebViewFragment.getInstance();
      webViewFragment.loadUrl(sty);
      switchContent(webViewFragment);
  }else{
      String valuz = SharedPreferenceUtils.getString(MainActivity.this,"user_name","");
      if(valuz!=null && !valuz.isEmpty()){
          switchContent(LoginFregment.getInstance());
      }else{
          if (getIntent().getStringExtra("url") != null && getIntent().getStringExtra("url").length() > 0) {
              WebViewFragment webViewFragment = WebViewFragment.getInstance();
              webViewFragment.loadUrl(getIntent().getStringExtra("url"));
              switchContent(webViewFragment);
          } else {
              switchContent(WebViewFragment.getInstance());
          }
      }

     
  }
  
}
public  void switchContent(Fragment to) {
    Boolean isAdded = false;
    try {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().getFragments() != null) {
            for (Fragment f : getSupportFragmentManager().getFragments()) {
                if (to.getClass().isAssignableFrom(f.getClass())) {
                    if (!f.isAdded()) {
                        transaction.add(R.id.fl_contain, f, f.getClass().getName());
                    } else {
                        transaction.show(f);
                    }
                    isAdded = true;
                } else {
                    transaction.hide(f);
                    f.setUserVisibleHint(false);
                }
            }
        }
        if (!isAdded) {
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.add(R.id.fl_contain, to, to.getClass().getName()).commitNow();
            } else {
                transaction.show(to).commitNow(); // 隐藏当前的fragment，显示下一个
            }
        } else {
            transaction.commitNow();
        }
        if (getSupportFragmentManager().findFragmentByTag(to.getClass().getName()) != null) {
            getSupportFragmentManager().findFragmentByTag(to.getClass().getName()).setUserVisibleHint(true);

        }
//            setSelectedFragment((BaseFragment) to);
    } catch (Exception e) {
        e.printStackTrace();
    }

}
    

@Override
protected void onStart() {
  super.onStart();
  handleUriStartupParams();
}
    @Override
    protected void onStop() {
        
        super.onStop();
    }

@Override
public void setSelectedFragment(BaseFragment selectedFragment) {
  this.mBackHandedFragment = selectedFragment;
}


private void handleUriStartupParams() {
  Intent intent = getIntent();
  if (intent == null) {
      return;
  }

  Uri uri = intent.getData();
  if (uri == null) {
      return;
  }
  // 这里要把data置空，否则每次进来锁屏解锁，都会触发这些逻辑
  intent.setData(null);

  String query = uri.getQuery();

  // "param="这个字符串已经占了6个字符了，所以query的长度至少要有8（加上花括号)
  if (query == null || query.length() < 8) {
      return;
  }

  try {
      // 通过uri.getQuery()得到的query已经是解码过的字符串了，不需要再decode
      String jsonString = query.substring(6);
      JSONObject json = new JSONObject(jsonString);

      WebViewFragment webViewFragment = WebViewFragment.getInstance();
      webViewFragment.loadUrl(json.getString("url"));

      switchContent(webViewFragment);
  } catch (Exception e) {
      e.printStackTrace();
  }
}



// what is in b that is not in a ?
public static Collection subtractSets(Collection a, Collection b)
{
  Collection result = new ArrayList(b);
  result.removeAll(a);
  return result;

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
  getMenuInflater().inflate(R.menu.popup_menu, menu);
  MenuItem homeButton = menu.findItem(R.id.home_menu);
  MenuItem settingButton = menu.findItem(R.id.setting_menu);
  MenuItem eventButton = menu.findItem(R.id.list_event_menu);
  MenuItem creLogButton = menu.findItem(R.id.create_log_menu);
  MenuItem openLogButton = menu.findItem(R.id.open_log_menu);
    MenuItem logoutButton = menu.findItem(R.id.open_log_menu);
  MenuItem exitButton = menu.findItem(R.id.exit_menu);
  homeButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
          /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);*/
          WebViewFragment webViewFragment= WebViewFragment.getInstance();
          switchContent(webViewFragment);
          return true;
      }
  })      ;
  settingButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
          SettingFragment   settingFragment = SettingFragment.getInstance();
              switchContent(settingFragment);
          return true;
      }
  });
  eventButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
          System.out.println(":>>>event:::<<<<<:");
          //PreviewFragment   previewFragment = PreviewFragment.getInstance();
          PreviewFrgUrl previewFragment = PreviewFrgUrl.getInstance();
          switchContent(previewFragment);
          return true;
      }
  });
  openLogButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
         SharedPreferenceUtils.putString(MainActivity.this,"user_name","");
          switchContent(LoginFregment.getInstance());
          return true;
      }
  });

  creLogButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {

       List<Logurl> listUrl = Contans.getList();
          if(listUrl!=null){
            System.out.println(":>>>logurl:::<<<<<:"+listUrl.size());
              createLogfile(listUrl);
              try {
                  Contans.sendServerfile();
              } catch (IOException e) {
                  e.printStackTrace();
              }


              Gson gson = new Gson();
               //convert your list to json
            String jsonLogurl = gson.toJson(listUrl);
               //print your generated json
          System.out.println("jsonLogurl: " + jsonLogurl);
          }
          return true;
      }
  });


  exitButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
          finish();
          System.exit(0);
          return true;
      }
  });

  return super.onCreateOptionsMenu(menu);
}



    public void createLogfile(List<Logurl> logurl) {
        File backupPath = Environment.getExternalStorageDirectory();

       // backupPath = new File(backupPath.getPath() + "/Android/data/com.maximusdev.bankrecord/files");

     /*   if (!backupPath.exists()) {
            backupPath.mkdirs();
        }
*/
        FileOutputStream fos;
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                    .format(new Date(System.currentTimeMillis()));
            FileUtils.forceMkdir(new File(Environment.getExternalStorageDirectory() + "/infotop"));
            fos = new FileOutputStream(backupPath.getPath()+File.separator+"infotop" +File.separator+ "/"+currentTime+".txt");
          //System.out.println(":>>>><<<<:"+backupPath.getPath());
      if(logurl!=null && logurl.size()>0){
          for(int i = 0; i < logurl.size(); ++i){
              Logurl entry = logurl.get(i);
             /* HarRequest harRequest = entry.getRequest();
              HarResponse harResponse = entry.getResponse();*/
             String valuz = SharedPreferenceUtils.getString(MainActivity.this,"user_name","");
              String loginthird = SharedPreferenceUtils.getString(MainActivity.this,"user_login","");

              if(valuz!=null && !valuz.isEmpty()){
                  fos.write(valuz.getBytes());
                  fos.write("||".getBytes());
              }

              fos.write(entry.getLogDetail().getServerIPAddress().getBytes());
              fos.write("||".getBytes());
              fos.write(entry.getUrl().getBytes());
              fos.write("||".getBytes());

              fos.write(DateFormat.format("yyyy-MM-dd HH:mm:ss",entry.getStartedDateTime()).toString().getBytes());
              fos.write("||".getBytes());
              if(loginthird!=null && !loginthird.isEmpty()){
                  fos.write("thirdLogin".getBytes());
                  fos.write("||".getBytes());
                  fos.write(loginthird.getBytes());
                  fos.write("||".getBytes());
              }
              fos.write("android".getBytes());
              fos.write("||".getBytes());
              fos.write("\n".getBytes());
             /* System.out.println(":>>>>ST<<<<:"+entry.getStartedDateTime().toString());
              String dateString = DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(entry.getTime())).toString();
              System.out.println(":>>>>dateString<<<<:"+dateString);*/

          }
      }
            fos.close();
            Toast.makeText(this, "Backup Complete", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
