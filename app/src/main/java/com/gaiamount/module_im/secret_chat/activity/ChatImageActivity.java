package com.gaiamount.module_im.secret_chat.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;

public class ChatImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_image);
        String imagePath = getIntent().getStringExtra("imagePath");
        ImageView imageView= (ImageView) findViewById(R.id.ivShowImage);
        if(imagePath==null){
            return;
        }
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap img = BitmapFactory.decodeFile(imagePath,options);
//        imageView.setImageBitmap(bigBitmap(img,4.0f,4.0f));
        Glide.with(this).load((imagePath)).into(imageView);
    }

    public void ChatBacks(View view) {
        finish();
    }
}
