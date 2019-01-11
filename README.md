# TRAVEL TOGETHER - TRAITORS
- An android application helps foreigners to understand banners, signs written in Vietnamese, know how to arrange an effective and enjoyable trip, give information about the place...
- The app will have these key features:
  1) Landmark recognition: user can take a photo of a famous place in Vietnam, outside or just in the brochure. The photo is sent to the server, and the landmark recognition code (using machine learning method) runs. Details about the place will be displayed to the user.    We decided to build the server by Django.
  2) Text recognition on banners or signs: this feature helps the user to understand the text written in Vietnamese. We decided to use Tesseract OCR.
  3) Schedule for a trip: this feature helps the user to store all the expected places to go, including the information of these places such as address, work hours, telephone number, images, and reviews.

## Prerequisite
  1) Android OS 5.0 (API 21) or higher
  2) Install Android Studio
  3) Using Linux environment to run Django server
  4) Google account to setup Firebase
## Setup

### Firebase

- build.gradle
```
implementation 'com.google.firebase:firebase-database:16.0.3'
implementation 'com.google.firebase:firebase-core:16.0.4'
implementation 'com.google.firebase:firebase-storage:16.0.3'
implementation 'com.google.firebase:firebase-auth:16.0.4'
implementation 'com.google.firebase:firebase-messaging:17.3.4'
implementation 'com.firebaseui:firebase-ui:0.6.2'
 ```
 
### Django server
- link: https://drive.google.com/drive/folders/15uQsMTcluuR54mwRgKEKJn4Y1jHsdip0?usp=sharing

### Landmark recognition (data + source code)
- Use SVM to classify 20 places in Vietnam
- link: https://drive.google.com/drive/folders/1Covx25GHrjebKErLCLAuSeSEkMUkSCm1?usp=sharing 

## Added features:  
- Choose image from device and upload to server
- Dislay place's infomation
- Create plan list, add place to plan, join plan's group chat, create note in plan
- Landmark recognition to classify 20 places in Vietnam
- Tesseract OCR for translating Vietnamese to English

## Dependencies:  
- Volley  
```
implementation 'com.android.volley:volley:1.1.1'
```
  
- Expandable recylerview
[Visit here](https://github.com/thoughtbot/expandable-recycler-view)  
```
implementation 'com.thoughtbot:expandablerecyclerview:1.3'
```
```
implementation 'com.thoughtbot:expandablecheckrecyclerview:1.4'
```

- ChatView
[Visit here](https://github.com/shrikanth7698/ChatView)
```
implementation 'com.github.shrikanth7698:ChatView:v0.1.2'
```

- Picasso
[Visit here](https://github.com/square/picasso)
```
implementation 'com.squareup.picasso:picasso:2.71828'
```

- CircleImageView
[Visit here](https://github.com/hdodenhof/CircleImageView)
```
implementation 'de.hdodenhof:circleimageview:2.2.0'
```

- Floating Action Button Speed Dial
[Visit here](https://github.com/leinardi/FloatingActionButtonSpeedDial)
```
implementation 'com.leinardi.android:speed-dial:2.0.0'
```

- tess-two
[Visit here](https://github.com/rmtheis/tess-two)
```
implementation 'com.rmtheis:tess-two:9.0.0'
```

## Demo
- link: https://drive.google.com/file/d/1fVCtW4NziRGm9pgkqQcW_wf9urybtCSE/view?usp=sharing
