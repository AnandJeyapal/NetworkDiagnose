package net.com.diagnose.utils;

import com.jcraft.jsch.*;

import net.com.diagnose.bean.DataSendServer;
import net.com.diagnose.bean.LogDetail;

import java.io.*;
import java.util.*;

public class JSCHcl {
    public JSCHcl() {
    }

   /* public static List<LogDetail> readFileFromLinuxJosnList(String serverIp, String userName, String password, int port, String s, String s1) {
    }*/

    public String ReadFileFromLinux(String str_Host, String str_Username, String str_Password, int int_Port, String str_FileDirectory, String str_FileName) {
        JSch obj_JSch = new JSch();
        Session obj_Session = null;
        StringBuilder obj_StringBuilder = new StringBuilder();
        try {
            obj_Session = obj_JSch.getSession(str_Username, str_Host);
            obj_Session.setPort(int_Port);
            obj_Session.setPassword(str_Password);
            Properties obj_Properties = new Properties();
            obj_Properties.put("StrictHostKeyChecking", "no");
            obj_Session.setConfig(obj_Properties);
            obj_Session.connect();
            Channel obj_Channel = obj_Session.openChannel("sftp");
            obj_Channel.connect();
            ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
            obj_SFTPChannel.cd(str_FileDirectory);
            InputStream obj_InputStream = obj_SFTPChannel.get(str_FileName);
            char[] ch_Buffer = new char[0x10000];
            Reader obj_Reader = new InputStreamReader(obj_InputStream, "UTF-8");
            int int_Line = 0;
            do {
                int_Line = obj_Reader.read(ch_Buffer, 0, ch_Buffer.length);
                if (int_Line > 0) {
                    obj_StringBuilder.append(ch_Buffer, 0, int_Line);
                }
            }
            while (int_Line >= 0);
            obj_Reader.close();
            obj_InputStream.close();
            obj_SFTPChannel.exit();
            obj_Channel.disconnect();
            obj_Session.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return obj_StringBuilder.toString();
    }

    public static List<LogDetail> readFileFromLinuxJosnList(String str_Host, String str_Username, String str_Password, int int_Port, String str_FileDirectory, String str_FileName) {
        List<LogDetail> logDetailList = new ArrayList<>(10);
        JSch obj_JSch = new JSch();
        Session obj_Session = null;
        StringBuilder obj_StringBuilder = new StringBuilder();
        try {
            obj_Session = obj_JSch.getSession(str_Username, str_Host);
            obj_Session.setPort(int_Port);
            obj_Session.setPassword(str_Password);
            Properties obj_Properties = new Properties();
            obj_Properties.put("StrictHostKeyChecking", "no");
            obj_Session.setConfig(obj_Properties);
            obj_Session.connect();
            Channel obj_Channel = obj_Session.openChannel("sftp");
            obj_Channel.connect();
            ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
            obj_SFTPChannel.cd(str_FileDirectory);
            InputStream obj_InputStream = obj_SFTPChannel.get(str_FileName);

            logDetailList.addAll(Contans.readJsonFile(new InputStreamReader(obj_InputStream, "UTF-8"), LogDetail.class));

            obj_InputStream.close();
            obj_SFTPChannel.exit();
            obj_Channel.disconnect();
            obj_Session.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logDetailList;
    }

    public String JsonReader(String str_Host, String str_Username, String str_Password, int int_Port, String str_FileDirectory, String str_FileName) {
        JSch obj_JSch = new JSch();
        Session obj_Session = null;
        StringBuilder obj_StringBuilder = new StringBuilder();
        try {
            obj_Session = obj_JSch.getSession(str_Username, str_Host);
            obj_Session.setPort(int_Port);
            obj_Session.setPassword(str_Password);
            Properties obj_Properties = new Properties();
            obj_Properties.put("StrictHostKeyChecking", "no");
            obj_Session.setConfig(obj_Properties);
            obj_Session.connect();
            Channel obj_Channel = obj_Session.openChannel("sftp");
            obj_Channel.connect();
            ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
            obj_SFTPChannel.cd(str_FileDirectory);
            Vector cls = obj_SFTPChannel.ls(str_FileDirectory);
            //System.out.println("cls finished"+ ));
            // Vector hh=(Vector) cls.iterator();
            /*for(int i =0;i<hh.size();i++){
                System.out.println("vector file printed here"+hh.get(i).toString());
            }
*/
            Iterator<ChannelSftp.LsEntry> itr = cls.iterator();
            while (itr.hasNext()) {
                ChannelSftp.LsEntry lsn = (ChannelSftp.LsEntry) itr.next();

                System.out.println(lsn.getFilename());
            }
            InputStream obj_InputStream = obj_SFTPChannel.get(str_FileName);

            // obj_InputStream.
            Contans.create(new InputStreamReader(obj_InputStream, "UTF-8"));

            //System.out.println("finisheddddddd"+cont.size());
            /*char[] ch_Buffer = new char[0x10000];
            Reader obj_Reader = new InputStreamReader(obj_InputStream, "UTF-8");
            int int_Line = 0;
            do
            {
                int_Line = obj_Reader.read(ch_Buffer, 0, ch_Buffer.length);
                if (int_Line > 0)
                { obj_StringBuilder.append(ch_Buffer, 0, int_Line);}
            }
            while (int_Line >= 0);
            obj_Reader.close();
            obj_InputStream.close();*/
            obj_SFTPChannel.exit();
            obj_Channel.disconnect();
            obj_Session.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void WriteFileToLinux(String str_Host, String str_Username, String str_Password, int int_Port, String str_Content, String str_FileDirectory, String str_FileName) {
        JSch obj_JSch = new JSch();
        Session obj_Session = null;

        try {
            obj_Session = obj_JSch.getSession(str_Username, str_Host);
            obj_Session.setPort(int_Port);
            obj_Session.setPassword(str_Password);
            Properties obj_Properties = new Properties();
            obj_Properties.put("StrictHostKeyChecking", "no");
            obj_Session.setConfig(obj_Properties);
            obj_Session.connect();

            System.out.println("==========obj_Session===============" + obj_Session);
            Channel obj_Channel = obj_Session.openChannel("sftp");
            obj_Channel.connect();
            ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
            obj_SFTPChannel.cd(str_FileDirectory);
            InputStream obj_InputStream = new ByteArrayInputStream(str_Content.getBytes());
            obj_SFTPChannel.put(obj_InputStream, str_FileDirectory + str_FileName);
            obj_SFTPChannel.exit();
            obj_InputStream.close();
            obj_Channel.disconnect();
            obj_Session.disconnect();
        } catch (Exception ex) {
            System.out.println("=========================" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /*  public static void main(String[] arg){
          List<LogDetail> logDetails = new ArrayList<>(10);
          LogDetail ld =new LogDetail();
          ld.setRuniingTime("2019-01-14");
          ld.setServerIPAddress("192.168.11.128");
          ld.setDns("192.168.1.12");
          ld.setNetworkType("wifi");
          ld.setUrl("www.bing.com");

          logDetails.add(ld) ;
          DataSendServer sendServer = new DataSendServer();
          sendServer.setLogDetailList(logDetails);
          sendServer.setStatus("success");

          String json = Contans.toJson(sendServer);
          System.out.println(":=-=>:"+json);
          JSCHcl.WriteFileToLinux(SSH2Util.serverIp,SSH2Util.userName,SSH2Util.password,SSH2Util.PORT,json,"/root/",Contans.currentDate()+".txt");
    *//* File file = new File("C:\\Users\\selvakumar\\Desktop\\text12.txt");
    try {
        InputStream is = new FileInputStream(file.getAbsoluteFile());
        InputStreamReader inputStreamReader =new InputStreamReader(is);
        Contans.readJsonFile(inputStreamReader,LogDetail.class);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }*//*





    }*/
    public static void main(String[] arg) {
        final List<LogDetail> logDetails = new ArrayList<>(10);
        final LogDetail ld = new LogDetail();
        ld.setRuniingTime("2019-01-16");
        ld.setServerIPAddress("192.168.11.128");
        ld.setDns("192.168.1.12");
        ld.setNetworkType("wifi");
        ld.setUrl("www.bing.com");

        logDetails.add(ld);
        DataSendServer sendServer = new DataSendServer();
        sendServer.setLogDetailList(logDetails);
        sendServer.setStatus("success");

        final String json = Contans.toJson(sendServer);
        System.out.println(":=-=>:" + json);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    DataSendServer dataSendServer = new DataSendServer();
                    List<LogDetail> list = JSCHcl.readFileFromLinuxJosnList(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.PORT, "/root/", Contans.currentDate() + ".txt");
                    if (list != null && list.size() != 0) {
                        list.add(ld);
                        dataSendServer.setLogDetailList(list);
                    } else {
                        dataSendServer.setLogDetailList(logDetails);
                    }

                    dataSendServer.setStatus("");

                    String jsonStr = Contans.toJson(dataSendServer);

                    JSCHcl.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.PORT, jsonStr, "/root/", Contans.currentDate() + ".txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}

   /* public static void main(String[] arg) {
        List<LogDetail> logDetails = new ArrayList<>(10);
        LogDetail ld = new LogDetail();
        ld.setRuniingTime("2019-01-14");
        ld.setServerIPAddress("192.168.11.128");
        ld.setDns("192.168.1.12");
        ld.setNetworkType("wifi");
        ld.setUrl("www.bing.com");

        logDetails.add(ld);
        DataSendServer sendServer = new DataSendServer();
        sendServer.setLogDetailList(logDetails);
        sendServer.setStatus("success");

        final String json = Contans.toJson(sendServer);
        System.out.println(":=-=>:" + json);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {


                    JSCHcl.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.PORT, json, "/root/", "15-01-2019.txt");

                    //JSCHcl.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.PORT, json, "/root/", Contans.currentDate()+".txt");

                  //JSCHcl.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.PORT, json, "/root/", Contans.currentDate() + ".txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}*/

    /*public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    LogDetail ls=new LogDetail();
                    ls.setDns("dns ls");
                    ls.setNetworkType("networktype");
                    ls.setRuniingTime("running time");
                    ls.setServerIPAddress("serveripaddress");

                    LogDetail ls1=new LogDetail();
                    ls1.setDns("dns ls");
                    ls1.setNetworkType("networktype");
                    ls1.setRuniingTime("running time");
                    ls1.setServerIPAddress("serveripaddress");
                    String json= Contans.toJson(ls);
                    String json1= Contans.toJson(ls1);
                    //System.out.println("===========json=============="+json);

                    JSCHcl m=new JSCHcl();


                    *//*String FILE_PATH = "/root/";
                    String FILE_EXTENSION = ".txt";
                    DateFormat df = new SimpleDateFormat("yyyyMMddhh"); // add S if you need milliseconds
                    String filename = FILE_PATH + df.format(new Date()) + "." + FILE_EXTENSION;*//*
// filename = "D:\Report20150915152301.pdf"



                String readFileFromLinux= m.JsonReader(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.int_SSHPort,"/root/","2019-01-14.txt" );
                    m.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.int_SSHPort,json+","+json1,"/root/","2019-01-14.txt" );
                 *//* if(m==null)
                  {
                      System.out.println("check out");
                  }
                  else
                  {
                      System.out.println("its not a null"+readFileFromLinux);
                      m.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.int_SSHPort,json.toString(),"/root/","str_FileName" );
                  }*//*
                *//*
                    if(readFileFromLinux==null)
                    {
                        System.out.println("is a null");
                    }
                    else
                    {
                        System.out.println("its not a null"+readFileFromLinux);
                        m.WriteFileToLinux(SSH2Util.serverIp, SSH2Util.userName, SSH2Util.password, SSH2Util.int_SSHPort,json.toString(),"/root/","2019-01-005.txt" );
                    }*//*
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        System.out.println("==============exit===========");
    }
}*/