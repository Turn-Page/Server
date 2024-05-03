package com.example.turnpage.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String home() {
        return "홈 화면입니다. 당신은 로그인한 상태네요? :>";
    }
    @GetMapping("/public")
    public String publicAccess() {
        return "이 페이지는 모든 사용자가 이용할 수 있습니다. 당신은 로그인하지 않았을 것 같네요 :<";
    }

    @GetMapping("/private")
    public String privateAccess() {
        return "이 페이지는 로그인된 사용자만 이용할 수 있습니다. 당신은 로그인한 상태네요? :>";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "이 페이지는 관리자만 이용할 수 있습니다. 당신은 관리자네요? :O";
    }
}
