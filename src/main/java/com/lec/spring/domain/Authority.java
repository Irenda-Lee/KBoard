package com.lec.spring.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {
    private Long id;
    private String name; // 권한명 ex) "ROLE_MEMBER", "ROLE_ADMIN"을 spring security 에서 접두어로 쓰기 위해 대문자... 예?

}
