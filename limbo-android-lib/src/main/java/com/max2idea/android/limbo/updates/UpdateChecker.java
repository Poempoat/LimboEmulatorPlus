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
package com.max2idea.android.limbo.updates;

import static com.max2idea.android.limbo.help.Help.isButtonCheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.limbo.emu.lib.R;
import com.max2idea.android.limbo.downloads.Downloads;
import com.max2idea.android.limbo.links.LinksManager2;
import com.max2idea.android.limbo.main.Config;
import com.max2idea.android.limbo.main.LimboApplication;
import com.max2idea.android.limbo.main.LimboSettingsManager;
import com.max2idea.android.limbo.network.NetworkUtils;
import com.max2idea.android.limbo.toast.ToastUtils;

/** Software Update notifier for checking if a new version is published.
  */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private static final String TAG = "UpdateChecker";

    public static void checkNewVersion(final Activity activity) {
        if (!LimboSettingsManager.getPromptUpdateVersion(activity) || isButtonCheck == 0) {
            return;
        }

        try {
            byte[] streamData = getContentFromUrl(Config.newVersionLink);
            Log.d(TAG, "update");
            final String versionStr = new String(streamData).trim();
            float version = Float.parseFloat(versionStr);
            String versionName = getVersionName(versionStr);

            int versionCheck = (int) (version * 100);
            if (versionCheck > LimboApplication.getLimboVersion()) {
                final String finalVersionName = versionName;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        promptNewVersion(activity, finalVersionName);
                    }
                });
            } else {
                ToastUtils.toastShort(activity, activity.getString(R.string.NoUpdate));
            }
        } catch (Exception ex) {
            ToastUtils.toastShort(activity, activity.getString(R.string.UpdateCheckFailed));
            Log.w(TAG, "Could not get new version: " + ex.getMessage());
            if (Config.debug)
                ex.printStackTrace();
        }
        isButtonCheck = 0;
    }

    private static byte[] getContentFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String getVersionName(String versionStr) {
        String[] versionSegments = versionStr.split("\\.");
        int maj = Integer.parseInt(versionSegments[0]) / 100;
        int min = Integer.parseInt(versionSegments[0]) % 100;
        int mic = 0;
        if (versionSegments.length > 1) {
            mic = Integer.parseInt(versionSegments[1]);
        }
        String versionName = maj + "." + min + "." + mic;
        return versionName;
    }

    public static void promptNewVersion(final Activity activity, String version) {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(activity.getString(R.string.NewVersion) + " " + version);
        TextView stateView = new TextView(activity);
        stateView.setText(R.string.NewVersionWarning);
        stateView.setPadding(20, 20, 20, 20);
        alertDialog.setView(stateView);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(R.string.GenNewVersion),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LinksManager2 manager = new LinksManager2(activity);
                        manager.show();
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.Cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();
    }
}
