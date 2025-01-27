# 스마트홈 안드로이드 앱 만들기 노트


안드로이드 스튜디오에서 ‘Empty Views Acitivity’ 로 새 프로젝트 생성



AndroidManifest.xml 설정파일 수정

아래의 코드 추가 (앱이 인터넷 접속을 할 수 있도록 허용하는 코드)

<uses-permission android:name="android.permission.INTERNET" />


아래는 위의 코드들을 추가한 최종 예시

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.SmartHome"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>




settings.gradle.kts 파일 수정
그래프 그리는 라이브러리의 외부 참조를 가능하게 하는 코드



아래의 코드 라인 추가

 maven { url = uri("https://jitpack.io") }  // Use `uri` for the URL
 
 maven { url = uri("https://jitpack.io") }  // Use `uri` for the URL


아래는 위의 코드들을 추가한 최종 파일의 예시

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }  // Use `uri` for the URL

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }  // Use `uri` for the URL
    }
}

rootProject.name = "smartHome"
include(":app")
 




build.gradle.kts(:app) 파일 수정


필요라이브러리들을 추가하는 파일



아래의 코드들을 추가

 implementation(libs.retrofit)
 implementation(libs.jsoup)
 implementation(libs.gson.converter)
 implementation(libs.okhttp.logging)
 
 implementation(libs.retrofit2.scalars)
 
 implementation(libs.mpandroidchart)


아래는 위의 코드들을 추가한 최종 파일의 예시

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smarthome"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smarthome"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.jsoup)
    implementation(libs.gson.converter)
    implementation(libs.okhttp.logging)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit2.scalars)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // MPAndroidChart
    implementation(libs.mpandroidchart)

}




libs.versions.toml 파일 수정


아래 라인들을 추가

retrofit = "2.9.0"  # Added Retrofit version
gson = "2.9.0"      # Added Gson converter version
okhttp_logging = "4.9.0"  # Added OkHttp logging interceptor version
jsoup = "1.15.3"
converter_scalars = "2.9.0"
mpandroidchart = "3.1.0"



retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }  # Added Retrofit library
gson-converter = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "gson" }  # Added Gson converter
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp_logging" }  # Added OkHttp logging interceptor
jsoup = { group = "org.jsoup", name = "jsoup", version.ref = "jsoup"}
retrofit2-scalars = { group = "com.squareup.retrofit2", name = "converter-scalars", version.ref = "converter_scalars"}
mpandroidchart = { group = "com.github.PhilJay", name = "MPAndroidChart", version.ref = "mpandroidchart" }


아래는 최종 참고 코드

[versions]
agp = "8.5.1"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
appcompat = "1.6.1"
material = "1.10.0"
activity = "1.9.1"
constraintlayout = "2.1.4"
retrofit = "2.9.0"  # Added Retrofit version
gson = "2.9.0"      # Added Gson converter version
okhttp_logging = "4.9.0"  # Added OkHttp logging interceptor version
jsoup = "1.15.3"
converter_scalars = "2.9.0"
mpandroidchart = "3.1.0"

[libraries]
junit = { group = "junit", name = "junit", version.ref = "junit" }
ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
activity = { group = "androidx.activity", name = "activity", version.ref = "activity" }
constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }  # Added Retrofit library
gson-converter = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "gson" }  # Added Gson converter
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp_logging" }  # Added OkHttp logging interceptor
jsoup = { group = "org.jsoup", name = "jsoup", version.ref = "jsoup"}
retrofit2-scalars = { group = "com.squareup.retrofit2", name = "converter-scalars", version.ref = "converter_scalars"}
mpandroidchart = { group = "com.github.PhilJay", name = "MPAndroidChart", version.ref = "mpandroidchart" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }




안드로이드 앱 시작시 보이는 메인 화면코드(res/activity_main.xml)

