package com.qiren.portal.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.RestManager;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class RequestResendController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/forward")
    public CommonResponse forwardRequest(
            HttpServletRequest servletRequest,
            @RequestParam String path,
            @RequestBody HashMap<String, Object> requestBody) {
        Logger.log("forwardRequest");
        UserBean userBean = loginService.getLoginUser(servletRequest);

        if (null == userBean) {
            return CommonUtils.fail("Not login");
        }
        
        CommonUserEntity userEntity = 
        		loginService.findFullUserInfoByName(CommonUtils.md5(userBean.getUsername()));

        String username = userBean.getUsername();
        String password = userEntity.getPassword();
        String type = userBean.getType();

        String token = CommonUtils.md5(username + "_" + password + "_" + type);

        // String token = "9c10ed072f149d90991a723099b68aa3";

        String role = userBean.getRole();
        // String role = "distributor";
        Role enumCompanyRole = Role.valueOf(role.toUpperCase());

        Logger.log(token);
        
        String url = "";

        switch (enumCompanyRole) {
            case SUPPLIER: {
                url = Constants.URL_SUPPLIER;
                break;
            }
            case DISTRIBUTOR: {
                url = Constants.URL_DISTRIBUTOR;
                break;
            }
            case MANUFACTURER: {
                url = Constants.URL_MANUFACTURER;
                break;
            }
            case ROUTER: {
                url = Constants.URL_ROUTER;
                break;
            }
            default:
                return CommonUtils.fail("Invalid input");
        }
        url += UriUtils.decode(path, StandardCharsets.UTF_8);

        Map<String, Object> headers = new HashMap<>();
        headers.put("customToken", token);
        headers.put("username", username);
        headers.put("password", password);
        headers.put("type", type);

        Logger.log(url);
        
        CommonResponse remoteResponse = RestManager.getInstance().sendHttpPostWithHeader(restTemplate, url, headers,
                requestBody);
        return remoteResponse;
    }
}
