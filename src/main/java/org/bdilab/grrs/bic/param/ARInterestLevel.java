package org.bdilab.grrs.bic.param;

/**
 * @author caytng@163.com
 * @date 2019/4/11
 */
public enum ARInterestLevel {
    LG("LG(K-3)"), MG1("MG(4-8)"), MG2("MG+(6 AND UP)"), UG("UG(9-12)");

    private String fullname;
    ARInterestLevel(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return fullname;
    }
}
