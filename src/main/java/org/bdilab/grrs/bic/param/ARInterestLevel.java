package org.bdilab.grrs.bic.param;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
public enum ARInterestLevel {
    LG("LG K-3"), MG("MG 4-8"), MG1("MG+ 6 AND UP"), UG("UG 9-12");

    private String fullname;
    ARInterestLevel(String fullname) {
        this.fullname = fullname;
    }

    public static ARInterestLevel find(String fullname) {
        for (ARInterestLevel il: ARInterestLevel.values()) {
            if (il.getFullname().equals(fullname)) {
                return il;
            }
        }
        return null;
    }

    public String getFullname() {
        return fullname;
    }
}
