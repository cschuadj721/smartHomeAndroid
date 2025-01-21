package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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


public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private ToggleButton toggleAC, toggleLight, toggleLock, toggleBoiler;
    private TextView statusAC, statusLight, statusLock, statusBoiler;

    // We'll track the "desired" set points for Temperature & Humidity
    private float desiredTemp = 25f;    // example default
    private float desiredHum = 50f;     // example default

    // LimitLines for dragging
    private LimitLine tempLimitLine, humLimitLine;

    // We’ll create a basic interface for our REST endpoints (similar to Django endpoints):
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
    public static class SensorDataResponse {
        public List<Integer> labels;
        public List<Float> temperature;
        public List<Float> humidity;
    }

    public static class ActuatorStatusResponse {
        public String acStatus;      // e.g. "ON" or "OFF"
        public String lightStatus;   // e.g. "ON" or "OFF"
        public String lockStatus;    // e.g. "LOCKED" or "UNLOCKED"
        public String boilerStatus;  // e.g. "ON" or "OFF"
    }

    public static class GenericResponse {
        public String status;
        public String message;
    }

    // Request bodies
    public static class ActuatorUpdateBody {
        public Integer main_led;
        public Integer heater_led;
        public Integer fan;
        public Integer servo_motor;

        public ActuatorUpdateBody(Integer main_led, Integer heater_led, Integer fan, Integer servo_motor) {
            this.main_led = main_led;
            this.heater_led = heater_led;
            this.fan = fan;
            this.servo_motor = servo_motor;
        }
    }

    public static class SensorChoiceBody {
        public Float temperature_choice;
        public Float humidity_choice;

        public SensorChoiceBody(Float temperature_choice, Float humidity_choice) {
            this.temperature_choice = temperature_choice;
            this.humidity_choice = humidity_choice;
        }
    }

    // Retrofit client
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        // Setup OkHttpClient with timeout settings
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Adjust timeout if needed
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // Setup Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://smartHome.pababel.com/") // Adjust base URL as needed
                .client(client)  // Attach the OkHttpClient for custom configurations
                .addConverterFactory(GsonConverterFactory.create())  // Gson converter
                .build();
        apiService = retrofit.create(ApiService.class);

        setupChart();          // Initialize chart styling
        fetchSensorData();     // Load initial graph data
        fetchActuatorStatus(); // Load current actuator status

        // Set toggle listeners
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
    }

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
    }

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

    private void updateActuator(String field, int value) {
        ActuatorUpdateBody body = new ActuatorUpdateBody(null, null, null, null);
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
        }

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

    private void updateSensorChoice(Float temp, Float hum) {
        SensorChoiceBody body = new SensorChoiceBody(temp, hum);
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
