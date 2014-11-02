package com.neo.crashhandlersample;

import android.app.Application;

/**
 * Created by Neo on 14/11/2. Copyright (c) 2014 Neo Hsu. All rights reserved.
 */
public class CrashApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
  }
}

