/*
 * Copyright (C) 2018 The Dirty Unicorns Project
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

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.drawer.SettingsDrawerActivity;

public class LsClockStyle extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_LS_CLOCK_STYLE = "ls_clock_style";

    private View mView;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.ls_clock_style, null);

        if (mView != null) {
            initView();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setNegativeButton(R.string.cancel, this)
                .setNeutralButton(R.string.theme_picker_auto, this)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {

        LinearLayout LsClockDigitalBold = mView.findViewById(R.id.LsClockDigitalBold);
        setClock("1", LsClockDigitalBold);

        LinearLayout LsClockAnalog = mView.findViewById(R.id.LsClockAnalog);
        setClock("2", LsClockAnalog);

        LinearLayout LsClockSammy = mView.findViewById(R.id.LsClockSammy);
        setClock("4", LsClockSammy);

        LinearLayout LsClockSammyBold = mView.findViewById(R.id.LsClockSammyBold);
        setClock("5", LsClockSammyBold);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            Settings.System.putIntForUser(resolver,
                    Settings.System.LOCKSCREEN_CLOCK_SELECTION, 0, mCurrentUserId);
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final LsClockStyle dialog = new LsClockStyle();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_LS_CLOCK_STYLE);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.VENOM;
    }

    private void setClock(final String clock, final LinearLayout layout) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (layout != null) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.LOCKSCREEN_CLOCK_SELECTION, Integer.parseInt(clock), mCurrentUserId);
                    dismiss();
                }
            });
        }
    }
}