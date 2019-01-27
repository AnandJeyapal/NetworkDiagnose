package net.com.diagnose.utils;



import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class Test8 {

    /**
     * 连接linux系统
     * @param args
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) {

        try {
            Connection conn = new Connection("172.18.17.40");
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword("root",
                    "infotop.123");
            if (isAuthenticated == false) {
                throw new IOException("Authentication failed");
            }
            Session sess = conn.openSession();

            sess.requestPTY("bash");
            sess.startShell();




            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());
            BufferedReader stdoutReader = new BufferedReader(
                    new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(
                    new InputStreamReader(stderr));
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(sess.getStdin());
            String temp = "";
            while (!temp.equals("exit")) {
                System.out.print("[root@vmone ~]#");
                temp = inputReader.readLine();
                out.println(temp);
                out.flush();
                String line = "Null";
                while ((line = stdoutReader.readLine()) != null ) {
                    if (line.length() == 0) { // line equals null never happens, causing the program card to be here
                        continue ;
                    } else {
                        System.out.println(line);
                    }
                }
                System.out.println("Here is the output from stderr:" );
                while ( true ) {
                    line = stderrReader.readLine() ;
                    if (line == null)
                        break;
                    System.out.println(line);
                }
            }
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close();
            conn.close();
            System.out.println("close connection");
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);

        }
    }


}