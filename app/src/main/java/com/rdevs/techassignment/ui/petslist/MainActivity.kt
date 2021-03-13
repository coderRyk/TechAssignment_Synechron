package com.rdevs.techassignment.ui.petslist

import Pet
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rdevs.techassignment.PetsAdapter
import com.rdevs.techassignment.R
import com.rdevs.techassignment.models.Settings
import com.rdevs.techassignment.network.NetworkHelper
import com.rdevs.techassignment.utils.UiUtils
import okhttp3.*
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progress: ProgressBar
    private lateinit var btnCall: Button
    private lateinit var btnChat: Button
    private lateinit var tvOfficeHrs: TextView
    private lateinit var rvPets: RecyclerView

    private lateinit var petsAdapter: PetsAdapter

    private lateinit var petsViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        checkConnectivity()

        petsViewModel = ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        ).get(MainActivityViewModel::class.java)

        initViewModelObservers()

    }

    private fun initViewModelObservers() {
        petsViewModel.petsListLiveData
            .observe(this, { petsList ->
                if (!petsList.isNullOrEmpty()) {
                    petsViewModel.isLoading.postValue(false)
                    petsAdapter.data = petsList as ArrayList<Pet>;
                }
            })

        petsViewModel.settingsLiveData
            .observe(this, { settings ->
                if (settings != null) {
                    petsViewModel.isLoading.postValue(false)
                    setupSupportUI(settings)
                }
            })

        petsViewModel.isLoading.observe(this, { isLoading: Boolean? ->
            if (isLoading != null) {
                UiUtils.toggleVisibility(progress, isLoading)
            }
        })
    }


    private fun initUI() {
        progress = findViewById(R.id.pbLoader)
        btnCall = findViewById(R.id.btnCall)
        btnChat = findViewById(R.id.btnChat)

        btnCall.setOnClickListener(this);
        btnChat.setOnClickListener(this);

        tvOfficeHrs = findViewById(R.id.tvOfficeHrs)
        rvPets = findViewById(R.id.rvPets)

        val divider = DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL)
        divider.setDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.item_separator
            )!!
        )
        rvPets.addItemDecoration(divider)


        petsAdapter = PetsAdapter(this);
        rvPets.adapter = petsAdapter

    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnChat,
            R.id.btnCall -> {

                val isWithinOfficeHrs = petsViewModel.settingsLiveData.value!!.isWithinOfficeHrs;

                val msgRes =
                    if (isWithinOfficeHrs) R.string.msg_support_within_office_hrs else R.string.msg_support_outside_office_hrs
                Toast.makeText(applicationContext, msgRes, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setupSupportUI(settings: Settings) {

        runOnUiThread {
            UiUtils.toggleVisibility(btnCall, settings.isCallEnabled);
            UiUtils.toggleVisibility(btnChat, settings.isChatEnabled);
            UiUtils.toggleVisibility(tvOfficeHrs, settings.workHours?.isNotEmpty()!!);
            tvOfficeHrs.text = settings.workHours;
        }
    }

    //endregion

    private fun checkConnectivity() {

        if (!NetworkHelper.isConnected(this@MainActivity)) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(getString(R.string.msg_no_internet_desc))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.btn_retry)) { dialog, id ->
                    petsViewModel.initViewModel()
                }
                .setNegativeButton(getString(R.string.btn_cancel)) { dialog, id ->
                    finish()
                }

            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.msg_no_internet))
            alert.setIcon(R.drawable.ic_paw)
            alert.show()
        }
    }

}