package com.webox.common.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.webox.common.model.Account;
import com.webox.common.model.Address;
import com.webox.common.model.RawData;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.User;
import com.webox.common.utils.AppConsts;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("weixinAccount")
public class WeiXinAccountRegister extends AccountRegister {
    private static final Logger logger = LoggerFactory.getLogger(WeiXinAccountRegister.class);

    private Response response;
    @Autowired
    private JWToken jwtoken;
    @Autowired
    @Qualifier("messageManage")
    MessageManage messageManage;

    @Value("${encrypted.weixinSecretKey}")
    private String weixinSecretKey;

    @Value("${encrypted.weixinAppId}")
    private String weixinAppId;

    public WeiXinAccountRegister() {
        this.response = new Response();
        this.response.setAppStatus(AppConsts.RETURN_TRUE);

    }

    @Override
    public Response register(Request request) {
        if (request.getUser() == null) {
            request.setUser(new User(request.getAccount().getOpenId() + "@unconfirming"));
        } else if (request.getUser().getPrimaryEmail() == null) {
            request.getUser().setPrimaryEmail(request.getAccount().getOpenId() + "@unconfirming");
        }

        request.getUser()
                .setRefVendorId(this.getRefreferalVendorId(request.getReferralId(), request.getReferralType()));
        request.getUser().setRegisterTime(Calendar.getInstance().getTime());
        request.getUser().setUserRole(AppConsts.userRole.CUSTOMER.toString());

        User newUser = request.getUser();
        Account account = new Account(request.getAccount().getOpenId(), request.getAccount().getAccName(),
                request.getAccount().getAccAvatar(), AppConsts.accountType.WECHAT.toString());
        boolean newAcc = !this.accountExist(request.getAccount().getOpenId());
        List<User> userList = this.loadUserbyEmail(request.getUser().getPrimaryEmail());

        if (userList.size() > 0) {
            User existUer = (User) userList.get(0);
            if (newAcc) {
                // send a emssage to new account
                /*
                 * Message msg = messageManage.constructMessageObj(existUer.getUserId(),
                 * "sender", "text", "尊敬的用户，欢迎来到友客世界!"); msg.setMsgStatus(0);// in Q
                 * msg.setMsgInQReason(2);// websocket disconnnected msg.setSendOutTime(null);
                 * // yet to be sent msg.setTrytoSendRound(0); messageManage.saveMessage(msg);
                 */
                // end of send
                existUer.addUserAccount(account);
                userRepository.save(existUer);
                response.setAppInfo(AppConsts.registerInfo.REGISTERSUCESS.toString()); // new account register
                response.setAppCode(AppConsts.REG_REGISTERSUCESS); // new account register
            } else {
                response.setAppInfo(AppConsts.registerInfo.ACCEXIST.toString());
                response.setAppCode(AppConsts.REG_ACCEXIST);
            }

        } else {
            if (newAcc) {
                newUser.addUserAccount(account);
                userRepository.save(newUser);
                // send a emssage to new account
                /*
                 * Message msg = messageManage.constructMessageObj(newUser.getUserId(),
                 * "sender", "text", "尊敬的用户，欢迎来到友客世界!"); msg.setMsgStatus(0);// in Q
                 * msg.setMsgInQReason(2);// websocket disconnnected msg.setSendOutTime(null);
                 * // yet to be sent msg.setTrytoSendRound(0); messageManage.saveMessage(msg);
                 */
                // end of send
                response.setAppInfo(AppConsts.registerInfo.REGISTERSUCESS.toString()); // new account and user register
                response.setAppCode(AppConsts.REG_REGISTERSUCESS);
            } else {
                response.setAppInfo(AppConsts.registerInfo.ACCEXIST.toString());
                response.setAppCode(AppConsts.REG_ACCEXIST);
            }
        }
        // set user and account into response
        User responseUser = this.loadUserbyEmail(request.getUser().getPrimaryEmail()).get(0);
        Account responseAccount = this.loadAccount(request.getAccount().getOpenId()).get(0);
        responseUser.setUserAccount(null); // clear account info in response
        response.setUser(responseUser);
        response.setAccount(responseAccount);
        return response;
    }

