package com.example.geocoder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private GetGPS gps;
    double latitude = 0;
    double longitude = 0;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static String baseUrl = "http://apis.data.go.kr/";
    String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //Retrofit 객체
    private RetrofitInterface retrofitInterface;


    //Retrofit interface에서 사용할 값
    String serviceKey = "NkrPTYU9SrIK0buIUpAyb4rpzuRZPp62Ab5Jd6sRtQJfxU2UamdedFptFdpI7P1nJz+s3GXvljdYCUksk7KTkg==";
    String pageNo = "1";
    String numOfRows = "10";
    String dataType = "JSON";
    String base_date = null;
    String base_time = null;
    String nx = "";
    String ny = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView_address = findViewById(R.id.addressView);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkPermission();
        }


        Button GpsBtn = (Button) findViewById(R.id.button);
        GpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GetGPS(MainActivity.this);

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                textView_address.setText(address);
                requestRetrofit();

                Toast.makeText(MainActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
            }
        });


    }

    public void checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {

            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";
    }


    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestRetrofit() {

        TextView weatherView_temp = findViewById(R.id.weatherView_temp);
        TextView weatherView_REH = findViewById(R.id.weatherView_REH);
        gps = new GetGPS(MainActivity.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        TransGPS transGPS = new TransGPS();
        TransGPS.LatXLngY tmp = transGPS.convertGRID_GPS(0, latitude, longitude);
        Log.d("$$", "latitude" + toString().valueOf(latitude));
        Log.d("$$", "longitude" + toString().valueOf(longitude));

        Long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        Log.d("$$", "mReDate " + mReDate);
        SimpleDateFormat mFormatYDM = new SimpleDateFormat("yyyyMMdd");
        String formatYDM = mFormatYDM.format(mReDate);
        SimpleDateFormat mFormatTime = new SimpleDateFormat("HHmm");
        Log.d("mFormatTime", "mFormatTime " + mFormatTime);

        String formatTime = String.valueOf(Integer.parseInt(mFormatTime.format(mReDate)) - 100);
        String formatTime1 = mFormatTime.format(mReDate);
        Log.d("%%", "formatTime1 " + formatTime1);


        Log.d("date", "현재 날짜" + formatYDM);
        Log.d("time", "현재 시각" + formatTime1);


        base_date = formatYDM;
        base_time = formatTime;
        nx = String.format("%.0f", tmp.x);
        Log.d("$$", "nx: " + nx);
        ny = String.format("%.0f", tmp.y);
        Log.d("$$", "ny: " + ny);

        //Retrofit Response

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<WeatherResult> call = retrofitInterface.getWeather(serviceKey, numOfRows, pageNo, dataType, base_date, base_time, nx, ny);
        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                if (!response.isSuccessful()) {
                    Log.d("##", "Code: " + response.code());
                } else {
                    Log.d("%%", "response? " + response.toString());
                    //String WR = response.body().toString();
                    ArrayList<WeatherResult.Item> were = response.body().getResponse().getBody().getItems().getItem();

                    Log.d("body", "body?" + were);
                    String Category0 = String.valueOf(were.get(0).getCategory());
                    Log.d("%%", "Category0 " + Category0);
                    for (int i = 0; i <= 7; i++) {
                        String Category = String.valueOf(were.get(i).getCategory());
                        Log.d("%%", "Category_desc " + Category);
                        String ObrValue = String.valueOf(were.get(i).getObsrValue());
                        Log.d("%%", "ObrValue_desc " + ObrValue);

                        switch (Category) {
                            case "T1H":
                                weatherView_temp.setText("기온:" + ObrValue);
                                break;

                            case "REH":
                                weatherView_REH.setText("습도" + ObrValue + "%");
                                break;
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                Log.d("##", "오류" + t.getMessage());
            }
        });
    }


}