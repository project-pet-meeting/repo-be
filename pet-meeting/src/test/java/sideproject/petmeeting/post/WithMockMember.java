//package sideproject.petmeeting.post;
//
//import org.springframework.security.test.context.support.WithSecurityContext;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
///**
// * 커스텀 어노테이션 @WithMockMember: @WithSecurityContext 을 이용해서 SecurityContext 를 만들 수 있음
// */
//@Retention(RetentionPolicy.RUNTIME)
//@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
//public @interface WithMockMember {
//
//    String username() default "sky@gmail.com";
//
//    String password() default "password";
//
//}
