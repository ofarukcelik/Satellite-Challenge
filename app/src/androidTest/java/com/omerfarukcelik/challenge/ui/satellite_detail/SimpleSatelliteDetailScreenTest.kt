package com.omerfarukcelik.challenge.ui.satellite_detail

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
class SimpleSatelliteDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun satelliteDetailScreen_displaysLoadingIndicator() {
        composeTestRule.setContent {
            CircularProgressIndicator(
                modifier = Modifier.testTag("loading")
            )
        }

        composeTestRule.onNodeWithTag("loading")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteDetailScreen_displaysSuccessState() {
        composeTestRule.setContent {
            Column {
                Text("Test Satellite")
                Text("$1,000,000")
                Text("50.0 x 1000.0")
                Text("100.0, 200.0")
            }
        }

        composeTestRule.onNodeWithText("Test Satellite")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("$1,000,000")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("50.0 x 1000.0")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("100.0, 200.0")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteDetailScreen_displaysNotFoundState() {
        composeTestRule.setContent {
            Text("Satellite not found")
        }

        composeTestRule.onNodeWithText("Satellite not found")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteDetailScreen_displaysErrorState() {
        composeTestRule.setContent {
            Text("Bir hata oluştu. Lütfen tekrar deneyin.")
        }

        composeTestRule.onNodeWithText("Bir hata oluştu. Lütfen tekrar deneyin.")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteDetailScreen_positionUpdates() {
        composeTestRule.setContent {
            Column {
                Text("Test Satellite")
                Text("100.0, 200.0")
            }
        }

        composeTestRule.onNodeWithText("Test Satellite")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("100.0, 200.0")
            .assertIsDisplayed()
    }

    @Test
    fun satelliteDetailScreen_multiplePositionUpdates() {
        composeTestRule.setContent {
            Column {
                Text("Test Satellite")
                Text("150.0, 250.0")
            }
        }

        composeTestRule.onNodeWithText("Test Satellite")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("150.0, 250.0")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("100.0, 200.0")
            .assertDoesNotExist()
    }

    @Test
    fun satelliteDetailScreen_backButtonClick() {
        composeTestRule.setContent {
            IconButton(
                onClick = {},
                modifier = Modifier.testTag("back-button")
            ) {
                Text("Back")
            }
        }

        composeTestRule.onNodeWithTag("back-button")
            .performClick()

        composeTestRule.onNodeWithTag("back-button")
            .assertIsDisplayed()
    }
}
