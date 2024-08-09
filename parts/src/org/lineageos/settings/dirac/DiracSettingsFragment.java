/*
 * Copyright (C) 2018, 2020 The LineageOS Project
 * Copyright (C) 2024 Paranoid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.dirac;

import android.os.Bundle;
import android.widget.Switch;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import org.lineageos.settings.R;

public class DiracSettingsFragment extends PreferenceFragment
        implements OnPreferenceChangeListener, OnMainSwitchChangeListener {

    private static final String PREF_ENABLE = "dirac_enable";
    private static final String PREF_HEADSET = "dirac_headset_pref";
    private static final String PREF_HIFI = "dirac_hifi_pref";
    private static final String PREF_PRESET = "dirac_preset_pref";
    protected static final String PREF_SCENARIO = "dirac_scenario_pref";

    private MainSwitchPreference mSwitchBar;
    private ListPreference mScenario;
    private ListPreference mHeadsetType;
    private ListPreference mPreset;
    private SwitchPreference mHifi;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.dirac_settings);

        DiracUtils.initialize(getActivity());
        boolean enhancerEnabled = DiracUtils.isDiracEnabled();

        mSwitchBar = findPreference(PREF_ENABLE);
        if (mSwitchBar != null) {
            mSwitchBar.addOnSwitchChangeListener(this);
            mSwitchBar.setChecked(enhancerEnabled);
        }

        mScenario = findPreference(PREF_SCENARIO);
        if (mScenario != null) {
            mScenario.setOnPreferenceChangeListener(this);
        }

        mHeadsetType = findPreference(PREF_HEADSET);
        if (mHeadsetType != null) {
            mHeadsetType.setOnPreferenceChangeListener(this);
        }

        mPreset = findPreference(PREF_PRESET);
        if (mPreset != null) {
            mPreset.setOnPreferenceChangeListener(this);
        }

        mHifi = findPreference(PREF_HIFI);
        if (mHifi != null) {
            mHifi.setOnPreferenceChangeListener(this);
        }

        boolean hifiEnabled = DiracUtils.getHifiMode();
        if (mHeadsetType != null) {
            mHeadsetType.setEnabled(!hifiEnabled && enhancerEnabled);
        }
        if (mPreset != null) {
            mPreset.setEnabled(!hifiEnabled && enhancerEnabled);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        switch (key) {
            case PREF_HEADSET:
                DiracUtils.setHeadsetType(Integer.parseInt(newValue.toString()));
                return true;
            case PREF_HIFI:
                boolean isHifiEnabled = (Boolean) newValue;
                DiracUtils.setHifiMode(isHifiEnabled ? 1 : 0);
                if (DiracUtils.isDiracEnabled()) {
                    if (mHeadsetType != null) {
                        mHeadsetType.setEnabled(!isHifiEnabled);
                    }
                    if (mPreset != null) {
                        mPreset.setEnabled(!isHifiEnabled);
                    }
                }
                return true;
            case PREF_PRESET:
                DiracUtils.setLevel((String) newValue);
                return true;
            case PREF_SCENARIO:
                DiracUtils.setScenario(Integer.parseInt(newValue.toString()));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        if (mSwitchBar != null) {
            mSwitchBar.setChecked(isChecked);
        }

        DiracUtils.setMusic(isChecked);

        if (!DiracUtils.getHifiMode()) {
            if (mHeadsetType != null) {
                mHeadsetType.setEnabled(isChecked);
            }
            if (mPreset != null) {
                mPreset.setEnabled(isChecked);
            }
        }
    }
}
