package hello.login.domain.config.auth;

import hello.login.domain.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.
@Getter
public class PrincipalDetail implements UserDetails {

    private User user;

    public PrincipalDetail(User user){
        this.user = user;
    }

    public PrincipalDetail() {

    }

    public String getName() {
        return user.getName(); // 이름 반환 메서드 추가
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {  // 계정이 만료되지 않았는지 리턴한다 (true: 만료안됨)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {   // // 계정이 잠겨있지 않았는지 리턴한다 (true: 잠기지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 비밀번호가 만료되지 않았는지 리턴한다 (true: 만료안됨)
        return true;
    }

    @Override
    public boolean isEnabled() {    // 계정이 활성화인지 리턴한다 (true: 활성화)
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(()->{return "ROLE_"+user.getRole();});

        return collection;
    }
}
