package com.sagar.livesubscounter.greendao;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompare;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompareDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SAGAR MAHOBIA on 20-Sep-18. at 17:14
 */

@ApplicationScope
public class FavoriteCompareService {
    private FavoriteCompareDao favoriteCompareDao;

    @Inject
    FavoriteCompareService(FavoriteCompareDao favoriteCompareDao) {
        this.favoriteCompareDao = favoriteCompareDao;
    }

    public Single<List<FavoriteCompare>> getFavoriteComparesSingle() {
        Single<List<FavoriteCompare>> listSingle = Single.create(emitter -> {
            try {
                List<FavoriteCompare> favorites = favoriteCompareDao.loadAll();
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

    public Single<Long> insertFavorite(FavoriteCompare favoriteCompare) {
        Single<Long> insertSingle = Single.create(emitter -> {
            try {
                long key = favoriteCompareDao.insert(favoriteCompare);
                emitter.onSuccess(key);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        return insertSingle.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Completable removeFavorite(String firstChannelId, String secondChannelId) {
        return Completable.create(emitter -> {
            try {
                QueryBuilder<FavoriteCompare> qb = favoriteCompareDao.queryBuilder();
                qb.where(
                        qb.or(
                                qb.and(
                                        FavoriteCompareDao.Properties.FirstChannelId.eq(firstChannelId),
                                        FavoriteCompareDao.Properties.SecondChannelId.eq(secondChannelId)
                                ),
                                qb.and(
                                        FavoriteCompareDao.Properties.FirstChannelId.eq(secondChannelId),
                                        FavoriteCompareDao.Properties.SecondChannelId.eq(firstChannelId)
                                )
                        )
                );

                FavoriteCompare favoriteCompare = qb.list().get(0);
                favoriteCompareDao.deleteByKey(favoriteCompare.getId());
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> checkFavorite(String firstChannelId, String secondChannelId) {
        Single<Boolean> singleBoolean = Single.create(emitter -> {
            try {
                QueryBuilder<FavoriteCompare> qb = favoriteCompareDao.queryBuilder();
                qb.where(
                        qb.or(
                                qb.and(
                                        FavoriteCompareDao.Properties.FirstChannelId.eq(firstChannelId),
                                        FavoriteCompareDao.Properties.SecondChannelId.eq(secondChannelId)
                                ),
                                qb.and(
                                        FavoriteCompareDao.Properties.FirstChannelId.eq(secondChannelId),
                                        FavoriteCompareDao.Properties.SecondChannelId.eq(firstChannelId)
                                )
                        )
                );

                long count = qb.count();

                emitter.onSuccess(count > 0);


            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        return singleBoolean.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
