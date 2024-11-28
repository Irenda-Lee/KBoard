package com.lec.spring.config;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.repository.AuthorityRepository;
import com.lec.spring.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//시큐리티가 /user/login (POST) 주소요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인(인증) 진행이 완료되면 '시큐리티 session' 에 넣어주게 된다.
//우리가 익히 알고 있는 같은 session 공간이긴 한데..
//시큐리티가 자신이 사용하기 위한 공간을 가집니다.
//=> Security ContextHolder 라는 키값에다가 session 정보를 저장합니다.
//여기에 들어갈수 있는 객체는 Authentication 객체이어야 한다.
//Authentication 안에 User 정보가 있어야 됨.
// ★★★★★ User 정보 객체는 ==> UserDetails 타입 객체 ★★★★★ 이어야 한다.

//따라서 로그인한 User 정보를 꺼내려면
//Security Session 에서
//   => Authentication 객체를 꺼내고, 그 안에서
//        => UserDetails 정보를 꺼내면 된다.

public class PrincipalDetails implements UserDetails, OAuth2User {

    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    private AuthorityRepository authorityRepository;

    private UserRepository userRepository;


    // 로그인한 사용자 정보
    private User user;

    public User getUser() {
        return user;
    }
// 일반 로그인 용 생성자
    // user 프로퍼티가 있단 뜻
    public PrincipalDetails(User user) {
        System.out.println("UserDetails(user) 생성 : " + user);
        this.user = user;
    }

// OAuth 로그인 용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes){
        System.out.println("""
           UserDetails(user, oauth attributes) 생성:
               user: %s
               attributes: %s
           """.formatted(user, attributes));
        this.user = user;
        this.attributes = attributes;



    }


    // 해당 User 의 '권한(들)'을 리턴
    // 현재 로그인한 사용자의 권한정보가 필요할 때마다 호출된다.
    // 혹은 필요할 때마다 직접 호출해 사용할 수도 있다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("getAuthorities() 호출");
        Collection<GrantedAuthority> collect = new ArrayList<>();

        List<Authority> list = authorityRepository.findByUser(user);

        for(Authority auth : list){
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth.getName();
                }

                // thymeleaf 등에서 활용할 문자열 (학습 목적)
                @Override
                public String toString() {
                    return auth.getName();
                }
            });
        }

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되진 않았는지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠긴 건 아닌지? 활동정지 가튼거? 오픈베타 서비스..?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 credential 이 만료된건 아닌지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었는지? 보통 페이지 켜논 채 휴면상태인지 확인..
    @Override
    public boolean isEnabled() {
        return true;
    }




    //-------------------------------------------------------------
    // OAuth2User 에서 구현할 메소드들  (구글 계정 정보 한 곳에 담아 가져올 놈 정의)

    private Map<String, Object> attributes; // OAuth2User 의 getAttributes() 값 넣을 거임

    @Override
    public String getName() {
        return null; // 예제에선 사용 안 할 거래여
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
