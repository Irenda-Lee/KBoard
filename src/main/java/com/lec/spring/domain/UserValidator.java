package com.lec.spring.domain;


import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {
// 가입 시 이미 있는 회원(특정 username 회원아이디)인지 확인해야함
    // -> DB 에 Valid.가 접근해야함 -> 밸리데이터 자체도 빈이어야함
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
        // ↓ 검증할 객체의 클래스 타입(유저 객체)인지 확인
        boolean result = User.class.isAssignableFrom(clazz);
        System.out.println(result);
        return result;

    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String username = user.getUsername();
        if(username == null || username.trim().equals("")) {
            errors.rejectValue("username", "username 은 필수입니다.");
        } else if (userService.isExist(username.toUpperCase())) {
            // 이미 등록된 중복된 아이디(username) 으로 회원가입 하려고 하면
            errors.rejectValue("username", "이미 존재하는 아이디(username) 입니다.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password 는 필수입니다.");

        // email
        // 정규표현식 체크


        // 입력 비번, 재확인비번이 동일한지 비교
        if(!user.getPassword().equals(user.getRe_password())){
            errors.rejectValue("re_password", "비밀번호와 비밀번화 확인 입력값은 같아야 합니다.");
        }



    }
}
