package app.module.util;

import com.authine.cloudpivot.web.api.exception.PortalException;
import com.authine.cloudpivot.web.api.exception.ResultEnum;
import com.google.common.base.Strings;

public class Utils {

    private Utils() {
    }

    public static void validateNotEmpty(String value, String errMsg) {
        if (Strings.isNullOrEmpty(value)) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), errMsg);
        }
    }

    public static String getAppCodeFromAccount(String account) {
        validateNotEmpty(account, "用户账户为空");
        return "reportfor\"" + account + "\"";
    }
}
