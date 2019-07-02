package moffice.meta.com.molibrary.utility;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by com.moffice.com.microoffice.app on 01/27/2017.
 */

public class LogWriter {
    public static void writeLog(String content, String event)
    {

        try {

            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/MicroOff" + "/Log";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/MicroOff" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/MicroOff" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLogDevMesg(String content, String event)
    {
        try {

            String filePath = "";
            File file = null;
            try {
                if(CommonDataArea.getCommonSettings().getAdminEmail().contains("bmjo"))
                {

                        filePath = Environment.getExternalStorageDirectory()
                                + "/MicroOff" + "/DevMesg";
                        if (!(new File(Environment.getExternalStorageDirectory()
                                + "/MicroOff" + "/")).exists()) {
                            (new File(Environment.getExternalStorageDirectory()
                                    + "/MicroOff" + "/")).mkdirs();
                        }
                        file = new File(filePath);
                    if(!file.exists())
                        file.createNewFile();
                }
            } catch (Exception e) {
            }
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/MicroOff" + "/DevMesg";

                file = new File(filePath);


            } catch (Exception e) {
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dateFormat.format(date) + " : " + content + " : " +event
                    + "\r\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLogException(String content, Exception exp)
    {
        try {
            String filePath = "";
            File file = null;
            try {
                filePath = Environment.getExternalStorageDirectory()
                        + "/MicroOff" + "/Exceptionlog";
                if (!(new File(Environment.getExternalStorageDirectory()
                        + "/MicroOff" + "/")).exists()) {
                    (new File(Environment.getExternalStorageDirectory()
                            + "/MicroOff" + "/")).mkdirs();
                }
                file = new File(filePath);

            } catch (Exception e) {
            }


            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            report.append("----------Exception Handler collected Report--------------------------\r\n");
            report.append("\r\nError Report collected on : ");
            report.append("\r\nError Report collected on : ").append(curDate.toString()).append('\r').append('\n');
            report.append("\r\nInformations :").append('\n');
            report.append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            exp.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\r\n\r\n"+dateFormat.format(date) + " : " + content + "\r\n\r\n"+ report.toString() );
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
