package com.astudio.progressmonitor.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.astudio.progressmonitor.support.ErrorMessage;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FileHandler {

    private static final String TAG = "FileHandler";
    private static final String PROMO_DIR = "promo_files";
    private File internalStorageDir;

    public FileHandler(File filesDir) {
        internalStorageDir = filesDir;
    }


    //PromoPresenter - createPromo
    public File convertBitmapToFile(Bitmap bitmap) throws IOException {
        File targetFile = new File(internalStorageDir + File.separator + "temp_promo_image");
        boolean result = targetFile.createNewFile(); // return false when file exists
//        if (!result){
//            FirebaseCrashlytics.getInstance().recordException(new Throwable("convertBitmapToFile-createNewFile-false"));
//            return null;
//        }
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , byteArrayOS); // YOU can also save it in JPEG
        byte[] bitMapData = byteArrayOS.toByteArray();

        FileOutputStream fOut;
        fOut = new FileOutputStream(targetFile);
        // Write a line to the file
        fOut.write(bitMapData);
        fOut.flush();
        // Close the file output stream
        fOut.close();
        //Log.e(TAG, "convertBitmapToFile OK");
        return targetFile;
    }


    // SyncServicePromo - resultGetPromoImage
    public void convertBitmapToJPEG(Bitmap bitmap, String fileName) throws IOException{
        File folder = new File(internalStorageDir + File.separator + PROMO_DIR);
        if(!folder.exists()){
            boolean mkdir = folder.mkdir();
            if (!mkdir) {
                //FirebaseCrashlytics.getInstance().recordException(new Exception("FileHandler-"));
                FirebaseCrashlytics.getInstance().recordException(new Throwable());
                return;
            }
        }
        File targetFile = new File(internalStorageDir + File.separator + PROMO_DIR + File.separator + fileName);
        boolean result = targetFile.createNewFile();
        if (!result) {
            FirebaseCrashlytics.getInstance().recordException(new Throwable());
            return;
        }
        int screenDeviceWidth = App.getInstance().screenDeviceWidth;
        //Log.e(TAG, "device width: " + screenDeviceWidth);
        bitmap = setWidthBitmap(bitmap, screenDeviceWidth, true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        //FileInputStream fileInputStream = null;
        //File file = new File("yourfile");
        //file.createNewFile();
        //byteArray = new byte[(int) file.length()];

        //convert file into array of bytes
//        fileInputStream = new FileInputStream(file);
//        fileInputStream.read(byteArray);
//        fileInputStream.close();

        //convert array of bytes into file
        FileOutputStream fileOutputStream =
                new FileOutputStream(targetFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    private Bitmap setWidthBitmap(Bitmap sourceImage, float requiredWidth, boolean filter) {
        float sourceWidth = sourceImage.getWidth();
        float sourceHeight = sourceImage.getHeight();
        float ratioBitmap = sourceWidth / sourceHeight;
        float newHeight = requiredWidth / ratioBitmap;
        int width = Math.round(requiredWidth);
        int height = Math.round(newHeight);
        return Bitmap.createScaledBitmap(sourceImage, width, height, filter);
    }


    // TaskDescriptionPresenter
    public List<String> getSortedFiles(String path){
        File dir = new File(path);
        List<File> files = new ArrayList<>();
        List<String> names = new ArrayList<>();
        if(dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            //Log.e(TAG, "source files: " + Arrays.toString(listFiles));
            for(File file : Objects.requireNonNull(listFiles)){
                if(file.isFile()) {
                    if (files.isEmpty()){
                        files.add(file);
                    } else {
                        Date freshestFileDate = new Date(files.get(0).lastModified());
                        Date currentFileDate = new Date(file.lastModified());
                        if (compareModifiedFiles(freshestFileDate, currentFileDate)) {
                            files.add(0, file);
                        } else {
                            files.add(file);
                        }
                    }
                }
            }
            //Log.e(TAG, "target files: " + files);
            for (File file: files){
                names.add(file.getName());
            }
        }
        return names;
    }


    private boolean compareModifiedFiles(Date freshestFile, Date currentFile){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(freshestFile);
        cal2.setTime(currentFile);
        if (currentFile.after(freshestFile)) {
            return true;
        } else {
            return false;
        }
    }


    // SyncServicePromo
    public boolean checkExistsFile(String fileName) {
        File file = new File(App.getInstance().getInternalDir() + File.separator + PROMO_DIR + File.separator + fileName);
        return file.exists();
    }


    // SyncServicePromo
    public String[] getFileNames(String directoryPath) {
        File dir = new File(directoryPath);
        Collection<String> files = new ArrayList<String>();
        //List<String> files = new ArrayList<String>();
        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();
            for(File file : Objects.requireNonNull(listFiles)){
                if(file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files.toArray(new String[]{});
    }


    //NOT USED
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "dir");
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
