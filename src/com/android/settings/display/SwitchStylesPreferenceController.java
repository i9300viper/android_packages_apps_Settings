/*
 * Copyright (C) 2017-2018 The Dirty Unicorns Project
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

package com.android.settings.display;

import android.app.Fragment;
import android.content.Context;
import android.os.UserHandle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.util.viper.Utils;

import com.android.settings.core.PreferenceControllerMixin;

import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnResume;

import com.android.settings.display.SwitchStyles;

public class SwitchStylesPreferenceController extends AbstractPreferenceController
        implements PreferenceControllerMixin, LifecycleObserver, OnResume {

    private static final String KEY_SWITCH_STYLES_FRAGMENT_PREF = "switch_style";
    private static final int MY_USER_ID = UserHandle.myUserId();

    private static final String SUBS_PACKAGE = "projekt.substratum";

    private final Fragment mParent;
    private Preference mSwitchStylesPref;

    public SwitchStylesPreferenceController(Context context, Lifecycle lifecycle, Fragment parent) {
        super(context);
        mParent = parent;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        mSwitchStylesPref  = (Preference) screen.findPreference(KEY_SWITCH_STYLES_FRAGMENT_PREF);
    }

    @Override
    public void onResume() {
        updateEnableState();
        updateSummary();
    }

    @Override
    public boolean isAvailable() {
        return !Utils.isPackageInstalled(mContext, SUBS_PACKAGE);
    }

    @Override
    public String getPreferenceKey() {
        return KEY_SWITCH_STYLES_FRAGMENT_PREF;
    }

    public void updateEnableState() {
        if (mSwitchStylesPref == null) {
            return;
        }

        mSwitchStylesPref.setOnPreferenceClickListener(
            new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SwitchStyles.show(mParent);
                    return true;
                }
            });
    }

    public void updateSummary() {
        if (mSwitchStylesPref != null) {
                mSwitchStylesPref.setSummary(mContext.getString(
                        com.android.settings.R.string.switch_styles_dialog_title));
        }
    }
}