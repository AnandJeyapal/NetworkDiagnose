package net.com.diagnose.utils;

public class LogUtils {

    /**
     * Pushes the logs to the server.
     * @param context the application context
     * @param url the server url
     */
    public static void pushLogs(Context context, String url) {
        File file = HyperLog.getDeviceLogsInFile(context);
        HyperLog.setURL(url);
        HyperLog.pushLogs(context, false, new HLCallback() {
            @Override
            public void onSuccess(@NonNull Object response) {
               // Successfully uploaded
            }

            @Override
            public void onError(@NonNull VolleyError HLErrorResponse) {
                // Failed
            }
        });
    }
}