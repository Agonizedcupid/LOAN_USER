package com.aariyan.loan_user.Notification;

public class Data implements Comparable<Data> {

    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;
    private String types, id;

    public Data() {
    }


    public Data(String user, int icon, String body, String title, String sented, String types, String id) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.types = types;
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    @Override
    public int compareTo(Data data) {
        return getTitle().compareTo(data.getTitle());
    }
}
