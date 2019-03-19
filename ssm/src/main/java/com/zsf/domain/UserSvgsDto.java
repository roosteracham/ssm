package com.zsf.domain;

import java.util.ArrayList;

public class UserSvgsDto {

    private int authId;

    private int userId;

    private ArrayList<Integer> svgIds;

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Integer> getSvgIds() {
        return svgIds;
    }

    public void setSvgIds(ArrayList<Integer> svgIds) {
        this.svgIds = svgIds;
    }
}
