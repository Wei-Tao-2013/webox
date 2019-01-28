package com.webox.common.model;

import java.time.LocalDateTime;

import com.webox.common.utils.AppConsts;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Account {
    @Id
    private String accountId;
    private String openId;
    private String accName;
    private String accAvatar;
    private LocalDateTime accCreateTime;
    private LocalDateTime lastTimeLogin;
    private LocalDateTime lastTimeLogout;
    private int visitNumber; // record times of visiting
    private String accType; // wechat , web, iosApp, andApp
    private String accStatus;

    public Account(String openId, String accName, String accAvtar, String accType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.openId = openId;
        this.accName = accName;
        this.accAvatar = accAvtar;
        this.accountId = openId;
        this.accType = accType; // wechat;
        this.accCreateTime = localDateTime;
        this.lastTimeLogin = localDateTime;
        this.lastTimeLogout = localDateTime;
        this.visitNumber = 1;
        this.accStatus = AppConsts.accountStatus.CREATED.toString();
    }

}
