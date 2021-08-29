package com.example.charhomework;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {



    //private int[] yDataTotal = {23, 30, 18, 10};
    //private String[]    xDataTotal = {"sport", "space", "art", "ff"};




   ////////////////PIE CHARTS///////////////
    PieChart pieChartHours, pieChartParents, pieChartKids, pieChartTotal;



    BarChart barchart;
    BarChart mChart;


    //////////PERCENTAGE VIEWS///////////////////
    TextView  percentage_hour,percentage_Parent,percentage_Children;

    /////////NAVIGATION BAR///////////
    BottomNavigationView navigationView;

   /////////////////BUTTONS FOR DATASET/////////////
    Button btn_week , btn_month , btn_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////RETROFIT API///////////////////////////////
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8090/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //////////////CHARTS VIEW////////////////
        pieChartHours = findViewById(R.id.idPieChartHours);
        pieChartParents = findViewById(R.id.idPieChartParent);
        pieChartKids = findViewById(R.id.idPieChartKids);
        pieChartTotal = (PieChart) findViewById(R.id.idPieChartTotal);
        barchart = findViewById(R.id.barchart);


        ////////////PERCENTAGE LABELS//////////////
        navigationView=findViewById(R.id.bottom_navigation);
        percentage_hour=findViewById(R.id.textView2);
        percentage_Parent=findViewById(R.id.textView3);
        percentage_Children=findViewById(R.id.textView4);


        ////////////On CLick Listener for bottoms (Weekly-Monthly-Year)///////////
        btn_week = findViewById(R.id.weekBtn);
        btn_month = findViewById(R.id.monthBtn);
        btn_year = findViewById(R.id.yearBtn);


        ArrayList<String> labels = new ArrayList<>();
        //String[] labels = {"", "1", "2", "3", "4", "5","6","7","8","9","10","11","12", ""};
        labels.add("");
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("");

        //////////DEFAULT START//////////

        Call<HashMap<String,Integer>> kids = retrofitAPI.getNewKids(2);
        kids.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> kids, Response<HashMap<String, Integer>> response) {
                HashMap<String,Integer> theKids = response.body();
                Integer kidsCount = theKids.get("newKids");
                Integer totalKids = theKids.get("totalKids");
                // DataKids();

                Integer percent = kidsCount *100/totalKids;
                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();

                int[] yData = {kidsCount, totalKids-kidsCount};
                String[] xData = {"newKids", "totalKids"};

                for (int i = 0; i < yData.length; i++) {
                    yEntrys.add(new PieEntry(yData[i], i));
                }

                for (int i = 1; i < xData.length; i++) {
                    xEntrys.add(xData[i]);
                }


                //create the data set
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "                  New Children");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(12);


                //add colors to dataset
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#4A92FC"));
                colors.add(Color.parseColor("#E8EAF6"));

                pieDataSet.setColors(colors);
                pieDataSet.setDrawValues(false);
                pieDataSet.setSliceSpace(0f);
                pieChartKids.setDrawSliceText((boolean) false);
                //add legend to chart
                Legend legend = pieChartKids.getLegend();
                legend.setForm(Legend.LegendForm.NONE);
                // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                //create pie data object
                PieData pieData = new PieData(pieDataSet);
                pieChartKids.setData(pieData);

                pieChartKids.invalidate();
                //pieChart.getLegend().setEnabled(false);

                percentage_Children.setText(String.valueOf(percent) +"%");
                pieChartKids.setCenterText(kidsCount.toString());
                pieChartKids.setRotationEnabled(false);
                pieChartKids.setUsePercentValues(false);
                pieChartKids.setHoleRadius(83f);
                pieChartKids.setTransparentCircleAlpha(0);
                pieChartKids.setCenterTextSize(20);
                pieChartKids.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pieChartKids.getDescription().setEnabled(false);
                pieChartKids.setHighlightPerTapEnabled(false);


                //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });


        Call<HashMap<String,Integer>> parents = retrofitAPI.getNewParents(2);
        parents.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> parents, Response<HashMap<String, Integer>> response) {
                HashMap<String,Integer> theParents = response.body();
                Integer parentsCount = theParents.get("New Parents");
                Integer totalParents = theParents.get("totalParents");
                //DataParents();

                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();

                int[] yData = {parentsCount, totalParents-parentsCount};
                String[] xData = {"New Parents", "totalParents"};

                for (int i = 0; i < yData.length; i++) {
                    yEntrys.add(new PieEntry(yData[i], i));
                }

                for (int i = 1; i < xData.length; i++) {
                    xEntrys.add(xData[i]);
                }

                //create the data set
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "                   New Parents");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(12);

                //add colors to dataset
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#ffbb33"));
                colors.add(Color.parseColor("#E8EAF6"));


                pieDataSet.setColors(colors);
                pieDataSet.setDrawValues(false);
                pieDataSet.setSliceSpace(0f);
                pieChartParents.setDrawSliceText(false);
                //add legend to chart
                Legend legend = pieChartParents.getLegend();
                legend.setForm(Legend.LegendForm.NONE);
                // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                //create pie data object
                PieData pieData = new PieData(pieDataSet);
                pieChartParents.setData(pieData);

                pieChartParents.invalidate();
                //pieChart.getLegend().setEnabled(false);

                ////////////////////////////////


                Integer percent = parentsCount *100/totalParents;
                percentage_Parent.setText(String.valueOf(percent) +"%");
                pieChartParents.setCenterText(parentsCount.toString());
                pieChartParents.setRotationEnabled(false);
                pieChartParents.setUsePercentValues(false);
                pieChartParents.setHoleRadius(83f);
                pieChartParents.setTransparentCircleAlpha(0);
                pieChartParents.setCenterTextSize(20);
                pieChartParents.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pieChartParents.getDescription().setEnabled(false);
                pieChartParents.setHighlightPerTapEnabled(false);

                //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });


        Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(2);
        kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> kidsByCategory, Response<HashMap<String, Integer>> response) {
                HashMap<String,Integer> categoryCount = response.body();
                Integer animalsCount = categoryCount.get("Animal");
                Integer artsCount = categoryCount.get("Arts");
                Integer scienceCount = categoryCount.get("Science");
                Integer spaceCount = categoryCount.get("Space");
                //DataTotal();
                ArrayList<PieEntry> yEntrysTotal = new ArrayList<>();
                ArrayList<String> xEntrysTotal = new ArrayList<>();

                int[] yDataTotal = {animalsCount, artsCount, scienceCount, spaceCount};
                String[] xDataTotal = {"Animal", "Arts", "Science", "Space"};

                for (int i = 0; i < yDataTotal.length; i++) {
                    yEntrysTotal.add(new PieEntry(yDataTotal[i], i));
                }

                for (int i = 1; i < xDataTotal.length; i++) {
                    xEntrysTotal.add(xDataTotal[i]);
                }
                PieDataSet pieDataSetTotal = new PieDataSet(yEntrysTotal, "     Total Per Category");
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#E8EAF6"));
                colors.add(Color.parseColor("#4A92FC"));
                colors.add(Color.parseColor("#ffbb33"));
                colors.add(Color.parseColor("#4E7FE7"));


                pieDataSetTotal.setSliceSpace(2);
                pieDataSetTotal.setValueTextSize(12);
                pieDataSetTotal.setColors(colors);
                pieDataSetTotal.setDrawValues(true);
                pieDataSetTotal.setSliceSpace(0f);
                // pieChartTotal.setDrawSliceText(false);
                //add legend to chart


                pieDataSetTotal.setColors(colors);
                pieDataSetTotal.setDrawValues(true);
                pieDataSetTotal.setSliceSpace(0f);
                //  pieChartTotal.setDrawSliceText(true);
                ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
                    @Override
                    public String getFormattedValue(float value) {
                        return "" + (int) value;
                    }
                };
                pieDataSetTotal.setValueFormatter(vf);
                Legend legendTotal = pieChartTotal.getLegend();
                legendTotal.setForm(Legend.LegendForm.NONE);

                // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                legendTotal.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legendTotal.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legendTotal.setDrawInside(false);
                //create pie data object
                PieData pieDataTotal = new PieData(pieDataSetTotal);
                pieChartTotal.setData(pieDataTotal);

                pieChartTotal.invalidate();

                pieChartTotal.setRotationEnabled(false);
                pieChartTotal.setUsePercentValues(false);
                pieChartTotal.setHoleRadius(0f);
                pieChartTotal.setTransparentCircleAlpha(0);
                pieChartTotal.getDescription().setEnabled(false);
                pieChartTotal.setHighlightPerTapEnabled(false);



                //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });



        Call<HashMap<String,Double>> activityTime = retrofitAPI.getActivityTime(2);
        activityTime.enqueue(new Callback<HashMap<String,Double>>() {
            @Override
            public void onResponse(Call<HashMap<String, Double>> activityTime, Response<HashMap<String, Double>> response) {
                HashMap<String,Double> times = response.body();

                Double allTime = times.get("totalTime");
                Double activeTime = times.get("activityTime");
                Integer percent = activeTime.intValue() *100/allTime.intValue();
                //DataHours();
                ArrayList<PieEntry> yEntrys = new ArrayList<>();
                ArrayList<String> xEntrys = new ArrayList<>();

                Double [] yData = {activeTime, allTime-activeTime};
                String[] xData = {"activityTime", "totalTime"};

                for (int i = 0; i < yData.length; i++) {
                    yEntrys.add(new PieEntry(yData[i].intValue(), i));
                }

                for (int i = 1; i < xData.length; i++) {
                    xEntrys.add(xData[i]);
                }


                //create the data set
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "          Monthly Activities In Hour");

                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(12);

                //add colors to dataset
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#4E7FE7"));
                colors.add(Color.parseColor("#E8EAF6"));


                pieDataSet.setColors(colors);
                pieDataSet.setDrawValues(false);
                pieDataSet.setSliceSpace(0f);
                pieChartHours.setDrawSliceText(false);
                //add legend to chart
                Legend legend = pieChartHours.getLegend();
                legend.setForm(Legend.LegendForm.NONE);
                // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setDrawInside(false);
                //create pie data object
                PieData pieData = new PieData(pieDataSet);
                pieChartHours.setData(pieData);
                pieChartHours.invalidate();
                //pieChart.getLegend().setEnabled(false);

                ////////////////////////////////
                pieChartHours.setCenterText(activeTime.toString());
                percentage_hour.setText(String.valueOf(percent) +"%");
                pieChartHours.setCenterText(activeTime.toString());
                pieChartHours.setRotationEnabled(false);
                pieChartHours.setUsePercentValues(false);
                pieChartHours.setHoleRadius(83f);
                pieChartHours.setTransparentCircleAlpha(0);
                pieChartHours.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                pieChartHours.setCenterTextSize(20);
                pieChartHours.getDescription().setEnabled(false);
                pieChartHours.setHighlightPerTapEnabled(false);
                //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });


        //GroupBarChartMonth(labels);




        //navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_courses:
                        fragment=new CoursesFragment();
                        break;
                    case R.id.nav_leaders:
                        fragment=new LeadersFragment();
                        break;
                    case R.id.nav_users:
                        fragment=new UsersFragment();
                        break;
                    case R.id.nav_more:
                        fragment=new MoreFragment();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main,fragment).commit();
                return true;
            }
        });


        // week button listener
        btn_week.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                ArrayList<String> labels1 = new ArrayList<>();
                //String[] labels = {"", "1", "2", "3", "4", "5","6","7","8","9","10","11","12", ""};
                labels.add("");
                labels.add("1");
                labels.add("2");
                labels.add("3");
                labels.add("4");
                labels.add("5");
                labels.add("6");
                labels.add("7");
                labels.add("");

                GroupBarChartWeek(labels1);
                Call<HashMap<String,Integer>> kids = retrofitAPI.getNewKids(1);
                kids.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kids, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theKids = response.body();
                        Integer kidsCount = theKids.get("newKids");
                        Integer totalKids = theKids.get("totalKids");
                        //DataKids();

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {kidsCount, totalKids-kidsCount};
                        String[] xData = {"newKids", "totalKids"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                  New Children");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);


                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#E8EAF6"));

                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartKids.setDrawSliceText((boolean) false);
                        //add legend to chart
                        Legend legend = pieChartKids.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartKids.setData(pieData);

                        pieChartKids.invalidate();
                        //pieChart.getLegend().setEnabled(false);


                        Integer percent = kidsCount *100/totalKids;
                        percentage_Children.setText(String.valueOf(percent) +"%");
                        pieChartKids.setCenterText(kidsCount.toString());
                        pieChartKids.setRotationEnabled(false);
                        pieChartKids.setUsePercentValues(false);
                        pieChartKids.setHoleRadius(83f);
                        pieChartKids.setTransparentCircleAlpha(0);
                        pieChartKids.setCenterTextSize(20);
                        pieChartKids.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartKids.getDescription().setEnabled(false);
                        pieChartKids.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Integer>> parents = retrofitAPI.getNewParents(1);
                parents.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> parents, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theParents = response.body();
                        Integer parentsCount = theParents.get("New Parents");
                        Integer totalParents = theParents.get("totalParents");
                        //DataParents();
                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {parentsCount, totalParents-parentsCount};
                        String[] xData = {"New Parents", "totalParents"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }

                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                   New Parents");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartParents.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartParents.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartParents.setData(pieData);

                        pieChartParents.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////

                        Integer percent = parentsCount *100/totalParents;
                        percentage_Parent.setText(String.valueOf(percent) +"%");
                        pieChartParents.setCenterText(parentsCount.toString());
                        pieChartParents.setRotationEnabled(false);
                        pieChartParents.setUsePercentValues(false);
                        pieChartParents.setHoleRadius(83f);
                        pieChartParents.setTransparentCircleAlpha(0);
                        pieChartParents.setCenterTextSize(20);
                        pieChartParents.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartParents.getDescription().setEnabled(false);
                        pieChartParents.setHighlightPerTapEnabled(false);

                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(1);
                kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kidsByCategory, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> categoryCount = response.body();
                        Integer animalsCount = categoryCount.get("Animal");
                        Integer artsCount = categoryCount.get("Arts");
                        Integer scienceCount = categoryCount.get("Science");
                        Integer spaceCount = categoryCount.get("Space");


                        //DataTotal();

                        ArrayList<PieEntry> yEntrysTotal = new ArrayList<>();
                        ArrayList<String> xEntrysTotal = new ArrayList<>();

                        if(animalsCount == null){
                            animalsCount=14;
                        }

                        if(artsCount == null){
                            artsCount=9;
                        }

                        if(scienceCount == null){
                            scienceCount=5;
                        }

                        if(spaceCount == null){
                            spaceCount=15;
                        }

                        Integer[] yDataTotal = {animalsCount, artsCount, scienceCount, spaceCount};
                        String[] xDataTotal = {"Animal", "Arts", "Science", "Space"};

                        for (int i = 0; i < yDataTotal.length; i++) {
                            yEntrysTotal.add(new PieEntry(yDataTotal[i], i));
                        }

                        for (int i = 1; i < xDataTotal.length; i++) {
                            xEntrysTotal.add(xDataTotal[i]);
                        }
                        PieDataSet pieDataSetTotal = new PieDataSet(yEntrysTotal, "     Total Per Category");
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#E8EAF6"));
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#4E7FE7"));


                        pieDataSetTotal.setSliceSpace(2);
                        pieDataSetTotal.setValueTextSize(12);
                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        // pieChartTotal.setDrawSliceText(false);
                        //add legend to chart


                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        //  pieChartTotal.setDrawSliceText(true);
                        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
                            @Override
                            public String getFormattedValue(float value) {
                                return "" + (int) value;
                            }
                        };
                        pieDataSetTotal.setValueFormatter(vf);
                        Legend legendTotal = pieChartTotal.getLegend();
                        legendTotal.setForm(Legend.LegendForm.NONE);

                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legendTotal.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legendTotal.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legendTotal.setDrawInside(false);
                        //create pie data object
                        PieData pieDataTotal = new PieData(pieDataSetTotal);
                        pieChartTotal.setData(pieDataTotal);

                        pieChartTotal.invalidate();

                        pieChartTotal.setRotationEnabled(false);
                        pieChartTotal.setUsePercentValues(false);
                        pieChartTotal.setHoleRadius(0f);
                        pieChartTotal.setTransparentCircleAlpha(0);
                        pieChartTotal.getDescription().setEnabled(false);
                        pieChartTotal.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Double>> activityTime = retrofitAPI.getActivityTime(1);
                activityTime.enqueue(new Callback<HashMap<String,Double>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Double>> activityTime, Response<HashMap<String, Double>> response) {
                        HashMap<String,Double> times = response.body();

                        Double allTime = times.get("totalTime");
                        Double activeTime = times.get("activityTime");
                        Integer percent = activeTime.intValue() *100/allTime.intValue();
                        //DataHours();
                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        Double [] yData = {activeTime, allTime-activeTime};
                        String[] xData = {"activityTime", "totalTime"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i].intValue(), i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "          Weekly Activities In Hour");

                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4E7FE7"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartHours.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartHours.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartHours.setData(pieData);
                        pieChartHours.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////
                        pieChartHours.setCenterText(activeTime.toString());
                        percentage_hour.setText(String.valueOf(percent) +"%");
                        pieChartHours.setCenterText(allTime.toString());
                        pieChartHours.setRotationEnabled(false);
                        pieChartHours.setUsePercentValues(false);
                        pieChartHours.setHoleRadius(83f);
                        pieChartHours.setTransparentCircleAlpha(0);
                        pieChartHours.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartHours.setCenterTextSize(20);
                        pieChartHours.getDescription().setEnabled(false);
                        pieChartHours.setHighlightPerTapEnabled(false);
                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        // month button listener

        btn_month.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                ArrayList<String> labels = new ArrayList<>();
                //String[] labels = {"", "1", "2", "3", "4", "5","6","7","8","9","10","11","12", ""};
                labels.add("");
                labels.add("1");
                labels.add("2");
                labels.add("3");
                labels.add("4");
                labels.add("5");
                labels.add("");

                GroupBarChartMonth(labels);

                Call<HashMap<String,Integer>> kids = retrofitAPI.getNewKids(2);
                kids.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kids, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theKids = response.body();
                        Integer kidsCount = theKids.get("newKids");
                        Integer totalKids = theKids.get("totalKids");
                       // DataKids();

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {kidsCount, totalKids-kidsCount};
                        String[] xData = {"newKids", "totalKids"};


                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i],i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                  New Children");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);


                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#E8EAF6"));

                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartKids.setDrawSliceText((boolean) false);
                        //add legend to chart
                        Legend legend = pieChartKids.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartKids.setData(pieData);

                        pieChartKids.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        Integer percent = kidsCount *100/totalKids;
                        percentage_Children.setText(String.valueOf(percent) +"%");
                        pieChartKids.setCenterText(kidsCount.toString());
                        pieChartKids.setRotationEnabled(false);
                        pieChartKids.setUsePercentValues(false);
                        pieChartKids.setHoleRadius(83f);
                        pieChartKids.setTransparentCircleAlpha(0);
                        pieChartKids.setCenterTextSize(20);
                        pieChartKids.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartKids.getDescription().setEnabled(false);
                        pieChartKids.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Integer>> parents = retrofitAPI.getNewParents(2);
                parents.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> parents, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theParents = response.body();
                        Integer parentsCount = theParents.get("New Parents");
                        Integer totalParents = theParents.get("totalParents");
                        //DataParents();

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {parentsCount, totalParents-parentsCount};
                        String[] xData = {"New Parents", "totalParents"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }

                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                   New Parents");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartParents.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartParents.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartParents.setData(pieData);

                        pieChartParents.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////


                        Integer percent = parentsCount *100/totalParents;
                        percentage_Parent.setText(String.valueOf(percent) +"%");
                        pieChartParents.setCenterText(parentsCount.toString());
                        pieChartParents.setRotationEnabled(false);
                        pieChartParents.setUsePercentValues(false);
                        pieChartParents.setHoleRadius(83f);
                        pieChartParents.setTransparentCircleAlpha(0);
                        pieChartParents.setCenterTextSize(20);
                        pieChartParents.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartParents.getDescription().setEnabled(false);
                        pieChartParents.setHighlightPerTapEnabled(false);

                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(2);
                kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kidsByCategory, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> categoryCount = response.body();
                        Integer animalsCount = categoryCount.get("Animal");
                        Integer artsCount = categoryCount.get("Arts");
                        Integer scienceCount = categoryCount.get("Science");
                        Integer spaceCount = categoryCount.get("Space");
                        //DataTotal();
                        ArrayList<PieEntry> yEntrysTotal = new ArrayList<>();
                        ArrayList<String> xEntrysTotal = new ArrayList<>();

                        int[] yDataTotal = {animalsCount, artsCount, scienceCount, spaceCount};
                        String[] xDataTotal = {"Animal", "Arts", "Science", "Space"};

                        for (int i = 0; i < yDataTotal.length; i++) {
                            yEntrysTotal.add(new PieEntry(yDataTotal[i], i));
                        }

                        for (int i = 1; i < xDataTotal.length; i++) {
                            xEntrysTotal.add(xDataTotal[i]);
                        }
                        PieDataSet pieDataSetTotal = new PieDataSet(yEntrysTotal, "     Total Per Category");
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#E8EAF6"));
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#4E7FE7"));


                        pieDataSetTotal.setSliceSpace(2);
                        pieDataSetTotal.setValueTextSize(12);
                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        // pieChartTotal.setDrawSliceText(false);
                        //add legend to chart


                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        //  pieChartTotal.setDrawSliceText(true);
                        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
                            @Override
                            public String getFormattedValue(float value) {
                                return "" + (int) value;
                            }
                        };
                        pieDataSetTotal.setValueFormatter(vf);
                        Legend legendTotal = pieChartTotal.getLegend();
                        legendTotal.setForm(Legend.LegendForm.NONE);

                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legendTotal.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legendTotal.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legendTotal.setDrawInside(false);
                        //create pie data object
                        PieData pieDataTotal = new PieData(pieDataSetTotal);
                        pieChartTotal.setData(pieDataTotal);

                        pieChartTotal.invalidate();

                        pieChartTotal.setRotationEnabled(false);
                        pieChartTotal.setUsePercentValues(false);
                        pieChartTotal.setHoleRadius(0f);
                        pieChartTotal.setTransparentCircleAlpha(0);
                        pieChartTotal.getDescription().setEnabled(false);
                        pieChartTotal.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });



                Call<HashMap<String,Double>> activityTime = retrofitAPI.getActivityTime(2);
                activityTime.enqueue(new Callback<HashMap<String,Double>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Double>> activityTime, Response<HashMap<String, Double>> response) {
                        HashMap<String,Double> times = response.body();

                        Double allTime = times.get("totalTime");
                        Double activeTime = times.get("activityTime");
                        Integer percent = activeTime.intValue() *100/allTime.intValue();
                        //DataHours();
                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        Double [] yData = {activeTime,allTime-activeTime};
                        String[] xData = {"activityTime", "totalTime"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i].intValue(), i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "          Monthly Activities In Hour");

                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4E7FE7"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartHours.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartHours.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartHours.setData(pieData);
                        pieChartHours.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////
                        pieChartHours.setCenterText(activeTime.toString());
                        percentage_hour.setText(String.valueOf(percent) +"%");
                        pieChartHours.setCenterText(activeTime.toString());
                        pieChartHours.setRotationEnabled(false);
                        pieChartHours.setUsePercentValues(false);
                        pieChartHours.setHoleRadius(83f);
                        pieChartHours.setTransparentCircleAlpha(0);
                        pieChartHours.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartHours.setCenterTextSize(20);
                        pieChartHours.getDescription().setEnabled(false);
                        pieChartHours.setHighlightPerTapEnabled(false);
                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                // Toast.makeText(MainActivity.this," youClickMonthBtn",Toast.LENGTH_SHORT).show();
            }


        });

        btn_month.performClick();
        btn_month.performClick();

        // year button listener

        btn_year.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                ArrayList<String> labels = new ArrayList<>();
                //String[] labels = {"", "1", "2", "3", "4", "5","6","7","8","9","10","11","12", ""};
                labels.add("");
                labels.add("1");
                labels.add("2");
                labels.add("3");
                labels.add("4");
                labels.add("5");
                labels.add("6");
                labels.add("7");
                labels.add("8");
                labels.add("9");
                labels.add("10");
                labels.add("11");
                labels.add("12");
                labels.add("");
                GroupBarChartYear(labels);

                Call<HashMap<String,Integer>> kids = retrofitAPI.getNewKids(3);
                kids.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kids, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theKids = response.body();
                        Integer kidsCount = theKids.get("newKids");
                        Integer totalKids = theKids.get("totalKids");
                        //DataKids();

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {kidsCount, totalKids-kidsCount};
                        String[] xData = {"newKids", "totalKids"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                  New Children");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);


                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#E8EAF6"));

                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartKids.setDrawSliceText((boolean) false);
                        //add legend to chart
                        Legend legend = pieChartKids.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartKids.setData(pieData);

                        pieChartKids.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        Integer percent = kidsCount *100/totalKids;
                        percentage_Children.setText(String.valueOf(percent) +"%");
                        pieChartKids.setCenterText(kidsCount.toString());
                        pieChartKids.setRotationEnabled(false);
                        pieChartKids.setUsePercentValues(false);
                        pieChartKids.setHoleRadius(83f);
                        pieChartKids.setTransparentCircleAlpha(0);
                        pieChartKids.setCenterTextSize(20);
                        pieChartKids.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartKids.getDescription().setEnabled(false);
                        pieChartKids.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                Call<HashMap<String,Integer>> parents = retrofitAPI.getNewParents(3);
                parents.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> parents, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> theParents = response.body();
                        Integer parentsCount = theParents.get("New Parents");
                        Integer totalParents = theParents.get("totalParents");
                        //DataParents();

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        int[] yData = {parentsCount, totalParents-parentsCount};
                        String[] xData = {"New Parents", "totalParents"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }

                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "                   New Parents");
                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartParents.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartParents.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartParents.setData(pieData);

                        pieChartParents.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////

                        Integer percent = parentsCount *100/totalParents;
                        percentage_Parent.setText(String.valueOf(percent) +"%");
                        pieChartParents.setCenterText(parentsCount.toString());
                        pieChartParents.setRotationEnabled(false);
                        pieChartParents.setUsePercentValues(false);
                        pieChartParents.setHoleRadius(83f);
                        pieChartParents.setTransparentCircleAlpha(0);
                        pieChartParents.setCenterTextSize(20);
                        pieChartParents.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartParents.getDescription().setEnabled(false);
                        pieChartParents.setHighlightPerTapEnabled(false);

                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Integer>> kidsByCategory = retrofitAPI.getKidsCountByCategory(3);
                kidsByCategory.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> kidsByCategory, Response<HashMap<String, Integer>> response) {
                        HashMap<String,Integer> categoryCount = response.body();
                        Integer animalsCount = categoryCount.get("Animal");
                        Integer artsCount = categoryCount.get("Arts");
                        Integer scienceCount = categoryCount.get("Science");
                        Integer spaceCount = categoryCount.get("Space");
                        //DataTotal();

                        ArrayList<PieEntry> yEntrysTotal = new ArrayList<>();
                        ArrayList<String> xEntrysTotal = new ArrayList<>();

                        int[] yDataTotal = {animalsCount, artsCount, scienceCount, spaceCount};
                        String[] xDataTotal = {"Animal", "Arts", "Science", "Space"};

                        for (int i = 0; i < yDataTotal.length; i++) {
                            yEntrysTotal.add(new PieEntry(yDataTotal[i], i));
                        }

                        for (int i = 1; i < xDataTotal.length; i++) {
                            xEntrysTotal.add(xDataTotal[i]);
                        }
                        PieDataSet pieDataSetTotal = new PieDataSet(yEntrysTotal, "     Total Per Category");
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#E8EAF6"));
                        colors.add(Color.parseColor("#4A92FC"));
                        colors.add(Color.parseColor("#ffbb33"));
                        colors.add(Color.parseColor("#4E7FE7"));


                        pieDataSetTotal.setSliceSpace(2);
                        pieDataSetTotal.setValueTextSize(12);
                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        // pieChartTotal.setDrawSliceText(false);
                        //add legend to chart


                        pieDataSetTotal.setColors(colors);
                        pieDataSetTotal.setDrawValues(true);
                        pieDataSetTotal.setSliceSpace(0f);
                        //  pieChartTotal.setDrawSliceText(true);
                        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
                            @Override
                            public String getFormattedValue(float value) {
                                return "" + (int) value;
                            }
                        };
                        pieDataSetTotal.setValueFormatter(vf);
                        Legend legendTotal = pieChartTotal.getLegend();
                        legendTotal.setForm(Legend.LegendForm.NONE);

                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legendTotal.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legendTotal.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legendTotal.setDrawInside(false);
                        //create pie data object
                        PieData pieDataTotal = new PieData(pieDataSetTotal);
                        pieChartTotal.setData(pieDataTotal);

                        pieChartTotal.invalidate();
                        pieChartTotal.setRotationEnabled(false);
                        pieChartTotal.setUsePercentValues(false);
                        pieChartTotal.setHoleRadius(0f);
                        pieChartTotal.setTransparentCircleAlpha(0);
                        pieChartTotal.getDescription().setEnabled(false);
                        pieChartTotal.setHighlightPerTapEnabled(false);


                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                Call<HashMap<String,Double>> activityTime = retrofitAPI.getActivityTime(3);
                activityTime.enqueue(new Callback<HashMap<String,Double>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Double>> activityTime, Response<HashMap<String, Double>> response) {
                        HashMap<String,Double> times = response.body();

                        Double allTime = times.get("totalTime");
                        Double activeTime = times.get("activityTime");
                        Integer percent = activeTime.intValue() *100/allTime.intValue();
                        //DataHours();
                        ArrayList<PieEntry> yEntrys = new ArrayList<>();
                        ArrayList<String> xEntrys = new ArrayList<>();

                        Double [] yData = {activeTime, allTime-activeTime};
                        String[] xData = {"activityTime", "totalTime"};

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i].intValue(), i));
                        }

                        for (int i = 1; i < xData.length; i++) {
                            xEntrys.add(xData[i]);
                        }


                        //create the data set
                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "          Yearly Activities In Hour");

                        pieDataSet.setSliceSpace(2);
                        pieDataSet.setValueTextSize(12);

                        //add colors to dataset
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.parseColor("#4E7FE7"));
                        colors.add(Color.parseColor("#E8EAF6"));


                        pieDataSet.setColors(colors);
                        pieDataSet.setDrawValues(false);
                        pieDataSet.setSliceSpace(0f);
                        pieChartHours.setDrawSliceText(false);
                        //add legend to chart
                        Legend legend = pieChartHours.getLegend();
                        legend.setForm(Legend.LegendForm.NONE);
                        // legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        // legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        legend.setDrawInside(false);
                        //create pie data object
                        PieData pieData = new PieData(pieDataSet);
                        pieChartHours.setData(pieData);
                        pieChartHours.invalidate();
                        //pieChart.getLegend().setEnabled(false);

                        ////////////////////////////////

                        percentage_hour.setText(String.valueOf(percent) +"%");
                        pieChartHours.setCenterText(activeTime.toString());
                        pieChartHours.setCenterText(allTime.toString());
                        pieChartHours.setRotationEnabled(false);
                        pieChartHours.setUsePercentValues(false);
                        pieChartHours.setHoleRadius(83f);
                        pieChartHours.setTransparentCircleAlpha(0);
                        pieChartHours.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        pieChartHours.setCenterTextSize(20);
                        pieChartHours.getDescription().setEnabled(false);
                        pieChartHours.setHighlightPerTapEnabled(false);
                        //Toast.makeText(MainActivity.this," youClickWeekBtn",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });
               // Toast.makeText(MainActivity.this," youClickYearBtn",Toast.LENGTH_SHORT).show();
            }
        });

        //////////////////////////////////////////////

        //DataHours();
       // DataParents();
        //DataTotal();
        //DataKids();
        //GroupBarChart(labels);
    }

    private void GroupBarChartWeek(ArrayList<String> labels) {


        mChart = (BarChart) findViewById(R.id.barchart);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        // empty labels so that the names are spread evenly
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);

        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(7, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);



        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        float[] valOne = {30, 12, 5, 13, 11, 12 , 23};
        float[] valTwo = {14, 12, 21, 23, 20 , 18, 27};
        float[] valThree = {34, 24, 40, 16, 19, 20, 30};
        float[] valfour = {23, 17, 19, 26, 30, 24, 12};

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        ArrayList<BarEntry> barFour = new ArrayList<>();
        ArrayList<BarEntry> barFive = new ArrayList<>();
        ArrayList<BarEntry> barSix = new ArrayList<>();
        ArrayList<BarEntry> barSeven = new ArrayList<>();

        for (int i = 0; i < valfour.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
            barFour.add(new BarEntry(i, valfour[i]));
            barFive.add(new BarEntry(i, valfour[i]));
            barSix.add(new BarEntry(i, valfour[i]));
            barSeven.add(new BarEntry(i, valfour[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Art");
        set1.setColor(Color.BLUE);
        BarDataSet set2 = new BarDataSet(barTwo, "Space");
        set2.setColor(Color.parseColor("#4E7FE7"));

        BarDataSet set3 = new BarDataSet(barThree, "Animal");
        set3.setColor(Color.GREEN);

        BarDataSet set4 = new BarDataSet(barFour, "Science");
        set4.setColor(Color.parseColor("#ffbb33"));


        BarData data = new BarData(set1,set2,set3,set4);
        barchart.setData(data);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set3.setHighlightEnabled(false);
        set4.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);
        set4.setDrawValues(false);

        /*
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
*/
        /*
        BarData data = new BarData(dataSets);
        float groupSpace = 0.5f;
        float barSpace = 0.01f;
        float barWidth = 0.1f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(14 - 1.1f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);
         */

        barchart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        int groupCount = 5;

        data.setBarWidth(0.1f);

        mChart.getXAxis().setAxisMinimum(1);


        mChart.setVisibleXRangeMaximum(5f);
        // mChart.groupBars(1, 0.5f, 0f);

        mChart.groupBars(1, 0.6f, 0f);
        mChart.setVisibleXRangeMaximum(6f);

        xAxis.setAxisMaximum(15 - 1.1f);
        xAxis.setXOffset(6f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        //mChart.setVisibleXRangeMaximum(6f);




        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);


    }

    public void GroupBarChartMonth(ArrayList<String> labels){


        mChart = (BarChart) findViewById(R.id.barchart);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        // empty labels so that the names are spread evenly
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);

        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(7, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);



        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        float[] valOne = {11, 14, 20, 25, 19};
        float[] valTwo = {14, 23, 15, 27, 20};
        float[] valThree = {12, 22, 14, 24, 30};
        float[] valfour = {16, 20, 13, 15, 28};


        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        ArrayList<BarEntry> barFour = new ArrayList<>();
        ArrayList<BarEntry> barFive= new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
            barFour.add(new BarEntry(i, valfour[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Art");
        set1.setColor(Color.BLUE);
        BarDataSet set2 = new BarDataSet(barTwo, "Space");
        set2.setColor(Color.parseColor("#4E7FE7"));

        BarDataSet set3 = new BarDataSet(barThree, "Animal");
        set3.setColor(Color.GREEN);

        BarDataSet set4 = new BarDataSet(barFour, "Science");
        set4.setColor(Color.parseColor("#ffbb33"));


        BarData data = new BarData(set1,set2,set3,set4);
        barchart.setData(data);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set3.setHighlightEnabled(false);
        set4.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);
        set4.setDrawValues(false);

        /*
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
*/
        /*
        BarData data = new BarData(dataSets);
        float groupSpace = 0.5f;
        float barSpace = 0.01f;
        float barWidth = 0.1f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(14 - 1.1f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);
         */

        barchart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        int groupCount = 5;

        data.setBarWidth(0.1f);

        mChart.getXAxis().setAxisMinimum(1);


        mChart.setVisibleXRangeMaximum(5f);
        // mChart.groupBars(1, 0.5f, 0f);

        mChart.groupBars(1, 0.6f, 0f);
        mChart.setVisibleXRangeMaximum(6f);

        xAxis.setAxisMaximum(15 - 1.1f);
        xAxis.setXOffset(6f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        //mChart.setVisibleXRangeMaximum(6f);




        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);

    }

    public void GroupBarChartYear(ArrayList<String> labels){
        mChart = (BarChart) findViewById(R.id.barchart);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        // empty labels so that the names are spread evenly
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);

        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(7, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);



        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        float[] valOne = {26,30,10,16, 22, 12, 27, 21, 14, 16, 19, 25};
        float[] valTwo = {25, 13, 13 ,10,12,14,28,27,14,16, 23, 29};
        float[] valThree = {10, 18,12,19,20,16,21,29,17,14,11,25};
        float[] valfour = {16, 20, 13, 15, 28,20,24,21,11, 22, 19, 12};


        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ArrayList<BarEntry> barThree = new ArrayList<>();
        ArrayList<BarEntry> barFour = new ArrayList<>();
        ArrayList<BarEntry> barFive= new ArrayList<>();
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));
            barThree.add(new BarEntry(i, valThree[i]));
            barFour.add(new BarEntry(i, valfour[i]));
        }

        BarDataSet set1 = new BarDataSet(barOne, "Art");
        set1.setColor(Color.BLUE);
        BarDataSet set2 = new BarDataSet(barTwo, "Space");
        set2.setColor(Color.parseColor("#4E7FE7"));

        BarDataSet set3 = new BarDataSet(barThree, "Animal");
        set3.setColor(Color.GREEN);

        BarDataSet set4 = new BarDataSet(barFour, "Science");
        set4.setColor(Color.parseColor("#ffbb33"));


        BarData data = new BarData(set1,set2,set3,set4);
        barchart.setData(data);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);
        set3.setHighlightEnabled(false);
        set4.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);
        set4.setDrawValues(false);

        /*
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
*/
        /*
        BarData data = new BarData(dataSets);
        float groupSpace = 0.5f;
        float barSpace = 0.01f;
        float barWidth = 0.1f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(14 - 1.1f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);
         */

        barchart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        int groupCount = 5;

        data.setBarWidth(0.1f);

        mChart.getXAxis().setAxisMinimum(1);


        mChart.setVisibleXRangeMaximum(5f);
       // mChart.groupBars(1, 0.5f, 0f);

        mChart.groupBars(1, 0.6f, 0f);
        mChart.setVisibleXRangeMaximum(6f);

        xAxis.setAxisMaximum(15 - 1.1f);
        xAxis.setXOffset(6f);
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        //mChart.setVisibleXRangeMaximum(6f);




        mChart.invalidate();
        mChart.setBackgroundColor(Color.WHITE);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        mChart.getLegend().setEnabled(true);

    }



    /*
    private void showBarChart() {
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        //input data
       // for (int i = 0; i < 5; i++) {
            valueList.add((double) 50);
            valueList.add((double) 60);
            valueList.add((double) 90);
            valueList.add((double) 200);
            valueList.add((double) 150);
        //}

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(new int[]{Color.parseColor("#4A92FC"),
                Color.parseColor("#ffbb33"), Color.parseColor("#4E7FE7"), Color.GRAY, Color.BLUE});
        BarData data = new BarData(barDataSet);
        barchart.setData(data);
        barchart.invalidate();
        //Changing the color of the bar
        //barDataSet.setColor(Color.parseColor("#304567"));
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);

        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(0f);
        barchart.setDoubleTapToZoomEnabled(false);

    }

    private void initBarChart() {
        //hiding the grey background of the chart, default false if not set
        barchart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barchart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barchart.setDrawBorders(false);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barchart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barchart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barchart.animateX(1000);
        barchart.getDescription().setEnabled(false);
        barchart.setHighlightPerTapEnabled(false);
        XAxis xAxis = barchart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barchart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barchart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barchart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);
        xAxis.setDrawLabels(false);
        barchart.setHighlightPerTapEnabled(false);
        barchart.getAxisLeft().setDrawLabels(true);
        barchart.getAxisRight().setDrawLabels(false);
        barchart.getXAxis().setDrawLabels(false);
        barchart.setTouchEnabled(false);
        barchart.getLegend().setEnabled(false);

    }
    */
}
