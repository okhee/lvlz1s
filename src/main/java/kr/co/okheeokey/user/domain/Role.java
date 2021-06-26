package kr.co.okheeokey.user.domain;

public interface Role {
    default Boolean isAdmin() {
        return false;
    }

    String getRoleName();

}
