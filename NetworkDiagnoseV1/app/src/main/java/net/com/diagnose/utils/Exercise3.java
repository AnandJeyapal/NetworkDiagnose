package net.com.diagnose.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exercise3 {
    public static void main(String[] args) {
        boolean bool = false;
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        // Create a File object
        File my_file_dir = new File("C://Users//dateFormat.txt");
        if (my_file_dir.exists())
        {
            System.out.println("The directory or file exists.\n");
        }
        else
        {
            System.out.println("The directory or file does not exist.\n");
        }
    }
}