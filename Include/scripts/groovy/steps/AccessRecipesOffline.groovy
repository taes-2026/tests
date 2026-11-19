package steps

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import io.cucumber.java.en.Given
import io.cucumber.java.en.When

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * SMAR-51 - access saved recipes offline.
 *
 * Navigation stays inside the SPA: no reload and no service worker. The test
 * records external fetch responses while online and replays them when offline,
 * while requests to the local app on localhost are always allowed.
 *
 * Katalon wraps Chrome in a RemoteWebDriver proxy that does not implement
 * HasCdp/HasNetworkConditions, so the offline mode is emulated at page level.
 */
class AccessRecipesOffline {

    @Given('I prepare the offline cache')
    def prepareOfflineCache() {
        WebUI.executeJavaScript('''
            if (!window.__smar51CacheReady) {
                window.__smar51CacheReady = true;
                window.__smar51Offline = false;
                window.__smar51ResponseCache = {};
                window.__smar51OriginalFetch = window.fetch;
                window.__smar51OriginalXhrOpen = XMLHttpRequest.prototype.open;

                window.__smar51IsLocal = function (value) {
                    try {
                        var url = new URL(value, window.location.href);
                        return url.hostname === 'localhost' ||
                               url.hostname === '127.0.0.1' ||
                               url.protocol === 'data:' ||
                               url.protocol === 'blob:';
                    } catch (e) {
                        return false;
                    }
                };

                window.fetch = function (input) {
                    var target = (input && input.url) ? input.url : String(input);

                    if (window.__smar51IsLocal(target)) {
                        return window.__smar51OriginalFetch.apply(this, arguments);
                    }

                    if (window.__smar51Offline) {
                        var cached = window.__smar51ResponseCache[target];
                        if (cached) {
                            console.info('[SMAR-51] Replaying cached response: ' + target);
                            return Promise.resolve(cached.clone());
                        }
                        console.warn('[SMAR-51] No cached response for: ' + target);
                        return Promise.reject(new TypeError('Failed to fetch (offline, uncached): ' + target));
                    }

                    return window.__smar51OriginalFetch.apply(this, arguments).then(function (response) {
                        try {
                            window.__smar51ResponseCache[target] = response.clone();
                        } catch (e) {
                            console.warn('[SMAR-51] Could not cache response: ' + target);
                        }
                        return response;
                    });
                };

                XMLHttpRequest.prototype.open = function (method, url) {
                    if (!window.__smar51IsLocal(String(url)) && window.__smar51Offline) {
                        throw new Error('Offline: network disabled: ' + url);
                    }
                    return window.__smar51OriginalXhrOpen.apply(this, arguments);
                };
            }
        ''', null)
    }

    @When('I go offline')
    def goOffline() {
        setNetworkFlag(true)
    }

    @When('I restore the network')
    def restoreNetwork() {
        setNetworkFlag(false)
    }

    @When('I open the first saved recipe')
    def openFirstSavedRecipe() {
        WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/recipe_card'), 10)
        WebUI.click(findTestObject('Page_SmartPantry/recipe_card'))
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/button_Comear a cozinhar'), 10)
    }

    private static void setNetworkFlag(boolean offline) {
        String eventName = offline ? 'offline' : 'online'
        WebUI.executeJavaScript('''
            window.__smar51Offline = ''' + offline + ''';
            try {
                Object.defineProperty(navigator, 'onLine', {
                    configurable: true,
                    get: function () { return ''' + !offline + '''; }
                });
            } catch (e) {
                // navigator.onLine cannot always be redefined; fetch is already controlled.
            }
            window.dispatchEvent(new Event("''' + eventName + '''"));
        ''', null)
    }
}
