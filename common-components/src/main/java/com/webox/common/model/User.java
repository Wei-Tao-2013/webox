package com.webox.common.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.webox.common.model.Account;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data @NoArgsConstructor
@EqualsAndHashCode(of = {"primaryEmail"})
public class User {
  //private List<Role> roleList;
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String midName;
    private LocalDate dateofBirth;
    private String Gender;  //male , female, unknown 
    private String refVendorId; 
    private String persnalGreetings;
    private String persnalPhoto;
    private String userRole;
    private String wechatId;
    private String userExperience;
    private boolean certified;
    private String  preferLanguage;
    @Indexed
    private String primaryEmail;
    private String secondEmail;
    private String socialID; // wechat id
    private String timeZone;
    private Date registerTime;
    private List<Account> userAccount = new ArrayList<Account>();
    private Contact contact;
    private Address address;

    public User(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public User addUserAccount(Account account) {
        this.userAccount.add(account);
        return this;
    }

    
}