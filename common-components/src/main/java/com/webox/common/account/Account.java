package com.webox.common.account;


public abstract class Account{
    AccountRegister accountRegister;
    String accountName;
    String accountAvatar;
    String accountId;
    String accountPassHash;
    

    public abstract void testLogin();

    public void loginAccount(){
       
        //accountRegister.loginAccount();
    }
   
}