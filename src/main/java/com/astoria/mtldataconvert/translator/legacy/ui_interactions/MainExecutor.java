package com.astoria.mtldataconvert.translator.legacy.ui_interactions;

import com.astoria.mtldataconvert.translator.legacy.AutomatedBrowserInteractionOld;
import com.astoria.mtldataconvert.translator.legacy.FindAndClickOpenCVOld;

public class MainExecutor {

    public static void main(String[] args) throws Exception {
        AutomatedBrowserInteractionOld browserInteraction = new AutomatedBrowserInteractionOld();
        browserInteraction.initializeWebDriver();

        FindAndClickOpenCVOld findAndClick = new FindAndClickOpenCVOld();
        // Call the necessary methods here or modify as needed

    }

}
