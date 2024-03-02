package com.limbo.emu.plus;

import android.os.Bundle;

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
