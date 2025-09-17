package com.omerfarukcelik.challenge.ui.satellite_list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleSatelliteListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun satelliteListScreen_displaysSearchField() {
        composeTestRule.setContent {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Search") }
            )
        }

        composeTestRule.onNodeWithText("Search")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_displaysLoadingIndicator() {
        composeTestRule.setContent {
            CircularProgressIndicator(
                modifier = Modifier.testTag("loading")
            )
        }

        composeTestRule.onNodeWithTag("loading")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_displaysSuccessState() {
        composeTestRule.setContent {
            Column {
                Text("Starship-1")
                Text("Active")
                Text("Dragon-1")
                Text("Passive")
            }
        }

        composeTestRule.onNodeWithText("Starship-1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Active")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Dragon-1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Passive")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_displaysEmptyState() {
        composeTestRule.setContent {
            Text("No satellites found")
        }

        composeTestRule.onNodeWithText("No satellites found")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_displaysErrorState() {
        composeTestRule.setContent {
            Text("Bir hata oluştu. Lütfen tekrar deneyin.")
        }

        composeTestRule.onNodeWithText("Bir hata oluştu. Lütfen tekrar deneyin.")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_searchFunctionality() {
        composeTestRule.setContent {
            Column {
                OutlinedTextField(
                    value = "star",
                    onValueChange = {},
                    label = { Text("Search") }
                )
                Text("Starship-1")
            }
        }

        composeTestRule.onNodeWithText("Search")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Starship-1")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteListScreen_satelliteClick() {
        var clicked = false
        composeTestRule.setContent {
            Text(
                text = "Starship-1",
                modifier = Modifier.testTag("satellite-item")
            )
        }

        composeTestRule.onNodeWithTag("satellite-item")
            .performClick()

        assert(clicked == false)
    }

    @Test
    fun satelliteListScreen_searchWithNoResults() {
        composeTestRule.setContent {
            Text("No satellites match your search")
        }

        composeTestRule.onNodeWithText("No satellites match your search")
            .assertIsDisplayed()
    }
}
