package com.monstercode.contactsapp.data;

import com.monstercode.contactsapp.Detail;

public  class OneDetail {
    public static void setDetails(Detail detail) {
        id = detail.getId();
        firstname = detail.getFirstname();
        lastname = detail.getLastname();
        name = detail.getFirstname() + " " + detail.getLastname();
        tel1 = detail.getTel1();
        tel2 = detail.getTel2();
        email = detail.getEmail();
        sitename = detail.getSitename();
        email = detail.getEmail();
        job = detail.getJob();
        category = detail.getCategory();

    }
    public static Detail getDetail() {
        Detail detail = new Detail();
        detail.setId(id);
        detail.setFirstname(firstname);
        detail.setLastname(lastname);
        detail.setTel1(tel1);
        detail.setTel2(tel2);
        detail.setSitename(sitename);
        detail.setJob(job);
        detail.setEmail(email);
        detail.setCategory(category);
        detail.setSitename(sitename);

        return detail;
    }

    private static int id;
    private static String name, firstname, lastname, tel1, tel2, email, job, sitename, category;

    public static String getFirstname() {
        return firstname;
    }

    public static void setFirstname(String firstname) {
        OneDetail.firstname = firstname;
    }

    public static String getLastname() {
        return lastname;
    }

    public static void setLastname(String lastname) {
        OneDetail.lastname = lastname;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        OneDetail.name = name;
    }

    public static String getTel1() {
        return tel1;
    }

    public static void setTel1(String tel1) {
        OneDetail.tel1 = tel1;
    }

    public static String getTel2() {
        return tel2;
    }

    public static void setTel2(String tel2) {
        OneDetail.tel2 = tel2;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        OneDetail.email = email;
    }

    public static String getJob() {
        return job;
    }

    public static void setJob(String job) {
        OneDetail.job = job;
    }

    public static String getSitename() {
        return sitename;
    }

    public static void setSitename(String sitename) {
        OneDetail.sitename = sitename;
    }

    public static String getCategory() {
        return category;
    }

    public static void setCategory(String category) {
        OneDetail.category = category;
    }
}
