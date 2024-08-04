package com.example.mpgpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainApp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] carOrgin = { "USA", "Europe", "Japan"};

    float[] mean = {5.477707f,195.318471f,104.869427f,2990.251592f,15.559236f,75.898089f,0.624204f,0.178344f,0.197452f};
    float[] std = {1.699788f,104.331589f,38.096214f,843.898596f,2.789230f,3.675642f,0.485101f,0.383413f,0.398712f};

    Interpreter interpreter;
    TextView resultBox;
    EditText cylinders, displacement, horsePower, weight, acceleration, modelYear;
    Button predictButton;
    Spinner orgin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        orgin = findViewById(R.id.spinner);
        orgin.setOnItemSelectedListener(this);

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        cylinders = findViewById(R.id.et_cylinders);
        displacement = findViewById(R.id.et_displacement);
        horsePower = findViewById(R.id.et_horsepower);
        weight = findViewById(R.id.et_weight);
        acceleration = findViewById(R.id.et_acceleration);
        modelYear = findViewById(R.id.et_modelYear);
        resultBox = findViewById(R.id.txt_results);
        predictButton = findViewById(R.id.btn_predict);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, carOrgin);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        orgin.setAdapter(arrayAdapter);



        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float[][] floats = new float[1][9];
                floats[0][0] = (Float.parseFloat(cylinders.getText().toString())-mean[0])/std[0];
                floats[0][1] = (Float.parseFloat(displacement.getText().toString())-mean[1])/std[1];
                floats[0][2] = (Float.parseFloat(horsePower.getText().toString())-mean[2])/std[2];
                floats[0][3] = (Float.parseFloat(weight.getText().toString()) -mean[3])/std[3];
                floats[0][4] = (Float.parseFloat(acceleration.getText().toString())-mean[4])/std[4];
                floats[0][5] = (Float.parseFloat(modelYear.getText().toString())-mean[5])/std[5];

                switch (orgin.getSelectedItemPosition())
                {
                    case 0:
                        floats[0][6] = (1 - mean[6])/std[6];
                        floats[0][7] = (0 - mean[7])/std[7];
                        floats[0][8] = (0 - mean[8])/std[8];
                        break;
                    case 1:
                        floats[0][6] = (0 - mean[6])/std[6];
                        floats[0][7] = (1 - mean[7])/std[7];
                        floats[0][8] = (0 - mean[8])/std[8];
                        break;
                    case 2:
                        floats[0][6] = (0 - mean[6])/std[6];
                        floats[0][7] = (0 - mean[7])/std[7];
                        floats[0][8] = (1 - mean[8])/std[8];
                        break;
                }

                float res = doInference(floats);

                float mpg = Math.round(res);
                float kmpl = (float) (0.4251 * mpg);

                resultBox.setText("Predicted MPG is: " + mpg +
                        "\n Predicted Kmpl is: " + kmpl);

            }
        });
    }


    public float doInference(float[][] input)
    {
        float[][] output = new float[1][1];

        interpreter.run(input,output);

        return output[0][0];
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("AutoMPG-Regressor.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
