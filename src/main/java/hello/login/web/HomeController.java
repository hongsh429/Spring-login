package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }


    //    @GetMapping("/")
    public String homeLoginV1(
            /* 쿠키가 없는사용자도 들어오므로 false 처리*/
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {
        if (memberId == null) {
            return "home";
        }

        //쿠키가 있는 사용자
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";

    }

//    @GetMapping("/")
    public String homeLoginV2(
            HttpServletRequest request,
            Model model
    ) {
        // request에는 세션에 저장되고 보내준 uuid값을 가지고 있는 cookie가 날라왓을것이다.
        // 세션관리자에 저장된 회원 정보를 조회
        Member loginMember = (Member) sessionManager.getSession(request);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";

    }

    /*HttpServletRequest에서 제공하는 HttpSession 이용하기 */
//    @GetMapping("/")
    public String homeLoginV3(
            HttpServletRequest request,
            Model model
    ) {
        HttpSession session = request.getSession(false);
        /* 세션을 생성하는 것이 목적이 아니라 조회하는 것이 목적일 때는 create=false 를 주어 불필요한 세션 생성을 막자 */

        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회윈 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";

    }

    @GetMapping ("/")
    public String homeLoginV3Spring(
            /*
                  ✨✨✨✨✨✨✨
                  해당 기능은 세션을 생성하지 않으므로, 세션을 찾아올 때만 사용하자
                  ✨✨✨✨✨✨✨
            */
            @SessionAttribute(
                    name= SessionConst.LOGIN_MEMBER,
                    required = false /*세션이 없을 수도 잇으므로.. false ex> 새로 들어온 사람*/
            ) Member loginMember,
            Model model
    ) {

        // 세션에 회윈 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";

    }
}