    @Override
    public Response signOut(Request request) {
        List<Account> accountList = this.loadAccount(request.getAccount().getOpenId());
        if (accountList.size() > 0) {
            Account account = (Account) accountList.get(0);
            account.setAccStatus(AppConsts.accountStatus.SINGOUT.toString());
            account.setLastTimeLogout(LocalDateTime.now());
            userRepository.updateNestedAccount(account);
            this.response.setAppInfo(AppConsts.registerInfo.SINGOUT.toString());
            response.setAppCode(AppConsts.REG_SINGOUT);
            // set user and account into response
            User responseUser = this.loadUserByAccountId(request.getAccount().getOpenId()).get(0);
            responseUser.setUserAccount(null); // clear account info in response
            response.setUser(responseUser);
            response.setAccount(account);
            response.setToken(null);
            return this.response;
        } else {
            this.response.setAppInfo(AppConsts.registerInfo.ACCOUNTNOTFOUND.toString());
            response.setAppCode(AppConsts.REG_CCOUNTNOTFOUND);
        }
        return this.response;
    }

    @Override
    public Response signAccount(Request request) {
        // call weixin
        HashMap<String, String> map = this.callWeixinByCode(request.getWeixinCode());
        if (map == null || map.get("openid") == null) {
            response.setAppStatus("false");
            response.setAppErr("call wechat errors occured.");
            logger.error("Unsuccessfull obtain open id with account of {} ",
                    request.getWeixinCode() + " weixin name" + request.getRawData());
            return response;
        }

        String openId = map.get("openid");
        this.response.setToken(jwtoken.generateToken(openId).getToken());

        request.setAccount(new Account());
        request.setUser(new User());
        request.getAccount().setOpenId(openId);
        RawData rawData = new Gson().fromJson(request.getRawData(), RawData.class);
        if (rawData != null) {

            if (rawData.getAvatarUrl() != null && !"".equalsIgnoreCase(rawData.getAvatarUrl())) {
                request.getAccount().setAccAvatar(rawData.getAvatarUrl());
            }

            if (rawData.getNickName() != null && !"".equalsIgnoreCase(rawData.getNickName())) {
                request.getAccount().setAccName(rawData.getNickName());
            }

            if (rawData.getGender() != null && !"".equalsIgnoreCase(rawData.getGender())) {
                request.getUser().setGender(rawData.getGender());
            }

            if (rawData.getLanguage() != null && !"".equalsIgnoreCase(rawData.getLanguage())) {
                request.getUser().setPreferLanguage(rawData.getLanguage());
            }
            request.getUser().setAddress(new Address());

            if (rawData.getCity() != null && !"".equalsIgnoreCase(rawData.getCity())) {
                request.getUser().getAddress().setCity(rawData.getCity());
            }

            if (rawData.getCountry() != null && !"".equalsIgnoreCase(rawData.getCountry())) {
                request.getUser().getAddress().setCountry(rawData.getCountry());
            }

            if (rawData.getProvince() != null && !"".equalsIgnoreCase(rawData.getProvince())) {
                request.getUser().getAddress().setState(rawData.getProvince());
            }

        }

        List<Account> accountList = this.loadAccount(openId);
        if (accountList.size() > 0) {
            Account account = (Account) accountList.get(0);
            if (request.getAccount().getAccAvatar() != null) {
                account.setAccAvatar(request.getAccount().getAccAvatar());
            }
            // System.out.println("-- Avatar --->" + account.getAccAvatar());

            if (request.getAccount().getAccName() != null) {
                account.setAccName(request.getAccount().getAccName());
            }

            account.setVisitNumber(account.getVisitNumber() + 1);
            account.setAccStatus(AppConsts.accountStatus.SIGNIN.toString());
            account.setLastTimeLogin(LocalDateTime.now());
            userRepository.updateNestedAccount(account);
            this.response.setAppInfo(AppConsts.registerInfo.SINGINSUCESS.toString());
            this.response.setAppCode(AppConsts.REG_SINGINSUCESS);
            // set user and account into response
            User responseUser = this.loadUserByAccountId(request.getAccount().getOpenId()).get(0);

            if (responseUser.getPreferLanguage() == null || "".equals(responseUser.getPreferLanguage())) {
                responseUser.setPreferLanguage(request.getUser().getPreferLanguage());
            }
            if (responseUser.getGender() == null || "".equals(responseUser.getGender())) {
                responseUser.setGender(request.getUser().getGender());
            }
            if (responseUser.getAddress() == null) {
                responseUser.setAddress(request.getUser().getAddress());
            }
            userRepository.save(responseUser);
            responseUser.setUserAccount(null); // clear account info in response
            response.setUser(responseUser);
            response.setAccount(account);

            return this.response;
        } else { // auto register if it is a new account
            return register(request);
        }

    }

