package com.example.pop2.myapplication10;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//package com.example.sairamkrishna.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button b, b2, b3, b4, b5, b6;
    ImageView im;

    myImage img;

    private Bitmap operation, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);

        im = (ImageView) findViewById(R.id.imageView);
        BitmapDrawable abmp = (BitmapDrawable) im.getDrawable();
        image = abmp.getBitmap();

        img = new myImage(image);

        SeekBar brightness, darkness, outline, window_norm;


        brightness  = (SeekBar) findViewById(R.id.seekBar1);
        darkness    = (SeekBar) findViewById(R.id.seekBar2);
        outline     = (SeekBar) findViewById(R.id.seekBar3);
        window_norm = (SeekBar) findViewById(R.id.seekBar4);


        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Value = " + progress, Toast.LENGTH_SHORT).show();
                make_brighter(progress);
            }
        });

        darkness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Value = " + progress, Toast.LENGTH_SHORT).show();
                make_darker(progress);
            }
        });

        outline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i * 2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Value = " + progress, Toast.LENGTH_SHORT).show();
                mark_outlines(progress);
            }
        });

        window_norm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i * 2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Value = " + progress, Toast.LENGTH_SHORT).show();
                make_WindowNorm(progress);
            }
        });


    }


    public void make_brighter(int val){
        int width = img.src.getWidth(), height = img.src.getHeight();
        operation = Bitmap.createBitmap(width, height, img.src.getConfig());

        for (int i = 0 ; i < width ; i++){
            for (int j = 0; j < height ; j++) {
                int p = img.src.getPixel(i, j);
                int r = Math.min(Color.red(p) + val, 255);
                int g = Math.min(Color.green(p) + val, 255);
                int b = Math.min(Color.blue(p) + val, 255);

                operation.setPixel(i, j, Color.rgb(r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }


    public void make_darker(int val){
        int width = img.src.getWidth(), height = img.src.getHeight();
        operation = Bitmap.createBitmap(width, height, img.src.getConfig());

        for (int i = 0 ; i < width ; i++){
            for (int j = 0; j < height ; j++) {
                int p = img.src.getPixel(i, j);
                int r = Math.max(Color.red(p) - val, 0);
                int g = Math.max(Color.green(p) - val, 0);
                int b = Math.max(Color.blue(p) - val, 0);

                operation.setPixel(i, j, Color.rgb(r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }


    public void mark_outlines(int val) {
        img.Outlines();

        int width = img.src.getWidth(), height = img.src.getHeight();

        operation = Bitmap.createBitmap(width, height, img.src.getConfig());

        for (int i = 0 ; i < width ; i++){
            for(int j = 0 ; j < height ; j++){
                if (img.outline[i][j] > val) operation.setPixel(i, j, Color.rgb(0, 0, 0));
                else operation.setPixel(i, j, Color.rgb(255, 255, 255));
            }
        }

        im.setImageBitmap(operation);

    }


    public void make_WindowNorm(int val) {
        int width = img.src.getWidth(), height = img.src.getHeight();

        operation = Bitmap.createBitmap(width, height, img.src.getConfig());

        int k = val;
        for (int i = 0 ; i < width ; i = i + k) {

            for (int j = 0 ; j < height ; j = j + k) {

                int lim_i = Math.min(i + k, width), lim_j = Math.min(j + k, height);
                int min_val = 256, max_val = 0;

                for (int l = i ; l < lim_i ; l++) {
                    for (int m = j ; m < lim_j ; m++) {

                        int p = img.src.getPixel(l, m);
                        int r = Color.red(p);
                        int g = Color.green(p);
                        int b = Color.blue(p);
                        int avg = (r + g + b) / 3;

                        min_val = Math.min(min_val, avg);
                        max_val = Math.max(max_val, avg);

                    }
                }

                for (int l = i ; l < lim_i ; l++) {
                    for (int m = j ; m < lim_j ; m++) {

                        int p = img.src.getPixel(l, m);
                        int r = Color.red(p);
                        int g = Color.green(p);
                        int b = Color.blue(p);
                        int avg = (r + g + b) / 3;

                        if(max_val != min_val) r = (avg - min_val) * 255 / (max_val - min_val);
                        else r = 128;

                        operation.setPixel(l, m, Color.rgb(r, r, r));

                    }
                }

            }

        }

        im.setImageBitmap(operation);

    }


    public void gray(View view) {
        im.setImageBitmap(img.gray_image);
    }


    public void original(View view) {
        im.setImageBitmap(image);
    }


    public void hist_normalise(View view){
        operation= Bitmap.createBitmap(img.src.getWidth(),img.src.getHeight(),img.src.getConfig());
        int[] hist=new int[256];
        for(int i=0;i<img.src.getWidth();i++)
        { for(int j=0;j<img.src.getHeight();j++)

        { int t=img.src.getPixel(i,j);
            int r=Color.red(t);
            int b=Color.blue(t);
            int g=Color.green(t);
            int alpha=Color.alpha(t);
            int avg=(r+b+g)/3;
            Log.d(""+Integer.toString((avg)),"aaa");
            hist[avg]+=1;


        }


        }
        int [] p=new int[256];
        int total_pixel=0;
        for(int i=0;i<256;i++)
            total_pixel+=hist[i];
        p[0]=hist[0];
        for(int i=1;i<255;i++)

        {  p[i]=hist[i]+p[i-1];


        }

        //////////assume output Bitmap opration is out
        for(int i=0;i<img.src.getWidth();i++)
        { for(int j=0;j<img.src.getHeight();j++)
        {  int x=img.src.getPixel(i,j);
            int r=Color.red(x);
            int alpha=Color.alpha(x);
            int new_r=((p[r]*255)/total_pixel);


            operation.setPixel(i, j, Color.argb(alpha,new_r,new_r,new_r));

        }
            // p[i]=(int)((p[i]*3)/total_pixel);

        }

        im.setImageBitmap(operation);
    }


    public void simple_thresholding(View view){
        operation= Bitmap.createBitmap(img.src.getWidth(), img.src.getHeight(),img.src.getConfig());

        for(int i=0; i<img.src.getWidth(); i++){
            for(int j=0; j<img.src.getHeight(); j++){
                int p = img.src.getPixel(i, j);
                int r = Color.red(p);
                int g,b;

                if(r<128)
                { r=255;g=255;b=255;

                }
                else
                {
                    r=0;g=0;b=0;
                }

                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }


    public void box_blur(View view){
        operation= Bitmap.createBitmap(img.src.getWidth(), img.src.getHeight(),img.src.getConfig());

        for(int i=0; i<img.src.getWidth(); i++){
            for(int j=0; j<img.src.getHeight(); j++){
                int p = img.src.getPixel(i, j);
                int sum1=0;int sum2=0;int sum3=0;
                for(int l=i-1;l<=i+1;l++)
                {
                    if(l>=0 &&l<img.src.getWidth())
                {
                    for(int m=j-1;m<=j+1;m++)
                    {
                        if(m>=0&&m<img.src.getHeight())
                        {
                            sum1=sum1+Color.red(img.src.getPixel(l, m));
                            sum2=sum2+Color.blue(img.src.getPixel(l, m));
                            sum3=sum3+Color.green(img.src.getPixel(l, m));

                        }
                    }
                }

                }
                sum1=sum1+9*Color.red(img.src.getPixel(i, j));sum2=sum2+9*Color.blue(img.src.getPixel(i, j));
                sum3=sum3+9*Color.green(img.src.getPixel(i, j));
                operation.setPixel(i, j, Color.argb(Color.alpha(p), (int)(sum1/9), (int)(sum2/9), (int)(sum3/9)));


            }
        }
        im.setImageBitmap(operation);
    }






    public void red(View view) {
        operation = Bitmap.createBitmap(img.src.getWidth(),img.src.getHeight(),img.src.getConfig());

        for(int i=0; i<img.src.getWidth(); i++){
            for(int j=0; j<img.src.getHeight(); j++){
                int p = img.src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r + 150;
                g =  0;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }

    public void green(View view){
        operation = Bitmap.createBitmap(img.src.getWidth(),img.src.getHeight(), img.src.getConfig());

        for(int i=0;i <img.src.getWidth(); i++){
            for(int j=0; j<img.src.getHeight(); j++){
                int p = img.src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  g+150;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }

    public void blue(View view){
        operation = Bitmap.createBitmap(img.src.getWidth(),img.src.getHeight(), img.src.getConfig());

        for(int i=0; i<img.src.getWidth(); i++){
            for(int j=0; j<img.src.getHeight(); j++){
                int p = img.src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  0;
                b =  b+150;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }
}