package com.aariyan.loan_user.Model;

public class TransactionModel {
    private String id,userId,transactionName,transactionMonth,transactionRecordDate;
    private String transactionFine,transactionAmount;
    private String transactionTenure,transactionInterest,memberType;
    private String title;

    public TransactionModel(){}

    public TransactionModel(String id, String userId, String transactionName, String transactionMonth, String transactionRecordDate, String transactionFine, String transactionAmount, String transactionTenure, String transactionInterest, String memberType,String title) {
        this.id = id;
        this.userId = userId;
        this.transactionName = transactionName;
        this.transactionMonth = transactionMonth;
        this.transactionRecordDate = transactionRecordDate;
        this.transactionFine = transactionFine;
        this.transactionAmount = transactionAmount;
        this.transactionTenure = transactionTenure;
        this.transactionInterest = transactionInterest;
        this.memberType = memberType;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getTransactionTenure() {
        return transactionTenure;
    }

    public void setTransactionTenure(String transactionTenure) {
        this.transactionTenure = transactionTenure;
    }

    public String getTransactionInterest() {
        return transactionInterest;
    }

    public void setTransactionInterest(String transactionInterest) {
        this.transactionInterest = transactionInterest;
    }

    //    public TransactionModel(String id, String userId, String transactionName, String transactionMonth, String transactionRecordDate, String transactionFine, String transactionAmount) {
//        this.id = id;
//        this.userId = userId;
//        this.transactionName = transactionName;
//        this.transactionMonth = transactionMonth;
//        this.transactionRecordDate = transactionRecordDate;
//        this.transactionFine = transactionFine;
//        this.transactionAmount = transactionAmount;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionMonth() {
        return transactionMonth;
    }

    public void setTransactionMonth(String transactionMonth) {
        this.transactionMonth = transactionMonth;
    }

    public String getTransactionRecordDate() {
        return transactionRecordDate;
    }

    public void setTransactionRecordDate(String transactionRecordDate) {
        this.transactionRecordDate = transactionRecordDate;
    }

    public String getTransactionFine() {
        return transactionFine;
    }

    public void setTransactionFine(String transactionFine) {
        this.transactionFine = transactionFine;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
