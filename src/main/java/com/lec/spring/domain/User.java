package com.lec.spring.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String username; // 회원 아이디
    @JsonIgnore
    private String password;
    @ToString.Exclude
    @JsonIgnore
    private String re_password; // PW 확인 입력. 검증체크 때 이 두개가 같은지 확인하기
    private String name; // 회원 이름
    @JsonIgnore
    private String email;
    @JsonIgnore
    private LocalDateTime regDate;


    // User : Authority = N:N 다대다 관계
    // 특정 유저의 권한들(이 뭐가 있는지 궁금한 상태)
//    @ToString.Exclude // toString() 결과에서는 제외 하는 것.
//    @JsonIgnore
//    private List<Authority> authorities = new ArrayList<>();


    // OAuth2 Client
    private String provider;
    private String providerId;


}
