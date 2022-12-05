//package sideproject.petmeeting.post;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithSecurityContextFactory;
//import sideproject.petmeeting.security.UserDetailsImpl;
//
//public class WithMockMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
//
//    @Override
//    public SecurityContext createSecurityContext(WithMockMember withMockMember) {
//
//        final SecurityContext context = SecurityContextHolder.createEmptyContext();
//
//        UserDetailsImpl principal = new UserDetailsImpl(withMockMember.username(), withMockMember.password());
//        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
//        context.setAuthentication(auth);
//
//        return context;
//    }
//
//
//
//}
