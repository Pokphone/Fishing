package com.jude.fishing.module.user;

import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListActivityPresenter;
import com.jude.fishing.model.AccountModel;
import com.jude.fishing.model.entities.Notification;
import com.jude.utils.JUtils;

import rx.Subscription;

/**
 * Created by zhuchenxi on 15/10/23.
 */
public class NotificationPresenter extends BeamListActivityPresenter<NotificationActivity,Notification> {

    @Override
    protected void onCreate(NotificationActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    Subscription subscription;
    @Override
    public void onRefresh() {
        subscription = AccountModel.getInstance().getNotification(0).unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onLoadMore() {
        AccountModel.getInstance().getNotification(getCurPage())
                .doOnError(throwable -> JUtils.Log(throwable.getLocalizedMessage()))
                .unsafeSubscribe(getMoreSubscriber());
    }
}
