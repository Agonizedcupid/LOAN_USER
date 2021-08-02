package com.aariyan.loan_user.Model;

public class LoanCategoryModel {

    private String id,typeOfLoan,interest,timeStart,timeEnd,memberPercentage,nonMemberPercentage;

    public LoanCategoryModel(){}

    public LoanCategoryModel(String id, String typeOfLoan, String interest, String timeStart, String timeEnd, String memberPercentage, String nonMemberPercentage) {
        this.id = id;
        this.typeOfLoan = typeOfLoan;
        this.interest = interest;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.memberPercentage = memberPercentage;
        this.nonMemberPercentage = nonMemberPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeOfLoan() {
        return typeOfLoan;
    }

    public void setTypeOfLoan(String typeOfLoan) {
        this.typeOfLoan = typeOfLoan;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getMemberPercentage() {
        return memberPercentage;
    }

    public void setMemberPercentage(String memberPercentage) {
        this.memberPercentage = memberPercentage;
    }

    public String getNonMemberPercentage() {
        return nonMemberPercentage;
    }

    public void setNonMemberPercentage(String nonMemberPercentage) {
        this.nonMemberPercentage = nonMemberPercentage;
    }
}