acitivity_main.xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:chart="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#f4f7fc"
    android:padding="16dp">

    <!-- Title / Header -->
    <TextView
        android:id="@+id/titleText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="스마트 홈 컨트롤 프로그램"
        android:textSize="24sp"
        android:textStyle="bold"
        android:textColor="#333"
        android:gravity="center"
        android:padding="8dp" />

    <!-- Chart Container -->
    <com.github.mikephil.charting.charts.LineChart
        android:id="@+id/lineChart"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:layout_margin="8dp" />

    <!-- Actuator Toggles -->
    <LinearLayout
        android:id="@+id/toggleLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center"
        android:layout_marginTop="8dp">

        <ToggleButton
            android:id="@+id/toggleAC"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textOff="에어컨 \nON/OFF"
            android:textOn="에어컨 \nON/OFF" />

        <ToggleButton
            android:id="@+id/toggleLight"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textOff="거실등 \nON/OFF"
            android:textOn="거실등 \nON/OFF"
            android:layout_marginStart="10dp"
            />

        <ToggleButton
            android:id="@+id/toggleLock"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textOff="문 잠금\n/잠금 해제"
            android:textOn="문 잠금\n/잠금 해제"
            android:layout_marginStart="10dp"
            />

        <ToggleButton
            android:id="@+id/toggleBoiler"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textOff="보일러 \nON/OFF"
            android:textOn="보일러 \nON/OFF"
            android:layout_marginStart="10dp"
            />
    </LinearLayout>

    <LinearLayout
        android:id="@+id/LightLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center"
        android:layout_marginTop="8dp">
        <Button
            android:id="@+id/buttonNormal"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="노멀 모드"
            android:layout_marginStart="10dp"
            />
        <Button
            android:id="@+id/buttonCinema"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="시네마 모드"
            android:layout_marginStart="10dp"
            />
        <Button
            android:id="@+id/buttonMilitary"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="군대 모드"
            android:layout_marginStart="10dp"
            />



    </LinearLayout>
    <TextView
        android:id="@+id/temp_label"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Temperature (°C)"
        android:textSize="16sp" />

    <SeekBar
        android:id="@+id/tempSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="40"
        android:progress="25"
        android:progressDrawable="@drawable/seekbar_progress" />


    <!-- Humidity SeekBar -->
    <TextView
        android:id="@+id/humidity_label"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Humidity (%)"
        android:textSize="16sp" />

    <SeekBar
        android:id="@+id/humSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="100"
        android:progress="50"
        android:progressDrawable="@drawable/seekbar_progress" />

    <TextView
        android:id="@+id/bright_label"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Brightness (%)"
        android:textSize="16sp" />

    <SeekBar
        android:id="@+id/brightSeekBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="100"
        android:progress="100"
        android:progressDrawable="@drawable/seekbar_progress" />


    <!-- Current Status Panel -->
    <TextView
        android:id="@+id/statusTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="현재 상태"
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="#555"
        android:layout_marginTop="16dp"
        />

    <LinearLayout
        android:id="@+id/statusLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginTop="8dp">

        <TextView
            android:id="@+id/statusAC"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="에어컨: OFF" />

        <TextView
            android:id="@+id/statusLight"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="거실등: OFF" />

        <TextView
            android:id="@+id/statusLock"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="현관문: LOCKED" />

        <TextView
            android:id="@+id/statusBoiler"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="보일러: OFF" />

    </LinearLayout>

    <!-- Footer -->
    <TextView
        android:id="@+id/footerText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="© 2025 Smart Environment Control System"
        android:textColor="#FFF"
        android:background="#343a40"
        android:gravity="center"
        android:padding="16dp"
        android:layout_marginTop="16dp"/>
</LinearLayout>


위파일에서 사용된 핵심 위젯:


ToggleButton (원래는 on/off 상태를 반영해서 보여주는 버튼이지만, 일반버튼처럼 사용하였음)

button (일반 버튼)

seekbar (좌우로 드래그 해서 값 조정 하는 위젯)

com.github.mikephil.charting.charts.LineChart





res/drawable 폴더에 seekbar_progress.xml 파일 추가 한 후 아래 코드 추가

seekbar를 구현하는 코드

<!-- res/drawable/seekbar_progress.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#FF6347"/> <!-- Tomato color -->
    <corners android:radius="10dp"/>
</shape>


핵심코드: MainActivity.java
안드로이드 앱이 실행되면 최초로 실행되는 java  파일 (설정에서 다른 파일이 먼저 실행되도록 변경 가능함) 세부적인 작동 내용들은 주석으로 설명함.



