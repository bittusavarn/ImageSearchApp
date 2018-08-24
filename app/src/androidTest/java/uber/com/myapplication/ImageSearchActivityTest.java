package uber.com.myapplication;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test Case to check whether Activity Search the result
 * and also load result when scroll down. There is no  Test case for failure
 * That can be added later on
 */

@RunWith(AndroidJUnit4.class)
public class ImageSearchActivityTest  {

   @Rule
    public ActivityTestRule<ImageSearchActivity> activityTestRule = new ActivityTestRule<>(ImageSearchActivity.class);

    /**
     * Test to check SearchWorks by entering Text kitten
     */
    @Test
     public void ensureSearchWorks(){
        //Empty Text check
        Espresso.onView(ViewMatchers.withId(R.id.searchBtn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.serachHeadr)).check(ViewAssertions.matches(ViewMatchers.withText("Please Enter text to Search")));
        //Perform Search with text kitten
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText)).perform(ViewActions.typeText("kitten"));
        Espresso.onView(ViewMatchers.withId(R.id.searchBtn)).perform(ViewActions.click());
        //wait for result
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {

        }
        //check If result is there
        Espresso.onView(ViewMatchers.withId(R.id.serachHeadr)).check(ViewAssertions.matches(ViewMatchers.withText("Search result for kitten")));
        Espresso.onView(ViewMatchers.withId(R.id.imageGrid)).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {

                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                Assert.assertThat(adapter.getItemCount(), Matchers.greaterThan(5));
            }
        });
     }


     @Test
      public void ensureScrollingLoadData(){
         Espresso.onView(ViewMatchers.withId(R.id.searchEditText)).perform(ViewActions.typeText("kitten"));
         Espresso.onView(ViewMatchers.withId(R.id.searchBtn)).perform(ViewActions.click());

         try {
             Thread.sleep(5000);
         } catch (InterruptedException ex) {

         }

         Espresso.onView(ViewMatchers.withId(R.id.imageGrid)).perform(RecyclerViewActions.scrollToPosition(96));
         try {
             Thread.sleep(5000);
         } catch (InterruptedException ex) {

         }

         Espresso.onView(ViewMatchers.withId(R.id.imageGrid)).check(new ViewAssertion() {
             @Override
             public void check(View view, NoMatchingViewException noViewFoundException) {

                 if (noViewFoundException != null) {
                     throw noViewFoundException;
                 }
                 RecyclerView recyclerView = (RecyclerView) view;
                 RecyclerView.Adapter adapter = recyclerView.getAdapter();
                 Assert.assertThat(adapter.getItemCount(), Matchers.greaterThan(105));
             }
         });
    }



}
