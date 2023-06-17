package com.example.tabletappdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawingView extends View {
    private Bitmap drawingBitmap;
    private Canvas drawingCanvas;
    private Path path;
    private Paint paint;
    private boolean isDrawingMode;
    private static final String TAG = "DrawingView";

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        isDrawingMode = true;
    }

    private void initializeDrawing() {
        drawingBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        drawingCanvas = new Canvas(drawingBitmap);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        initializeDrawing();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(drawingBitmap, 0, 0, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isDrawingMode) {
                    startPath(x, y);
                } else {
                    erasePath(x, y);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDrawingMode) {
                    updatePath(x, y);
                } else {
                    erasePath(x, y);
                }
                return true;
            case MotionEvent.ACTION_UP:
                stopPath();
                return true;
        }
        return false;
    }

    public void setDrawingMode(boolean drawingMode) {
        isDrawingMode = drawingMode;
    }

    public void setDrawingColor(int color) {
        paint.setColor(color);
    }

    private void startPath(float x, float y) {
        path.moveTo(x, y);
        drawingCanvas.drawPath(path, paint);
        invalidate();
    }

    private void updatePath(float x, float y) {
        path.lineTo(x, y);
        drawingCanvas.drawPath(path, paint);
        invalidate();
    }

    private void stopPath() {
        invalidate();
    }

    private void erasePath(float x, float y) {
        Paint eraserPaint = new Paint();
        eraserPaint.setAntiAlias(true);
        eraserPaint.setStrokeWidth(20); // Adjust the eraser size as needed
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setColor(Color.TRANSPARENT);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        drawingCanvas.drawCircle(x, y, 10, eraserPaint); // Adjust the circle size as needed
        invalidate();
    }
}
