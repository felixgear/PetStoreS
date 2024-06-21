package com.felixgear.petstores.utils;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;

public class DynamicGroupSelector implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        String selection = System.getProperty("selection", "");
        String[] selectionGroups = selection.split(",");
        List<String> includedGroups = context.getSuite().getXmlSuite().getIncludedGroups();
        List<String> selectedGroups = new ArrayList<String>();
        for (String selected : selectionGroups) {
            if (includedGroups.contains(selected)) {
        	selectedGroups.add(selected);
            }
	}
        context.getSuite().getXmlSuite().setIncludedGroups(selectedGroups);
    }

    // Implement the other methods of ITestListener if necessary
}