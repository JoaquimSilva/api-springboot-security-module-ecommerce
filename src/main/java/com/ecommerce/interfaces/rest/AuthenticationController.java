package com.ecommerce.interfaces.rest;

import com.ecommerce.domain.model.user.UserRequest;
import com.ecommerce.domain.model.user.User;
import com.ecommerce.infrastruture.security.TokenData;
import com.ecommerce.infrastruture.security.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/login")
public class AuthenticationController {
    private AuthenticationManager manager;
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity makeLogin(@RequestBody @Valid UserRequest userData) {
        var token = new UsernamePasswordAuthenticationToken(userData.userName(), userData.password());
        var authentication = manager.authenticate(token);
        var response = tokenService.tokenGenerate((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenData(response));
    }
}
