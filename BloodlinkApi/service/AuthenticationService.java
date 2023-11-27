package np.com.bloodlink.BloodlinkApi.service;

import lombok.RequiredArgsConstructor;
import np.com.bloodlink.BloodlinkApi.controller.AuthenticationRequest;
import np.com.bloodlink.BloodlinkApi.controller.AuthentucationResponse;
import np.com.bloodlink.BloodlinkApi.controller.RegisterRequest;
import np.com.bloodlink.BloodlinkApi.entity.Role;
import np.com.bloodlink.BloodlinkApi.entity.User;
import np.com.bloodlink.BloodlinkApi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthentucationResponse register(RegisterRequest request) {
    var user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
    userRepository.save(user);
    var jwtToken= jwtService.generateToken(user);
    return AuthentucationResponse.builder().jwtToken(jwtToken).build();

    }

    public AuthentucationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken= jwtService.generateToken(user);
        return AuthentucationResponse.builder().jwtToken(jwtToken).build();

    }
}
