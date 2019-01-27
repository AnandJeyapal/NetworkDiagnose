package net.com.diagnose.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import net.com.diagnose.bean.DataSendServer;
import net.com.diagnose.bean.LogDetail;
import net.com.diagnose.bean.Logurl;
import net.com.diagnose.ldn.Task.TraceTask;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Contans {



    public static Activity activity;
    public static List<Logurl> listLogurl= new ArrayList<Logurl>(10);
    public static String getString(Context context, int rid){
        return context.getResources().getString(rid) ;
    }

    public static void addList(Logurl logurl){
        listLogurl.add(logurl) ;
    }
    public static List<Logurl> getList(){
        return listLogurl;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Contans.activity = activity;
    }

    public static boolean isBlankString(String value) {
        return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
    }

    public static <T> String listObjecttoJson(List<T> listObject)
    {
        String jsonObject=null;

        try{
            Gson gson = new Gson();

            String listString = gson.toJson(
                    listObject,
                    new TypeToken<ArrayList<T>>() {}.getType());


            JSONArray jsonArray =  new JSONArray(listString);
            if(jsonArray!=null){

                jsonObject=jsonArray.toString();


            }

        } catch (Exception e){

        }
        return  jsonObject;
    }
    public static Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }


    public static void storeFile(String path,Context context,long periodz){
        Timer timer = new Timer();
        if(!isBlankString(path)){

        }else{
            if(periodz==0L){
                new MyTimeTask(getList(),context);
            }else{
                timer.schedule (new MyTimeTask(getList(),context),0,periodz);  //1000*60*15
            }


        }
    }
    public static void getCaptueractivity(String url,Context context){
        Logurl logurl =new Logurl();
        logurl.setUrl(url);
        logurl.setStartedDateTime(new Date());
        TraceTask pingTask = new TraceTask(getActivity(context),url+"",new TextView(context),logurl);
        pingTask.doTask();
    }

    public static List<Logurl> getListLogurl() {
        return listLogurl;
    }

    public static String currentDate() {

        String currentTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(System.currentTimeMillis()));

        return currentTime;
    }

    /*public static String currentDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateVal = sdf.format(new java.util.Date());

        return dateVal;
    }
*/
    /*public static String currentDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateVal = sdf.format(new java.util.Date());
        return dateVal;
    }*/

    public static  class MyTimeTask extends TimerTask {
        private List<Logurl> logurl;
        private Context context;
        public MyTimeTask(List<Logurl> logurl, Context context){
            this.logurl=logurl;
            this.context=context;
        }
        public void run() {
            createLogfile(logurl,context);
        }
    }
    public static void createLogfile(List<Logurl> logurl, Context context) {
        File backupPath = Environment.getExternalStorageDirectory();

        FileOutputStream fos;
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                    .format(new Date(System.currentTimeMillis()));
            FileUtils.forceMkdir(new File(Environment.getExternalStorageDirectory() + "/infotop"));
            String filePath =backupPath.getPath()+File.separator+"infotop" +File.separator+ "/"+currentTime+".txt";
            fos = new FileOutputStream(filePath);

           // fos = new FileOutputStream(backupPath.getPath()+File.separator+"infotop" +File.separator+ "/"+currentTime+".txt");

            if(logurl!=null && logurl.size()>0){
                String jsonStr = listObjecttoJson(logurl);
                if(!isBlankString(jsonStr)){
                    fos.write(jsonStr.getBytes());
                }
                /*for(int i = 0; i < logurl.size(); ++i){
                    Logurl entry = logurl.get(i);

                    String valuz = SharedPreferenceUtils.getString(context,"user_name","");
                    String loginthird = SharedPreferenceUtils.getString(context,"user_login","");

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


                }*/
            }
            fos.close();
            Toast.makeText(context, "Backup Complete", Toast.LENGTH_SHORT).show();
            SSH2Util ssh2Util = new SSH2Util();
           // ssh2Util.uploadFile(filePath,"/root/logfiles","/lognetwork.txt");

            String jsonStr = listObjecttoJson(logurl);
          // JSCHcl.WriteFileToLinux(SSH2Util.serverIp,SSH2Util.userName,SSH2Util.password,SSH2Util.int_SSHPort,jsonStr,"/root/logfiles","/networkdia.txt");


        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void getCaptuerWebView(final Context context , WebView webView, View view){

        WebSettings webSettings = webView.getSettings();

        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        String dir =context.getApplicationContext().getDir("database", 0).getPath();
        webSettings.setDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);

        String baseUserAgentString = "Mozilla/5.0 (Linux; Android 5.0.2) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0";
        webSettings.setUserAgentString(baseUserAgentString);
        //webView.setDownloadListener(new MyWebViewDownLoadListener(null));
        final Activity activity = getActivity(context);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Logurl logurl = new Logurl();
                logurl.setUrl(url);
                logurl.setStartedDateTime(new Date());
                TraceTask pingTask = new TraceTask(activity, view.getUrl() + "", new TextView(context), logurl);
                pingTask.doTask();
                if (url.startsWith("jdhttpmonitor://webview")) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //urlEdit.setText(url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            public void onProgressChanged(WebView view, int progress) {

            }
        });


        /*urlEdit.setImeOptions(2);
        urlEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 2 || actionId == 6 || event != null && 66 == event.getKeyCode() && 0 == event.getAction()) &&urlEdit.getText().length() > 0) {
                    Activity activity =getActivity(context);
                    InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

                return false;
            }
        });*/


    }
    public static void loadUrl(String url,WebView webView) {

        if (webView != null) {


            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }


            webView.loadUrl(url);
        }

    }

    public static void sendServerfile() throws IOException {
        Socket socket;
        System.out.println("=ddd=====>>>>>>");
        List<Logurl> logurl = getListLogurl();
        File f = new File(Environment.getExternalStorageDirectory(), "new");
        if (!f.exists()) {
            f.mkdir();
        }
        File file = new File("diagnose/networkDiagnose.txt");
        FileWriter w = new FileWriter("/sdcard/new/new.txt");
        BufferedWriter out = new BufferedWriter(w);
        if (logurl != null && logurl.size() > 0) {
            String jsonStr = listObjecttoJson(logurl);
            if (!isBlankString(jsonStr)) {
                out.write(jsonStr);
            }
            //out.write(data);
            out.flush();
            out.close();
            String file_name = "/sdcard/new/new.txt";

            File file1 = new File(file_name);
            if(!file.exists()){
              file.createNewFile();
              System.out.println("======>>>>>>"+file.getName());
            }
            socket = new Socket("172.18.17.40", 2020);
            System.out.println("===socket===>>>>>>"+file.getName());
            System.out.println("===socket===>>>>>>"+socket.toString());
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(file.getName());

            FileInputStream fis = new FileInputStream(file1);
            byte[] buffer = new byte[450000];
            Integer bytesRead = 0;

            while ((bytesRead = fis.read(buffer)) > 0) {
                oos.writeObject(bytesRead);
                oos.writeObject(Arrays.copyOf(buffer, buffer.length));
            }
            System.out.println("===socket=finish==>>>>>>");
            oos.close();
            ois.close();
            System.exit(0);
        }
    }
    public static String toJson(DataSendServer o)
    {
        String jsonObject=null;

        try{
            Gson gson = new Gson();
            jsonObject = gson.toJson(o);

        } catch (Exception e){
            Log.i("s--=-=-:==",e.toString());
        }
        return  jsonObject;
    }
    public static List<LogDetail> filereader(InputStreamReader filename){
    Type REVIEW_TYPE = new TypeToken<List<LogDetail>>() {
    }.getType();
    Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(filename);//new FileReader(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<LogDetail> data = gson.fromJson(reader, LogDetail[].class); // contains the whole reviews list
return data; // prints to screen some values
}
public static void create(InputStreamReader filename)
{
    Reader reader = null;
    TypeToken<List<LogDetail>> token = new TypeToken<List<LogDetail>>() {};
    try {
      //  reader = new FileReader(filename.);
        JsonElement elem = new JsonParser().parse(filename);
        Gson gson  = new GsonBuilder().create();
        List<LogDetail> o = gson.fromJson(elem, token.getType());
        System.out.println(o.size());
    } catch (Exception e) {

        e.printStackTrace();
    }

}
    public static <T> List<T>readJsonFile(InputStreamReader filename,Class<T> cls)
    {
        Set<T> list = new HashSet<>(10);
        //  Reader reader = null;
        TypeToken<List<LogDetail>> token = new TypeToken<List<LogDetail>>() {};
        try {
            //  reader = new FileReader(filename.);

            JsonElement elem = new JsonParser().parse(filename);
            //elem.setLenient(true);

            Gson gson  = new GsonBuilder().create();
            //JsonArray arry = new JsonParser().parse(elem).getAsJsonArray();
            System.out.println(elem);
            if(!elem.isJsonNull()){
                if(elem.isJsonArray()){
                    //System.out.println("=====isArray==========");
                    JsonArray arry = elem.getAsJsonArray();
                    for (JsonElement jsonElement : arry) {
                        list.add(gson.fromJson(jsonElement, cls));
                    }
                }else if(elem.isJsonObject()){
                    //System.out.println("=====isObject==========");
                    JsonObject jsonObject =elem.getAsJsonObject();
                    if(jsonObject!=null){
                        JsonArray jsonArray=  jsonObject.getAsJsonArray("logDetailList");
                        // System.out.println("=====jsonArray=========="+jsonArray);
                        for (JsonElement jsonElement : jsonArray) {
                            list.add(gson.fromJson(jsonElement, cls));
                        }
                    }
                } else if(elem.isJsonPrimitive()){
                    //System.out.println("=====isPrimiti==========");
                }
            }

            /*List<LogDetail> o = gson.fromJson(elem, token.getType());
             */ System.out.println(list.size());
        } catch (Exception e) {

            e.printStackTrace();
        }
        return new ArrayList<>(list);
    }
    public static void generaterLog(final LogDetail logDetail){
        try {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        //Your code goes here

                        List<LogDetail> list=  JSCHcl.readFileFromLinuxJosnList(SSH2Util.serverIp,SSH2Util.userName,SSH2Util.password,SSH2Util.PORT,"/root/",currentDate()+".txt");
                        if(list!=null&& list.size()!=0){
                            list.add(logDetail);
                        }else{
                            list = new ArrayList<>(10);
                            list.add(logDetail);
                        }
                        DataSendServer dataSendServer =new DataSendServer();
                        dataSendServer.setStatus("");
                        dataSendServer.setLogDetailList(list);
                        String jsonStr = Contans.toJson(dataSendServer);

                        JSCHcl.WriteFileToLinux(SSH2Util.serverIp,SSH2Util.userName,SSH2Util.password,SSH2Util.PORT,jsonStr,"/root/",currentDate()+".txt");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


        }catch (Exception e){

        }
    }

}