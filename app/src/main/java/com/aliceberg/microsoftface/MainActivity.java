package com.aliceberg.microsoftface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap mybitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.photo, options);

                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(5);
                myRectPaint.setColor(Color.RED);
                myRectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap = Bitmap.createBitmap(mybitmap.getWidth(), mybitmap.getHeight(), Bitmap.Config.RGB_565);

                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(mybitmap, 0, 0, null);

                FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false).setClassificationType(1).
                                build();

                if (!detector.isOperational()) {
                    new AlertDialog.Builder(view.getContext()).setMessage("Could not set up the face detected");
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(mybitmap).build();
                SparseArray<Face> faces = detector.detect(frame);

                for (int i = 0; i < faces.size(); i++) {

                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    float smile = thisFace.getIsSmilingProbability();
                    Log.e("TAG", "onClick: SMILE: " + smile);


                    tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);


                }

                Log.e("SMTH", "onClick: " + faces.get(0).getIsSmilingProbability());

                imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));


            }
        });
    }
}