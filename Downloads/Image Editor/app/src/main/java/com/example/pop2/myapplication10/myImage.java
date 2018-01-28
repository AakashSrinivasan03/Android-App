package com.example.pop2.myapplication10;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * Created by raghavan on 6/10/17.
 */

public class myImage {
    public Bitmap src, gray_image;

    public boolean outlines_computed = false;

    public int[] hist;
    public int[][] outline;
    int width, height;

    public myImage(Bitmap img) {
        width = img.getWidth(); height = img.getHeight();
        src = img;
        gray_image = Bitmap.createBitmap(width, height, img.getConfig());

        gray_image = getGrayImage();

        getHist();

    }

    Bitmap getGrayImage() {
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        for (int i = 0 ; i < width ; ++i) {
            for (int j = 0 ; j < height ; ++j) {
                int p = src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) (r + g + b) / 3;
                result.setPixel(i, j, Color.argb(Color.alpha(p), r, r, r));
            }
        }

        return result;
    }

    void getHist() {
        hist = new int[256];

        for (int i = 0 ; i < width ; ++i) {
            for (int j = 0; j < height; ++j) {
                int p = src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                hist[(r + b + g) / 3]++;
            }
        }

    }


    int[][][] convolve(int[][] kernel) {
        final int size = kernel.length;  // Should be odd
        int[][][] result = new int[width][height][3];

        for (int i = 0 ; i < width - size + 1 ; ++i) {
            for (int j = 0 ; j < height - size + 1 ; ++j) {
                int[][] pixels = new int[size][size];

                for (int x = 0 ; x < size ; ++x) {
                    for (int y  = 0 ; y < size ; ++y) {
                        pixels[x][y] = src.getPixel(i + x, j + y);
                    }
                }

                int sumR = 0, sumG = 0, sumB = 0;
                for (int x = 0 ; x < size ; ++x) {
                    for (int y = 0 ; y < size ; ++y) {
                        sumR += (Color.red(pixels[x][y]) * kernel[x][y]);
                        sumG += (Color.green(pixels[x][y]) * kernel[x][y]);
                        sumB += (Color.blue(pixels[x][y]) * kernel[x][y]);
                    }
                }

                result[i + size / 2][j + size / 2][0] = sumR;
                result[i + size / 2][j + size / 2][1] = sumG;
                result[i + size / 2][j + size / 2][2] = sumB;

            }
        }

        return result;
    }


    void Outlines() {

        if (outlines_computed) return;

        outlines_computed = true;

        int[][] SobelX = new int[3][3];
        int[][] SobelY = new int[3][3];

        SobelX[0][0] = -1; SobelX[0][1] = -2; SobelX[0][2] = -1;
        SobelX[1][0] =  0;  SobelX[1][1] = 0;  SobelX[1][2] = 0;
        SobelX[2][0] =  1;  SobelX[2][1] = 2;  SobelX[2][2] = 1;

        SobelY[0][0] = -1; SobelY[0][1] = 0; SobelY[0][2] = 1;
        SobelY[1][0] = -2; SobelY[1][1] = 0; SobelY[1][2] = 2;
        SobelY[2][0] = -1; SobelY[2][1] = 0; SobelY[2][2] = 1;

        int[][][] gradX = convolve(SobelX), gradY = convolve(SobelY);

        int width = src.getWidth(), height = src.getHeight();

        outline = new int[width][height];

        for (int i = 1 ; i < width - 1 ; ++i) {
            for (int j = 1 ; j < height - 1 ; ++j) {
                int gX, gY, max_val = 0;

                gX = gradX[i][j][0]; gY = gradY[i][j][0];
                max_val = Math.max(max_val, (gX * gX + gY * gY));

                gX = gradX[i][j][1]; gY = gradY[i][j][1];
                max_val = Math.max(max_val, (gX * gX + gY * gY));

                gX = gradX[i][j][2]; gY = gradY[i][j][2];
                max_val = Math.max(max_val, (gX * gX + gY * gY));

                outline[i][j] =  max_val;

            }
        }


    }

}
