package com.limbo.emu.plus;

import android.os.Bundle;

import com.max2idea.android.limbo.links.LinksManager2;
import com.max2idea.android.limbo.log.Logger;
import com.max2idea.android.limbo.main.Config;
import com.max2idea.android.limbo.main.LimboActivity;
import com.max2idea.android.limbo.links.LinksManager;
import com.max2idea.android.limbo.main.LimboApplication;
public class LimboEmuActivity extends LimboActivity {
    public void onCreate(Bundle bundle) {
        LimboApplication.arch = Config.Arch.x86_64;
        Config.clientClass = this.getClass();
        Config.enableKVM = true;
        Config.enableEmulatedSDCard = true;
        Config.downloadLinks.put(getString(com.limbo.emu.lib.R.string.GoToWiki), new LinksManager2.LinkInfo(getString(com.limbo.emu.lib.R.string.GoToWiki),
                "",
                "https://limboplus.xtzyj.top/#/?id=downloads",
                LinksManager2.LinkType.SITE));
        Config.downloadLinks.put("GitHub", new LinksManager2.LinkInfo("GitHub",
                "GitHub Releases",
                "https://github.com/Poempoat/LimboEmulatorPlus/releases",
                LinksManager2.LinkType.GITHUB));
        Config.downloadLinks.put("Telegram", new LinksManager2.LinkInfo(getString(com.limbo.emu.lib.R.string.Telegram),
                "Telegram Channel @limboemuplus",
                "https://t.me/limboemuplus",
                LinksManager2.LinkType.TELEGRAM));
        Config.downloadLinks.put(getString(com.limbo.emu.lib.R.string.Yukaidi), new LinksManager2.LinkInfo(getString(com.limbo.emu.lib.R.string.Yukaidi),
                "",
                "https://pan.yukaidi.com/s/3vlrSB",
                LinksManager2.LinkType.YUKAIDI));
        Config.downloadLinks.put(getString(com.limbo.emu.lib.R.string.lanzou), new LinksManager2.LinkInfo(getString(com.limbo.emu.lib.R.string.lanzou),
                getString(com.limbo.emu.lib.R.string.lanzoupwd),
                "https://xtzyj.lanzouu.com/b02k2wazc",
                LinksManager2.LinkType.LANZOU));
        //XXX; only for 64bit hosts, also make sure you have qemu 3.1.0 x86_64-softmmu and above compiled
        if(LimboApplication.isHost64Bit() && Config.enableMTTCG)
            Config.enableMTTCG = true;
        else
            Config.enableMTTCG = false;
        Config.osImages.put(getString(R.string.DebianLinux), new LinksManager.LinkInfo(getString(R.string.DebianLinux),
                getString(R.string.DebianLinuxDescr),
                "https://www.debian.org/distrib/",
                LinksManager.LinkType.ISO));
        Config.osImages.put("Ubuntu", new LinksManager.LinkInfo(getString(R.string.Ubuntu),
                getString(R.string.UbuntuDescr),
                "https://ubuntu.com/",
                LinksManager.LinkType.ISO));
        Config.osImages.put(getString(R.string.Windows), new LinksManager.LinkInfo(getString(R.string.Windows),
                getString(R.string.WindowsDescr),
                "https://pan.yukaidi.com/s/BwdySl",
                LinksManager.LinkType.ISO));
        Config.osImages.put("VirtIO", new LinksManager.LinkInfo("VirtIO",
                getString(R.string.VirtIODescr),
                "https://fedorapeople.org/groups/virt/virtio-win/direct-downloads/latest-virtio/",
                LinksManager.LinkType.TOOL));
        super.onCreate(bundle);
        //TODO: change location to something that the user will have access outside of limbo
        //  like internal storage
        Logger.setupLogFile("/limbo/limbo-x86-log.txt");
    }

    protected void loadQEMULib() {
        try {
            System.loadLibrary("qemu-system-i386");
        } catch (Error ex) {
            System.loadLibrary("qemu-system-x86_64");
        }

    }
}
