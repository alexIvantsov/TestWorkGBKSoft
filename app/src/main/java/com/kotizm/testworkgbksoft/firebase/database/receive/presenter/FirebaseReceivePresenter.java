package com.kotizm.testworkgbksoft.firebase.database.receive.presenter;

import com.kotizm.testworkgbksoft.firebase.database.receive.view.IFirebaseReceiveView;
import com.kotizm.testworkgbksoft.firebase.database.receive.data.IFirebaseReceiveData;
import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.util.List;

public class FirebaseReceivePresenter
        implements IFirebaseReceivePresenter, IFirebaseReceiveData.IOnFinishedListener {

    private IFirebaseReceiveView receiveView;
    private IFirebaseReceiveData getReceiveNoticeIntractor;

    public FirebaseReceivePresenter(IFirebaseReceiveView receiveView, IFirebaseReceiveData getReceiveNoticeIntractor) {
        this.receiveView = receiveView;
        this.getReceiveNoticeIntractor = getReceiveNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        receiveView = null;
    }

    @Override
    public void requestData() {
        getReceiveNoticeIntractor.getReceiveData(this);
    }

    @Override
    public void onFinished(List<FirebaseData> dataFirebase) {
        if(receiveView != null){
            receiveView.onResponseSuccess(dataFirebase);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(receiveView != null){
            receiveView.onResponseFailure(throwable);
        }
    }
}