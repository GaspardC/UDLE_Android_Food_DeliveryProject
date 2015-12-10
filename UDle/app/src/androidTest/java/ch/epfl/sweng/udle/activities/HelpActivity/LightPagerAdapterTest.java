package ch.epfl.sweng.udle.activities.HelpActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.udle.R;

import static org.junit.Assert.*;

/**
 * Created by Johan on 10.12.2015.
 */
public class LightPagerAdapterTest {
    public ActivityTestRule<LightActivity> mActivityRule = new ActivityTestRule<>(LightActivity.class, false);
    private LightActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCount() throws Exception {

    }

    @Test
    public void testIsViewFromObject() throws Exception {

    }

    @Test
    public void testInstantiateItem() throws Exception {
        ViewPager pager = new ViewPager(activity.getApplicationContext());
        LightPagerAdapter adapter = new LightPagerAdapter(activity.getApplicationContext());
        pager.setAdapter(adapter);

    }

    @Test
    public void testDestroyItem() throws Exception {

    }
}

class LightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);
    }
}
