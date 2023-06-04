package fr.kunze.crossepsv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MyView extends View {

    String chaine;
    Bitmap bitmap;
    Paint paint;
    TextPaint textPaint;
    Rect rect;
    public final static int QRcodeWidth = 300 ;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context,String chaine) throws WriterException {
        super(context);
        this.chaine=chaine;

        bitmap=TextToImageEncode(this.chaine);
        paint=new Paint();
        textPaint=new TextPaint();
        rect=new Rect(10,10,500,360);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(android.R.color.black));
        textPaint.setTextSize(18.0F);
        textPaint.setStrokeWidth(5);
        textPaint.setColor(getResources().getColor(android.R.color.black));
        Typeface var5 = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        textPaint.setTypeface(var5);
        setMinimumWidth(600);
        setMinimumHeight(600);
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String[] var2 = chaine.split(" ");

        float var4 = 80;

        int var5;
        for(var5 = 0; var5 < var2.length; ++var5) {
            canvas.drawText(var2[var5], 340, var4, textPaint);
            var4 += 20.0F;
        }
        canvas.drawRect(rect,paint);

        canvas.drawBitmap(bitmap,20,20,paint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(200,200);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

       private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(android.R.color.black):getResources().getColor(android.R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 300, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