package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.MotionEvent;
import android.view.GestureDetector;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;  // Import OkHttpClient
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import com.github.mikephil.charting.components.LimitLine;

import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    //클래스 변수선언 부
    //라인차트
    private LineChart lineChart;
    
    //토글버튼 (괜히 토글버튼으로 만들었음. 사용법은 일반버튼과 거의 동일)
    //에어콘, 조명, 문잠금, 보일러 온 오프 버튼
    private ToggleButton toggleAC, toggleLight, toggleLock, toggleBoiler;
    
    //일반버튼
    //조명모드 세개(노멀, 시네마, 밀리터리)
    private Button  buttonNormal, buttonCinema, buttonMilitary;
    //텍스트 상자(에어컨, 조명, 문잠금, 보일러 온오프 상태 보여주기 용)
    private TextView statusAC, statusLight, statusLock, statusBoiler;

    //드래그 조절 바 세개  (온도, 습도, 조명밝기)
    private SeekBar tempSeekBar, humSeekBar, brightSeekBar;
    //드래그 조절에 따라 그래프위에서 움직이는 라인 두개
    private LimitLine tempLimitLine, humLimitLine;
    
    //온도, 습도, 밝기 기본값
    private float desiredTemp = 25f;  // Default value for temperature
    private float desiredHum = 50f;   // Default value for humidity
    private float desiredBright = 100f; // Default value for brightness

    //쓰레드를 실행할때 사용되는 핸들러
    private Handler handler = new Handler(); // Declare a handler for posting tasks
    
    //센서상태 상태가져오기 쓰레드
    //왜 쓰레드를 사용하는가? 백그라운드에서 타이머에 따라 작동시키려고
    private Runnable fetchStatusRunnable; // Declare a runnable to hold the fetch status logic


    // REST API 선언: 장고에서 선언해 놓은 함수를 호출하기위해 날려주는 웹프로토콜이 REST API임
    // 아래 함수들은 장고 싸이트 만들기 노트 참고
    // 장고 함수 호출할때만 사용하는 것이 아니라 PHP호출 등 다른 명령어 날리기도 가능함.
    // POST는 DB의 무언가를 바꿀때 흔히 사용되며
    // GET은 DB의 무언가를 가져올때 흔히 사용됨
    interface ApiService {
        @GET("/get_last_60_sensor_data")
        Call<SensorDataResponse> getLast60SensorData();

        @GET("/get_actuator_status")
        Call<ActuatorStatusResponse> getActuatorStatus();

        @POST("/update_actuator")
        Call<GenericResponse> updateActuator(@Body ActuatorUpdateBody body);

        @POST("/update_sensor_choice")
        Call<GenericResponse> updateSensorChoice(@Body SensorChoiceBody body);
    }
    
    // Example data models for JSON
    // 센서(온습도)값 받아와서 저장할때 사용되는 클래스
    public static class SensorDataResponse {
        public List<Integer> labels;
        public List<Float> temperature;
        public List<Float> humidity;
    }

    // ON/OFF 상태 받아와서 저장할때 사용되는 클래스
    public static class ActuatorStatusResponse {
        public String acStatus;      // e.g. "ON" or "OFF"
        public String lightStatus;   // e.g. "ON" or "OFF"
        public String lockStatus;    // e.g. "LOCKED" or "UNLOCKED"
        public String boilerStatus;  // e.g. "ON" or "OFF"
    }

    // 받아올꺼 없을때 사용될 클래스
    public static class GenericResponse {
        public String status;
        public String message;
    }

    // 센서 제어 명령 보낼때 사용되는 클래스 (조명, 보일러, 에어컨, 문잠김 ON/OFF 명령 + 조명모드)
    public static class ActuatorUpdateBody {
        public Integer main_led;
        public Integer heater_led;
        public Integer fan;
        public Integer servo_motor;
        public Integer main_led_mode;

        public ActuatorUpdateBody(Integer main_led, Integer heater_led, Integer fan, Integer servo_motor, Integer main_led_mode) {
            this.main_led = main_led;
            this.heater_led = heater_led;
            this.fan = fan;
            this.servo_motor = servo_motor;
            this.main_led_mode = main_led_mode;
        }
    }

    // 온습도, 밝기 값 보낼때 사용되는 클래스
    public static class SensorChoiceBody {
        public Float temperature_choice;
        public Float humidity_choice;
        public Float brightness_choice;

        public SensorChoiceBody(Float temperature_choice, Float humidity_choice, Float brightness_choice) {

            this.temperature_choice = temperature_choice;
            this.humidity_choice = humidity_choice;
            this.brightness_choice = brightness_choice;
        }
    }

    // Retrofit client
    // REST API를 사용할때 필요한 클래스 변수 (Retrofit 라이브러리 설치 필요)
    private ApiService apiService;

    // 이부분(onCreate)은 안드로이드 앱 시작되면 각종 전역변수들 선언되는 곳임
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //activity_mail.xml을 보여주라는 명령어
        setContentView(R.layout.activity_main);
        
        //activity_main안에 있는 모든 위젯들을 init 해주는 함수
        initViews();

        // 인터넷 연결하는 내용같은데 나도 잘 모름
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Adjust timeout if needed
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // REST API 사용을 위한 Retrofit Init
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://smartHome.pababel.com/") // Adjust base URL as needed
                .client(client)  // Attach the OkHttpClient for custom configurations
                .addConverterFactory(GsonConverterFactory.create())  // Gson converter
                .build();
        apiService = retrofit.create(ApiService.class);

        
        setupChart();          // Initialize chart styling
        fetchSensorData();     // Load initial graph data
        startPeriodicFetch();
