package com.sagar.livesubscounter.greendao;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannel;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannelDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SAGAR MAHOBIA on 18-Sep-18. at 23:18
 */
@ApplicationScope
public class FavoriteChannelService {
    private FavoriteChannelDao favoriteChannelDao;

    @Inject
    FavoriteChannelService(FavoriteChannelDao favoriteChannelDao) {
        this.favoriteChannelDao = favoriteChannelDao;
    }

    public Single<List<FavoriteChannel>> getFavoriteChannelsSingle() {
        Single<List<FavoriteChannel>> listSingle = Single.create(emitter -> {
            try {
                List<FavoriteChannel> favorites = favoriteChannelDao.loadAll();
                Collections.sort(
                        favorites, (f1, f2) -> Long.compare(f2.getTimestamp(), f1.getTimestamp())
                );
                emitter.onSuccess(favorites);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        return listSingle.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> insertFavorite(FavoriteChannel favoriteChannel) {
        Single<Long> insertSingle = Single.create(emitter -> {
            try {
                long key = favoriteChannelDao.insert(favoriteChannel);
                emitter.onSuccess(key);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        return insertSingle.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeFavorite(String channelId) {
        return Completable.create(emitter -> {
            try {
                QueryBuilder<FavoriteChannel> qb = favoriteChannelDao.queryBuilder();
                qb.where(FavoriteChannelDao.Properties.ChannelId.eq(channelId));
                List<FavoriteChannel> favoriteChannels = qb.list();
                Long id = favoriteChannels.get(0).getId();
                favoriteChannelDao.deleteByKey(id);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> checkFavorite(String channelId) {
        Single<Boolean> singleBoolean = Single.create(emitter -> {
            try {

                QueryBuilder<FavoriteChannel> qb = favoriteChannelDao.queryBuilder();
                qb.where(FavoriteChannelDao.Properties.ChannelId.eq(channelId));
                long count = qb.count();
                emitter.onSuccess(count > 0);

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        return singleBoolean.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
