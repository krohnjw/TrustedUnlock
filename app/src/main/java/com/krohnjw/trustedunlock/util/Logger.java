package com.krohnjw.trustedunlock.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class Logger {
    public static final String  DIR_NAME = "TrustedUnlock";
    public static final String  FILE_NAME = "debug.txt";
    
    private static final String TAG = "TrustedUnlock";
    
    public static void i(String Message) {
        Logger.i(TAG, Message);
    }

    public static void i(String TAG, String Message) {
        Log.i(TAG, Message);
        writeFile(Message);
    }

    public static void e(String Message) {
        Logger.e(TAG, Message);
    }

    public static void e(String Message, Exception e) {
        Logger.e(TAG, Message, e);
    }
    
    public static void e(String TAG, String Message, Exception e) {
        e.printStackTrace();
        
        if (true) {
            Log.e(TAG, Message);
            writeFile(e.getMessage());
        }
    }

    public static void e(String TAG, String Message) {
        
        if (true) {
            Log.e(TAG, Message);
            writeFile(Message);
        }
    }

    public static void d(String Message) {
        Logger.d(TAG, Message);
    }

    public static void d(String TAG, String Message) {
        // Only log debug if we have debug enabled
        if (true) {
            Log.d(TAG, Message);
            writeFile(Message);
        }
    }

    private static class fileWriter extends AsyncTask<String, Void, Void> {
        
        private boolean mEncode = false;;
        private boolean mStamp = true;
        private boolean mAppendFile = true;
        
        public fileWriter(boolean encode, boolean timestamp, boolean append) {
            this.mEncode = encode;
            this.mStamp = timestamp;
            this.mAppendFile = append;
        }
        
        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(String... params) {
            String message = params[0];
            if (this.mEncode) {
                message = Base64.encodeToString(message.getBytes(), Base64.DEFAULT);
            }
            String title = params[1];
            
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                try {
                    File container = new File(root.getPath() + "/" + DIR_NAME + "/");
                    container.mkdirs();
                    File log = new File(container, title);
                    FileWriter writer = new FileWriter(log, mAppendFile);
                    BufferedWriter out = new BufferedWriter(writer);
                    Date now = new Date();
                    if (this.mStamp) {
                        out.write(now.toLocaleString() + ": " + message + "\n");
                    } else {
                        out.write(message + "\n");
                    }
                    out.flush();
                    writer.flush();
                    out.close();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    public static void writeFile(String message, String title, boolean stamp, boolean encode, boolean append) {
        new Logger.fileWriter(encode, stamp, append).execute(message, title);
    }
    private static void writeFile(String message) {
        new Logger.fileWriter(false, true, true).execute(message, FILE_NAME);
    }
}
