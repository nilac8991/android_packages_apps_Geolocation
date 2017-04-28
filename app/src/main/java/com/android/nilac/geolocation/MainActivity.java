package com.android.nilac.geolocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GeoTask.Geo {
    EditText edttxt_from,edttxt_to;
    Button btn_get;
    TextView tv_result1,tv_result2;

    private RadioGroup driving_method=null;
    private RadioButton driving_method_car=null;
    private RadioButton driving_method_bike=null;
    private RadioButton driving_method_walk=null;

    private String google_api_transport_choice=null;
    private String google_api_starting_string=null;
    private String google_api_arrival_string=null;

    private EditText starting_filed1=null;
    private EditText starting_filed2=null;
    private EditText starting_filed3=null;

    private EditText destination_field1=null;
    private EditText destination_field2=null;
    private EditText destination_field3=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.android.nilac.geolocation.R.layout.activity_main);

        btn_get= (Button) findViewById(com.android.nilac.geolocation.R.id.button_get);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreGetValues();
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + google_api_starting_string + "&destinations=" + google_api_arrival_string + "&mode=" + google_api_transport_choice + "&language=ro-RO&key=AIzaSyDbXLHlv9NZvgvC4nr2CVe-wb-j4qvKvoM";
                new GeoTask(MainActivity.this).execute(url);
            }
        });

    }

    public void onPreGetValues(){

        //Driving transport part
        driving_method = (RadioGroup) findViewById(com.android.nilac.geolocation.R.id.transport_method_choice);
        driving_method_car = (RadioButton) findViewById(R.id.transport_method_car);
        driving_method_bike = (RadioButton) findViewById(R.id.transport_method_bike);
        driving_method_walk = (RadioButton) findViewById(R.id.transport_method_walk);

        if(driving_method_walk.isChecked()==true)
            google_api_transport_choice="walking";
        else if(driving_method_bike.isChecked()==true)
            google_api_transport_choice="bicycling";
        else if(driving_method_car.isChecked()==true)
            google_api_transport_choice="driving";
        else
            google_api_transport_choice="404";

        //Starting point
        starting_filed1 = (EditText) findViewById(R.id.starting_filed1);
        starting_filed2 = (EditText) findViewById(R.id.starting_filed2);
        starting_filed3 = (EditText) findViewById(R.id.starting_field3);

        //Check first if the user specified the Road only but not the Country and City as well
        if(starting_filed1.getText().toString()=="" || starting_filed2.getText().toString()=="" || starting_filed3.getText().toString()=="") {
            starting_filed3.setError("Country is required");
            starting_filed2.setError("City is required!");
            starting_filed1.setError("Road is required!");
        }
        else{
                google_api_starting_string=starting_filed1.getText().toString();
                google_api_starting_string=google_api_starting_string+","+starting_filed2.getText().toString();
                google_api_starting_string=google_api_starting_string+","+starting_filed3.getText().toString();
        }

        //Arrival point
        destination_field1 = (EditText) findViewById(R.id.arrival_filed1);
        destination_field2 = (EditText) findViewById(R.id.arrival_filed2);
        destination_field3 = (EditText) findViewById(R.id.arrival_field3);

        //Check first if the user specified the Road only but not the Country and City as well
        if(destination_field1.getText().toString()=="" || destination_field2.getText().toString()=="" || destination_field3.getText().toString()=="") {
            destination_field3.setError("Country is required");
            destination_field2.setError("City is required!");
            destination_field1.setError("Road is required!");
        }
        else{
            google_api_arrival_string=destination_field1.getText().toString();
            google_api_arrival_string=google_api_arrival_string+","+destination_field2.getText().toString();
            google_api_arrival_string=google_api_arrival_string+","+destination_field3.getText().toString();
        }
        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2= (TextView) findViewById(R.id.textView_result2);
    }

    @Override
    public void setDouble(String result) {
        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        int dist=Integer.parseInt(res[1])/1000;
        tv_result1.setText("Estimated Arrival Time: " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result2.setText("Distance: " + dist + " kilometers");

    }
}
