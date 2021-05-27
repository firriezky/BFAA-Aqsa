package com.aqsa.myapplication.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aqsa.myapplication.R
import com.aqsa.myapplication.databinding.FragmentHomeBinding
import com.aqsa.myapplication.databinding.FragmentSettingsBinding
import com.aqsa.myapplication.util.ReminderReceiver
import com.aqsa.myapplication.util.Util.makeLongToast

class SettingsFragment : Fragment() {

    var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding as FragmentSettingsBinding

    private val reminderReceiver by lazy { ReminderReceiver() }


    private val MY_PREF_NAME = "shared_preferences"

    lateinit var mySharedPref: SharedPreferences


    lateinit var prefEditor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPref = requireContext().getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE)
        prefEditor = mySharedPref.edit()

        binding.cardLanguage.setOnClickListener {
            requireContext().makeLongToast(getString(R.string.change_language))
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        //Check current alarm status,then change the switch
        val alarmStatus = mySharedPref.getBoolean("ALARM_SET", false)
        binding.switchReminder.isChecked = alarmStatus


        binding.switchReminder.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                reminderReceiver.setRepeatingAlarm(
                    requireContext(), getString(R.string.yuk)
                )
                prefEditor.putBoolean("ALARM_SET", true)
                prefEditor.commit()

            } else {
                reminderReceiver.abortAlarm(requireContext())
                prefEditor.putBoolean("ALARM_SET", false)
                prefEditor.commit()
            }
        }

    }


}