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
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.statusbar.ThemeAccentUtils;

import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class SwitchStyles extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_SWITCH_STYLES = "switch_style";

    private View mView;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.switch_styles_main, null);

        if (mView != null) {
            initView();
            setAlpha(mContext.getResources());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setNegativeButton(R.string.cancel, this)
                .setNeutralButton(R.string.switch_default, this)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {
        LinearLayout md2 = mView.findViewById(R.id.SwitchStyleMd2);
        setLayout("1", md2);

        LinearLayout oneplus = mView.findViewById(R.id.SwitchStyleOneplus);
        setLayout("2", oneplus);

        LinearLayout telegram = mView.findViewById(R.id.SwitchStyleTelegram);
        setLayout("3", telegram);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            Settings.System.putIntForUser(resolver,
                    Settings.System.SWITCH_STYLE, 0, mCurrentUserId);
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final SwitchStyles dialog = new SwitchStyles();
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_SWITCH_STYLES);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.VENOM;
    }

    private void setLayout(final String style, final LinearLayout layout) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (layout != null) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.SWITCH_STYLE, Integer.parseInt(style), mCurrentUserId);
                    dismiss();
                }
            });
        }
    }

    private void setAlpha(Resources res) {
        LinearLayout md2 = mView.findViewById(R.id.SwitchStyleMd2);
        LinearLayout oneplus = mView.findViewById(R.id.SwitchStyleOneplus);
        LinearLayout telegram = mView.findViewById(R.id.SwitchStyleTelegram);

        TypedValue typedValue = new TypedValue();
        res.getValue(R.dimen.qs_styles_layout_opacity, typedValue, true);
        float mLayoutOpacity = typedValue.getFloat();

        if (ThemeAccentUtils.isUsingSwitchStyles(mOverlayManager, mCurrentUserId, 1 )) {
            md2.setAlpha((float) 1.0);
            oneplus.setAlpha(mLayoutOpacity);
            telegram.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingSwitchStyles(mOverlayManager, mCurrentUserId, 2 )) {
            md2.setAlpha(mLayoutOpacity);
            oneplus.setAlpha((float) 1.0);
            telegram.setAlpha(mLayoutOpacity);
        } else if (ThemeAccentUtils.isUsingSwitchStyles(mOverlayManager, mCurrentUserId, 3 )) {
            md2.setAlpha(mLayoutOpacity);
            oneplus.setAlpha(mLayoutOpacity);
            telegram.setAlpha((float) 1.0);
        } else {
            md2.setAlpha((float) 1.0);
            oneplus.setAlpha((float) 1.0);
            telegram.setAlpha((float) 1.0);
        }
    }
}