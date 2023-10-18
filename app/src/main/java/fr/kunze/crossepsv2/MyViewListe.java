package fr.kunze.crossepsv2;

import android.annotation.SuppressLint;
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

public class MyViewListe extends View {
    public static final int QRcodeWidth = 250;
    public static final int QRcodeHeight = 250;
    Bitmap b1;
    Bitmap b2;
    String chaineNom1;
    String chaineNom2;
    Paint paint;
    Rect rect1;
    Rect rect2;
    TextPaint textPaint;
    Bitmap logo;

    public MyViewListe(Context context) {
        super(context);
    }

    public MyViewListe(Context context,String chaineNom1,String chaineNom2,Bitmap logo){
        super(context);
        this.chaineNom1=chaineNom1;
        this.chaineNom2=chaineNom2;
        this.logo=logo;

        if(this.chaineNom1.matches("")){

        }else {
            try {
                b1 = TextToImageEncode(this.chaineNom1);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        if(this.chaineNom2.matches("")){

        }else {
            try {
                b2 = TextToImageEncode(this.chaineNom2);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        this.paint = new Paint();
        this.textPaint = new TextPaint();
        this.rect1 = new Rect(10, 10, 530, 360);
        this.rect2 = new Rect(10, 380, 530, 740);
        this.paint.setStrokeWidth(3.0F);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(this.getResources().getColor(android.R.color.black));
        this.textPaint.setTextSize(18.0F);
        Typeface var8 = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        this.textPaint.setTypeface(var8);
        this.setMinimumWidth(600);
        this.setMinimumHeight(600);
}
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(this.rect1, this.paint);
        canvas.drawRect(this.rect2, this.paint);

        String[] var2 = this.chaineNom1.split(" ");
        byte var3 = 0;
        float var4 = 100.0F;

        int var5;
        for(var5 = 0; var5 < var2.length; ++var5) {
            canvas.drawText(var2[var5], 300, var4, this.textPaint);
            var4 += 20.0F;
        }

        var2 = this.chaineNom2.split(" ");
        var4 = 470.0F;

        for(var5 = var3; var5 < var2.length; ++var5) {
            canvas.drawText(var2[var5], 300.0F, var4, this.textPaint);
            var4 += 20.0F;
        }

        if(b1!=null) {
            canvas.drawBitmap(this.b1, 20.0F, 30.0F, this.paint);
        }
        Bitmap var6 = this.b2;
        if (var6 != null) {
            canvas.drawBitmap(var6, 20.0F, 400.0F, this.paint);
        }
        if(logo!=null){
            Bitmap resizedLogo = Bitmap.createScaledBitmap(logo, 50, 50, true);
            canvas.drawBitmap(resizedLogo, 470, 30, this.paint);
            canvas.drawBitmap(resizedLogo, 470, 400, this.paint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(this.getSuggestedMinimumWidth(),this.getMinimumHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeHeight, null
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

        bitmap.setPixels(pixels, 0, 250, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
