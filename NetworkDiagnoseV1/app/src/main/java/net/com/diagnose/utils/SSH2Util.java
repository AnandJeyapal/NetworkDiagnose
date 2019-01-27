package net.com.diagnose.utils;


import java.io.File;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;

public class SSH2Util {



    //远端服务器IP地址
    public static String serverIp="172.18.17.40";

    //远端服务器登录用户名
    public static String userName="root";

    //远端服务器登录密码
    public static String password="infotop.123";

    public static int PORT = 22;

    //public static int int_SSHPort = 22;
    //远端服务器的基准目录
    private String baseDir;

    //连接对象
    private Connection connection;



    public boolean connectAndAuth()
    {
        boolean isConnAndAuth = false;
        if (null != serverIp)
        {

            connection = new Connection(serverIp);
            try {
                connection.connect();
                isConnAndAuth = connection.authenticateWithPassword(userName,password);
            } catch (Exception e) {
                System.out.println("============>IP:" + serverIp + ",用户名:userName[i]，请检查IP，用户名或者密码是否正确!");
                e.printStackTrace();
                //如果存在主机连接或者认证失败，那么设置连接是否的标识为false
                isConnAndAuth = false;
            }

        }
        System.out.println("=====isConnAndAuth=======>IP:"+isConnAndAuth);
        return isConnAndAuth;
    }

    public void uploadFile(String localFile,String remoteTargetDirectory,String fileName,String json)
    {
        mkDir(remoteTargetDirectory,4096);
        if (connectAndAuth())
        {
            System.out.println("+_222222222222__+_+++");
            Connection conn = connection ;

            try
            {       File file = new File(localFile);

                SCPClient scpClient = conn.createSCPClient();
               // scpClient.put(fileName,json.getBytes().length,remoteTargetDirectory,"0644");
               // scpClient.put(json.getBytes(),json.getBytes().length,remoteTargetDirectory,"0644");
                System.out.println("+___+_+++");
            } catch (Exception e)
            {
                System.out.println("获取ssh上传客户端失败!"+e.getMessage());
                e.printStackTrace();
            }

            closeConnection();
            System.out.println("+_completed__+_+++");
        }
    }

    public void mkDir(String dirName,int posixPermissions)
    {
        if (connectAndAuth())
        {
            System.out.println("************开始创建目录:" + dirName + "************");
            Connection conn = connection;

            try
            {
                SFTPv3Client sftpClient = new SFTPv3Client(conn);
                sftpClient.mkdir(dirName, posixPermissions);
            } catch (Exception e)
            {
                System.out.println("************目录:" + dirName + "已经存在!************");
                e.printStackTrace();
            }


            closeConnection();
        }
    }

    public void closeConnection()
    {
        if (null != connection )
        {
            Connection conn = connection;
            conn.close();

        }
    }
}
