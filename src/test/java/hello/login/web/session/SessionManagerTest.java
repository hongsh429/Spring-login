package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        // 세션 생성(저장소 저장 -> 쿠키에 키값을 벨류로 전달)
        MockHttpServletResponse response = new MockHttpServletResponse(); // 가짜 response
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장()
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);
    
        
        //세션만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        Assertions.assertThat(expired).isEqualTo(null);
        Assertions.assertThat(expired).isNull();
    }
}
