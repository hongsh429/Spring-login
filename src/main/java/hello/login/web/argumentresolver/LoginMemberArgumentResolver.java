package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        /* @Login 어노테이션이 붙어 잇는지 여부 체크 */
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class);

        /* 들어온 파라미터의 타입이 Member 클래스 타입인지 여부 체크 */
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        /* 위의 두개의 정보가 true이면 true를 반환 -> 아래 resolveArgument가 실행됨 */
        return hasParameterAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        /*
         위의 함수가 true면 아래가 해당 메소드가 실행된다.
                   이 메소드의 기능 :  resolveArgument() 를 호출해서 실제 객체를 생성
        */
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return member;

    }
}
