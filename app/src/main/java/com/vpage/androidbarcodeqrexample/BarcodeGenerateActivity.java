package com.vpage.androidbarcodeqrexample;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.onbarcode.barcode.android.AndroidColor;
import com.onbarcode.barcode.android.AndroidFont;
import com.onbarcode.barcode.android.IBarcode;
import com.onbarcode.barcode.android.UPCA;

public class BarcodeGenerateActivity extends Activity {

    private static final String TAG = BarcodeGenerateActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodegenerate);

       /* ImageView imageView = (ImageView)findViewById(R.id.barcodeImage);
        TextView textView = (TextView)findViewById(R.id.barcodeDigit);

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        // barcode data
        Random random = new Random();
        String barcode_data = new String();
        for(int i=0 ; i < 6 ; i++) {
            barcode_data += random.nextInt(10);
        }

        try {

            Bitmap bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Log.e(TAG,e.getMessage());
        }

        textView.setText(barcode_data);*/

        LinearLayout linearLayout =(LinearLayout)findViewById(R.id.barcodeView);
        linearLayout.setVisibility(View.VISIBLE);
        AndroidBarcodeView view = new AndroidBarcodeView(this);
        linearLayout.addView(view);

    }

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException e) {
            Log.e(TAG,e.getMessage());
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
