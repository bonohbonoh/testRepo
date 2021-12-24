package com.jeonggolee.helpanimal.domain.user.service;

import com.jeonggolee.helpanimal.common.jwt.JwtTokenProvider;
import com.jeonggolee.helpanimal.domain.user.dto.UserInfoReadDto;
import com.jeonggolee.helpanimal.domain.user.dto.UserLoginDto;
import com.jeonggolee.helpanimal.domain.user.dto.UserSignupDto;
import com.jeonggolee.helpanimal.domain.user.entity.User;
import com.jeonggolee.helpanimal.domain.user.exception.login.EmailPasswordNullPointException;
import com.jeonggolee.helpanimal.domain.user.exception.login.WrongPasswordException;
import com.jeonggolee.helpanimal.domain.user.exception.signup.UserDuplicationException;
import com.jeonggolee.helpanimal.domain.user.exception.signup.UserInfoNotFoundException;
import com.jeonggolee.helpanimal.domain.user.query.UserSearchSpecification;
import com.jeonggolee.helpanimal.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserSearchSpecification userSearchSpecification;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider provider;

    public boolean signUpUser(UserSignupDto dto) throws Exception {
        String encodePassword = passwordEncoder.encode(dto.getPassword());
        dto.PasswordEncoding(encodePassword);
        Optional<User> user = userRepository.findOne(userSearchSpecification.searchWithEmailEqual(dto.getEmail()));
        if (user.isPresent()) {
            throw new UserDuplicationException("이미 존재하는 유저입니다.");
        }
        String email = userRepository.save(dto.toEntity()).getEmail();
        if (email != null && !email.equals("")) {
            return true;
        }
        return false;
    }

    public String loginUser(UserLoginDto dto) throws Exception {
        User user = userRepository.findOne(userSearchSpecification.searchWithEmailEqual(dto.getEmail()))
                .orElseThrow(() -> new UserInfoNotFoundException("존재하지 않는 회원입니다."));
        boolean isMatchingPassword = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        boolean isMatchingEmail = user.getEmail().matches(dto.getEmail());
        if (isMatchingPassword && isMatchingEmail) {
            return provider.generateToken(dto.getEmail(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())));
        }
        throw new WrongPasswordException("잘못된 이메일 혹은 패스워드 입니다.");
    }

    public UserInfoReadDto userInfoReadDto() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserInfoReadDto dto = new UserInfoReadDto(userRepository.findOne(userSearchSpecification.searchWithEmailEqual(email))
                .orElseThrow(() -> new UserInfoNotFoundException("존재하지 않는 회원입니다.")));
        return dto;
    }

}

