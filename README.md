General Android Crash Handler Sample
===

[![Build Status](https://travis-ci.org/NeoHsu/Crash-Handler-Sample.svg?branch=master)](https://travis-ci.org/neohsu/Crash-Handler-Sample)

此為自定義處理 Android App Crash Log 的範例

### 處理 Crash Log 的步驟
1. Overload Application 類別擷取發生 Crash 的例外事件訊息
2. 建立處理 Crash 機制，通常會建立發生 Crash 顯示提示訊息並於幾秒後自行關閉程式並存取當前資訊
3. 存取 Crash 訊息到檔案中，並看後續是否規劃傳送到自己的 Server 中做記錄

> 可以利用此方式去關閉一些可能因為 Crash 而來不及停止的背景服務
