/*
Copyright (C) Max Kastanas 2012

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.max2idea.android.limbo.help;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.limbo.emu.lib.R;
import com.max2idea.android.limbo.main.Config;
import com.max2idea.android.limbo.main.LimboActivity;
import com.max2idea.android.limbo.main.LimboApplication;
import com.max2idea.android.limbo.main.LimboSettingsManager;
import com.max2idea.android.limbo.network.NetworkUtils;
import com.max2idea.android.limbo.updates.UpdateChecker;


public class Help {
    private static final String TAG = "Help";

    public static void showHelp(final Activity activity) {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(Config.APP_NAME + " " + LimboApplication.getLimboVersionString()
                + " " + "QEMU" + " " + LimboApplication.getQemuVersionString() );

        LinearLayout mLayout = new LinearLayout(activity);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(activity);
        textView.setTextSize(15);
        textView.setText(activity.getResources().getString(R.string.welcomeText));
        textView.setPadding(20, 20, 20, 20);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        scrollView.addView(textView);
        mLayout.addView(scrollView);

        CheckBox checkUpdates = new CheckBox(activity);
        checkUpdates.setText(R.string.checkForUpdates);
        checkUpdates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LimboSettingsManager.setPromptUpdateVersion(activity, b);
            }
        });
        checkUpdates.setChecked(LimboSettingsManager.getPromptUpdateVersion(activity));
        mLayout.addView(checkUpdates);
        alertDialog.setView(mLayout);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(android.R.string.ok),
                (dialog, which) -> {

                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.GoToWiki),
                (dialog, which) -> NetworkUtils.openURL(activity, Config.guidesLink));

        // 设置 Neutral 按钮
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getString(R.string.CheckUpdate),
                (dialog, which) -> {
                    LimboSettingsManager.setPromptUpdateVersion(activity, true);
                    // 在新线程中检查更新
                    isButtonCheck = 1;
                    Thread tsdl = new Thread(new Runnable() {
                        public void run() {
                            UpdateChecker.checkNewVersion(activity);
                        }
                    });
                    tsdl.start();
                    showHelp(activity);
                });
        Log.d(TAG, "Go to Updatechecker");

        alertDialog.show();
    }

    public static int isButtonCheck = 0;
}
