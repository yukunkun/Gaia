package com.gaiamount.util;

import android.content.Context;
import android.support.annotation.MainThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by haiyang-lu on 16-4-7.
 * 文件与对象的转化等操作的工具类
 */
public class FileUtils {
    public static void writeObjectToFile(Object obj,String fileName)
    {
        File file =new File(fileName+".dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String fileName)
    {
        Object temp=null;
        File file =new File(fileName+".dat");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    @MainThread
    public static void deleteFile(File file, Context context) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();

            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i],context);
                }
            }
            file.delete();
            LogUtil.e(FileUtils.class,"删除成功");
        } else {
            LogUtil.e(FileUtils.class,"文件不存在");
        }
    }

}
