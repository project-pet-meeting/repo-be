//package sideproject.petmeeting.post;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import sideproject.petmeeting.member.domain.Member;
//import sideproject.petmeeting.member.domain.UserRole;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class CustomMember implements UserDetails {
//
//    private CustomMember customMember;
//
//    public CustomMember(Object member) {
//    }
//
//    public CustomMember(String username, String password) {
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserRole.ROLE_MEMBER.toString());
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(authority);
//        return authorities;
//    }
//
//    @Override
//    public String getUsername() {
//        return customMember.getUsername();
//    }
//
//    @Override
//    public String getPassword() {
//        return customMember.getPassword();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
