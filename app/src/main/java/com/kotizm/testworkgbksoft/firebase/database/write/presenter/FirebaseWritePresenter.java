package com.kotizm.testworkgbksoft.firebase.database.write.presenter;

import com.kotizm.testworkgbksoft.firebase.database.write.IFirebaseWriteView;
import com.kotizm.testworkgbksoft.firebase.database.write.data.IFirebaseWriteData;
import com.kotizm.testworkgbksoft.model.FirebaseData;

public class FirebaseWritePresenter
        implements IFirebaseWritePresenter, IFirebaseWriteData.IOnFinishedListener {

    private IFirebaseWriteView writeView;
    private IFirebaseWriteData getWriteNoticeIntractor;

    public FirebaseWritePresenter(IFirebaseWriteView writeView, IFirebaseWriteData getWriteNoticeIntractor) {
        this.writeView = writeView;
        this.getWriteNoticeIntractor = getWriteNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        writeView = null;
    }

    @Override
    public void writeItem(FirebaseData item) {
        getWriteNoticeIntractor.writeItem(this, item);
    }

    @Override
    public void onWriteFinished(String id, FirebaseData item) {
        if(writeView != null){
            writeView.onWriteSuccess(id, item);
        }
    }

    @Override
    public void onWriteFailure(Throwable throwable) {
        if(writeView != null){
            writeView.onWriteFailure(throwable);
        }
    }
}