package com.krohnjw.trustedunlock.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class Root {

    /**
     * Tries to detect the presence of the su binary or a SuperUser APK
     * @return
     */
    public static boolean isRootPresent() {
        Logger.d("Checking root");
        try {

            File file = new File("/system/app/Superuser.apk");
            File file2 = new File("/system/app/SuperSU.apk");
            if (file.exists() || file2.exists()) {
                Logger.d("Superuser APK found");
                return true;
            }
        } catch (Exception e) {
            Logger.e("NFCT", "Exception thrown checking for SU APK", e);
        }

        try {
            String binaryName = "su";
            String[] places = { "/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/" };
            for (String where : places) {
                File file = new File(where + binaryName);
                if (file.exists()) {
                    Logger.d(binaryName + " was found here: " + where);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.e("NFCT", "Exception locating su binary", e);
        }

        return false;   
    }

    /**
     * Runs a specific command using su
     * @param command
     * @throws IOException
     */
    public static String runCommandAsRoot(String... commands) throws IOException, InterruptedException {
        String results = "";
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream STDIN = new DataOutputStream(process.getOutputStream());
            BufferedReader STDOUT = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader STDERR = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Write all commands
            for (int i=0; i < commands.length; i++) {
                Logger.d("Adding command " + commands[i]);
                STDIN.writeBytes(commands[i] + "\n");
                STDIN.flush();
            }
            STDIN.writeBytes("exit\n");
            STDIN.flush();
            Logger.d("Waiting");
            process.waitFor();
            Logger.d("Done");
            if (process.exitValue() == 255) {
                Logger.d("su exited with 255");
                return null; // su denied
            }

            StringBuilder output = new StringBuilder();
            while (STDOUT.ready()) {
                String read = STDOUT.readLine();
                output.append(read);
                Logger.d("Output:" + read);
            }

            while (STDERR.ready()) {
                String read = STDERR.readLine();
                output.append(read);
                Logger.d("Error:" + read);
            }
            results = output.toString();
            process.destroy();
        } catch (IOException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        }
        return results;
    }
}
