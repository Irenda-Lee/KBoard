package com.lec.spring.repository;

import com.lec.spring.domain.User;

public interface UserRepository {

    // 특정 id (PK) 의 user 리턴
    // SELECT 동작
    User findById(Long id);

    // 특정 username 의 user 리턴
    // SELECT 동작
    User findByUsername(String username);
    // DB 에서 찾아서 리턴
    // 회원가입할 때 아이디 중복 체크 해야 하니까!
    // 검증 동작할 때 필요!

    // 새로운 User 등록
    int save(User user);

    // User 정보 수정
    int update(User user);





}
