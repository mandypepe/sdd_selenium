package com.project.tests.contracts;

import com.project.pages.PeopleSectionPage;
import com.project.pages.components.AlphabetFilterComponent;
import com.project.tests.base.BaseTest;
import com.project.utils.ReportLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Contract test for {@link AlphabetFilterComponent}.
 *
 * Verifies that the component exposes the required public API and that
 * the delegating methods on {@link PeopleSectionPage} are present.
 * These tests are structural (reflection-based) and do not require a live browser
 * for the API-shape assertions; the live-browser tests are in
 * {@code AnyFilterDisplaysGeneralListTest} and {@code AnyFilterPaginationTest}.
 */
@Feature("Alphabetical Filter")
public class AlphabetFilterComponentContractTest extends BaseTest {

    // -------------------------------------------------------------------------
    // AlphabetFilterComponent API shape
    // -------------------------------------------------------------------------

    @Test
    @Story("Contract - AlphabetFilterComponent API")
    @Description("AlphabetFilterComponent must expose selectAny() with no parameters.")
    public void alphabetFilterComponent_hasSelectAnyMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying AlphabetFilterComponent.selectAny() exists");
        Method method = AlphabetFilterComponent.class.getMethod("selectAny");
        Assert.assertEquals(method.getReturnType(), void.class,
                "selectAny() must return void");
        Assert.assertEquals(method.getParameterCount(), 0,
                "selectAny() must take no parameters");
    }

    @Test
    @Story("Contract - AlphabetFilterComponent API")
    @Description("AlphabetFilterComponent must expose selectLetter(String) with one String parameter.")
    public void alphabetFilterComponent_hasSelectLetterMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying AlphabetFilterComponent.selectLetter(String) exists");
        Method method = AlphabetFilterComponent.class.getMethod("selectLetter", String.class);
        Assert.assertEquals(method.getReturnType(), void.class,
                "selectLetter(String) must return void");
        Assert.assertEquals(method.getParameterCount(), 1,
                "selectLetter(String) must take exactly one parameter");
    }

    @Test
    @Story("Contract - AlphabetFilterComponent API")
    @Description("AlphabetFilterComponent must expose getActiveLetter() returning String.")
    public void alphabetFilterComponent_hasGetActiveLetterMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying AlphabetFilterComponent.getActiveLetter() exists");
        Method method = AlphabetFilterComponent.class.getMethod("getActiveLetter");
        Assert.assertEquals(method.getReturnType(), String.class,
                "getActiveLetter() must return String");
        Assert.assertEquals(method.getParameterCount(), 0,
                "getActiveLetter() must take no parameters");
    }

    @Test
    @Story("Contract - AlphabetFilterComponent API")
    @Description("AlphabetFilterComponent must expose isAnySelected() returning boolean.")
    public void alphabetFilterComponent_hasIsAnySelectedMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying AlphabetFilterComponent.isAnySelected() exists");
        Method method = AlphabetFilterComponent.class.getMethod("isAnySelected");
        Assert.assertEquals(method.getReturnType(), boolean.class,
                "isAnySelected() must return boolean");
        Assert.assertEquals(method.getParameterCount(), 0,
                "isAnySelected() must take no parameters");
    }

    // -------------------------------------------------------------------------
    // PeopleSectionPage delegating API shape
    // -------------------------------------------------------------------------

    @Test
    @Story("Contract - PeopleSectionPage delegation")
    @Description("PeopleSectionPage must expose applyAnyFilter() delegating to AlphabetFilterComponent.")
    public void peopleSectionPage_hasApplyAnyFilterMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying PeopleSectionPage.applyAnyFilter() exists");
        Method method = PeopleSectionPage.class.getMethod("applyAnyFilter");
        Assert.assertEquals(method.getReturnType(), void.class,
                "applyAnyFilter() must return void");
    }

    @Test
    @Story("Contract - PeopleSectionPage delegation")
    @Description("PeopleSectionPage must expose applyLetterFilter(String) delegating to AlphabetFilterComponent.")
    public void peopleSectionPage_hasApplyLetterFilterMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying PeopleSectionPage.applyLetterFilter(String) exists");
        Method method = PeopleSectionPage.class.getMethod("applyLetterFilter", String.class);
        Assert.assertEquals(method.getReturnType(), void.class,
                "applyLetterFilter(String) must return void");
    }

    @Test
    @Story("Contract - PeopleSectionPage delegation")
    @Description("PeopleSectionPage must expose isAnyFilterActive() returning boolean.")
    public void peopleSectionPage_hasIsAnyFilterActiveMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying PeopleSectionPage.isAnyFilterActive() exists");
        Method method = PeopleSectionPage.class.getMethod("isAnyFilterActive");
        Assert.assertEquals(method.getReturnType(), boolean.class,
                "isAnyFilterActive() must return boolean");
    }

    @Test
    @Story("Contract - PeopleSectionPage delegation")
    @Description("PeopleSectionPage must expose getActiveFilterLetter() returning String.")
    public void peopleSectionPage_hasGetActiveFilterLetterMethod() throws NoSuchMethodException {
        ReportLogger.log("Contract: verifying PeopleSectionPage.getActiveFilterLetter() exists");
        Method method = PeopleSectionPage.class.getMethod("getActiveFilterLetter");
        Assert.assertEquals(method.getReturnType(), String.class,
                "getActiveFilterLetter() must return String");
    }

    // -------------------------------------------------------------------------
    // Live-browser smoke: component instantiates and returns safe defaults
    // -------------------------------------------------------------------------

    @Test
    @Story("Contract - AlphabetFilterComponent live smoke")
    @Description("AlphabetFilterComponent.isAnySelected() must not throw when called on the loaded page.")
    public void alphabetFilterComponent_isAnySelectedDoesNotThrow() {
        ReportLogger.log("Contract smoke: AlphabetFilterComponent.isAnySelected() on live page");
        AlphabetFilterComponent component = new AlphabetFilterComponent(driver);
        // Should not throw — returns true or false gracefully
        boolean result = component.isAnySelected();
        ReportLogger.log("isAnySelected() returned: " + result + " (no exception thrown — contract satisfied)");
        // No assertion on the value; we only assert it does not throw
        Assert.assertTrue(true, "isAnySelected() completed without exception");
    }

    @Test
    @Story("Contract - AlphabetFilterComponent live smoke")
    @Description("AlphabetFilterComponent.getActiveLetter() must return a non-null String on the loaded page.")
    public void alphabetFilterComponent_getActiveLetterReturnsNonNull() {
        ReportLogger.log("Contract smoke: AlphabetFilterComponent.getActiveLetter() on live page");
        AlphabetFilterComponent component = new AlphabetFilterComponent(driver);
        String activeLetter = component.getActiveLetter();
        Assert.assertNotNull(activeLetter,
                "getActiveLetter() must never return null — expected empty string or a letter value");
        ReportLogger.log("getActiveLetter() returned: '" + activeLetter + "' (non-null — contract satisfied)");
    }
}
