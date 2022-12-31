package ee.ciszewsj.secureapplication.controllers;

import ee.ciszewsj.secureapplication.data.CreateNoteRequest;
import ee.ciszewsj.secureapplication.data.LoginRequest;
import ee.ciszewsj.secureapplication.data.RegisterRequest;
import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("login_request", new LoginRequest());
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping("/perform_logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateNote(@Valid RegisterRequest registerRequest, Errors errors) {
		if (errors.hasErrors()) {
			return "register";
		}
//		if (userRepository.findByUsername(registerRequest.getUsername()))
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userRepository.save(user);
		return "redirect:/login";
	}
}
