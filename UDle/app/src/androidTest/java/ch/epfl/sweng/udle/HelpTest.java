package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import org.junit.*;

import java.util.concurrent.locks.ReentrantLock;

import ch.epfl.sweng.udle.activities.HelpActivity.HelpActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Johan on 04.12.2015.
 */
public class HelpTest {

    public ActivityTestRule<HelpActivity> mActivityRule = new ActivityTestRule<>(HelpActivity.class, false);
    private HelpActivity activity;
    ReentrantLock sequential = new ReentrantLock();

    @Before
    public void setUp() throws Exception {
        sequential.lock();
        activity = mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        activity.finish();
        sequential.unlock();
    }

    @Test
    public void imageDisplay(){
        onView(withId(R.id.help_page_0_imageView)).check(matches(isDisplayed()));
    }

    /**
     * Test if a right-to-left swipe changes the image.
     *
     * This test will work only if the test activity contains at least two images.
     */
    @Test
    public void imageChange() {
        onView(withId(R.id.help_page_0_imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.help_page_0_imageView)).perform(swipeLeft());
        onView(withId(R.id.help_page_1_imageView)).check(matches(isDisplayed()));
    }

    /**
     * Test if the app does not crash when swiping to the far end of help pages.
     */
    @Test
    public void swipeToEnd() {
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
        onView(withId(R.id.help_pager)).perform(swipeLeft());
    }

}
