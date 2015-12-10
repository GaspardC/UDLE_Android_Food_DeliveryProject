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

import java.util.concurrent.locks.ReentrantLock;

import ch.epfl.sweng.udle.R;

import static org.junit.Assert.*;

/**
 * Created by Johan on 10.12.2015.
 */
public class LightPagerAdapterTest {
    public ActivityTestRule<LightActivity> mActivityRule = new ActivityTestRule<>(LightActivity.class, false);
    private LightActivity activity;
    ViewGroup pager;
    LightPagerAdapter adapter;
    int before;
    Object newView;
    ReentrantLock sequential = new ReentrantLock();

    @Before
    public void setUp() throws Exception {
        sequential.lock();
        activity = mActivityRule.launchActivity(new Intent());
        pager = new ViewPager(activity.getApplicationContext());
        adapter = new LightPagerAdapter(activity.getApplicationContext());
        before = pager.getChildCount();
        newView = adapter.instantiateItem(pager, 0);
    }

    @After
    public void tearDown() throws Exception {
        pager.removeAllViews();
        sequential.unlock();
    }

    @Test
    public void testIsViewFromObject() throws Exception {
        assertTrue(adapter.isViewFromObject(pager.getChildAt(0), newView));
    }

    @Test
    public void testInstantiateItem() throws Exception {
        adapter.instantiateItem(pager, 0);
        assertEquals(before + 1, pager.getChildCount());
    }

    @Test
    public void testDestroyItem() throws Exception {
        adapter.destroyItem(pager, 0, newView);
        assertEquals(pager.getChildCount(), before);
    }
}

class LightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);
    }
}
