package excel.util;

/**
 * id, testCase, method, baseUrl, interfaceUrl, requests_param, response_expect, response_actual, result
 *
 * @author chenyanqing
 */
public class ModeExceUtil extends ValueSet<ExcelMode> {

    void value(int index, String str, ExcelMode mode) {
        switch (index) {
            case 0:
                mode.setId(str);
                break;
            case 1:
                mode.setTestCase(str);
                break;
            case 2:
                mode.setMethod(str);
                break;
            case 3:
                mode.setBaseUrl(str);
                break;
            case 4:
                mode.setInterfaceUrl(str);
                break;
            case 5:
                mode.setRequests_param(str);
                break;
            case 6:
                mode.setResponse_expect(str);
                break;
            case 7:
                mode.setResponse_actual(str);
                break;
        }
    }

}
