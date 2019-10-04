package com.monstercode.contactsapp;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.monstercode.contactsapp.data.Settings;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat tapToCallSwitchCompat, addPhoneContactsSwitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tapToCallSwitchCompat = findViewById(R.id.tapToCallSwitch);
        tapToCallSwitchCompat.setChecked(Settings.isClickToCall());

        tapToCallSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setClickToCall(b);
            }

        });

        addPhoneContactsSwitch = findViewById(R.id.addPhoneContactsSwitch);
        addPhoneContactsSwitch.setChecked(Settings.isAddPhoneContacts());

        addPhoneContactsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setAddPhoneContacts(b);
            }
        });

    }
}
