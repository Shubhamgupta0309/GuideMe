package com.example.guideme.viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.guideme.models.NavigationData;

public class BlindModeViewModel extends ViewModel {
    private NavigationData currentNavigationData;
    private String assistantResponse;
    private String readingText;

    public NavigationData getCurrentNavigationData() {
        return currentNavigationData;
    }

    public void setCurrentNavigationData(NavigationData data) {
        this.currentNavigationData = data;
    }

    public String getAssistantResponse() {
        return assistantResponse;
    }

    public void setAssistantResponse(String response) {
        this.assistantResponse = response;
    }

    public String getReadingText() {
        return readingText;
    }

    public void setReadingText(String text) {
        this.readingText = text;
    }
}
