package app.module.util;

import com.authine.cloudpivot.engine.api.model.organization.UserModel;
import com.authine.cloudpivot.web.api.exception.PortalException;
import com.authine.cloudpivot.web.api.exception.ResultEnum;
import com.authine.cloudpivot.web.api.service.EngineService;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class UserUtil {

    private static final String USER_ACCOUNT_EMPTY = "用户账户为空";

    @Resource
    EngineService engineService;

    /**
     * 检查字符串是否为空，为空，将抛出异常
     *
     * @param value  字符串
     * @param errMsg 异常信息
     */
    public void validateNotEmpty(String value, String errMsg) {
        if (Strings.isNullOrEmpty(value)) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), errMsg);
        }
    }

    /**
     * 获取应用编码从账户信息
     *
     * @param account 用户账户
     * @return 应用编码
     */
    public String getAppCodeFromAccount(String account) {
        validateNotEmpty(account, USER_ACCOUNT_EMPTY);
        return "report_for_" + account;
    }

    /**
     * 获取用户信息从用户账户
     *
     * @param account 用户账户
     * @return 用户信息
     */
    public UserModel getUserInfoFromAccount(String account) {
        validateNotEmpty(account, USER_ACCOUNT_EMPTY);
        UserModel userInfo = engineService.getUserFacade().getFirstByUserName(account);
        if (Objects.isNull(userInfo)) {
            throw new PortalException(ResultEnum.ILLEGAL_PARAMETER_ERR.getErrCode(), USER_ACCOUNT_EMPTY);
        }
        return userInfo;
    }
}
