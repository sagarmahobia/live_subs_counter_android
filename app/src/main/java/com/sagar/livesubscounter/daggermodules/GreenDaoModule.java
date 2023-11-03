package com.sagar.livesubscounter.daggermodules;

import android.content.Context;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.greendao.DbOpenHelper;
import com.sagar.livesubscounter.greendao.entities.DaoMaster;
import com.sagar.livesubscounter.greendao.entities.DaoSession;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannelDao;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompareDao;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SAGAR MAHOBIA on 03-Aug-18. at 23:54
 */
@Module(includes = ApplicationModule.class)
public class GreenDaoModule {

    private static final String DB_NAME = "user_data.db";

    @ApplicationScope
    @Provides
    DaoSession getDaoSession(Context context) {
        return new DaoMaster(new DbOpenHelper(context, DB_NAME).getWritableDb()).newSession();
    }

    @ApplicationScope
    @Provides
    FavoriteChannelDao getFavoriteDao(DaoSession daoSession) {
        return daoSession.getFavoriteChannelDao();
    }

    @ApplicationScope
    @Provides
    FavoriteCompareDao favoriteCompareDao(DaoSession daoSession) {
        return daoSession.getFavoriteCompareDao();
    }
}
