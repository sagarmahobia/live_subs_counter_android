package com.sagar.livesubscounter.greendao;

import android.content.Context;

import com.sagar.livesubscounter.greendao.entities.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by SAGAR MAHOBIA on 03-Aug-18. at 23:48
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

// do nothing

//        switch (oldVersion) {
//            case 1:
//                //db.execSQL("");//use this to modify database in v2
//        }
    }
}