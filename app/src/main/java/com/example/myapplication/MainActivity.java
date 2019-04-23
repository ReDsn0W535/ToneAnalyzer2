package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.NetClient.Client;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText text;
    private BarChart barChart;
    private static ArrayList<Integer> colors;
    private Button searchInTwitter;
    private static HashMap<String, String> outText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        colors = new ArrayList<>();
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.editText);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        barChart = findViewById(R.id.chart);
        barChart.setVisibility(View.GONE);
        barChart.setNoDataTextDescription("");
        super.onCreate(savedInstanceState);
        Button analyze = findViewById(R.id.button);
        analyze.setOnClickListener(v -> startAnalyze(this));
        searchInTwitter = findViewById(R.id.twitterButton);
        searchInTwitter.setOnClickListener(v -> twitterSearch());

    }

    private void startAnalyze(Context context) {
        Client.getInstance().setUsername("apikey");
        Client.getInstance().setPassword(this.getString(R.string.API_KEY));
        Client.getInstance().createClient();
        Client.getInstance().createApi().getToneAlanyze( "2019-03-07", text.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                System.out.println(response.body().keySet());
                Gson gson = new Gson();
                List a = gson.fromJson(response.body().getAsJsonObject("document_tone").getAsJsonArray("tones"), List.class);
                List b = gson.fromJson(response.body().getAsJsonArray("sentences_tone"), List.class);
                setupBar(a, context);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("error: " + t.toString());
            }
        });
    }
    private void setupBar(List<LinkedTreeMap> input, Context context){
        barChart.setVisibility(View.VISIBLE);
        ArrayList<BarEntry> BarEntry = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i){
            String name = input.get(i).get("tone_name").toString();
            double score = (double) input.get(i).get("score");
            float floatScore = (float) score;
            BarEntry.add(new BarEntry(floatScore, i));
            labels.add(name);
        }
        BarDataSet dataSet = new BarDataSet(BarEntry , "Tones");
        dataSet.setBarSpacePercent(1f);
        BarData data = new BarData(labels, dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        barChart.getAxisRight().setLabelCount(labels.size(), true);
        barChart.getXAxis().setLabelsToSkip(0);
        barChart.getAxisRight().setAxisMinValue(0f);
        barChart.getAxisLeft().setAxisMinValue(0f);
        //barChart.getAxisLeft().setAxisMaxValue(0f);
        //barChart.getAxisRight().setAxisMaxValue(0f);
        barChart.setData(data);
        barChart.setDescription(" ");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
    }

    private String twitterSearch(){
        String url = "https://twitter.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivityForResult(i, 0);
        return null;
    }
}
