package com.gaiamount.widgets.improved;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by luxiansheng on 16/6/28.
 * 小组用户头像控件
 * 1.圆形
 * 2.右上角有一个小圆（如果是组长，显示为红色，如果是管理员，显示为绿色，如果是普通组员，则不显示）
 */
public class GroupUserImageView extends ImageView {
    /**
     * 组长
     */
    public static int MONITOR = 1;
    /**
     * 管理员
     */
    public static int ADMIN = 2;
    /**
     * 组员
     */
    public static int NORMAL = 3;

    private int mRadius;

    private Paint mPaint;

    private Rect mRect;

    private Matrix mMatrix;
    private int mDiameter;



    private int memeberKind = MONITOR;

    public GroupUserImageView(Context context) {
        super(context);
        init();
    }

    public GroupUserImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupUserImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    public GroupUserImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRect = new Rect();

        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为绘制的形状为圆形，所以view得宽高应该一致

        //直径，取小值
        mDiameter = Math.min(getMeasuredWidth(),getMeasuredHeight());
        //radius
        mRadius = mDiameter /2;
        //设置
        setMeasuredDimension(mDiameter, mDiameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (getDrawable()==null) {
            return;
        }
        //转bitmap
        Bitmap bitmap = drawableToBitmap(getDrawable());


        //设置bitmapShader
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //设置shader的p的matrix
        int bmSize = Math.min(bitmap.getWidth(),bitmap.getHeight());
        float scale = mDiameter *1.0f / bmSize;
        mMatrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(mMatrix);
        //设置bitmapPaint的shader
        mPaint.setShader(bitmapShader);

        Paint paint = new Paint();


//        ovalShape.resize(60,60);
//        ovalShape.draw(canvas,paint);
        //计算小圆点的位置
        double x = mRadius * Math.sqrt(0.5);
        float locX = (float) (mRadius+x);
        float locY = (float) (mRadius-x);
//        canvas.drawOval(30,40,40,50,paint);


        //绘制圆形
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        //绘制右上角小圆点
        if (memeberKind==MONITOR) {//de385e
            paint.setColor(Color.rgb(0xde,38,0x5e));
            canvas.drawCircle(locX,locY,16,paint);
        }else if (memeberKind==ADMIN) {
            paint.setColor(Color.rgb(0x53,0xee,0x38));
            canvas.drawCircle(locX,locY,16,paint);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable==null) {
            throw new RuntimeException("drawable为空");
        }
        if(drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        //新建一个bitmap
            //准备宽高geti
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //将drawable填充到bitmap中
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas);

        return bitmap;
    }

    public void setMemberKind(int kind) {
        if (kind<1&&kind>3) {
            throw new RuntimeException("kind的类型不正确");
        }
        memeberKind = kind;
    }

}
