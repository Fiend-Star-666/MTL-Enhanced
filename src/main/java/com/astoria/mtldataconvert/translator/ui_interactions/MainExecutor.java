package com.astoria.mtldataconvert.translator.ui_interactions;

import com.astoria.mtldataconvert.translator.AutomatedBrowserInteractionOld;
import com.astoria.mtldataconvert.translator.FindAndClickOpenCVOld;

public class MainExecutor {

    public static void main(String[] args) throws Exception {
        AutomatedBrowserInteractionOld browserInteraction = new AutomatedBrowserInteractionOld();
        browserInteraction.initializeWebDriver();

        FindAndClickOpenCVOld findAndClick = new FindAndClickOpenCVOld();
        // Call the necessary methods here or modify as needed

    }

}
