package ch.epfl.sweng.udle;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.*;

import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Johan on 07.12.2015.
 */
public class SlideMenuTest {

    public ActivityTestRule<LightActivity> mActivityRule = new ActivityTestRule<>(LightActivity.class, false);
    private LightActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void visibility(){
        onView(withId(R.id.help_page_imageView)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.slideMenu_frame)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void swipeOpeningClosing(){
        onView(withId(R.id.help_page_imageView)).perform(swipeRight());
        onView(withId(R.id.slideMenu_frame)).check(matches(isDisplayed()));
        onView(withId(R.id.help_page_imageView)).perform(swipeLeft());
        onView(withId(R.id.help_page_imageView)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.slideMenu_frame)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }
}

class LightActivity extends SlideMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_page);
    }
}