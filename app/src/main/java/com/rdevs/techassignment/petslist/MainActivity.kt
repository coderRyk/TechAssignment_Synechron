package com.rdevs.techassignment.petslist

import Pet
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rdevs.techassignment.Constants
import com.rdevs.techassignment.PetsAdapter
import com.rdevs.techassignment.R
import com.rdevs.techassignment.utils.PrefsHelper
import com.rdevs.techassignment.utils.UiUtils
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var progress: ProgressBar
    private lateinit var btnCall: Button
    private lateinit var btnChat: Button
    private lateinit var tvOfficeHrs: TextView
    private lateinit var rvPets: RecyclerView

    private var petsList = ArrayList<Pet>();
    private lateinit var petsAdapter: PetsAdapter

    private var supportEnabled: Boolean = false

    private lateinit var client: OkHttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()


        if (PrefsHelper.isDataSet(this@MainActivity)) {
            // We already have data so setup the UI.
            setupSupportUI(PrefsHelper.getSettingsResponse(this@MainActivity))
            setupPetsList(PrefsHelper.getPetsListResponse(this@MainActivity))
        } else {
            // Make API call to fetch settings which will in turn call pets list API internally
            fetchSettings()
        }
    }


    private fun initUI() {
        progress = findViewById(R.id.pbLoader)
        btnCall = findViewById(R.id.btnCall)
        btnChat = findViewById(R.id.btnChat)

        btnCall.setOnClickListener(this);
        btnChat.setOnClickListener(this);

        tvOfficeHrs = findViewById(R.id.tvOfficeHrs)
        rvPets = findViewById(R.id.rvPets)

        val divider = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
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

                val msgRes =
                    if (supportEnabled) R.string.msg_support_within_office_hrs else R.string.msg_support_outside_office_hrs
                Toast.makeText(applicationContext, msgRes, Toast.LENGTH_SHORT).show()
            }

        }
    }

    // region API call and handling

    fun fetchPetsList() {

        progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(Constants.PETS_LIST_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    getString(R.string.err_fetching_pets_list_failed) + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var petsResponse = response.body()!!.string()

                    Log.d("fetchPetsList ", "Response string $petsResponse")

                    PrefsHelper.savePreferences(
                        this@MainActivity,
                        PrefsHelper.PREF_KEY_PETS_LIST,
                        petsResponse
                    );

                    setupPetsList(petsResponse)
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.err_fetching_pets_list) + response.code() + response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.visibility = View.GONE
                    }

                }


            }
        })
    }

    // Parse Pets list response and set in recyclerview
    private fun setupPetsList(petsResponse: String?) {
        //creating main json object
        val petsResponseObject = JSONObject(petsResponse)

        //creating json array
        var petsJsonArray: JSONArray =
            petsResponseObject.getJSONArray(Constants.KEY_PETS)
        var size: Int = petsJsonArray.length()

        for (i: Int in 0 until size) {
            val petJSONObject: JSONObject = petsJsonArray.getJSONObject(i)

            val pet = Pet(
                petJSONObject.getString(Constants.KEY_IMAGE_URL),
                petJSONObject.getString(Constants.KEY_TITLE),
                petJSONObject.getString(Constants.KEY_CONTENT_URL),
                petJSONObject.getString(Constants.KEY_DATE_ADDED)
            );
            petsList.add(pet)
        }

        runOnUiThread {
            progress.visibility = View.GONE
            //update the list
            petsAdapter.data = petsList;
        }
    }


    fun fetchSettings() {

        client = OkHttpClient();
        progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(Constants.SETTINGS_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    getString(R.string.err_fetch_settings_failed) + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var settingsResponse = response.body()!!.string()
                    Log.d("fetchSettings ", "Response string $settingsResponse")

                    PrefsHelper.savePreferences(
                        this@MainActivity,
                        PrefsHelper.PREF_KEY_SETTINGS,
                        settingsResponse
                    );

                    setupSupportUI(settingsResponse)

                    // Fetched settings now fetch pets list.
                    fetchPetsList();

                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.err_fetching_settings) + response.code() + response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.visibility = View.GONE
                    }

                }


            }
        })
    }

    // Parse Settings response and handle UI
    private fun setupSupportUI(settingsResponse: String?) {
        //creating response json object
        val configsResponseObject = JSONObject(settingsResponse)

        //creating settings object
        var settings: JSONObject = configsResponseObject.getJSONObject(Constants.KEY_SETTINGS)

        // fetching fields
        val isChatEnabled = settings.getBoolean(Constants.KEY_IS_CHAT_ENABLED);
        val isCallEnabled = settings.getBoolean(Constants.KEY_IS_CALL_ENABLED);
        val workHours = settings.getString(Constants.KEY_WORK_HOURS);

        runOnUiThread {
            UiUtils.toggleVisibility(btnCall, isCallEnabled);
            UiUtils.toggleVisibility(btnChat, isChatEnabled);

            supportEnabled = PrefsHelper.isSupportAvailabile(this@MainActivity)

            UiUtils.toggleVisibility(tvOfficeHrs, supportEnabled);

            tvOfficeHrs.text = workHours;
        }
    }

    //endregion


}