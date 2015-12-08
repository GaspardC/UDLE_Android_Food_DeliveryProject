package ch.epfl.sweng.udle;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.udle.activities.DeliveryActivity;
import ch.epfl.sweng.udle.activities.HelpActivity.HelpActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Johan on 07.12.2015.
 */
public class SlideMenuTest extends ActivityInstrumentationTestCase2<HelpActivity> {

    private HelpActivity activity;

    public SlideMenuTest() {
        super(HelpActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testVisibility(){
        onView(withId(R.id.content_frame)).check(matches(isDisplayed()));
        onView(withId(R.id.slideMenu_frame)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testOpening(){
        onView(withId(R.id.content_frame)).perform(swipeRight());
        onView(withId(R.id.slideMenu_frame)).check(matches(isDisplayed()));
    }
}
