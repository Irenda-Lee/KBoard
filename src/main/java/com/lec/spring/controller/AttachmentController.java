package com.lec.spring.controller;

import com.lec.spring.domain.Attachment;
import com.lec.spring.service.AttachmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


// @RestController <= @Controller + @ResponseBody

@RestController // 파일 다운로드, 데이터 response
public class AttachmentController {

    @Value("${app.upload.path}")  // org.springframework.beans.factory.annotation.Value
    private String uploadDir;

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
        System.out.println(getClass().getName() + "() 생성");
    }
    // 파일 다운로드
    // id: 첨부파일의 id
    // ResponseEntity<T> 를 사용하여
    // '직접' Response data 를 구성


    @RequestMapping("/board/download")
    public ResponseEntity<?> download(Long id) {
        if (id == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Attachment file = attachmentService.findById(id);
        if (file == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        String sourceName = file.getSourcename();
        String fileName = file.getFilename();

        String path = new File(uploadDir, fileName).getAbsolutePath(); // 저장될 파일의 절대경로 지정
        try {
            // 파일 유형 (MIMEtype) 추출
            String mimeType = Files.probeContentType(Paths.get(path));
            // ex) "image.png"...


            // 유형이 지정되지 않은 경우 처리
            if(mimeType == null) {
                mimeType = "application/octet-stream";
            } // 일련의 8bit 스트림 타입.  유형이 알려지지 않은 파일에 대한 형식 지정

            // response body 준비
            Path filePath = Paths.get(path);
            Resource resource
                    // Resource <- InputStream <- 저장된 파일
                    = new InputStreamResource(Files.newInputStream(filePath));

            // response header 세팅
            HttpHeaders headers = new HttpHeaders();
            // ↓ 원본 파일 이름(sourceName) 으로 다운로드 하게 하기위한 세팅
            //   반.드.시 URL 인코딩해야 함. 한글 절대 불가
            // ex) Content-Disposition: filename ="filename.jpg"
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(URLEncoder.encode(sourceName, "utf-8"))
                            .build());
            headers.setCacheControl("no-cache");
            headers.setContentType(MediaType.parseMediaType(mimeType)); // 파일유형 지정. 브라우저가 이걸 보고 판단해서 열어줌

            // ResponseEntity<> 리턴
            return new ResponseEntity<>(resource, headers, HttpStatus.OK); // 200


        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // status=500 error
        }


    }
}