    private HashMap<String, String> callWeixinByCode(String code) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + this.weixinAppId
                    + "&secret=" + this.weixinSecretKey + "&js_code=" + code + "&grant_type=authorization_code");
            HttpResponse response = client.execute(request);
            logger.info("Response of calling wechat api for obtain session key and open id {} ",
                    response.getStatusLine().getStatusCode());
            // System.out.println("----->" +
            // "https://api.weixin.qq.com/sns/jscode2session?appid=" + this.weixinAppId
            // + "&secret=" + this.weixinSecretKey + "&js_code=" + code +
            // "&grant_type=authorization_code");
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            // System.out.println("----->" + result.toString());
            JSONObject jObject = new JSONObject(result.toString());
            Iterator<?> keys = jObject.keys();
            HashMap<String, String> map = new HashMap<String, String>();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jObject.getString(key);
                map.put(key, value);
            }

            logger.debug("Response of calling wechat api for obtain session key and open id {} ", map.toString());
            return map;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("ClientProtocolException {} ", e.getMessage());
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            logger.error("UnsupportedOperationException {} ", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException {} ", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("IOException {} ", e.getMessage());
        }
        return null;

    }

    public User loadUserInfo(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return this.maskSencitiveInfo(user);
        } else {
            return null;
        }

    }

    private User maskSencitiveInfo(User user) {
        user.setDateofBirth(null);
        user.setSocialID("######");
        user.setPersnalGreetings("######");
        user.setRefVendorId("######");
        user.setPersnalPhoto("######");
        user.setRegisterTime(null);
        user.setUserRole("######");
        user.setPrimaryEmail("######");
        List<Account> accountList = user.getUserAccount().stream()
                .filter(o -> AppConsts.accountType.WECHAT.toString().equalsIgnoreCase(o.getAccType()))
                .collect(Collectors.toList());
        accountList.forEach(o -> {
            o.setAccCreateTime(null);
            o.setAccStatus("#######");
            o.setLastTimeLogout(null);
            o.setAccountId("######");
            o.setLastTimeLogin(null);
            o.setOpenId("######");
            o.setLastTimeLogin(null);
            o.setVisitNumber(999999);

        });

        user.setUserAccount(accountList);
        return user;
    }

    private List<User> loadUserbyEmail(String emailAddress) {
        List<User> userList = new ArrayList<User>();
        userList = userRepository.findByPrimaryEmail(emailAddress);
        return userList;
    }

    private List<User> loadUserByAccountId(String accountId) {
        List<User> userList = userRepository.findByNestedAccountId(accountId);
        return userList;
    }

    private List<Account> loadAccount(String accountId) {
        List<Account> accountList = new ArrayList<Account>();
        List<User> userList = userRepository.findByNestedAccountId(accountId);
        userList.forEach(user -> {
            user.getUserAccount().forEach(account -> accountList.add(account));
        });
        return accountList.stream().filter(o -> o.getAccountId().equalsIgnoreCase(accountId))
                .collect(Collectors.toList());
    }

    private boolean accountExist(String accountId) {
        return loadAccount(accountId).size() > 0;
    }

    private String getRefreferalVendorId(String referralId, String referralType) {
        if (referralId == null)
            return "001";
        if ("Vendor".equalsIgnoreCase(referralType)) {
            return referralId;
        } else if ("Service".equalsIgnoreCase(referralType)) {
            return "001"; /// not completed yet ,need get vendorId by service id
        } else {
            return "001";
        }
    }
}