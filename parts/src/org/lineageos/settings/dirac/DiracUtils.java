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

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.preference.PreferenceManager;

public final class DiracUtils {

    private static DiracSound mDiracSound;
    private static boolean mInitialized;
    private static Context mContext;

    private DiracUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initializes the DiracUtils with the provided context.
     *
     * @param context The application context.
     */
    public static void initialize(Context context) {
        if (!mInitialized) {
            mContext = context;
            mDiracSound = new DiracSound(0, 0);
            mInitialized = true;

            // Restore selected scenario
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String scene = sharedPrefs.getString(DiracSettingsFragment.PREF_SCENARIO, "0");
            setScenario(Integer.parseInt(scene));
        }
    }

    /**
     * Enables or disables the music enhancement.
     *
     * @param enable True to enable, false to disable.
     */
    protected static void setMusic(boolean enable) {
        SystemProperties.set("persist.vendor.audio.misound.disable", Boolean.toString(!enable));
        mDiracSound.setMusic(enable ? 1 : 0);
    }

    /**
     * Checks if Dirac music enhancement is enabled.
     *
     * @return True if enabled, false otherwise.
     */
    protected static boolean isDiracEnabled() {
        return mDiracSound != null && mDiracSound.getMusic() == 1;
    }

    /**
     * Sets the equalizer levels based on the given preset.
     *
     * @param preset A comma-separated string of levels.
     */
    protected static void setLevel(String preset) {
        String[] levels = preset.split("\\s*,\\s*");
        for (int band = 0; band < levels.length; band++) {
            mDiracSound.setLevel(band, Float.parseFloat(levels[band]));
        }
    }

    /**
     * Sets the headset type for the Dirac sound effect.
     *
     * @param type The headset type identifier.
     */
    protected static void setHeadsetType(int type) {
        mDiracSound.setHeadsetType(type);
    }

    /**
     * Checks if HiFi mode is enabled.
     *
     * @return True if HiFi mode is enabled, false otherwise.
     */
    protected static boolean getHifiMode() {
        AudioManager audioManager = mContext.getSystemService(AudioManager.class);
        return audioManager.getParameters("hifi_mode").contains("true");
    }

    /**
     * Sets the HiFi mode.
     *
     * @param mode 1 to enable, 0 to disable.
     */
    protected static void setHifiMode(int mode) {
        AudioManager audioManager = mContext.getSystemService(AudioManager.class);
        audioManager.setParameters("hifi_mode=" + (mode == 1));
        mDiracSound.setHifiMode(mode);
    }

    /**
     * Sets the current scenario for the Dirac sound effect.
     *
     * @param scenario The scenario identifier.
     */
    protected static void setScenario(int scenario) {
        mDiracSound.setScenario(scenario);
    }
}
