package ch.epfl.sweng.udle.tools;

import android.graphics.BitmapFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static ch.epfl.sweng.udle.tools.ImageLoader.calculateInSampleSize;
import static org.junit.Assert.*;

/**
 * Created by Johan on 10.12.2015.
 */
public class ImageLoaderTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCalculateInSampleSize() throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int imgHeight, imgWidth, reqHeight, reqWidth, sampleSize;
        options.outHeight = imgHeight = Math.round((float) Math.random() * 1000) + 500;
        options.outWidth = imgWidth = Math.round((float) Math.random() * 1000) + 500;
        reqHeight = Math.round((float) Math.random() * 400) + 50;
        reqWidth = Math.round((float) Math.random() * 400) + 50;
        sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        assertTrue("Image height fits requirement", reqHeight <= imgHeight / sampleSize);
        assertTrue("Image width fits requirement", reqWidth <= imgWidth / sampleSize);
        assertTrue("Image height or width cannot be reduced", reqHeight > imgHeight / (2 * sampleSize) || reqWidth > imgWidth / (2 * sampleSize));
    }
}