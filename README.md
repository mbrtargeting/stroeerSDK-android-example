# Stroeer SDK – Ad Integration Examples for Android

## 📖 **Integration Guide:**  
For detailed instructions on integrating Stroeer SDK into your Android application, refer to the official documentation:  
[Android Integration Documentation](https://stroeerdigitalgroup.atlassian.net/wiki/spaces/SDGPUBLIC/pages/1890648275/Android+integration+documentation)

## Release Notes - v8.0.2 (5.Feb.2026)

## 🐞 Bug Fixes
-	Fixed an issue where configuration loading could fail permanently due to unstable or lost network connections.

## 🛠 Improvements
- Improved configuration download reliability by storing and reusing the last successfully downloaded configuration until a new one is available.
- Added retry logic for configuration downloads when network failures occur.
- Removed loading timeout to allow publisher-controlled banner loading
- Added support for enabling inspection mode via adb shell setprop log.tag.StroeerSDK V


## Release Notes - v8.0.1 (5.Jan.2026)
## 🐞 Bug Fixes
- Fixed the issue that the SDK may crash when the activity context is destroyed.
## 🛠 Improvements
- Removed the adex call.
- Improved the performance of okhttpRequest.


## Release Notes – v8.0.0 (29.Nov.2025)
## 🚀 New Features
- Added new debug information.
- Simplified the implementation for banner, interstitial, and rewarded formats. 
- The class name is updated. (Yieldlove -> Stroeer). Please make sure to update your imports accordingly.
## 🛠 Improvements
- Updated third-party libraries:
    - CMP: 7.15.9
    - Gravite: 3.14.3
    - Confiant: 6.1.3
    - Google Mobile Ads SDK: 24.6.0


## Release Notes – v7.2.2
**Release Date:** 2025-09-03


## 🚀 New Features


## 🛠 Improvements


## 🐞 Bug Fixes
- Fixed an issue where the application triggers an ANR (Android Not Responding) state when it crashed.

---

## Release Notes – v7.2.1
**Release Date:** 2025-09-01


## 🚀 New Features
 

## 🛠 Improvements


## 🐞 Bug Fixes
- Fixed a memory leak in the Google SDK integration.
- Fixed an issue where the SDK could freeze under certain conditions


---

## Release Notes – v7.2.0
**Release Date:** 2025-08-25


## 🚀 New Features
- Updated Kotlin to 2.1.0 (required for Google Mobile Ads SDK 24).
- Updated Android Target SDK to 35.
- Updated Gradle to 8.7.0 and Android Gradle Plugin to 8.6.0.
- Library updates:
	- Google Mobile Ads SDK 24.5.0
	- Gravite 3.13.6
    - SourcePoint 7.11.2
	- Prebid 2.5.0

## 🛠 Improvements
- General memory management improvements.
- Reduced the risk of leaks when handling ads.

## 🐞 Bug Fixes

## 📌 Notes
- A minor memory leak related to the Google Mobile Ads SDK has been identified.
Investigation is in progress, and a fix will be included in an upcoming release.
---

## Release Notes – v7.1.0
**Release Date:** 2025-08-14


## 🚀 New Features
- Added **Reload Banner**, **Interstitial**, **Consent**, **Privacy**, and **Reset Consent** buttons.

## 🛠 Improvements
- Integrated **banner** and **interstitial** ad examples into the sample app.
- Fixed various UI issues for smoother user experience.

## 🐞 Bug Fixes
- Resolved an issue where the banner `destroy()` method was not being called in the application.

## 📌 Notes
- Starting from this release, the **example application version** will match the **SDK version**.
- A **minor memory leak** in the SDK has been identified and will be fixed in **v7.2.0** (scheduled for release by the end of August 2025).

---
