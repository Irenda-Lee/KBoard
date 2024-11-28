package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping("/") // root 부분 만드는 것. 없으면 localhost:8080 not found 404 에러 뜸
    public String home(Model model) {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public void home(){

    }


    //-------------------------------------------------------------------------
    // 현재 Authentication 보기 (디버깅 등 용도로 활용)
    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 매개변수에 Authentication 을 명시해도 주입된다.  (매개변수 authentication 이 인증 안 되어 있으면 null)
    @RequestMapping("/userDetails")                 // ↓↓↓↓↓↓↓↓
    @ResponseBody
    public PrincipalDetails userDetails(Authentication authentication){
        if(authentication != null){
            return (PrincipalDetails) authentication.getPrincipal();
        }
        return null;
    }

    // @AuthenticationPrincipal 을 사용하여 로그인한 사용자 정보 주입받을수 있다.
    // org.springframework.security.core.annotation.AuthenticationPrincipal
    @RequestMapping("/user")
    @ResponseBody
    public User username(@AuthenticationPrincipal PrincipalDetails userDetails){ // 얘도 인증 안 돼있음 널값 리턴
        return (userDetails != null) ? userDetails.getUser() : null;

    }

    // OAuth2 Client 를 사용하여 로그인 경우.
    // Principal 객체는 OAuth2User 타입으로 받아올수도 있다.
    // AuthenticatedPrincipal(I)
    //  └─ OAuth2AuthenticatedPrincipal(I)
    //       └─ OAuth2User (I)

    @RequestMapping("/oauth2")
    @ResponseBody
    public OAuth2User oauth2(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        return oAuth2User;
    }


    @RequestMapping("/oauth2user")
    @ResponseBody
    public Map<String, Object> oauth2user(@AuthenticationPrincipal OAuth2User oAuth2User){
        return (oAuth2User != null) ? oAuth2User.getAttributes() : null;
    }





}
