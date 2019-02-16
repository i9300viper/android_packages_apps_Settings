/*
 * Copyright (C) 2018 Viper-OS
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

package com.android.settings.deviceinfo.viper;

import android.content.Context;
import android.os.SystemProperties;
import android.os.UserManager;

import com.android.settings.R;

public class ViperMaintainerDialogController {

    private static final int VIPER_MAINTAINER_VALUE_ID = R.id.viper_maintainer_value;
    private static final int VIPER_MAINTAINER_LABEL_ID = R.id.viper_maintainer_label;

    private final ViperInfoDialogFragment mDialog;
    private final Context mContext;
    private final UserManager mUserManager;

    public ViperMaintainerDialogController(ViperInfoDialogFragment dialog) {
        mDialog = dialog;
        mContext = dialog.getContext();
        mUserManager = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
    }

    public void initialize() {

        mDialog.setText(VIPER_MAINTAINER_VALUE_ID,
                mContext.getResources().getString(R.string.viper_maintainer_summary));
    }
}
