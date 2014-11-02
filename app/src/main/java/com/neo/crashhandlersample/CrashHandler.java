package com.neo.crashhandlersample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Neo on 14/11/2. Copyright (c) 2014 Neo Hsu. All rights reserved.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

  public static final String TAG = "CrashHandler";

  // 系統默認的 UncaughtException 處理類別
  private Thread.UncaughtExceptionHandler mDefaultHandler;

  // CrashHandler 實例
  private static CrashHandler INSTANCE = new CrashHandler();

  private Context mContext;

  // 用來儲存設備資訊和異常訊息
  private Map<String, String> infos = new HashMap<String, String>();

  // 格式化日期
  private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  // 保證只有一個 CrashHandler 實例
  private CrashHandler() {
  }

  // 取得 CrashHandler 實例，單例模式
  public static CrashHandler getInstance() {
    return INSTANCE;
  }

  private static LogToJson logToJson = new LogToJson();

  // 初始化
  public void init(Context context) {
    mContext = context;
    // 取得系統默認的 UncaughtException 處理器
    mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    // 設定 CrashHandler 為程式的默認處理器
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  // 當 UncaughtException 發生時會調用該函式處理（重構 UncaughtExceptionHandler 內的 uncaughtException 方法）
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    if (!handleException(ex) && mDefaultHandler != null) {
      // 没有處理則讓系統默認的異常處理器來處理
      mDefaultHandler.uncaughtException(thread, ex);
    } else {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        Log.e(TAG, "error : ", e);
      }
      // 退出程序
      android.os.Process.killProcess(android.os.Process.myPid());
      System.exit(1);
    }
  }

  /**
   * 自定義錯誤處理方式，用來處理收集錯誤訊息的操作並存入檔案中
   *
   * @return 如果處理了錯誤訊息回傳 True 否则返回 False
   */
  private boolean handleException(Throwable ex) {
    if (ex == null) {
      return false;
    }
    // 使用 Toast 來顯示程式發生異常的提示訊息
    new Thread() {
      @Override
      public void run() {
        Looper.prepare();
        Toast.makeText(mContext, "程式出現異常即將退出", Toast.LENGTH_LONG).show();
        Looper.loop();
      }
    }.start();
    logToJson.setTime(String.valueOf(System.currentTimeMillis()));
    logToJson.setStatus("Crash");
    logToJson.setType("Crash");
    logToJson.setOS("Android");
    // 收集裝置資訊
    collectDeviceInfo(mContext);
    // 存取資訊到檔案中
    saveCrashInfo2File(ex);
    return true;
  }

  /**
   * 收集裝置訊息
   */
  public void collectDeviceInfo(Context ctx) {
    StringBuffer sb = new StringBuffer();
    try {
      PackageManager pm = ctx.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
      if (pi != null) {

        String versionName = pi.versionName == null ? "null" : pi.versionName;
        String versionCode = pi.versionCode + "";
        logToJson.setVersion(versionCode);
      }
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, "an error occured when collect package info", e);
    }
    Field[] fields = Build.class.getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        infos.put(field.getName(), field.get(null).toString());
        sb.append(field.getName() + " = " + field.get(null).toString() + "\n");
      } catch (Exception e) {
        Log.e(TAG, "an error occured when collect crash info", e);
      }
    }
    logToJson.getLogDetail().setLogDeviceInfo(sb.toString());
  }

  /**
   * 存取錯誤訊息到檔案中
   *
   * @return 回傳檔名
   */
  private String saveCrashInfo2File(Throwable ex) {

    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, String> entry : infos.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key + "=" + value + "\n");
    }

    Writer writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    ex.printStackTrace(printWriter);
    Throwable cause = ex.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }
    printWriter.close();
    String result = writer.toString();
    logToJson.getLogDetail().setLogException(result);
    sb.append(result);

    try {
      long timestamp = System.currentTimeMillis();
      String time = formatter.format(new Date());
      String fileName = "crash-" + time + "-" + timestamp + ".json";
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        String path = "/sdcard/crash/";
        File dir = new File(path);
        if (!dir.exists()) {
          dir.mkdirs();
        }

        File inFile = new File(dir, "setting.json");
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        try {
          reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
          String line;
          while ((line = reader.readLine()) != null) {
            data.append(line);
          }
        } catch (Exception e) {
        } finally {
          try {
            reader.close();
          } catch (Exception e) {
          }
        }
        JSONObject jObject = new JSONObject(data.toString());
        logToJson.setAccessToken(jObject.getString("AccessTokenID").toString());
        logToJson.getLogDetail().setLogDeviceToken((jObject.getString("DeviceTokenID").toString()));
        logToJson.setUserId(jObject.getString("UserID").toString());

        StringBuffer context = new StringBuffer();
        context.append(toJSon(logToJson).toString());
        FileOutputStream fos = new FileOutputStream(path + fileName);
        fos.write(context.toString().getBytes());
        fos.close();
      }
      return fileName;
    } catch (Exception e) {
      Log.e(TAG, "an error occured while writing file...", e);
    }
    return null;
  }

  public static String toJSon(LogToJson mLogToJson) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Id", mLogToJson.getId());
      jsonObj.put("OS", mLogToJson.getOS());
      jsonObj.put("Type", mLogToJson.getType());
      jsonObj.put("Status", mLogToJson.getStatus());
      jsonObj.put("Time", mLogToJson.getTime());
      jsonObj.put("Version", mLogToJson.getVersion());
      jsonObj.put("UserId", mLogToJson.getUserId());
      jsonObj.put("AccessToken", mLogToJson.getAccessToken());
      jsonObj.put("Remark", mLogToJson.getRemark());

      JSONObject jsonLog = new JSONObject();
      jsonLog.put("DeviceInfo", mLogToJson.getLogDetail().getLogDeviceInfo());
      jsonLog.put("DeviceToken", mLogToJson.getLogDetail().getLogDeviceToken());
      jsonLog.put("ExecutionTime", mLogToJson.getLogDetail().getLogExecutionTime());
      jsonLog.put("Url", mLogToJson.getLogDetail().getLogUrl());
      jsonLog.put("Type", mLogToJson.getLogDetail().getLogType());
      jsonLog.put("Parameter", mLogToJson.getLogDetail().getLogParameter());
      jsonLog.put("Response", mLogToJson.getLogDetail().getLogResponse());
      jsonLog.put("Exception", mLogToJson.getLogDetail().getLogException());
      jsonLog.put("ResponseCode", mLogToJson.getLogDetail().getLogResponseCode());

      jsonObj.put("Log", jsonLog);

      return jsonObj.toString();
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}