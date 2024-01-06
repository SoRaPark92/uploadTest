package com.jinhwan.sampletest.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jinhwan.sampletest.vo.SignupUserVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails{
	//아이디
	@Id  
	@Column(name = "userId" , updatable = false, unique = true, nullable = false)
	private String userId;
	
    //비밀번호 (저장시 암호화 하여 적용)
    @Column(name = "password" , nullable = false)
    private String password;

    //이름
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    //주민등록번호
    @Column(name = "reg_no", nullable = false)
    private String regNo;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    
    @Builder
    public Member (SignupUserVo signupUserVo) {
    	this.userId = signupUserVo.getUserId(); 
    	this.password = signupUserVo.getPassword(); 
    	this.name = signupUserVo.getName(); 
    	this.regNo = signupUserVo.getRegNo(); 
    	this.roles.add("USER");
    }

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}


	@Override
	public boolean isAccountNonLocked() {
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
