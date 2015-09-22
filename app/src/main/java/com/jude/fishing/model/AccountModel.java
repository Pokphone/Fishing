package com.jude.fishing.model;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.fishing.config.Dir;
import com.jude.fishing.model.bean.Account;
import com.jude.fishing.model.bean.PersonBrief;
import com.jude.fishing.model.service.DefaultTransform;
import com.jude.utils.JFileManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/9/11.
 */
public class AccountModel extends AbsModel {
    public static final String FILE_ACCOUNT = "Account";
    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }

    public Account userAccountData = null;
    public BehaviorSubject<Account> userAccountDataBehaviorSubject = BehaviorSubject.create();

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        super.onAppCreateOnBackThread(ctx);
        setAccount((Account) JFileManager.getInstance().getFolder(Dir.Object).readObjectFromFile(FILE_ACCOUNT));
    }

    public Account getAccount(){
        return userAccountData;
    }

    public Subscription registerAccountUpdate(Action1<? super Account> accountAction1){
        return userAccountDataBehaviorSubject.subscribe(accountAction1);
    }
    public Subscription registerAccountUpdate(Observer<? super Account> accountAction1){
        return userAccountDataBehaviorSubject.subscribe(accountAction1);
    }
    public Observable<Account> login(String name,String password){
        Observable<Account> observable = Observable.create(new Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                subscriber.onNext(createVirtualAccount());
                subscriber.onCompleted();
            }
        }).delay(500, TimeUnit.MILLISECONDS).compose(new DefaultTransform<>());

        observable.subscribe(new Action1<Account>() {
            @Override
            public void call(Account account) {
                saveAccount(account);
                setAccount(account);
            }
        });
        return observable;
    }

    public void logout(){
        saveAccount(null);
        setAccount(null);
    }

    void saveAccount(Account account){
        if (account == null){
            JFileManager.getInstance().getFolder(Dir.Object).deleteChild(FILE_ACCOUNT);
        }else{
            JFileManager.getInstance().getFolder(Dir.Object).writeObjectToFile(account, FILE_ACCOUNT);
        }
    }

    void setAccount(Account account){
        userAccountData = account;
        userAccountDataBehaviorSubject.onNext(account);
    }

    public PersonBrief[] createVirtualPersonBriefs(int count){
        PersonBrief[] personBriefs = new PersonBrief[count];
        for (int i = 0; i < count; i++) {
            personBriefs[i] = new PersonBrief("http://i1.hdslb.com/user/1570/157056/myface.jpg",0,"赛亚♂sya", (int) (Math.random()*2),"沉迷于手游无法自拔填坑是什么能吃吗");
        }
        return personBriefs;
    }

    public Account createVirtualAccount(){
        return new Account("http://i2.hdslb.com/user/18232/1823239/myface.jpg",0,"Jude",0,"喂不熟的人，忘不掉的狗","海底捞针，倒挂金钩",18,
                "http://img3.imgtn.bdimg.com/it/u=3619136483,1678174220&fm=21&gp=0.jpg","jack slow fuck",BlogModel.getInstance().createVirtualSeed(3),
                5,8,10,"156*****295","");

    }

}