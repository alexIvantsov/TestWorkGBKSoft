package com.kotizm.testworkgbksoft.utils.dialog.addpoint;

public class AddPointDialogPresenter implements IAddPointDialogPresenter, IAddPointDialogReceiveData.IOnFinishedListener {

    private IAddPointDialogView dialogView;
    private IAddPointDialogReceiveData getNoticeIntractor;

    public AddPointDialogPresenter(IAddPointDialogView dialogView, IAddPointDialogReceiveData getNoticeIntractor) {
        this.dialogView = dialogView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        dialogView = null;
    }

    @Override
    public void requestData() {
        getNoticeIntractor.getPointName(this);
    }

    @Override
    public void onFinished(String pointName) {
        if(dialogView != null){
            dialogView.setPointName(pointName);
        }
    }
}