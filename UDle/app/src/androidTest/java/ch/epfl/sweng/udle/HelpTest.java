package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageView;

import junit.framework.TestResult;

import org.hamcrest.Matcher;
import org.junit.Test;

import ch.epfl.sweng.udle.activities.HelpActivity.HelpActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Johan on 04.12.2015.
 */
public class HelpTest extends ActivityInstrumentationTestCase2<HelpActivity> {

    public HelpTest() {
        super(HelpActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testImageDisplay(){
        getActivity();
        onView(withId(R.id.help_page_imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testImageChange() {
        getActivity();
        onView(withId(R.id.help_page_imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.help_page_imageView)).perform(swipeRight());
        onView(withId(R.id.help_page_imageView)).perform(swipeLeft());
        onView(withId(R.id.help_page_imageView)).check(matches(withContentDescription(getActivity().getResources().getString(R.string.help_page) + "0")));
    }

}