//        fetchActuatorStatus(); // Load current actuator status

        // 185~197번 라인은 에어컨, 조명, 문잠금, 보일러 on/off 버튼 클릭시 함수 지정 하는 코드
        toggleAC.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateActuator("fan", isChecked ? 1 : 0);
        });
        toggleLight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateActuator("main_led", isChecked ? 1 : 0);
        });
        toggleLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateActuator("servo_motor", isChecked ? 0 : 1);
        });
        toggleBoiler.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateActuator("heater_led", isChecked ? 1 : 0);
        });

        // 199~220번 코드는 조명모드 3가지 버튼 각각 클릭시 호출할 함수 지정
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateActuator 함수 호출
                updateActuator("main_led_mode", 0);
            }
        });

        buttonCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateActuator 함수 호출
                updateActuator("main_led_mode", 1);            }
        });
        
        buttonMilitary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updateActuator 함수 호출
                updateActuator("main_led_mode", 2);            }
        });


        // 223~273번 라인은 온도/습도/밝기 조정 드래그바(seekbar)설정 및 값변시 함수호출 설정 해주는 코드
        tempSeekBar.setMax(40); // Max temperature = 40°C
        tempSeekBar.setProgress((int) desiredTemp); // Set initial progress
        tempSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredTemp = progress;
                updateLimitLine(desiredTemp, true);
                updateSensorChoice(desiredTemp, desiredHum, desiredBright); // Update both values
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        humSeekBar.setMax(100); // Max humidity = 100%
        humSeekBar.setProgress((int) desiredHum); // Set initial progress
        humSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredHum = progress;
                updateLimitLine(desiredHum, false);
                updateSensorChoice(desiredTemp, desiredHum, desiredBright); // Update both values
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        brightSeekBar.setMax(100); // Max humidity = 100%
        brightSeekBar.setProgress((int) desiredBright); // Set initial progress
        brightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredBright = progress;
                updateSensorChoice(desiredTemp, desiredHum, desiredBright); // Update both values
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    //주기적으로 온습도 데이터 받아오기 함수를 호출하는 쓰레드 함수
    private void startPeriodicFetch() {
        // Create a runnable to fetch actuator status
        fetchStatusRunnable = new Runnable() {
            @Override
            public void run() {
                fetchActuatorStatus(); // Call your method
                handler.postDelayed(this, 60000); // Run every 60,000 milliseconds (1 minute)
            }
        };

        // Start the first fetch immediately
        handler.post(fetchStatusRunnable);
    }

    //activity_main안에 있는 모든 위젯들을 init 해주는 함수
    private void initViews() {
        lineChart = findViewById(R.id.lineChart);

        toggleAC = findViewById(R.id.toggleAC);
        toggleLight = findViewById(R.id.toggleLight);
        toggleLock = findViewById(R.id.toggleLock);
        toggleBoiler = findViewById(R.id.toggleBoiler);

        statusAC = findViewById(R.id.statusAC);
        statusLight = findViewById(R.id.statusLight);
        statusLock = findViewById(R.id.statusLock);
        statusBoiler = findViewById(R.id.statusBoiler);

        tempSeekBar = findViewById(R.id.tempSeekBar);
        humSeekBar = findViewById(R.id.humSeekBar);
        brightSeekBar = findViewById(R.id.brightSeekBar);

        buttonNormal = findViewById(R.id.buttonNormal);
        buttonCinema = findViewById(R.id.buttonCinema);
        buttonMilitary = findViewById(R.id.buttonMilitary);
    }

    //차트 스타일 설정 함수 (y axis, x axis 최소값 최대값 단위 등등)
    private void setupChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setPinchZoom(true);

        // X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Y Axis for Temperature (Left Axis)
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);  // Min value for temperature
        leftAxis.setAxisMaximum(40f); // Max value for temperature
        leftAxis.setLabelCount(5, false); // Number of ticks (labels) on the axis
        leftAxis.setDrawGridLines(true); // Optional: To draw grid lines on the left axis

        // Y Axis for Humidity (Right Axis)
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setAxisMinimum(0f);  // Min value for humidity
        rightAxis.setAxisMaximum(100f); // Max value for humidity
        rightAxis.setLabelCount(5, false); // Number of ticks (labels) on the axis
        rightAxis.setDrawGridLines(false); // Do not draw grid lines for the right axis

        // Optional: Hide the right axis if you do not want it to show
        lineChart.getAxisRight().setEnabled(true); // Enable right axis for humidity

        // Setup limit lines for desired temperature/humidity
        tempLimitLine = new LimitLine(desiredTemp, "Temp Setpoint");
        tempLimitLine.setLineWidth(2f);
        tempLimitLine.setLineColor(0xFFFF6347); // tomato color
        tempLimitLine.enableDashedLine(10f, 10f, 0f);

        humLimitLine = new LimitLine(desiredHum, "Hum Setpoint");
        humLimitLine.setLineWidth(2f);
        humLimitLine.setLineColor(0xFFFF69B4); // hotpink
        humLimitLine.enableDashedLine(10f, 10f, 0f);

        // Add LimitLines to their respective axes
        leftAxis.addLimitLine(tempLimitLine);  // Limit for Temperature (left axis)
        rightAxis.addLimitLine(humLimitLine);  // Limit for Humidity (right axis)

        // Optional: Adjust appearance for LimitLines (e.g., dashed lines)
        tempLimitLine.setTextColor(0xFFFF6347);
        humLimitLine.setTextColor(0xFFFF69B4);
    }

    
    private void updateLimitLine(float value, boolean isTemp) {
        // Create a new LimitLine with the updated value
        LimitLine limitLine = new LimitLine(value);

        // Optionally, set other properties like color, dashed line, etc.
        limitLine.setLineWidth(2f);
        limitLine.setLineColor(0xFFFF6347);  // For temperature (tomato color)
        limitLine.enableDashedLine(10f, 10f, 0f);

        if (isTemp) {
            // If it's the temperature, update the LimitLine on the left Y-axis
            lineChart.getAxisLeft().removeAllLimitLines();  // Remove all limit lines (or you can remove only tempLimitLine)
            lineChart.getAxisLeft().addLimitLine(limitLine);
        } else {
            // If it's humidity, update the LimitLine on the right Y-axis
            lineChart.getAxisRight().removeAllLimitLines(); // Remove all limit lines (or you can remove only humLimitLine)
            lineChart.getAxisRight().addLimitLine(limitLine);
        }

        lineChart.invalidate(); // Refresh the chart
    }

    // Networking Calls
    private void fetchSensorData() {
        Call<SensorDataResponse> call = apiService.getLast60SensorData();
        call.enqueue(new Callback<SensorDataResponse>() {
            @Override
            public void onResponse(Call<SensorDataResponse> call, Response<SensorDataResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("fetchSensorData", "Response error");
                    return;
                }
                SensorDataResponse data = response.body();
                updateChart(data.labels, data.temperature, data.humidity);
            }

            @Override
            public void onFailure(Call<SensorDataResponse> call, Throwable t) {
                Log.e("fetchSensorData", "Failure: " + t.getMessage());
            }
        });
    }

    //그래프 그리기 또는 다시 그리기
    private void updateChart(List<Integer> xLabels, List<Float> tempVals, List<Float> humVals) {
        List<Entry> tempEntries = new ArrayList<>();
        List<Entry> humEntries = new ArrayList<>();

        for (int i = 0; i < xLabels.size(); i++) {
            tempEntries.add(new Entry(i, tempVals.get(i)));
            humEntries.add(new Entry(i, humVals.get(i)));
        }

        // Temperature DataSet - Assign to left Y-Axis
        LineDataSet tempDataSet = new LineDataSet(tempEntries, "Temperature (°C)");
        tempDataSet.setColor(0xFFFF6347);
        tempDataSet.setCircleColor(0xFFFF6347);
        tempDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);  // Set to LEFT Y-Axis for Temperature

        // Humidity DataSet - Assign to right Y-Axis
        LineDataSet humDataSet = new LineDataSet(humEntries, "Humidity (%)");
        humDataSet.setColor(0xFF36A2EB);
        humDataSet.setCircleColor(0xFF36A2EB);
        humDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);  // Set to RIGHT Y-Axis for Humidity

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(tempDataSet);
        dataSets.add(humDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    
    //온습도 데이터 받아오기 함수
    private void fetchActuatorStatus() {
        Call<ActuatorStatusResponse> call = apiService.getActuatorStatus();
        call.enqueue(new Callback<ActuatorStatusResponse>() {
            @Override
            public void onResponse(Call<ActuatorStatusResponse> call, Response<ActuatorStatusResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("fetchActuatorStatus", "Response error");
                    return;
                }
                ActuatorStatusResponse data = response.body();
                // Update UI text
                statusAC.setText("에어컨: " + data.acStatus);
                statusLight.setText("거실등: " + data.lightStatus);
                statusLock.setText("현관문: " + data.lockStatus);
                statusBoiler.setText("보일러: " + data.boilerStatus);
            }

            @Override
            public void onFailure(Call<ActuatorStatusResponse> call, Throwable t) {
                Log.e("fetchActuatorStatus", "Failure: " + t.getMessage());
            }
        });
    }

    //DB의 ON/OFF 및 조명 모드값 변경 api 호출 함수
    private void updateActuator(String field, int value) {
        ActuatorUpdateBody body = new ActuatorUpdateBody(null, null, null, null, null);
        
        //각각의 on/off 버튼이 눌러질 때 어떤 버튼이 눌러졌는지 구분하는 case문 
        switch(field) {
            case "main_led":
                body.main_led = value;
                break;
            case "heater_led":
                body.heater_led = value;
                break;
            case "fan":
                body.fan = value;
                break;
            case "servo_motor":
                body.servo_motor = value;
                break;
            case "main_led_mode":
                body.main_led_mode= value;
                break;
        }

        //DB의 ON/OFF 및 조명 모드값 변경 api 호출 라인   
        Call<GenericResponse> call = apiService.updateActuator(body);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("updateActuator", "Response code: " + response.code());
                    return;
                }
                fetchActuatorStatus();
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Log.e("updateActuator", "Failure: " + t.getMessage());
            }
        });
    }

    //DB의 온습도값 변경 api 호출 함수    
    private void updateSensorChoice(Float temp, Float hum, Float bright) {
        SensorChoiceBody body = new SensorChoiceBody(temp, hum, bright);
        Call<GenericResponse> call = apiService.updateSensorChoice(body);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("updateSensorChoice", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Log.e("updateSensorChoice", "Failure: " + t.getMessage());
            }
        });
    }
}
