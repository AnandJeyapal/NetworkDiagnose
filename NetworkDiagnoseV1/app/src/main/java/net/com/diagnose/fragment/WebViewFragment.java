package net.com.diagnose.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import net.com.diagnose.R;
import net.com.diagnose.bean.Logurl;
import net.com.diagnose.ldn.Task.TraceTask;
import net.com.diagnose.utils.SharedPreferenceUtils;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends BaseFragment{ //DialogFragment {

    @Override
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

    public final static String HOME_URL = "http://www.bing.com";//"http://h5.darkal.cn/har/guide/widget.basic2.html";//"http://cn.bing.com";"https://www.baidu.com";//
    public final static String GUIDE_URL = "http://h5.darkal.cn/har/guide/widget.guide2.html";
    public final static String IP_ADDRESS ="127.0.0.1";// "192.168.1.110";
    public final static int PORT= 8888;
    public String url="";

    //@BindView(R.id.fl_webview)
  public static  WebView webView;

  //  @BindView(R.id.bt_jump)
    Button jumpButton;

    //@BindView(R.id.et_url)
    EditText urlText;

   // @BindView(R.id.pb_progress)
    ProgressBar progressBar;

    //@BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    

    public Boolean isSetProxy = false;

    public String baseUserAgentString = "Mozilla/5.0 (Linux; Android 5.0.2) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0"; //"Mozilla/5.0 (Linux; Android 5.0.2) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0";
                                   //https://developers.whatismybrowser.com/useragents/parse/673155-uc-browser-android-vivo-webkit
    public String userAgentString = baseUserAgentString;

    private final static WebViewFragment webViewFragment = new WebViewFragment();

    public static WebViewFragment getInstance() {
        
        return webViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        ButterKnife.bind(this, view);

        isSetProxy = false;
        webView=(WebView) view.findViewById(R.id.fl_webview);
        jumpButton=(Button) view.findViewById(R.id.bt_jump);
        urlText=(EditText) view.findViewById(R.id.et_url);
        progressBar =(ProgressBar)view.findViewById(R.id.pb_progress);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);

        urlText.setText(HOME_URL);
        //getCertification();
        WebSettings webSettings = webView.getSettings();
        System.out.println(":><><URL><><>"+url+":nnnn:"+HOME_URL);
       // webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUserAgentString(userAgentString);
        
        /*baseUserAgentString = webSettings.getUserAgentString()+" jdhttpmonitor/" + DeviceUtils.getVersion(getContext());
        webSettings.setUserAgentString(userAgentString);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }*/
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebViewClient(new WebViewClient() {

        @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
          // System.out.println(":><><URL><><>"+url+":nnnn:"+view.getUrl());
            Logurl logurl =new Logurl();
            logurl.setUrl(url);
            logurl.setStartedDateTime(new Date());
            TraceTask pingTask = new TraceTask(getActivity(),view.getUrl()+"",new TextView(getContext()),logurl);
            pingTask.doTask();
                if(url.startsWith("jdhttpmonitor://webview")) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
              //  System.out.println(":><><URL><22222222222><>"+url+":nnnn:"+view.getUrl());
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                urlText.setText(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
//                super.onConsoleMessage(message, lineNumber, sourceID);
//                ((MainActivity)getActivity()).consoleLog.append(message).append("\n").append("\n");
//            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
              // ((MainActivity)getActivity()).consoleLog.append(consoleMessage.message()).append("\n").append("\n");
                return super.onConsoleMessage(consoleMessage);
            }

            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlText.getText().length() > 0) {
                    loadUrl(urlText.getText() + "");
                }
            }
        });

        urlText.setImeOptions(EditorInfo.IME_ACTION_GO);
        urlText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                
                if (actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (urlText.getText().length() > 0) {
                        //loadUrl(v.getText() + "");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                    //处理事件
                }
                return false;
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                webView.loadUrl(webView.getUrl());
            }
        });

        

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            initProxyWebView();
            //((MainActivity) getActivity()).navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        setUserAgent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void setUserAgent(){
        String originUA = userAgentString;

        switch (SharedPreferenceUtils.getString(getContext(),"select_ua", "0")) {
            case "0":
                userAgentString = baseUserAgentString;
                break;
            case "1":
                userAgentString = baseUserAgentString + " MQQBrowser/6.2 TBS/036524 MicroMessenger/6.3.18.800 NetType/WIFI Language/zh_CN";
                break;
            case "2":
                userAgentString = baseUserAgentString + " MQQBrowser/6.2 TBS/036524 V1_AND_SQ_6.0.0_300_YYB_D QQ/6.0.0.2605 NetType/WIFI WebP/0.3.0 Pixel/1440";
                break;
               /*default:
                   userAgentString = baseUserAgentString;
                   break;*/
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(userAgentString);

        if(!originUA.equals(userAgentString) && webView!=null){
            reload();
        }
    }

    public void reload(){
        if(webView!=null && webView.getUrl()!=null) {
            webView.reload();
        }
    }

    public void loadUrl(String url) {
        System.out.println(":=========webView=====:"+webView);
        if (webView != null) {

            if (!isSetProxy) {
               // Boolean stas= ProxyUtils.setProxy(webView, IP_ADDRESS,PORT);
               // Log.e("~~~~", "initProxyWebView()"+stas);
                isSetProxy = true;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            System.out.println(":=========URL=====:"+url);
            urlText.setText(url);
            webView.loadUrl(url);
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
