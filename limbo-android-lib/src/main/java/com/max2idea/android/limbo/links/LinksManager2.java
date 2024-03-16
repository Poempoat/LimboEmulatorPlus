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
package com.max2idea.android.limbo.links;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.limbo.emu.lib.R;
import com.max2idea.android.limbo.main.Config;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Links for usefull applications and OS images
 */
public class LinksManager2 extends AlertDialog {
    private final static String TAG = "LinksManager2";
    private ListView listIsoView;

    private ArrayList<LinkInfo> itemsISOs = null;

    public LinksManager2(@NonNull Context context) {
        super(context);
    }

    protected LinksManager2(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LinksManager2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void goToURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.os_links);
        setupListeners();
        fill();
    }

    private void setupListeners() {
        listIsoView = (ListView) findViewById(R.id.os_list);
        listIsoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                int selectionRowID = (int) id;
                String path = itemsISOs.get(selectionRowID).url;
                if(path!=null)
                    goToURL(path);
                dismiss();
            }
        });
    }

    private void fill() {
        itemsISOs = new ArrayList<>();
        if (Config.downloadLinks != null) {
            Set<Map.Entry<String, LinkInfo>> linkSet = Config.downloadLinks.entrySet();
            for (Map.Entry<String, LinkInfo> item : linkSet) {
                LinkInfo linkInfo = item.getValue();
                itemsISOs.add(linkInfo);
            }
        }
        FileAdapter fileList = new FileAdapter(getContext(), R.layout.link_row, itemsISOs);
        listIsoView.setAdapter(fileList);
    }


    public enum LinkType {
        SITE, GITHUB, TELEGRAM, YUKAIDI, LANZOU
    }

    public static class LinkInfo {
        LinkType type;
        String title;
        String descr;
        String url;

        public LinkInfo(String title, String descr, String url, LinkType type) {
            this.type = type;
            this.descr = descr;
            this.url = url;
            this.title = title;
        }
    }

    public static class FileAdapter extends ArrayAdapter<LinkInfo> {
        private final Context context;
        private final ArrayList<LinkInfo> files;

        public FileAdapter(Context context, int layout, ArrayList<LinkInfo> files) {
            super(context, layout, files);
            this.context = context;
            this.files = files;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @SuppressLint("ViewHolder")
            View rowView = inflater.inflate(R.layout.link_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.LINK_NAME);
            TextView descrView = (TextView) rowView.findViewById(R.id.LINK_DESCR);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.LINK_ICON);
            LinkInfo linkInfo = files.get(position);
            textView.setText(linkInfo.title);
            descrView.setText(linkInfo.descr);

            if (linkInfo.type == LinkType.SITE)
                imageView.setImageResource(R.drawable.limbo);
            else if (linkInfo.type == LinkType.GITHUB)
                imageView.setImageResource(R.drawable.github);
            else if (linkInfo.type == LinkType.TELEGRAM)
                imageView.setImageResource(R.drawable.telegram);
            else if (linkInfo.type == LinkType.YUKAIDI)
                imageView.setImageResource(R.drawable.yukaidi);
            else if (linkInfo.type == LinkType.LANZOU)
                imageView.setImageResource(R.drawable.lanzou);

            return rowView;
        }
    }

}
