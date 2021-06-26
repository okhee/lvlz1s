package kr.co.okheeokey.user.domain;

import lombok.Getter;

@Getter
public enum UserRole implements Role {
    ADMIN {
        @Override
        public Boolean isAdmin() {
            return true;
        }

        @Override
        public String getRoleName() {
            return "관리자";
        }
    },
    USER {
        @Override
        public Boolean isAdmin() {
            return false;
        }

        @Override
        public String getRoleName() {
            return "일반 사용자";
        }
    }

}
