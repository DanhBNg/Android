package com.example.tangthucac;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> shouldRefresh = new MutableLiveData<>();

    // Gọi khi cần refresh Library
    public void requestRefresh() {
        shouldRefresh.postValue(true);
    }

    // Lắng nghe sự kiện refresh
    public MutableLiveData<Boolean> getShouldRefresh() {
        return shouldRefresh;
    }

    // Reset trạng thái sau khi refresh
    public void refreshComplete() {
        shouldRefresh.postValue(false);
    }
}
