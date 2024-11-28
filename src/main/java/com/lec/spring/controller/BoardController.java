package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        System.out.println("BoardController() 생성되었다");
        this.boardService = boardService;
    }


    @GetMapping("/write")
    public void write(){
    }


    @PostMapping("/write") // ↓ 매개변수로 받은 커맨드(현 post)는 모델로 옮겨 담아준다
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files   // 첨부파일들 <name, file>
            , @Valid Post post
            , BindingResult bindingResult  // <- Validator 가 유효성 검사를 마친 결과가 담긴 객체
            , Model model // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다!!!! 안그럼 바인딩 동작을 안 함;;
            , RedirectAttributes redirectAttributes // redirect 시 넘겨줄 값들
    ){
        // ↓ validation 에러가 있었다면 redirect 할 거다!
        if(bindingResult.hasErrors()){
            // addAttribute(name, value)
            //    request parameters 로 값을 전달.  redirect URL 에 query string 으로 값이 담김
            //    request.getParameter 에서 해당 값에 액세스 가능
            // addFlashAttribute(name, value)
            //    일회성. 한번 사용하면 Redirect 후 값이 소멸
            //    request parameters 로 값을 전달하지않음
            //    '객체'로 값을 그대로 전달

            // ↓↓↓ redirect 시, 기존에 입력했던 값들은 보이도록 전달 해줌.
            // 전달한 attribute name 값들은 => 템플릿에서 사용이 가능한 변수!
            redirectAttributes.addFlashAttribute("subject", post.getSubject()); // 기존에 입력했던 값들
            redirectAttributes.addFlashAttribute("content", post.getContent()); // 기존에 입력했던 값들

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error : errorList){
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getCode());
            }

            return "redirect:/board/write"; // GET 으로 요청 감
        }

        // ↓  validation 에러가 없었따면 정상적으로 response
       model.addAttribute("result", boardService.write(post, files));
        // ↑ ~~서비스단 메소드~~
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
//        Post detail = boardService.detail(id);
        model.addAttribute("post", boardService.detail(id));
        return "board/detail";
    }

    @GetMapping("/list")
//    public void list(Model model){
    public void list(Integer page, Model model){
//        model.addAttribute("list", boardService.list());
        boardService.list(page, model);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
       model.addAttribute("post", boardService.selectById(id));
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files // 새로 추가될 첨부파일들
            , Long[] delfile // 삭제될 파일들의 id 를 담고있음
            , @Valid Post post
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            // ↓ 기존 입력값 유지하도록 해줌
            redirectAttributes.addFlashAttribute("subject", post.getSubject());// 기존에 입력했던 값들
            redirectAttributes.addFlashAttribute("content", post.getContent());// 기존에 입력했던 값들

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error : errorList){
                redirectAttributes.addFlashAttribute("error_" + error.getField(), error.getCode());
            }

            return "redirect:/board/update/" + post.getId();
        }

        model.addAttribute("result", boardService.update(post, files, delfile)); // <- post(id, subject, content)
        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        model.addAttribute("result", boardService.deleteById(id));
        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        System.out.println("initBinder 호출");
        binder.setValidator(new PostValidator());
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/board/list?page=" + page;
    }

}
