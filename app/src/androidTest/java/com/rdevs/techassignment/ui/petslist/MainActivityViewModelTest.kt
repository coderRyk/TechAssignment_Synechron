package com.rdevs.techassignment.ui.petslist

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Created by Rounak Khandeparkar on 3/15/21.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest : TestCase() {

   //@Mock
   private lateinit var viewModel: MainActivityViewModel

   // @Mock
   private lateinit var isLoading: LiveData<Boolean>

   // @Mock
   private lateinit var observer: Observer<in Boolean>

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewModel(ApplicationProvider.getApplicationContext())
        isLoading = viewModel.isLoading
        observer = Observer {
            Log.d("observer test", it.toString())
        }
    }

    @Test
    fun testObserverChange() {
        assertNotNull(viewModel.fetchSettings())
        viewModel.isLoading.observeForever(observer)
        verify(observer).onChanged(false)
        viewModel.fetchPetsList()
        verify(observer).onChanged(true)
    }


}