package com.zsf.util.errorcode;

public enum MailServerEnum {

    QQ("qq.com", 0),
    NETEASE("163.com", 1),
    MS("outlook.com", 2);

    private String email;

    private int index;

    MailServerEnum(String email, int index) {
        this.email = email;
        this.index = index;
    }

    public static int getEmailServer(String email) {
        for (MailServerEnum c : MailServerEnum.values()) {
            if (c.getEmail().equals(email)) {
                return c.getIndex();
            }
        }
        return -1;
    }

    public int getIndex() {
        return index;
    }

    public String getEmail() {
        return email;
    }
}
