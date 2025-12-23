package com.example.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.frontend.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EncadrantActivityTest {

    @Rule
    public ActivityScenarioRule<EncadrantActivity> activityRule =
            new ActivityScenarioRule<>(EncadrantActivity.class);

    /**
     * Vérifie que l’activité Encadrant s’ouvre correctement
     */
    @Test
    public void encadrantActivity_isDisplayed() {
        onView(withId(R.id.tabLayout))
                .check(matches(isDisplayed()));

        onView(withId(R.id.viewPager))
                .check(matches(isDisplayed()));
    }

    /**
     * Vérifie que l’onglet "Étudiants disponibles" est visible
     */
    @Test
    public void tabEtudiantsDisponibles_isVisible() {
        onView(withText("Étudiants disponibles"))
                .check(matches(isDisplayed()));
    }

    /**
     * Vérifie que les composants du fragment sont affichés
     */
    @Test
    public void etudiantsDisponiblesFragment_uiElementsDisplayed() {

        // Spinner filière
        onView(withId(R.id.spinnerFiliere))
                .check(matches(isDisplayed()));

        // RecyclerView étudiants
        onView(withId(R.id.recyclerEtudiants))
                .check(matches(isDisplayed()));
    }
}
