package com.pettrack.modules.usuario;

import com.pettrack.BaseIntegrationTest;
import com.pettrack.modules.usuario.dto.request.LoginRequest;
import com.pettrack.modules.usuario.dto.response.LoginResponse;
import com.pettrack.modules.usuario.service.AuthService;
import com.pettrack.shared.exception.NegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Auth Service")
class AuthServiceTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Nested
    @DisplayName("Login")
    class Login {

        @Test
        @DisplayName("Deve fazer login com sucesso")
        void deveFazerLoginComSucesso() {
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@pettrack.com");
            request.setSenha("password");

            LoginResponse response = authService.login(request);

            assertThat(response.getToken()).isNotBlank();
            assertThat(response.getEmail()).isEqualTo("admin@pettrack.com");
            assertThat(response.getPerfil()).isNotNull();
        }

        @Test
        @DisplayName("Não deve fazer login com senha incorreta")
        void naoDeveFazerLoginComSenhaIncorreta() {
            LoginRequest request = new LoginRequest();
            request.setEmail("admin@pettrack.com");
            request.setSenha("senha-errada");

            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(NegocioException.class)
                    .hasMessageContaining("inválidos");
        }

        @Test
        @DisplayName("Não deve fazer login com email inexistente")
        void naoDeveFazerLoginComEmailInexistente() {
            LoginRequest request = new LoginRequest();
            request.setEmail("naoexiste@pettrack.com");
            request.setSenha("password");

            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(NegocioException.class);
        }
    }

}