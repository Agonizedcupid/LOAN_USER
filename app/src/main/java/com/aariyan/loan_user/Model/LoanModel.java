package com.aariyan.loan_user.Model;

public class LoanModel {

    private String id,userId,name,dob,gender,address,aadhar,pan,uploadProof,uploadProofImage,loanCategory,interestRate,status;
    private String loanAmount,tenure;

    public LoanModel(){}

    public LoanModel(String id, String userId, String name, String dob, String gender, String address, String aadhar, String pan, String uploadProof, String uploadProofImage, String loanCategory, String interestRate, String status, String loanAmount, String tenure) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.aadhar = aadhar;
        this.pan = pan;
        this.uploadProof = uploadProof;
        this.uploadProofImage = uploadProofImage;
        this.loanCategory = loanCategory;
        this.interestRate = interestRate;
        this.status = status;
        this.loanAmount = loanAmount;
        this.tenure = tenure;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    //    public LoanModel(String id, String userId, String name, String dob, String gender, String address, String aadhar, String pan, String uploadProof, String uploadProofImage, String loanCategory, String interestRate, String status) {
//        this.id = id;
//        this.userId = userId;
//        this.name = name;
//        this.dob = dob;
//        this.gender = gender;
//        this.address = address;
//        this.aadhar = aadhar;
//        this.pan = pan;
//        this.uploadProof = uploadProof;
//        this.uploadProofImage = uploadProofImage;
//        this.loanCategory = loanCategory;
//        this.interestRate = interestRate;
//        this.status = status;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //    public LoanModel(String id, String userId, String name, String dob, String gender, String address, String aadhar, String pan, String uploadProof, String uploadProofImage, String loanCategory, String interestRate) {
//        this.id = id;
//        this.userId = userId;
//        this.name = name;
//        this.dob = dob;
//        this.gender = gender;
//        this.address = address;
//        this.aadhar = aadhar;
//        this.pan = pan;
//        this.uploadProof = uploadProof;
//        this.uploadProofImage = uploadProofImage;
//        this.loanCategory = loanCategory;
//        this.interestRate = interestRate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getUploadProof() {
        return uploadProof;
    }

    public void setUploadProof(String uploadProof) {
        this.uploadProof = uploadProof;
    }

    public String getUploadProofImage() {
        return uploadProofImage;
    }

    public void setUploadProofImage(String uploadProofImage) {
        this.uploadProofImage = uploadProofImage;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
}
