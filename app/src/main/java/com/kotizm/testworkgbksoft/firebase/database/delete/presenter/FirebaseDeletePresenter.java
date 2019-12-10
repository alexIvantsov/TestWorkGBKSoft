package com.kotizm.testworkgbksoft.firebase.database.delete.presenter;

import com.kotizm.testworkgbksoft.firebase.database.delete.data.IFirebaseDeleteData;
import com.kotizm.testworkgbksoft.firebase.database.delete.view.IFirebaseDeleteView;
import com.kotizm.testworkgbksoft.model.FirebaseData;

public class FirebaseDeletePresenter
        implements IFirebaseDeletePresenter, IFirebaseDeleteData.IOnFinishedListener {

    private IFirebaseDeleteView deleteView;
    private IFirebaseDeleteData getDeleteNoticeIntractor;

    public FirebaseDeletePresenter(IFirebaseDeleteView deleteView, IFirebaseDeleteData getDeleteNoticeIntractor) {
        this.deleteView = deleteView;
        this.getDeleteNoticeIntractor = getDeleteNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        deleteView = null;
    }

    @Override
    public void deleteItem(FirebaseData item) {
        getDeleteNoticeIntractor.deleteItem(this, item);
    }

    @Override
    public void onDeleteFinished(FirebaseData item) {
        if(deleteView != null){
            deleteView.onDeleteSuccess(item);
        }
    }

    @Override
    public void onDeleteFailure(Throwable throwable) {
        if(deleteView != null){
            deleteView.onDeleteFailure(throwable);
        }
    }
}