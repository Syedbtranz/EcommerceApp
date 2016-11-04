package com.btranz.ecommerceapp.utils;

/**
 * Created by all on 9/7/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HexagonImageView extends ImageView {


    public HexagonImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();


//        Bitmap roundBitmap =  getRoundedCroppedBitmap(bitmap, w);
        Bitmap roundBitmap =  getHexagonShape(bitmap);
        canvas.drawBitmap(roundBitmap, 0,0, null);

    }
    public Bitmap getHexagonShape(Bitmap scaleBitmapImage) {

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();
        float stdW = 200;
        float stdH = 200;
        float w3 =stdW / 2;
        float h2 = stdH / 2;

        float radius=stdH/2-10;
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = stdW/2;
        float centerY = stdH/2;
        path.moveTo(centerX, centerY + radius);
        path.lineTo(centerX - triangleHeight, centerY + radius/2);
        path.lineTo(centerX - triangleHeight, centerY - radius/2);
        path.lineTo(centerX, centerY - radius);
        path.lineTo(centerX + triangleHeight, centerY - radius/2);
        path.lineTo(centerX + triangleHeight, centerY + radius/2);
        path.moveTo(centerX, centerY + radius);

        //I made changes  here

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth((float)5);

        canvas.clipPath(path);

        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), mPaint);   //Added the Paint here
        return targetBitmap;
    }
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if(bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        Point point1_draw = new Point(75,0);
        Point point2_draw = new Point(0,50);
        Point point3_draw = new Point(0,100);
        Point point4_draw = new Point(75,150);
        Point point5_draw = new Point(150,100);
        Point point6_draw = new Point(150,50);

        Path path = new Path();
        path.moveTo(point1_draw.x,point1_draw.y);
        path.lineTo(point2_draw.x,point2_draw.y);
        path.lineTo(point3_draw.x,point3_draw.y);
        path.lineTo(point4_draw.x,point4_draw.y);
        path.lineTo(point5_draw.x,point5_draw.y);
        path.lineTo(point6_draw.x,point6_draw.y);

        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);


        return output;
    }

}