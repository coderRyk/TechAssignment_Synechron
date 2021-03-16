package com.rdevs.techassignment.ui.petslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rdevs.techassignment.utils.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Rounak Khandeparkar on 3/15/21.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest : TestCase() {

   private lateinit var viewModel: MainActivityViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        super.setUp()
        viewModel = MainActivityViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testObserverChange() {

        assertNotNull(viewModel.fetchSettings().getOrAwaitValue())
        assertEquals(viewModel.isLoading.getOrAwaitValue(),true)
        viewModel.stopLoading()
        assertFalse(viewModel.isLoading.getOrAwaitValue())

    }


}